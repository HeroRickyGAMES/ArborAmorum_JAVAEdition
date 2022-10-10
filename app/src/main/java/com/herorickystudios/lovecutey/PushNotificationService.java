package com.herorickystudios.lovecutey;

//Programado por HeroRickyGames

//Serviço de notificações do app!
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {

    private DatabaseReference usersDb, chatID, chatdb;
    private String UIDcurrent;
    @RequiresApi(api = Build.VERSION_CODES.O)
    //@Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);


        String nomeUser = prefs.getString("nome", "");
        String sexoProcura = prefs.getString("SexoProcura", "");
        String cidadeUsuario = prefs.getString("cidadeUsuario", "");
        String sexoUser = prefs.getString("sexoUsuario", "");
        String clicktimere = prefs.getString("clicktime", "");

        UIDcurrent = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference nameDB = usersDb.child(sexoUser).child(UIDcurrent);
        nameDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String ConexionMatch = dataSnapshot.child("connections").child("matches").getValue().toString();
                    //String ConexionMatch = "titulo";

                    System.out.println(ConexionMatch);
                    //messages(ConexionMatch);

                    Toast.makeText(PushNotificationService.this, ConexionMatch, Toast.LENGTH_LONG).show();
                    String titulo = remoteMessage.getNotification().getTitle();
                    String texto = remoteMessage.getNotification().getBody();

                    final String CHANNEL_ID = "HANDS_UP_NOTIFICATION";
                    NotificationChannel channel = new NotificationChannel(
                            CHANNEL_ID,
                            "Hands Up Notification",
                            NotificationManager.IMPORTANCE_HIGH
                    );

                    getSystemService(NotificationManager.class).createNotificationChannel(channel);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                            .setContentTitle(titulo)
                            .setContentText(ConexionMatch)
                            .setContentText("String ramdom aqui")
                            .setSmallIcon(R.drawable.hearticon)
                            .setAutoCancel(true);

                    NotificationManagerCompat.from(getApplicationContext()).notify(1,builder.build());

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
        super.onMessageReceived(remoteMessage);
    }
}
