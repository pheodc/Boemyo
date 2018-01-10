package br.com.boemyo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.boemyo.Model.CategoriaCardapio;
import br.com.boemyo.Model.Estabelecimento;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 07/11/2017.
 */

public class ListaCategoriaAdapter extends ArrayAdapter<CategoriaCardapio> {

    private  ArrayList<CategoriaCardapio> categoriaCardapios;
    private Context context;

    public ListaCategoriaAdapter(@NonNull Context c, @NonNull ArrayList<CategoriaCardapio> objects) {
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
    }
}
