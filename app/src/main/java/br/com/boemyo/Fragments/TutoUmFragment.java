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
public class TutoUmFragment extends Fragment {

    private TextView tvProximoTutoUm;
    private TextView tvPularTutoUm;

    public TutoUmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuto_um, container, false);

        tvProximoTutoUm = (TextView) view.findViewById(R.id.tv_proximo_tuto_um);
        tvPularTutoUm= (TextView) view.findViewById(R.id.tv_pular_tuto_um);

        tvPularTutoUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }
        });

        tvProximoTutoUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TutoDoisFragment tutoDoisFragment = new TutoDoisFragment();
                fragmentTransaction.add(R.id.rl_tutorial, tutoDoisFragment);
                fragmentTransaction.commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(TutoUmFragment.this).commit();
            }
        });

        return view;
    }

}
