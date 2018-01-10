package br.com.boemyo.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.boemyo.Adapter.ListaPedidosAdapter;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Helper;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

public class ComandaActivity extends AppCompatActivity {

    private ListView lvComanda;
    private ArrayAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<Pedido> arrayPedidos;
    private Preferencias preferencias;
    private Toolbar tbComanda;
    private TextView tvSubTotal;
    private Button btFinalizarComanda;
    private Double subTotal = 0.0;
    private Pedido pedido;
    private NumberFormat format = NumberFormat.getCurrencyInstance();


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

        lvComanda = (ListView) findViewById(R.id.lv_comanda);
        tvSubTotal = (TextView) findViewById(R.id.tv_subtotal_pedido);
        btFinalizarComanda = (Button) findViewById(R.id.bt_finalizar_comanda);
        arrayPedidos = new ArrayList<>();
        adapter = new ListaPedidosAdapter(this, arrayPedidos);
        lvComanda.setAdapter(adapter);
        lvComanda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Date horarioAtual =  Calendar.getInstance().getTime();
                Pedido pedido = (Pedido) parent.getItemAtPosition(position);
                SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");
                String horaFormatada = dataFormat.format(horarioAtual);
                int horaPedido = Integer.parseInt(pedido.getHoraPedido().substring(0,2));
                int minPedido = Integer.parseInt(pedido.getHoraPedido().substring(3,5));
                int horaAtual = Integer.parseInt(horaFormatada.substring(0,2));
                int minAtual = Integer.parseInt(horaFormatada.substring(3,5));
                Long diferenca = Helper.getDiferencaData(horaPedido, minPedido, horaAtual, minAtual);

                Log.i("LOG_HORA", String.valueOf(diferenca));
                if(pedido.getSituacaoPedido() == 1) {
                    Toast.makeText(ComandaActivity.this, R.string.cancelamento_pedido_entregue, Toast.LENGTH_LONG).show();
                }else if(pedido.getSituacaoPedido() == 2){
                    Toast.makeText(ComandaActivity.this, R.string.cancelamento_realizado, Toast.LENGTH_LONG).show();
                }else {
                    alertaCancelamento(pedido, diferenca);
                }
            }
        });

        //Recuperar Firebase

        firebase = FirebaseInstance.getFirebase()
                .child("comanda")
                    .child(preferencias.getcodQRcode())
                        .child(preferencias.getidComanda())
                            .child(preferencias.getIdentificador());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayPedidos.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    pedido = dados.getValue(Pedido.class);
                    Log.i("LOG_NOMECAT", pedido.getIdPedido());
                    arrayPedidos.add(pedido);
                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if(preferencias.getSubTotal() != null){
            subTotal = Double.parseDouble(preferencias.getSubTotal());
        }

        tvSubTotal.setText(format.format(subTotal));

        btFinalizarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferencias.getSubTotal() == null){
                    preferencias.removerPreferencias();
                    Intent intent = new Intent(ComandaActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                Intent intent = new Intent(ComandaActivity.this, FinalizarComandaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void alertaCancelamento(final Pedido pedido, final Long diferencaHorario){
        final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_confirma_cancelar_pedido_title);
        if(diferencaHorario > 5){

            builder.setMessage(R.string.dialog_confirma_cancelar_pedido_com_taxa_message);

        }else{

            builder.setMessage(R.string.dialog_confirma_cancelar_pedido_sem_taxa_message);
        }

        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(diferencaHorario > 5){
                    subTotal = (subTotal - pedido.getValorPedido()) + 5;
                    preferencias.salvarSubTotal(subTotal);
                    tvSubTotal.setText(format.format(subTotal));

                }else{
                    subTotal = subTotal - pedido.getValorPedido();
                    preferencias.salvarSubTotal(subTotal);
                    tvSubTotal.setText(format.format(subTotal));
                }

                firebase = FirebaseInstance.getFirebase()
                        .child("comanda")
                        .child(preferencias.getcodQRcode())
                        .child(preferencias.getidComanda())
                        .child(preferencias.getIdentificador())
                        .child(pedido.getIdPedido())
                        .child("situacaoPedido");

                firebase.setValue(2);


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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ComandaActivity.this, EstabelecimentoMainActivity.class);
        startActivity(intent);
        finish();
    }
}
