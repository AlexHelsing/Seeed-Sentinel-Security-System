# SeeedSentinel

![alt text](https://i.imgur.com/o3uBdQp.png)




## ![alt text](https://i.imgur.com/nnc05xy.png) What is SeeedSentinel?

The SeeedSentinel is a security system designed and created by group 4 of DIT113-2023 at the University of Gothenburg.
The project will provide an added layer of protection for homes or businesses by detecting potential intrusions and 
sending alerts to the homeowner or business owner. The system uses a variety of sensors to be able to warn the security
system owners of an intrusion, and an android app to be able to control the system.


## What is the purpose of the project?

Our project aims to create a security system using various sensors including an infrared motion sensor,sound sensor, a buzzer, 
and an internal joystick. The system is designed to provide a layer of security for homes or businesses, allowing 
individuals to feel more secure and protected from burglaries or break-ins. 
When the security system is activated, the person that activated it has a certain amount of time to deactivate the alarm 
on the WIO terminal device before "Intruder mode" is initiated. The alarm can be deactivated by entering a numerical password on the terminal.
When "Intruder mode" is initiated the system will send an automated message to the homeowners phone, alerting them of the intrusion.
The homeowner can then react accordingly, such as calling the police or turning the alarm off from the mobile phone
app in case of a false alarm.

## Key functionalities

- Security system which uses infrared motion or sound sensors to detect intruders
- Notifies owner of alarm triggers through android application
- Activate or deactivate alarm through an android app
- Deactivate alarm using WIO terminal sensors by entering a numerical password with the built in joystick
- Change the wio terminal password in android app
- Stores user information and password safely in a database


## Developer Guide
Please refer to the [Developer guide](https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector/-/wikis/Developer-guide) for getting started with the project.

## Diagrams and documents

- [Diagrams](https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector/-/wikis/Diagrams)

## How is the system supposed to work?

To handle communication between the client and the SeedSentinel device, we will use an MQTT broker.
The SeedSentinel gadget is made up of a Wio terminal and external sensors as PIR sensor, sound sensor and a buzzer. The Passive Infrared 
Sensor (PIR), which is the main component, activates the alarm via Wio once it detects an object that emits heat such as a
human body.
Once an object is detected by the PIR the Wio sends a signal to turn on the buzzer, which start making a loud 
buzzing noise to signal that the alarm is activated. The alarm can also be activated if the sound sensor picks up loud noises.
When the alarm is activated, the owner will be notified in the android app, and can choose to turn off the alarm either through the app or by entering a password using the joystick on the Wio terminal. Users can choose their own passwords, and they will be stored safely in a database.



## ![alt text](https://i.imgur.com/GBdgh4z.png) Technologies used

- C++
- Java
- Arduino IDE
- Android Studio
- Eclipse Paho MQTT 
- Wio Seed Terminal with built-in sensors
- External sensors such as a PIR infrared motion sensor, buzzer, sound sensor.
- MongoDB


## ![alt text](https://i.imgur.com/S0Q1MxJ.png) Team Members

- [ ] Johan Sandgren (gussanjoba@student.gu.se)
- [ ] Stefan Ingimarsson (gusingist@student.gu.se)
- [ ] Feride Hansson (gusferiha@student.gu.se)
- [ ] Alexander Helsing (gushelsial@student.gu.se)
- [ ] Deba Arif Mohammed (gusmohamde@student.gu.se)
- [ ] Henrik Andren (gusandrhe@student.gu.se)

## Group contributions

[Team member contributions](https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector/-/wikis/Team-member-contributions)
