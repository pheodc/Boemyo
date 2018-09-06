package br.com.boemyo.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;

import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class EstabelecimentoMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser userInfo;
    private RelativeLayout conexao;
    private CircleImageView cvImgDrawerEstab;
    private TextView tvNomeDrawerEstab;
    private TextView tvEmailDrawerEstab;
    private CardView cardCardapio;
    private CardView cardComanda;
    private Preferencias preferencias;
    private String novaComanda;
    private ImageView ivCardapio;
    private ImageView ivComanda;
    private Button btDeixarEstabelecimento;
    private EditText etFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento_main);
        preferencias = new Preferencias(EstabelecimentoMainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bem vindo");
        setSupportActionBar(toolbar);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        firebaseAuth = FirebaseInstance.getFirebaseAuth();

        conexao = (RelativeLayout) findViewById(R.id.conexao_estabelecimento_main);
        ivCardapio = (ImageView) findViewById(R.id.iv_img_cardapio);
        ivComanda = (ImageView) findViewById(R.id.iv_img_comanda);
        btDeixarEstabelecimento = (Button) findViewById(R.id.bt_deixar_estabelecimento);
        //Picasso.get().load(R.drawable.comanda_new_menor).resize(200,200).into(ivComanda);


        if(preferencias.getAbrirCategoria() == "open" ){
            Intent intentNovaComanda = new Intent(EstabelecimentoMainActivity.this, CategoriaCardapioActivity.class);
            startActivity(intentNovaComanda);

        }

        recuperaEstabelecimento(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        cvImgDrawerEstab = (CircleImageView) view.findViewById(R.id.iv_img_drawer_estab);
        tvNomeDrawerEstab = (TextView) view.findViewById(R.id.tv_nome_drawer_estab);
        tvEmailDrawerEstab = (TextView) view.findViewById(R.id.tv_email_drawer_estab);
        cardCardapio = (CardView) findViewById(R.id.card_cardapio);
        cardComanda = (CardView) findViewById(R.id.card_comanda);

        if(preferencias.getNome() != null){
            //PicassoClient.downloadImage(this, preferencias.getUrlImagem(), cvImgDrawerEstab);
            tvNomeDrawerEstab.setText(preferencias.getNome());
            tvEmailDrawerEstab.setText(preferencias.getEmail());
        }


       cardCardapio.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                cardCardapio.setElevation(22);
                Intent intent = new Intent(EstabelecimentoMainActivity.this, CategoriaCardapioActivity.class);
                startActivity(intent);
            }
        });

        cardComanda.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                cardComanda.setElevation(22);
                Intent intent = new Intent(EstabelecimentoMainActivity.this, ComandaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btDeixarEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.estabelecimento_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_estab) {

            Intent intentConfig = new Intent(EstabelecimentoMainActivity.this, ConfigActivity.class);
            startActivity(intentConfig);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home_estab) {

        }else if (id == R.id.nav_pagamento_estab) {

            Intent intentPagamento = new Intent(EstabelecimentoMainActivity.this, PagamentosActivity.class);
            startActivity(intentPagamento);


        } else if (id == R.id.nav_favorito_estab) {
            Intent intentFavorito = new Intent(EstabelecimentoMainActivity.this, FavoritoActivity.class);
            startActivity(intentFavorito);

        } else if (id == R.id.nav_sobre_estab) {

            Intent intentSobre = new Intent(EstabelecimentoMainActivity.this, SobreActivity.class);
            startActivity(intentSobre);

        } else if (id == R.id.nav_configuracao_estab) {

            Intent intentConfig = new Intent(EstabelecimentoMainActivity.this, ConfigActivity.class);
            startActivity(intentConfig);

        } else if (id == R.id.nav_estabelecimentos_estab) {

            Intent intentEstab = new Intent(EstabelecimentoMainActivity.this, ProcuraLocaisActivity.class);
            startActivity(intentEstab);


        } else if (id == R.id.nav_indique_estab) {

            Intent intentIndique = new Intent(EstabelecimentoMainActivity.this, IndiqueEstabelecimentoActivity.class);
            startActivity(intentIndique);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void alertaPendencias(){
        final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_confirma_pedencias_title);
        builder.setMessage(R.string.dialog_confirma_pendencias_message);

        builder.setPositiveButton(R.string.bt_dialog_comanda, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EstabelecimentoMainActivity.this, ComandaActivity.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton(R.string.bt_dialog_nagative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

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

    public void recuperaEstabelecimento(final Toolbar toolbar){

        databaseReference = FirebaseInstance.getFirebase();

        databaseReference.child("estabelecimento")
                .child(preferencias.getIdEstabelecimento()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Estabelecimento estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
                final ActionBar ab = getSupportActionBar();
                toolbar.setTitle(estabelecimento.getNomeEstabelecimento());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void Feedback(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        View viewCustomDialog = getLayoutInflater().inflate(R.layout.custom_feedback, null);
        etFeedback = (EditText) viewCustomDialog.findViewById(R.id.et_feedback);
        builder.setView(viewCustomDialog);
        builder.setPositiveButton("FINALIZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                databaseReference = FirebaseInstance.getFirebase();
                databaseReference.child("comanda")
                        .child(preferencias.getidComanda())
                        .child("situacaoComanda")
                        .setValue(false);

                databaseReference.child("usuario")
                        .child(preferencias.getIdentificador())
                        .child("comandaAberta")
                        .removeValue();

                databaseReference.child("feedback")
                        .child(preferencias.getIdentificador())
                        .child(preferencias.getIdEstabelecimento())
                        .child(preferencias.getidComanda())
                        .setValue(true);

                databaseReference.child("feedback")
                        .child(preferencias.getIdentificador())
                        .child(preferencias.getIdEstabelecimento())
                        .child("mensagemFeed")
                        .setValue(etFeedback.getText().toString());



                preferencias.removerPreferencias();

                Intent intent = new Intent(EstabelecimentoMainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton(R.string.bt_dialog_nagative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
