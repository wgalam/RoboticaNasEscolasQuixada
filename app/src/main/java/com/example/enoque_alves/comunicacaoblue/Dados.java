package com.example.enoque_alves.comunicacaoblue;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Dados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);

        AlertDialog.Builder dialago = new AlertDialog.Builder(this);
        Intent it = getIntent();
        String texto = it.getStringExtra("nome");
        dialago.setTitle("Enviado " + texto);
        dialago.setNeutralButton("ok", null);
        dialago.show();
    }
}
