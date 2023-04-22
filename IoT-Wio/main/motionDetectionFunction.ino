/*code for PIR to detect motion and to turn on the buzzer*/
#define PIR_MOTION_SENSOR 2//Use pin 2 on Wio
#define BUZZER 3//Use pin 3 on Wio


void setup()
{
    pinMode(PIR_MOTION_SENSOR, INPUT);
    pinMode(BUZZER, OUTPUT); 

}

void loop()
{
    if(digitalRead(PIR_MOTION_SENSOR))//to detect motion
       { Serial.println("Warning, INTRUDOR");
        digitalWrite(BUZZER,HIGH);
        delay(100);
       }
    else
        {Serial.println("Watching");
        digitalWrite(BUZZER,LOW);
        delay(100);}
}

 

