package br.com.boemyo.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class ListaProdutosAdapter extends ArrayAdapter<Produto> {

    private  ArrayList<Produto> produtos;
    private Context context;

    public ListaProdutosAdapter(@NonNull Context c, @NonNull ArrayList<Produto> objects) {
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

    }
}
