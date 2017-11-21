package com.example.enoque_alves.comunicacaoblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class novo_comando extends AppCompatActivity {
    Button enviar;
    EditText comando, nome;
    String resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_comando);

        enviar= (Button) findViewById(R.id.enviar_novo_comando);
        nome = (EditText) findViewById(R.id.nome_comando);
        comando = (EditText) findViewById(R.id.valor_comando);

        enviar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                resultado = nome.getText() + "\n" + comando.getText();
                Intent devolve = new Intent();
                devolve.putExtra("resultado", resultado);
                setResult(RESULT_OK, devolve);
                Log.i("asd", "comando");
                finish();

            }

        });

    }
}
