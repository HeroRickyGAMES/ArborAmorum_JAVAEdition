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
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class PushNotificationService extends Service {

    private DatabaseReference usersDb, chatID, chatdb;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        chatdb = FirebaseDatabase.getInstance().getReference().child("Chat").child("-NGF15GWvDia5Qoc3kh5");

        chatdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String data = dataSnapshot.getValue().toString();

                    System.out.println(data);



                    }
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
                                .setContentTitle("titulo")
                                .setContentText(data)
                                .setSmallIcon(R.drawable.hearticon)
                                .setAutoCancel(true);
                    }

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

        System.out.println("Serviço rodando de fundo!!!");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}