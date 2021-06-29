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
  conn = mysqlConnection()
  return "This will be an HTML with the api's body..."

  

@app.route('/api', methods=['GET'])
def actions():
  params = request.args
  
  if 'action' in params:
    return jsonify({'res': params['action']})
  else :
    return jsonify(errorJsonHandler("The required param 'action' was not specified"))

def errorJsonHandler(res): return {'res' : res}

app.run()