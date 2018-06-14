package br.com.boemyo.Activitys;

import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.Date;

import br.com.boemyo.Configure.Base64Custom;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.IndiqueEtabelecimentos;
import br.com.boemyo.R;

public class IndiqueEstabelecimentoActivity extends AppCompatActivity implements ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private Toolbar tbIndique;
    private EditText nomeEstabIndique;
    private EditText cidadeEstabIndique;
    private EditText telefoneEstabIndique;
    private EditText obsEstabIndique;
    private FloatingActionButton fabIndique;
    private ProgressBar pbIndique;
    private RelativeLayout rlFundoIndique;
    private Preferencias preferencias;
    private Date hora = Calendar.getInstance().getTime();
    private RelativeLayout conexao;
    private DatabaseReference databaseReference = FirebaseInstance.getFirebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indique_estabelecimento);


        preferencias = new Preferencias(IndiqueEstabelecimentoActivity.this);
        tbIndique = (Toolbar) findViewById(R.id.tb_indique_estabelecimento);
        tbIndique.setTitle(R.string.title_indique);
        setSupportActionBar(tbIndique);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbIndique.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        conexao = (RelativeLayout) findViewById(R.id.conexao_indique_estabelecimento);
        nomeEstabIndique = (EditText) findViewById(R.id.et_nome_indique);
        cidadeEstabIndique = (EditText) findViewById(R.id.et_cidade_indique);
        telefoneEstabIndique = (EditText) findViewById(R.id.et_telefone_indique);
        obsEstabIndique = (EditText) findViewById(R.id.et_obs_indique);
        pbIndique = (ProgressBar) findViewById(R.id.pb_indique);
        rlFundoIndique = (RelativeLayout) findViewById(R.id.rl_fundo_indique_progress);
        fabIndique = (FloatingActionButton) findViewById(R.id.fab_indique);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefoneEstabIndique, simpleMaskTelefone);
        telefoneEstabIndique.addTextChangedListener(maskTelefone);


        fabIndique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirProgress(true);
                validaIndicado( nomeEstabIndique.getText().toString(),
                                cidadeEstabIndique.getText().toString(),
                                telefoneEstabIndique.getText().toString(),
                                obsEstabIndique.getText().toString());
            }
        });


    }

    public void validaIndicado(String nomeEstabValido, String cidaddeEstabValido, String telefoneEstabValido, String obsEstabValido){

        if (nomeEstabValido.isEmpty() || telefoneEstabValido.isEmpty()) {
            exibirProgress(false);
            Snackbar snackbar = Snackbar.make(fabIndique, R.string.valida_empty, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            exibirProgress(false);
            IndiqueEtabelecimentos indiqueEtabelecimentos = new IndiqueEtabelecimentos();
            String idIndicadoCodificado = databaseReference.child("indiqueEstabelecimentos").push().getKey();
            indiqueEtabelecimentos.setIdUsuario(preferencias.getIdentificador());
            indiqueEtabelecimentos.setIdIndicado(idIndicadoCodificado);
            indiqueEtabelecimentos.setNomeIndicado(nomeEstabValido);
            indiqueEtabelecimentos.setCidadeIndicado(cidaddeEstabValido);
            indiqueEtabelecimentos.setTelefoneIndicado(telefoneEstabValido);
            indiqueEtabelecimentos.setObsIndicado(obsEstabValido);
            indiqueEtabelecimentos.salvarIndicado();
            limparDados();
            Snackbar snackbar = Snackbar.make(fabIndique, R.string.valida_sucesso_indique, Snackbar.LENGTH_LONG);
            snackbar.show();
        }



    }

    private void exibirProgress(boolean exibir) {
        pbIndique.setVisibility(exibir ? View.VISIBLE : View.GONE);
        rlFundoIndique.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void limparDados(){

        nomeEstabIndique.setText("");
        cidadeEstabIndique.setText("");
        telefoneEstabIndique.setText("");
        obsEstabIndique.setText("");

    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        Log.i("LOG_CONEXAO", String.valueOf(isConnected));
        if (isConnected == false){
            conexao.setVisibility(View.VISIBLE);
        }else{
            conexao.setVisibility(View.GONE);
        }
    }


}

