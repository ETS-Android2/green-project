from flask import Flask
from flask import render_template

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/get-readings')
def get_readings():
    return render_template('reading.html')

@app.route('/get-fruits')
def get_fruits():
    return render_template('fruits.html')

@app.route('/get-fruit-Results')
def get_fruitResults():
     return render_template('results.html')

@app.route('/get-productionLines')
def get_productionLines():
    return render_template('production.html')

@app.route('/get-enviromentVariables')
def get_enviromentVariables():
    return render_template('emviroment.html')   

@app.route('/set-fruit')
def get_set_fruit():
    return render_template('setFruits.html')   

if __name__ == '__main__':
    app.run(debug=True, port = 5000)