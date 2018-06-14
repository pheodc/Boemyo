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
public class TutoTresFragment extends Fragment {

    private TextView tvPularTutoTres;
    private TextView tvProximoTutoTres;

    public TutoTresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuto_tres, container, false);

        tvProximoTutoTres = (TextView) view.findViewById(R.id.tv_proximo_tuto_tres);
        tvPularTutoTres = (TextView) view.findViewById(R.id.tv_pular_tuto_tres);

        tvPularTutoTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }
        });

        tvProximoTutoTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TutoQuatroFragment tutoQuatroFragment = new TutoQuatroFragment();
                fragmentTransaction.add(R.id.rl_tutorial, tutoQuatroFragment);
                fragmentTransaction.commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(TutoTresFragment.this).commit();
            }
        });




        return view;
    }

}
