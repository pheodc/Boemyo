package br.com.boemyo.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.facebook.FacebookSdk;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.boemyo.Configure.Base64Custom;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Helper;
import br.com.boemyo.Configure.Permissao;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.DataComanda;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QRCodeReaderView.OnQRCodeReadListener, ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private RelativeLayout conexao;
    private RelativeLayout rlQrcodeDialog;
    private CircleImageView cvImgDrawer;
    private TextView tvNomeDrawer;
    private TextView tvEmailDrawer;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser userInfo;
    private DatabaseReference firebase;
    private QRCodeReaderView qrCodeReaderView;
    private Preferencias preferencias;
    private ArrayList<String> qrcodes;
    private String idEstabelecimento;
    private Date hora = Calendar.getInstance().getTime();
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.VIBRATE,

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home);
        preferencias = new Preferencias(HomeActivity.this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(preferencias.getNome());
        setSupportActionBar(toolbar);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Permissao.validaPermissoes(1 ,this, permissoesNecessarias);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);


        toolbar.setSubtitle(R.string.app_name);

        firebaseAuth = FirebaseInstance.getFirebaseAuth();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        cvImgDrawer = (CircleImageView) view.findViewById(R.id.iv_img_drawer);
        tvNomeDrawer = (TextView) view.findViewById(R.id.tv_nome_drawer);
        tvEmailDrawer = (TextView) view.findViewById(R.id.tv_email_drawer);
        conexao = (RelativeLayout) findViewById(R.id.conexao_home);



        cvImgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EditarPerfilActivity.class);
                startActivity(intent);
            }
        });

        if(preferencias.getAbrirTutorial().equals(null)){
            preferencias.salvarAbrirTutorial("abrir_tutorial");
        }

        if(preferencias.getAbrirTutorial().equals("abrir_tutorial")){

            Intent intentTutorial = new Intent(HomeActivity.this, TutorialActivity.class);
            startActivity(intentTutorial);


        }

        if(preferencias.getNome() != null){

            //PicassoClient.downloadImage(this, preferencias.getUrlImagem(), cvImgDrawer);
            tvNomeDrawer.setText(preferencias.getNome());
            tvEmailDrawer.setText(preferencias.getEmail());
        }






        rlQrcodeDialog =(RelativeLayout) findViewById(R.id.rl_qrcode_dialog);
        rlQrcodeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlQrcodeDialog.setVisibility(View.INVISIBLE);
            }
        });

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.camera_preview);
        qrCodeReaderView.setOnQRCodeReadListener((QRCodeReaderView.OnQRCodeReadListener) this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();
        qrcodes = new ArrayList<>();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.botton_nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.action_estabelecimentos:
                        Intent intentEstab = new Intent(HomeActivity.this, ProcuraLocaisActivity.class);
                        startActivity(intentEstab);
                        break;

                    case R.id.action_indique:
                        Intent intentIndique = new Intent(HomeActivity.this, IndiqueEstabelecimentoActivity.class);
                        startActivity(intentIndique);
                        break;

                    case R.id.action_favoritos:
                        Intent intentFavorito = new Intent(HomeActivity.this, FavoritoActivity.class);
                        startActivity(intentFavorito);
                        break;
                }
                return true;
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


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int resultado : grantResults){

            if( resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();

            }
        }



    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_permission_title);
        builder.setMessage(R.string.dialog_permission_message);

        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentConfig = new Intent(HomeActivity.this, ConfigActivity.class);
            startActivity(intentConfig);

            return true;

        } else if (id == R.id.action_logout) {
            preferencias.limparDados();

            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            Intent intentPagamento = new Intent(HomeActivity.this, PagamentosActivity.class);
            startActivity(intentPagamento);
            Intent intentSair = new Intent(HomeActivity.this, ChoiceActivity.class);
            startActivity(intentSair);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_pagamento) {
            Intent intentPagamento = new Intent(HomeActivity.this, PagamentosActivity.class);
            startActivity(intentPagamento);

        } else if (id == R.id.nav_promocao) {
            Snackbar snackbar = Snackbar.make(rlQrcodeDialog, "Em Construção", Snackbar.LENGTH_SHORT);
            snackbar.show();

        } else if (id == R.id.nav_favorito) {
            Intent intentFavorito = new Intent(HomeActivity.this, FavoritoActivity.class);
            startActivity(intentFavorito);

        } else if (id == R.id.nav_sobre) {

            Intent intentSobre = new Intent(HomeActivity.this, SobreActivity.class);
            startActivity(intentSobre);

        } else if (id == R.id.nav_configuracao) {

            Intent intentConfig = new Intent(HomeActivity.this, ConfigActivity.class);
            startActivity(intentConfig);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onQRCodeRead(final String text, PointF[] points) {
        firebase = FirebaseInstance.getFirebase();
        final Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        Log.i("LOG_QRS", text);
        qrCodeReaderView.stopCamera();

        if(!text.isEmpty()) {
            Log.i("LOG_QRS", "Entrou aqui");


            firebase.child("qrcode")
                    .child(EncodeString(text)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("LOG_QRS", dataSnapshot.toString());
                    if(dataSnapshot.getValue() != null){
                        for (DataSnapshot dados : dataSnapshot.getChildren()){
                            Log.i("LOG_QRS", dados.getKey());

                            Comanda comanda = new Comanda();
                            Log.i("LOG_CONTAINS", "Possue");
                            String idComanda = firebase.child("comanda").push().getKey();
                            preferencias.salvarQrCodeEstabelecimento(text, idComanda, dados.getKey());

                            comanda.setIdComanda(idComanda);
                            comanda.setIdUsuario(preferencias.getIdentificador());
                            comanda.setIdEstabelecimento(dados.getKey());
                            comanda.setSituacaoComanda(true);
                            comanda.setDataAbertura(getDataAbertura(hora));
                            comanda.setHoraAbertura(getHoraAbertura(hora));
                            comanda.setSubTotal(0.0);

                            comanda.salvarFirebase();
                            comanda.salvarComandaUsuario();
                            comanda.salvarComandaEstabelecimento();
                            comanda.salvarComandaAberta();

                            DataComanda dataComanda = new DataComanda();

                            dataComanda.setDataComanda(getDataAbertura(hora));
                            dataComanda.setIdEstabelecimento(dados.getKey());
                            dataComanda.setIdComanda(idComanda);

                            dataComanda.salvarFirebase();

                            Intent intent = new Intent(HomeActivity.this, EstabelecimentoMainActivity.class);
                            startActivity(intent);

                            preferencias.salvarAbrirCategoria("open");

                            finish();

                        }
                    }else{
                        alertaQRCODE();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.i("VALUE_QR", text);

            vibrator.vibrate(500);
        }


    }



    private void alertaQRCODE(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_qrcode_title);
        builder.setMessage(R.string.dialog_qrcode_message);

        builder.setPositiveButton(R.string.bt_dialog_qrcode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                recreate();
            }
        });

        AlertDialog dialog = builder.create();
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

    private String getDataAbertura(Date hora){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        String dataAbertura = simpleDateFormat.format(hora);

        return dataAbertura;
    }


    private String getHoraAbertura(Date hora){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        String horaAbertura = simpleDateFormat.format(hora);

        return horaAbertura;
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}
