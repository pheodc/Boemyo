package br.com.boemyo.Activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Favorito;
import br.com.boemyo.R;

public class PerfilEstabelecimentoActivity extends AppCompatActivity implements ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
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
    private String validaFavorito;
    private ArrayList<String> qrcodes;
    private Preferencias preferencias;
    private RelativeLayout conexao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_estabelecimento);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        preferencias = new Preferencias(PerfilEstabelecimentoActivity.this);
        intent = getIntent();
        estabelecimento = (Estabelecimento) intent.getSerializableExtra("estabelecimento");
        conexao = (RelativeLayout) findViewById(R.id.conexao_perfil_estab);
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
        tvhorarioEstabelecimento.setText(estabelecimento.getDiaSemanaEstabelecimento() + " das " +
                                            estabelecimento.getHorarioAberturaEstabelecimento() + " às " +
                                                estabelecimento.getHorarioFechamentoEstabelecimento());

        tvenderecoEstabelecimento = (TextView) findViewById(R.id.tv_endereco_estab);
        tvenderecoEstabelecimento.setText(estabelecimento.getEnderecoEstabelecimento());

        qrcodes = new ArrayList<String>();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil_estab_menu, menu);
        this.menu = menu;

        firebase = FirebaseInstance.getFirebase()
                .child("usuario")
                    .child(preferencias.getIdentificador())
                        .child("favorito");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    validaFavorito =  dados.getKey().toString();


                    qrcodes.add(validaFavorito.toString());


                }
                if(qrcodes.contains(estabelecimento.getIdEstabelecimento())){
                    qrcodes.clear();
                    aux_favorito = true;
                    Log.i("LOG_AUX_LISTENER", "ENTROU_TRUE");
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(PerfilEstabelecimentoActivity.this, R.mipmap.ic_heart_white_24dp));
                }else{
                    qrcodes.clear();
                    aux_favorito = false;
                    Log.i("LOG_AUX_LISTENER", "ENTROU_FALSE");
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(PerfilEstabelecimentoActivity.this, R.mipmap.ic_heart_outline_white_24dp));
                }



                //Log.i("LOG_AUX_LISTENER", String.valueOf(aux_favorito));
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
            /*if(aux_favorito == false){
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

            }*/
            validaFavorito(aux_favorito);
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
            favorito.setIdEstabelecimento(estabelecimento.getIdEstabelecimento());
            favorito.salvarFavoritoEstabelecimento();
            favorito.salvarFavoritoUsuario();
            menu.getItem(0).setIcon(ContextCompat.getDrawable(PerfilEstabelecimentoActivity.this, R.mipmap.ic_heart_white_24dp));
            aux_favorito = true;
            //firebase.removeEventListener(valueEventListener);
        }else{

            removeFavoritoUsuario();
            removeFavoritoEstabelecimento();
            menu.getItem(0).setIcon(ContextCompat.getDrawable(PerfilEstabelecimentoActivity.this, R.mipmap.ic_heart_outline_white_24dp));
            aux_favorito = false;
            //firebase.removeEventListener(valueEventListener);
        }

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

    private void removeFavoritoUsuario(){
        firebase = FirebaseInstance.getFirebase()
                .child("usuario")
                .child(preferencias.getIdentificador())
                .child("favorito")
                .child(estabelecimento.getIdEstabelecimento());
        firebase.removeValue();
    }

    private void removeFavoritoEstabelecimento(){

        firebase = FirebaseInstance.getFirebase()
                .child("estabelecimento")
                .child(estabelecimento.getIdEstabelecimento())
                .child("favorito")
                .child(preferencias.getIdentificador());
        firebase.removeValue();
    }
}


