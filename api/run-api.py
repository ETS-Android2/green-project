from flask import Flask, json, render_template, request, jsonify
from flask_cors import CORS
from flask import send_file
from sys import path
from werkzeug.utils import secure_filename
import math


import os
path.append("./mysql")
from mysql_connection import mysqlConnection


app = Flask(__name__)
app.config['DEBUG']=True
CORS(app)

@app.route('/')
def home():
  return render_template('index.html')

## Getting the fruit readings 
@app.route("/get-readings", methods=['GET'])
def getReadings():

  params = request.args
  conn = mysqlConnection()

  if request.method != 'GET':
    return jsonify(responseJsonHandler("The request method is wrong, we expect the GET method. "))


  if 'productionLine' in params:
    r = conn.consult("select * from VW_fruit_reading_seconds where productionLineCode='%s';" % params['productionLine'])

    return  jsonify(fruitReadingStructure(r[0]) if r != () else [])


  

  data = conn.consult("select * from VW_productionLines;") if 'search' not in params else conn.consult("select * from VW_productionLines where code='%s';" % (params['search']))
  
  fruitReadingData = []

  for row in data:
    fr = { 'productionLine' : ProductionLineStructure(row)}
    fr['readings'] = []
    
    for r in conn.consult("select * from  VW_readings_last_hour where code = '%s' ;" % (row['code'])) if 'fruit' not in params else conn.consult("select * from  VW_readings_last_hour where code = '%s' and fruitCode = '%s';" % (row['code'], params['fruit'])):
      reading = fruitReadingStructure(r)

      reading['fruit'] = FruitStructure(conn.consult("select * from VW_fruits where code = '%s';" % r['fruitCode'])[0])

      fr['readings'].append(reading)
    
    if fr['readings'] != []: fruitReadingData.append(fr)

    
  
  conn.closeConnection()

  return jsonify(fruitReadingData)

## Getting the fruits
@app.route("/get-fruits", methods=['GET'])
def getFruits():
  if request.method != 'GET':
    return jsonify(responseJsonHandler("The request method is wrong, GET method expected. "))

  params = request.args
  conn = mysqlConnection()


  data = conn.consult("select * from VW_fruits;")
  fruits = []

  for row in data:
    
    str = FruitStructure(row)
    str['requirements'] = {
      'R': row['R'],
      'G': row['G'],
      'B': row['B']
    }

    fruits.append(str)

  conn.closeConnection()

  return jsonify(fruits)

## Getting the production lines
@app.route("/get-productionLines", methods=['GET'])
def getProductionLines():
  if request.method != 'GET':
    return jsonify(responseJsonHandler("The request method is wrong, GET method expected. "))

  params = request.args
  conn = mysqlConnection()

  data = conn.consult("select * from VW_productionLines;")
  productionLines = []

  for row in data :

    productionLines.append(ProductionLineStructure(row))

  conn.closeConnection()

  return jsonify(productionLines)

## Getting the area readings 
@app.route("/get-enviromentVariables", methods=['GET'])
def getAreaReadings():

  if request.method != 'GET':
    return jsonify(responseJsonHandler("The request method is wrong, GET method expected. "))

  params = request.args
  conn = mysqlConnection()

  if 'productionLine' in params:
    res = conn.consult("select * from VW_enviroment_variable_last_second where code = '%s' order by id desc limit 1;" % params['productionLine'])[0]

    return jsonify(
      responseJsonHandler("There are not area readings availables for: %s" % params['productionLine'])
      if res == () else {'temperature':res['temperature'], 'humidity':res['humidity']}
    )

  data = conn.consult("select * from VW_productionLines;")
  
  productionLineData = []

  for row in data:

    pl = { 'productionLine' : ProductionLineStructure(row)}

    pl['values'] = []

    for r in conn.consult("select * from  VW_enviroment_variable where code = '%s';" % row['code']):
      pl['values'].append(
        { 
          'temperature': r['temperature'],
          'humidity': r['humidity']
        }
      )

    productionLineData.append(pl)

  conn.closeConnection()

  return jsonify(productionLineData)

## Setting a fruit to scan into a production line
@app.route("/setFruit", methods=['POST'])
def setFruit():

  if request.method != 'POST':
    return jsonify(responseJsonHandler("The presented method request was wrong. Expected POST. "))
    
  params = request.form
  conn = mysqlConnection()

  if 'fruit' and 'productionLine' in params:
      
    ## NOTES: pls = productionLines

    fruits = conn.consult("select * from VW_fruits where code = '%s';" % params['fruit'])
    pls = conn.consult("select * from VW_productionLines where code = '%s';" % params['productionLine'])

    if fruits != () and pls != ():
      conn.insert(
        "call SP_fruit_productionLine_relation('%s','%s');" 
        % (params['productionLine'], params['fruit'] )
      )

      conn.closeConnection()

      return jsonify(responseJsonHandler("Success!", True))

    else :
      return jsonify(responseJsonHandler("The required params 'fruit' and 'productionLines' are wrong. "))

  else :
    return jsonify(responseJsonHandler("The required params 'fruit' and 'productionLines' are not included in the request. "))


## Insert a new fruit
@app.route("/insertFruit", methods=['POST'])
def insertFruit():

  params = request.form

  conn = mysqlConnection()

  if request.method != 'POST':
    return jsonify(responseJsonHandler("The presented method request was wrong. Expected POST "))
    
  if 'name' and 'code' and 'description' in params and 'image' in request.files:
    
    files = request.files.getlist('image')

    filename = "noImage.png"

    for file in files:

      try:

        filename = secure_filename(file.filename)
        file.save(os.getcwd() + "/img/" + filename)

      except FileNotFoundError as e:

        print(" ** LOG ERROR: No Image was presented: ", e)

        
    conn.insert("call SP_insert_fruit('%s', '%s', '%s','%s');" 
    % ( params['code'],params['name'], params['description'], filename))

    conn.closeConnection()

    return jsonify(responseJsonHandler("Success!", status=True))

  else : 
    return jsonify(responseJsonHandler("The given params are wrong. "))

## get the inspection Results of today
@app.route("/get-inspectionResults", methods=['GET'])
def getInspectionResults():

  if request.method != 'GET':
    return jsonify(responseJsonHandler("The request method is wrong, GET method expected. "))



  conn = mysqlConnection()
  params = request.args
  
  if 'period' in params: 

    data = []

    if params['period'] == 'day' :
      data = conn.consult("select * from VW_fruits_results_group_by_hour_during_one_day;")

      
      data = {
        'dates' : [ d['time'] for d in data ],
        
        'counts' : [int( d['fruit_count']) for d in data]
      }

      return jsonify(data)

    elif params['period'] == 'week':
      

      for ir in  conn.consult("select * from VW_fruits_results_week;"):

        row = InspectionResultStructure(ir)
        row['productionLine'] = ProductionLineStructure(ir)
        row['fruit'] = "undefined"

        data.append(row)

      return jsonify(data)

    return jsonify(responseJsonHandler("The given period is wrong: %s. " % params['period']))
    

  conn.consult("select * from VW_fruits_result_today;")

  data = []

  for ir in conn.consult("select * from VW_fruits_result_today;") :

    row = InspectionResultStructure(ir)
    row['productionLine'] = ProductionLineStructure(ir)
    row['fruit'] = FruitStructure(conn.consult("select * from VW_fruits where code = '%s';" % ir['fruitCode'])[0])

    data.append(row)

  conn.closeConnection()
  return jsonify(data)

## Returning all the relations from the production lines and fruits
@app.route("/get-relations", methods= ['GET'])
def getRelations():

  if request.method != 'GET':
    return jsonify(responseJsonHandler("The request method is wrong, GET method expected. "))


  conn = mysqlConnection()
  params = request.args

  if 'productionLine' in params: 
    res = conn.consult("select * from VW_fruit_productionLine_relation where productionLineCode = '%s';" % params['productionLine'])[0]

    return jsonify(
      responseJsonHandler("This production line does not exists or does not have a relation asigned yet: %s ." % params['productionLine'])
      if res == () else {
        "fruit": FruitStructure(conn.consult("select * from VW_fruits where code = '%s';" % res['fruitCode'])[0]) ,
        "productionLine": ProductionLineStructure(conn.consult("select * from VW_productionLines where code ='%s';" % res['productionLineCode'])[0]) 
      }
    )

  data = []

  for rel in conn.consult("select * from VW_fruit_productionLine_relation;"):
    data.append({
      "fruit": FruitStructure(conn.consult("select * from VW_fruits where code = '%s';" % rel['fruitCode'])[0]) ,
      "productionLine": ProductionLineStructure(conn.consult("select * from VW_productionLines where code ='%s';" % rel['productionLineCode'])[0]) 
    })

  return jsonify(data)
  

  

## Insert a fruit requirements, just the colors.
@app.route("/insertFruitRequirements", methods = ['POST'])
def insertFruitRequirements():

  conn = mysqlConnection()

  params = request.json

  if request.method != 'POST':
    return jsonify(responseJsonHandler("The presented method request was wrong. Expected POST "))

  if 'fruit' and 'color' in params: 

    if conn.consult("select * from VW_fruits where code = '%s';" % params['fruit']) == (): 
      return jsonify(responseJsonHandler("The fruit asigned does not exists, please verify the code used: %s " % params['fruit']))

    if  isinstance(params['color'], ()):
      return jsonify(responseJsonHandler("The presented param color is not an array, please check your message."))

    
    colorsArray = params['color']

    r_des = 0
    g_des = 0
    b_des = 0

    r_med = 0
    g_med = 0
    b_med = 0

    for color in colorsArray:
      if 'R' and 'G' and 'B' in color:
        r_med += color['R'] 
        g_med += color['G'] 
        b_med += color['B']
      else :
        return jsonify(responseJsonHandler("The required params are not included in the color value; 'R', 'G', 'B"))

    r_med /= len(colorsArray)
    g_med /= len(colorsArray)
    b_med /= len(colorsArray)

    ## cool stuff!

    (r_des, g_des, b_des) = [math.pow((c['R'] - r_med), 2)  for c in colorsArray], [math.pow((c['G'] - g_med), 2)  for c in colorsArray], [math.pow((c['B'] - b_med), 2)  for c in colorsArray]

    r_des = sum(r_des) / len(colorsArray)
    g_des = sum(g_des) / len(colorsArray)
    b_des = sum(b_des) / len(colorsArray)

    r_medi = [c['R'] for c in colorsArray]
    g_medi = [c['G'] for c in colorsArray]
    b_medi = [c['B'] for c in colorsArray]

    r_medi.sort()
    g_medi.sort()
    b_medi.sort()

    print(r_medi)
    print(g_medi)
    print(b_medi)

    middle = len(colorsArray)


    r_medi = r_medi[int(middle/2-1)]  if middle%2 > 1 else (r_medi[int(middle/2-1)] + r_medi[int(middle/2)])/2
    g_medi = g_medi[int(middle/2-1)]  if middle%2 > 1 else (g_medi[int(middle/2-1)] + g_medi[int(middle/2)])/2
    b_medi = b_medi[int(middle/2-1)]  if middle%2 > 1 else (b_medi[int(middle/2-1)] + b_medi[int(middle/2)])/2

    conn.insert("call SP_insert_fruitRequirements('%s',%d,%d,%d,%d,%d,%d);" % (params['fruit'], r_med, g_med, b_med, math.sqrt(r_des) , math.sqrt(g_des), math.sqrt(b_des) ))

    # return jsonify({ 
    #   "desviacion" : {"R": r_des, "G" : g_des, "B" : b_des} , 
    #   "media" : { "R": r_med, "G" : g_med, "B" : b_med},
    #   "mediana" : { "R": r_medi, "G" : g_medi, "B" : b_medi},
    #   "varianza" : {"R": math.sqrt(r_des) , "G" : math.sqrt(g_des), "B" : math.sqrt(b_des)}
          
    #   }
    # )
    conn.closeConnection()
    return jsonify(responseJsonHandler("Success!", status=True))

  else :
    return jsonify(responseJsonHandler("The required params are not included in the request. 'fruit', 'color'"))


@app.route("/get-all", methods = ['GET'])
def getAll():
  print("Get all works!")

  conn = mysqlConnection()
  params = request.args

  data = { 'productionLines' : []}
  
  
  for p in conn.consult("select * from VW_productionLines;"):

    p = ProductionLineStructure(p)

    currentFruit = conn.consult("select * from VW_fruit_productionLine_relation where productionLineCode = '%s';" % p['code'])[0]['fruitCode']

    currentFruit = FruitStructure(conn.consult("select * from VW_fruits where code = '%s';" % currentFruit)[0])

    p['currentFruit'] = currentFruit
    print(p["code"])
    areaReadings = conn.consult("select * from VW_enviroment_variable_last_second where code = '%s' order by id desc limit 1;" % (p['code']))[0]

    areaReadings = [
      {
        'id': areaReadings['id'],
        'current' : areaReadings['temperature'],
        'type': {
          "id": "TMP",
					"name": "Temperature",
					"icon": "temperature",
					"unitOfMeasurement": {
						"symbol": "°C",
						"name": "Celsius"
					}
        },
        "ranges": [
          {
            "name": "Low",
            "values": {
              "minimum": 0,
              "maximum": 10
            },
            "color": "#03A9F4"
          },
          {
            "name": "Normal",
            "values": {
              "minimum": 10,
              "maximum": 22
            },
            "color": "#77E03A"
          },
          {
            "name": "High",
            "values": {
              "minimum": 22,
              "maximum": 50
            },
            "color": "#F8002F"
          }
        ]
      },
      {
        'id': areaReadings['id'],
        'current' : areaReadings['humidity'],
        "type": {
          "name": "Humidity",
          "icon": "humidity",
          "unitOfMeasurement": {
            "symbol": "%",
            "name": "Percentage"
          }
        },
        "ranges": [
          {
            "name": "Low",
            "values": {
              "minimum": 0,
              "maximum": 75
            },
            "color": "#FDD835"
          },
          {
            "name": "Normal",
            "values": {
              "minimum": 75,
              "maximum": 90
            },
            "color": "#77E03A"
          },
          {
            "name": "High",
            "values": {
              "minimum": 90,
              "maximum": 100
            },
            "color": "#F88F55"
          }
        ],
      }
    ]

    p['areaReadings'] = areaReadings

    fruitReadings = []
    
    
    readings = conn.consult("select * from VW_readings_last_hour where code = '%s';" % ( p['code']))


    if readings != ():
      for r in readings:

        r = {
          'date' : r['date'],
          'fruit' : FruitStructure(conn.consult("select * from VW_fruits where code = '%s';" % r['fruitCode'])[0]),
          'results' : [
            {
              "type": {
								"id": "WGT",
								"name": "Weight",
								"icon": "weight.png",
								"unitOfMeasurement": {
									"symbol": "g",
									"name": "Grams"
								}
							},
							"current": r['weight']
            },
            {
              "type": {
								"id": "RRR",
								"name": "Red",
								"icon": "Red.png",
								"unitOfMeasurement": {
									"symbol": "R",
									"name": "Red color"
								}
							},
              'current' : r['R']
            },
            {
              "type": {
								"id": "GGG",
								"name": "Green",
								"icon": "Green.png",
								"unitOfMeasurement": {
									"symbol": "G",
									"name": "Green color"
								}
							},
							"current": r['G']
            },
            {
              "type": {
								"id": "BBB",
								"name": "Blue",
								"icon": "Blue.png",
								"unitOfMeasurement": {
									"symbol": "B",
									"name": "Blue color"
								}
							},
							"current": r['B']
            }
          ]
        }

        fruitReadings.append(r)

    p['fruitReadings'] = fruitReadings

    inspectionResults = []

    for ir in conn.consult("select * from VW_fruits_result_today where code = '%s';" % p['code']):
      ir = {
        'date': ir['date'],
        'fruit' : FruitStructure(conn.consult("select * from VW_fruits where code = '%s';" % ir['fruitCode'])[0]) ,
        'results' : {
          'accepted'  : ir['accepted'],
          'rejected' : ir['rejected']
        }
      }

      inspectionResults.append(ir)

    p['inspectionResults'] = inspectionResults

    
    data["productionLines"].append(p)
  
  conn.closeConnection()

  return jsonify(data)

## Searching an image 
@app.route("/image/<string:image>",)
def getImage(image):
  filename = 'img/'+image

  if os.path.isfile(filename) :
    return send_file(filename)
  else :

    ## Send a proper response!!
    return "No image was found"

## Structure Functions

#Function to get the ProductionLine Structure
def ProductionLineStructure(row):

  res = True if row['status'] == 'Online' else False

  return {
    'code':row['code'],
    'ip': row['ip'],
    'description':row['description'],

    'status':{
      'lastConnection': row['lastConnection'],
      'value': row['status'], 
      'res': res
    }
  }

#Function to get the Fruit structure 
def FruitStructure(row):

  filename = False

  if os.path.isfile('img/'+row['url']) :
    filename = row['url']
  
  return {
    'code': row['code'],
    'name': row['name'], 
    'description': row['description'],
    'image' : filename,
    'requirements' : {
      'R': row['R'],
      'G': row['G'],
      'B': row['B']
    }
  }


## Fruit readings structure
def fruitReadingStructure(row):
  return {
    'date':row['date'],      

    'weight': {
      'value': row['weight'],
    },

    'color': {
      'R': row['R'],
      'G': row['G'],
      'B': row['B'],
    }
  }

def InspectionResultStructure(row):
  return {
    'date': row['date'],
    'results' : {
      'accepted'  : int(row['accepted']),
      'rejected' : int(row['rejected'])
    }
  }

def responseJsonHandler(res, status = False): return {'res' : res, 'status': status}


app.run(host="0.0.0.0", port=5000)