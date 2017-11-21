#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <Servo.h>

LiquidCrystal_I2C disp(0x27,20,4);
Servo axis;
int Position = 0;
int ServoMax = 180;
int ServoPin = 2;

void setup() {
  Serial.begin(9600);
  disp.init();
  disp.backlight();
  axis.attach(ServoPin);
}

void loop() {
  while(Serial.available() > 0){
    disp.clear();
    disp.setCursor(0,0);
    disp.println("Recieved Serial Data");
    int Direction = Serial.parseInt();
    int Angle = Serial.parseInt();
    if (Direction == 0 && Position >= Angle){
      Position -= Angle;
      axis.write(Position);
      disp.println("Now at " + Position + " degrees");
      Serial.println("Now at " + Position + " degrees");
    }else if (Direction == 1 && Position <= ServoMax-Angle){
      Position += Angle;
      axis.write(Position);
      disp.println("Now at " + Position + " degrees");
      Serial.println("Now at " + Position + " degrees");
    }
    disp.println("Serial data not valid.");
    Serial.println("Serial data not valid.");
  }
}
