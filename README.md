# FRILLER
The FRILLER prototype is a compact robot that can change the radius of its wheels to overcome obstacles. It can be used to explore extreme terrains and reach targets of interest. The wheel deformation mechanism is composed of two DC geared motors, sliding racks, and an elastic cord. When the motors push out the racks, the wheel diameter becomes larger. The elastic cord around the wheels assists with the return of the petals to the close position when the motors pull the racks back inside. The wheels remain round for faster travel, but transform into spikes to overcome obstacles.

On a level dirt path, FRILLER can drive about 2000 feet (610 meters) on one battery charge. That could fluctuate a bit depending on how much any onboard instruments are used though. Besides desert conditions, FRILLER has also been outfitted for snow. Its expanded wheels and flat fishtail allows it to traverse wintry terrain.

[![demo](/images/FRILLER.gif)]()

## How it works
FRILLER employees an Raspberry Pi 3b running the Android Things embedded OS platforml which receives command via WiFi to control the motors though the Adafruit motor hat. 

![fritzing](/images/FRILLER.jpg)
For testing purposes, it was easy to use the TouchOSC app to send UDP packets to the robot; which was also cheaper than the traditional hobby RC.  However, you will have to setup your Android device as an access point (hotspot) so the FRILLER can connect to it; which is needed for field tests.

![touchosc](/images/TouchOSC.png)
### What you'll need

- [Android Studio 3.0+](https://developer.android.com/studio/index.html).
- Flash Android Things on the Raspberry Pi 3 (instructions [here](https://developer.android.com/things/hardware/raspberrypi.html)).
- The following individual components:

Part             | Qty 
---------------- | ----
[Raspberry Pi 3 Model B](https://www.adafruit.com/product/3055)<br /> | 1 
[DC & Stepper Motor HAT](https://www.adafruit.com/product/2348)<br /> | 1 
[Extra Tall Header](https://www.adafruit.com/product/1979)<br /> | 1
[Micro-B USB DIY Connector](https://www.adafruit.com/product/1390)<br /> | 1
[Brushed DC Gearmotor](https://www.pololu.com/product/1104)<br /> | 2 
[Solarbotics Geamotor 90deg Output](https://www.pololu.com/product/181)<br /> | 2 
[Voltage and Current Sense Breakout](https://www.sparkfun.com/products/9028)<br />*or similar* | 1 
[Lithium Ion batter](https://www.gettitanpower.com/pages/3-5ah-11-1v-60w-endurance)<br /> | 1
[XT60 Connector](https://www.pololu.com/product/2158)<br /> | 1
