package br.com.boemyo.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

import br.com.boemyo.Activitys.ComandaActivity;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Helper;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Comanda;
import br.com.boemyo.Model.Pedido;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class ListaPedidosAdapter extends RecyclerView.Adapter<ListaPedidosAdapter.ViewHolderPedidos> {

    private  ArrayList<String> pedidos;
    private Context context;
    private DatabaseReference firebase;
    private Preferencias preferencias;
    private LayoutInflater liPedidos;
    //private Date hora = Calendar.getInstance().getTime();
    private NumberFormat format = NumberFormat.getCurrencyInstance();
    public ListaPedidosAdapter(Context c, ArrayList<String> pedidos){
        this.context = c;
        this.pedidos = pedidos;
        this.liPedidos = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderPedidos onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = liPedidos.inflate(R.layout.lista_pedidos, viewGroup, false);
        ListaPedidosAdapter.ViewHolderPedidos vhPedidos = new ListaPedidosAdapter.ViewHolderPedidos(v);

        return vhPedidos;
    }

    @Override
    public void onBindViewHolder(final ViewHolderPedidos holder, final int position) {
        preferencias = new Preferencias(context);
        Log.i("LOG_TAMANHO", String.valueOf(pedidos.size()));
        //Recuperar Firebase

        firebase = FirebaseInstance.getFirebase();
        firebase.child("pedido")
                    .child(pedidos.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Pedido pedido = dataSnapshot.getValue(Pedido.class);

                situacaoPedido(holder.ivSituacao,holder.tvSituacao,pedido.getSituacaoPedido());
                holder.tvValor.setText(format.format(pedido.getValorPedido()));
                firebase.child("produto").child(preferencias.getIdEstabelecimento()).child(pedido.getProduto()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            Produto produto = dataSnapshot.getValue(Produto.class);
                            Log.i("LOG_PRODUTO", produto.getNomeProduto());
                            holder.tvProduto.setText(pedido.getQtdeProduto() + " x " + produto.getNomeProduto());



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        YoYo.with(Techniques.FadeInLeft)
                .duration(700)
                .playOn(holder.itemView);

        /*valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Produto produto = dataSnapshot.getValue(Produto.class);
                //Log.i("LOG_NOMECAT", produto.getNomeProduto());
                holder.tvProduto.setText(pedidos.get(position).getQtdeProduto() + " x " + produto.getNomeProduto());
                NumberFormat format = NumberFormat.getCurrencyInstance();
                holder.tvValor.setText(format.format(pedidos.get(position).getValorPedido()));
                SimpleDateFormat dataPedidoFormat = new SimpleDateFormat("HH:mm:ss");
                DateFormat df = DateFormat.getInstance();
                String horaAtualEdit = dataPedidoFormat.format(hora);



                situacaoPedido(holder.ivSituacao, holder.tvSituacao, pedidos.get(position).getSituacaoPedido());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(valueEventListener);*/
    }

    @Override
    public int getItemCount() {return pedidos.size();}


    public class ViewHolderPedidos extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvProduto;
        TextView tvSituacao;
        TextView tvValor;
        ImageView ivSituacao;
        public RelativeLayout viewBackgroundPedido, viewForegroundPedido;

        public ViewHolderPedidos(View itemView) {
            super(itemView);

            tvProduto = (TextView) itemView.findViewById(R.id.tv_nome_pedido_lista);
            tvSituacao = (TextView) itemView.findViewById(R.id.tv_situacao_pedido_lista);
            tvValor = (TextView) itemView.findViewById(R.id.tv_valor_pedido_lista);
            ivSituacao= (ImageView) itemView.findViewById(R.id.iv_situacao_pedido_lista);
            viewBackgroundPedido = itemView.findViewById(R.id.view_background_pedido);
            viewForegroundPedido = itemView.findViewById(R.id.view_foreground_pedido);


        }

        @Override
        public void onClick(View view) {

        }
    }



    public void situacaoPedido(ImageView ivSituacao, TextView tvSituacao, int situacao){

        if(situacao == 0 || situacao == 1){
            tvSituacao.setText(R.string.situacao_andamento);
            ivSituacao.setImageResource(R.drawable.checkbox_pedido_andamento);

        }else if(situacao == 2){
           /*Date hora = Calendar.getInstance().getTime();
            SimpleDateFormat dataPedidoFormat = new SimpleDateFormat("HH:mm:ss");
            String horaPedido = dataPedidoFormat.format(hora);*/

            tvSituacao.setText(R.string.situacao_entregue);
            ivSituacao.setImageResource(R.drawable.checkbox_pedido_confirmado);
        }else{
            tvSituacao.setText(R.string.situacao_cancelado);
            ivSituacao.setImageResource(R.drawable.checkbox_pedido_cancelado);
        }


    }

    /*public void cancelaPedido(int position){

        ComandaActivity comanda = new ComandaActivity();

        Date horarioAtual =  Calendar.getInstance().getTime();
        SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");
        String horaFormatada = dataFormat.format(horarioAtual);
        int horaPedido = Integer.parseInt(pedidos.get(position).getHoraPedido().substring(0,2));
        int minPedido = Integer.parseInt(pedidos.get(position).getHoraPedido().substring(3,5));
        int horaAtual = Integer.parseInt(horaFormatada.substring(0,2));
        int minAtual = Integer.parseInt(horaFormatada.substring(3,5));
        Long diferenca = Helper.getDiferencaData(horaPedido, minPedido, horaAtual, minAtual);

        Log.i("LOG_HORA", String.valueOf(diferenca));
        if(pedidos.get(position).getSituacaoPedido() == 1) {
            Toast.makeText(context.getApplicationContext(), R.string.cancelamento_pedido_entregue, Toast.LENGTH_LONG).show();
        }else if(pedidos.get(position).getSituacaoPedido() == 2){
            Toast.makeText(context.getApplicationContext(), R.string.cancelamento_realizado, Toast.LENGTH_LONG).show();
        }else {
            Log.i("LOG_CANCEL", String.valueOf(pedidos.get(position)));
            notifyItemChanged(position);
            //alertaCancelamento(pedidos.get(position), diferenca);
        }
    }*/

    /*public ListaPedidosAdapter(@NonNull Context c, @NonNull ArrayList<Pedido> objects) {
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
    }*/

}
