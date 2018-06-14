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
public class TutoCincoFragment extends Fragment {

    private TextView tvPularTutoCinco;
    private TextView tvProximoTutoCinco;

    public TutoCincoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tuto_cinco, container, false);

        tvProximoTutoCinco = (TextView) view.findViewById(R.id.tv_proximo_tuto_cinco);
        tvPularTutoCinco = (TextView) view.findViewById(R.id.tv_pular_tuto_cinco);

        tvPularTutoCinco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }
        });

        tvProximoTutoCinco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TutoSeisFragment tutoSeisFragment= new TutoSeisFragment();
                fragmentTransaction.add(R.id.rl_tutorial, tutoSeisFragment);
                fragmentTransaction.commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(TutoCincoFragment.this).commit();
            }
        });


        return view;
    }

}
