package br.com.boemyo.Fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import br.com.boemyo.Activitys.FindPlaceMapsActivity;
import br.com.boemyo.Activitys.ProcuraLocaisActivity;
import br.com.boemyo.Configure.Permissao;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.SlidingTabLayout;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EncontraLocaisFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,


    };

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FragmentManager fragmentManager;
    private Usuario usuario;
    private Preferencias preferencias;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        preferencias = new Preferencias(getContext());
        usuario = new Usuario();
        Permissao.validaPermissoes(1, getActivity(), permissoesNecessarias);

        callConnection();
        return inflater.inflate(R.layout.fragment_encontra_locais, container, false);


    }

    public synchronized void callConnection() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

    }

    public void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, EncontraLocaisFragment.this);
    }

    private void stopLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, EncontraLocaisFragment.this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location != null ){
            Log.i("LOG", "Latitude" + location.getLatitude());
            Log.i("LOG", "Longitude" + location.getLongitude());
            usuario.setLatUsuario(location.getLatitude());
            usuario.setLongUsuario(location.getLongitude());
            preferencias.salvarCoordusuario(location.getLatitude(), location.getLongitude());
            fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fl_maps, new FindPlaceMapsActivity(), "MapsFragment" );
            transaction.commitAllowingStateLoss();
        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int resultado : grantResults) {

            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setTitle(R.string.dialog_permission_title);
        builder.setMessage(R.string.dialog_permission_message);

        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
