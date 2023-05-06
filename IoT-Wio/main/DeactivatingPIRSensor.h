#ifndef ALARM_SYSTEM_H
#define ALARM_SYSTEM_H

#include <Arduino.h>

class AlarmSystem {
private:
  const int BUZZER_PIN = 3; // Use pin 3 on Wio
  const int PIR_PIN = 2; // Use pin 2 for PIR motion sensor
  const unsigned long ALERT_DURATION = 5000; // Set the duration for the alert in milliseconds

  bool isAlertActive = false; // Flag to track if an alert is active
  unsigned long alertStartTime = 0; // Variable to store the start time of the alert

public:
  void setup() {
    pinMode(PIR_PIN, INPUT);
    pinMode(BUZZER_PIN, OUTPUT);
  }

  void loop() {
    if (isAlertActive) {
      // Check if the alert duration has elapsed
      if (millis() - alertStartTime >= ALERT_DURATION) {
        deactivatePIRSensor(); // Call the function to deactivate the PIR sensor
      }
    } else {
      // Check for motion detection
      if (digitalRead(PIR_PIN)) {
        activateAlert(); // Call the function to activate the alert
      }
    }
  }

  void activateAlert() {
    Serial.println("Warning: Intruder!");
    digitalWrite(BUZZER_PIN, HIGH);
    isAlertActive = true;
    alertStartTime = millis();
  }

  void deactivatePIRSensor() {
    Serial.println("Alert dismissed. PIR sensor deactivated.");
    digitalWrite(BUZZER_PIN, LOW);
    isAlertActive = false;
  }
};

#endif
