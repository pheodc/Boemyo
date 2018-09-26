package br.com.boemyo.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.boemyo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutoSeisFragment extends Fragment {

    private TextView tvPularTutoSeis;
    private TextView tvConcluidoTutoSeis;


    public TutoSeisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tuto_seis, container, false);

        tvConcluidoTutoSeis = (TextView) view.findViewById(R.id.tv_concluido_tuto_seis);
        tvPularTutoSeis = (TextView) view.findViewById(R.id.tv_pular_tuto_seis);

        tvPularTutoSeis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }
        });

        tvConcluidoTutoSeis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();
            }
        });

        return view;
    }

}
