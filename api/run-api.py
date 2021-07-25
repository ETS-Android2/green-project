from flask import Flask, json, render_template, request, jsonify
from flask_cors import CORS
from flask import send_file
from sys import path
from werkzeug.utils import secure_filename


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
    r = conn.consult("select * from VW_fruit_readings where productionLineCode='%s';" % params['productionLine'])

    return  jsonify(fruitReadingStructure(r[0]) if r != () else [])


  data = conn.consult("select * from VW_productionLines;")
  fruitReadingData = []

  for row in data:
    fr = { 'productionLine' : ProductionLineStructure(row)}
    fr['readings'] = []
    
    for r in conn.consult("select * from  VW_readings_last_hour where code = '%s';" % row['code']):
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

    fruits.append(FruitStructure(row))

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


# if action == 'get_fruits_result':
#   return jsonify(connection.consult("select * from VW_fruits_result_today;"))

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
    'image' : filename
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

def responseJsonHandler(res, status = False): return {'res' : res, 'status': status}


app.run(host="0.0.0.0", port=5000)