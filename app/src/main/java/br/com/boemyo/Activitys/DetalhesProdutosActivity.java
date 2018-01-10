package br.com.boemyo.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

public class DetalhesProdutosActivity extends AppCompatActivity {
    private Intent intent;
    private Produto produto;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar tbDetalheProduto;
    private Preferencias preferencias;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produtos);
        preferencias = new Preferencias(DetalhesProdutosActivity.this);

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


        btConfirmaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaCorfirmaProduto();
            }
        });

    }

    private void alertaCorfirmaProduto(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_confirma_produto_title);
        builder.setMessage(R.string.dialog_confirma_produto_message);

        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                SimpleDateFormat dataPedidoFormat = new SimpleDateFormat("HH:mm:ss");
                String horaFormatada = dateFormat.format(hora);
                String horaPedido = dataPedidoFormat.format(hora);

                Pedido pedido =  new Pedido();
                Double valorProdutoTotal = produto.getValorProduto() * qtdeAtual;
                pedido.setCodQrCode(preferencias.getcodQRcode());
                pedido.setIdComanda(preferencias.getidComanda());
                pedido.setIdUsuario(preferencias.getIdentificador());
                pedido.setIdPedido(horaFormatada);
                pedido.setIdProduto(produto.getIdProduto());
                pedido.setQtdeProduto(String.valueOf(qtdeAtual));
                pedido.setObsProduto(etObsProduto.getText().toString());
                pedido.setValorPedido(valorProdutoTotal);
                pedido.setHoraPedido(horaPedido);
                pedido.setSituacaoPedido(0);
                pedido.salvarFirebase();

                subTotal(valorProdutoTotal);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(R.string.bt_dialog_nagative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void subTotal(Double valorProduto){
        if(preferencias.getSubTotal() == null){
            subTotal = valorProduto;
        }else{
            subTotal = Double.parseDouble(preferencias.getSubTotal()) + valorProduto;
        }

        preferencias.salvarSubTotal(subTotal);
    }

}
