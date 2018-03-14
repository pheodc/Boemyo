package br.com.boemyo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.boemyo.Activitys.PerfilEstabelecimentoActivity;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class ListaLocaisAdapter extends RecyclerView.Adapter<ListaLocaisAdapter.ViewHolderListaLocais> {

    private  ArrayList<Estabelecimento> estabelecimentos;
    private Context context;
    private LayoutInflater liListaLocais;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    public ListaLocaisAdapter(Context c, ArrayList<Estabelecimento> estabelecimentos){
        this.context = c;
        this.estabelecimentos = estabelecimentos;
        this.liListaLocais = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderListaLocais onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = liListaLocais.inflate(R.layout.lista_locais, viewGroup, false);
        ListaLocaisAdapter.ViewHolderListaLocais vhListaLocais = new ListaLocaisAdapter.ViewHolderListaLocais(v);

        return vhListaLocais;
    }

    @Override
    public void onBindViewHolder(ViewHolderListaLocais holder, int position) {
        PicassoClient.downloadImage(context, estabelecimentos.get(position).getLogoEstabelecimento(), holder.logoLocal);
        holder.nomeLocal.setText(estabelecimentos.get(position).getNomeEstabelecimento());
        holder.tipoLocal.setText(estabelecimentos.get(position).getTipoEstabelecimento());
    }

    @Override
    public int getItemCount() {return estabelecimentos.size();}

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    public class ViewHolderListaLocais extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CircleImageView logoLocal;
        public TextView nomeLocal;
        public TextView tipoLocal;

        public ViewHolderListaLocais(View itemView) {
            super(itemView);

            logoLocal = (CircleImageView) itemView.findViewById(R.id.iv_logo);
            nomeLocal = (TextView) itemView.findViewById(R.id.tv_nome_local_lista);
            tipoLocal = (TextView) itemView.findViewById(R.id.tvtipo_estabelecimento_lista);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Estabelecimento estabelecimento = estabelecimentos.get(getAdapterPosition());
            Intent intent = new Intent(context, PerfilEstabelecimentoActivity.class);
            intent.putExtra("estabelecimento", estabelecimento);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

    /*public ListaLocaisAdapter(@NonNull Context c,  @NonNull ArrayList<Estabelecimento> objects) {
        super(c, 0, objects);
        this.estabelecimentos = objects;
        this.context = c;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if( estabelecimentos != null ){




            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_locais, parent, false);

            CircleImageView logoLocal = (CircleImageView) view.findViewById(R.id.iv_logo);
            TextView nomeLocal = (TextView) view.findViewById(R.id.tv_nome_local_lista);
            TextView tipoLocal = (TextView) view.findViewById(R.id.tvtipo_estabelecimento_lista);

           Estabelecimento estabelecimento = estabelecimentos.get( position );

           PicassoClient.downloadImage(context, estabelecimento.getLogoEstabelecimento(), logoLocal);
           nomeLocal.setText(estabelecimento.getNomeEstabelecimento());
           tipoLocal.setText(estabelecimento.getTipoEstabelecimento());

        }

        return view;

    }*/
}
