# SeeedSentinel

![alt text](https://i.imgur.com/nvrCLK7.png)

## ![alt text](https://i.imgur.com/nnc05xy.png) What is SeeedSentinel?

The SeeedSentinel is a security system designed and created by group 4 of DIT113-2023 at the University of Gothenburg.
The project will provide an added layer of protection for homes or businesses by detecting potential intrusions and 
sending alerts to the homeowner or business owner. The system uses a variety of sensors to be able to warn the security
system owners of an intrusion, and an android app to be able to control the system.


## What is the purpose of the project?

Our project aims to create a security system using various sensors including an infrared motion sensor, a buzzer, an LED, 
a microphone, and a joystick. The system is designed to provide a layer of security for homes or businesses, allowing 
individuals to feel more secure and protected from potential burglaries or break-ins. 
When the security system is activated, the person that activated it has a certain amount of time to deactivate the alarm 
on the WIO terminal device before "Intruder mode" is initiated. The alarm can be deactivated by entering a joystick swipe 
combination or saying a keyword that the built-in WIO terminal microphone detects. During "Intruder mode" the system will 
send an automated message to the homeowner, alerting them of the intrusion. The homeowner can then react accordingly,
such as calling the police or turning the system off from the mobile phone app in case of a false alarm.

## Key functionalities

- Activate or deactivate alarm through an android app
- Deactivate alarm using WIO terminal sensors by entering a joystick combination or saying keyword
- Change notification settings in android app
- Stores password safely in a database

## Diagrams and documents

- [Component diagram](https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector/-/wikis/uploads/ba0a6d5df55323925547d3189a61e5c1/Blank_diagram_-_Component_Diagram__6_.png/)
- [Wiki](https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector/-/wikis/SeeedSentinel/)


## How is the system supposed to work?

To handle communication between the UI and the SeedSentinel device, we will use HiveMQ, a MQTT broker.
The SeedSentinel gadget is made up of Wio terminal and external sensors as PIR, buzzer, and LED. The Passive Infrared 
Sensor (PIR), which is the main component, activates the alarm via Wio once it detects an object that emits heat such as
human body within a range of (2 – 5m).
Once an object is detected by the PIR the Wio sends a signal to turn on the LED and the buzzer, which start making a loud 
buzzing noise to frighten the intruder away.
When an alarm is detected, the owner will be notified through SMS. When entering a password, the database will be used 
to store user credentials for convenience.



## ![alt text](https://i.imgur.com/GBdgh4z.png) Technologies used

- C++
- Java/Kotlin
- Arduino IDE
- Android Studio
- HiveMQ 
- Wio Seed Terminal with built-in sensors
- External sensors such as (Grove-Mini PIR motion sensor,Grove - RGB LED Stick (10 WS2813 Mini),Grove-Buzzer)
- Speech-to-text WIO Terminal library
- MongoDB


## ![alt text](https://i.imgur.com/S0Q1MxJ.png) Team Members

- [ ] Johan Sandgren (gussanjoba@student.gu.se)
- [ ] Stefan Ingimarsson (gusingist@student.gu.se)
- [ ] Feride Hansson (gusferiha@student.gu.se)
- [ ] Alexander Helsing (gushelsial@student.gu.se)
- [ ] Deba Arif Mohammed (gusmohamde@student.gu.se)
- [ ] Henrik Andren (gusandrhe@student.gu.se)
