package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ControllerBancoDados controllerBancoDados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);

        controllerBancoDados = new ControllerBancoDados(this);

        binding.btnCriarContaLogin.setOnClickListener(v -> {
            startActivity(intentRegister);
        });

        binding.btnEntrarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String CPF = binding.CPFLogin.getText().toString().trim().toUpperCase();
            String Senha = binding.SenhaLogin.getText().toString().trim().toUpperCase();


            if (controllerBancoDados.isCPFInDatabase(CPF) && controllerBancoDados.isSenhaInDatabase(Senha)){

                try {
                    intentMain.putExtra("CPF", CPF);
                    intentMain.putExtra("Senha", Senha);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    controllerBancoDados.close();
                    startActivity(intentMain);
                    finish();
                }



            }else {
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
            }

        });



    }
}