#include <PubSubClient.h>
#include <rpcWiFi.h>
#include <TFT_eSPI.h>
#include "rpcWiFi.h"
#include "globalVariables.h"
#include "config.h"
#include "keyPadConfig.h"

//
const char *ssid = SSID;              // replace with your own wifi network to test
const char *password = WIFI_PASSWORD; // and password :)
const char *server = Broker_IP;

// topics
const char *AlarmTopic = "/SeeedSentinel/AlarmOnOff";
const char *GetPasscodeFromClient = "/SeeedSentinel/GetPatternFromClient";
//

//ui lib
TFT_eSPI tft;

// WIfi/mqtt 
WiFiClient wioClient;
PubSubClient client(wioClient);

// MOTION STUFF
#define PIR_MOTION_SENSOR D0 // Use pin 2 for PIR motion sensor
bool alarmOn = false;
//

bool initAuth = false;

// Callback function where all the incoming topic subscriptions are handled.
void Callback(char *topic, byte *payload, unsigned int length)
{
  if (strcmp(topic, AlarmTopic) == 0)
  {
    // check the incoming message if its alarmOn or AlarmOff
    if (strncmp((char *)payload, "AlarmOn", length) == 0)
    {
      Serial.println("Activate motion detection");

      ShowAlarmLoadingScreen();

      delay(1000);
      // turn on alarm
      alarmOn = true;
    }
    else if (strncmp((char *)payload, "AlarmOff", length) == 0)
    {
      Serial.println("Turn off motion detection");
      // handle the AlarmOff message here .
      alarmOn = false;
      initAuth = false;
    }

    // subscribe to the pattern being sent and store it as global variable, flashstorage lags right now so this will have to do.
  }
  else if (strcmp(topic, GetPasscodeFromClient) == 0)
  {
    String answerString = "";
    for (int i = 0; i < length; i++)
    {
      answerString += (char)payload[i];
    }
    // update our answerString
    setAnswerString(answerString);
  }
}
// set the answerString
void setAnswerString(const String &newAnswerString)
{
  answerString = newAnswerString;
}

void setupScan()
{

  // Set WiFi to station mode and disconnect from an AP if it was previously connected
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();

  Serial.println("Connecting to WiFi..");
  tft.setTextSize(2);
  tft.setCursor(0, 0);
  tft.setTextColor(TFT_WHITE);
  tft.println("Connecting to WiFi...");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }
  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP()); // prints out the device's IP address
}

void initializeGrid()
{
  // Resets all our state variables, useful when someone uses more than one attempt.
  for (int row = 0; row < NUM_ROWS; row++)
  {
    for (int col = 0; col < NUM_COLS; col++)
    {
      pressedRectangles[row][col] = false;
    }
  }
  memset(userInputString, 0, sizeof(userInputString)); // reset user input
  userInputCount = 0;
  currentRow = 0;
  currentCol = 0;
  isInputting = true;

  tft.setRotation(3);
  tft.fillScreen(TFT_BLACK); // Set background color

  // Initialize the rectangle positions
  int gridWidth = NUM_COLS * RECTANGLE_WIDTH + (NUM_COLS - 1) * RECTANGLE_SPACING;
  int gridHeight = NUM_ROWS * RECTANGLE_HEIGHT + (NUM_ROWS - 1) * RECTANGLE_SPACING;
  int gridX = (tft.width() - gridWidth) / 2;
  int gridY = (tft.height() - gridHeight) / 2;
  for (int row = 0; row < NUM_ROWS; row++)
  {
    for (int col = 0; col < NUM_COLS; col++)
    {
      rectangleX[row][col] = gridX + (RECTANGLE_WIDTH + RECTANGLE_SPACING) * col;
      rectangleY[row][col] = gridY + (RECTANGLE_HEIGHT + RECTANGLE_SPACING) * row;
      tft.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, TFT_WHITE);
      tft.setTextColor(TFT_BLACK);
      tft.setTextSize(3);
      tft.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
      tft.print(rectangleNumber[row][col]);
    }
  }
}

void setup()
{
  Serial.begin(115200);

  // PINS
  pinMode(PIR_MOTION_SENSOR, INPUT);
  pinMode(WIO_5S_UP, INPUT_PULLUP);
  pinMode(WIO_5S_DOWN, INPUT_PULLUP);
  pinMode(WIO_5S_LEFT, INPUT_PULLUP);
  pinMode(WIO_5S_RIGHT, INPUT_PULLUP);
  pinMode(WIO_5S_PRESS, INPUT_PULLUP);

  //
  tft.init(); // initialize the TFT screen
  tft.setRotation(3);
  tft.fillScreen(TFT_BLACK);

  // Wifi
  setupScan();

  // MQTT Setup
  client.setServer(server, 1883);
  client.setCallback(Callback);

  if (client.connect("WioID"))
  {
    Serial.println("Connection has been established");

    // topics we can subscribe to and do actions via callback function
    client.subscribe(GetPasscodeFromClient);
    client.subscribe(AlarmTopic);
  }
  else
  {
    Serial.println("You did not set up the Mqtt connection correctly");
  }
}

void keypadauthloop()
{ // Read joystick values
  int joystickUp = digitalRead(WIO_5S_UP);
  int joystickDown = digitalRead(WIO_5S_DOWN);
  int joystickLeft = digitalRead(WIO_5S_LEFT);
  int joystickRight = digitalRead(WIO_5S_RIGHT);
  int joystickPress = digitalRead(WIO_5S_PRESS);

  int newRow = currentRow;
  int newCol = currentCol;
  if (joystickUp == LOW)
  {
    newRow = (currentRow - 1 + NUM_ROWS) % NUM_ROWS;
  }
  else if (joystickDown == LOW)
  {
    newRow = (currentRow + 1) % NUM_ROWS;
  }
  else if (joystickLeft == LOW)
  {
    newCol = (currentCol - 1 + NUM_COLS) % NUM_COLS;
  }
  else if (joystickRight == LOW)
  {
    newCol = (currentCol + 1) % NUM_COLS;
  }

  // Update the current rectangle
  currentRow = newRow;
  currentCol = newCol;

  // handle the UI For rectangles depending on what state they are in.
  if (isInputting)
  {
    for (int row = 0; row < NUM_ROWS; row++)
    {
      for (int col = 0; col < NUM_COLS; col++)
      {
        // Deal with all rectangles that have been pressed
        if (pressedRectangles[row][col])
        {
          tft.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, 0x8410);
          tft.setTextColor(TFT_BLACK);
          tft.setTextSize(3);
          tft.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
          tft.print(rectangleNumber[row][col]);
        }
        // deal with the current active rectangle
        else if (row == currentRow && col == currentCol)
        {
          tft.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, 0xF81F);
          tft.setTextColor(TFT_BLACK);
          tft.setTextSize(3);
          tft.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
          tft.print(rectangleNumber[row][col]);
        }
        // deal with the rest
        else
        {
          tft.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, TFT_WHITE);
          tft.setTextColor(TFT_BLACK);
          tft.setTextSize(3);
          tft.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
          tft.print(rectangleNumber[row][col]);
        }
      }
    }
  }

  // when joystick is pressed down we should mark that rectangle in the color green
  if (joystickPress == LOW)
  {
    if (!pressedRectangles[currentRow][currentCol])
    { // Check if the rectangle hasn't already been marked
      pressedRectangles[currentRow][currentCol] = true;
      userInputString[userInputCount++] = '0' + rectangleNumber[currentRow][currentCol];

      if (userInputCount >= MAX_INPUT_LENGTH)
      {
        isInputting = false; // Stop inputting when maximum length is reached
      }
    }
  }

  // check if keyword is correct TODO

  if (userInputCount == answerString.length())
  {

    bool isCorrect = checkAnswer();
    if (isCorrect)
    {

      AcessGrantedScreen();
      client.publish(AlarmTopic, "AlarmOff");

      delay(2000);
      initAuth = false;
      isInputting = false;
    }
    else
    {
      AcessDeniedScreen();
      delay(2000);
      initializeGrid();
    }
  }
  delay(250);
}

bool checkAnswer()
{
  return (strcmp(userInputString, answerString.c_str()) == 0);
}

// SCREEN UI  //////////////////////
void ShowHomeScreen()
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
  client.publish(AlarmTopic, "AlarmIntruder");
}

void AcessGrantedScreen()
{
  tft.fillScreen(TFT_BLACK);   // set the background color to black
  tft.setTextColor(TFT_GREEN); // set the text color to green
  tft.setTextSize(3);          // set the text size to 3
  tft.setCursor((tft.width() - tft.textWidth("Access Granted")) / 2, tft.height() / 2 - 30);
  tft.println("Access Granted"); // print the text
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
///////////////////////////////////////////////

void loop()
{
  client.loop();

  if (!answerString.length() == 0)
  {
    setAnswerString(answerString);
  }

  if (alarmOn)
  {
    ShowAlarmHuntScreen();

    delay(3000);
    if (digitalRead(PIR_MOTION_SENSOR))
    {
      ShowIntruderScreen();
      Serial.println("intruder found");
      delay(2000);
      // turn on auth since intruder is found
      initAuth = true;
      // set up the grid for keypad
      initializeGrid();
      alarmOn = false;
    }
    else
    {
      Serial.println("Watching");

      delay(200);
    }
  }

  if (initAuth)
  {
    // loop keypad auth

    Serial.println(answerString);
    keypadauthloop();
  }

  if (!initAuth && !alarmOn)
  {
    ShowHomeScreen();
    delay(2000);
  }
}
