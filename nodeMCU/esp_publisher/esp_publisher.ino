#include <WiFiClient.h>
#include <WebServer.h>
#include <PubSubClient.h>
#include <ArduinoJson.hpp>
#include <ArduinoJson.h>
#include <RunningMedian.h>
#include "DHT.h"
#include "HX711.h"

// Define DHT
//#define BUZZER 2                // Pending --
#define DHTPIN 15                 // D15
#define DHTTYPE DHT11             // DHT 11
DHT dht(DHTPIN, DHTTYPE);         // Initialize DHT

// Define Ultrasonic Sensor
#define TRIG_PIN 32               // D32
#define ECHO_PIN 33               // D33

// Define Buzzer
#define BUZZER 12                 // D12

// HX711 circuit wiring
#define LOADCELL_DOUT_PIN 18                    // D18
#define LOADCELL_SCK_PIN 19                     // D19
#define CALIBRATION_FACTOR 22.8678670361108     // Obtained by HX711 full example
const int errorRange = 20;                      // The sensor has an error range of 20 grams
int weight;
HX711 scale;

// Define TCS3200
#define S0 4
#define S1 5
#define S2 2
#define S3 21
#define sensorOut 22

// Calibration Values
int redMin = 163;       // Red minimum value
int redMax = 304;       // Red maximum value
int greenMin = 180;     // Green minimum value
int greenMax = 301;     // Green maximum value
int blueMin = 148;      // Blue minimum value
int blueMax = 227;      // Blue maximum value

// Variables for Color Pulse Width Measurements
int redPW = 0;
int greenPW = 0;
int bluePW = 0;

// Variables for final Color values
int redValue;
int greenValue;
int blueValue;

// Sample Varibles
RunningMedian weightSamples = RunningMedian(5);
RunningMedian redSamples = RunningMedian(5);
RunningMedian greenSamples = RunningMedian(5);
RunningMedian blueSamples = RunningMedian(5);

// Network credentials
const char *ssid = "INFINITUM67F2_2.4";
const char *password = "Ma2F3YqgAb"; 

// Variables for MQTT connection
// const char *mqtt_broker = "189.223.79.36";
// const int port = 6000;
const char *mqtt_broker = "broker.hivemq.com";   // For testing
const int port = 1883;
const String clientId = "NMCU-ARX";
const String description = "Production Line 1";

String ipAddress;
int ipSent = 0;

WiFiClient espClient;
PubSubClient client(espClient);
unsigned long lastMsg = 0;

// Topics
char publishProductionLine[] = "/UTT/register/productionLine";
char publishFruitTopic[] = "/UTT/register/fruitReadings";
char publishAreaTopic[] = "/UTT/register/enviromentVariables";
char subscribedTopic[] = "/UTT/register/readingsValidation";

// miliseconds sampling rate for the area variables
int samplingRateA = 60000;    
// miliseconds sampling rate for fruit results 
// and last fruit message - DELETE LATER BOTH
int samplingRateF = 5000;    
unsigned long lastMsgF = 0;

void setup_wifi(){
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while(WiFi.status() != WL_CONNECTED){
    delay(500);  
    Serial.print(".");
  }    

  randomSeed(micros());
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  IPAddress ip = WiFi.localIP();
  ipAddress = String(ip[0]) + "." + String(ip[1]) + "." + String(ip[2]) + "." + String(ip[3]);
}

// This function is pending
void callback(char* topic, byte* payload, unsigned int length){
  Serial.print("Message arrived [");    // Show the message received
  Serial.print(topic);  
  Serial.print("] ");
  for(int i = 0; i < length; i++){
    Serial.print((char)payload[i]);   // Print each character of the payload  
  }

  Serial.println();
  // Switch on the LED if an 1 was received as first character
  if((char)payload[0] == '1'){
     digitalWrite(BUILTIN_LED, HIGH);    // Turn the LED on 
     // (Note that LOW is the voltage level but actually the LED is on;
     // this is because it is active low on the ESP-01)
  } else {
    digitalWrite(BUILTIN_LED, LOW);    // Turn the LED off by making the voltage HIGH 
  }
}

void reconnect(){
  // Loop until we're reconnected
  while(!client.connected()){
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if(client.connect(clientId.c_str())){
      Serial.println("connected");  
      client.subscribe(subscribedTopic);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(2000);      // Wait 2 seconds before retrying  
    }  
  }
}

void getTone(){
  // Tone alert
  digitalWrite(BUZZER, HIGH);
  delay(700);
  digitalWrite(BUZZER, LOW);  
}

void sendProductionLine(){
  // JSON message values
  String JSON_msg;         
  StaticJsonDocument<300> doc; 
  doc["ID"] = clientId;
  doc["description"] = description; 
  doc["IP"] = ipAddress; 

  // Serialize values into JSON_msg
  serializeJson(doc, JSON_msg);
      
  Serial.print(F("Publishing message: "));
  Serial.println(JSON_msg);
      
  // MQTT Publish to topic
  char JSON_msg_array[100];
  char JSON_msg_length = JSON_msg.length();
  JSON_msg.toCharArray(JSON_msg_array, 100);
  Serial.println(JSON_msg_array);
  if(client.connected()){
    client.publish(publishProductionLine, JSON_msg_array);
    Serial.print("Published to topic:");
    Serial.println(publishProductionLine);
    Serial.println();
  } else {
    Serial.print("Not connected to broker... couldn't send MQTT message...");  
    Serial.println();
  }
  
}

void sendFruitResults(){
  // Take five readings and get the average and median
  for(int x = 0; x < 5; x++){
    // Read weight
    int w = scale.get_units(20);
    if(w <= errorRange) w = 0;
    weightSamples.add(w);
    
    // Set sensor to read Red only
    digitalWrite(S2,LOW);
    digitalWrite(S3,LOW);
    // Read Red value
    redPW = pulseIn(sensorOut, LOW);
    // Map to value from 0-255
    int red = map(redPW, redMin,redMax,255,0);
    // Validate range 0-255
    if(red < 0 || red > 255){
      if(red < 0){ red = 0; }
      else { red = 255; }
    }
    // add sample
    redSamples.add(red);
    // Delay to stabilize sensor
    delay(200);

    // Set sensor to read Green only
    digitalWrite(S2,HIGH);
    digitalWrite(S3,HIGH);
    // Read Green value
    greenPW = pulseIn(sensorOut, LOW);
    // Map to value from 0-255
    int green = map(greenPW, greenMin,greenMax,255,0);
    // Validate range 0-255
    if(green < 0 || green > 255){
      if(green < 0){ green = 0; }
      else { green = 255; }
    }
    // add sample
    greenSamples.add(green);
    // Delay to stabilize sensor
    delay(200);

    // Set sensor to read Blue only
    digitalWrite(S2,LOW);
    digitalWrite(S3,HIGH);
    // Read Blue value
    bluePW = pulseIn(sensorOut, LOW);
    // Map to value from 0-255
    int blue = map(bluePW, blueMin,blueMax,255,0);
    // Validate range 0-255
    if(blue < 0 || blue > 255){
      if(blue < 0){ blue = 0; }
      else { blue = 255; }
    }
    // add sample
    blueSamples.add(blue);
    // Delay to stabilize sensor
    delay(200);

    // Conditional for show a message
    Serial.println("Reading number " + String(x+1) + " taken");
    if(x < 4){
      Serial.println("Change the position of the fruit");
      Serial.print("5-");        delay(1000);
      Serial.print("4-");        delay(1000);
      Serial.print("3-");        delay(1000);
      Serial.print("2-");        delay(1000);
      Serial.println("1...");    delay(1000);
    }else {
      getTone();
      Serial.println("Remove any objects on the weight scale"); 
      Serial.println();
    }
  }
  
  // Define red as the average
  redValue = redSamples.getAverage();
  // Define green as the average
  greenValue = greenSamples.getAverage();
  // Define blue as the average
  blueValue = blueSamples.getAverage();
  // Define weight as the median
  weight = weightSamples.getMedian();
  
  // JSON message values
  String JSON_msg;         
  StaticJsonDocument<300> values; 
  values["ID"] = clientId; 
  values["W"] = weight;

  // JSON object for the color inside "values"
  JsonObject color = values.createNestedObject("Color");
  color["R"] = redValue;
  color["G"] = greenValue;
  color["B"] = blueValue;

  // Serialize values into JSON_msg
  serializeJson(values, JSON_msg);
  
  Serial.print(F("Publishing message: "));
  Serial.println(JSON_msg);

  // Reconnect client before to publish
  if(!client.connected()){
    reconnect();  
  }
  
  // MQTT Publish to topic
  char JSON_msg_array[100];
  char JSON_msg_length = JSON_msg.length();
  JSON_msg.toCharArray(JSON_msg_array, 100);
  Serial.println(JSON_msg_array);
  if(client.connected()){
    client.publish(publishFruitTopic, JSON_msg_array);
    Serial.print("Published to topic:");
    Serial.println(publishFruitTopic);
    Serial.println();
  } else {
    Serial.print("Not connected to broker... couldn't send MQTT message...");  
    Serial.println();
  }

  // Verify that objects are removed from the weight scale
  int w = scale.get_units(20);
  while(w != 0){
    getTone();
    Serial.println("Remove any objects on the weight scale"); 
    delay(1000);
    w = scale.get_units(20);
    if(w <= errorRange) w = 0;
  }

  Serial.println();
}

void sendAreaVariables(){
  float ToC = dht.readTemperature();
  float RH = dht.readHumidity();

  if (isnan(ToC) || isnan(RH)) {
    Serial.println(F("Failed to read from DHT sensor!"));
  }else{
    // JSON message values
    String JSON_msg;         
    StaticJsonDocument<300> values; 
    values["ID"] = clientId; 
    values["ToC"] = ToC;
    values["RH"] = RH;

    // Serialize values into JSON_msg
    serializeJson(values, JSON_msg);
      
    Serial.print(F("Publishing message: "));
    Serial.println(JSON_msg);
      
    // MQTT Publish to topic
    char JSON_msg_array[100];
    char JSON_msg_length = JSON_msg.length();
    JSON_msg.toCharArray(JSON_msg_array, 100);
    Serial.println(JSON_msg_array);
    if(client.connected()){
      client.publish(publishAreaTopic, JSON_msg_array);
      Serial.print("Published to topic:");
      Serial.println(publishAreaTopic);
      Serial.println();
    } else {
      Serial.print("Not connected to broker... couldn't send MQTT message..."); 
      Serial.println(); 
    }
  }  
}

void setup() {
  Serial.begin(74880);                                // Initializing on 748800
    
  setup_wifi();                                       // Connecting to the network
  client.setServer(mqtt_broker, port);                // Initializing connection with the broker
  client.setCallback(callback);                       // Callback based on the function with the same name
  
  // Start message
  Serial.println("Starting program...");
  Serial.println("Initializing sensors ...");
  Serial.println("Don't place any objects on the weight scale");
  
  pinMode(BUILTIN_LED, OUTPUT);                       // Initialize the BUILTIN_LED pin as an output
  dht.begin();                                        // Initialize dht
  pinMode(BUZZER, OUTPUT);                            // Initialize BUZZER pin as an output
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);   // Initialize HX711
  scale.set_scale(CALIBRATION_FACTOR);                // Set the scale on based the calibration factor
  scale.tare(20);                                     // Assuming there is no weight on the scale at start up, reset the scale to 0
  
  pinMode(S0, OUTPUT);                                // 
  pinMode(S1, OUTPUT);                                //
  pinMode(S2, OUTPUT);                                // Initialize TCS3200 pins
  pinMode(S3, OUTPUT);                                //
  pinMode(sensorOut, INPUT);                          //
                                                      
  digitalWrite(S0,HIGH);                              //
  digitalWrite(S1,LOW);                               // Setting frequency-scaling to 20% for TCS3200

  pinMode(TRIG_PIN, OUTPUT);                          //
  pinMode(ECHO_PIN, INPUT);                           // Initialize Ultrasonic Sensor Pins

  Serial.println("Ready to use");
}

void loop() {
  if(!client.connected()){
    reconnect();  
  }

  // Verify if any fruit is on the scale
  // using the ultrasonic sensor
  float duration, distance, scale;         
  // Declaring a duration and distance for the process
  // and scale depeding the heigth of the scale
  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);
  duration = pulseIn(ECHO_PIN, HIGH);
  distance = (duration / 2) * 0.0344;
  scale = 10;

  client.loop();
  unsigned long now = millis();

  // The IP Address has to be sent once
  if (ipSent == 0){
    Serial.println();
    sendProductionLine();
    ipSent++;  
  }

  if((distance <= scale && distance > 0) && (now - lastMsgF > samplingRateF)){
    Serial.println("\nFruit detected...");
    delay(1000);
    sendFruitResults();
    lastMsgF = millis();
  }

  now = millis();
  if(now - lastMsg > samplingRateA){
    sendAreaVariables();
    lastMsg = millis();
  }
 
}