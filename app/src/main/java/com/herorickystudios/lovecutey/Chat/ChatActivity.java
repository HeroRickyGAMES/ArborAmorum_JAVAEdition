package com.herorickystudios.lovecutey.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    chatAdapter adapter;
    ArrayList<cardsChat> list;
    private RecyclerView.LayoutManager ChatLayoutManager;

    private String UIDcurrent, matchId, chatId, MatchIDD, nameoposite, isOnline;
    int intero = 1;
    Boolean clicou = false;

    TextView opositeUserNameI, onlineTextView;

    String APM;

    public List<String> tagArray;

    private EditText mandarEditText;
    private Button Mandar;

    String currentUserID = FirebaseAuth.getInstance().getUid();

    private DatabaseReference usersDb, chatID, chatdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //Esconde a action Bar
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerChat);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        opositeUserNameI = findViewById(R.id.userNameInterfece);
        onlineTextView = findViewById(R.id.onlineTextView);

        list = new ArrayList<cardsChat>();
        adapter = new chatAdapter(this, list);
        recyclerView.setAdapter(adapter);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        chatdb = FirebaseDatabase.getInstance().getReference().child("Chat");

        String UserSexMasc = "Masculino";
        String UserSexFem = "Feminino";

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);


        String nomeUser = prefs.getString("nome", "");
        String sexoProcura = prefs.getString("SexoProcura", "");
        String cidadeUsuario = prefs.getString("cidadeUsuario", "");
        String sexoUser = prefs.getString("sexoUsuario", "");
        String clicktimere = prefs.getString("clicktime", "");

        UIDcurrent = FirebaseAuth.getInstance().getCurrentUser().getUid();

        matchId = getIntent().getExtras().getString("matchId");
        nameoposite = getIntent().getExtras().getString("nameOposite");
        isOnline = getIntent().getExtras().getString("isOnline");

        MatchIDD = getIntent().getExtras().getString("MatchIdd");

        mandarEditText = findViewById(R.id.menssage);
        Mandar = findViewById(R.id.send);

        opositeUserNameI.setText(nameoposite);
        String currentUserID = FirebaseAuth.getInstance().getUid();



        Mandar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMenssage();

                String UserSexMasc = "Masculino";
                String UserSexFem = "Feminino";

                chatdb = FirebaseDatabase.getInstance().getReference().child("Chat");

                DatabaseReference nameDB = usersDb.child(sexoUser).child(UIDcurrent);
                nameDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            int soma = intero = intero + 1;

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("clicktime", String.valueOf(intero));
                            editor.commit();

                            Calendar c = Calendar.getInstance();
                            String str = c.getTime().toString();

                            String Day = String.valueOf(c.get(Calendar.DATE)) + "↔";
                            String Mes = String.valueOf(c.get(Calendar.MONTH)) + "↔";
                            String Ano = String.valueOf(c.get(Calendar.YEAR)) + " ";

                            int PMAM = c.get(Calendar.AM_PM);

                            if(PMAM == 0){
                                APM = "AM ";
                            }else if(PMAM == 1){
                                APM = "PM ";
                            }

                            System.out.println(intero);

                            String AMPM = APM;



                            String Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND);

                            if(Hora.length() == 8){

                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ intero;

                            }else if(Hora.length() == 7){
                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ intero+intero;
                            }else if(Hora.length() == 6){
                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ intero+intero+intero;
                            }else if(Hora.length() == 5){
                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ intero+intero+intero+intero;
                            }else if(Hora.length() == 4){
                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ intero+intero+intero+intero+intero;
                            }else if(Hora.length() == 3){
                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ intero+intero+intero+intero+intero+intero;
                            }else if(Hora.length() == 2){
                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ intero+intero+intero+intero+intero+intero+intero;
                            }else if(Hora.length() == 1){
                                Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND)+ "0"+"0"+"0"+"0"+"0"+"0"+"0"+"0";
                            }

                            System.out.println("x *" + Hora.length());


                            String DATA_HORA = Day + Mes + Ano + "➧" + AMPM + Hora;

                            String nome = snapshot.child("nome").getValue().toString();
                            String ConexionMatch = snapshot.child("connections").child("matches").child(matchId + " C").child("ChatId").getValue().toString();



                            System.out.println(DATA_HORA);


                            String texto =  mandarEditText.getText().toString();


                            System.out.println(nomeUser + ": "+ texto);





                            if(texto.isEmpty()){

                                Toast.makeText(ChatActivity.this, "Não é possivel enviar menssagens de texto vazias!", Toast.LENGTH_SHORT).show();


                            }else{

                                chatdb.child(ConexionMatch).child(str +  "/ Data: " + DATA_HORA + ", "+ nomeUser + "﹁").setValue(texto);

                            }

                            if(texto.equals(texto)){

                                mandarEditText.setText("");

                                clicou = true;

                                ck();

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        DatabaseReference nameDB = usersDb.child(sexoUser).child(UIDcurrent);
        nameDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String ConexionMatch = snapshot.child("connections").child("matches").child(matchId + " C").child("ChatId").getValue().toString();
                    messages(ConexionMatch);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(ChatLayoutManager);

        //ChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);



        //recyclerView.setAdapter(ChatAdapter);

    }


    private void sendMenssage() {

        String sendMenssageText = mandarEditText.getText().toString();

    }

    private void messages(String ConexionMatch){

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);

        String nomeUser = prefs.getString("nome", "");
        String sexoProcura = prefs.getString("SexoProcura", "");
        String cidadeUsuario = prefs.getString("cidadeUsuario", "");
        String sexoUser = prefs.getString("sexoUsuario", "");

        chatdb = FirebaseDatabase.getInstance().getReference().child("Chat").child(ConexionMatch);

        DatabaseReference nameDB = usersDb.child(sexoUser).child(UIDcurrent);
        //DatabaseReference PhotoDb = usersDb.child(sexoUser).child(UIDcurrent);

        chatdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){


                    //Database to frontend
                    String datadb = dataSnapshot.getKey();

                    String Menssage = dataSnapshot.toString().replace(",", " ").replace(datadb,"").replace("key", "").replace("{", "").replace("}", "").replace("=", " ").replace("DataSnapshot","").replace("]", "").replace("[", "").replace("value", "").replace("↔", "/").replace("﹁", ",");

                    String Datan = dataSnapshot.toString().replace(",", " ").replace(datadb,"").replace("key", "").replace("{", "").replace("}", "").replace("=", "").replace("DataSnapshot","").replace("]", "").replace("[", "").replace("value", "").replace("↔", "/").replace(nomeUser,"").replace(nameoposite,"").replaceAll("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzçéáãõ~´íÃÕÉÇÍÁ﹁!?,'<->=*.+ ]", "").replace("-", "").replace("➧"," ");

                    String msg = dataSnapshot.getValue().toString();

                    String UserMsg = dataSnapshot.toString().replaceAll("é", "=").replace(",", " ").replace(datadb,"").replace("key", "").replace("{", "").replace("}", "").replace("=", ": ").replace("DataSnapshot","").replace("]", "").replace("[", "").replace("value", "").replace("↔", "/").replace("➧","").replace(" ","➩").replaceAll("[" + Datan + "]", "").replace("-", " ").replace("﹁", ": ").replace("➩", " ").replace("Data  PM","");

                    String[] array = Menssage.split("\\s*, \\s* ,");

                    tagArray = Arrays.asList(array);

                    onlineTextView.setText(isOnline);


                    System.out.println("A data é: " + Datan + " A mensagem é " + msg);

                    //cardsChat chaatTxt = new cardsChat(Datan, UserMsg);


                    if(UserMsg.contains(nomeUser)){
                        cardsChat chaatTxt = new cardsChat(Datan,nomeUser +  ": " + msg, "https://firebasestorage.googleapis.com/v0/b/lovecutey-95cc0.appspot.com/o/balon%2FblconversaiconUser.png?alt=media&token=d51b0e2e-e294-46aa-ba7e-72b7307cd0fd");
                        list.add(chaatTxt);
                    }
                    if(UserMsg.contains(nameoposite)){
                        cardsChat chaatTxt = new cardsChat(Datan, nameoposite +  ": " + msg, "https://firebasestorage.googleapis.com/v0/b/lovecutey-95cc0.appspot.com/o/balon%2FblconversaiconOposite.png?alt=media&token=34bb37c7-ab61-4e91-ad02-e4fecd2b5174");
                        list.add(chaatTxt);
                    }

                    System.out.println("O nome do usuario contem dentro da string " + UserMsg.contains(nomeUser));
                    System.out.println("O nome do opositor contem dentro da string " + UserMsg.contains(nameoposite));

                    //list.add(chaatTxt);

                    //Users users = dataSnapshot.getValue(Users.class);


                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ck (){
        if(clicou == true){

            System.out.println("Clicado");

            //list.clear();
        }
    }
}