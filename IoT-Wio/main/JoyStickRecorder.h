#include <TFT_eSPI.h>
#include "globalVariables.h"

#ifndef JoyStickRecorder_h
#define JoyStickRecorder_h



// Library to put stuff on screeen.
TFT_eSPI tft;
TFT_eSPI tftNew;

//Temporary storage unitl we got SD CARD.


class JoyStickRecorder {
  public: static
  const int MAX_BUTTON_PRESSES = 100;
  String buttonPresses[MAX_BUTTON_PRESSES];
  int buttonPressCount = 0;
  boolean IsInputting = true;


  void beginAuth() {   
    tft.fillScreen(TFT_BLACK);
    tft.setCursor(0, 0);
    tft.setTextColor(TFT_WHITE, TFT_BLACK);
    tft.setTextSize(10);



    // We could also store these pressess as ints i suppose { left=1, up=2, down=3, top=4 }
    if (digitalRead(WIO_5S_UP) == LOW) {
      Serial.println("Key_Pressed: UP");
      addButtonPress("Up");
      printDirectionOnScreen("UP", 110, 105);

    } else if (digitalRead(WIO_5S_DOWN) == LOW) {
      Serial.println("Key_Pressed: DOWN");
      addButtonPress("Down");
      printDirectionOnScreen("DOWN", 80, 105);
    } else if (digitalRead(WIO_5S_LEFT) == LOW) {
      Serial.println("Key_Pressed: LEFT");
      addButtonPress("Left");
      printDirectionOnScreen("LEFT", 80, 105);

    } else if (digitalRead(WIO_5S_RIGHT) == LOW) {
      Serial.println("Key_Pressed: RIGHT");
      addButtonPress("Right");
      printDirectionOnScreen("RIGHT", 60, 105);

    } else if (digitalRead(WIO_5S_PRESS) == LOW) {
      Serial.println("Submitting...");

      
  
      delay(2000);


      onSubmit();

      IsInputting == false;

      while (IsInputting);
    }

    delay(200);
  }

  void addButtonPress(String buttonPress) {
    if (buttonPressCount < MAX_BUTTON_PRESSES) {
      buttonPresses[buttonPressCount] = buttonPress;
      buttonPressCount++;
    }
  }

  void printDirectionOnScreen(String direction, int x, int y) {
    tft.setTextDatum(TL_DATUM);
    tft.drawString(direction, x, y);
  }


void onSubmit() {
  String attempt = "";
  for (int i = 0; i < buttonPressCount; i++) {
    attempt += buttonPresses[i];
    // remove last comma
    if (i != buttonPressCount - 1) {
      attempt += ", ";
    }
  }
  if (attempt == currentPattern) {
    Serial.println("ACCESS GRANTED");
    tft.fillScreen(TFT_GREEN);

    delay(2000);
    initAuth = false;
    alarmOn = false;
  } else {
    Serial.println("INTRUDER");
    tft.fillScreen(TFT_RED);

    Serial.println("TRY AGAIN");
    // idk how to try again
  }
}
};

#endif
