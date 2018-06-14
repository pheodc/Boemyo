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
public class TutoQuatroFragment extends Fragment {

    private TextView tvPularTutoQuatro;
    private TextView tvProximoTutoQuatro;


    public TutoQuatroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tuto_quatro, container, false);

        tvProximoTutoQuatro = (TextView) view.findViewById(R.id.tv_proximo_tuto_quatro);
        tvPularTutoQuatro = (TextView) view.findViewById(R.id.tv_pular_tuto_quatro);

        tvPularTutoQuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }
        });

        tvProximoTutoQuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TutoCincoFragment tutoCincoFragment= new TutoCincoFragment();
                fragmentTransaction.add(R.id.rl_tutorial, tutoCincoFragment);
                fragmentTransaction.commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(TutoQuatroFragment.this).commit();
            }
        });

        return view;
    }

}
