package br.com.boemyo.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.R;


public class ChoiceActivity extends AppCompatActivity {
    Button btLogin;
    Button btCadastro;
    CarouselView cvInfo;
    int[] informacoes = {R.string.info_um, R.string.info_dois, R.string.info_tres};

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        firebaseAuth = FirebaseAuth.getInstance();

        cvInfo = (CarouselView) findViewById(R.id.cv_info);
        cvInfo.setPageCount(informacoes.length);


        ViewListener viewListener = new ViewListener() {
            @Override
            public View setViewForPosition(int position) {

                View customView = getLayoutInflater().inflate(R.layout.custom_info, null);

                TextView tvInfo = (TextView) customView.findViewById(R.id.tv_info_choice);

                tvInfo.setText(informacoes[position]);

                return customView;
            }
        };

        cvInfo.setViewListener(viewListener);
        if(firebaseAuth.getCurrentUser() != null ){
            Intent intent = new Intent(ChoiceActivity.this, HomeActivity.class);
            startActivity(intent);
        }else {

            btLogin = (Button) findViewById(R.id.bt_go_login);
            btCadastro = (Button) findViewById(R.id.bt_go_cadastro);

            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChoiceActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //finish();
                }
            });

            btCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChoiceActivity.this, CadastroActivity.class);
                    startActivity(intent);
                    //finish();
                }
            });

        }
    }
}
