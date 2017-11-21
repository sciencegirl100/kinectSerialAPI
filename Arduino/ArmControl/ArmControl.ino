#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <Servo.h>

LiquidCrystal_I2C disp(0x3f,20,4);
Servo axis;
int Position = 0;
int ServoMax = 180;
int ServoPin = 2;

void setup() {
  Serial.begin(9600);
  disp.init();
  disp.backlight();
  axis.attach(ServoPin);
  disp.setCursor(0,0);
  disp.print("Ready to go!");
}

void loop() {
  while(Serial.available() > 0){
    disp.clear();
    disp.setCursor(0,0);
    disp.print("Recieved Serial Data");
    int Direction = Serial.parseInt();
    disp.setCursor(0,2);
    int Angle = Serial.parseInt();
    if (Direction == 0 && Position >= Angle){
      Position -= Angle;
      axis.write(Position);
      String output = "Moved -" + (String)Angle + " degrees";
      disp.print(output);
      Serial.println(output);
    }else if (Direction == 1 && Position <= ServoMax-Angle){
      Position += Angle;
      axis.write(Position);
      String output = "Moved +" + (String)Angle + " degrees";
      disp.print(output);
      Serial.println(output);
    }else if(Direction == 3){
      disp.setCursor(0,2);
      disp.print("Reset Servo");
      Serial.println("Reset Servo Position");
      axis.write(0);
      
    }else{
      disp.setCursor(0,2);
      disp.print("Serial data invalid");
      Serial.println("Serial data not valid.");
    }
    disp.setCursor(0,3);
    String Status = "Current Angle: " + (String)Position;
    disp.print(Status);
    Serial.println(Status);
  }
  delay(10);
}
