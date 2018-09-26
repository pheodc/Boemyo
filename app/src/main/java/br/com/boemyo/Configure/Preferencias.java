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
    private String CHAVE_IDESTABELECIMENTO = "idEstabelecimento";
    private String CHAVE_NUMMESA = "numMesa";
    private String CHAVE_IDPAGAMENTO = "idPagamento";
    private String CHAVE_ABRIR_CATEGORIA = "abrirCategoria";
    private String CHAVE_ABRIR_TUTORIAL = "abrirTutorial";
    private String CHAVE_VALOR_ADICIONAL = "valorAdicional";

    public Preferencias(Context contextoParametro) {

        context = contextoParametro;
        preferences = context.getSharedPreferences(ARQUIVO_PREFERENCES, MODE);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String idUsuario, String nomeUsuario, String emailUsuario){
        editor.putString(CHAVE_IDUSUARIO, idUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.putString(CHAVE_EMAIL, emailUsuario);
        editor.commit();
    }

    public void salvarCoordusuario(Double latUsuario, Double longUsuario){
        editor.putString(CHAVE_LAT_USUARIO, latUsuario.toString());
        editor.putString(CHAVE_LONG_USUARIO, longUsuario.toString());

        editor.commit();
    }

    public void salvarQrCodeEstabelecimento(String codQrcode, String idComanda, String idEstabelecimento){
        editor.putString(CHAVE_QR_CODE, codQrcode);
        editor.putString(CHAVE_IDCOMANDA, idComanda);
        editor.putString(CHAVE_IDESTABELECIMENTO, idEstabelecimento);


        editor.commit();
    }

    public void salvarDadosComanda(String idComanda, String idEstabelecimento){
        editor.putString(CHAVE_IDCOMANDA, idComanda);
        editor.putString(CHAVE_IDESTABELECIMENTO, idEstabelecimento);


        editor.commit();
    }

    public void salvarNumMesa(String numMesa){
        editor.putString(CHAVE_NUMMESA, numMesa);

        editor.commit();
    }

    public void salvarIdPagamento(String idPagamento){
        editor.putString(CHAVE_IDPAGAMENTO, idPagamento);

        editor.commit();
    }

    public void salvarAbrirCategoria(String abrirCategoria){
        editor.putString(CHAVE_ABRIR_CATEGORIA, abrirCategoria);

        editor.commit();
    }

    public void salvarAbrirTutorial(String abrirTutorial){
        editor.putString(CHAVE_ABRIR_TUTORIAL, abrirTutorial);

        editor.commit();
    }

    public void salvarValorAdicional(String valorAdicional){
        editor.putString(CHAVE_VALOR_ADICIONAL, valorAdicional);

        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDUSUARIO, null);
    }

    public String getIdEstabelecimento(){
        return preferences.getString(CHAVE_IDESTABELECIMENTO, null);
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

    public String getIdPagamento(){
        return preferences.getString(CHAVE_IDPAGAMENTO, null);
    }

    public String getAbrirCategoria(){
        return preferences.getString(CHAVE_ABRIR_CATEGORIA, null);
    }

    public String getAbrirTutorial(){
        return preferences.getString(CHAVE_ABRIR_TUTORIAL, null);
    }

    public String getValorAdicional(){
        return preferences.getString(CHAVE_VALOR_ADICIONAL, null);
    }

    public void limparDados(){
        editor.remove(CHAVE_IDUSUARIO);
        editor.remove(CHAVE_NOME);
        editor.remove(CHAVE_EMAIL);
        editor.remove(CHAVE_URLIMAGEM);
        editor.remove(CHAVE_LAT_USUARIO);
        editor.remove(CHAVE_LONG_USUARIO);
        editor.remove(CHAVE_QR_CODE);
        editor.remove(CHAVE_IDCOMANDA);
        editor.remove(CHAVE_IDESTABELECIMENTO);
        editor.remove(CHAVE_NUMMESA);
        editor.remove(CHAVE_IDPAGAMENTO);
        editor.remove(CHAVE_ABRIR_CATEGORIA);
        editor.remove(CHAVE_VALOR_ADICIONAL);

        editor.commit();
    }


    public void removerPreferencias(){
        editor.remove(CHAVE_QR_CODE);
        editor.remove(CHAVE_IDCOMANDA);
        editor.remove(CHAVE_IDESTABELECIMENTO);
        editor.remove(CHAVE_NUMMESA);
        editor.remove(CHAVE_IDPAGAMENTO);
        editor.commit();
    }

    public void removerAbrirCategoria(){
        editor.remove(CHAVE_ABRIR_CATEGORIA);
        editor.commit();
    }

    public void removerAbrirTutorial(){
        editor.remove(CHAVE_ABRIR_TUTORIAL);
        editor.commit();
    }

    public void removerValorAdicional(){
        editor.remove(CHAVE_VALOR_ADICIONAL);
        editor.commit();
    }

    public void removerIdPagamento(){
        editor.remove(CHAVE_IDPAGAMENTO);
        editor.commit();
    }

}
