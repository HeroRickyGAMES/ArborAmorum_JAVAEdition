package com.herorickystudios.lovecutey.ui.login;

//Programado por HeroRickyGames

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseUser;
import com.herorickystudios.lovecutey.ListUsersActivity;
import com.herorickystudios.lovecutey.R;
import com.herorickystudios.lovecutey.registerActivity;

import java.util.List;

public class logiActivity extends AppCompatActivity {

    private EditText edit_Email,edit_senha;
    private Button btn_login;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private ProgressBar progressbar_login;
    LocationRequest locationRequest;

    private String TestString = "";

    private boolean testMode = true;


    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;

    String[] menssagens = {"Preencha todos os campos para continuar", "Login feito com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logi);

        //Esconde a action Bar
        getSupportActionBar().hide();

        String tst = getString(R.string.testModeAction);

        testMode = Boolean.parseBoolean(tst);


        //Test Mode Verificador
        if(testMode == true){
            TestString = "IMG_16_9_APP_INSTALL#";
        }else if(testMode == false){
            TestString = "";
        }


        edit_Email = findViewById(R.id.username);
        edit_senha = findViewById(R.id.password);
        btn_login = findViewById(R.id.login);
        progressbar_login = findViewById(R.id.loading);

        //Codigos de localização
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 30);
        locationRequest.setFastestInterval(1000 * 5);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        updateGPS();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edit_Email.getText().toString();
                String senha = edit_senha.getText().toString();


                if( email.isEmpty() || senha.isEmpty() ){
                    Snackbar snackbar = Snackbar.make(view, menssagens[0],Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    AutenticarUsuario(view);
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioLogado != null){

            AbrirTelaInicial();

        }

    }

    public void CadastroActivity(View v){
        Intent intent = new Intent(this,registerActivity.class);
        startActivity(intent);
        finish();
    }

    public void AbrirTelaInicial(){
        Intent intent = new Intent(this,ListUsersActivity.class);
        startActivity(intent);
    }

    public void AutenticarUsuario(View view){
        String email = edit_Email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressbar_login.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AbrirTelaInicial();
                        }
                    }, 3000);
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao logar!";
                        Snackbar snackbar = Snackbar.make(view, erro,Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Não fazer nada
                }else{
                    Toast.makeText(this, "Esse aplicativo precisa das permissões para funcionar, caso você negou sem querer, acesse as configurações!", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location == null){

                        Toast.makeText(logiActivity.this, "O seu GPS está desativado! Por favor, ative o GPS para conseguir usar o Arbor Amorum!", Toast.LENGTH_LONG).show();
                        Toast.makeText(logiActivity.this, "Clique no FAB que centraliza a localização, pós isso, volte ao aplicativo!", Toast.LENGTH_LONG).show();

                        Uri uri = Uri.parse("https://www.google.pt/maps");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                    Geocoder geocoder = new Geocoder(logiActivity.this);
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
}
