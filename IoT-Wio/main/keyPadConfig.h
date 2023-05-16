// all constants used in the keypad program
const int RECTANGLE_WIDTH = 50;
const int RECTANGLE_HEIGHT = 50;
const int RECTANGLE_SPACING = 10;
const int GRID_TOP_MARGIN = 40;
const int GRID_LEFT_MARGIN = 40;
const int NUM_ROWS = 3;
const int NUM_COLS = 3;
// 2D array to store the rectangle positions
int rectangleX[NUM_ROWS][NUM_COLS];
int rectangleY[NUM_ROWS][NUM_COLS];
int rectangleNumber[NUM_ROWS][NUM_COLS] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
// track current coordinate
int currentRow = 0;
int currentCol = 0;
// track every pressed button
bool pressedRectangles[NUM_ROWS][NUM_COLS] = {0};
// user input store
int userInputCount = 0;

const int MAX_INPUT_LENGTH = 9;
char userInputString[MAX_INPUT_LENGTH + 1] = "";

bool isInputting = true;
// keyword answer store, we will replace this with the sent passcode.
String answerString = "123"; //doesnt matter what we put here since it will be changed when app connects. In a better world where we had an sd card we could have persistent storage for this :(
