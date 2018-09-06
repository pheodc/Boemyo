package br.com.boemyo.Activitys;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.R;

public class MotivoCancelamentoActivity extends AppCompatActivity {

    private Toolbar tbMotivoCancelamento;
    private String idPedido;
    private int motivo = 0;
    private Button btEnviarMotivo;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivo_cancelamento);
        final Preferencias preferencias = new Preferencias(this);
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        idPedido = bundle.getString("BUNDLE_PEDIDO_MOTIVO");

        tbMotivoCancelamento = (Toolbar) findViewById(R.id.tb_motivos_cancelamento);

        tbMotivoCancelamento.setTitle(R.string.title_motivos_cancelamento);

        setSupportActionBar(tbMotivoCancelamento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbMotivoCancelamento.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();}});

        btEnviarMotivo = (Button) findViewById(R.id.bt_confirma_motivo_cancelamento);

        btEnviarMotivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(motivo == 0){
                    Snackbar snackbar = Snackbar.make(btEnviarMotivo, R.string.erro_motivo_cancel, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else {

                    firebase = FirebaseInstance.getFirebase();

                    firebase.child("pedido")
                                .child(idPedido)
                                    .child("situacaoPedido")
                                        .setValue(4);

                    firebase.child("pedido")
                                .child(idPedido)
                                    .child("motivoCancelamento")
                                        .setValue(motivo);

                    firebase.child("statusPedido")
                                .child("cancelados")
                                    .child(preferencias.getIdEstabelecimento())
                                        .child(idPedido)
                                            .setValue(true);

                    firebase.child("statusPedido")
                                .child("andamentoPedido")
                                    .child(preferencias.getIdEstabelecimento())
                                        .child(idPedido)
                                            .removeValue();

                    firebase.child("statusPedido")
                                .child("novoPedido")
                                    .child(preferencias.getIdEstabelecimento())
                                        .child(idPedido)
                                            .removeValue();

                    finish();

                }
            }
        });


    }

    public void rbClicked(View view){

        boolean checked = ((RadioButton)  view).isChecked();

        switch (view.getId()){

            case R.id.rb_motivo_um:
                if( checked)
                    motivo = 1;
                    Log.i("LOG_SELECT_MOTIVO",  String.valueOf(motivo));
                    break;
            case R.id.rb_motivo_dois:
                if(checked)
                    motivo = 2;
                    Log.i("LOG_SELECT_MOTIVO",  String.valueOf(motivo));
                    break;
        }
    }
}
