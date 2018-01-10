package br.com.boemyo.Activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.boemyo.Adapter.TabAdapter;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Permissao;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.SlidingTabLayout;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

public class ProcuraLocaisActivity extends AppCompatActivity
        {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,


    };

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FragmentManager fragmentManager;
    private Button btVerLocais;
    private Usuario usuario;
    private Preferencias preferencias;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar tbProcura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procura_locais);

        tbProcura = (Toolbar) findViewById(R.id.tb_procura);
        tbProcura.setTitle(R.string.title_procura);
        setSupportActionBar(tbProcura);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbProcura.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);


    }



}
