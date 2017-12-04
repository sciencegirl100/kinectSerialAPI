#include <Servo.h>

Servo axis;
char Angle = 'a';
int ServoPin = 2;

void setup() {
  Serial.begin(115200);
  axis.attach(ServoPin);
}

void loop() {
  if (Serial.available() > 0){
    Angle = Serial.read();
    if (Angle == 'a'){
      axis.write(15);
    }else if (Angle == 'b'){
      axis.write(60);
    }else if (Angle == 'c'){
      axis.write(100);
    }else if (Angle == 'd'){
      axis.write(160);
    }else{
      Serial.println("Serial data not valid.");
      Serial.println(Angle);
    }
    delay(50);
  }
}
