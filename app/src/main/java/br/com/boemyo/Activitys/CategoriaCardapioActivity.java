package br.com.boemyo.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.boemyo.Adapter.ListaCategoriaAdapter;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.R;

public class CategoriaCardapioActivity extends AppCompatActivity {

    private ListView lvCategoria;
    private ArrayAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<CategoriaCardapio> arrayCategorias;
    private Preferencias preferencias;
    private Toolbar tbCategoriaCardapio;

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

        tbCategoriaCardapio.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        lvCategoria = (ListView) findViewById(R.id.lv_categoria_cardapio);

        arrayCategorias = new ArrayList<>();

        adapter = new ListaCategoriaAdapter(this, arrayCategorias);
        lvCategoria.setAdapter(adapter);
        lvCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });

        //Recuperar Firebase

        firebase = FirebaseInstance.getFirebase()
                .child("categoria")
                .child(preferencias.getcodQRcode());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayCategorias.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    CategoriaCardapio categoriaCardapio = dados.getValue(CategoriaCardapio.class);
                    Log.i("LOG_NOMECAT", categoriaCardapio.getNomeCategoria());
                    arrayCategorias.add(categoriaCardapio);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
