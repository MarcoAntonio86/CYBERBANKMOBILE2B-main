package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ControllerBancoDados controllerBancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

        binding.btnCriarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String nome = binding.NomeRegister.getText().toString().trim();
            String CPF = binding.CPFRegister.getText().toString().trim();
            String senha = binding.SenhaRegister.getText().toString().trim();
            String SaldoTxT = binding.SaldoRegister.getText().toString().trim();

            if (!nome.isEmpty() && !CPF.isEmpty() && !SaldoTxT.isEmpty() && !senha.isEmpty()) {
                if (controllerBancoDados.isCPFInDatabase(CPF)) {
                    Toast.makeText(getApplicationContext(), "CPF já registrado!", Toast.LENGTH_LONG).show();
                } else {
                    double Saldo = Double.parseDouble(SaldoTxT);
                    double chequeEspecial = Saldo * 4;
                    double chequeI = chequeEspecial;

                    try {
                        controllerBancoDados.insertData(nome, CPF, senha, Saldo, chequeEspecial, chequeI);
                        intent.putExtra("nome", nome);
                        intent.putExtra("CPF", CPF);
                        intent.putExtra("Senha", senha);
                        intent.putExtra("Saldo", Saldo);
                        intent.putExtra("ChequeEspecial", chequeEspecial);
                        intent.putExtra("ChequeI", chequeI);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        controllerBancoDados.close();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
            }
        });
    }
}
