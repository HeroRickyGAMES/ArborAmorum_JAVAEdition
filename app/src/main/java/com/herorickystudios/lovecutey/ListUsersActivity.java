package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Programado por HeroRickyGames

public class ListUsersActivity extends AppCompatActivity {

    private cards cards_data[];
    private ArrayAdapter arrayAdapter;
    private int i;

    String SexoProcura;
    String username;
    String cidadeUsuario;
    String sexoUsuario;
    String Bio;
    String isFistTime;

    private String TestString = "";

    private boolean testMode = true;

    String APM;

    private String nameUser;

    private FirebaseFirestore usersDb;

    private static final int PERMISSIONS_FINE_LOCATION = 99;

    LocationRequest locationRequest;

    SwipeFlingAdapterView flingContainer;

    ListView listView;
    List<cards> rowItems;

    private String idadeUsuario;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;
    private String profileURI;
    private String idadeLimite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        startService(new Intent(getBaseContext(), PushNotificationService.class));

        String tst = getString(R.string.testModeAction);

        testMode = Boolean.parseBoolean(tst);


        //Test Mode Verificador
        if(testMode == true){
            TestString = "IMG_16_9_APP_INSTALL#";
        }else if(testMode == false){
            TestString = "";
        }

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UID = user.getUid();

        usersDb = FirebaseFirestore.getInstance();

        //Esconde a action Bar
        getSupportActionBar().hide();

        checkUserSex();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);
        //@InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                rowItems.remove(0);
            }

            //Pu lado
            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

                cards obj = (cards) dataObject;

                //UserID do ou da pretendente
                String userIdE = obj.getUserID();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                System.out.println(oppositeUserSex);

                String UID = user.getUid();

                //DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference("Usuarios").child(oppositeUserSex).child(userIdE);

                //Database push
                //DatabaseReference reference = referencia.getReference();

                DocumentReference maleDb =  usersDb.collection("Usuarios").document(user.getUid());

                maleDb.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isComplete()){
                            DocumentSnapshot document = task.getResult();

                            if(document.exists()){
                                nameUser = document.getString("username");

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }

            //Pu oto
            @Override
            public void onRightCardExit(Object dataObject) {

                cards obj = (cards) dataObject;

                //UserID do ou da pretendente
                String userIdE = obj.getUserID();

                isConnectiomMatch(UID, userIdE);

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                System.out.println(oppositeUserSex);

                String UID = user.getUid();



                DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference("Usuarios").child(oppositeUserSex).child(userIdE);

                maleDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        //Aqui eu faço o Sim, ainda não finalizei


                        //usersDb.child(oppositeUserSex).child(userIdE).child("connections").child("yeps").child(UID).setValue(true);

                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                //View view = flingContainer.getSelectedView();
                //view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                //view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Clicou para iniciar uma conversa.
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                cards obj = (cards) dataObject;

                //UserID do ou da pretendente
                String userIdE = obj.getUserID();

                isConnectiomMatch(UID, userIdE);

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                System.out.println(oppositeUserSex);

                String UID = user.getUid();



                /*DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference("Usuarios").child(oppositeUserSex).child(userIdE);

                maleDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        usersDb.child(oppositeUserSex).child(userIdE).child("connections").child("yeps").child(UID).setValue(true);
                        usersDb.child(userSex).child(UID).child("connections").child("yeps").child(UID).setValue(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                DatabaseReference chat = FirebaseDatabase.getInstance().getReference().child("Chat");
                DatabaseReference currentUserConections = usersDb.child(userSex).child(UID).child("connections").child("yeps").child(userIdE);
                DatabaseReference nameDB = usersDb.child(userSex).child(UID);

                nameDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot3) {
                        currentUserConections.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists() == false){


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
                                    String AMPM = APM;



                                    String Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND);

                                    Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND);


                                    System.out.println("x *" + Hora.length());


                                    String DATA_HORA = Day + Mes + Ano + "➧" + AMPM + Hora;


                                    String name = snapshot3.child("nome").getValue().toString();

                                    String IDChat = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                                    usersDb.child(oppositeUserSex).child(snapshot.getKey()).child("connections").child("matches").child(UID).setValue(true);
                                    usersDb.child(oppositeUserSex).child(snapshot.getKey()).child("connections").child("matches").child(UID + " C").child("ChatId").setValue(IDChat);
                                    //chat.child(IDChat).setValue(name + ": Fez o Match!");


                                    usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey()).setValue(true);
                                    usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey() + " C").child("ChatId").setValue(IDChat);
                                    usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey() + " C").child("isOnChat").setValue(false);

                                    //usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey()).child(UID).child(" C").setValue(IDChat);



                                    chat.child(IDChat).child(str + "/ Data: " + DATA_HORA + ", "+ name +  "﹁").setValue(": Criou o chat!");

                                    System.out.println("Current time " +  str);

                                    Toast.makeText(ListUsersActivity.this, "Chat criado!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), MatchesActivity.class);
                                    intent.putExtra("userSex", userSex);
                                    intent.putExtra("oppositeUserSex", oppositeUserSex);
                                    startActivity(intent);



                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

*/

            }
        });

        //Codigos de localização
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 30);
        locationRequest.setFastestInterval(1000 * 5);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Esconde a action Bar
        getSupportActionBar().hide();

        updateGPS();

    }

    private void isConnectiomMatch(String UID, String userIdE) {
        /*
        DatabaseReference chat = FirebaseDatabase.getInstance().getReference().child("Chat");
        DatabaseReference currentUserConections = usersDb.child(userSex).child(UID).child("connections").child("yeps").child(userIdE);
        DatabaseReference nameDB = usersDb.child(userSex).child(UID);

        nameDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot3) {
                currentUserConections.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){


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
                            String AMPM = APM;



                            String Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND);

                            Hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND);


                            System.out.println("x *" + Hora.length());


                            String DATA_HORA = Day + Mes + Ano + "➧" + AMPM + Hora;



                            //Exibe o novo match!
                            Toast.makeText(ListUsersActivity.this, "Novo Match!", Toast.LENGTH_SHORT).show();

                            String name = snapshot3.child("nome").getValue().toString();

                            String IDChat = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                            usersDb.child(oppositeUserSex).child(snapshot.getKey()).child("connections").child("matches").child(UID).setValue(true);
                            usersDb.child(oppositeUserSex).child(snapshot.getKey()).child("connections").child("matches").child(UID + " C").child("ChatId").setValue(IDChat);
                            //chat.child(IDChat).setValue(name + ": Fez o Match!");


                            usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey()).setValue(true);
                            usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey() + " C").child("ChatId").setValue(IDChat);
                            usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey() + " C").child("isOnChat").setValue(false);
                            //usersDb.child(userSex).child(UID).child("connections").child("matches").child(snapshot.getKey()).child(UID).child(" C").setValue(IDChat);



                            chat.child(IDChat).child(str + "/ Data: " + DATA_HORA + ", "+ name +  "﹁").setValue(": Fez o Match!");

                            System.out.println("Current time " +  str);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }



    private String userSex;
    private String notUserSex;
    private String oppositeUserSex;
    private String oth = "Feminino";

    public void checkUserSex() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference onScreen =  usersDb.collection("Usuarios").document(user.getUid());

        onScreen.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()){
                        SexoProcura = document.getString("sexoDeProcura");
                        username = document.getString("username");
                        cidadeUsuario = document.getString("cidade");
                        sexoUsuario = document.getString("Genero");
                        profileURI = document.getString("profileUri");
                        idadeLimite = document.getString("IdadeLimite");

                        System.out.println("Informações do Usuario");
                        System.out.println(SexoProcura);
                        System.out.println(username);
                        System.out.println(cidadeUsuario);
                        System.out.println(sexoUsuario);
                        //SHARED PREFERENCES PARA REDUZIR O TAMANHO DO CODIGO!
                        SharedPreferences prefs = getSharedPreferences("userPreferencias", MODE_PRIVATE);

                        SharedPreferences.Editor editor = prefs.edit();


                        editor.putString("nome", username);

                        editor.putString("SexoProcura", SexoProcura);
                        editor.putString("sexoUsuario", sexoUsuario);
                        editor.putString("cidadeUsuario", cidadeUsuario);
                        editor.commit();

                        userSex = "Masculino";
                        oppositeUserSex = SexoProcura;
                        getOppositeSexUsers();

                        Map<String, Object> edicao = new HashMap<>();
                        edicao.put("isOnline", false);


                        getOppositeSexUsers();

                        onScreen.update(edicao).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getOppositeSexUsers() {
        CollectionReference onScreen =  usersDb.collection("Usuarios");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UID = user.getUid();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location == null){

                        Toast.makeText(ListUsersActivity.this, "O seu GPS está desativado! Por favor, ative o GPS para conseguir usar o Arbor Amorum!", Toast.LENGTH_LONG).show();
                        Toast.makeText(ListUsersActivity.this, "Clique no FAB que centraliza a localização, pós isso, volte ao aplicativo!", Toast.LENGTH_LONG).show();

                        Uri uri = Uri.parse("https://www.google.pt/maps");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                    Geocoder geocoder = new Geocoder(ListUsersActivity.this);

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String cidade = addresses.get(0).getSubAdminArea();

                        usersDb.collection("Usuarios").whereEqualTo("Genero", SexoProcura).whereEqualTo("cidade", cidade).whereEqualTo("sexoDeProcura", sexoUsuario).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()){


                                String data = dataSnapshot.getId();

                                System.out.println("UIDS: " + data);

                                System.out.println("Dados recuperados: "+ dataSnapshot.get("username"));

                                String nameuser = dataSnapshot.get("username").toString().replaceAll(username, "");
                                String userIDo = dataSnapshot.get("id").toString();
                                String profURI = dataSnapshot.get("profileUri").toString().replaceAll(profileURI, "");
                                String idade = dataSnapshot.get("idade").toString();
                                String cidade = dataSnapshot.get("cidade").toString();
                                String bio = dataSnapshot.get("bio").toString();

                                //O que mostra na interface
                                cards Item = new cards("£" + userIDo + "£", nameuser, profURI, idade, cidade, bio);

                                if(Integer.valueOf(idade) <= Integer.valueOf(idadeLimite)){

                                    rowItems.add(Item);
                                    arrayAdapter.notifyDataSetChanged();

                                }

                            }
                        }
                    });

                }
            });
        }

    }
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    Geocoder geocoder = new Geocoder(ListUsersActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String cidade = addresses.get(0).getSubAdminArea();

                        //Codigos de registro

                        System.out.println("LOCALIZAÇÃO EXATA: " + cidade);
                    } catch (Exception e) {
                        System.out.println("Não foi possivel encontrar sua localização!" + e);
                    }
                }
            });

        } else {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_FINE_LOCATION);
            }

        }
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(ListUsersActivity.this, ConfiguracoesActivity.class);
        startActivity(intent);
    }

    public void matchAc(View view){
        Intent intent = new Intent(this, MatchesActivity.class);
        intent.putExtra("userSex", userSex);
        intent.putExtra("oppositeUserSex", oppositeUserSex);
        startActivity(intent);
    }

    @Override
    protected void onStop() {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UID = user.getUid();

        DatabaseReference recoverydbFM = FirebaseDatabase.getInstance().getReference("Usuarios").child(userSex).child(UID);

        recoverydbFM.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    recoverydbFM.child("isOnline").setValue("false");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        super.onStop();
    }

    @Override
    protected void onPause() {


        super.onPause();
    }

    @Override
    protected void onRestart() {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPreferencias", Context.MODE_PRIVATE);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UID = user.getUid();

        DatabaseReference recoverydbFM = FirebaseDatabase.getInstance().getReference("Usuarios").child(userSex).child(UID);

        recoverydbFM.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    recoverydbFM.child("isOnline").setValue("true");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onRestart();
    }

    @Override
    protected void onDestroy() {

        startService(new Intent(getBaseContext(), PushNotificationService.class));


        super.onDestroy();
    }
}