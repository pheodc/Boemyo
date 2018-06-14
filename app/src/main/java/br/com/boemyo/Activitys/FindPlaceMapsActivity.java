package br.com.boemyo.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

public class FindPlaceMapsActivity extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private List<Estabelecimento> estabelecimentoList = new ArrayList<>();
    private Preferencias preferencias;
    private Usuario usuario;



    @Override
    public void onStop() {
        super.onStop();

        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);

        usuario = new Usuario();
        preferencias = new Preferencias(getContext().getApplicationContext());
        if(preferencias.getLatUsuario() == null || preferencias.getLongUsuario() == null){
            Log.i("LOG_LATLNG", "NULL");
        }else{
            Log.i("LOG_LATLNG", preferencias.getLatUsuario());
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng meuLocal = new LatLng(Double.parseDouble(preferencias.getLatUsuario()),Double.parseDouble(preferencias.getLongUsuario()));
        mMap.addMarker(new MarkerOptions()
                .title("Meu Local")
                .position(new LatLng(meuLocal.latitude, meuLocal.longitude)))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marked_boemyo));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(meuLocal));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(meuLocal.latitude, meuLocal.longitude), 14.0f));


        databaseReference = FirebaseInstance.getFirebase()
                .child("estabelecimento");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    final Estabelecimento estabelecimento = d.getValue( Estabelecimento.class );
                    estabelecimentoList.add(estabelecimento);


                    if(estabelecimento.getLatEstabelecimento() != null && estabelecimento.getLongEstabelecimento() != null){
                        mMap.addMarker(new MarkerOptions()

                                .position(new LatLng(estabelecimento.getLatEstabelecimento(), estabelecimento.getLongEstabelecimento()))
                                .title(estabelecimento.getNomeEstabelecimento()))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logo_nova_pin));
                    }



                                /*Estabelecimento estabelecimentoParse = estabelecimentoList.get(i);
                                Log.i("LOG_POSITION_ESTAB", estabelecimentoList.get(i).toString());
                                Intent intent = new Intent(getActivity(), PerfilEstabelecimentoActivity.class);
                                intent.putExtra("estabelecimento", estabelecimentoParse);
                                startActivity(intent);*/

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);




    }
}


