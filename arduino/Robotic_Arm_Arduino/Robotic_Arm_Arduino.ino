/*

  Braço Robótico

  ############################################

  Controle dos servos via Monitor Serial e comunicação Android

  INSTRUÇÕES

  A - (10 - 67  GRAUS) : MOVIMENTA A GARRA
  B - (10 - 190 GRAUS) : MOVIMENTA A BASE
  C - (60 - 180 GRAUS) : MOVIMENTA O EIXO CENTRAL
  D - (60 - 130 GRAUS) : MOVIMENTA O EIXO LATERAL

*/

// Bibliotecas auxiliares padrão
#include <Servo.h>

// Definição de constantes
#define SERVO_GARRA       6 // Servo 1 no protótipo
#define SERVO_BASE        3 // Servo 4 no protótipo
#define SERVO_CENTRAL     5 // Servo 2 no protótipo
#define SERVO_LATERAL     9 // Servo 3 no protótipo



#define INCREMENT         10 //Delay do servo

// Funções para movimento suave e comunicacao
void splitString(char* data);

// Variáveis
  char buffer[5]; //vetor para armazenar string de texto
boolean run = false; //verifica constantemente se o comando de guarda foi recebido
int Ans;   //recebe o valor inteiro do angulo recebido


// variaveis para guardar a posição inicial dos servos
int pos_garra = 67;
int pos_base = 97;
int pos_central = 160;
int pos_lateral = 100;

// Atribuição de objetos (servos do braço)
Servo servo;  //variavel genérica
Servo servo_base;
Servo servo_garra;
Servo servo_central;
Servo servo_lateral;

void setup() {

  Serial.begin(9600);  //inicia comunicação serial a 9600bps
  Serial.flush();      //libera caracteres que estejam na linha
  //serial, deixando-a pronta para in/outs

  pinMode(SERVO_GARRA, OUTPUT);
  pinMode(SERVO_BASE, OUTPUT);
  pinMode(SERVO_CENTRAL, OUTPUT);
  pinMode(SERVO_LATERAL, OUTPUT);

  servo_garra.attach(SERVO_GARRA);
  servo_base.attach(SERVO_BASE);
  servo_central.attach(SERVO_CENTRAL);
  servo_lateral.attach(SERVO_LATERAL);

  servo_garra.write(pos_garra);           // seta um posicionamento inicial para teste do braço
  servo_base.write(pos_base);
  servo_central.write(pos_central);
  servo_lateral.write(pos_lateral);

  delay(3000);
}

void loop() {
  if ((Serial.available() > 0)) { //verifica se foi enviado um caractere
    //para serial
    int index = 0; //armazena a posição de um ponteiro para os
    //caracteres do vetor

    delay(INCREMENT); //deixa o buffer encher
    int numChar = Serial.available(); //atribui o numero de
    //caracteres digitados

    if (numChar > 5) numChar = 5; //evita o estouro do buffer

    while (numChar--) { //executa até que numChar seja zero

      buffer[index++] = Serial.read(); //le a serial e armazena no vetor
    }
    splitString(buffer); //chama a função splitString com buffer como parametro
  }

}

void splitString(char* data) {
  Serial.println(data);

  char* parameter; //variavel para acessar os elementos do vetor data
  parameter = strtok (data, " ,"); //divide a string quando encontrar um espaço ou uma
  //vírgula

  while (parameter != NULL) { //executa enquanto parameter não estiver vazia
    setPosition(parameter);
    parameter = strtok (NULL, " ,"); //preenche o vetor com caracteres NULL
  }

  //Limpa o texto e os buffers seriais
  for (int x = 0; x < 5; x++) {
    buffer[x] = '\0';
  }

  Serial.flush(); //libera caracteres que estejam na linha
  //serial, deixando-a pronta para in/outs
}

void setPosition(char* data) {
  if (data[0] == '!') {
    int *Pos;
    run = true;
    
    Ans = strtol(data + 2, NULL, 10);   //define Ans como numero na proxima parte do texto
    
    
    Pos = selectServo(data[1]); //Pos recebe o endereço do posicionamento do servo selecionado
    if (data[1] == 'D' || data[1] == 'd'){
      Ans = valor_de_d (Ans); 
    }
    else if (data[1] == 'C' || data[1] == 'c'){
      Ans = valor_de_c (Ans);
    }
    if (Pos == NULL) return;
    
    if (Ans < *Pos) {        //condição que define a direção do movimento(Abre/Fecha, Sobe/Desce)
     
      for (int i = *Pos; i >= Ans; i--) {
        //Serial.print("LOOP = ");
        //Serial.print(i);
        //Serial.print("\n");
        //Serial.print(servo.read());
        servo.write(i); //atribui o grau da posição do eixo do servo
        delay(INCREMENT);
      }
      *Pos = servo.read(); //Atualiza o valor da posiçao 
      Serial.print("O servo esta na angulacao: ");
      Serial.println(servo.read());
    }
    else {
      //Serial.print ("\n\nEFGH");
      for (int i = *Pos; i <= Ans; i++) {
        //servo.write(i); //atribui o grau da posição do eixo do servo
        //delay(increment);
       // Serial.print("LOOP = ");
       // Serial.print(i);
       // Serial.print("\n");
       // Serial.print(servo.read());
        servo.write(i); //atribui o grau da posição do eixo do servo
        delay(INCREMENT);
      }
      *Pos = servo.read(); //Atualiza o valor da posiçao
      Serial.print("O servo esta na angulacao: ");
      Serial.println(servo.read());
    }

  }
  else if (data[0] == '@'){
     int *Pos = selectServo(data[1]);
     if (Pos == NULL) return;
    Serial.print("Posicao atual do servo = ");
    Serial.print(*Pos);
    Serial.print("\n");
  }
  else {
    run = false;
    Serial.print("Erro de comunicacao");
  }
}


// Funções de Movimento de cada Servo
int *selectServo(char comand) {
  if ((comand == 'a') || (comand == 'A')) {

    servo = servo_garra;
    Ans = constrain(Ans, 10, 67); //garante que o valor digitado esteja entre 70 e 105
    return &pos_garra;
  }
  else if ((comand == 'b') || (comand == 'B')) {
    servo = servo_base;
    Ans = constrain(Ans, 10, 190); //garante que valor digitado esteja entre 35 e 160
    return &pos_base;
  }
  else if ((comand == 'c') || (comand == 'C')) {
    servo = servo_central;
    Ans = constrain(Ans, 60, 180); //garante que valor digitado esteja entre 30 e 160
    return &pos_central;
  }
  else if ((comand == 'd') || (comand == 'D')) {
    servo = servo_lateral;
    Ans = constrain(Ans, 60, 130); //garante que valor digitado esteja entre 30 e 160
    return &pos_lateral;
  }
  else {
     return NULL; 
  }
}

int valor_de_d (int d){
  if (pos_central <= 160 && pos_central > 130){
	return d;
  }
      	
  if (pos_central <= 130 && pos_central > 120){
            if (d > 120){
      		d = 120;
      }
   }
      	
   if (pos_central <= 120 && pos_central > 110){
    	if (d > 110){
      	    d = 110;
     	}
   }
   else {
       if (d > 100){
           d = 100;
       }
   } 
   return d;
}

int valor_de_c (int c){
	if (pos_lateral >= 120){
		if (c < 120){
			c = 120;
			return c;
		}
	}
	if (pos_lateral < 120 && pos_lateral >= 100){
		if (c > 180){
			c = 180;
			return c;
		}
	}
	if (pos_lateral < 100 && pos_lateral >= 70){
		if (c < 70){
			c = 70;
			return c;
		}
	}
	else{
		if (c < 100){
			c = 100;
			return c;
		}
	}
	return c;
}
