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
      structureData = []
      for row in data:
        structureData.append(
          {
            'ip': row['IP'],
            'code':row['code'],
            'date': row['date'],
            'fruit': row['fruit'],
            'color': {
               'R': row['R'],
               'G': row['G'],
               'B': row['B'],
            },
            'weight': {
                'value': row['weight'],
            },
            'status':{
              'value':row['status'],
              'lastConnection': row['lastConnection'],
            }
          }
        )
      return jsonify(structureData)
    
   
    if action == 'get_fruits':
      return jsonify(connection.consult("select * from  VW_fruits;"))



    if action == 'get_fruits_result':
      return jsonify(connection.consult("select * from VW_fruits_result_today;"))

    if action == 'get_productionLines':
      return jsonify(connection.consult("select * from VW_productionLines;"))

    if action == 'get_enviromentVariables':
      data = connection.consult("select * from  VW_enviroment_variable;")
      productionLineData = []
      for row in data:
        productionLineData.append(
        {
          'ip': row['IP'],
          'code':row['code'],
          'date':row['date'],
          'values':{
              'temperature': row['temperature'],
              'humidity': row['humidity'],
           },
           'status':{
             'status': row['status'], 
             'lastConnection': row['lastConnection']
           }
        }
      )
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

def responseJsonHandler(res, status = False): 
  return {'res' : res, 'status': status}




app.run(host="0.0.0.0", port=5000)