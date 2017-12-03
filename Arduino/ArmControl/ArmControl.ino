#include <Servo.h>

Servo axis;
int Position = 0;
int Angle = 0;
int Direction = 0;
int ServoMax = 180;
int ServoPin = 2;
byte readMe[1] = {0};

void setup() {
  Serial.begin(115200);
  axis.attach(ServoPin);
}

void loop() {
  if (Serial.available() > 0){
    Serial.readBytes(readMe, 1);
    Angle = (int)readMe[1];
    while(Serial.read() != 13);
    if (Angle >= 0 && Angle <= 180){
      axis.write(Angle);
      Serial.println("Changed angle to " + Angle);
    }else{
      Serial.println("Serial data not valid.");
      Serial.println(Direction);
      Serial.println(Angle);
    }
    String Status = "Current Angle: " + (String)Position;
    Serial.println(Status);
  }
}
