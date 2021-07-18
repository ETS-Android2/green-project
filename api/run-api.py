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
  return "This will be an HTML with the api's body..."


@app.route('/api', methods=['GET'])
def actions():
  params = request.args
  connection = mysqlConnection()

  if 'action' in params:

    action = params['action']

    if action == 'get_readings':

      data = connection.consult("select * from  VW_readings_last_hour;")
      fruitReadingData = []

      for row in data:
        fr = ProductionLineStructure(row)
        fr['reading'] = {

          'date':row['date'],
          'fruit': row['fruit'],

          'weight': {
            'value': row['weight'],
          },

          'color': {
            'R': row['R'],
            'G': row['G'],
            'B': row['B'],
          }
        }

        fruitReadingData.append(fr)
      return jsonify(fruitReadingData)

    if action == 'get_fruits':

      data = connection.consult("select * from VW_fruits;")
      fruits = []

      for row in data:

        fruits.append(FruitStructure(row))

      return jsonify(fruits)

    if action == 'get_fruits_result':
      return jsonify(connection.consult("select * from VW_fruits_result_today;"))

    if action == 'get_productionLines':

      data = connection.consult("select * from VW_productionLines;")
      productionLines = []

      for row in data :

        productionLines.append(ProductionLineStructure(row))

      return jsonify(productionLines)

    if action == 'get_enviromentVariables':

      data = connection.consult("select * from  VW_enviroment_variable;")
      productionLineData = []

      for row in data:

        pl = ProductionLineStructure(row)

        pl['values'] = {
          'temperature': row['temperature'],
          'humidity': row['humidity']
        }

        productionLineData.append(pl)

      return jsonify(productionLineData)

    if action == 'set_fruit':
      if 'fruit' and 'productionLine' in params:
        
        ## NOTES: pls = productionLines

        fruits = connection.consult("select * from VW_fruits where code = '%s';" % params['fruit'])
        pls = connection.consult("select * from VW_productionLines where code = '%s';" % params['productionLine'])

        if fruits != () and pls != ():
          connection.insert(
            "call SP_fruit_productionLine_relation('%s','%s');" 
            % (params['productionLine'], params['fruit'] )
          )

          return jsonify(responseJsonHandler("Success!", True))

        else :
          return jsonify(responseJsonHandler("The required params 'fruit' and 'productionLines' are wrong. "))

      else :
        return jsonify(responseJsonHandler("The required params 'fruit' and 'productionLines' are not included in the request. "))

    else:
      return jsonify(responseJsonHandler("The param 'action' has a wrong value"))

  else :
    return jsonify(responseJsonHandler("The required param 'action' was not specified"))


@app.route("/insertFruit", methods=['POST'])
def insertFruit():

  params = request.form

  conn = mysqlConnection()


  if request.method == 'POST':

    if 'name' and 'code' and 'description' in params and 'image' in request.files:
      
      files = request.files.getlist('image')

      for file in files:

        try:

          filename = secure_filename(file.filename)
          file.save(os.getcwd() + "/img/" + filename)

        except FileNotFoundError as e:

          print(" ** LOG ERROR: No Image was presented: ", e)
          
      conn.insert("call SP_insert_fruit('%s', '%s', '%s','%s');" 
      % ( params['code'],params['name'], params['description'], filename))

      return jsonify(responseJsonHandler("Success!", status=True))

    else : 
      return jsonify(responseJsonHandler("The given params are wrong. "))

  else : 
    return jsonify(responseJsonHandler("The presented method request was wrong. "))


#prueba de imagen
@app.route("/testImage", methods=['GET'])
def testImage():
      filename = 'imagenes\\pingu.svg'
      return send_file(filename)

@app.route('/saveImage', methods=['POST'])
def saveImage():

  if request.method == "POST":
    files = request.files.getlist('files')

    for file in files:

      try:

        filename = secure_filename(file.filename)
        file.save(os.getcwd() + "/imagenes/" + filename)

      except FileNotFoundError:

        return 'Error, no hay imagen'

  return "Everything right"


#Function to get the ProductionLine Structure
def ProductionLineStructure(row):
  return {
    'code':row['code'],
    'ip': row['ip'],
    'description':row['description'],

    'status':{
      'lastConnection': row['lastConnection'],
      'value': row['status'], 
    }
  }

#Function to get the Fruit structure 
def FruitStructure(row):
  return {
    'code': row['code'],
    'name': row['name'], 
    'description': row['description'],
    'url' : row['url']
  }


def responseJsonHandler(res, status = False): 
  return {'res' : res, 'status': status}


app.run(host="0.0.0.0", port=5000)