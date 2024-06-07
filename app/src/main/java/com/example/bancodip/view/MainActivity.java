package com.example.bancodip.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityMainBinding;
import com.example.bancodip.model.ModelBancoDados;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private ControllerBancoDados controllerBancoDados;

    private static final int REQUEST_TRANSFERIR = 123;

    private TextView meuTextView;
    private ModelBancoDados databaseHelper;



    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);


        Intent intentTrans = new Intent(MainActivity.this, TransferirActivity.class);
        Intent intent = getIntent();

        String CPF = intent.getStringExtra("CPF");
        double saldoIntent = intent.getDoubleExtra("Saldo", 0.0);

        intentTrans.putExtra("CPFTransfer", CPF);



        try {
            controllerBancoDados.open();

            Double saldoBanco = controllerBancoDados.getSaldoByTitular(CPF);
            Double chequeBanco = controllerBancoDados.getChequeByTitular(CPF);
            String saldoString = String.valueOf(saldoBanco);
            String chequeString = String.valueOf(chequeBanco);

            binding.saldoConta.setText("R$ " + String.valueOf(saldoIntent));
            binding.chequeEspecialConta.setText(chequeString);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            controllerBancoDados.close();
        }



        binding.btnDepositar.setOnClickListener(v -> {
            controllerBancoDados.open();

            String valor = binding.hintUserValor.getText().toString();

            if(!valor.isEmpty()){
                try {

                    Double cheque = controllerBancoDados.getChequeByTitular(CPF);
                    Double valorSaldo = controllerBancoDados.getSaldoByTitular(CPF);
                    Double CHEQUEESPECIAL = controllerBancoDados.getChequeDEFIByTitular(CPF);

                    Double novoSaldo = Double.parseDouble(valor) + valorSaldo ;
                    Double novoCheque = cheque + Double.parseDouble(valor);

                    controllerBancoDados.updateSaldo(CPF, novoSaldo);
                    binding.saldoConta.setText(String.valueOf(novoSaldo));


                    if(valorSaldo < 0 ){
                        controllerBancoDados.updateCheque(CPF, novoCheque);
                        binding.chequeEspecialConta.setText(String.valueOf(novoCheque));
                    }
                    if(novoSaldo >= 0 && cheque < CHEQUEESPECIAL){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setMessage("CHEQUE ESPECIAL COMPENSADO");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // nada aqui
                            }
                        });

                        AlertDialog alerta = builder.create();
                        alerta.show();

                        controllerBancoDados.updateCheque(CPF, CHEQUEESPECIAL);
                        binding.chequeEspecialConta.setText(String.valueOf(CHEQUEESPECIAL));

                    }

                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    binding.hintUserValor.setText("");
                }
            }

        });

        binding.btnSacar.setOnClickListener(v -> {
            controllerBancoDados.open();

            String valorCliente = binding.hintUserValor.getText().toString();

            if (!valorCliente.isEmpty()) {
                try {
                    Double saldo = controllerBancoDados.getSaldoByTitular(CPF);
                    Double cheque = controllerBancoDados.getChequeByTitular(CPF);
                    Double CHEQUE_ESPECIAL = controllerBancoDados.getChequeDEFIByTitular(CPF);

                    Double valorSaque = Double.parseDouble(valorCliente);
                    Double novoSaldo = saldo - valorSaque;

                    // Verifica se há saldo suficiente ou se é necessário utilizar o cheque especial
                    if (novoSaldo >= 0) {
                        // Atualiza o saldo no banco de dados e na interface do usuário
                        controllerBancoDados.updateSaldo(CPF, novoSaldo);
                        binding.saldoConta.setText(String.valueOf(novoSaldo));
                    } else if (cheque + novoSaldo >= 0) {
                        // Calcula o saldo restante após utilizar o cheque especial
                        Double saldoRestante = novoSaldo + cheque;

                        // Atualiza o saldo e o cheque especial no banco de dados e na interface do usuário
                        controllerBancoDados.updateSaldo(CPF, 0.0);
                        controllerBancoDados.updateCheque(CPF, saldoRestante);
                        binding.saldoConta.setText("R$ 0.00");
                        binding.chequeEspecialConta.setText("R$ " + String.valueOf(saldoRestante));
                    } else {
                        // Se não houver saldo suficiente nem cheque especial disponível, exibe mensagem de saldo insuficiente
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("SALDO INSUFICIENTE !");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Nada aqui
                            }
                        });

                        AlertDialog alerta = builder.create();
                        alerta.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    binding.hintUserValor.setText("");
                }
            }
        });




        binding.btnTransferir.setOnClickListener(v -> {
            startActivity(intentTrans);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        controllerBancoDados.open();
        Intent intent = getIntent();

        String CPF = intent.getStringExtra("CPF");
        Double saldo = controllerBancoDados.getSaldoByTitular(CPF);
        Double cheque = controllerBancoDados.getChequeByTitular(CPF);

        binding.saldoConta.setText("R$ " + String.valueOf(saldo));
        binding.chequeEspecialConta.setText("R$ " + String.valueOf(cheque));
    }




}