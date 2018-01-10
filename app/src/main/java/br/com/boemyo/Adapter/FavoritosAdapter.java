package br.com.boemyo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Model.Favorito;
import br.com.boemyo.R;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolderFavoritos> {
    private ArrayList<Favorito> favoritos;
    private LayoutInflater liFavoritos;
    private Context context;
    private DatabaseReference firebase;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private Preferencias preferencias;
    public FavoritosAdapter(Context c, ArrayList<Favorito> favoritos) {
        this.favoritos = favoritos;
        this.liFavoritos = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = c;
    }

    @Override
    public ViewHolderFavoritos onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = liFavoritos.inflate(R.layout.card_favoritos, viewGroup, false);
        ViewHolderFavoritos vhFavoritos = new ViewHolderFavoritos(v);
        return vhFavoritos;
    }

    @Override
    public void onBindViewHolder(ViewHolderFavoritos holder, final int position) {
        PicassoClient.downloadImage( context, favoritos.get(position).getImgFundoFavorito(), holder.ivBackFavorito);
        holder.tvNomeLocalFavorito.setText(favoritos.get(position).getNomeFavorito());

    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    public void removeItemFavorito(int position){
        preferencias = new Preferencias(context);
        firebase = FirebaseInstance.getFirebase()
                .child("favorito")
                .child(preferencias.getIdentificador())
                .child(favoritos.get(position).getIdQrCode());

        firebase.removeValue();
    }

    public class ViewHolderFavoritos extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivBackFavorito;
        public ImageView ivIconFavorito;
        public TextView tvNomeLocalFavorito;


        public ViewHolderFavoritos(View itemView) {
            super(itemView);

            ivBackFavorito = (ImageView) itemView.findViewById(R.id.iv_back_card_favorito);
            ivIconFavorito = (ImageView) itemView.findViewById(R.id.iv_icon_favorito);
            tvNomeLocalFavorito = (TextView) itemView.findViewById(R.id.tv_nome_favorito);

            ivIconFavorito.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(view, getPosition());

                removeItemFavorito(getPosition());
            }
        }
    }


}
