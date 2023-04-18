#include <TFT_eSPI.h>
#include <FlashStorage.h>

#ifndef JoyStickRecorder_h
#define JoyStickRecorder_h


// Library to put stuff on screeen.
TFT_eSPI tft;

//Temporary storage unitl we got SD CARD.
FlashStorage(storage, String);

class JoyStickRecorder {
  private: static
  const int MAX_BUTTON_PRESSES = 100;
  String buttonPresses[MAX_BUTTON_PRESSES];
  int buttonPressCount = 0;
  boolean IsInputting = true;

  public: void setup() {
    clearStorage();
    Serial.begin(115200);
    pinMode(WIO_5S_UP, INPUT_PULLUP);
    pinMode(WIO_5S_DOWN, INPUT_PULLUP);
    pinMode(WIO_5S_LEFT, INPUT_PULLUP);
    pinMode(WIO_5S_RIGHT, INPUT_PULLUP);
    pinMode(WIO_5S_PRESS, INPUT_PULLUP);

    tft.begin();
    tft.fillScreen(TFT_BLACK);
    tft.setRotation(3);
    tft.setTextSize(10);

  }

  void loop() {
    tft.fillScreen(TFT_BLACK);
    tft.setCursor(0, 0);
    tft.setTextColor(TFT_WHITE, TFT_BLACK);
    tft.setTextSize(10);

    // We could also store these pressess as ints i suppose { left=1, up=2, down=3, top=4 }
    if (digitalRead(WIO_5S_UP) == LOW) {
      Serial.println("Key_Pressed: UP");
      addButtonPress("Up");
      printDirectionOnSreen("UP", 110, 105);

    } else if (digitalRead(WIO_5S_DOWN) == LOW) {
      Serial.println("Key_Pressed: DOWN");
      addButtonPress("Down");
      printDirectionOnSreen("DOWN", 80, 105);
    } else if (digitalRead(WIO_5S_LEFT) == LOW) {
      Serial.println("Key_Pressed: LEFT");
      addButtonPress("Left");
      printDirectionOnSreen("LEFT", 80, 105);

    } else if (digitalRead(WIO_5S_RIGHT) == LOW) {
      Serial.println("Key_Pressed: RIGHT");
      addButtonPress("Right");
      printDirectionOnSreen("RIGHT", 60, 105);

    } else if (digitalRead(WIO_5S_PRESS) == LOW) {
      Serial.println("Submitting...");


      /////////////////////////
      /// Will replace this later but works for now so one can see when theyve submitted/stopped recording.
      
      tft.fillScreen(TFT_GREEN);

      delay(2000);

      tft.fillScreen(TFT_RED);


      //////////////////////

      // Save pressess to they are preserved even after restart
      writeButtonPressesToStorage();

      // Prints all directions pressed in the serial monitor.
      printButtonPresses();

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

  void writeButtonPressesToStorage() {
    // Flashstorage cant store arrays so we gotta do it like this for now :(
    String buttonPressString = "";
    for (int i = 0; i < buttonPressCount; i++) {
      buttonPressString += buttonPresses[i];

    }
    storage.write(buttonPressString);
  }

  void printButtonPresses() {
    Serial.print("Pattern: ");
    for (int i = 0; i < buttonPressCount; i++) {
      Serial.print(buttonPresses[i] + ", ");
    }
  }

  
  void peekAtStorage() {
    String storedButtonPresses = storage.read();
    Serial.println(storedButtonPresses);
  }

  void clearStorage() {
    // theres no erase method in this library so we gotta do it like this.
    storage.write("");
  }

};

#endif
