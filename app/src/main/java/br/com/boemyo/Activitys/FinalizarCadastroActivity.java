package br.com.boemyo.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

public class FinalizarCadastroActivity extends AppCompatActivity {
    private static final String ARQUIVO_PREFERENCES = "ArquivoBoemyo";

    EditText nomeAtual;
    EditText emailAtual;
    EditText dataNascAtual;
    EditText generoAtual;
    ImageView imagemPerfilAtual;
    Button atualizaDados;

    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_cadastro);

        Preferencias preferencias = new Preferencias(FinalizarCadastroActivity.this);
        nomeAtual = (EditText) findViewById(R.id.et_nome_atual);
        emailAtual = (EditText) findViewById(R.id.et_email_atual);
        dataNascAtual = (EditText) findViewById(R.id.et_data_nasc_atual);
        generoAtual = (EditText) findViewById(R.id.et_genero_atual);
        imagemPerfilAtual = (ImageView) findViewById(R.id.iv_perfil_finalize);


        usuario = new Usuario();

            usuario.setIdUsuario(preferencias.getIdentificador());
            usuario.setNomeUsuario(preferencias.getNome());
            usuario.setEmailUsuario(preferencias.getEmail());
            usuario.setImagemUsuario(preferencias.getUrlImagem());
            Log.i("LOG_NOME", preferencias.getNome());


        //Log.i("LOG_USUARIO", usuario.getIdUsuario());
        PicassoClient.downloadImage(this, usuario.getImagemUsuario(), imagemPerfilAtual);
        nomeAtual.setText(usuario.getNomeUsuario());
        emailAtual.setText(usuario.getEmailUsuario());




        atualizaDados = (Button) findViewById(R.id.bt_atualiza_dados);
        atualizaDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario.setDataNascimentoUsuario(dataNascAtual.getText().toString());
                usuario.setGeneroUsuario(generoAtual.getText().toString());
                usuario.salvarFirebase();

                Intent intent = new Intent(FinalizarCadastroActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
