package com.example.enoque_alves.comunicacaoblue;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class joystickk extends AppCompatActivity {
    private SeekBar garra_bar, base_bar, c_bar, d_bar;
    private OutputStream mmOutputStream;
    private BluetoothSocket mmSocket;
    private boolean gravando = false;
    private ArrayList<String> comandosGravados = new ArrayList<String>();
    private ArrayList<String> comandos = new ArrayList<String>();
    private ArrayList<String> comandosTela = new ArrayList<String>();
    private Button gravar, limpar, play, reset, programar;
    private ImageButton more_base, more_garra, less_base, less_garra, more_c, more_d, less_c, less_d;
    private ConexaoBlue connection = ConexaoBlue.getInstance(null, false);
    private EnviaDados enviaDados = EnviaDados.getEnviaDados();
    private TextView label_garra, label_base, label_c, label_d;
    private int delay = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystickk);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        garra_bar = (SeekBar) findViewById(R.id.seekbar_garra);
        base_bar = (SeekBar) findViewById(R.id.seekbar_base);
        c_bar = (SeekBar) findViewById(R.id.seekbar_c);
        d_bar = (SeekBar) findViewById(R.id.seekbar_d);
        play = (Button) findViewById(R.id.butao_play);
        reset = (Button) findViewById(R.id.butao_reset);
        limpar = (Button) findViewById(R.id.butao_limpa);
        gravar = (Button) findViewById(R.id.butao_gravar);
        programar = (Button) findViewById(R.id.programar);
        more_c = (ImageButton) findViewById(R.id.button_more_c);
        more_d = (ImageButton) findViewById(R.id.button_more_d);
        less_c = (ImageButton) findViewById(R.id.button_less_c);
        less_d = (ImageButton) findViewById(R.id.button_less_d);
        more_base = (ImageButton) findViewById(R.id.button_more_base);
        more_garra = (ImageButton) findViewById(R.id.button_more_garra);
        less_garra = (ImageButton) findViewById(R.id.button_less_garra);
        less_base = (ImageButton) findViewById(R.id.button_less_base);
        label_base = (TextView) findViewById(R.id.label_base);
        label_garra = (TextView) findViewById(R.id.label_garra);
        label_c = (TextView) findViewById(R.id.label_c);
        label_d = (TextView) findViewById(R.id.label_d);

        label_garra.setText("" + (int) (garra_bar.getProgress()/0.57));
        label_base.setText("" + (int) (base_bar.getProgress()/1.8));
        label_c.setText("" + (int) (c_bar.getProgress()/0.7));
        label_d.setText("" + (int) (d_bar.getProgress()/1.2));

        //play.setVisibility(View.INVISIBLE);
        mmSocket = connection.getConection();

        garra_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                label_garra.setText("" + (int) (garra_bar.getProgress()/0.57));
                String comando = "!a" + (seekBar.getProgress()+10);
                enviarComando(comando);

            }
        });
        base_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                label_base.setText("" + (int) (base_bar.getProgress()/1.8));
                String comando = "!b" + (seekBar.getProgress()+10);
                enviarComando(comando);

            }
        });
        c_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                label_c.setText("" + (int) (c_bar.getProgress()/1.2));
                String comando = "!c" + (seekBar.getProgress()+60);
                enviarComando(comando);

            }
        });
        d_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                label_d.setText("" + (int) (d_bar.getProgress()/0.7));
                String comando = "!d" + (seekBar.getProgress()+60);
                enviarComando(comando);

            }
        });

        gravar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gravando = !gravando;
                if (gravando){
                    Toast.makeText(joystickk.this, "Gravando!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(joystickk.this, "Fim da gravação!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        limpar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                comandos.clear();
            }
        });

        reset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                garra_bar.setProgress(57);
                base_bar.setProgress(87);
                c_bar.setProgress(100);
                d_bar.setProgress(40);
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                    enviarComando("!a67");
                    TimeUnit.MILLISECONDS.sleep(300);
                    enviarComando("!b97");
                    TimeUnit.MILLISECONDS.sleep(300);
                    enviarComando("!c160");
                    TimeUnit.MILLISECONDS.sleep(300);
                    enviarComando("!d100");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        programar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(joystickk.this, TelaDeProgramacao.class);
                intent.putExtra("comandos", comandosGravados);
                intent.putExtra("comandosTela", comandosTela);
                startActivityForResult(intent, 100);
            }
        });

        play.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (gravando) gravando = !gravando;
                enviaDados.enviarVariosComandos(comandosGravados, delay);
//                garra_bar.setProgress(enviaDados.getPosicaoA());
//                base_bar.setProgress(enviaDados.getPosicaoB());
//                c_bar.setProgress(enviaDados.getPosicaoC());
//                d_bar.setProgress(enviaDados.getPosicaoD());
//                Trocar por set
            }
        });

        more_c.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                c_bar.setProgress(c_bar.getProgress()+1);
                String comando = "!c" + (c_bar.getProgress()+60);
                enviarComando(comando);
            }
        });
        less_c.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                c_bar.setProgress(c_bar.getProgress()-1);
                String comando = "!c" + (c_bar.getProgress()+60);
                enviarComando(comando);
            }
        });

        more_d.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                d_bar.setProgress(d_bar.getProgress()+1);
                String comando = "!d" + (d_bar.getProgress()+60);
                enviarComando(comando);
            }
        });

        less_d.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                d_bar.setProgress(d_bar.getProgress()-1);
                String comando = "!d" + (d_bar.getProgress()+60);
                enviarComando(comando);
            }
        });
        less_base.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                base_bar.setProgress(base_bar.getProgress()-1);
                String comando = "!b" + (base_bar.getProgress()+10);
                enviarComando(comando);
            }
        });
        less_garra.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                garra_bar.setProgress(garra_bar.getProgress()-1);
                String comando = "!a" + (garra_bar.getProgress()+10);
                enviarComando(comando);
            }
        });

        more_base.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                base_bar.setProgress(base_bar.getProgress()+1);
                String comando = "!b" + (base_bar.getProgress()+10);
                enviarComando(comando);
            }
        });
        more_garra.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                garra_bar.setProgress(garra_bar.getProgress()+1);
                String comando = "!a" + (garra_bar.getProgress()+10);
                enviarComando(comando);
            }
        });


        try{
            mmOutputStream = mmSocket.getOutputStream();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == joystickk.this.RESULT_OK && requestCode == 100) {
            comandosGravados = data.getStringArrayListExtra("comandos");
            comandosTela = data.getStringArrayListExtra("comandosTela");
            delay = data.getIntExtra("delay", 1000);
            //Toast.makeText(ReceivingData.this,"Mensagem Recebida da SegundaActivity:\n" + resposta, Toast.LENGTH_LONG).show();
        }
    }

    public void enviarComando (String comando){
//        try {
//            mmOutputStream.write(comando.getBytes());
            //Toast.makeText(joystickk.this, "enviado : " + comando, Toast.LENGTH_SHORT).show();
            if (gravando){
                comandosGravados.add(comando);
                char chars [] = comando.toCharArray();
                char servo = chars[1];
                String aux = "";
                String comandoTela = "";
                for (int j = 2 ; j < chars.length; j++) {
                    aux = aux + chars[j];
                }
                int posicaoAtual = Integer.parseInt(aux);
                if (servo == 'a'){
                    comandoTela = "Abre/fecha  ";
                    posicaoAtual = (int) ((posicaoAtual-10)/0.57);
                    Log.i("asd", "valor posicao: " + posicaoAtual);
                    label_garra.setText(""+ posicaoAtual);
                }
                else if (servo == 'b'){
                    comandoTela = "Girar  ";
                    posicaoAtual = (int) ((posicaoAtual-10)/1.8);
                    label_base.setText(""+ posicaoAtual);
                }
                else if (servo == 'c'){
                    comandoTela = "Avanca/recua  ";
                    posicaoAtual = (int) ((posicaoAtual-60)/0.7);
                    label_c.setText(""+posicaoAtual);
                }
                else if (servo == 'd'){
                    comandoTela = "Sobe/desce  ";
                    posicaoAtual = (int) ((posicaoAtual-60)/1.2);
                    label_d.setText(""+ posicaoAtual);
                }
                comandoTela += ""+posicaoAtual;
                comandosTela.add(comandoTela);
            }

        char chars [] = comando.toCharArray();
        char servo = chars[1];
        String aux = "";
        for (int j = 2 ; j < chars.length; j++) {
            aux = aux + chars[j];
        }
        int posicaoAtual = Integer.parseInt(aux);
        if (servo == 'a'){
            posicaoAtual = (int) ((posicaoAtual-10)/0.57);
            label_garra.setText(""+ posicaoAtual);
        }
        else if (servo == 'b'){
            posicaoAtual = (int) ((posicaoAtual-10)/1.8);
            label_base.setText(""+ posicaoAtual);
        }
        else if (servo == 'c'){
            posicaoAtual = (int) ((posicaoAtual-60)/0.7);
            label_c.setText(""+posicaoAtual);
        }
        else if (servo == 'd'){
            posicaoAtual = (int) ((posicaoAtual-60)/1.2);
            label_d.setText(""+ posicaoAtual);
        }
            enviaDados.enviarComando(comando);
//            garra_bar.setProgress(enviaDados.getPosicaoA());
//            base_bar.setProgress(enviaDados.getPosicaoB());
//            c_bar.setProgress(enviaDados.getPosicaoC());
//            d_bar.setProgress(enviaDados.getPosicaoD());
//        }
//        catch (IOException e){
//            e.printStackTrace();
//            char chars [] = comando.toCharArray();
//            char servo = chars[1];
//            String aux = "";
//            for (int i = 2 ; i < chars.length; i++) {
//                aux = aux + chars[i];
//            }
//            Log.i("asd", aux + " servo = " + servo);
//
//
//        }
    }
}