package br.com.boemyo.Activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.boemyo.Configure.Base64Custom;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Permissao;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroActivity extends AppCompatActivity {

    public static final int IMAGEM_INTERNA = 12;
    private EditText nomeUser;
    private EditText emailUser;
    private EditText passwordUser;
    private EditText passwordConfirmUser;
    private Button cadastroUser;
    private CircleImageView inserirImagem;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri filePath;
    private Bitmap image;
    private String urlImagemUser;
    private Toolbar tbCadastro;
    private final String DOWNLOADURL = "https://firebasestorage.googleapis.com/v0/b/boemyo-8e4ef.appspot.com/o/Imagens_Usuarios%2F";
    private final String DOWNLOADURL2 ="?alt=media&token=b4be21c8-4abc-467f-9544-9e8af1a741eb";
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    Preferencias preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Permissao.validaPermissoes(1 ,this, permissoesNecessarias);
        preferencias = new Preferencias(CadastroActivity.this);
        firebaseStorage = FirebaseInstance.getFirebaseStorage();
        storageReference = firebaseStorage.getReference();

        tbCadastro = (Toolbar) findViewById(R.id.toolbar_cadastro);
        tbCadastro.setTitle(R.string.title_cadastro);
        tbCadastro.setSubtitle(R.string.sub_title_cadastro);
        setSupportActionBar(tbCadastro);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbCadastro.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        urlImagemUser = "perfil_" + UUID.randomUUID().toString();
        nomeUser = (EditText) findViewById(R.id.et_nome_cadastro);
        emailUser = (EditText) findViewById(R.id.et_email_cadastro);
        passwordUser = (EditText) findViewById(R.id.et_password_cadastro);
        passwordConfirmUser = (EditText) findViewById(R.id.et_password_cnfirma_cadastro);
        cadastroUser = (Button) findViewById(R.id.bt_cadastro);
        inserirImagem = (CircleImageView) findViewById(R.id.iv_inseirir_imagem);

        inserirImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Selecione a Imagem"), IMAGEM_INTERNA);
            }
        });

        cadastroUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                validaCadastro(nomeUser.getText().toString(),
                        emailUser.getText().toString(),
                        passwordUser.getText().toString());


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGEM_INTERNA && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView

                inserirImagem.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void uploadImage(){
        if(filePath != null){

            StorageReference reference = storageReference.child("Imagens_Usuarios/" + urlImagemUser);

            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else{
        }
    }

    public void validaCadastro(final String nomeValido, final String emailValido,  final String senhaValido) {
        firebaseAuth = FirebaseInstance.getFirebaseAuth();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando");
        progressDialog.show();
        Log.i("Email_Classe", emailValido);


        if (emailValido.isEmpty() || senhaValido.isEmpty()) {
            progressDialog.dismiss();
            Snackbar snackbar = Snackbar.make(cadastroUser, R.string.valida_empty, Snackbar.LENGTH_SHORT);
            snackbar.show();

        } else if(!passwordConfirmUser.getText().toString().equals(passwordUser.getText().toString())){
            progressDialog.dismiss();
            Snackbar snackbar = Snackbar.make(cadastroUser, R.string.erro_confirma_senha, Snackbar.LENGTH_SHORT);
            snackbar.show();

        } else {

            firebaseAuth.createUserWithEmailAndPassword(emailValido, senhaValido)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String urlPerfilUser;
                            if (task.isSuccessful()) {
                                if(filePath == null){
                                    urlPerfilUser = "NOIMAGE";
                                }else{
                                    urlPerfilUser = DOWNLOADURL + urlImagemUser + DOWNLOADURL2;
                                }

                                                Usuario usuario = new Usuario();

                                                String identificadorUsuario = Base64Custom.codificarBase64(emailValido);
                                                preferencias.salvarUsuarioPreferencias(identificadorUsuario, nomeValido, emailValido, urlPerfilUser);
                                                usuario.setIdUsuario(identificadorUsuario);
                                                usuario.setEmailUsuario(emailValido);
                                                usuario.setNomeUsuario(nomeValido);
                                                usuario.setPasswordUsuario(senhaValido);
                                                usuario.setImagemUsuario(urlPerfilUser);
                                                usuario.salvarFirebase();
                                                autenticaEmail();
                                                progressDialog.dismiss();
                                                /*Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();*/

                            } else {

                                int erro = 0;

                                try{
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    passwordUser.setFocusable(true);

                                    erro = R.string.erro_password;

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    emailUser.setFocusable(true);
                                    erro = R.string.erro_email;

                                } catch (FirebaseAuthUserCollisionException e) {
                                    emailUser.setFocusable(true);
                                    erro = R.string.erro_exist;

                                } catch (Exception e) {
                                    erro = R.string.erro_geral;
                                    e.printStackTrace();
                                }

                                Log.i("ERRO", task.getException().toString());
                                progressDialog.dismiss();
                                Snackbar snackbar = Snackbar.make(cadastroUser, erro, Snackbar.LENGTH_SHORT);
                                snackbar.show();

                            }
                        }
                    });
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

    private void autenticaEmail(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Snackbar snackbar = Snackbar.make(cadastroUser, R.string.send_email_sucesso, Snackbar.LENGTH_LONG);
                            snackbar.show();
                            firebaseAuth.signOut();

                        }else{
                            Snackbar snackbar = Snackbar.make(cadastroUser, R.string.send_email_erro, Snackbar.LENGTH_LONG);
                            snackbar.show();
                            firebaseAuth.signOut();
                        }
                    }
                });

    }
}
