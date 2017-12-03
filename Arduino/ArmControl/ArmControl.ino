#include <Servo.h>

Servo axis;
int Position = 0;
int Angle = 0;
int Direction = 0;
int ServoMax = 180;
int ServoPin = 2;
byte readMe[2] = {0,0};

void setup() {
  Serial.begin(115200);
  axis.attach(ServoPin);
}

void loop() {
  if (Serial.available() > 0){
    Serial.readBytes(readMe, 2);
    Direction = (int)readMe[0];
    Angle = (int)readMe[1];
    while(Serial.read() != 13);
    if (Direction == 0 && Position >= Angle){
      Position -= Angle;
      axis.write(Position);
      String output = "Moved -" + (String)Angle + " degrees";
      Serial.println(output);
    }else if (Direction == 1 && Position <= ServoMax-Angle){
      Position += Angle;
      axis.write(Position);
      String output = "Moved +" + (String)Angle + " degrees";
      Serial.println(output);
    }else if(Direction == 3){
      Serial.println("Reset Servo Position");
      axis.write(0);
      Position=0;
    }else{
      Serial.println("Serial data not valid.");
      Serial.println(Direction);
      Serial.println(Angle);
    }
    String Status = "Current Angle: " + (String)Position;
    Serial.println(Status);
  }
}
