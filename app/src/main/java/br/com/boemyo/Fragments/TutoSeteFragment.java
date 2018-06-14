package br.com.boemyo.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.boemyo.R;


public class TutoSeteFragment extends Fragment {

    private TextView tvConcluidoTuto;

    public TutoSeteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tuto_sete, container, false);

        tvConcluidoTuto = (TextView) view.findViewById(R.id.tv_concluido_tuto_sete);

        tvConcluidoTuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();
            }
        });


        return view;
    }


}
