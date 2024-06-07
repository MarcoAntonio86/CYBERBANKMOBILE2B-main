package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private ControllerBancoDados controllerBancoDados;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        controllerBancoDados = new ControllerBancoDados(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(i);
                controllerBancoDados.open();


                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}