package com.herorickystudios.lovecutey;

//Programado por HeroRickyGames

//Serviço de notificações do app!
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;


public class PushNotificationService extends Service {

    private DatabaseReference usersDb, chatID, chatdb;
    private String isOnChat;
    private String matchKey;
    private String ConexionMatch;
    private String userName;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UID = user.getUid();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);

        String sexoProcura = prefs.getString("SexoProcura", "");
        String sexoUser = prefs.getString("sexoUsuario", "");


        DatabaseReference recoverydbFM = FirebaseDatabase.getInstance().getReference("Usuarios").child(sexoUser).child(UID).child("connections").child("matches");

        usersDb = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        recoverydbFM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {

                for(DataSnapshot dataSnapshot2 : snapshot2.getChildren()){



                    DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(sexoUser).child(UID).child("connections").child("matches");
                    matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for(DataSnapshot match : snapshot.getChildren()){

                                    matchKey = match.getKey().replaceAll(" C", "");

                                    ConexionMatch = snapshot.child(matchKey + " C").child("ChatId").getValue().toString();
                                    isOnChat = snapshot.child(matchKey + " C").child("isOnChat").getValue().toString();

                                    System.out.println("Está no chat? "+ ConexionMatch);


                                    //ConexionMatch = snapshot.child(matchKey + " C").child("ChatId").getValue().toString();
                                    //isOnChat = snapshot.child(matchKey + " C").child("isOnChat").getValue().toString();



                                    System.out.println(matchKey);

                                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);

                                    String nomeUser = prefs.getString("nome", "");
                                    String sexoProcura = prefs.getString("SexoProcura", "");
                                    String cidadeUsuario = prefs.getString("cidadeUsuario", "");
                                    String sexoUser = prefs.getString("sexoUsuario", "");

                                    //System.out.println("tste "+dataSnapshot.getValue().toString());

                                    chatdb = FirebaseDatabase.getInstance().getReference().child("Chat").child(ConexionMatch);


                                    DatabaseReference nameDB = usersDb.child(sexoUser).child(UID);
                                    //DatabaseReference PhotoDb = usersDb.child(sexoUser).child(UIDcurrent);

                                    chatdb.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            if(snapshot.exists()){
                                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    System.out.println("O valor do está no chat? é: " + isOnChat);

                                                    //Database to frontend
                                                    String datadb = dataSnapshot.getKey();

                                                    String Menssage = dataSnapshot.toString().replace(",", " ").replace(datadb, "").replace("key", "").replace("{", "").replace("}", "").replace("=", " ").replace("DataSnapshot", "").replace("]", "").replace("[", "").replace("value", "").replace("↔", "/").replace("﹁", ",");


                                                    System.out.println(Menssage);

                                                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);

                                                    userName = prefs.getString("nome", "");



                                                    String msg = dataSnapshot.getValue().toString();

                                                    if(Menssage.contains(msg)){
                                                        if(Menssage.contains(userName)){



                                                        }
                                                    }
                                                    System.out.println("Menssagem : " + msg);

                                                    if(Menssage.contains(msg)){

                                                        if(!Menssage.contains(userName)){

                                                            if(isOnChat.equals("false")){

                                                                System.out.println(isOnChat);

                                                                final String CHANNEL_ID = "HANDS_UP_NOTIFICATION";
                                                                NotificationChannel channel = null;
                                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                    channel = new NotificationChannel(
                                                                            CHANNEL_ID,
                                                                            "Hands Up Notification",
                                                                            NotificationManager.IMPORTANCE_DEFAULT
                                                                    );
                                                                }

                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        getSystemService(NotificationManager.class).createNotificationChannel(channel);
                                                                    }
                                                                }
                                                                Notification.Builder builder = null;
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                    builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                                                                            .setContentTitle("Chegou uma nova Mensagem! Verifique nos chats!!")
                                                                            .setContentText(msg)
                                                                            .setSmallIcon(R.drawable.hearticon)
                                                                            .setAutoCancel(true);
                                                                }

                                                                NotificationManagerCompat.from(getApplicationContext()).notify(1,builder.build());

                                                            }
                                                        }
                                                    }
                                                }
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
                            }
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

        System.out.println("Serviço rodando de fundo!!!");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}