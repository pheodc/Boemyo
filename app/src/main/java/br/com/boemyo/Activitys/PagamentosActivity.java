package br.com.boemyo.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.boemyo.Adapter.ListaPagamentoAdapter;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.R;

public class PagamentosActivity extends AppCompatActivity {

    private ListView lvPagamento;
    private ArrayAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<Pagamento> arrayPagamento;
    private Preferencias preferencias;
    private Toolbar tbPagamentos;

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
        setContentView(R.layout.activity_pagamentos);

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

        lvPagamento = (ListView) findViewById(R.id.lv_pagamentos);

        arrayPagamento = new ArrayList<>();
        adapter = new ListaPagamentoAdapter(this, arrayPagamento);
        lvPagamento.setAdapter(adapter);

        firebase = FirebaseInstance.getFirebase()
                .child("Pagamento")
                .child(preferencias.getIdentificador());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayPagamento.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Pagamento pagamento = dados.getValue(Pagamento.class);
                    Log.i("LOG_NOMECAT", pagamento.getNumCartao());
                    arrayPagamento.add(pagamento);
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

}
