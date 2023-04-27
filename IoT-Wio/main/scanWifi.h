//including necessary libraries
#include "rpcWiFi.h" //Wi-Fi library
#include "rpcBLEDevice.h" //bluetooth library 
#include "BLEScan.h" //bluetooth library 
#include "BLEAdvertisedDevice.h" //bluetooth library

int scanTime = 5; //set time for scanning bluetooth to 5 second 

BLEScan* pBLEScan; //declaring pointer variable pointerOfBLEScan of class BLEScan

//to print the MAC addresses of available bluetooth devices
class MyAdvertisedDeviceCallbacks : public BLEAdvertisedDeviceCallbacks {
public:
  void onResult(BLEAdvertisedDevice advertisedDevice){
    Serial.printf("Advertised Device: %s \n", advertisedDevice.toString().c_str()); //to print the MAC addresses of found bluetooth devices
  }

  void setupScan() {
    Serial.begin(115200); //to initialize serial communication between computer and microcontroller at speed of 115200 bits per second
    while(!Serial); //the program should wait until serial communication is established
    delay(100); //have 100 milliseconds delay

    //Wi-Fi setup
    WiFi.mode(WIFI_STA); //connect Wio as a client to WiFi
    WiFi.disconnect(); // to disconnect Wio from previously connected WiFi

    //Bluetooth setup
    BLEDevice::init(""); //initialize bluetooth
    pBLEScan = BLEDevice::getScan(); 
    pBLEScan->setAdvertisedDeviceCallbacks(this); 
    pBLEScan->setActiveScan(true); //active scan (to scan for bluetooth devices actively)
    pBLEScan->setInterval(100); //to set the scan interval to 100 milliseconds for frequent scanning 
    pBLEScan->setWindow(99); //setting the scan window to 99 milliseconds (to actively listen to bluetooth devices for 99 milliseconds)
  }

  void loopScan() {
    //Wi-Fi scan
    Serial.println("Wi-Fi scan start");
    int n = WiFi.scanNetworks(); //return the number of Wi-Fi networks found 

    if (n == 0){
      Serial.println("no networks found"); 
    } else{
      Serial.print(n); //print the number of networks found 
      Serial.println(" network(s) found");
      for (int i = 0; i < n; ++i) { 
          //print SSID and RSSI  
          Serial.print(i + 1); 
          Serial.print(": ");
          Serial.print(WiFi.SSID(i)); //print Wi-Fi name 
          Serial.print(" (");
          Serial.print(WiFi.RSSI(i)); //print Wi-Fi signal strength
          Serial.print(")");
          Serial.println((WiFi.encryptionType(i) == WIFI_AUTH_OPEN) ? " " : "*"); 
          delay(10);
      }
    }

    Serial.println("Wi-Fi scan done!");
    Serial.println(""); //print an empty line

    //Bluetooth scan 
    Serial.println("BLE scan start");
    BLEScanResults foundDevices = pBLEScan->start(scanTime, false); //scan for bluetooth devices 
    Serial.print(foundDevices.getCount()); //print number of bluetooth devices 
    Serial.println(" BLE device(s) found");
    Serial.println("BLE scan done!");
    pBLEScan->clearResults(); //delete results from pBLEScan buffer 
    Serial.println("");

    delay(1000); //wait before scanning again

  }
};