package br.com.boemyo.Activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Permissao;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private ImageView ivTipografia;
    private DatabaseReference firebase;
    private FirebaseAuth firebaseAuth;
    private Preferencias preferencias;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Permissao.validaPermissoes(1 ,this, permissoesNecessarias);
        firebaseAuth = FirebaseAuth.getInstance();
        preferencias = new Preferencias(this);
        //firebaseAuth.signOut();
        YoYo.with(Techniques.BounceInDown)
                .duration(2000)
                .playOn(findViewById(R.id.iv_logo_splash));

        YoYo.with(Techniques.BounceInDown)
                .duration(2000)
                .playOn(findViewById(R.id.iv_tipografia));


        if(firebaseAuth.getCurrentUser() != null ){

            validaComandaAtiva();

        }else {

            Intent intent = new Intent(SplashScreenActivity.this, ChoiceActivity.class);
            startActivity(intent);
            finish();

        }


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int resultado : grantResults){

            if( resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();

            }
        }



    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_permission_title);
        builder.setMessage(R.string.dialog_permission_message);

        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void validaComandaAtiva(){

        firebase = FirebaseInstance.getFirebase();

        firebase.child("usuario")
                .child(preferencias.getIdentificador()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        if(usuario.getComandaAberta() != null){
                    firebase.child("comanda")
                                .child(usuario.getComandaAberta()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotComanda) {
                            Comanda comanda = dataSnapshotComanda.getValue(Comanda.class);

                            preferencias.salvarDadosComanda(usuario.getComandaAberta(), comanda.getIdEstabelecimento());

                            Intent intent = new Intent(SplashScreenActivity.this, EstabelecimentoMainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }else{

                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }


}
