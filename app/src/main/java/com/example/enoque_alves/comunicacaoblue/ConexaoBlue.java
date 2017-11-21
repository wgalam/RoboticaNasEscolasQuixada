package com.example.enoque_alves.comunicacaoblue; /**
 * Created by enoque_alves on 19/09/17.
 */

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;



public class ConexaoBlue {
    private static ConexaoBlue conexao; //objeto que referencia a classe
    private final BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter(); //acessa o dispositivo bluetooth de um celular genérico
    private BluetoothDevice  BTDevice; //todo acesso ao módulo bluetooth utilizado no tutorial será feito a partir deste objeto.
    private final BluetoothSocket  BTSocket; // garante a instância da rede bluetooth entre o celular e o módulo
    private BufferedReader mBufferedReader = null; //lê as comunicações
    private final UUID activeUUID= UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // ID padrão da porta serial
    private boolean isConnected = false; // variável de controle da conexão
    private final int REQUEST_ENABLE_BT = 1; // Constante padrão para requisição de permissão de acesso ao bluetooth em tempo de execução.
    //private static boolean deuErro;

    //Construtor da classe
    private ConexaoBlue(BluetoothDevice device){
        BTDevice = device;
        BluetoothSocket temp = null;
        if (device == null){
            Log.i("asd", "Ta null");
        }
        try{
            temp = BTDevice.createRfcommSocketToServiceRecord(activeUUID);
            //TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //temp = BTDevice.createRfcommSocketToServiceRecord(device.ACTION_UUID);
            Log.i("asd", "UUID = " + device.ACTION_UUID);


        }
        catch (Exception e)
        {
            Log.i("asd", "Erro IO");
        }
        BTSocket = temp;

        if(BTAdapter.isDiscovering())
            BTAdapter.cancelDiscovery();

        try {
            if (BTSocket == null)
                Log.i("asd", "sockenull");
            InputStream aStream = null;
            InputStreamReader aReader = null;
            BTSocket.connect();

            aStream = BTSocket.getInputStream();
            aReader = new InputStreamReader( aStream );
            mBufferedReader = new BufferedReader( aReader );
            isConnected = true;
        }catch (IOException e) {
            isConnected = false;
            Log.i("asd", "nao contectou = " + e.getMessage());
            return;
        }
    }



    //Garante a conexão com o módulo bluetooth
    public static ConexaoBlue getInstance(BluetoothDevice d, boolean subrescrever) {
        if (conexao == null)
            conexao = new ConexaoBlue(d);
        else
        if(subrescrever)
        {
            conexao = new ConexaoBlue(d);
            Log.i( "ConexaoBluetooth","Sobrescreveu a conexão");
        }
        return conexao;
    }

    //Verifica o estado da conexão
    public boolean isConnected () {
        return isConnected;
    }

    //Referencia a conexão
    public BluetoothSocket getConection() {
        return BTSocket;
    }

    //Termina a conexão
    public boolean finish() {
        try {
            BTSocket.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public BufferedReader getmBufferedReader() {
        return mBufferedReader;
    }
}
