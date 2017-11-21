package com.example.enoque_alves.comunicacaoblue;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReceivingData extends AppCompatActivity {

    private BluetoothSocket mmSocket;
    private OutputStream mmOutputStream;
    private InputStream mmInputStream;
    private Thread workerThread;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;
    //estabelecendo a conexão entre a activity e a classe de suporte
    private ConexaoBlue connection = ConexaoBlue.getInstance(null, false);
    boolean enviado = false;
    EditText valor;
    Button butao;
    String texto;
    Button abrir_guarra, fechar_guarra, meia_guarra, novo_comando_butao;
    List<String> textos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_data);
        mmSocket = connection.getConection();
        valor = (EditText) findViewById(R.id.id_valor);
        butao = (Button) findViewById(R.id.id_enviar);
        abrir_guarra = (Button) findViewById(R.id.abrir_guarra);
        fechar_guarra = (Button) findViewById(R.id.fechar_guarra);
        meia_guarra = (Button) findViewById(R.id.meio_aberto);

        butao.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                texto = valor.getText().toString();
                enviarComando(texto);
                if (enviado)
                    Toast.makeText(ReceivingData.this, "Enviado Com sucesso", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ReceivingData.this, "Erro de comunicacao", Toast.LENGTH_SHORT).show();
            }

        });
        abrir_guarra.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                enviarComando("!a0");
                if (enviado)
                    Toast.makeText(ReceivingData.this, "Enviado Com sucesso", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ReceivingData.this, "Erro de comunicacao", Toast.LENGTH_SHORT).show();
            }

        });
        fechar_guarra.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                enviarComando("!a60");
                if (enviado)
                    Toast.makeText(ReceivingData.this, "Enviado Com sucesso", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ReceivingData.this, "Erro de comunicacao", Toast.LENGTH_SHORT).show();
            }

        });
        meia_guarra.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                enviarComando("!a40");
                if (enviado)
                    Toast.makeText(ReceivingData.this, "Enviado Com sucesso", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ReceivingData.this, "Erro de comunicacao", Toast.LENGTH_SHORT).show();
            }

        });
        novo_comando_butao = (Button)findViewById(R.id.novoComando);
        textos = new ArrayList<String>();
        Log.i("asd", "chegou até aqui!!");
        final ListView listaDeDispositivos = (ListView) findViewById(R.id.id_comandos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReceivingData.this, android.R.layout.simple_list_item_1, textos);
        listaDeDispositivos.setAdapter(adapter);

        //listaDeCursos.setClickable(true);
        listaDeDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listaDeDispositivos.getItemAtPosition(position);
                String comando=(String)o;
                String valor[] = comando.split("\n");
                comando = valor[1];
                enviarComando(comando);
                if (enviado)
                    Toast.makeText(ReceivingData.this, "Enviado Com sucesso", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ReceivingData.this, "Erro de comunicacao", Toast.LENGTH_SHORT).show();

            }
        });

        novo_comando_butao.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivingData.this, novo_comando.class);
                startActivityForResult(intent, 10);
            }
        });
        try {
            if (mmSocket == null) {
                Log.i("asd", "chegou");
            }
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            Log.i("asd", "deuruimData = " + e.getMessage());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ReceivingData.this.RESULT_OK && requestCode == 10) {
            texto = data.getStringExtra("resultado");
            textos.add(texto);
            //Toast.makeText(ReceivingData.this,"Mensagem Recebida da SegundaActivity:\n" + resposta, Toast.LENGTH_LONG).show();
        }
    }

    public void beginListenForData(final String texto) {
        final BufferedReader reader = connection.getmBufferedReader();
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker){
                    try {
                        String info = reader.readLine(); // recebe os dados da comunicação
                        Toast.makeText(ReceivingData.this, "Mensagem recebida: " + info, Toast.LENGTH_SHORT).show();

                        mmOutputStream.write(texto.getBytes()); // envia dados.


                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void enviarComando (String comando){
        try {
            mmOutputStream.write(comando.getBytes());
            enviado = true;
        }
        catch (IOException e){
            enviado = false;
            Log.i("asd", "comando nao enviado");
            e.printStackTrace();
        }
    }
}
