package br.com.boemyo.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.R;

public class MainEstabelecimentoActivity extends AppCompatActivity {

    private Button btCardapio;
    private Button btComanda;
    private Button bt_sair_estabelecimentos;
    private Preferencias preferencias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estabelecimento);
        preferencias = new Preferencias(MainEstabelecimentoActivity.this);
        btCardapio = (Button) findViewById(R.id.bt_cardapio);
        btComanda = (Button) findViewById(R.id.bt_comanda);
        bt_sair_estabelecimentos = (Button) findViewById(R.id.bt_sair_estabelecimento);

        if(preferencias.getcodQRcode() != null ){
            Log.i("LOG_CODQRCODE", preferencias.getcodQRcode());
        }

        if(preferencias.getidComanda() != null ){
            Log.i("LOG_CODQRCODE", preferencias.getidComanda());
        }

        btCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEstabelecimentoActivity.this, CategoriaCardapioActivity.class);
                startActivity(intent);
            }
        });

        btComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEstabelecimentoActivity.this, ComandaActivity.class);
                startActivity(intent);
            }
        });

        bt_sair_estabelecimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencias.removerPreferencias();
                Intent intent = new Intent(MainEstabelecimentoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
