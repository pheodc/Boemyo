package br.com.boemyo.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import br.com.boemyo.R;

public class ConfigActivity extends AppCompatActivity {


    private Toolbar tbConfig;
    private RelativeLayout rlEditarPerfil;
    private RelativeLayout rlNotificacao;
    private RelativeLayout rlTermos;
    private RelativeLayout rlPoliticas;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        tbConfig = (Toolbar) findViewById(R.id.tb_config);

        tbConfig.setTitle(R.string.title_config);
        setSupportActionBar(tbConfig);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbConfig.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlEditarPerfil = (RelativeLayout) findViewById(R.id.rl_editar_perfil);
        rlNotificacao = (RelativeLayout) findViewById(R.id.rl_notificacao);
        rlTermos = (RelativeLayout) findViewById(R.id.rl_termos);
        rlPoliticas = (RelativeLayout) findViewById(R.id.rl_politicas);

        bundle = new Bundle();

        rlEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEditarPerfil = new Intent(ConfigActivity.this, EditarPerfilActivity.class);
                startActivity(intentEditarPerfil);
            }
        });

        rlNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ConfigActivity.this, "Em Construção", Toast.LENGTH_SHORT).show();
            }
        });

        rlTermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentTermos = new Intent(ConfigActivity.this, TermosPrivacidadeActivity.class);
                bundle.putInt("CHAVE_BUNDLE_TB", 1);
                bundle.putString("CHAVE_BUNDLE_URL", "https://boemyo.com/termos.html");
                intentTermos.putExtras(bundle);
                startActivity(intentTermos);
            }
        });

        rlPoliticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentPoliticas = new Intent(ConfigActivity.this, TermosPrivacidadeActivity.class);
                bundle.putInt("CHAVE_BUNDLE_TB", 2);
                bundle.putString("CHAVE_BUNDLE_URL", "https://boemyo.com/politicas.html");
                intentPoliticas.putExtras(bundle);
                startActivity(intentPoliticas);
            }
        });

    }
}
