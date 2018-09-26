package br.com.boemyo.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONObject;

import java.util.Arrays;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Permissao;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;


public class ChoiceActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private Preferencias preferencias;
    private ImageView ivFundoChoice;
    private Button btLogin;
    private Button btCadastro;
    private CarouselView cvInfo;
    private Button loginUserFacebook;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressBar pbChoice;
    private RelativeLayout rlFundoProgressChoice;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA,


    };
    private int[] informacoes = {R.string.info_um, R.string.info_dois, R.string.info_tres};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        preferencias = new Preferencias(ChoiceActivity.this);

        Permissao.validaPermissoes(1 ,this, permissoesNecessarias);
        ivFundoChoice = (ImageView) findViewById(R.id.iv_fundo_choice);
        pbChoice= (ProgressBar) findViewById(R.id.pb_choice);
        rlFundoProgressChoice = (RelativeLayout) findViewById(R.id.rl_fundo_progress_choice);
        cvInfo = (CarouselView) findViewById(R.id.cv_info);
        cvInfo.setPageCount(informacoes.length);
        cvInfo.setIndicatorVisibility(View.GONE);

        loginUserFacebook = (Button) findViewById(R.id.bt_login_facebook);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                validaLoginFace(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                Snackbar snackbar = Snackbar.make(loginUserFacebook, error.toString(), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        ViewListener viewListener = new ViewListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public View setViewForPosition(int position) {

                View customView = getLayoutInflater().inflate(R.layout.custom_info, null);

                TextView tvInfo = (TextView) customView.findViewById(R.id.tv_info_choice);
//                tvInfo.setTextColor(getColor(R.color.colorAccent));
                tvInfo.setText(informacoes[position]);
                tvInfo.setTextColor(R.color.colorPrimaryDark);

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

        loginUserFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager
                        .getInstance()
                        .logInWithReadPermissions(ChoiceActivity.this, Arrays.asList("public_profile", "email"));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    public void validaLoginFace(final AccessToken accessToken){
        firebaseAuth = FirebaseInstance.getFirebaseAuth();
        exibirProgress(true);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(ChoiceActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            GraphRequestAsyncTask requestAsyncTask = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject userFace, GraphResponse response) {
                                    user = firebaseAuth.getCurrentUser();

                                    Usuario usuario = new Usuario();
                                    //Log.i("LOG_IDFACE", userFace.optString("id"));
                                    String tipoIndetificador = userFace.optString("id");
                                    //Log.i("LOG_IDFACE", tipoIndetificador);
                                    preferencias.salvarUsuarioPreferencias(tipoIndetificador, user.getDisplayName(), user.getEmail());
                                    usuario.setIdUsuario(tipoIndetificador);
                                    usuario.setEmailUsuario(user.getEmail());
                                    usuario.setNomeUsuario(user.getDisplayName());
                                    //usuario.setPasswordUsuario(senhaValido);
                                    //usuario.setImagemUsuario(user.getPhotoUrl().toString());
                                    usuario.salvarFirebase();

                                    Intent intent = new Intent(ChoiceActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }).executeAsync();

                            abrirTutorial();

                        }else{
                            int erro = 0;

                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e) {
                                erro = R.string.erro_email;
                            } catch (FirebaseAuthInvalidCredentialsException e) {

                                erro = R.string.erro_password_login;
                            } catch (Exception e) {
                                erro = R.string.erro_geral;
                                e.printStackTrace();
                            }
                            Log.i("ERRO", task.getException().toString());
                            exibirProgress(false);
                            Snackbar snackbar = Snackbar.make(loginUserFacebook, erro, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });



    }

    private void exibirProgress(boolean exibir) {
        pbChoice.setVisibility(exibir ? View.VISIBLE : View.GONE);
        rlFundoProgressChoice.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public void abrirTutorial(){

        if(preferencias.getAbrirTutorial() == null){

            preferencias.salvarAbrirTutorial("abrir_tutorial");
        }

    }



}
