package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.herorickystudios.lovecutey.ui.login.logiActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Programado por HeroRickyGames

public class ListUsersActivity extends AppCompatActivity {

    private cards cards_data[];
    private ArrayAdapter arrayAdapter;
    private int i;

    private static final int PERMISSIONS_FINE_LOCATION = 99;

    LocationRequest locationRequest;

    SwipeFlingAdapterView flingContainer;

    ListView listView;
    List<cards> rowItems;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);



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

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(ListUsersActivity.this, "Pu lado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                Toast.makeText(ListUsersActivity.this, "Pro oto", Toast.LENGTH_SHORT).show();

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


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(ListUsersActivity.this, "Foi clicado!", Toast.LENGTH_SHORT).show();
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

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public void deslogbtn(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, logiActivity.class);
        startActivity(intent);
        finish();
        return;
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
                    userSex = "Masculino";
                    oppositeUserSex = "Feminino";
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

        DatabaseReference femDB = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Feminino");
        femDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    userSex = "Feminino";
                    oppositeUserSex = "Masculino";
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
        DatabaseReference homoDB = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Homem_Homossexual");
        homoDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    userSex = "Homem_Homossexual";
                    oppositeUserSex = "Homem_Homossexual";
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

        DatabaseReference homo2DB = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Mulher_Homossexual");
        homo2DB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    userSex = "Mulher_Homossexual";
                    oppositeUserSex = "Mulher_Homossexual";
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

        DatabaseReference bissexDB = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Bissexual");
        bissexDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.getKey().equals(user.getUid())) {
                    userSex = "Bissexual";
                    oppositeUserSex = "Bissexual";
                    bi();

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


                    DatabaseReference opositeSexDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(oppositeUserSex);
                    opositeSexDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            if (snapshot.exists()) {


                                //Tive dificuldades de adicionar mais adiconei como uma String e assim foi!

                                cards Item = new cards(snapshot.getKey(), (String) snapshot.child(cidade).child("Dados do Usuario").child("nome").getValue(), snapshot.child(cidade).child("Dados do Usuario").child("nome").getValue().toString(), snapshot.child(cidade).child("Dados do Usuario").child("idade").getValue().toString(), snapshot.child(cidade).child("Dados do Usuario").child("Cidade").getValue().toString());
                                //cards Item = new cards((String) snapshot.child(cidade).child("Dados do Usuario").child("nome").getValue(), snapshot.child(cidade).child("Dados do Usuario").child("nome").getValue().toString() + ", "
                                        //+ snapshot.child(cidade).child("Dados do Usuario").child("idade").getValue(), snapshot.child(cidade).child("Dados do Usuario").child("idade").getValue().toString() + " anos");

                                String idadeteste = snapshot.child("Guarulhos").child("Dados do Usuario").child("idade").getValue().toString();

                                rowItems.add(Item);
                                arrayAdapter.notifyDataSetChanged();

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
            });
        }

    }

    public void bi() {

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


                    DatabaseReference opositeSexDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(oppositeUserSex);
                    opositeSexDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            if (snapshot.exists()) {


                                //Tive dificuldades de adicionar mais adiconei como uma String e assim foi!
                                //Bugs acontecem e isso foi, porem tá corrigodo!!
                                cards Item = new cards(snapshot.getKey(), (String) snapshot.child(cidade).child("Dados do Usuario").child("nome").getValue(), snapshot.child(cidade).child("Dados do Usuario").child("nome").getValue().toString(), snapshot.child(cidade).child("Dados do Usuario").child("idade").getValue().toString(), snapshot.child(cidade).child("Dados do Usuario").child("Cidade").getValue().toString());
                                rowItems.add(Item);
                                arrayAdapter.notifyDataSetChanged();

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

        } else {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_FINE_LOCATION);
            }

        }
    }
}