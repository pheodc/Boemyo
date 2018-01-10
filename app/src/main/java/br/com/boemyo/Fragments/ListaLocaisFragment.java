package br.com.boemyo.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaLocaisFragment extends Fragment {

    private ListView lvLocais;
    private ArrayAdapter adapter;
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

        lvLocais = (ListView) view.findViewById(R.id.lv_locais);

        locais = new ArrayList<>();
        /*adapter = new ArrayAdapter(
                this,
                R.layout.lista_locais,
                locais

        );*/
        adapter = new ListaLocaisAdapter(getActivity(), locais);
        lvLocais.setAdapter(adapter);
        lvLocais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Estabelecimento estabelecimento = (Estabelecimento) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), PerfilEstabelecimentoActivity.class);
                intent.putExtra("estabelecimento", estabelecimento);
                startActivity(intent);
                getActivity().finish();
            }
        });
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

}
