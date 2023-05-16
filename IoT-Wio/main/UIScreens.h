#include <TFT_eSPI.h>

class UIScreens

{
public:
  TFT_eSPI &tft; // Public member variable for storing the TFT_eSPI instance

  UIScreens(TFT_eSPI &tftinstance) : tft(tftinstance) {}
  void setup()
  {
    // ui lib

    tft.init();
    tft.setRotation(3);
    tft.fillScreen(TFT_BLACK);
  }

  void SHOWHOMESCREEN()
  {
    tft.fillScreen(0x07FF);      // set the background color to black
    tft.setTextColor(TFT_BLACK); // set the text color to white
    tft.setTextSize(4);          // set the text size to 2
    tft.setCursor((tft.width() - tft.textWidth("SeeedSentinel")) / 2 - 5, tft.height() / 2 - 30);
    tft.println("SEEEDSENTINEL"); // print the first line
    tft.setCursor(30, 150);       // set text position
    tft.setTextSize(2);
    tft.println("Turn on alarm on phone"); // print the second line
  }

  void AcessDeniedScreen()
  {
    tft.fillScreen(TFT_BLACK); // set the background color to black
    tft.setTextColor(TFT_RED); // set the text color to green
    tft.setTextSize(3);        // set the text size to 3
    tft.setCursor((tft.width() - tft.textWidth("Access Denied")) / 2, tft.height() / 2 - 30);
    tft.println("Access Denied"); // print the text

    tft.setTextSize(2); // set the text size to 2
    tft.setCursor((tft.width() - tft.textWidth("Try again!")) / 2, tft.height() / 2 + 20);
    tft.println("Try again!"); // print the subtitle
  }

  void AcessGrantedScreen(String uname)
  {
    tft.fillScreen(TFT_BLACK);   // set the background color to black
    tft.setTextColor(TFT_GREEN); // set the text color to green
    tft.setTextSize(3);          // set the text size to 3
    tft.setCursor((tft.width() - tft.textWidth("Access Granted")) / 2, tft.height() / 2 - 30);
    tft.println("Welcome home!"); // print the text
    tft.setTextSize(4); // set the text size to 2
    tft.setCursor((tft.width() - tft.textWidth(uname)) / 2, tft.height() / 2 + 20);
    tft.println(uname); // print the subtitle
    
  }
  void ShowAlarmHuntScreen()
  {
    tft.fillScreen(TFT_BLACK); // set the background color to black
    tft.setTextColor(TFT_RED); // set the text color to white
    tft.setTextSize(4);        // set the text size to 3
    tft.setCursor((tft.width() - tft.textWidth("HUNTING MODE")) / 2, tft.height() / 2 - 20);
    tft.println("HUNTING MODE"); // print the title
  }
  void ShowAlarmLoadingScreen()
  {
    tft.fillScreen(TFT_BLACK);   // set the background color to black
    tft.setTextColor(TFT_WHITE); // set the text color to white
    tft.setTextSize(3);          // set the text size to 3
    tft.setCursor((tft.width() - tft.textWidth("LOADING..")) / 2, tft.height() / 2 - 20);
    tft.println("LOADING..."); // print the title
  }

  void ShowIntruderScreen()
  {
    tft.fillScreen(TFT_RED);     // set the background color to red
    tft.setTextColor(TFT_WHITE); // set the text color to white
    tft.setTextSize(4);          // set the text size to 4

    tft.setCursor((tft.width() - tft.textWidth("INTRUDER")) / 2, tft.height() / 2 - 50);
    tft.println("INTRUDER"); // print the title

    tft.setTextSize(2); // set the text size to 2
    tft.setCursor((tft.width() - tft.textWidth("Authorize yourself")) / 2, tft.height() / 2 + 20);
    tft.println("Authorize yourself"); // print the subtitle
  }
};
