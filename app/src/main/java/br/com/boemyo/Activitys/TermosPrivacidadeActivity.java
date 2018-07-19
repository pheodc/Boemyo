package br.com.boemyo.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import br.com.boemyo.R;

public class TermosPrivacidadeActivity extends AppCompatActivity {

    private Toolbar tbTermosPrivacidade;
    private WebView wsTermosPrivacidade;
    private String urlTermosPrivacidades;
    private int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos_privacidade);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        tbTermosPrivacidade = (Toolbar) findViewById(R.id.tb_termos_privacidade);
        wsTermosPrivacidade = (WebView) findViewById(R.id.ws_termos_privacidade);

        setSupportActionBar(tbTermosPrivacidade);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbTermosPrivacidade.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        WebSettings webSettings = wsTermosPrivacidade.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);

        tipo = bundle.getInt("CHAVE_BUNDLE_TB");

        if(tipo == 1){
            tbTermosPrivacidade.setTitle(R.string.title_termos);
        }else{
            tbTermosPrivacidade.setTitle(R.string.title_privacidades);
        }

        urlTermosPrivacidades = bundle.getString("CHAVE_BUNDLE_URL");
        wsTermosPrivacidade.loadUrl(urlTermosPrivacidades);



    }
}
