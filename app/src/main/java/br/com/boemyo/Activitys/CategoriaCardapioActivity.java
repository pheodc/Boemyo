package br.com.boemyo.Activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.boemyo.Adapter.ListaCategoriaAdapter;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.R;

public class CategoriaCardapioActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack, ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private RelativeLayout conexao;
    private RecyclerView rvCategoria;
    private ListaCategoriaAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<CategoriaCardapio> arrayCategorias;
    private Preferencias preferencias;
    private Toolbar tbCategoriaCardapio;
    private ProgressBar pbCarregaCategoriaCardapio;
    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_cardapio);

        preferencias = new Preferencias(CategoriaCardapioActivity.this);

        tbCategoriaCardapio = (Toolbar) findViewById(R.id.tb_categoria_cardapio);
        tbCategoriaCardapio.setTitle(R.string.title_categoria);
        setSupportActionBar(tbCategoriaCardapio);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(preferencias.getAbrirCategoria() != null){

            preferencias.removerAbrirCategoria();

        }

        tbCategoriaCardapio.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        conexao = (RelativeLayout) findViewById(R.id.conexao_categoria_cardapio);

        pbCarregaCategoriaCardapio = (ProgressBar) findViewById(R.id.pb_categoria_cardapio);
        rvCategoria = (RecyclerView) findViewById(R.id.rv_categoria_cardapio);
        rvCategoria.setHasFixedSize(true);
        arrayCategorias = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvCategoria.setLayoutManager(gridLayoutManager);

        adapter = new ListaCategoriaAdapter(this, arrayCategorias);
        rvCategoria.setAdapter(adapter);
        /*lvCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoriaCardapio categoriaCardapio = (CategoriaCardapio) parent.getItemAtPosition(position);
                Log.i("LOG_POSITION", categoriaCardapio.getNomeCategoria());
                Bundle bundle = new Bundle();
                Intent intent = new Intent(CategoriaCardapioActivity.this, ProdutoCardapioActivity.class);

                bundle.putString("CHAVE_BUNDLE_CATEGORIA", categoriaCardapio.getNomeCategoria());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });*/

        //Recuperar Firebase

        firebase = FirebaseInstance.getFirebase()
                .child("categoria")
                .child(preferencias.getIdEstabelecimento());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayCategorias.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    CategoriaCardapio categoriaCardapio = dados.getValue(CategoriaCardapio.class);
                    Log.i("LOG_NOMECAT", categoriaCardapio.getNomeCategoria());
                    arrayCategorias.add(categoriaCardapio);
                    pbCarregaCategoriaCardapio.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
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

    @Override
    public void onClickListener(AdapterView<?> parent, View view, int position) {

    }
}
