package com.example.enoque_alves.comunicacaoblue;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TelaDeProgramacao extends AppCompatActivity {
    private Button abreFecha, sobeDesce, girar, avancaRecua, salvar, apagar, testar;
    private ListView listaComandos;
    private EditText valor, delay;
    private ArrayList<String> comandos = new ArrayList<String>();
    private ArrayList<String> comandosTela = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private int valorPassado;
    private EnviaDados enviarDados = EnviaDados.getEnviaDados();
    private int delayValor = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_programacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent it = getIntent();
        comandos = it.getStringArrayListExtra("comandos");
        Log.i("asd", "tamanho = " + comandos.size());
        comandosTela = it.getStringArrayListExtra("comandosTela");
        abreFecha = (Button) findViewById(R.id.telaDeProgramacao_abreFecha);
        girar = (Button) findViewById(R.id.telaDeProgramacao_girar);
        sobeDesce = (Button) findViewById(R.id.telaDeProgramacao_sobeDesce);
        avancaRecua = (Button) findViewById(R.id.telaDeProgramacao_avancaRecua);
        salvar = (Button) findViewById(R.id.telaDeProgramacao_salvar);
        apagar = (Button) findViewById(R.id.telaDeProgramacao_apagar);
        testar = (Button) findViewById(R.id.telaDeProgramacao_testar);
        listaComandos = (ListView) findViewById(R.id.telaDeProgramacao_lista_de_comandos);
        delay = (EditText) findViewById(R.id.telaDeProgramacao_delay);
        valor = (EditText) findViewById(R.id.telaDeProgramacao_ValorEnviado);
        adapter = new ArrayAdapter<String>(TelaDeProgramacao.this, android.R.layout.simple_list_item_1, comandosTela);
        listaComandos.setAdapter(adapter);
        abreFecha.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (valor.getText().length() > 0){
                    valorPassado = Integer.parseInt(valor.getText().toString());
                    String comandoTela = "abre/fecha   " + valorPassado;
                    comandosTela.add(comandoTela);
                    valorPassado = (int) (valorPassado * 0.57)+10;
                    comandos.add("!a" + valorPassado);
                    listaComandos.setAdapter(adapter);
                    valor.setText("");
                    if (delay.getText().length() > 0){
                        delayValor = Integer.parseInt(delay.getText().toString());
                        delay.setText("");
                    }
                }
                else{
                    Log.i("asd", "a + "+ "");

                }
            }
        });

        girar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (valor.getText().length() != 0){
                    valorPassado = Integer.parseInt(valor.getText().toString());
                    String comandoTela = "girar   " + valorPassado;
                    comandosTela.add(comandoTela);
                    valorPassado = (int) (valorPassado * 1.8)+10;
                    comandos.add("!b" + valorPassado);
                    listaComandos.setAdapter(adapter);
                    if (delay.getText().length() > 0){
                        delayValor = Integer.parseInt(delay.getText().toString());
                        //delay.setText("");
                    }
                }
            }
        });
        avancaRecua.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (valor.getText().length() != 0){
                    valorPassado = Integer.parseInt(valor.getText().toString());
                    String comandoTela = "avanca/recua   " + valorPassado;
                    comandosTela.add(comandoTela);
                    valorPassado = (int) (valorPassado * 1.2)+60;
                    comandos.add("!c" + valorPassado);
                    listaComandos.setAdapter(adapter);
                    if (delay.getText().length() > 0){
                        delayValor = Integer.parseInt(delay.getText().toString());
                        delay.setText("");
                    }
                }
            }
        });
        sobeDesce.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (valor.getText().length() != 0){
                    valorPassado = Integer.parseInt(valor.getText().toString());
                    String comandoTela = "sobe/desce   " + valorPassado;
                    comandosTela.add(comandoTela);
                    valorPassado = (int) (valorPassado * 0.7)+60;
                    comandos.add("!d" + valorPassado);
                    listaComandos.setAdapter(adapter);
                    if (delay.getText().length() > 0){
                        delayValor = Integer.parseInt(delay.getText().toString());
                        delay.setText("");
                    }
                }
            }
        });
        apagar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (comandos.size() > 0 && comandosTela.size() > 0){
                    comandosTela.remove(comandosTela.size()-1);
                    comandos.remove(comandos.size()-1);
                    listaComandos.setAdapter(adapter);

                }
            }
        });

        salvar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent devolve = new Intent();
                devolve.putExtra("comandos", comandos);
                devolve.putExtra("comandosTela", comandosTela);
                devolve.putExtra("delay", delayValor);
                setResult(RESULT_OK, devolve);
                finish();
            }
        });

        testar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                enviarDados.enviarVariosComandos(comandos, delayValor);
            }
        });

    }


}
