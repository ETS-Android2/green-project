from flask import Flask, json, render_template, request, jsonify
from flask_cors import CORS

from sys import path
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
      return jsonify(connection.consult("select * from  VW_readings_last_hour;"))

    if action == 'get_fruits':
      return jsonify(connection.consult("select * from VW_fruits;"))

    if action == 'get_fruits_result':
      return jsonify(connection.consult("select * from VW_fruits_result_today;"))

    if action == 'get_productionLines':
      return jsonify(connection.consult("select * from VW_productionLines;"))

    if action == 'get_enviromentVariables':
      return jsonify(connection.consult("select * from  VW_enviroment_variable;"))

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

  

def responseJsonHandler(res, status = False): 
  return {'res' : res, 'status': status}




app.run(host="0.0.0.0", port=5000)