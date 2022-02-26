package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Programado por HeroRickyGames

public class ListUsersActivity extends AppCompatActivity {

    TextView nome, idade, cidade;
    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        //Esconde a action Bar
        getSupportActionBar().hide();

        nome = findViewById(R.id.textNomeUser);
        idade = findViewById(R.id.textIdadeUser);
        cidade = findViewById(R.id.textLocalização);

        usernamessc();

    }

    public void usernamessc(){

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String refer = snapshot.getValue().toString();

                //Ele está lendo os dados diretamente do Banco de Dados, ainda não fiz a lista, porem logo vou fazer!
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                   String UIDO =  user.setUserID(dataSnapshot.getKey());

                  String nomeud =  snapshot.child(UIDO).child("Dados do Usuario").child("nome").getValue().toString();
                  String idadeud =  snapshot.child(UIDO).child("Dados do Usuario").child("idade").getValue().toString();

                  nome.setText(nomeud);
                  idade.setText(idadeud);

                    System.out.println(nomeud);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}