#define DEBOUNCE 10  // anti rebote, cuantos ms para evitar el rebote, 5+ ms es suficiente

// definimos los pines a usar 2 analógicos y 5 botones (4 cruceta mas 1 reset)
byte buttons[] = {A0,A1,2,3,4,5,6}; 
// es tratado como array de botones
#define NUMBUTTONS sizeof(buttons)
//comprobarémos cuando un botón just pressed(acaba de presionar), just released (acaba de ser soltado), or 'currently pressed'(actualmente presionado) 
volatile byte pressed[NUMBUTTONS], justpressed[NUMBUTTONS], justreleased[NUMBUTTONS];

void setup() {
  byte i;

  //  abrir puerto serie, recordar cambiar baudios
  Serial.begin(115200);

  // Si conectamos un LED en el pin 13
  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT);

  // establecer pines como entrada y habilitar pull-up de las resistencias.
  for (i=0; i< NUMBUTTONS; i++) {
    pinMode(buttons[i], INPUT);
    digitalWrite(buttons[i], HIGH);
  }

  // timer2 interrumpe cada 15 ms 
  TCCR2A = 0;
  TCCR2B = 1<<CS22 | 1<<CS21 | 1<<CS20;

  //Timer2 se desborda la Interrupción
  TIMSK2 |= 1<<TOIE2;

}

SIGNAL(TIMER2_OVF_vect) {
  check_switches();
}

void check_switches()
{
  static byte previousstate[NUMBUTTONS];
  static byte currentstate[NUMBUTTONS];
  static long lasttime;
  byte index;

  if (millis() < lasttime) {
//algún error, volverémos a intentarlo de nuevo
  lasttime = millis();
  }

  if ((lasttime + DEBOUNCE) > millis()) {
    //  no suficiente tiempo para anti rebote
    return;
  }
  // OK, hemos esperado hasta que se cumple el tiempo anti rebote y reseteamos el timer
  lasttime = millis();

  for (index = 0; index < NUMBUTTONS; index++) {

    currentstate[index] = digitalRead(buttons[index]);   // lee el boton

  

    if (currentstate[index] == previousstate[index]) {
      if ((pressed[index] == LOW) && (currentstate[index] == LOW)) {
          // just pressed (acaba de presionar)
          justpressed[index] = 1;
      }
      else if ((pressed[index] == HIGH) && (currentstate[index] == HIGH)) {
          // just released (se libera)
          justreleased[index] = 1;
      }
      pressed[index] = !currentstate[index];  // Digital HIGH significa no está presionada
    }
    previousstate[index] = currentstate[index];   // mantiene ejecutandose los botones
  }
}
int i=millis();
int lastt=0;
void loop() {
    if (Comp("on")==0) {
  digitalWrite(12,HIGH);
  }
  if (Comp("off")==0) {
  digitalWrite(12,LOW);  
  }
  for (byte i = 0; i < NUMBUTTONS; i++) {
    if (justpressed[i]) {
      justpressed[i] = 0;
        Serial.println(i+4);
      // check_switches()  limpiará la bandera de 'just pressed'
    }
    if (justreleased[i]) {
      justreleased[i] = 0;
       Serial.println(i);
    }
    if (pressed[i]) {
       // Nos dice si el botón está presionado en este momento..
       
       
    }
  }
}

char inData[20]; //  reserva espacio de memoria para el String
char inChar=-1; //  Donde se guarda el caracter leido
byte index = 0; // Indice dentro del array donde será guardado el dato

char Comp(char* This) {
    while (Serial.available() > 0) // No lee hasta que no sepamos que tenemos datos
                                   
    {
        if(index < 19) //  uno menos que el tamaño del array
        {
            inChar = Serial.read(); // Lee un caracter
            inData[index] = inChar; // Lo almacena
            index++; // incrementa el contador
            inData[index] = '\0'; // termina, cadena que finaliza el string
        }
    }

    if (strcmp(inData,This)  == 0) {
        for (int i=0;i<19;i++) {
            inData[i]=0;
        }
        index=0;
        return(0);
    }
    else {
        return(1);
    }
}
