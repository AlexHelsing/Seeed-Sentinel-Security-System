#include <PubSubClient.h>
#include <rpcWiFi.h>
#include <TFT_eSPI.h>
#include "rpcWiFi.h"
#include "config.h" // config file containing credentials, replace with your own
#include "keyPadConfig.h"
#include "UIScreens.h"

// Pins for sensors
#define PIR_MOTION_SENSOR D0 // Motion sensor pin
#define BUZZER D4 
#define SOUND_SENSOR D2

// WIFI Credentials
const char *ssid = SSID;
const char *password = WIFI_PASSWORD;
const char *server = Broker_IP;

// MQTT TOPICS
const char *AlarmTopic = "/SeeedSentinel/AlarmOnOff";
const char *GetPasscodeFromClient = "/SeeedSentinel/GetPasscodeFromClient";
const char *GetUserProfile = "/SeeedSentinel/GetUserProfile";

// UI instance so we can use the tft library
TFT_eSPI tftinstance;
UIScreens uiScreens(tftinstance);
// WIfi client and PubSubClient instance
WiFiClient wioClient;
PubSubClient client(wioClient);

// alarm state
bool alarmOn = false;
bool initAuth = false;

String username = "..."; // doesnt matter what we put here since it will be changed when app connects. In a better world where we had an sd card we could have persistent storage for this :(


int SOUND_THRESHOLD = 500;

// Different screens statuses
bool huntScreenStatus = false;
bool welcomeScreenStatus = false;

unsigned long timerStart = 0;
bool timerRunning = false;
const unsigned long TIMER_DURATION = 30000;  // 30 seconds in milliseconds
// only used to make timer work correctly with all the re-initializations of the grid/keypad.
int attemptCount = 0;

bool sentPub = false;


// Callback function where all the incoming topic subscriptions are handled.
void Callback(char *topic, byte *payload, unsigned int length)
{
  if (strcmp(topic, AlarmTopic) == 0)
  {
    // check the incoming message if its alarmOn or AlarmOff
    if (strncmp((char *)payload, "AlarmOn", length) == 0)
    {
      Serial.println("Activate motion detection");

      uiScreens.ShowAlarmLoadingScreen();

      delay(1000);
      // turn on alarm
      attemptCount = 0; // not sure where else to put this rn but it works :)
      alarmOn = true;
    }
    else if (strncmp((char *)payload, "AlarmOff", length) == 0)
    {
      Serial.println("Turn off motion detection");
      // handle the AlarmOff message here .
      welcomeScreenStatus = false;
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
  } else if (strcmp(topic, GetUserProfile) == 0) {
    String usernameTemp = "";
    for (int i = 0; i < length; i++)
    {
      usernameTemp += (char)payload[i];
    }
     setUserName(usernameTemp);
  }
}
// set the answerString
void setAnswerString(const String &newAnswerString)
{
  answerString = newAnswerString;
}

// set the username
void setUserName(const String &newUserName)
{
  username = newUserName;
}

void setupScan()
{

  // Set WiFi to station mode and disconnect from an AP if it was previously connected
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();

  Serial.println("Connecting to WiFi..");
  tftinstance.setTextSize(2);
  tftinstance.setCursor(0, 0);
  tftinstance.setTextColor(TFT_WHITE);
  tftinstance.println("Connecting to WiFi...");
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
    if (attemptCount == 0) {
    timerRunning = true;  // Start the timer
    timerStart = millis();  // Store the current time
   }
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

  tftinstance.setRotation(3);
  tftinstance.fillScreen(TFT_BLACK); // Set background color
  

  // Initialize the rectangle positions
  int gridWidth = NUM_COLS * RECTANGLE_WIDTH + (NUM_COLS - 1) * RECTANGLE_SPACING;
  int gridHeight = NUM_ROWS * RECTANGLE_HEIGHT + (NUM_ROWS - 1) * RECTANGLE_SPACING;
  int gridX = (tftinstance.width() - gridWidth) / 2;
  int gridY = (tftinstance.height() - gridHeight) / 2;
  for (int row = 0; row < NUM_ROWS; row++)
  {
    for (int col = 0; col < NUM_COLS; col++)
    {
      rectangleX[row][col] = gridX + (RECTANGLE_WIDTH + RECTANGLE_SPACING) * col;
      rectangleY[row][col] = gridY + (RECTANGLE_HEIGHT + RECTANGLE_SPACING) * row;
      tftinstance.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, TFT_WHITE);
      tftinstance.setTextColor(TFT_BLACK);
      tftinstance.setTextSize(3);
      tftinstance.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
      tftinstance.print(rectangleNumber[row][col]);
    }
  }
}

void setup()
{
  Serial.begin(115200);

  // PINS
  pinMode(PIR_MOTION_SENSOR, INPUT);
  pinMode(BUZZER, INPUT);
  pinMode(SOUND_SENSOR, INPUT);
  pinMode(WIO_5S_UP, INPUT_PULLUP);
  pinMode(WIO_5S_DOWN, INPUT_PULLUP);
  pinMode(WIO_5S_LEFT, INPUT_PULLUP);
  pinMode(WIO_5S_RIGHT, INPUT_PULLUP);
  pinMode(WIO_5S_PRESS, INPUT_PULLUP);

  //
  uiScreens.setup();

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
    client.subscribe(GetUserProfile);
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
          tftinstance.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, 0x8410);
          tftinstance.setTextColor(TFT_BLACK);
          tftinstance.setTextSize(3);
          tftinstance.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
          tftinstance.print(rectangleNumber[row][col]);
        }
        // deal with the current active rectangle
        else if (row == currentRow && col == currentCol)
        {
          tftinstance.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, 0xF81F);
          tftinstance.setTextColor(TFT_BLACK);
          tftinstance.setTextSize(3);
          tftinstance.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
          tftinstance.print(rectangleNumber[row][col]);
        }
        // deal with the rest
        else
        {
          tftinstance.fillRect(rectangleX[row][col], rectangleY[row][col], RECTANGLE_WIDTH, RECTANGLE_HEIGHT, TFT_WHITE);
          tftinstance.setTextColor(TFT_BLACK);
          tftinstance.setTextSize(3);
          tftinstance.setCursor(rectangleX[row][col] + RECTANGLE_WIDTH / 2 - 10, rectangleY[row][col] + RECTANGLE_HEIGHT / 2 - 10);
          tftinstance.print(rectangleNumber[row][col]);
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

      uiScreens.AcessGrantedScreen(username);
      client.publish(AlarmTopic, "AlarmOff");
      
      delay(1000);
      timerRunning = false;
      sentPub = false;
      initAuth = false;
      isInputting = false;
    }
    else
    {
      attemptCount++;
      uiScreens.AcessDeniedScreen();
      delay(2000);
      initializeGrid();
    }
  }
  delay(100);
}

bool isTimerElapsed()
{
  if (timerRunning)
  {
    unsigned long currentTime = millis();
    unsigned long elapsedTime = currentTime - timerStart;
    if (elapsedTime >= TIMER_DURATION)
    {
      return true;
    }
  }
  return false;
}



bool checkAnswer()
{
  return (strcmp(userInputString, answerString.c_str()) == 0);
}

void loop()
{
  client.loop();


  if (!answerString.length() == 0)
  {
    setAnswerString(answerString);
  }
  if (!username.length() == 0)
  {
    setUserName(username);
  }

  if (alarmOn)
  {
    if(huntScreenStatus == false){
      uiScreens.ShowAlarmHuntScreen();
      huntScreenStatus = true;
    }
    else{
    int soundLevel = analogRead(SOUND_SENSOR);
    Serial.println(soundLevel);
    delay(50);
    if (digitalRead(PIR_MOTION_SENSOR) || soundLevel > SOUND_THRESHOLD)
    {
      huntScreenStatus = false;
      uiScreens.ShowIntruderScreen();
      Serial.println("intruder found");
      delay(2000);
      // turn on auth since intruder is found
      initAuth = true;
      // set up the grid for keypad
      initializeGrid();
      alarmOn = false;
    }
  }}

  if (initAuth)
  {
    welcomeScreenStatus = false;
    // loop keypad auth
    keypadauthloop();

    // if 30 secs or whatever we have set passess, trigger some actions ie notice
    if (isTimerElapsed()) {
      if (sentPub == false) {
        Serial.println("publish alarmIntuder");
        client.publish(AlarmTopic, "AlarmIntruder");
        sentPub = true;
      }
      digitalWrite(BUZZER, HIGH);
      delay(50);
      digitalWrite(BUZZER, LOW);
      timerStart = 0;
    }
  }

  if (!initAuth && !alarmOn)
  {
    if(welcomeScreenStatus == false){
      uiScreens.SHOWHOMESCREEN();
      welcomeScreenStatus = true;
    }
    else{
      delay(50);
    }

  }
  }
