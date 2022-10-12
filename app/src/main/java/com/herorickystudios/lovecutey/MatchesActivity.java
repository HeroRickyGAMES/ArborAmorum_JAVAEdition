package com.herorickystudios.lovecutey;

//Programado por HeroRickyGames

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter MatchesAdaper;
    private RecyclerView.LayoutManager MatchesLayoutManager;
    private boolean online;
    private AdView adView;
    private String TestString = "";
    private String isOnline;
    private String on;

    private boolean testMode = true;

    private String UIDcurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        //Esconde a action Bar
        getSupportActionBar().hide();

        UIDcurrent = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String userSex = getIntent().getExtras().getString("userSex");
        String oppositeUserSex = getIntent().getExtras().getString("oppositeUserSex");

        recyclerView = findViewById(R.id.recyclerChat);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        MatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        recyclerView.setLayoutManager(MatchesLayoutManager);

        MatchesAdaper = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);

        recyclerView.setAdapter(MatchesAdaper);

        getUserMatchId(userSex, oppositeUserSex);


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


    }

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();

    public List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }


    public void getUserMatchId(String userSex, String oppositeUserSex){


        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userSex).child(UIDcurrent).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot match : snapshot.getChildren()){



                        FecthMatchInformation(match.getKey(), oppositeUserSex);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FecthMatchInformation(String key, String oppositeUserSex) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(oppositeUserSex).child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userID = snapshot.getKey();
                    String name = snapshot.child("nome").getValue().toString();

                    isOnline = snapshot.child("isOnline").getValue().toString();

                    if(isOnline.equals("true")){

                        online = true;

                    }else if(isOnline.equals("false")){

                        online = false;

                    }

                    if(online == true){

                        on = "Online";

                    }else if(online == false){

                        on = "Offline";

                    }

                    String profileUrl = snapshot.child("profileImageUri").child(userID).getValue().toString();

                    MatchesObject obj = new MatchesObject(userID, name, profileUrl, on);
                    resultsMatches.add(obj);
                    MatchesAdaper.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}