#include "MotionDetection.h"


MotionDetect motion;

void setup() {
  motion.setupMotion();
  // String[]/Int[] KeywordAttempt = patternRecorder.peekAtStorage(); Use this in another class for comparing i guess
}

void loop() {
  motion.loopMotion();
}
