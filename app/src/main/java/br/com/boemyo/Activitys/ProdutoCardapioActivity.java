package br.com.boemyo.Activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.boemyo.Adapter.ListaCategoriaAdapter;
import br.com.boemyo.Adapter.ListaProdutosAdapter;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

public class ProdutoCardapioActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack, ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private RelativeLayout conexao;
    private RecyclerView rvProduto;
    private ListaProdutosAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<Produto> arrayProdutos;
    private Preferencias preferencias;
    private Toolbar tbProduto;

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
        setContentView(R.layout.activity_produto_cardapio);

        preferencias = new Preferencias(ProdutoCardapioActivity.this);

        tbProduto = (Toolbar) findViewById(R.id.tb_produtos);
        tbProduto.setTitle(R.string.title_produto);
        setSupportActionBar(tbProduto);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbProduto.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        conexao = (RelativeLayout) findViewById(R.id.conexao_produtos);
        rvProduto = (RecyclerView) findViewById(R.id.rv_produto_cardapio);
        arrayProdutos = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvProduto.setLayoutManager(linearLayoutManager);

        adapter = new ListaProdutosAdapter(this, arrayProdutos);
        rvProduto.setAdapter(adapter);
        /*lvProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produto produto = (Produto) parent.getItemAtPosition(position);
                Intent intent = new Intent(ProdutoCardapioActivity.this, DetalhesProdutosActivity.class);
                intent.putExtra("produto", produto);
                startActivity(intent);
            }
        });*/

        //Recuperar Firebase

        firebase = FirebaseInstance.getFirebase()
                .child("cardapio")
                .child(preferencias.getIdEstabelecimento());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayProdutos.clear();
                Intent intent = getIntent();

                Bundle bundle = intent.getExtras();

                String categoriaAtual = bundle.getString("CHAVE_BUNDLE_CATEGORIA");


                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Produto produto = dados.getValue(Produto.class);
                    Log.i("LOG_NOMECAT", produto.getNomeProduto());
                    if(produto.getCategoria().equals(categoriaAtual)){
                        arrayProdutos.add(produto);
                        adapter.notifyDataSetChanged();
                    }

                }

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
