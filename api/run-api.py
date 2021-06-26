from flask import Flask, render_template, request, jsonify
from flask_cors import CORS




app = Flask(__name__)
app.config['DEBUG']=True
CORS(app)

#@app.route('/',method=['GET', 'POST'])
#def home():
 #   if  request.method == 'POST':
  #      return render_template('index.html')

@app.route('/api', methods=['GET'])
def actions():
    params = request.args
    if 'action' in params:
        return jsonify({'res': 'funcionando'})
app.run()