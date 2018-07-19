package br.com.boemyo.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 07/11/2017.
 */

public class ListaPagamentoAdapter extends RecyclerView.Adapter<ListaPagamentoAdapter.ViewHolderPagamento> {

    private  ArrayList<Pagamento> pagamentos;
    private LayoutInflater liPagamentos;
    private Context context;
    private Preferencias preferencias;
    private DatabaseReference firebase;

    public ListaPagamentoAdapter(Context c, ArrayList<Pagamento> pagamentos){

        this.context = c;
        this.pagamentos = pagamentos;
        this.liPagamentos = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderPagamento onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = liPagamentos.inflate(R.layout.lista_pagamento, viewGroup, false);
        ListaPagamentoAdapter.ViewHolderPagamento vhPagamentos = new ListaPagamentoAdapter.ViewHolderPagamento(v);

        return vhPagamentos;
    }

    @Override
    public void onBindViewHolder(ViewHolderPagamento holder, int position) {
        String dataVencimento = pagamentos.get(position).getDataValidaMes() + "/" + pagamentos.get(position).getDataValidaAno();
        String numCartaoEdit = pagamentos.get(position).getNumCartao().substring(pagamentos.get(position).getNumCartao().length() -4);
        Log.i("LOG_SUBSTRING", numCartaoEdit);
        validaBandeira(pagamentos.get(position), holder.ctSuportados);
        holder.tvNumCartao.setText("●●●● " + numCartaoEdit);
        holder.tvDtVencimento.setText(dataVencimento);
    }

    @Override
    public int getItemCount() {return pagamentos.size();}

    public class ViewHolderPagamento extends RecyclerView.ViewHolder implements View.OnClickListener{

        public SupportedCardTypesView ctSuportados;
        public TextView tvNumCartao;
        public TextView tvDtVencimento;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolderPagamento(View itemView) {
            super(itemView);

            ctSuportados = (SupportedCardTypesView) itemView.findViewById(R.id.card_type_bandeira);
            tvNumCartao = (TextView) itemView.findViewById(R.id.tv_num_cartao);
            tvDtVencimento = (TextView) itemView.findViewById(R.id.tv_dt_vencimento);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        @Override
        public void onClick(View view) {

        }
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

    public void deleteCartao(int position){
        preferencias = new Preferencias(context);
        firebase = FirebaseInstance.getFirebase()
                .child("Pagamento")
                    .child(preferencias.getIdentificador())
                        .child(pagamentos.get(position).getIdPagamento());

        firebase.removeValue();
            notifyItemRemoved(position);

    }
    /*public ListaPagamentoAdapter(@NonNull Context c, @NonNull ArrayList<Pagamento> objects) {
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

    */
}
