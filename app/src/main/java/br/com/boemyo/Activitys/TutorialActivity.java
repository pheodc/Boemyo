package br.com.boemyo.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Fragments.TutoUmFragment;
import br.com.boemyo.R;

public class TutorialActivity extends AppCompatActivity {

    private Preferencias preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        preferencias = new Preferencias(this);

        preferencias.salvarAbrirTutorial("esconde_tutorial");

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TutoUmFragment tutoUmFragment = new TutoUmFragment();
        fragmentTransaction.add(R.id.rl_tutorial, tutoUmFragment);
        fragmentTransaction.commit();

    }
}
