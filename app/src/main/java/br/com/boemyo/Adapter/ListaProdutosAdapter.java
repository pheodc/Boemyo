package br.com.boemyo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.NumberFormat;
import java.util.ArrayList;

import br.com.boemyo.Activitys.DetalhesProdutosActivity;
import br.com.boemyo.Activitys.ProdutoCardapioActivity;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class ListaProdutosAdapter extends RecyclerView.Adapter<ListaProdutosAdapter.ViewHolderProdutos>{

    private  ArrayList<Produto> produtos;
    private Context context;
    private LayoutInflater liProdutos;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ListaProdutosAdapter(Context c, ArrayList<Produto> produtos){

        this.context = c;
        this.produtos = produtos;
        this.liProdutos = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderProdutos onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = liProdutos.inflate(R.layout.lista_categoria_produtos, viewGroup, false);
        ListaProdutosAdapter.ViewHolderProdutos vhProdutos = new ListaProdutosAdapter.ViewHolderProdutos(v);

        return vhProdutos;
    }

    @Override
    public void onBindViewHolder(ViewHolderProdutos holder, int position) {
        if(produtos.get(position).getUrlImagemProduto() != null){
            PicassoClient.downloadImage(context, produtos.get(position).getUrlImagemProduto(), holder.ivImgProduto);
        }
        holder.tvNomeProduto.setText(produtos.get(position).getNomeProduto());
        holder.tvDescProduto.setText(produtos.get(position).getDescProduto());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        holder.tvValorProduto.setText(format.format(produtos.get(position).getValorProduto()));

        YoYo.with(Techniques.FadeInLeft)
                .duration(700)
                .playOn(holder.itemView);
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    public class ViewHolderProdutos extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView ivImgProduto;
        TextView tvNomeProduto;
        TextView tvDescProduto;
        TextView tvValorProduto;

        public ViewHolderProdutos(View itemView) {
            super(itemView);

            ivImgProduto = (CircleImageView) itemView.findViewById(R.id.iv_img_produto);
            tvNomeProduto = (TextView) itemView.findViewById(R.id.tv_nome_produto_lista);
            tvDescProduto = (TextView) itemView.findViewById(R.id.tv_des_produto_lista);
            tvValorProduto = (TextView) itemView.findViewById(R.id.tv_valor_lista);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Produto produto = produtos.get( getAdapterPosition() );

            Intent intent = new Intent(context.getApplicationContext(), DetalhesProdutosActivity.class);
            intent.putExtra("produto", produto);
            context.startActivity(intent);
        }
    }

    /*public ListaProdutosAdapter(@NonNull Context c, @NonNull ArrayList<Produto> objects) {
        super(c, 0, objects);
        this.produtos = objects;
        this.context = c;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if( produtos != null ){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_categoria_produtos, parent, false);

            CircleImageView ivImgProduto = (CircleImageView) view.findViewById(R.id.iv_img_produto);
            TextView tvNomeProduto = (TextView) view.findViewById(R.id.tv_nome_produto_lista);
            TextView tvDescProduto = (TextView) view.findViewById(R.id.tv_des_produto_lista);
            TextView tvValorProduto = (TextView) view.findViewById(R.id.tv_valor_lista);

           Produto produto = produtos.get( position );

           if(produto.getUrlImagemProduto() != null){
                   PicassoClient.downloadImage(context, produto.getUrlImagemProduto(), ivImgProduto);
           }
           tvNomeProduto.setText(produto.getNomeProduto());
           tvDescProduto.setText(produto.getDescProduto());
            NumberFormat format = NumberFormat.getCurrencyInstance();
           tvValorProduto.setText(format.format(produto.getValorProduto()));

        }

        return view;

    }*/
}
