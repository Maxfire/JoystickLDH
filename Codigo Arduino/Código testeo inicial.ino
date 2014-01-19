const byte PIN_BUTTON_SELECT = 2; // definimos a que pin va cada boton o el joystick nuestro

const byte PIN_BUTTON_RIGHT = 3;
const byte PIN_BUTTON_UP = 4;
const byte PIN_BUTTON_DOWN = 5;
const byte PIN_BUTTON_LEFT = 6;

const byte PIN_ANALOG_X = 0;
const byte PIN_ANALOG_Y = 1;


void setup() {
  Serial.begin(9600);  // abrimos puerto serie

  pinMode(PIN_BUTTON_RIGHT, INPUT);   //establecemos pines entrada
  digitalWrite(PIN_BUTTON_RIGHT, HIGH);
 
  pinMode(PIN_BUTTON_LEFT, INPUT);  
  digitalWrite(PIN_BUTTON_LEFT, HIGH);
 
  pinMode(PIN_BUTTON_UP, INPUT);  
  digitalWrite(PIN_BUTTON_UP, HIGH);
 
  pinMode(PIN_BUTTON_DOWN, INPUT);  
  digitalWrite(PIN_BUTTON_DOWN, HIGH);
 
  pinMode(PIN_BUTTON_SELECT, INPUT);  
  digitalWrite(PIN_BUTTON_SELECT, HIGH);  
}


void loop() {  //imprimir por el puerto serie lo que lee cada botón y 
  Serial.print("l:"); // y los valores de los ejes x e y del joystick
  Serial.print(digitalRead(PIN_BUTTON_LEFT));
  Serial.print(" ");
 
  Serial.print("r:");
  Serial.print(digitalRead(PIN_BUTTON_RIGHT));
  Serial.print(" ");
 
  Serial.print("u:");
  Serial.print(digitalRead(PIN_BUTTON_UP));
  Serial.print(" ");
 
  Serial.print("d:");
  Serial.print(digitalRead(PIN_BUTTON_DOWN));
  Serial.print(" ");

  Serial.print("x:");
  Serial.print(analogRead(PIN_ANALOG_X));
  Serial.print(" ");
 
  Serial.print("y:");
  Serial.print(analogRead(PIN_ANALOG_Y));
  Serial.print(" ");  
 
  Serial.print("s:");
  Serial.print(digitalRead(PIN_BUTTON_SELECT));
  Serial.print(" ");
 
  Serial.println();
}
