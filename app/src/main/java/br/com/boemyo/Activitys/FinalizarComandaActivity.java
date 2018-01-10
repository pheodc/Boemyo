package br.com.boemyo.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;

import br.com.boemyo.Adapter.ListaPagamentoAdapter;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.R;

public class FinalizarComandaActivity extends AppCompatActivity {

    private ListView lvFinalizaComanda;
    private ArrayAdapter adapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private ArrayList<Pagamento> arrayFinalizaComanda;
    private Preferencias preferencias;
    private Toolbar tbFizalizaComanda;
    private Comanda comanda;

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
        setContentView(R.layout.activity_finalizar_comanda);

        preferencias = new Preferencias(FinalizarComandaActivity.this);

        tbFizalizaComanda = (Toolbar) findViewById(R.id.tb_finaliza_comanda);
        tbFizalizaComanda.setTitle(R.string.title_finalizar_comanda);
        setSupportActionBar(tbFizalizaComanda);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbFizalizaComanda.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lvFinalizaComanda = (ListView) findViewById(R.id.lv_finaliza_comanda);

        arrayFinalizaComanda = new ArrayList<>();
        adapter = new ListaPagamentoAdapter(this, arrayFinalizaComanda);
        lvFinalizaComanda.setAdapter(adapter);

        lvFinalizaComanda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Pagamento pagamento = (Pagamento) parent.getItemAtPosition(position);
                alertaFinalizarCartao(pagamento);
            }
        });

        firebase = FirebaseInstance.getFirebase()
                .child("Pagamento")
                .child(preferencias.getIdentificador());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayFinalizaComanda.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Pagamento pagamento = dados.getValue(Pagamento.class);
                    Log.i("LOG_NOMECAT", pagamento.getNumCartao());
                    arrayFinalizaComanda.add(pagamento);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adiciona_cartao_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_adiciona_cartao) {

            Intent intent = new Intent(FinalizarComandaActivity.this, AdicionaNovoCartaoActivity.class);
            startActivity(intent);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void alertaFinalizarCartao(Pagamento pagamento){
        final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(R.string.dialog_finalizar_cartao_title);
        View viewCustomDialog = getLayoutInflater().inflate(R.layout.custom_dados_cartao_finaliza, null);
        TextView tvNumCartaoFinaliza = (TextView) viewCustomDialog.findViewById(R.id.tv_num_cartao_dialog_finaliza);
        TextView tvBandeiraCartaoFinaliza = (TextView) viewCustomDialog.findViewById(R.id.tv_bandeira_cartao_dialog_finaliza);
        String numCartaoEdit = pagamento.getNumCartao().substring(pagamento.getNumCartao().length() -4);
        String bandeiraEdit = pagamento.getBandeira().toLowerCase();
        tvNumCartaoFinaliza.setText("Cartão: ●●●● " + numCartaoEdit);
        tvBandeiraCartaoFinaliza.setText("Bandeira: " + bandeiraEdit);
        builder.setView(viewCustomDialog);
        builder.setPositiveButton(R.string.bt_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                comanda = new Comanda();

                comanda.setIdQrCode(preferencias.getcodQRcode());
                comanda.setIdComanda(preferencias.getidComanda());
                comanda.setNumMesa(preferencias.getNumMesa());
                comanda.setSubTotal(preferencias.getSubTotal());
                comanda.setSituacaoComanda("1");
                comanda.salvarFirebase();

                preferencias.removerPreferencias();
                Intent intent = new Intent(FinalizarComandaActivity.this, HomeActivity.class);
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

    }
}
