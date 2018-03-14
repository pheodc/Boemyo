package br.com.boemyo.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.boemyo.Activitys.DetalhesProdutosActivity;
import br.com.boemyo.Activitys.PerfilEstabelecimentoActivity;
import br.com.boemyo.Activitys.ProdutoCardapioActivity;
import br.com.boemyo.Adapter.ListaLocaisAdapter;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaLocaisFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView rvLocais;
    private ListaLocaisAdapter adapter;
    private ArrayList<Estabelecimento> locais;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    public ListaLocaisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_locais, container, false);

        rvLocais = (RecyclerView) view.findViewById(R.id.rv_locais);
        rvLocais.setHasFixedSize(true);
        locais = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvLocais.setLayoutManager(linearLayoutManager);

        adapter = new ListaLocaisAdapter(getActivity(), locais);
        adapter.setRecyclerViewOnClickListenerHack(this);
        rvLocais.setAdapter(adapter);

        //Recuperar Firebase

        firebase = FirebaseInstance.getFirebase()
                .child("estabelecimento");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                locais.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Estabelecimento estabelecimento = dados.getValue( Estabelecimento.class );
                    Log.i("LOG_QRCODES", estabelecimento.getIdQRCODE());
                    locais.add(estabelecimento);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListener);
        return view;
    }

    @Override
    public void onClickListener(AdapterView<?> parent, View view, int position) {}
}
