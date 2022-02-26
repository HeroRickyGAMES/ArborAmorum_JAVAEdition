package com.herorickystudios.lovecutey.ui.login;

//Programado por HeroRickyGames

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.herorickystudios.lovecutey.ListUsersActivity;
import com.herorickystudios.lovecutey.R;
import com.herorickystudios.lovecutey.registerActivity;

public class logiActivity extends AppCompatActivity {

    private EditText edit_Email,edit_senha;
    private Button btn_login;
    private ProgressBar progressbar_login;
    String[] menssagens = {"Preencha todos os campos para continuar", "Login feito com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logi);

        //Esconde a action Bar
        getSupportActionBar().hide();

        edit_Email = findViewById(R.id.username);
        edit_senha = findViewById(R.id.password);
        btn_login = findViewById(R.id.login);
        progressbar_login = findViewById(R.id.loading);

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
}
