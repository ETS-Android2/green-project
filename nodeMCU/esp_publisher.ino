#include <WiFiClient.h>
#include <WebServer.h>
#include <PubSubClient.h>
#include <ArduinoJson.hpp>
#include <ArduinoJson.h>
#include "DHT.h"
#include "HX711.h"

// Define DHT
//#define BUZZER 2                // Pending --
#define DHTPIN 15                 // D15
#define DHTTYPE DHT11             // DHT 11
DHT dht(DHTPIN, DHTTYPE);         // Initialize DHT

// PENDING
/*
// Define Ultrasonic Sensor
#define TRIG_PIN 12                               // D14
#define ECHO_PIN 13                               // D13
*/

// HX711 circuit wiring
#define LOADCELL_DOUT_PIN 18          // D18
#define LOADCELL_SCK_PIN 19           // D19
#define CALIBRATION_FACTOR -2317.0    // Obtained by HX711 full example
HX711 scale;

// Define TCS3200
#define S0 4
#define S1 5
#define S2 2
#define S3 21
#define sensorOut 22
// int frequency = 0;

// Network credentials
const char *ssid = "INFINITUM67F2_2.4";
const char *password = "Ma2F3YqgAb"; 
const char *mqtt_broker = "broker.hivemq.com";   // For testing

WiFiClient espClient;
PubSubClient client(espClient);
unsigned long lastMsg = 0;
char OutTopic[] = "/UTT/0319124849/";
char InTopic[] = "/UTT/0319124849/cntrl/";
int Ts = 5000;    // miliseconds sampling rate

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
    String clientId = "NMCU-0319124849";
    // Attempt to connect
    if(client.connect(clientId.c_str())){
      Serial.println("connected");  
      client.subscribe(InTopic);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(2000);      // Wait 2 seconds before retrying  
    }  
  }
}

void setup() {
  pinMode(BUILTIN_LED, OUTPUT);                       // Initialize the BUILTIN_LED pin as an output
  Serial.begin(74880);                                // Initializing on 748800
  dht.begin();                                        // Initialize dht
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);   // Initialize HX711
  scale.set_scale(CALIBRATION_FACTOR);                // Set the scale on based the calibration factor
  scale.tare();                                       // Assuming there is no weight on the scale at start up, reset the scale to 0
  
  pinMode(S0, OUTPUT);                                // 
  pinMode(S1, OUTPUT);                                //
  pinMode(S2, OUTPUT);                                // Initialize TCS3200 pins
  pinMode(S3, OUTPUT);                                //
  pinMode(sensorOut, INPUT);                          //
                                                      
  digitalWrite(S0,HIGH);                              //
  digitalWrite(S1,HIGH);                              // Setting frequency-scaling to 100% for TCS3200

  //pinMode(TRIG_PIN, OUTPUT);                          //
  //pinMode(ECHO_PIN, INPUT);                           // Initialize Ultrasonic Sensor Pins
  
  setup_wifi();                                       // Connecting to the network
  client.setServer(mqtt_broker, 1883);                // Initializing connection with the broker
  client.setCallback(callback);                       // Callback based on the function with the same name
}

void loop() {
  if(!client.connected()){
    reconnect();  
  }
  
  client.loop();
  unsigned long now = millis();
  if(now - lastMsg > Ts){
    float ToC = dht.readTemperature();
    float RH = dht.readHumidity();
    float weight = scale.get_units();       // This part is not calibrated yet

    // assign variables for the color
    // setting red filtered photodiodes to be read
    digitalWrite(S2,LOW);
    digitalWrite(S3,LOW);
    // Reading the output frequency
    int R = pulseIn(sensorOut, LOW);
    // Setting Green filtered photodiodes to be read
    digitalWrite(S2,HIGH);
    digitalWrite(S3,HIGH);
    // Reading the output frequency
    int G = pulseIn(sensorOut, LOW);
    // Setting Blue filtered photodiodes to be read
    digitalWrite(S2,LOW);
    digitalWrite(S3,HIGH);
    // Reading the output frequency
    int B = pulseIn(sensorOut, LOW);
   
    if (isnan(ToC) || isnan(RH)) {
      Serial.println(F("Failed to read from DHT sensor!"));
    } else if(scale.is_ready()){
      Serial.println(F("HX711 not found."));
    }else if (isnan(R) || isnan(G) || isnan(B)){
      Serial.println(F("Failed to read from TCS3200"));
    } else {
      
      // JSON message values
      String JSON_msg;         
      StaticJsonDocument<300> values; 
      values["n"] = "0319124849"; 
      values["ToC"] = ToC;
      values["RH"] = RH;
      values["W"] = weight;

      // JSON object for the color inside "values"
      JsonObject color = values.createNestedObject("Color");
      color["R"] = R;
      color["G"] = G;
      color["B"] = B;

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
        client.publish(OutTopic, JSON_msg_array);
        Serial.print("Published to topic:");
        Serial.println(OutTopic);
      } else {
        Serial.print("Not connected to broker... couldn't send MQTT message...");  
      }
    }
    
    lastMsg = millis();
  }
}