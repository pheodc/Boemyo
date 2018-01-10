package br.com.boemyo.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.boemyo.Fragments.EncontraLocaisFragment;
import br.com.boemyo.Fragments.ListaLocaisFragment;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 23/11/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] titleAbas = {"PROXIMOS A MIM", "LOCAIS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new EncontraLocaisFragment();
                break;
            case 1:
                fragment = new ListaLocaisFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return titleAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titleAbas[ position ];
    }
}
