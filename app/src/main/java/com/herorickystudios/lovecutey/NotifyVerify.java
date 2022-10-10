package com.herorickystudios.lovecutey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.herorickystudios.lovecutey.R;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.herorickystudios.lovecutey.R;
import com.herorickystudios.lovecutey.ui.login.logiActivity;

public class NotifyVerify extends AppCompatActivity {
    private View view;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_verify);
        verifyNotifyPermission(view);
    }
    public void verifyNotifyPermission(View view){

        if (!checkPermission()) {

            requestPermission();

        } else {
            Intent intent = new Intent(this, logiActivity.class);
            startActivity(intent);
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), POST_NOTIFICATIONS);

        return result == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);


    }
    public void Intent(View view){

        Intent intent = new Intent(this, logiActivity.class);
        startActivity(intent);
    }
}