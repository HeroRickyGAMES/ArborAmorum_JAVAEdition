package com.herorickystudios.lovecutey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.herorickystudios.lovecutey.ui.login.logiActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

//Programado por HeroRickyGames

public class registerActivity extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private EditText edit_Nome,edit_Email,edit_senha,reeditsenha, idade_text;
    private Button register_button;
    private RadioGroup radioGrup;
    String[] menssagens = {"Preencha todos os campos para continuar", "Cadastro feito com sucesso!"};
    //String[] Filtropalavrões = {"Preencha todos os campos para continuar", "Cadastro feito com sucesso!"};
    String usuarioID;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    private String TestString = "";

    private boolean testMode = true;

    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //iniciar Componentes
        edit_Nome = findViewById(R.id.nome_register);
        edit_Email = findViewById(R.id.username_Register);
        edit_senha = findViewById(R.id.password_Register);
        reeditsenha = findViewById(R.id.repassword_Register);
        register_button = findViewById(R.id.register_button);
        idade_text = findViewById(R.id.idade_text);
        radioGrup = findViewById(R.id.radioGroup);


        //Codigos de localização
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 30);
        locationRequest.setFastestInterval(1000 * 5);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



        String tst = getString(R.string.testModeAction);

        testMode = Boolean.parseBoolean(tst);


        //Test Mode Verificador
        if(testMode == true){
            TestString = "IMG_16_9_APP_INSTALL#";
        }else if(testMode == false){
            TestString = "";
        }


        //Esconde a action Bar
        getSupportActionBar().hide();

        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(this, "Esse aplicativo precisa das permissões para funcionar, caso você negou sem querer, acesse as configurações!", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }

    private void CadastrarUsuario(View view){


    }
    private void updateGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    Geocoder geocoder = new Geocoder(registerActivity.this);
                    try{
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String cidade = addresses.get(0).getSubAdminArea();

                        //Codigos de registro
                        register_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //Cadastrando o fulano.
                                String email = edit_Email.getText().toString();
                                String senha = edit_senha.getText().toString();

                                String UID = FirebaseAuth.getInstance().getUid();

                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()){

                                            Snackbar snackbar = Snackbar.make(view, menssagens[1],Snackbar.LENGTH_LONG);
                                            snackbar.show();

                                            FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                                            String getUID = usuarioLogado.getUid();

                                            String nome = edit_Nome.getText().toString();
                                            String idade = idade_text.getText().toString();
                                            String email = edit_Email.getText().toString();


                                            int selectID = radioGrup.getCheckedRadioButtonId();
                                            final RadioButton radioButton = (RadioButton) findViewById(selectID);

                                            if(radioButton.getText() == null){
                                                return;
                                            }

                                            String genero = radioButton.getText().toString();

                                            referencia.child(genero).child(getUID).child(cidade).child("Dados do Usuario").child("nome").setValue(nome);
                                            referencia.child(genero).child(getUID).child("nome").setValue(nome);
                                            referencia.child(genero).child(getUID).child("isOnline").setValue("true");
                                            referencia.child(genero).child(getUID).child(cidade).child("Dados do Usuario").child("email").setValue(email);
                                            referencia.child(genero).child(getUID).child(cidade).child("Dados do Usuario").child("idade").setValue(idade);
                                            referencia.child(genero).child(getUID).child(cidade).child("Dados do Usuario").child("bio").setValue("Digite sua Bio!");
                                            referencia.child(genero).child(getUID).child("cidade").setValue(cidade);
                                            referencia.child(genero).child(getUID).child("Genero").setValue(genero);
                                            referencia.child(genero).child(getUID).child(cidade).child("Dados do Usuario").child("cidade").setValue(cidade);

                                            String URL = "https://firebasestorage.googleapis.com/v0/b/lovecutey-95cc0.appspot.com/o/" +  "ProfileImages" + "%2F" + getUID + "?alt=media";



                                            referencia.child(genero).child(getUID).child("profileImageUri").child(getUID).setValue(URL);



                                            if(genero.equals("Gay")){
                                                referencia.child(genero).child(getUID).child("ConfiguracoesPessoais").child("sexoDeProcura").setValue(genero);
                                                referencia.child(genero).child(getUID).child("connections").child("yeps").child(getUID).setValue(true);
                                            }
                                            if (genero.equals("Lesbica")){
                                                referencia.child(genero).child(getUID).child("ConfiguracoesPessoais").child("sexoDeProcura").setValue(genero);
                                                referencia.child(genero).child(getUID).child("connections").child("yeps").child(getUID).setValue(true);
                                            }
                                            if (genero.equals("Bi Sexual")){
                                                referencia.child(genero).child(getUID).child("ConfiguracoesPessoais").child("sexoDeProcura").setValue("Bi Sexual");
                                                referencia.child(genero).child(getUID).child("connections").child("yeps").child(getUID).setValue(true);
                                            }
                                            if(genero.equals("Feminino")){
                                                referencia.child(genero).child(getUID).child("ConfiguracoesPessoais").child("sexoDeProcura").setValue("Masculino");
                                            }
                                            if (genero.equals("Masculino")){
                                                referencia.child(genero).child(getUID).child("ConfiguracoesPessoais").child("sexoDeProcura").setValue("Feminino");
                                            }

                                            int idadee = Integer.parseInt(idade);
                                            int aMais = 5;
                                            int Resultado = idadee + 5;

                                            String ConfigIdade = String.valueOf(Resultado);

                                            System.out.println(ConfigIdade);

                                            referencia.child(genero).child(getUID).child("ConfiguracoesPessoais").child("IdadeLimite").setValue(ConfigIdade);

                                            //Lançar activity dps do cadastro
                                            Intent intent = new Intent(registerActivity.this, ListUsersActivity.class);
                                            startActivity(intent);

                                        }else{

                                            String erro;
                                            try {

                                                throw task.getException();
                                            }
                                            catch (FirebaseAuthWeakPasswordException e) {
                                                erro = "Digite uma senha com no mínimo 6 caracteres!";
                                            }catch (FirebaseAuthUserCollisionException e) {

                                                erro = "Essa conta já foi criada, crie uma nova conta ou clique em esqueci minha senha na tela de login!";
                                            }catch (FirebaseAuthInvalidCredentialsException e){

                                                erro = "Seu email está digitado errado, verifique novamente!";

                                            }catch (Exception e){
                                                erro = "Ao cadastrar usuário!";
                                            }
                                            Snackbar snackbar = Snackbar.make(view, erro,Snackbar.LENGTH_LONG);
                                            snackbar.show();

                                        }

                                    }
                                });

                            }
                        });

                        System.out.println("LOCALIZAÇÃO EXATA: " + cidade);
                    }catch (Exception e){
                        System.out.println("Não foi possivel encontrar sua localização!" + e);
                    }

                    System.out.println(location.getLatitude());
                    System.out.println(location.getLongitude());
                    System.out.println(location.getLongitude());
                    System.out.println(location.getAccuracy());

                    if( location.hasAltitude() ){
                        System.out.println( "Latitude " + location.getLatitude());
                    }else{
                        System.out.println("Não disponivel");
                    }
                    if( location.hasSpeed() ){
                        System.out.println("Velocidade " + location.getSpeed());
                    }else{
                        System.out.println("Não disponivel");
                    }
                    if( location.hasAltitude() ){
                        System.out.println("Altitude " +location.getLatitude());
                    }else{
                        System.out.println("Não disponivel");
                    }
                }
            });

        }else{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_FINE_LOCATION);
            }

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), logiActivity.class);
        startActivity(intent);

    }
}