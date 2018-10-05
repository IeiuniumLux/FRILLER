# FRILLER
FRILLER (FRILL Explorer Robot) is a 3D printed compact robot that changes the radius of its wheels to overcome obstacles. It can be used to explore extreme terrains and reach targets of interest. On a level dirt path, FRILLER can drive about 2000 feet (610 meters) on one battery charge. That could fluctuate a bit depending on how much any onboard instruments or sensor are used. Its expanded wheels and flat fishtail allows it to traverse desert or snow terrains.

[![demo](/images/FRILLER.gif)](https://youtu.be/IRr8w6uVp10)

## How it works
FRILLER employees a [Raspberry Pi 3b](https://pinout.xyz/) running the [Android Things embedded OS platform](https://developer.android.com/things/); which receives command via WiFi to control the motors though the Adafruit motor hat. The wheel deformation mechanism is composed of two DC geared motors, sliding racks, and an elastic cord. When the motors push out the racks, the wheel diameter becomes larger. The elastic cord around the wheels assists with the return of the spikes to the close position when the motors pull the racks back inside. The wheels remain round for faster travel on roads or indoors, but transform into spikes to overcome obstacles off-road.

![fritzing](/images/FRILLER.jpg)
For testing purposes, it was easy to use the [TouchOSC app](https://play.google.com/store/apps/details?id=net.hexler.touchosc_a&hl=en_US) to send UDP packets to the robot; which was also cheaper than the traditional hobby RC.  However, you will have to setup your Android device as an access point (hotspot) so the FRILLER can connect to it; which is useful for field tests.

![touchosc](/images/TouchOSC.png)
### What you'll need

- [Android Studio 3.0+](https://developer.android.com/studio/index.html).
- Flash Android Things on the Raspberry Pi 3 (instructions [here](https://developer.android.com/things/hardware/raspberrypi.html)).
- [TouchOSC](https://play.google.com/store/apps/details?id=net.hexler.touchosc_a&hl=en_US).
- The following individual components:

Part             | Qty 
---------------- | ----
[Raspberry Pi 3 Model B](https://www.adafruit.com/product/3055)<br /> | 1 
[DC & Stepper Motor HAT](https://www.adafruit.com/product/2348)<br /> | 1 
[Extra Tall Header](https://www.adafruit.com/product/1979)<br /> | 1
[3mm Pegs (package of 100)](https://www.amazon.com/gp/product/B00B3MFWY8/ref=ox_sc_act_title_1?smid=ATVPDKIKX0DER&psc=1)<br /> | 1
[Micro-B USB DIY Connector](https://www.adafruit.com/product/1390)<br /> | 1
[50:1 Brushed DC Gearmotor](https://www.pololu.com/product/1104)<br /> | 2 
[Solarbotics Geamotor 90deg Output](https://www.pololu.com/product/181)<br /> | 2 
[Voltage and Current Sense Breakout](https://www.sparkfun.com/products/9028)<br />*or similar* | 1 
[Lithium Ion batter](https://www.gettitanpower.com/pages/3-5ah-11-1v-60w-endurance)<br /> | 1
[XT60 Connector](https://www.pololu.com/product/2158)<br /> | 1
[Vytaflex-60](https://shop.smooth-on.com/vytaflex-60)<br /> | 1

## Improvements
- Make FRILLER smarter. Right now, it runs off WiFi and can be controlled remotely, but I'd like to add autonomy.
- Add GPS and compass sensors for position control.
- Replace the 3D printed body with a lighter and flexible structure; capable to resist high temperatures and handle impacts from a fall.

## Acknowledgement
The FRILLER prototype is based on former OSU student Carter Hurd's design.

## MagPi Magazine (English & Simplified Chinese)
>[FRILLER Article](https://github.com/TommyZihao/MagPi_Chinese/blob/master/MagPi74_14-15%E5%9F%BA%E4%BA%8E%E6%A0%91%E8%8E%93%E6%B4%BE%E7%9A%84FRILLER%E5%A4%9A%E5%9C%B0%E5%BD%A2%E6%9C%BA%E5%99%A8%E4%BA%BA.md)

## References
- [Differential-Drive Trackers](https://www.coursera.org/lecture/mobile-robot/differential-drive-trackers-NORKS)
- [Systems and Methods for Mobile Robot Positioning](http://www-personal.umich.edu/~johannb/Papers/pos96rep.pdf)
- [NASA Game On](https://gameon.nasa.gov/)

License
-------

Copyright 2018 Al Bencomo

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
