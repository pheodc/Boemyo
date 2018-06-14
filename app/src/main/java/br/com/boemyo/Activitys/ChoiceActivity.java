package br.com.boemyo.Activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Permissao;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;


public class ChoiceActivity extends AppCompatActivity {
    private ImageView ivFundoChoice;
    private Button btLogin;
    private Button btCadastro;
    private CarouselView cvInfo;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA,


    };
    private int[] informacoes = {R.string.info_um, R.string.info_dois, R.string.info_tres};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Permissao.validaPermissoes(1 ,this, permissoesNecessarias);
        ivFundoChoice = (ImageView) findViewById(R.id.iv_fundo_choice);
        cvInfo = (CarouselView) findViewById(R.id.cv_info);
        cvInfo.setPageCount(informacoes.length);
        cvInfo.setIndicatorVisibility(View.GONE);

        ViewListener viewListener = new ViewListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public View setViewForPosition(int position) {

                View customView = getLayoutInflater().inflate(R.layout.custom_info, null);

                TextView tvInfo = (TextView) customView.findViewById(R.id.tv_info_choice);
//                tvInfo.setTextColor(getColor(R.color.colorAccent));
                tvInfo.setText(informacoes[position]);

                return customView;
            }
        };

        cvInfo.setViewListener(viewListener);

            btLogin = (Button) findViewById(R.id.bt_go_login);
            btCadastro = (Button) findViewById(R.id.bt_go_cadastro);

            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChoiceActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            btCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChoiceActivity.this, CadastroActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


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



}
