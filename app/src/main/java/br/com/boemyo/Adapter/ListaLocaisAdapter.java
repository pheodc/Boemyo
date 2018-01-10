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

import java.util.ArrayList;

import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class ListaLocaisAdapter extends ArrayAdapter<Estabelecimento> {

    private  ArrayList<Estabelecimento> estabelecimentos;
    private Context context;
    private final String URLSTORAGE = "gs://boemyo-8e4ef.appspot.com/";
    private final String PATHESTABS = "Imagens_Estabelecimentos/";

    public ListaLocaisAdapter(@NonNull Context c,  @NonNull ArrayList<Estabelecimento> objects) {
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

    }
}
