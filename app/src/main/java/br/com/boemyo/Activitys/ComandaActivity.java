package br.com.boemyo.Activitys;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import br.com.boemyo.Adapter.ListaPedidosAdapter;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Helper;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.RecyclerItemTouchHelper;
import br.com.boemyo.Configure.RecyclerItemTouchHelperComanda;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

public class ComandaActivity extends AppCompatActivity implements RecyclerItemTouchHelperComanda.RecyclerItemTouchHelperListener, ConnectivityChangeReceiver.OnConnectivityChangedListener{

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private RelativeLayout conexao;
    private RecyclerView rvComanda;
    private ListaPedidosAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<String> arrayPedidos;
    private Preferencias preferencias;
    private Toolbar tbComanda;
    private TextView tvSubTotal;
    //private Button btFinalizarComanda;
    private Button btConfirmaPagamentoSheet;
    private Double subTotal = 0.0;
    private Pedido pedido;
    private NumberFormat format = NumberFormat.getCurrencyInstance();
    private Pagamento pagamento;
    private ProgressBar pbCarregaComanda;

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda);

        preferencias = new Preferencias(ComandaActivity.this);

        tbComanda = (Toolbar) findViewById(R.id.tb_comanda);
        tbComanda.setTitle(R.string.title_comanda);
        setSupportActionBar(tbComanda);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        tbComanda.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComandaActivity.this, EstabelecimentoMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        conexao = (RelativeLayout) findViewById(R.id.conexao_comanda);
        rvComanda = (RecyclerView) findViewById(R.id.rv_comanda);
        tvSubTotal = (TextView) findViewById(R.id.tv_subtotal_pedido);
        //btFinalizarComanda = (Button) findViewById(R.id.bt_finalizar_comanda);

        pbCarregaComanda = (ProgressBar) findViewById(R.id.pb_carrega_comanda);
        arrayPedidos = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvComanda.setLayoutManager(linearLayoutManager);

        adapter = new ListaPedidosAdapter(this, arrayPedidos);
        rvComanda.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperComanda(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvComanda);
        //Recuperar Firebase

        firebase = FirebaseInstance.getFirebase().child("comanda")
                                                     .child(preferencias.getidComanda());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayPedidos.clear();



                    Comanda comanda = dataSnapshot.getValue(Comanda.class);
                    Log.i("LOG_COMANDA", comanda.getSubTotal().toString());
                    //tvSubTotal.setText(format.format( comanda.getSubTotal()));
                    startCountAnimation(Float.parseFloat(comanda.getSubTotal().toString())/100);
                firebase = FirebaseInstance.getFirebase();
                if(preferencias.getidComanda() != null){
                    firebase.child("comanda").child(preferencias.getidComanda()).child("pedidos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dados : dataSnapshot.getChildren()){
                                Log.i("LOG_COMANDA_PEDIDOS", dados.getKey());
                                arrayPedidos.add(dados.getKey());
                                adapter.notifyDataSetChanged();
                                pbCarregaComanda.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        //Fluxo antigo

        /*firebase.child("comanda")
                    .child(preferencias.getidComanda()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayPedidos.clear();
                Comanda comanda = dataSnapshot.getValue(Comanda.class);
                Log.i("LOG_COMANDA", comanda.getSubTotal().toString());
                //tvSubTotal.setText(format.format( comanda.getSubTotal()));
                startCountAnimation(Float.parseFloat(comanda.getSubTotal().toString())/100);

                firebase.child("comanda").child(preferencias.getidComanda()).child("pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dados : dataSnapshot.getChildren()){
                            Log.i("LOG_COMANDA_PEDIDOS", dados.getKey());
                            arrayPedidos.add(dados.getKey());
                            adapter.notifyDataSetChanged();
                            pbCarregaComanda.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



        /*btFinalizarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Feedback();

            }
        });*/



    }

   /* public void validaPagamento(){

        final SupportedCardTypesView ivIconCardSheet = (SupportedCardTypesView) findViewById(R.id.iv_sheet_icon_card_finaliza);
        final TextView tvNumCardSheet = (TextView) findViewById(R.id.tv_sheet_numero_cartao_finaliza);
        //final TextView tvAlterarCardSheet = (TextView) findViewById(R.id.tv_sheet_alterar_cartao_finaliza);
        final RelativeLayout rlAdicionaCartao = (RelativeLayout) findViewById(R.id.rl_aciona_cartao_bottom_sheet_edit_finaliza);

        rlAdicionaCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComandaActivity.this, EscolhePagamentoActivity.class);
                startActivity(intent);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        if(preferencias.getIdPagamento() != null) {
            firebase.child("Pagamento").child(preferencias.getIdentificador()).child(preferencias.getIdPagamento()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        pagamento = dataSnapshot.getValue(Pagamento.class);

                        if(preferencias.getIdPagamento().equals("pag_local")){

                            tvNumCardSheet.setText(R.string.pagamento_local);
                            ivIconCardSheet.setSupportedCardTypes(CardType.UNKNOWN);
                        }else{

                            String numCartaoEdit = pagamento.getNumCartao().substring(pagamento.getNumCartao().length() - 4);
                            tvNumCardSheet.setText("●●●● " + numCartaoEdit);
                            helper.validaBandeira(pagamento, ivIconCardSheet);
                        }

                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
        }else {

            firebase.child("Pagamento")
                    .child(preferencias.getIdentificador()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null) {
                        Log.i("LOG_COM_NULO", "ENTROU NULO");
                        tvNumCardSheet.setText(R.string.pagamento_adiciona);
                        ivIconCardSheet.setSupportedCardTypes(CardType.UNKNOWN);

                    }else{

                        for(DataSnapshot dados : dataSnapshot.getChildren()){
                            pagamento = dados.getValue(Pagamento.class);

                            String numCartaoEdit = pagamento.getNumCartao().substring(pagamento.getNumCartao().length() - 4);
                            tvNumCardSheet.setText("●●●● " + numCartaoEdit);
                            helper.validaBandeira(pagamento, ivIconCardSheet);

                        }

                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }




       /* final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_title_encerra_comanda);
        builder.setMessage(R.string.dialog_message_encerra_comanda);


        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(preferencias.getIdPagamento() == "pag_local"){
                    Toast.makeText(ComandaActivity.this, "Em Construção", Toast.LENGTH_SHORT).show();
                }else{
                    firebase.child("comanda")
                            .child(preferencias.getidComanda())
                            .child("situacaoComanda")
                            .setValue(false);

                    alertaConfirmaEncerrado();
                }

                preferencias.removerPreferencias();
                Intent intent = new Intent(ComandaActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton(R.string.bt_dialog_nagative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ComandaActivity.this, EstabelecimentoMainActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        adapter.cancelaPedido(viewHolder.getAdapterPosition());
    }

    private void startCountAnimation(Float finalValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, finalValue);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                tvSubTotal.setText(format.format(animation.getAnimatedValue()));
            }
        });
        animator.start();
    }


}
