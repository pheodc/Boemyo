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
public class TutoDoisFragment extends Fragment {

    private TextView tvPularTutoDois;
    private TextView tvProximoTutoDois;

    public TutoDoisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuto_dois, container, false);

        tvProximoTutoDois = (TextView) view.findViewById(R.id.tv_proximo_tuto_dois);
        tvPularTutoDois = (TextView) view.findViewById(R.id.tv_pular_tuto_dois);

        tvPularTutoDois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }
        });

        tvProximoTutoDois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TutoTresFragment tutoTresFragment = new TutoTresFragment();
                fragmentTransaction.add(R.id.rl_tutorial, tutoTresFragment);
                fragmentTransaction.commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(TutoDoisFragment.this).commit();
            }
        });



        return view;
    }

}
