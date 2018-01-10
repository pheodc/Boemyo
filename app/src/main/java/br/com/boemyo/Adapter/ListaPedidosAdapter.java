package br.com.boemyo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class ListaPedidosAdapter extends ArrayAdapter<Pedido> {

    private  ArrayList<Pedido> pedidos;
    private Context context;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private Preferencias preferencias;
    private Date hora = Calendar.getInstance().getTime();

    public ListaPedidosAdapter(@NonNull Context c, @NonNull ArrayList<Pedido> objects) {
        super(c, 0, objects);
        this.pedidos = objects;
        this.context = c;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (pedidos != null) {
            preferencias = new Preferencias(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_pedidos, parent, false);

            final TextView tvProduto = (TextView) view.findViewById(R.id.tv_nome_pedido_lista);
            final TextView tvSituacao = (TextView) view.findViewById(R.id.tv_situacao_pedido_lista);
            final TextView tvValor = (TextView) view.findViewById(R.id.tv_valor_pedido_lista);
            final ImageView ivSituacao= (ImageView) view.findViewById(R.id.iv_situacao_pedido_lista);

            final Pedido pedido = pedidos.get(position);


            //Recuperar Firebase

            firebase = FirebaseInstance.getFirebase()
                    .child("cardapio")
                        .child(preferencias.getcodQRcode())
                            .child(pedido.getIdProduto());

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Produto produto = dataSnapshot.getValue(Produto.class);
                    //Log.i("LOG_NOMECAT", produto.getNomeProduto());
                    tvProduto.setText(pedido.getQtdeProduto() + " x " + produto.getNomeProduto());
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    tvValor.setText(format.format(pedido.getValorPedido()));
                    SimpleDateFormat dataPedidoFormat = new SimpleDateFormat("HH:mm:ss");
                    DateFormat df = DateFormat.getInstance();
                    String horaAtualEdit = dataPedidoFormat.format(hora);



                    situacaoPedido(ivSituacao, tvSituacao, pedido.getSituacaoPedido());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            firebase.addValueEventListener(valueEventListener);
        }

        return view;
    }

    public void situacaoPedido(ImageView ivSituacao, TextView tvSituacao, int situacao){

        if(situacao == 0){
            tvSituacao.setText(R.string.situacao_andamento);
            ivSituacao.setImageResource(R.drawable.checkbox_pedido_andamento);

        }else if(situacao == 1){
           /* Date hora = Calendar.getInstance().getTime();
            SimpleDateFormat dataPedidoFormat = new SimpleDateFormat("HH:mm:ss");
            String horaPedido = dataPedidoFormat.format(hora);*/

            tvSituacao.setText(R.string.situacao_entregue);
            ivSituacao.setImageResource(R.drawable.checkbox_pedido_confirmado);
        }else{
            tvSituacao.setText(R.string.situacao_cancelado);
            ivSituacao.setImageResource(R.drawable.checkbox_pedido_cancelado);
        }


    }

}
