package br.com.boemyo.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Favorito;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.R;

public class PerfilEstabelecimentoActivity extends AppCompatActivity {

    private Intent intent;
    private Estabelecimento estabelecimento;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar tbPerfilEstab;
    private ImageView ivImgEstabelecimento;
    private TextView tvDescEstabelecimento;
    private TextView tvhorarioEstabelecimento;
    private TextView tvenderecoEstabelecimento;
    private Boolean aux_favorito;
    private Menu menu;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private String idQRCODE;
    private ArrayList<String> qrcodes;
    private Preferencias preferencias;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_estabelecimento);
        preferencias = new Preferencias(PerfilEstabelecimentoActivity.this);
        intent = getIntent();
        estabelecimento = (Estabelecimento) intent.getSerializableExtra("estabelecimento");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tb_perfil_estab);
        collapsingToolbarLayout.setTitle(estabelecimento.getNomeEstabelecimento());
        tbPerfilEstab = (Toolbar) findViewById(R.id.tb_perfil_estab);
        tbPerfilEstab.setTitle(R.string.title_estabelecimento);
        setSupportActionBar(tbPerfilEstab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbPerfilEstab.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });


        ivImgEstabelecimento = (ImageView) findViewById(R.id.iv_estab);
        PicassoClient.downloadImage(this, estabelecimento.getPerfilEstabelecimento(), ivImgEstabelecimento);

        tvDescEstabelecimento = (TextView) findViewById(R.id.tv_desc_estab);
        tvDescEstabelecimento.setText(estabelecimento.getDescEstabelecimento());

        tvhorarioEstabelecimento = (TextView) findViewById(R.id.tv_horario_estab);
        tvhorarioEstabelecimento.setText(estabelecimento.getHorarioEstabelecimento());

        tvenderecoEstabelecimento = (TextView) findViewById(R.id.tv_endereco_estab);
        tvenderecoEstabelecimento.setText(estabelecimento.getEnderecoEstabelecimento());

        qrcodes = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil_estab_menu, menu);
        this.menu = menu;

        firebase = FirebaseInstance.getFirebase()
                .child("favorito")
                .child(preferencias.getIdentificador());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    idQRCODE = String.valueOf(dados.child("idQrCode").getValue());
                    qrcodes.add(idQRCODE);
                    Log.i("LOG_QRCODES", idQRCODE);

                }

                if(qrcodes.contains(estabelecimento.getIdQRCODE())){
                    aux_favorito = true;
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(PerfilEstabelecimentoActivity.this, R.mipmap.ic_heart_white_24dp));
                }else{
                    aux_favorito = false;
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(PerfilEstabelecimentoActivity.this, R.mipmap.ic_heart_outline_white_24dp));
                }

                Log.i("LOG_AUX", String.valueOf(aux_favorito));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListener);

        Log.i("LOG_AUX", String.valueOf(aux_favorito));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i("LOG_AUX_1", String.valueOf(aux_favorito));
        int id = item.getItemId();


        if (id == R.id.action_favoritos_perfil) {
            if(aux_favorito == false){
                AlertDialog.Builder builder = new AlertDialog.Builder( this );
                builder.setTitle(R.string.dialog_confirma_favorito_title);
                builder.setMessage(R.string.dialog_confirma_favorito_message);

                builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(PerfilEstabelecimentoActivity.this, R.mipmap.ic_heart_white_24dp));
                        validaFavorito(false);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.bt_dialog_nagative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.i("LOG_AUX", "AGORA FAVORITO");

            }

            Log.i("LOG_AUX_2", String.valueOf(aux_favorito));

        } else if (id == R.id.action_qrcode_perfil) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
       openActivity();
    }

    public void openActivity(){
        Intent intent = new Intent(PerfilEstabelecimentoActivity.this, ProcuraLocaisActivity.class);
        startActivity(intent);
        finish();
    }

    public void validaFavorito(Boolean aux){
        if(aux == false){
            Favorito favorito = new Favorito();

            favorito.setIdUsuario(preferencias.getIdentificador());
            favorito.setIdQrCode(estabelecimento.getIdQRCODE());
            favorito.setNomeFavorito(estabelecimento.getNomeEstabelecimento());
            favorito.setImgFundoFavorito(estabelecimento.getPerfilEstabelecimento());
            favorito.salvarFavorito();


        }

    }

}


