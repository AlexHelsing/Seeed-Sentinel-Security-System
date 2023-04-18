#include "JoyStickRecorder.h"


JoyStickRecorder patternRecorder;

void setup() {
  patternRecorder.setup();
  // String[]/Int[] KeywordAttempt = patternRecorder.peekAtStorage(); Use this in another class for comparing i guess
}

void loop() {
  patternRecorder.loop();
}
