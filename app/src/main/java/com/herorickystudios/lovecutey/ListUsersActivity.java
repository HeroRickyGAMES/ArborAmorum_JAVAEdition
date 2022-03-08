package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

//Programado por HeroRickyGames

public class ListUsersActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        //Esconde a action Bar
        getSupportActionBar().hide();

        checkUserSex();

        al = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al);
        //@InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                al.remove(0);
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
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
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

    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
     }

     public void deslogbtn(View view){
         FirebaseAuth.getInstance().signOut();
         Intent intent = new Intent(this, logiActivity.class);
         startActivity(intent);
         finish();
         return;
     }
     private String userSex;
     private String notUserSex;
    private String oppositeUserSex;
     public void checkUserSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDb= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Masculino");
         maleDb.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                 if(snapshot.getKey().equals(user.getUid())){
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

         DatabaseReference femDB= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Feminino");
         femDB.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                 if(snapshot.getKey().equals(user.getUid())){
                     userSex = "Feminino";
                     notUserSex = "Masculino";
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
    public void getOppositeSexUsers(){

        DatabaseReference opositeSexDb= FirebaseDatabase.getInstance().getReference().child("Usuarios").child(oppositeUserSex);
        opositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               if(snapshot.exists()){

                   al.add(snapshot.child("Dados do Usuario").child("nome").getValue().toString());
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
}