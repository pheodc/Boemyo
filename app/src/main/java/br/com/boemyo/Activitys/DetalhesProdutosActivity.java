package br.com.boemyo.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import br.com.boemyo.Adapter.ListaAdicionalAdapter;
import br.com.boemyo.Adapter.ListaPedidosAdapter;
import br.com.boemyo.Configure.Base64Custom;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Helper;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Adicional;
import br.com.boemyo.Model.Carteira;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;
import cieloecommerce.sdk.Merchant;
import cieloecommerce.sdk.ecommerce.Card;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetalhesProdutosActivity extends AppCompatActivity implements ConnectivityChangeReceiver.OnConnectivityChangedListener {
    private final String MERCHANT_ID = "ae999df1-e980-4437-84cd-27f5bc5a8bc6";
    private final String MERCHANT_KEY = "6T90iOa5b8IPUZUSkqPQuLvshK7vY9SMLLWgvX36";
    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private DatabaseReference firebse;
    private RelativeLayout conexao;
    private Intent intent;
    public Produto produto;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar tbDetalheProduto;
    private Preferencias preferencias;
    private Date hora = Calendar.getInstance().getTime();
    private ImageView ivImgProduto;
    private TextView tvDescProduto;
    //private TextView tvValorProduto;
    public TextView tvQtdeProduto;
    private EditText etObsProduto;
    public ImageView ivSubtraiProduto;
    public ImageView ivSomaProduto;
    public Button btConfirmaProduto;
    public int qtdeAtual = 1;
    private String valor;
    private Double subTotal = 0.0;
    private BottomSheetBehavior behavior;
    private DatabaseReference databaseReference = FirebaseInstance.getFirebase();
    private String numMesa;
    private EditText etNuMesa;
    private ProgressBar pbCarregaTransacao;
    private RelativeLayout rlCarregaProgressProduto;
    public Button btConfirmaPedidoSheet;
    public TextView tvValorProduto;
    private RecyclerView rvAdicional;
    private ListaAdicionalAdapter adapter;
    private ArrayList<String> arrayAdicionais;
    private boolean temAdicional;
    private CardView cardAdicional;
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
        tvValorProduto = (TextView) findViewById(R.id.tv_valor_detalhes);
        tvQtdeProduto = (TextView) findViewById(R.id.tv_valor_qtde_detalhes);
        etObsProduto = (EditText) findViewById(R.id.et_obs_detalhes);
        ivSubtraiProduto = (ImageView) findViewById(R.id.iv_subtrai_qtde_detalhes);
        ivSomaProduto = (ImageView) findViewById(R.id.iv_soma_qtde_detalhes);
        btConfirmaProduto = (Button) findViewById(R.id.bt_confirma_pedido);
        pbCarregaTransacao = (ProgressBar) findViewById(R.id.pb_carrega_detalhes_produtos);
        rlCarregaProgressProduto = (RelativeLayout) findViewById(R.id.rl_carrega_detalhes_produtos);
        btConfirmaPedidoSheet = (Button) findViewById(R.id.bt_confirma_sheet);
        rvAdicional = (RecyclerView) findViewById(R.id.rv_adicional_detalhes_produtos);
        cardAdicional = (CardView) findViewById(R.id.card_adicional_detalhes_produto);

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
        if (produto.getUrlImagemProduto() == null){
            PicassoClient.downloadImage(this, "NOIMAGE", ivImgProduto);
        }else {
            PicassoClient.downloadImage(this, produto.getUrlImagemProduto(), ivImgProduto);
        }


        tbDetalheProduto.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        NumberFormat format = NumberFormat.getCurrencyInstance();
        tvDescProduto.setText(produto.getDescProduto());
        tvValorProduto.setText(format.format((produto.getValorProduto()) / 100));
        btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(produto.getValorProduto() / 100));
        btConfirmaProduto.setText("Confirmar Pedido - " + format.format(produto.getValorProduto() / 100));
        Log.i("LOG_IDCOMANDA", String.valueOf(hora));





        getSubTotal();
        //btConfirmaProduto.setText("Confirmar Pedido - " + format.format(produto.getValorProduto() /100));
        btConfirmaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaPagamento();


            }
        });


        arrayAdicionais = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvAdicional.setLayoutManager(linearLayoutManager);

        adapter = new ListaAdicionalAdapter(this, arrayAdicionais);
        rvAdicional.setAdapter(adapter);

        //firebase
        firebse = FirebaseInstance.getFirebase();
        firebse.child("produto")
                    .child(preferencias.getIdEstabelecimento())
                        .child(produto.getIdProduto())
                            .child("adicional").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayAdicionais.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    arrayAdicionais.add(dados.getKey());
                    Log.i("LOG_ADICIONAL", dados.getKey());
                    adapter.notifyDataSetChanged();

                    if(arrayAdicionais.size() < 1){
                        temAdicional = false;



                    }else {
                        temAdicional = true;
                        cardAdicional.setVisibility(View.VISIBLE);
                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(temAdicional == false){
            ivSubtraiProduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    qtdeAtual = qtdeAtual - 1;
                    valor = String.valueOf(qtdeAtual);
                    Double valorProdutoEdit;
                    if(qtdeAtual < 1){
                        qtdeAtual = 1;
                        valor = String.valueOf(qtdeAtual);
                        tvQtdeProduto.setText(valor);
                        valorProdutoEdit = (produto.getValorProduto() /100) * Integer.parseInt(valor);
                        //btConfirmaProduto.setText("Confirmar Pedido - " + format.format(valorProdutoEdit));
                        btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(valorProdutoEdit));
                        tvValorProduto.setText(format.format(valorProdutoEdit));

                    }else{

                        tvQtdeProduto.setText(valor);
                        valorProdutoEdit = (produto.getValorProduto() /100) * Integer.parseInt(valor);
                        // btConfirmaProduto.setText("Confirmar Pedido - " + format.format(valorProdutoEdit));
                        btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(valorProdutoEdit));
                        tvValorProduto.setText(format.format(valorProdutoEdit));
                    }
                }
            });

            ivSomaProduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    Double valorProdutoEdit;
                    qtdeAtual = qtdeAtual + 1;
                    valor = String.valueOf(qtdeAtual);

                    if(qtdeAtual > 10){
                        qtdeAtual = 10;
                        valor = String.valueOf(qtdeAtual);
                        tvQtdeProduto.setText(valor);
                        valorProdutoEdit = (produto.getValorProduto() /100) * Integer.parseInt(valor);
                        btConfirmaProduto.setText("Confirmar Pedido - " + format.format(valorProdutoEdit));
                        btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(valorProdutoEdit));

                        tvValorProduto.setText(format.format(valorProdutoEdit));
                    }else{
                        tvQtdeProduto.setText(valor);
                        valorProdutoEdit = (produto.getValorProduto() /100) * Integer.parseInt(valor);
                        btConfirmaProduto.setText("Confirmar Pedido - " + format.format(valorProdutoEdit));
                        btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(valorProdutoEdit));
                        tvValorProduto.setText(format.format(valorProdutoEdit));
                    }


                }
            });

        }

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

        btConfirmaPedidoSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(numMesa == null){
                    alertaInformaMesa();
                }else {
                    try {

                        validaPedido();
                        pbCarregaTransacao.setVisibility(View.VISIBLE);
                        rlCarregaProgressProduto.setVisibility(View.VISIBLE);
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                try {
                    validaPedido();
                    pbCarregaTransacao.setVisibility(View.VISIBLE);
                    rlCarregaProgressProduto.setVisibility(View.VISIBLE);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();


            }
        });
    }


    public void validaPedido() throws JSONException {
        Log.i("LOG_VALOR_PEDIDO", String.valueOf(adapter.somaTotalGeral));
        final SimpleDateFormat dataPedidoFormat = new SimpleDateFormat("HH:mm:ss");
        final String idPedido = databaseReference.child("pedido").push().getKey();
        String horaPedido = dataPedidoFormat.format(hora);

        final Pedido pedido =  new Pedido();
        final Comanda comanda = new Comanda();
        final Double valorProdutoTotal;
        final int qtdeProdutoFinal;

        if(temAdicional == true){
            valorProdutoTotal = adapter.somaTotalGeral;
            qtdeProdutoFinal = adapter.qtdeAtual;
        }else {
            valorProdutoTotal = produto.getValorProduto() * qtdeAtual;
            qtdeProdutoFinal = qtdeAtual;
        }



        pedido.setComanda(preferencias.getidComanda());
        pedido.setIdPedido(idPedido);
        pedido.setProduto(produto.getIdProduto());
        pedido.setQtdeProduto(String.valueOf(qtdeProdutoFinal));
        pedido.setObsProduto(etObsProduto.getText().toString());
        pedido.setValorPedido(valorProdutoTotal);
        pedido.setHoraPedido(horaPedido);
        pedido.setIdEstabelecimento(preferencias.getIdEstabelecimento());
        pedido.setNomeUsuario(preferencias.getNome());
        pedido.setSituacaoPedido(0);
        comanda.setIdPedido(idPedido);
        comanda.setIdComanda(preferencias.getidComanda());


        firebse.child("carteira")
                    .child(preferencias.getIdentificador())
                        .child(preferencias.getIdPagamento()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Carteira carteira = dataSnapshot.getValue(Carteira.class);
                pedido.setTokenCardPedido(preferencias.getIdPagamento());
                pedido.setBandeiraCartaoPedido(carteira.getBandeira());
                pedido.setCvvPedido(Base64Custom.decodifcarBase64(carteira.getCvv()));
                pedido.setTipoPagamentoPedido(carteira.getTipoPagamento());
                Log.i("LOG_TIPO_PAG", carteira.getTipoPagamento());
                if(carteira.getTipoPagamento().equals("DebitCard")){
                    /*pedido.salvarFirebase();
                    pedido.salvarStatusPedido();
                    comanda.salvarPedidoComanda();
                    salvaSubTotal(valorProdutoTotal);*/
                    alertaDebito();
                    pbCarregaTransacao.setVisibility(View.GONE);
                    rlCarregaProgressProduto.setVisibility(View.GONE);
                    //Toast.makeText(DetalhesProdutosActivity.this, R.string.cod_zero, Toast.LENGTH_LONG).show();
                }

                if(carteira.getTipoPagamento().equals("CreditCard")){
                    try {
                        pedido.post(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException{
                                if (response.isSuccessful()) {
                                    String jsonData = response.body().string();
                                    Log.i("LOG_TRANSACAO",jsonData);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pbCarregaTransacao.setVisibility(View.GONE);
                                            rlCarregaProgressProduto.setVisibility(View.GONE);
                                        }
                                    });

                                    try {
                                        JSONObject jsonOResponse = new JSONObject(jsonData);
                                        String jsonPay = jsonOResponse.getString("Payment");

                                        JSONObject jsonOResponsePay = new JSONObject(jsonPay);
                                        String code = jsonOResponsePay.getString("ReturnCode");
                                        String payId = jsonOResponsePay.getString("PaymentId");
                                        pedido.setIdCieloPedido(payId);

                                        Log.i("LOG_TRANSACAO", code);
                                        if(code.equals("0")|| code.equals("00") || code.equals("000")) {

                                            pedido.salvarFirebase();
                                            pedido.salvarStatusPedido();
                                            comanda.salvarPedidoComanda();
                                            salvaSubTotal(valorProdutoTotal);
                                            if(carteira.getTipoPagamento().equals("CreditCard")){
                                                pedido.postCaptura(new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {

                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {

                                                        Log.i("LOGCAPTURA", response.body().string());
                                                    }
                                                });
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Toast.makeText(DetalhesProdutosActivity.this, R.string.cod_zero, Toast.LENGTH_LONG).show();

                                                }
                                            });

                                            if(temAdicional == true){

                                                for (String idAdicional: adapter.arrayIdAdicionais) {
                                                    databaseReference.child("pedido")
                                                                        .child(idPedido)
                                                                            .child("adicional")
                                                                                    .child(idAdicional).setValue(true);
                                                }



                                            }

                                            finish();
                                        }else {
                                            Log.i("LOG_TRANSACAO", "Entrou Helper");
                                            Helper helper = new Helper();

                                            helper.validaCodCielo(btConfirmaProduto, code);
                                            //validaCodCielo(code);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.i("LOG_TRANSACAO", e.toString());
                                    }


                                } else {
                                    // Request not successful
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("LOG_TRANSACAO", e.toString());
                    }
                }








            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    public void validaPagamento(){
        firebse = FirebaseInstance.getFirebase();
        final Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        final ArrayList<String> arrayCarteira = new ArrayList<>();

        firebse.child("carteira")
                .child(preferencias.getIdentificador()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotPagamento) {
                for(DataSnapshot dados : dataSnapshotPagamento.getChildren()){

                    arrayCarteira.add(dados.getKey());

                    if(preferencias.getIdPagamento() == null){
                        Log.i("LOG_SALVA_CARD", "Entrou Novo");
                        preferencias.salvarIdPagamento(dados.getKey());
                        alteraCartao();
                    }else {
                        alteraCartao();
                    }

                }

                if(arrayCarteira.size() >= 1){
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

                Intent intentInserir = new Intent(DetalhesProdutosActivity.this, AdicionaNovoCartaoActivity.class);
                startActivity(intentInserir);
                dialog.dismiss();

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

    public void alteraCartao(){

        final SupportedCardTypesView ivIconCardSheet = (SupportedCardTypesView) findViewById(R.id.iv_sheet_icon_card_finaliza);
        final TextView tvNumCardSheet = (TextView) findViewById(R.id.tv_sheet_numero_cartao_finaliza);
        //final TextView tvAlterarCardSheet = (TextView) findViewById(R.id.tv_sheet_alterar_cartao_finaliza);
        final RelativeLayout rlAdicionaCartao = (RelativeLayout) findViewById(R.id.rl_aciona_cartao_bottom_sheet_edit_finaliza);
        firebse = FirebaseInstance.getFirebase();
        firebse.child("carteira").child(preferencias.getIdentificador()).child(preferencias.getIdPagamento()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Helper helper = new Helper();
                Carteira carteira= dataSnapshot.getValue(Carteira.class);

                //String numCartaoEdit = pagamento.getNumCartao().substring(pagamento.getNumCartao().length() - 4);
                tvNumCardSheet.setText("●●●● " + Base64Custom.decodifcarBase64(carteira.getNumCartao()));
                helper.validaBandeira(carteira, ivIconCardSheet);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        rlAdicionaCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesProdutosActivity.this, EscolhePagamentoActivity.class);
                startActivity(intent);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


    }

    private void alertaDebito(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Cartão de Débito");
        builder.setMessage(R.string.menssagem_debito);

        builder.setPositiveButton("ENTENDIDO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }






}
