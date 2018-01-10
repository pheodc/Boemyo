package br.com.boemyo.Activitys;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Arrays;

import br.com.boemyo.Configure.Base64Custom;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailUser;
    private EditText passwordUser;
    private TextView tvCriarConta;
    private Button loginUser;
    private Button loginFacebookUser;
    private String identificadorUsuarioLogado;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private Preferencias preferencias;
    private CallbackManager callbackManager;
    private ProgressBar pbLogin;
    private RelativeLayout rlFundoProgress;
    /*@Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        preferencias = new Preferencias(LoginActivity.this);
        emailUser = (EditText) findViewById(R.id.et_email_login);
        passwordUser = (EditText) findViewById(R.id.et_password_login);
        tvCriarConta = (TextView) findViewById(R.id.tv_abrir_cadastro);
        loginUser = (Button) findViewById(R.id.bt_login);
        loginFacebookUser = (Button) findViewById(R.id.bt_login_facebook);
        pbLogin = (ProgressBar) findViewById(R.id.pb_login);
        rlFundoProgress = (RelativeLayout) findViewById(R.id.rl_fundo_progress);
        if(preferencias.getEmail() != null){
            Log.i("LOG_FIREBASE_LOGIN",  preferencias.getNome());
        }else{
            Log.i("LOG_FIREBASE_LOGIN",  "NULL");
        }

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                validaLoginFace(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                Snackbar snackbar = Snackbar.make(loginUser, error.toString(), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validaLogin(emailUser.getText().toString(), passwordUser.getText().toString());

            }
        });

        loginFacebookUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager
                        .getInstance()
                        .logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
            }
        });

        tvCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void validaLogin(final String emailValido, String senhaValido){
        firebaseAuth = FirebaseInstance.getFirebaseAuth();
        exibirProgress(true);
        Log.i("Email_Classe", emailValido);


        if(emailValido.isEmpty() || senhaValido.isEmpty()){
            exibirProgress(false);
            Snackbar snackbar = Snackbar.make(loginUser, R.string.valida_empty, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else {

            firebaseAuth.signInWithEmailAndPassword(emailValido, senhaValido)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                emailUser.setEnabled(false);
                                passwordUser.setEnabled(false);
                                loginUser.setEnabled(false);
                                identificadorUsuarioLogado = Base64Custom.codificarBase64(emailValido);
                                databaseReference = FirebaseInstance.getFirebase()
                                                .child("usuario")
                                                .child(identificadorUsuarioLogado);


                                valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                        Log.i("LOG_FIREBASE",  usuario.getNomeUsuario());
                                        preferencias.salvarUsuarioPreferencias( identificadorUsuarioLogado,
                                                                                usuario.getNomeUsuario(),
                                                                                usuario.getEmailUsuario(),
                                                                                usuario.getImagemUsuario());
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                };
                                databaseReference.addValueEventListener(valueEventListener);




                            } else {
                                int erro = 0;

                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthInvalidUserException e) {
                                    emailUser.setFocusable(true);
                                    emailUser.setText("");
                                    passwordUser.setText("");
                                    erro = R.string.erro_email;
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    passwordUser.setFocusable(true);
                                    passwordUser.setText("");
                                    erro = R.string.erro_password_login;
                                } catch (Exception e) {
                                    erro = R.string.erro_geral;
                                    e.printStackTrace();
                                }
                                Log.i("ERRO", task.getException().toString());
                                exibirProgress(false);
                                Snackbar snackbar = Snackbar.make(loginUser, erro, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    });
        }
    }

    public void validaLoginFace(AccessToken accessToken){
        firebaseAuth = FirebaseInstance.getFirebaseAuth();
        exibirProgress(true);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            emailUser.setEnabled(false);
                            passwordUser.setEnabled(false);
                            loginUser.setEnabled(false);
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                                Usuario usuario = new Usuario();
                                String tipoIndetificador;
                                if(user.getEmail() != null){
                                    tipoIndetificador = user.getEmail();
                                }else{
                                    tipoIndetificador = user.getUid();
                                }
                                String identificadorUsuario = Base64Custom.codificarBase64(tipoIndetificador);
                                preferencias.salvarUsuarioPreferencias(identificadorUsuario, user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
                                usuario.setIdUsuario(identificadorUsuario);
                                usuario.setEmailUsuario(user.getEmail());
                                usuario.setNomeUsuario(user.getDisplayName());
                                //usuario.setPasswordUsuario(senhaValido);
                                usuario.setImagemUsuario(user.getPhotoUrl().toString());
                                usuario.salvarFirebase();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();


                        }else{
                            int erro = 0;

                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e) {
                                emailUser.setFocusable(true);
                                emailUser.setText("");
                                passwordUser.setText("");
                                erro = R.string.erro_email;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                passwordUser.setFocusable(true);
                                passwordUser.setText("");
                                erro = R.string.erro_password_login;
                            } catch (Exception e) {
                                erro = R.string.erro_geral;
                                e.printStackTrace();
                            }
                            Log.i("ERRO", task.getException().toString());
                            exibirProgress(false);
                            Snackbar snackbar = Snackbar.make(loginUser, erro, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });



    }

    private void exibirProgress(boolean exibir) {
        pbLogin.setVisibility(exibir ? View.VISIBLE : View.GONE);
        rlFundoProgress.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
