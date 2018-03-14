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
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.Model.Favorito;
import br.com.boemyo.R;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolderFavoritos> {
    private ArrayList<Estabelecimento> estabelecimentos;
    private LayoutInflater liFavoritos;
    private Context context;
    private DatabaseReference firebase;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private Preferencias preferencias;

    public FavoritosAdapter(Context c, ArrayList<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
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

        PicassoClient.downloadImage( context, estabelecimentos.get(position).getPerfilEstabelecimento(), holder.ivBackFavorito);
        holder.tvNomeLocalFavorito.setText(estabelecimentos.get(position).getNomeEstabelecimento());

    }

    @Override
    public int getItemCount() {
        return estabelecimentos.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    public void removeItemFavorito(int position){
        preferencias = new Preferencias(context);
        firebase = FirebaseInstance.getFirebase()
                .child("usuario")
                    .child(preferencias.getIdentificador())
                        .child("favorito")
                            .child(estabelecimentos.get(position).getIdEstabelecimento());

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
                recyclerViewOnClickListenerHack.onClickListener(null,view, getAdapterPosition());

                removeItemFavorito(getAdapterPosition());
            }
        }
    }


}
