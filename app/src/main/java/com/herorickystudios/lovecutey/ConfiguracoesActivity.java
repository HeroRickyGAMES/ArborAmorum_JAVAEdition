package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Programado por HeroRickyGames

public class ConfiguracoesActivity extends AppCompatActivity {

    private EditText nameField, idadeField, bioField, idadeProcuraField;

    private Button voltarbtn, Confirmbtn;

    private ImageView profileImage;

    private FirebaseAuth Autenticacao;
    private DatabaseReference reference;

    private String userID, name, idade, procura, bio, profileImageUrl;

    private Uri resultadoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);




        String userSex = getIntent().getExtras().getString("userSex");
        String cidade = getIntent().getExtras().getString("cidade");
        nameField = findViewById(R.id.name);
        idadeField = findViewById(R.id.idade);
        bioField = findViewById(R.id.bio);
        idadeProcuraField = findViewById(R.id.idadePocura);

        profileImage = findViewById(R.id.profileImage);

        voltarbtn = findViewById(R.id.backBTN);
        Confirmbtn = findViewById(R.id.confirmBtn);

        Autenticacao = FirebaseAuth.getInstance();
        userID = Autenticacao.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userSex).child(userID).child(cidade).child("Dados do Usuario");

        getUserInfo();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        Confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        voltarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();


                    String nome = snapshot.child("nome").getValue().toString();
                    String bio = snapshot.child("bio").getValue().toString();
                    String idade = snapshot.child("idade").getValue().toString();



                    if(map.get("nome") != null){
                        name = map.get("nome").toString();
                        nameField.setText(nome);
                    }
                    if(map.get("idade") != null){
                        idadeField.setText(idade);
                    }
                    if(map.get("bio") != null){

                        bioField.setText(bio);

                    }
                    if(map.get("idadeProcura") != null){
                        idade = map.get("bio").toString();
                        nameField.setText(procura);
                    }



                    StorageReference filepath = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(userID);
                    String URL = "https://firebasestorage.googleapis.com/v0/b/lovecutey-95cc0.appspot.com/o/" +  "ProfileImages" + "%2F" + userID + "?alt=media";
                    Glide.with(getApplication()).load(URL).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void saveUserInformation() {
    name = nameField.getText().toString();
    idade = idadeField.getText().toString();
    bio = bioField.getText().toString();
    procura = idadeProcuraField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("nome", name);
        userInfo.put("idade", idade);
        userInfo.put("bio", bio);
        userInfo.put("idadeProcura", procura);

        reference.updateChildren(userInfo);
        if(resultadoUri != null){

            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(userID);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultadoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    System.out.println(taskSnapshot);

                    userInfo.put("profileImageUri", downloadUri.toString());

                    //userInfo.put("profileImageUrl", downloadUri.toString());

                    reference.updateChildren(userInfo);

                    finish();
                    return;
                }
            });

        }else{
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();

            resultadoUri = imageUri;


            System.out.println(resultadoUri);
            profileImage.setImageURI(resultadoUri);

        }
    }
}