package br.com.boemyo.Configure;

import android.util.Base64;

/**
 * Created by Phelipe Oberst on 07/10/2017.
 */

public class Base64Custom {

    public static String codificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");

    }

    public static String decodifcarBase64(String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }

}
