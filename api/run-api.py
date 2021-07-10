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
      return jsonify(connection.consult("select * from VW_fruits_result_today;"))
    if action == 'get_devices':
      return jsonify(connection.consult("select * from VW_productionLines;"))
    else:
      return jsonify(errorJsonHandler("The param 'action' has a wrong value"))
  else :
    return jsonify(errorJsonHandler("The required param 'action' was not specified"))

  

def errorJsonHandler(res): return {'res' : res}




app.run(host="0.0.0.0", port=5000)