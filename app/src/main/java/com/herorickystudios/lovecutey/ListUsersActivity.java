package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.herorickystudios.lovecutey.ui.login.logiActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

//Programado por HeroRickyGames

public class ListUsersActivity extends AppCompatActivity {

    private cards cards_data[];
    private ArrayAdapter arrayAdapter;
    private int i;

    String SexoProcura;
    String username;
    String cidadeUsuario;
    String sexoUsuario;

    private AdView adView;
    private String TestString = "";

    private boolean testMode = true;

    String APM;

    private String nameUser;

    private DatabaseReference usersDb;

    private static final int PERMISSIONS_FINE_LOCATION = 99;

    LocationRequest locationRequest;

    SwipeFlingAdapterView flingContainer;

    ListView listView;
    List<cards> rowItems;

    private String idadeUsuario;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        String tst = getString(R.string.testModeAction);

        testMode = Boolean.parseBoolean(tst);

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        //Test Mode Verificador
        if(testMode == true){
            TestString = "IMG_16_9_APP_INSTALL#";
        }else if(testMode == false){
            TestString = "";
        }

        adView = new AdView(this,  TestString + "826059172156140_826059262156131", AdSize.BANNER_HEIGHT_50);

// Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);

// Request an ad
        adView.loadAd();



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UID = user.getUid();

        usersDb = FirebaseDatabase.getInstance().getReference().child("Usuarios");

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

                DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference("Usuarios").child(oppositeUserSex).child(userIdE);

                maleDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nameUser = snapshot.child("nome").getValue().toString();
                        usersDb.child(oppositeUserSex).child(userIdE).child("connections").child("nope").child(UID).setValue(true);

                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

                        usersDb.child(oppositeUserSex).child(userIdE).child("connections").child("yeps").child(UID).setValue(true);

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



                DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference("Usuarios").child(oppositeUserSex).child(userIdE);

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

        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Masculino");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {

                    //Strings para usar no sharedpreferences e em outras areas do aplicativo
                    SexoProcura = snapshot.child("ConfiguracoesPessoais").child("sexoDeProcura").getValue().toString();
                    username = snapshot.child("nome").getValue().toString();
                    cidadeUsuario = snapshot.child("cidade").getValue().toString();
                    sexoUsuario = snapshot.child("Genero").getValue().toString();


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

                    maleDb.child(user.getUid()).child("isOnline").setValue("true");
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

        DatabaseReference femDB = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Feminino");
        femDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    SexoProcura = snapshot.child("ConfiguracoesPessoais").child("sexoDeProcura").getValue().toString();

                    username = snapshot.child("nome").getValue().toString();
                    cidadeUsuario = snapshot.child("cidade").getValue().toString();
                    sexoUsuario = snapshot.child("Genero").getValue().toString();
                    idadeUsuario = snapshot.child(cidadeUsuario).child("Dados do Usuario").child("idade").getValue().toString();


                    //SHARED PREFERENCES PARA REDUZIR O TAMANHO DO CODIGO!
                    SharedPreferences prefs = getSharedPreferences("userPreferencias", MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();


                    editor.putString("nome", username);
                    editor.putString("SexoProcura", SexoProcura);
                    editor.putString("sexoUsuario", sexoUsuario);
                    editor.putString("cidadeUsuario", cidadeUsuario);
                    editor.commit();


                    userSex = "Feminino";
                    oppositeUserSex = SexoProcura;
                    getOppositeSexUsers();

                    femDB.child(user.getUid()).child("isOnline").setValue("true");
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

        DatabaseReference biQ = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Bi Sexual");
        biQ.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    String SexoProcura = snapshot.child("ConfiguracoesPessoais").child("sexoDeProcura").getValue().toString();

                    userSex = "Bi Sexual";
                    oppositeUserSex = SexoProcura;
                    getOppositeSexUsers();
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
        DatabaseReference LesQ = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Lesbica");
        LesQ.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    String SexoProcura = snapshot.child("ConfiguracoesPessoais").child("sexoDeProcura").getValue().toString();

                    userSex = "Lesbica";
                    oppositeUserSex = SexoProcura;
                    getOppositeSexUsers();
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
        DatabaseReference GayQ = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Gay");
        GayQ.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    String SexoProcura = snapshot.child("ConfiguracoesPessoais").child("sexoDeProcura").getValue().toString();


                    System.out.println(SexoProcura);
                    userSex = "Gay";
                    oppositeUserSex = SexoProcura;
                    getOppositeSexUsers();
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

    public void getOppositeSexUsers() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UID = user.getUid();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    Geocoder geocoder = new Geocoder(ListUsersActivity.this);

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String cidade = addresses.get(0).getSubAdminArea();

                    DatabaseReference userConfig = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userSex).child(UID);
                    userConfig.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String idadeConfig = snapshot.child("ConfiguracoesPessoais").child("IdadeLimite").getValue().toString();
                            DatabaseReference opositeSexDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(oppositeUserSex);

                            opositeSexDb.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(UID) && !snapshot.child("connections").child("yeps").hasChild(UID) && snapshot.hasChild(cidade))    {




                                        //Tive dificuldades de adicionar mais adiconei como uma String e assim foi!
                                        String UIDEX = snapshot.getKey();

                                        String profileUrl = snapshot.child("profileImageUri").child(UIDEX).getValue().toString();

                                        //Idade Configurada pelo proprio usuario
                                        int idadeConfigC = Integer.parseInt(idadeConfig);

                                        String cidadeOposite = String.valueOf(snapshot.child("cidade").getValue());






                                        //Pega a idade do usuario para filtrar

                                        String idadeU = String.valueOf(snapshot.child(cidadeOposite).child("Dados do Usuario").child("idade").getValue());



                                        System.out.println(cidadeOposite);

                                        if (Integer.parseInt(idadeU) <= idadeConfigC) {

                                            cards Item = new cards(snapshot.getKey(), snapshot.child(cidade).child("Dados do Usuario").child("nome").getValue().toString(), profileUrl, idadeU, String.valueOf(snapshot.child("cidade").getValue()));

                                            rowItems.add(Item);
                                            arrayAdapter.notifyDataSetChanged();

                                        } else {

                                        }



                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    int primeiro_elemento = rowItems.size();

                                    int enesimo_elemento_multiplo_3 = 1000 - (1000 % 3);
                                    int enesimo_elemento_multiplo_5 = 1000 - (1000 % 5);
                                    int enesimo_elemento_multiplo_15 = 1000 - (1000 % 15);

                                    double n_multiplo_3 = Math.ceil(1000 / 3.0);
                                    double n_multiplo_5 = Math.ceil(1000 / 5.0);
                                    double n_multiplo_15 = Math.ceil(1000 / 15.0);

                                    double soma_multiplo_3 = (primeiro_elemento + enesimo_elemento_multiplo_3) * n_multiplo_3 / 2.0;
                                    double soma_multiplo_5 = (primeiro_elemento + enesimo_elemento_multiplo_5) * n_multiplo_5 / 2.0;
                                    double soma_multiplo_15 = (primeiro_elemento + enesimo_elemento_multiplo_15) * n_multiplo_15 / 2.0;

                                    double soma = soma_multiplo_3 + soma_multiplo_5 - soma_multiplo_15;

                                    System.out.println(soma_multiplo_5);


                                    if( rowItems.size() == (1000 % 5)){
                                        System.out.println("O multiplo é 5");
                                    }else{
                                        System.out.println("Não é");
                                    }

                                    System.out.println("O tamanho da lista é: " + rowItems.size());


                                    System.out.println("As informações estão sendo exibidas com sucesso!");
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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

                    System.out.println(location.getLatitude());
                    System.out.println(location.getLongitude());
                    System.out.println(location.getLongitude());




                    if (location.hasAltitude()) {
                        System.out.println("Latitude " + location.getLatitude());
                    } else {
                        System.out.println("Não disponivel");
                    }
                    if (location.hasSpeed()) {
                        System.out.println("Velocidade " + location.getSpeed());
                    } else {
                        System.out.println("Não disponivel");
                    }
                    if (location.hasAltitude()) {
                        System.out.println("Altitude " + location.getLatitude());
                    } else {
                        System.out.println("Não disponivel");
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    Geocoder geocoder = new Geocoder(ListUsersActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String cidade = addresses.get(0).getSubAdminArea();
                        Intent intent = new Intent(ListUsersActivity.this, ConfiguracoesActivity.class);
                        intent.putExtra("userSex", userSex);
                        intent.putExtra("cidade", cidade);
                        startActivity(intent);

                        //Codigos de registro

                        System.out.println("LOCALIZAÇÃO EXATA: " + cidade);
                    } catch (Exception e) {
                        System.out.println("Não foi possivel encontrar sua localização!" + e);
                    }

                    System.out.println(location.getLatitude());
                    System.out.println(location.getLongitude());
                    System.out.println(location.getLongitude());
                    System.out.println(location.getAccuracy());

                    if (location.hasAltitude()) {
                        System.out.println("Latitude " + location.getLatitude());
                    } else {
                        System.out.println("Não disponivel");
                    }
                    if (location.hasSpeed()) {
                        System.out.println("Velocidade " + location.getSpeed());
                    } else {
                        System.out.println("Não disponivel");
                    }
                    if (location.hasAltitude()) {
                        System.out.println("Altitude " + location.getLatitude());
                    } else {
                        System.out.println("Não disponivel");
                    }
                }
            });
        }
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
}