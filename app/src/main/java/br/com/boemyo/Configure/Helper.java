package br.com.boemyo.Configure;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Phelipe Oberst on 08/01/2018.
 */

public class Helper {

    public static Long getDiferencaData(int horaPedido, int minPedido, int horaAtual, int minAtual) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Calendar.HOUR,horaPedido);
        c1.set(Calendar.MINUTE,minPedido);

        c2.set(Calendar.HOUR,horaAtual);
        c2.set(Calendar.MINUTE,minAtual);

        //diferenca em minutos
        long diffMinutos = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (60 * 1000);


        return diffMinutos;
    }



}
