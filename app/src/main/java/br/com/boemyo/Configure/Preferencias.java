package br.com.boemyo.Configure;

import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.Double2;

/**
 * Created by Phelipe Oberst on 05/10/2017.
 */

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private static final String ARQUIVO_PREFERENCES = "ArquivoBoemyo";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_IDUSUARIO = "idUsuario";
    private String CHAVE_NOME = "nomeUsuario";
    private String CHAVE_EMAIL = "emailUsuario";
    private String CHAVE_URLIMAGEM = "urlImagemUsuario";
    private String CHAVE_LAT_USUARIO = "latusuario";
    private String CHAVE_LONG_USUARIO = "longUsuario";
    private String CHAVE_QR_CODE = "longUsuario";
    private String CHAVE_IDCOMANDA = "idComanda";
    private String CHAVE_NUMMESA = "numMesa";
    private String CHAVE_SUBTOTAL = "subtotal";

    public Preferencias(Context contextoParametro) {

        context = contextoParametro;
        preferences = context.getSharedPreferences(ARQUIVO_PREFERENCES, MODE);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String idUsuario, String nomeUsuario, String emailUsuario, String urlImagemUsuario){
        editor.putString(CHAVE_IDUSUARIO, idUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.putString(CHAVE_EMAIL, emailUsuario);
        editor.putString(CHAVE_URLIMAGEM, urlImagemUsuario);
        editor.commit();
    }

    public void salvarCoordusuario(Double latUsuario, Double longUsuario){
        editor.putString(CHAVE_LAT_USUARIO, latUsuario.toString());
        editor.putString(CHAVE_LONG_USUARIO, longUsuario.toString());

        editor.commit();
    }

    public void salvarQrCodeEstabelecimento(String codQrcode, String idComanda){
        editor.putString(CHAVE_QR_CODE, codQrcode);
        editor.putString(CHAVE_IDCOMANDA, idComanda);


        editor.commit();
    }

    public void salvarNumMesa(String numMesa){
        editor.putString(CHAVE_NUMMESA, numMesa);

        editor.commit();
    }

    public void salvarSubTotal(Double subTotal){
        editor.putString(CHAVE_SUBTOTAL, subTotal.toString());

        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDUSUARIO, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }

    public String getEmail(){
        return preferences.getString(CHAVE_EMAIL, null);
    }

    public String getUrlImagem(){
        return preferences.getString(CHAVE_URLIMAGEM, null);
    }

    public String getLatUsuario(){
        return preferences.getString(CHAVE_LAT_USUARIO, null);
    }

    public String getLongUsuario(){
        return preferences.getString(CHAVE_LONG_USUARIO, null);
    }

    public String getcodQRcode(){
        return preferences.getString(CHAVE_QR_CODE, null);
    }

    public String getidComanda(){
        return preferences.getString(CHAVE_IDCOMANDA, null);
    }

    public String getNumMesa(){
        return preferences.getString(CHAVE_NUMMESA, null);
    }

    public String getSubTotal(){
        return preferences.getString(CHAVE_SUBTOTAL, null);
    }

    public void limparDados(){
        editor.clear();
        editor.commit();
    }

    public void removerPreferencias(){
        editor.remove(CHAVE_QR_CODE);
        editor.remove(CHAVE_IDCOMANDA);
        editor.remove(CHAVE_NUMMESA);
        editor.remove(CHAVE_SUBTOTAL);
        editor.commit();
    }

}
