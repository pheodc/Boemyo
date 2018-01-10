package br.com.boemyo.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.SupportedCardTypesView;

import java.util.ArrayList;

import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 07/11/2017.
 */

public class ListaPagamentoAdapter extends ArrayAdapter<Pagamento> {

    private  ArrayList<Pagamento> pagamentos;
    private Context context;

    public ListaPagamentoAdapter(@NonNull Context c, @NonNull ArrayList<Pagamento> objects) {
        super(c, 0, objects);
        this.pagamentos = objects;
        this.context = c;

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(pagamentos != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_pagamento, parent, false);
            SupportedCardTypesView ctSuportados = (SupportedCardTypesView) view.findViewById(R.id.card_type_bandeira);
            TextView tvNumCartao = (TextView) view.findViewById(R.id.tv_num_cartao);
            TextView tvDtVencimento = (TextView) view.findViewById(R.id.tv_dt_vencimento);

            Pagamento pagamento = pagamentos.get(position);
            String dataVencimento = pagamento.getDataValidaMes() + "/" + pagamento.getDataValidaAno();
            String numCartaoEdit = pagamento.getNumCartao().substring(pagamento.getNumCartao().length() -4);
            Log.i("LOG_SUBSTRING", numCartaoEdit);
            validaBandeira(pagamento, ctSuportados);
            tvNumCartao.setText("●●●● " + numCartaoEdit);
            tvDtVencimento.setText(dataVencimento);


        }

        return view;
    }

    public void validaBandeira(Pagamento pagamento, SupportedCardTypesView cardTypesView){
        Log.i("LOG_TIPO_CARD", pagamento.getBandeira());
        if(pagamento.getBandeira().equals("VISA") ){
            cardTypesView.setSupportedCardTypes(CardType.VISA);
        }else if(pagamento.getBandeira().equals("MASTERCARD") ){
            cardTypesView.setSupportedCardTypes(CardType.MASTERCARD);
        }else if(pagamento.getBandeira().equals("AMEX")){
            cardTypesView.setSupportedCardTypes(CardType.AMEX);
        }else if(pagamento.getBandeira().equals("DINERS_CLUB")){
            cardTypesView.setSupportedCardTypes(CardType.DINERS_CLUB);
        }else{
            cardTypesView.setSupportedCardTypes(CardType.UNKNOWN);
        }
    }
}
