# Thief Detector



## Introduction

The thief detector project is a security system designed and created by the Group 4 of DIT113-2023 of the University of Gothenburg to provide an added layer of protection for homes or 
businesses by detecting potential intrusions and sending alerts to the homeowner or business owner. 
The system uses a variety of sensors, including a PIR sensor, a speaker, an LED, a microphone, a joystick, and 
a keypad sign-in to detect any unauthorized entry. When the door is opened, the person has a certain amount of time to 
deactivate the alarm on the WIO (Wireless Input/Output) device before "Intruder mode" is initiated. 
During "Intruder mode," the system will send an automated message to the homeowner or business owner, 
alerting them to the potential intrusion.

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

The project also requires various libraries to be installed in the Arduino IDE. These libraries include the PIR library,
Keypad library, and Adafruit_SSD1306 library, among others. The installation of these libraries can be done using the 
Arduino IDE's library manager.

It is advised to use a version control system (VCS) such as Git to manage the project's source code. 
The developers used Git and hosted the project on GitHub[2]. Collaborators and contributors can clone the project's 
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
Visual Studio Code or any other IDE that supports Arduino development.


