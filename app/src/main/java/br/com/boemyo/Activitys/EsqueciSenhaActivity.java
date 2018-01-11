package br.com.boemyo.Activitys;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.R;

public class EsqueciSenhaActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar tbEsqueciSenha;
    private EditText etEmailEsqueci;
    private Button btEnviaEmailEsqueci;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        tbEsqueciSenha = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_esqueci_senha);
        tbEsqueciSenha.setTitle(R.string.title_esqueci_senha);
        setSupportActionBar(tbEsqueciSenha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbEsqueciSenha.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        firebaseAuth = FirebaseInstance.getFirebaseAuth();
        etEmailEsqueci = (EditText) findViewById(R.id.et_email_esqueci);
        btEnviaEmailEsqueci = (Button) findViewById(R.id.bt_enviar_email_esqueci);

        btEnviaEmailEsqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etEmailEsqueci.getText().equals(null)){
                    Snackbar snackbar = Snackbar.make(btEnviaEmailEsqueci, R.string.valida_empty, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    firebaseAuth
                            .sendPasswordResetEmail(etEmailEsqueci.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Snackbar snackbar = Snackbar.make(btEnviaEmailEsqueci, R.string.send_troca_email_sucesso, Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        finish();
                                    }else{
                                        Snackbar snackbar = Snackbar.make(btEnviaEmailEsqueci, R.string.send_email_erro, Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }
                                }
                            });
                }


            }
        });

    }
}
