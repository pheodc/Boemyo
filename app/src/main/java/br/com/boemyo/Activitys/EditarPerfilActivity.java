package br.com.boemyo.Activitys;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

public class EditarPerfilActivity extends AppCompatActivity implements ConnectivityChangeReceiver.OnConnectivityChangedListener  {
    public static final int IMAGEM_INTERNA = 13;
    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private RelativeLayout conexao;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar tbEditarPerfil;
    private TextInputLayout tilNomeEditar;
    private TextInputLayout tilCpfEditar;
    private TextInputLayout tilDataNascEditar;
    private EditText nomeEditar;
    private EditText cpfEditar;
    private EditText dataNascEditar;
    private ImageView imagemPerfilEditar;
    private Button btGeneroMasculino;
    private Button btGeneroFeminino;
    private Button btAtualizaDados;
    private LinearLayout llDataNasc;
    private String generoEditar;
    private Usuario usuario;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri filePath;
    private Bitmap image;
    private String urlImagemUser;
    private final String DOWNLOADURL = "https://firebasestorage.googleapis.com/v0/b/boemyo-8e4ef.appspot.com/o/Imagens_Usuarios%2F";
    private final String DOWNLOADURL2 ="?alt=media&token=90781f88-2568-4228-9fbb-d0d1194a7b84";
    private boolean aux_edit = false;
    private Menu menu;
    private Preferencias preferencias;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        preferencias = new Preferencias(EditarPerfilActivity.this);
        firebaseStorage = FirebaseInstance.getFirebaseStorage();
        storageReference = firebaseStorage.getReference();

        urlImagemUser = "perfil_" + UUID.randomUUID().toString();
        nomeEditar = (EditText) findViewById(R.id.et_nome_editar);
        cpfEditar = (EditText) findViewById(R.id.et_cpf_editar);
        dataNascEditar = (EditText) findViewById(R.id.et_data_nasc_editar);
        imagemPerfilEditar = (ImageView) findViewById(R.id.iv_editar_perfil);
        btGeneroMasculino = (Button) findViewById(R.id.bt_genero_masculino);
        btGeneroFeminino = (Button) findViewById(R.id.bt_genero_feminino);
        llDataNasc = (LinearLayout) findViewById(R.id.ll_data_nasc_editar);
        tilNomeEditar = (TextInputLayout) findViewById(R.id.til_nome_editar);
        tilCpfEditar = (TextInputLayout) findViewById(R.id.til_cpf_editar);
        tilDataNascEditar = (TextInputLayout) findViewById(R.id.til_data_nasc_editar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tb_editar_perfil);
        btAtualizaDados = (Button) findViewById(R.id.bt_atualizar_perfill);

        collapsingToolbarLayout.setTitle("Seu Perfil:");
        tbEditarPerfil = (Toolbar) findViewById(R.id.tb_editar_perfil);
        tbEditarPerfil.setTitle(R.string.title_editar_perfil);
        setSupportActionBar(tbEditarPerfil);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tbEditarPerfil.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        conexao = (RelativeLayout) findViewById(R.id.conexao_editar_perfil);
        imagemPerfilEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Selecione a Imagem"), IMAGEM_INTERNA);
            }
        });
        if(preferencias.getUrlImagem() != null){
            PicassoClient.downloadImage(this, preferencias.getUrlImagem(), imagemPerfilEditar);
        }else {

        }

        desabilitaCampos();
        selecionaGenero();

        databaseReference = FirebaseInstance.getFirebase()
                .child("usuario")
                .child(preferencias.getIdentificador());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usuario = dataSnapshot.getValue(Usuario.class);
                recuperaDados(usuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(cpfEditar, simpleMaskTelefone);
        cpfEditar.addTextChangedListener(maskTelefone);

        abrirCalendario();

        dataNascEditar.setFocusable(false);

        btAtualizaDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar;
                String urlPerfilUser = null;
                if(filePath == null){
                   urlPerfilUser = usuario.getImagemUsuario();
                }else{
                    uploadImage();
                    urlPerfilUser = DOWNLOADURL + urlImagemUser + DOWNLOADURL2;
                }

                if(nomeEditar.getText().toString().isEmpty()){
                    snackbar = Snackbar.make(btAtualizaDados, R.string.valida_empty_nome_editar, Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }else if(cpfEditar.getText().toString().length() < 14){
                    snackbar = Snackbar.make(btAtualizaDados, R.string.valida_cpf_editar, Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }else{
                    usuario.setIdUsuario(preferencias.getIdentificador());
                    usuario.setImagemUsuario(urlPerfilUser);
                    usuario.setNomeUsuario(nomeEditar.getText().toString());
                    usuario.setCpfUsuario(cpfEditar.getText().toString());
                    usuario.setDataNascimentoUsuario(dataNascEditar.getText().toString());
                    usuario.setGeneroUsuario(generoEditar);

                    preferencias.salvarUsuarioPreferencias(usuario.getIdUsuario(),
                            usuario.getNomeUsuario(),
                            usuario.getEmailUsuario());
                    Log.i("LOG_USER", usuario.getNomeUsuario());
                    usuario.salvarFirebase();
                    snackbar = Snackbar.make(btAtualizaDados, R.string.valida_sucesso, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }




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

                imagemPerfilEditar.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editar_perfil_menu, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_editar_perfil){
            if (aux_edit == true){
                aux_edit = false;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(EditarPerfilActivity.this, R.mipmap.ic_close_white_24dp));
                habilitaCampos();
            }else{
                aux_edit = true;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(EditarPerfilActivity.this, R.mipmap.ic_border_color_white_24dp));
                desabilitaCampos();
            }
        }


        return super.onOptionsItemSelected(item);
    }


    private void recuperaDados(Usuario usuario){
        nomeEditar.setText(usuario.getNomeUsuario());
        cpfEditar.setText(usuario.getCpfUsuario());
        dataNascEditar.setText(usuario.getDataNascimentoUsuario());
        generoEditar = usuario.getGeneroUsuario();
        if(generoEditar != null){
            if( generoEditar.equals("M") ){
                btGeneroMasculino.setBackground(getResources().getDrawable(R.drawable.style_radius_button));
                btGeneroMasculino.setTextColor(getResources().getColor(R.color.colorText));
            }else if(generoEditar.equals("F")){
                btGeneroFeminino.setBackground(getResources().getDrawable(R.drawable.style_radius_button));
                btGeneroFeminino.setTextColor(getResources().getColor(R.color.colorText));
            }
        }

    }

    private void selecionaGenero(){
        btGeneroMasculino.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                btGeneroMasculino.setBackground(getResources().getDrawable(R.drawable.style_radius_button));
                btGeneroMasculino.setTextColor(getResources().getColor(R.color.colorText));
                btGeneroFeminino.setBackground(getResources().getDrawable(R.drawable.style_radius_button_transparent));
                btGeneroFeminino.setTextColor(getResources().getColor(R.color.colorAccent));
                generoEditar = "M";
            }
        });

        btGeneroFeminino.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                btGeneroFeminino.setBackground(getResources().getDrawable(R.drawable.style_radius_button));
                btGeneroFeminino.setTextColor(getResources().getColor(R.color.colorText));
                btGeneroMasculino.setBackground(getResources().getDrawable(R.drawable.style_radius_button_transparent));
                btGeneroMasculino.setTextColor(getResources().getColor(R.color.colorAccent));
                generoEditar = "F";
            }
        });
    }

    public void uploadImage(){
        if(filePath != null){

            StorageReference reference = storageReference.child("Imagens_Usuarios/" + urlImagemUser);

            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i("LOG_IMG_EDITAR", "Sucesso" );
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("LOG_IMG_EDITAR", "Não salvou" + e );
                        }
                    });
        }else{
        }
    }

    public void desabilitaCampos(){
        btAtualizaDados.setBackgroundColor(getResources().getColor(R.color.colorSecondaryText));
        btAtualizaDados.setTextColor(getResources().getColor(R.color.color_grey));
        btAtualizaDados.setEnabled(false);
        btGeneroFeminino.setEnabled(false);
        btGeneroMasculino.setEnabled(false);
        nomeEditar.setEnabled(false);
        cpfEditar.setEnabled(false);
        dataNascEditar.setEnabled(false);
        nomeEditar.setClickable(false);
        cpfEditar.setClickable(false);
        dataNascEditar.setClickable(false);
        imagemPerfilEditar.setEnabled(false);

    }

    public void habilitaCampos(){
        btAtualizaDados.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btAtualizaDados.setEnabled(true);
        btGeneroFeminino.setEnabled(true);
        btGeneroMasculino.setEnabled(true);
        nomeEditar.setEnabled(true);
        cpfEditar.setEnabled(true);
        dataNascEditar.setEnabled(true);
        nomeEditar.setClickable(true);
        cpfEditar.setClickable(true);
        dataNascEditar.setClickable(true);
        imagemPerfilEditar.setEnabled(true);

    }

    public void abrirCalendario(){


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dataNascEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditarPerfilActivity.this, R.style.AppThemeDialog, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        dataNascEditar.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        Log.i("LOG_CONEXAO", String.valueOf(isConnected));
        if (isConnected == false){
            conexao.setVisibility(View.VISIBLE);
            desabilitaCampos();
        }else{
            conexao.setVisibility(View.GONE);

        }
    }

}
