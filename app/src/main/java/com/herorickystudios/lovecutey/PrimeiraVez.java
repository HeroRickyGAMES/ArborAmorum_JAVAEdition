package com.herorickystudios.lovecutey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.herorickystudios.lovecutey.ui.login.logiActivity;

public class PrimeiraVez extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeira_vez);

        //Esconde a action Bar
        getSupportActionBar().hide();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent(getApplicationContext(), logiActivity.class);
                startActivity(intent);
            }
        }, 5000);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioLogado != null){

            AbrirTelaInicial();

        }

    }

    public void AbrirTelaInicial(){
        Intent intent = new Intent(this,ListUsersActivity.class);
        startActivity(intent);
    }
}