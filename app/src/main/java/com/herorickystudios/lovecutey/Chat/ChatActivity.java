package com.herorickystudios.lovecutey.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.herorickystudios.lovecutey.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter ChatAdapter;
    private RecyclerView.LayoutManager ChatLayoutManager;

    private String UIDcurrent, matchId, chatId, MatchIDD;

    private EditText mandarEditText;
    private Button Mandar;

    private TextView msg;

    String currentUserID = FirebaseAuth.getInstance().getUid();

    private DatabaseReference usersDb, chatID, chatdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msg = findViewById(R.id.msg);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        chatdb = FirebaseDatabase.getInstance().getReference().child("Chat");

        String UserSexMasc = "Masculino";
        String UserSexFem = "Feminino";


        UIDcurrent = FirebaseAuth.getInstance().getCurrentUser().getUid();
        matchId = getIntent().getExtras().getString("matchId");

        MatchIDD = getIntent().getExtras().getString("MatchIdd");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        mandarEditText = findViewById(R.id.menssage);
        Mandar = findViewById(R.id.send);

        String currentUserID = FirebaseAuth.getInstance().getUid();

        Mandar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserSexMasc = "Masculino";
                String UserSexFem = "Feminino";

                chatdb = FirebaseDatabase.getInstance().getReference().child("Chat");

                DatabaseReference nameDB = usersDb.child(UserSexMasc).child(UIDcurrent);
                nameDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            Calendar c = Calendar.getInstance();
                            String str = c.getTime().toString();

                            String nome = snapshot.child("nome").getValue().toString();
                            String ConexionMatch = snapshot.child("connections").child("matches").child(matchId + " C").child("ChatId").getValue().toString();

                            String texto =  mandarEditText.getText().toString();

                            System.out.println(nome + ": "+ texto);
                            System.out.println(ConexionMatch);


                            chatdb.child(ConexionMatch).child(nome + " " + str).setValue(texto);



                        }else{
                            DatabaseReference nameDB = usersDb.child(UserSexFem).child(UIDcurrent);
                            nameDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot3) {

                                    Calendar c = Calendar.getInstance();
                                    String str = c.getTime().toString();

                                    String nome = snapshot3.child("nome").getValue().toString();

                                    String texto =  mandarEditText.getText().toString();

                                    String ConexionMatch = snapshot3.child("connections").child("matches").child(matchId + " C").child("ChatId").getValue().toString();

                                    System.out.println(nome + " : "+ texto);

                                    chatdb.child(ConexionMatch).child(nome + " " + str).setValue(texto);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







            }

        });


        sendMenssage();
        recyclerView.setHasFixedSize(true);
        ChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(ChatLayoutManager);

        ChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);

        recyclerView.setAdapter(ChatAdapter);

        getChatId();

    }


    private void sendMenssage() {
        String sendMenssageText = mandarEditText.getText().toString();

        if(sendMenssageText.isEmpty()){
            Toast.makeText(this, "Não é possivel enviar menssagens de texto vazias!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getChatId(){

    }

    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();

    public List<ChatObject> getDataSetChat() {
        return resultsChat;
    }


}