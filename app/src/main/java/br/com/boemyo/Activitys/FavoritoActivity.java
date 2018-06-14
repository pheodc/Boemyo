package br.com.boemyo.Activitys;

import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.boemyo.Adapter.FavoritosAdapter;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Favorito;
import br.com.boemyo.R;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;

public class FavoritoActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack, ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private RecyclerView rvFavorito;
    private FavoritosAdapter adapter;
    private DatabaseReference firebase;
     private ArrayList<Estabelecimento> arrayFavoritos;
    private ArrayList<String> arrayIdEstabelecimentos;
    private Preferencias preferencias;
    private android.support.v7.widget.Toolbar tbFavoritos;

    private RelativeLayout conexao;

    @Override
    protected void onStop() {
        super.onStop();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);



        tbFavoritos = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_favoritos);
        tbFavoritos.setTitle(R.string.title_favoritos);
        tbFavoritos.setSubtitle(R.string.sub_title_favoritos);
        setSupportActionBar(tbFavoritos);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbFavoritos.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        preferencias = new Preferencias(FavoritoActivity.this);
        conexao = (RelativeLayout) findViewById(R.id.conexao_favoritos);
        rvFavorito = (RecyclerView) findViewById(R.id.rv_favoritos);
        rvFavorito.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFavorito.setLayoutManager(linearLayoutManager);
        arrayFavoritos = new ArrayList<>();
        arrayIdEstabelecimentos = new ArrayList<>();
        adapter = new FavoritosAdapter(this, arrayFavoritos);
        adapter.setRecyclerViewOnClickListenerHack(this);
        rvFavorito.setAdapter(adapter);


        firebase = FirebaseInstance.getFirebase();

                firebase.child("usuario").child(preferencias.getIdentificador()).child("favorito").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        arrayFavoritos.clear();

                        for(DataSnapshot dados : dataSnapshot.getChildren()){

                           firebase.child("estabelecimento").child(dados.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot2) {


                                    Estabelecimento estabelecimento = dataSnapshot2.getValue(Estabelecimento.class);
                                    arrayFavoritos.add(estabelecimento);
                                    //Log.i("LOG_FAV", estabelecimento.getNomeEstabelecimento());
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    @Override
    public void onClickListener(AdapterView<?> parent, View view, int position) {

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
