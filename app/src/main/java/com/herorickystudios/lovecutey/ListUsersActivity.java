package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

//Programado por HeroRickyGames

public class ListUsersActivity extends AppCompatActivity {

    ArrayList<String> arrayListE;
    ArrayAdapter arrayAdapter;
    int n=0;

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

        arrayListE= new ArrayList<String >();
        arrayListE.add("UM");
        arrayListE.add("DOIS");
        arrayListE.add("TRÊS");
        arrayListE.add("QUAATRO");
        arrayListE.add("RECARREGA");
        SwipeFlingAdapterView swipeFlingAdapterView= (SwipeFlingAdapterView) findViewById(R.id.usersSwipe);


    arrayAdapter= new ArrayAdapter<String>(this, R.layout.detalhes,R.id.exibir, arrayListE);
    swipeFlingAdapterView.setAdapter(arrayAdapter);
    swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
        @Override
        public void removeFirstObjectInAdapter() {
            arrayListE.remove(0);
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLeftCardExit(Object o) {
            Toast.makeText(ListUsersActivity.this, "Movido para o lado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRightCardExit(Object o) {
            Toast.makeText(ListUsersActivity.this, "Movido paro oto", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdapterAboutToEmpty(int i) {

        }

        @Override
        public void onScroll(float v) {

        }
    });

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