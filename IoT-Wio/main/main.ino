#include <PubSubClient.h>
#include <rpcWiFi.h>
#include "scanWifi.h"




const char* server = "192.168.1.178";
const char* topic = "test/topic";

scanWifi wifiScanner;

WiFiClient wioClient;
PubSubClient client(wioClient);

void callback(char* topic, byte* payload, unsigned int length) {
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
  wifiScanner.setupScan();
  Serial.println("test");
  client.setServer(server, 1883);
  client.setCallback(callback);

  if (client.connect("wioID")) {
    Serial.println("connection has been established");
    client.subscribe(topic); // subscribe to the specified topic
  } else {
    Serial.println("you done fucked up");
  }

  
}

void loop() {
  client.loop();
}
