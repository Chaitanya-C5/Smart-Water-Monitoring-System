#include <OneWire.h>
#include <DallasTemperature.h>
#include <SoftwareSerial.h>
#include "SoftwareSerial.h"

SoftwareSerial gsmSerial(2, 3);  
// RX, TX pins on Arduino

#define TEMP_SENSOR_PIN 8
#define PH_SENSOR_PIN A3
#define TURBIDITY_SENSOR_PIN A5

OneWire oneWire(TEMP_SENSOR_PIN);
DallasTemperature sensors(&oneWire);

unsigned long int phAvgValue = 8.0; 
int phBuf[10], phTemp;
int purity;
float lat, longit;

void setup() {
  
  //serial_connection.listen();
  gsmSerial.begin(9600);
  Serial.begin(9600);
  pinMode(13,OUTPUT);
  pinMode(12,OUTPUT);
  sensors.begin();
  Serial.println("Ready");
}

void loop() {
  // Temperature Sensor
  sensors.requestTemperatures();
  float tempCelsius = sensors.getTempCByIndex(0);
  float tempFahrenheit = sensors.toFahrenheit(tempCelsius);
  Serial.print("Temperature: ");
  Serial.print(tempCelsius);
  Serial.print(" C  ");
  Serial.print(tempFahrenheit);
  Serial.println(" F");

  // pH Sensor
  for (int i = 0; i < 10; i++) {
    phBuf[i] = analogRead(PH_SENSOR_PIN);
    delay(10);
  }
  for (int i = 0; i < 9; i++) {
    for (int j = i + 1; j < 10; j++) {
      if (phBuf[i] > phBuf[j]) {
        phTemp = phBuf[i];
        phBuf[i] = phBuf[j];
        phBuf[j] = phTemp;
      }
    }
  }
  phAvgValue = 0;
  for (int i = 2; i < 8; i++)
    phAvgValue += phBuf[i];
  float phValue = (float)phAvgValue * 5.0 / 1024 / 6;
  float scalingFactor = 2.2;
  float offset = 0.0;
  phValue = scalingFactor * phValue + offset;
  phValue = constrain(phValue, 0, 14);
  Serial.print("pH: ");
  Serial.print(phValue, 2);
  Serial.println(" ");

  // Turbidity Sensor
  int turbiditySensorValue = analogRead(TURBIDITY_SENSOR_PIN);
  int turbidity = map(turbiditySensorValue, 0, 800, 100, 0);
  Serial.print("Turbidity: ");
  Serial.println(turbidity);
  delay(1000);
  if (turbidity < 23 && phValue > 6.5 && phValue < 8.5 && tempCelsius > 15 && tempCelsius < 60) {
    Serial.println("Pure");
    purity = 1;
    digitalWrite(13,HIGH);
    digitalWrite(12,LOW);
  } else {
    Serial.println("Impure");
    purity = 0;
    digitalWrite(13,LOW);
    digitalWrite(12,HIGH);
  }

  delay(1000);
  Connect2Server();
  SendData(phValue, tempCelsius, turbidity, purity);
  EndConnection();
}


void Connect2Server()
{
    
    gsmSerial.println("AT+CIPSTART=\"tcp\",\"api.thingspeak.com\",\"80\"");
    delay(2000);

    ShowSerialData();

    gsmSerial.println("AT+CIPSEND");
    delay(2000);

    ShowSerialData();
}

void SendData(float ph, float temp, int turb, int pur)
{
    String str = "GET https://api.thingspeak.com/update?api_key=RQKSSTCJG6DDQQ0V&field1=" + String(ph, 2) + "&field2=" + String(temp, 2) + "&field3=" + String(turb) + "&field4=" + String(pur) +" HTTP/1.1";
    gsmSerial.println(str);
    gsmSerial.println("Host: api.thingspeak.com");
    gsmSerial.println("Connection: close");
    gsmSerial.println();
    delay(2000);

    ShowSerialData();

    gsmSerial.println((char)26);
    delay(4000);

    gsmSerial.println();

    ShowSerialData();
}

void EndConnection()
{
    gsmSerial.println("AT+CIPSHUT");
    delay(100);
    ShowSerialData();
}

void ShowSerialData()
{
  while(gsmSerial.available() != 0)
  {
      Serial.write(gsmSerial.read());
  }
}

