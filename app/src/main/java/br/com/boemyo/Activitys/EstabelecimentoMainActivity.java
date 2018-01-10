package br.com.boemyo.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class EstabelecimentoMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private CircleImageView cvImgDrawerEstab;
    private TextView tvNomeDrawerEstab;
    private TextView tvEmailDrawerEstab;
    private CardView cardCardapio;
    private CardView cardComanda;
    private Preferencias preferencias;
    private String numMesa;
    EditText etNuMesa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento_main);
        preferencias = new Preferencias(EstabelecimentoMainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bem vindo");
        setSupportActionBar(toolbar);

        numMesa = preferencias.getNumMesa();
        //preferencias.salvarSubTotal(0.0);
        //Log.i("LOG_SUB", preferencias.getSubTotal());
        if(numMesa == null){
            alertaInformaMesa();
        }

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
            PicassoClient.downloadImage(this, preferencias.getUrlImagem(), cvImgDrawerEstab);
            tvNomeDrawerEstab.setText(preferencias.getNome());
            tvEmailDrawerEstab.setText(preferencias.getEmail());
        }


       cardCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EstabelecimentoMainActivity.this, CategoriaCardapioActivity.class);
                startActivity(intent);
            }
        });

        cardComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EstabelecimentoMainActivity.this, ComandaActivity.class);
                startActivity(intent);
                finish();
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
            return true;
        }else if (id == R.id.action_logout_estab) {

            if(preferencias.getSubTotal() != null && !preferencias.getSubTotal().equals("0.0")){
                alertaPendencias();
            }else{
                preferencias.removerPreferencias();
                Intent intent = new Intent(EstabelecimentoMainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home_estab) {

        } else if (id == R.id.nav_pagamento_estab) {
            Intent intentPagamento = new Intent(EstabelecimentoMainActivity.this, PagamentosActivity.class);
            startActivity(intentPagamento);

        } else if (id == R.id.nav_promocao_estab) {

        } else if (id == R.id.nav_favorito_estab) {
            Intent intentFavorito = new Intent(EstabelecimentoMainActivity.this, FavoritoActivity.class);
            startActivity(intentFavorito);

        } else if (id == R.id.nav_avalie_estab) {

        } else if (id == R.id.nav_sobre_estab) {

        } else if (id == R.id.nav_configuracao_estab) {

        } else if (id == R.id.nav_estabelecimentos_estab) {

        } else if (id == R.id.nav_indique_estab) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alertaInformaMesa(){
        final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        View viewCustomDialog = getLayoutInflater().inflate(R.layout.custom_num_mesa, null);
        etNuMesa = (EditText) viewCustomDialog.findViewById(R.id.et_num_mesa);
        builder.setTitle(R.string.dialog_informa_mesa_title);
        builder.setCancelable(false);
        builder.setView(viewCustomDialog);
        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numMesa = etNuMesa.getText().toString();

                if(numMesa.isEmpty()){
                    Toast.makeText(EstabelecimentoMainActivity.this, R.string.mesa_empty, Toast.LENGTH_LONG).show();
                }else{
                    preferencias.salvarNumMesa(numMesa);

                    Comanda comanda = new Comanda();
                    comanda.setIdQrCode(preferencias.getcodQRcode());
                    comanda.setIdComanda(preferencias.getidComanda());
                    comanda.setNumMesa(numMesa);
                    comanda.setSituacaoComanda("0");
                    comanda.salvarFirebase();
                    dialog.dismiss();
                }
            }
        });
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

}