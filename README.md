# Kinect Serial API
An API to operate a serial-controller robotic arm via a Xbox 360 Kinect Sensor using Java.

## Usage:
The uses the location of the user's right elbow and wrist joints, provided by the J4K library, and calculates the angle between those two using the cartesian math. This requires the use of the Java Math libraries for atan.
```java
angle = (float)Math.atan((elbowPoint[1] - wristPoint[1])/(elbowPoint[0] - wristPoint[0]));
```
Using this angle, serial output is sent to the JSSC API to be handled and sent to the motors.

If you wish to create your own program using the angle of the forearm you can use the equation below to get the real value of the angle outputted by the API.
```java 
realAngle = (int)((angle/1.5)*100); 
```
## External Libraries and References:
- [J4K Libraries](http://research.dwi.ufl.edu/ufdw/download.php) - connecting to the Kinect input stream
- [JSSC](https://github.com/scream3r/java-simple-serial-connector/releases) - sending serial output to the motors

## About:
Authors: Austin Monson, Ed Greene, and Anthony Catricala 

Made for SUNY Polytechnic Institute, CS490 - Portable NUI Development, taught by William "Amos" Confer.
## License:
```kinectSerialAPI``` uses the [GNU General Public License v2.0](https://www.gnu.org/licenses/gpl-2.0.html#SEC4), see the LICENSE file for more information
