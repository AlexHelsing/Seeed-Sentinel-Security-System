# Thief Detector



## Introduction

The thief detector project is a security system designed and created by the Group 4 of DIT113-2023 of the University of Gothenburg to provide an added layer of protection for homes or 
businesses by detecting potential intrusions and sending alerts to the homeowner or business owner. 
The system uses a variety of sensors, including a PIR sensor, a speaker, an LED, a microphone, a joystick, and 
a keypad sign-in to detect any unauthorized entry. When the door is opened, the person has a certain amount of time to 
deactivate the alarm on the WIO (Wireless Input/Output) device before "Intruder mode" is initiated. 
During "Intruder mode," the system will send an automated message to the homeowner or business owner, 
alerting them to the potential intrusion.
Team Members:

Johan Sandgren (gussanjoba@student.gu.se)
Stefan Ingimarsson (gusingist@student.gu.se)
Feride Hansson (gusferiha@student.gu.se)
Alexander Helsing (gushelsial@student.gu.se)
Deba Arif Mohammed (gusmohamde@student.gu.se)
Henrik Andren (gusandrhe@student.gu.se)
Project Title: Thief Detection System

Description:
Our project aims to create a thief detection system using various sensors, including a PIR sensor, a speaker, an LED, a microphone, a joystick, and a keypad sign-in. The system is designed to provide an added layer of security for homes or businesses, allowing individuals to feel more secure and protected from potential burglaries or break-ins. When the door is opened, the person has a certain amount of time to deactivate the alarm on the WIO (Wireless Input/Output) device before "Intruder mode" is initiated. During "Intruder mode," the system will send an automated message to the homeowner, alerting them to the potential intrusion. The homeowner can then react accordingly, such as calling the police or checking their home security cameras.

Requirements:

PIR sensor
Speaker
LED
Microphone
Joystick
Keypad sign-in
Wireless Input/Output (WIO) device
Arduino board
C++ programming language
Java programming language
Trello board for task management
Discord for communication
Weekly meetings every Wednesday from 1:00 PM to 3:00 PM at Lindholmen
Team members to inform the group of their schedule in advance, including their working hours
Shared leadership role among team members
Team Contract:

All team members agreed that main communication platform is Discord. Weekly meetings will be held every Wednesday from 1:00 PM to 3:00 PM at Lindholmen to discuss project progress and address any issues or concerns. In the event of any circumstances that require additional or emergency meetings, we will decide on an appropriate time together during our weekly meetings or through the primary communication channel. Furthermore, our TA meetings will occur every Wednesday from 1:00 PM to 3:00 PM at Lindholmen.

In order to keep everyone on the same page and manage our time effectively, it was agreed that team members should inform the group of their schedule in advance, including their working hours. The weekly group meeting will be held on Wednesdays, and all team members are expected to attend.

Each team member will be assigned specific tasks and responsibilities for the project, with predetermined deadlines. It is the responsibility of each team member to complete their assigned tasks and work cooperatively with others to accomplish the project's objectives. The distribution of tasks and providing weekly updates on our individual progress will be done during meetings. It is expected that all team members will come prepared to contribute.

To manage our project effectively, we decided to share the leadership role.

## Installation
To install the thief detector project, you will need to have the following components:

Arduino IDE: This is the development environment for programming the Arduino board.
WIO Node board: This is the wireless input/output board that will be used to communicate with the sensors and 
activate the alarm.
PIR sensor, speaker, LED, microphone, joystick, and keypad: These are the various sensors that will be used 
to detect intruders and alert the user.


```
git@git.chalmers.se:courses/dit113/2023/group-4/thief-detector.git
https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector.git
```

## Project Structure, Dependencies and Requirements

Project Structure, Dependencies and Requirements:

The thief detector project is developed using the Arduino platform, and the source code contains the configuration 
of the Arduino IDE. The project uses various sensors, including a PIR sensor, a speaker, an LED, a microphone, 
a joystick, and a keypad sign-in.
To set up the project, the user needs to have a proper installation of the Arduino IDE on their machine. 
The Arduino IDE can be downloaded from the official website[1], and it is recommended to use the latest version. 
Moreover, the user needs to have an Arduino board that supports the necessary sensors and components.
- [ ] The user shall be able to set a keyword phrase for turning off the alarm.
- [ ] The user shall be able to set a joystick pattern for turning off the alarm
- [ ] The user shall be able to turn off the alarm using joystick And/Or saying the keyword phrase
- [ ] The user shall be notified when alarm is triggered and intruder is in house or some shit.
- [ ] The user shall be able to set trigger actions that occur when intruder triggers alarm
- [ ] The user shall be able to modify settings (change keyword, change pattern)


The project also requires various libraries to be installed in the Arduino IDE. These libraries include the PIR library,
Keypad library, and Adafruit_SSD1306 library, among others. The installation of these libraries can be done using the 
Arduino IDE's library manager.

It is advised to use a version control system (VCS) such as Git to manage the project's source code. 
The developers used Git. Collaborators and contributors can clone the project's 
repository to their local machine using the following command:

```
git@git.chalmers.se:courses/dit113/2023/group-4/thief-detector.git
https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector.git
```


Furthermore, the project's developers used  IntelliJ IDEA as their integrated development environment (IDE) 
for building and running the project. It is recommended to use the same IDE or any other IDE that supports 
Arduino development.

In summary, to set up the thief detector project, the user needs to have the following requirements:
Arduino IDE installed,
Arduino board that supports necessary sensors and components,
required libraries installed in Arduino IDE,
Git installed,
IntelliJ IDEA or any other IDE that supports Arduino development.



