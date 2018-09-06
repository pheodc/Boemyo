package br.com.boemyo.Activitys;

import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.OnCardFormValidListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.boemyo.Configure.Base64Custom;
import br.com.boemyo.Configure.ConnectivityChangeReceiver;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.Model.Usuario;
import br.com.boemyo.R;

public class AdicionaNovoCartaoActivity extends AppCompatActivity implements OnCardFormSubmitListener,
        CardEditText.OnCardTypeChangedListener, ConnectivityChangeReceiver.OnConnectivityChangedListener{

    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private static final CardType[] CARD_TYPES = { CardType.VISA, CardType.MASTERCARD,
            CardType.AMEX, CardType.DINERS_CLUB,  };
    private Toolbar tbAddCartao;
    private CardForm cfAdicionaCartao;
    private SupportedCardTypesView ctSuportados;
    private Button btAddCartao;
    private Button bttipoCredito;
    private Button bttipoDebito;
    private Preferencias preferencias;
    private Date hora = Calendar.getInstance().getTime();
    private String bandeiraCartao;
    private RelativeLayout conexao;
    private String tipoPagamento;
    private DatabaseReference databaseReference = FirebaseInstance.getFirebase();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_novo_cartao);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);

        preferencias = new Preferencias(AdicionaNovoCartaoActivity.this);
        tbAddCartao = (Toolbar) findViewById(R.id.tb_adiciona_cartao);
        tbAddCartao.setTitle(R.string.title_adiciona_cartao);
        setSupportActionBar(tbAddCartao);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbAddCartao.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        conexao = (RelativeLayout) findViewById(R.id.conexao_adiciona_cartao);
        ctSuportados = (SupportedCardTypesView) findViewById(R.id.card_types);
        ctSuportados.setSupportedCardTypes(CARD_TYPES);
        cfAdicionaCartao = (CardForm) findViewById(R.id.cf_novo_card);
        cfAdicionaCartao.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel("Purchase")
                .setup(this);
        cfAdicionaCartao.setOnCardTypeChangedListener(this);
        btAddCartao = (Button) findViewById(R.id.bt_adiciona_cartao);
        bttipoCredito = (Button) findViewById(R.id.bt_tipo_credito);
        bttipoDebito = (Button) findViewById(R.id.bt_tipo_debito);
        btAddCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardFormSubmit();
            }
        });

        selecionaTipoPagamento();

    }

    @Override
    public void onCardFormSubmit() {
        if (cfAdicionaCartao.isValid()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            Log.i("LOG_BANDEIRA", bandeiraCartao);
            String horaFormatada = dateFormat.format(hora);
            String idPagamento = databaseReference.child("Pagamento").push().getKey();
            Toast.makeText(this, R.string.valida_sucesso, Toast.LENGTH_SHORT).show();
            Pagamento pagamento = new Pagamento();
            pagamento.setIdUsuario(preferencias.getIdentificador());
            pagamento.setIdPagamento( idPagamento );
            pagamento.setNomeUsuario(preferencias.getNome());
            pagamento.setNumCartao(cfAdicionaCartao.getCardNumber());
            pagamento.setDataValidaMes(cfAdicionaCartao.getExpirationMonth());
            pagamento.setDataValidaAno(cfAdicionaCartao.getExpirationYear());
            pagamento.setCvv(cfAdicionaCartao.getCvv());
            if(tipoPagamento == null){
                pagamento.setTipoPagamento("CreditCard");
            }else{
                pagamento.setTipoPagamento(tipoPagamento);
            }
            if(bandeiraCartao.equals("MASTERCARD")){
                bandeiraCartao = "MASTER";
                pagamento.setBandeira(bandeiraCartao);
            }else {
                pagamento.setBandeira(bandeiraCartao);
            }


            //pagamento.salvarFirebase();
            try {
                pagamento.salvarCartaoTokenizado(this);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            cfAdicionaCartao.validate();
            Toast.makeText(this, R.string.valida_cartao_invalido, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            ctSuportados.setSupportedCardTypes(CARD_TYPES);

        } else {
            ctSuportados.setSelected(cardType);
            bandeiraCartao = cardType.name();
            Log.i("LOG_CARD", cardType.name());
        }
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

    private void selecionaTipoPagamento(){
        bttipoCredito.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                bttipoCredito.setBackground(getResources().getDrawable(R.drawable.style_radius_button));
                bttipoCredito.setTextColor(getResources().getColor(R.color.colorText));
                bttipoDebito.setBackground(getResources().getDrawable(R.drawable.style_radius_button_transparent));
                bttipoDebito.setTextColor(getResources().getColor(R.color.colorAccent));
                tipoPagamento = "CreditCard";
            }
        });

        bttipoDebito.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bttipoDebito.setBackground(getResources().getDrawable(R.drawable.style_radius_button));
                bttipoDebito.setTextColor(getResources().getColor(R.color.colorText));
                bttipoCredito.setBackground(getResources().getDrawable(R.drawable.style_radius_button_transparent));
                bttipoCredito.setTextColor(getResources().getColor(R.color.colorAccent));
                tipoPagamento= "DebitCard";
            }
        });
    }
}
