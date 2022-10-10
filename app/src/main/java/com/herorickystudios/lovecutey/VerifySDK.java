package com.herorickystudios.lovecutey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.herorickystudios.lovecutey.NotifyVerify;
import com.herorickystudios.lovecutey.R;
import com.herorickystudios.lovecutey.ui.login.logiActivity;

public class VerifySDK extends AppCompatActivity {

    int AndroidVersionSDK = Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_sdk);

        if(AndroidVersionSDK < 33){
            Intent intent = new Intent(this, logiActivity.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(this, NotifyVerify.class);
            startActivity(intent);
        }

    }
}