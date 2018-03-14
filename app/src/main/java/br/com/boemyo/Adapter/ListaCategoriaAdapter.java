package br.com.boemyo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.boemyo.Activitys.CategoriaCardapioActivity;
import br.com.boemyo.Activitys.ProdutoCardapioActivity;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 07/11/2017.
 */

public class ListaCategoriaAdapter extends RecyclerView.Adapter<ListaCategoriaAdapter.ViewHolderCategoria> {

    private  ArrayList<CategoriaCardapio> categoriaCardapios;
    private Context context;
    private DatabaseReference firebase;
    private LayoutInflater liCategoria;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private Preferencias preferencias;

    public ListaCategoriaAdapter(Context c, ArrayList<CategoriaCardapio> categoriaCardapios){
        this.context = c;
        this.categoriaCardapios = categoriaCardapios;
        this.liCategoria = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public ViewHolderCategoria onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = liCategoria.inflate(R.layout.lista_categoria_cardapio, viewGroup, false);
        ListaCategoriaAdapter.ViewHolderCategoria vhCategoria = new ListaCategoriaAdapter.ViewHolderCategoria(v);
        return vhCategoria;
    }

    @Override
    public void onBindViewHolder(final ViewHolderCategoria holder, int position) {
        preferencias = new Preferencias(context.getApplicationContext());

        firebase = FirebaseInstance.getFirebase();

        firebase.child("categoria")
                .child(preferencias.getIdEstabelecimento())
                    .child(categoriaCardapios.get(position).getIdCategoria())
                       .child("cardapio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey() != null){
                    for(DataSnapshot dados : dataSnapshot.getChildren()){
                        Log.i("LOG_CATDATA", dados.getKey());
                        firebase.child("cardapio").child(preferencias.getIdEstabelecimento()).child(dados.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Produto produto = dataSnapshot.getValue(Produto.class);
                                Log.i("LOG_CATValue", produto.getUrlImagemProduto());
                                if(produto.getUrlImagemProduto().isEmpty()){

                                    holder.ivImgProdutoCaetgoria.setImageResource(R.drawable.boemyo_marker);

                                }else {
                                    PicassoClient.downloadImage(context, produto.getUrlImagemProduto(), holder.ivImgProdutoCaetgoria);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Log.i("LOG_CATARRAY", categoriaCardapios.get(position).getIdCategoria());
        holder.tvNomeCategoria.setText(categoriaCardapios.get(position).getNomeCategoria());
        YoYo.with(Techniques.BounceInDown)
                .duration(700)
                .playOn(holder.itemView);
    }

    @Override
    public int getItemCount() {return categoriaCardapios.size();}

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    public class ViewHolderCategoria extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvNomeCategoria;
        ImageView ivImgProdutoCaetgoria;


        public ViewHolderCategoria(View itemView) {
            super(itemView);

            tvNomeCategoria = (TextView) itemView.findViewById(R.id.tv_nome_categoria_lista);
            ivImgProdutoCaetgoria = (ImageView) itemView.findViewById(R.id.iv_img_produto_categoria);
            context = itemView.getContext();
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            CategoriaCardapio categoriaCardapio = categoriaCardapios.get(getAdapterPosition());

            Bundle bundle = new Bundle();
            Intent intent = new Intent(context.getApplicationContext(), ProdutoCardapioActivity.class);

            bundle.putString("CHAVE_BUNDLE_CATEGORIA", categoriaCardapio.getIdCategoria());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    /*public ListaCategoriaAdapter(@NonNull Context c, @NonNull ArrayList<CategoriaCardapio> objects) {
        super(c, 0, objects);
        this.categoriaCardapios = objects;
        this.context = c;

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(categoriaCardapios != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_categoria_cardapio, parent, false);

            TextView tvNomeCategoria = (TextView) view.findViewById(R.id.tv_nome_categoria_lista);

            CategoriaCardapio categoriaCardapio = categoriaCardapios.get(position);

            tvNomeCategoria.setText(categoriaCardapio.getNomeCategoria());


        }

        return view;
    }*/
}
