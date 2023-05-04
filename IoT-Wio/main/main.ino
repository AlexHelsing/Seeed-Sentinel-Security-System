#include <PubSubClient.h>
#include <rpcWiFi.h>
#include "scanWifi.h"
#include "JoyStickRecorder.h"


JoyStickRecorder JoystickAuth;



const char* server = "192.168.1.178";
const char* AlarmTopic = "/SeeedSentinel/AlarmOnOff";
const char* PatternAuthTopic = "/SeeedSentinel/pattern";
bool initAuth = false;
scanWifi wifiScanner;

WiFiClient wioClient;
PubSubClient client(wioClient);


// MOTION STUFF
#define PIR_MOTION_SENSOR 2 // Use pin 2 for PIR motion sensor
bool intruder = false; // Flag to track if an alert is active
bool alarmOn = false;
//


void AlarmTopicCallback(char* topic, byte* payload, unsigned int length) {
  // check the incoming message if its alarmOn or AlarmOff 
  if (strncmp((char*)payload, "AlarmOn", length) == 0) {
    Serial.println("Activate motion detection");

    tft.fillScreen(TFT_GREEN);
    alarmOn = true;
   
    //client.setCallback(PatternAuthCallback);
    //client.subscribe(PatternAuthTopic); // subscribe to the control topic
  } else if (strncmp((char*)payload, "AlarmOff", length) == 0) {
    Serial.println("Turn off motion detection");
    // handle the AlarmOff message here
  } else {
    Serial.print("Unknown message: ");
    for (int i = 0; i < length; i++) {
      Serial.print((char)payload[i]);
    }
    Serial.println();
  }
}

void PatternAuthCallback(char* topic, byte* payload, unsigned int length) {
  // handle the incoming message here
  Serial.println("New message received:");
  Serial.print("Topic: ");
  Serial.println(topic);
  Serial.print("Payload: ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
}


void setup() {
  pinMode(PIR_MOTION_SENSOR, INPUT);
  Serial.begin(9600);
  JoystickAuth.setup();


  wifiScanner.setupScan();
  client.setServer(server, 1883);
  client.setCallback(AlarmTopicCallback);

  
  if (client.connect("WioID")) {
    Serial.println("Connection has been established");
    client.publish("testpub", "ooooo");


    client.subscribe(AlarmTopic); // subscribe to the alarm topic
  } else {
    Serial.println("You done fucked up");
  }
}

void loop() {
  client.loop();

  if (alarmOn) {
    if(digitalRead(PIR_MOTION_SENSOR))   { 
        Serial.println("Intruder");
        initAuth = true;
        alarmOn = false;
    }    else {
        Serial.println("Watching");

        delay(200);
    }
  }

  if (initAuth) {
    JoystickAuth.loop();
  }
}
