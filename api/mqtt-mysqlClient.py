import json
import datetime
from json.decoder import JSONDecodeError
import time
import paho.mqtt.client as mqtt

from sys import path

path.append("./mysql")
from mysql_connection import mysqlConnection

broker = "127.0.0.1"
port = 1885

## /UTT/register/productionLine -> Registrar una nueva línea de producción
## /UTT/register/enviromentVariables -> Humidity and Temperature
## /UTT/register/fruitReadings -> Fruits' readings

## /UTT/[I HAVENT DECIDE YET]/readingsValidation -> [I WILL ADD THIS LATER!]

topicInsertPL = "/UTT/register/productionLine"
topicInsertEV = "/UTT/register/enviromentVariables"
topicInsertFR = "/UTT/register/fruitReadings"

def on_subscribe(client, userdata, mid, granted_qos):
    print("Subscribed with QOS:", granted_qos, "\n")

def on_connect(client, userdata, flag, rc):
    print("Connected with the result code ", str(rc))


def on_message(client, userdata, msg):

    json_data = parseJSON(str(msg.payload.decode("utf-8")))
    intopic = str(msg.topic)

    print("Topic %s, Message: %s " % (intopic,  json_data))
    

    if intopic == topicInsertPL:

        

        print("inserting")



    if intopic == topicInsertEV:
        print("inserting")



    if intopic == topicInsertFR:
        print("inserting")


def parseJSON(json_msg): 
    try :
        return json.loads(json_msg)
    except JSONDecodeError as e:
        print(" ** LOG ERROR: The message received was not a JSON obsejct: ", e)


client = mqtt.Client("MySQL_Client", transport="tcp")

client.on_connect = on_connect
client.on_message = on_message
# client.on_subscribe = on_subscribe

try: 
    client.connect(broker, port)
except Exception as e:
    print(" ** LOG ERROR: The MQTT connection has failed: ", e)


client.subscribe(topicInsertEV)
client.subscribe(topicInsertFR)
client.subscribe(topicInsertPL)

client.loop_forever()


