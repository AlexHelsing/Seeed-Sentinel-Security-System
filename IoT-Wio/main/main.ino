#include <PubSubClient.h>
#include <rpcWiFi.h>
#include "scanWifi.h"
#include "JoyStickRecorder.h"
#include "globalVariables.h"
#include "config.h"







const char* server = Broker_IP;

// topics
const char* AlarmTopic = "/SeeedSentinel/AlarmOnOff";
const char* GetPatternFromClient = "/SeeedSentinel/GetPatternFromClient";
const char* PeekAtCurrentPattern = "/SeeedSentinel/PeekAtCurrentPattern";
//


// wifiScan class
scanWifi wifiScanner;
//

WiFiClient wioClient;
PubSubClient client(wioClient);


// MOTION STUFF
#define PIR_MOTION_SENSOR 2 // Use pin 2 for PIR motion sensor
bool alarmOn = false;
//

// Auth stuff 
JoyStickRecorder JoystickAuth;
bool initAuth = false;
String currentPattern;

//


void Callback(char* topic, byte* payload, unsigned int length) {
  if (strcmp(topic, AlarmTopic) == 0) {
    // check the incoming message if its alarmOn or AlarmOff 
    if (strncmp((char*)payload, "AlarmOn", length) == 0) {
      Serial.println("Activate motion detection");

      tft.fillScreen(TFT_GREEN);
      tft.println("turning on alarm"); // print the second line

      delay(2000);
      alarmOn = true;
     
      //client.setCallback(PatternAuthCallback);
      //client.subscribe(PatternAuthTopic); // subscribe to the control topic
    } else if (strncmp((char*)payload, "AlarmOff", length) == 0) {
      Serial.println("Turn off motion detection");
      // handle the AlarmOff message here
    }

    // subscribe to the pattern being sent and store it as global variable, flashstorage lags right now so this will have to do.
  } else if (strcmp(topic, GetPatternFromClient) == 0) {
    String temp = "";
    for (int i = 0; i < length; i++) {
      temp+=((char)payload[i]);
    }
    currentPattern = temp;
    Serial.println(currentPattern);
   
  

    // Check current stored pattern, just for dev purposes
  } else if (strcmp(topic, PeekAtCurrentPattern) == 0)  {
      Serial.println(currentPattern);
  }

   else {
    Serial.print("Unknown message: ");
    for (int i = 0; i < length; i++) {
      Serial.print((char)payload[i]);
    }
    Serial.println();
  }
}


void setup() {
   Serial.begin(9600);

   // PINS
    pinMode(PIR_MOTION_SENSOR, INPUT);
    pinMode(WIO_5S_UP, INPUT_PULLUP);
    pinMode(WIO_5S_DOWN, INPUT_PULLUP);
    pinMode(WIO_5S_LEFT, INPUT_PULLUP);
    pinMode(WIO_5S_RIGHT, INPUT_PULLUP);
    pinMode(WIO_5S_PRESS, INPUT_PULLUP);
    //    

    
  // Wifi
   wifiScanner.setupScan();
  //

  
  tft.init(); // initialize the TFT screen
  tft.setRotation(3);
  tft.fillScreen(TFT_RED); // set the background color to black
  tft.setTextColor(TFT_WHITE); // set the text color to white
  tft.setTextSize(2); // set the text size to 2
  tft.setCursor(40, 50); // set the cursor position
  tft.println("ALARM STATUS"); // print the first line
  tft.setCursor(40, 80); // set the cursor position
  tft.println("OFF"); // print the second line


  // MQTT Setup 
  client.setServer(server, 1883);
  client.setCallback(Callback);

  
  if (client.connect("WioID")) {
    Serial.println("Connection has been established");


    // topics we can subscribe to and do actions via callback function
    client.subscribe(GetPatternFromClient);
    client.subscribe(PeekAtCurrentPattern);
    client.subscribe(AlarmTopic); 
  } else {
    Serial.println("You done fucked up");
  }
}


void loop() {
  client.loop();
  
  if (alarmOn) {
        tft.fillScreen(TFT_GREEN); 
        tft.setTextSize(5);
        tft.setCursor(40, 50); // set the cursor position
        tft.setTextColor(TFT_BLACK); // set the text color to white
        tft.println("INTRUDER HUNT MODE");
    if(digitalRead(PIR_MOTION_SENSOR))   {
        tft.fillScreen(TFT_RED);
        tft.setTextColor(TFT_BLACK); // set the text color to white
        Serial.println("intruder found");
        initAuth = true;
        alarmOn = false;
    }    else {
        Serial.println("Watching");

        delay(200);
    }
  }

  if (initAuth) {
    JoystickAuth.beginAuth();
  }
  }
  
