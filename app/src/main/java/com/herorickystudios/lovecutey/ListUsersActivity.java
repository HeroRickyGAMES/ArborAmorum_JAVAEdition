package com.herorickystudios.lovecutey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListUsersActivity extends AppCompatActivity {

    TextView nome, idade, cidade;
    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("usuario");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        //Esconde a action Bar
        getSupportActionBar().hide();

        nome = findViewById(R.id.textNomeUser);
        idade = findViewById(R.id.textIdadeUser);
        cidade = findViewById(R.id.textLocalização);

    }


}