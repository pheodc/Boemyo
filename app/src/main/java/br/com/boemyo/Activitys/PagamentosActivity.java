package br.com.boemyo.Activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.boemyo.Adapter.ListaPagamentoAdapter;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.RecyclerItemTouchHelper;
import br.com.boemyo.Model.Carteira;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.R;

public class PagamentosActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, ConnectivityChangeReceiver.OnConnectivityChangedListener{

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private RecyclerView rvPagamento;
    private ListaPagamentoAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<Carteira> arrayPagamento;
    private Preferencias preferencias;
    private Toolbar tbPagamentos;
    private RelativeLayout conexao;

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TESTE_CICLO", "Passou onResume");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TESTE_CICLO", "Passou onStop");
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamentos);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        preferencias = new Preferencias(PagamentosActivity.this);

        tbPagamentos = (Toolbar) findViewById(R.id.tb_pagamentos);
        tbPagamentos.setTitle(R.string.title_pagamentos);
        setSupportActionBar(tbPagamentos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbPagamentos.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        conexao = (RelativeLayout) findViewById(R.id.conexao_favoritos);
        rvPagamento = (RecyclerView) findViewById(R.id.rv_pagamentos);

        arrayPagamento = new ArrayList<>();
        adapter = new ListaPagamentoAdapter(this, arrayPagamento);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvPagamento.setLayoutManager(linearLayoutManager);


        rvPagamento.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvPagamento);

        firebase = FirebaseInstance.getFirebase()
                .child("carteira")
                .child(preferencias.getIdentificador());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayPagamento.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Carteira carteira = dados.getValue(Carteira.class);
                    carteira.setTokenCartao(dados.getKey());
                    arrayPagamento.add(carteira);
                    Log.i("LOG_CARTEIRA", "Entrou FOR carteira");

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adiciona_cartao_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_adiciona_cartao) {

            Intent intent = new Intent(PagamentosActivity.this, AdicionaNovoCartaoActivity.class);
            startActivity(intent);

            return true;

        }

        return super.onOptionsItemSelected(item);
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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        adapter.deleteCartao(viewHolder.getAdapterPosition());
    }


}
