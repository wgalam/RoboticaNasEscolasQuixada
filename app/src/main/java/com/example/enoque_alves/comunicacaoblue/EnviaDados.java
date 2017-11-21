package com.example.enoque_alves.comunicacaoblue;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by enoque_alves on 05/11/17.
 */

public class EnviaDados {
    private static EnviaDados enviaDados;
    private ConexaoBlue connection = ConexaoBlue.getInstance(null, false);
    private OutputStream mmOutputStream;
    private BluetoothSocket mmSocket;
    private int posicaoA, posicaoB, posicaoC, posicaoD;

    public EnviaDados(){
        posicaoA = 57;
        posicaoB = 87;
        posicaoC = 100;
        posicaoD = 30;
        mmSocket = connection.getConection();
        try{
            mmOutputStream = mmSocket.getOutputStream();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static EnviaDados getEnviaDados(){
        if (enviaDados == null) {
            enviaDados = new EnviaDados();
            return enviaDados;
        }
        return enviaDados;
    }
    public void enviarComando (String comando){
        try {
            mmOutputStream.write(comando.getBytes());
            //Toast.makeText(joystickk.this, "enviado : " + comando, Toast.LENGTH_SHORT).show();
            /*if (gravando){
                comandos.add(comando);
            }
            */
        }
        catch (IOException e){
            e.printStackTrace();
            char chars [] = comando.toCharArray();
            char servo = chars[1];
            String aux = "";
            for (int i = 2 ; i < chars.length; i++) {
                aux = aux + chars[i];
            }
            Log.i("asd", aux + " servo = " + servo);


        }
    }
    public void enviarVariosComandos(ArrayList<String> comandos, int delay){
        try {
            for (int i = 0; i < comandos.size(); i++){
                enviarComando(comandos.get(i));
                char chars [] = comandos.get(i).toCharArray();
                char servo = chars[1];
                String aux = "";
                for (int j = 2 ; j < chars.length; j++) {
                    aux = aux + chars[j];
                }
                int posicaoAtual = Integer.parseInt(aux);
                if (servo == 'a'){
                    posicaoA = (posicaoAtual-10);
                }
                else if (servo == 'b'){
                    posicaoB = (posicaoAtual-10);
                }
                else if (servo == 'c'){
                    posicaoC = (posicaoAtual-60);
                }
                else if (servo == 'd'){
                    posicaoD = (posicaoAtual-60);
                }
                TimeUnit.MILLISECONDS.sleep(delay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resetar(){
        posicaoA = (57);
        posicaoB = (87);
        posicaoC = (100);
        posicaoD = (40);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            enviarComando("!a67");
            TimeUnit.MILLISECONDS.sleep(500);
            enviarComando("!b97");
            TimeUnit.MILLISECONDS.sleep(500);
            enviarComando("!c160");
            TimeUnit.MILLISECONDS.sleep(500);
            enviarComando("!d100");
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getPosicaoA() {
        return posicaoA;
    }

    public int getPosicaoB() {
        return posicaoB;
    }

    public int getPosicaoC() {
        return posicaoC;
    }

    public int getPosicaoD() {
        return posicaoD;
    }
}
