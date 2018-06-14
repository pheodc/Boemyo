package br.com.boemyo.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Helper;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

public class DetalhesProdutosActivity extends AppCompatActivity implements ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private DatabaseReference firebse;
    private RelativeLayout conexao;
    private Intent intent;
    private Produto produto;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar tbDetalheProduto;
    private Preferencias preferencias;
    private Date hora = Calendar.getInstance().getTime();
    private ImageView ivImgProduto;
    private TextView tvDescProduto;
    private TextView tvValorProduto;
    private TextView tvQtdeProduto;
    private EditText etObsProduto;
    private ImageView ivSubtraiProduto;
    private ImageView ivSomaProduto;
    private Button btConfirmaProduto;
    private int qtdeAtual = 1;
    private String valor;
    private Double subTotal = 0.0;
    private BottomSheetBehavior behavior;
    private DatabaseReference databaseReference = FirebaseInstance.getFirebase();
    private String numMesa;
    private EditText etNuMesa;
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produtos);
        preferencias = new Preferencias(DetalhesProdutosActivity.this);
        numMesa = preferencias.getNumMesa();

        View bottomsheet = findViewById(R.id.ll_bottom_sheet);

        behavior = BottomSheetBehavior.from(bottomsheet);


        tvDescProduto = (TextView) findViewById(R.id.tv_desc_produto_detalhes);
        tvValorProduto = (TextView) findViewById(R.id.tv_valor_produto_detalhes);
        tvQtdeProduto = (TextView) findViewById(R.id.tv_valor_qtde_detalhes);
        etObsProduto = (EditText) findViewById(R.id.et_obs_detalhes);
        ivSubtraiProduto = (ImageView) findViewById(R.id.iv_subtrai_qtde_detalhes);
        ivSomaProduto = (ImageView) findViewById(R.id.iv_soma_qtde_detalhes);
        btConfirmaProduto = (Button) findViewById(R.id.bt_confirma_pedido);

        intent = getIntent();
        produto = (Produto) intent.getSerializableExtra("produto");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tb_detalhes_produtos);
        collapsingToolbarLayout.setTitle(produto.getNomeProduto());
        tbDetalheProduto = (Toolbar) findViewById(R.id.tb_detalhes_produtos);
        tbDetalheProduto.setTitle(produto.getNomeProduto());
        setSupportActionBar(tbDetalheProduto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        conexao = (RelativeLayout) findViewById(R.id.conexao_detalhes_produtos);

        ivImgProduto = (ImageView) findViewById(R.id.iv_img_detalhe_produto);
        PicassoClient.downloadImage(this, produto.getUrlImagemProduto(), ivImgProduto);

        tbDetalheProduto.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        NumberFormat format = NumberFormat.getCurrencyInstance();
        tvDescProduto.setText(produto.getDescProduto());
        tvValorProduto.setText(format.format(produto.getValorProduto()));
        Log.i("LOG_IDCOMANDA", String.valueOf(hora));

        if(produto.getIdProduto() != null ){
            Log.i("LOG_IDPRODUTO", produto.getIdProduto());
        }
        ivSubtraiProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qtdeAtual = qtdeAtual - 1;
                valor = String.valueOf(qtdeAtual);
                if(qtdeAtual < 1){
                    qtdeAtual = 1;
                    valor = String.valueOf(qtdeAtual);
                    tvQtdeProduto.setText(valor);
                }else{

                    tvQtdeProduto.setText(valor);
                }
            }
        });

        ivSomaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qtdeAtual = qtdeAtual + 1;
                valor = String.valueOf(qtdeAtual);
                tvQtdeProduto.setText(valor);
            }
        });

        getSubTotal();
        btConfirmaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaPagamento();
            }
        });

    }


    public Double getSubTotal(){

        firebse = FirebaseInstance.getFirebase();

        firebse.child("comanda")
                .child(preferencias.getidComanda()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Comanda comanda = dataSnapshot.getValue(Comanda.class);
                    Log.i("LOG_SUBTOTAL", comanda.getSubTotal().toString());
                    subTotal = comanda.getSubTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    return subTotal;
    }

    public void salvaSubTotal(Double valorProduto){

        if(subTotal == 0){
            subTotal = valorProduto;
        }else{
            subTotal = subTotal + valorProduto;
        }
        Log.i("LOG_SUBTOTAL", subTotal.toString());

        firebse.child("comanda")
                .child(preferencias.getidComanda())
                    .child("subTotal")
                        .setValue(subTotal);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        Log.i("LOG_CONEXAO", String.valueOf(isConnected));
        if (isConnected == false){
            conexao.setVisibility(View.VISIBLE);
        }else{
            conexao.setVisibility(View.GONE);
        }
    }

    public void aceitaPedido(){


        Button btConfirmaPedidoSheet = (Button) findViewById(R.id.bt_confirma_sheet);


        btConfirmaPedidoSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(numMesa == null){
                    alertaInformaMesa();
                }else {
                    validaPedido();
                }

            }
        });




    }

    private void alertaInformaMesa(){
        final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        View viewCustomDialog = getLayoutInflater().inflate(R.layout.custom_num_mesa, null);
        etNuMesa = (EditText) viewCustomDialog.findViewById(R.id.et_num_mesa);
        builder.setTitle(R.string.dialog_informa_mesa_title);
        builder.setCancelable(false);
        builder.setView(viewCustomDialog);
        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        etNuMesa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else {
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numMesa = etNuMesa.getText().toString();

                preferencias.salvarNumMesa(numMesa);

                firebse = FirebaseInstance.getFirebase();

                firebse.child("comanda")
                            .child(preferencias.getidComanda())
                                .child("numMesa")
                                    .setValue(numMesa);

                validaPedido();
                dialog.dismiss();


            }
        });
    }


    public void validaPedido(){

        SimpleDateFormat dataPedidoFormat = new SimpleDateFormat("HH:mm:ss");
        String idPedido = databaseReference.child("pedido").push().getKey();
        String horaPedido = dataPedidoFormat.format(hora);

        Pedido pedido =  new Pedido();
        Comanda comanda = new Comanda();
        Double valorProdutoTotal = produto.getValorProduto() * qtdeAtual;
        pedido.setComanda(preferencias.getidComanda());
        pedido.setIdPedido(idPedido);
        pedido.setProduto(produto.getIdProduto());
        pedido.setQtdeProduto(String.valueOf(qtdeAtual));
        pedido.setObsProduto(etObsProduto.getText().toString());
        pedido.setValorPedido(valorProdutoTotal);
        pedido.setHoraPedido(horaPedido);
        pedido.setSituacaoPedido(0);
        pedido.salvarFirebase();


        comanda.setIdPedido(idPedido);
        comanda.setIdComanda(preferencias.getidComanda());
        //comanda.setIdEstabelecimento(preferencias.getIdEstabelecimento());
        comanda.salvarPedidoComanda();
        salvaSubTotal(valorProdutoTotal);
        finish();


    }

    public void validaPagamento(){
        firebse = FirebaseInstance.getFirebase();
        final Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        final ArrayList<Pagamento> arrayPagamento = new ArrayList<>();

        firebse.child("Pagamento")
                .child(preferencias.getIdentificador()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotPagamento) {
                for(DataSnapshot dados : dataSnapshotPagamento.getChildren()){
                    Pagamento pagamento = dados.getValue(Pagamento.class);
                    arrayPagamento.add(pagamento);
                }

                if(arrayPagamento.size() >= 1){
                    aceitaPedido();
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    alertaAdicionaCartao();
                    vibrator.vibrate(500);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void alertaAdicionaCartao(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_title_adiciona_cartao);
        builder.setMessage(R.string.dialog_message_adiciona_cartao);

        builder.setPositiveButton(R.string.bt_inserir_cartao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(DetalhesProdutosActivity.this, AdicionaNovoCartaoActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.bt_dialog_nagative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
