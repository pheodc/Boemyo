package br.com.boemyo.Model;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Phelipe Oberst on 13/11/2017.
 */

public class Pedido {

    private final String MERCHANT_ID = "ae999df1-e980-4437-84cd-27f5bc5a8bc6";
    private final String MERCHANT_ID_SANDBOX = "e1212049-e60c-43c9-a570-5636ce5a7fe1";
    private final String MERCHANT_KEY = "6T90iOa5b8IPUZUSkqPQuLvshK7vY9SMLLWgvX36";
    private final String MERCHANT_KEY_SANDBOX = "DUKPGIRXJOFGUQMIEYERVQBCPZSWLFIXYBDYWULA";
    private String idPedido;
    private String comanda;
    private String produto;
    private String qtdeProduto;
    private String obsProduto;
    private int situacaoPedido;
    private Double valorPedido;
    private String horaPedido;
    private String idEstabelecimento;
    private String nomeUsuario;
    private String tokenCardPedido;
    private String bandeiraCartaoPedido;
    private String cvvPedido;
    private String idCieloPedido;
    private String tipoPagamentoPedido;
    private DatabaseReference databaseReference = FirebaseInstance.getFirebase();
    private OkHttpClient client = new OkHttpClient();
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private final String URLPRODUCTIONSALES = "https://api.cieloecommerce.cielo.com.br/1/sales";
    private final String URLSANDBOXSALES = "https://apiquery.cieloecommerce.cielo.com.br/1/sales";
    public Pedido() {
    }

    public void salvarFirebase(){


        databaseReference.child("pedido")
                            .child(getIdPedido())
                                .setValue(this);

    }

    public void salvarStatusPedido(){

        databaseReference.child("statusPedido")
                            .child("novoPedido")
                                .child(getIdEstabelecimento())
                                    .child(getIdPedido())
                                        .setValue(true);

    }

    public Call post(Callback callback) throws JSONException {

        JSONObject jsonObjCust = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonPay = new JSONObject();
        JSONObject jsonCard = new JSONObject();


        jsonObjCust.put("Name", getNomeUsuario());

        jsonCard.put("CardToken", getTokenCardPedido());
        jsonCard.put("Brand", getBandeiraCartaoPedido());
        jsonCard.put("SecurtyCode", getCvvPedido());

        jsonPay.put("Type", getTipoPagamentoPedido());
        jsonPay.put("Amount", getValorPedido());
        jsonPay.put("Installments", 1);
        jsonPay.put(getTipoPagamentoPedido(), jsonCard);
        jsonPay.put("Provider", "Cielo");
        if(getTipoPagamentoPedido().equals("DebitCard")){
            jsonCard.put("ReturnUrl", "http://www.google.com.br");
        }

        jsonObj.put("MerchantOrderId", getIdPedido());
        jsonObj.put("Customer", jsonObjCust);
        jsonObj.put("Payment", jsonPay);

        Log.i("LOG_JSON", jsonObj.toString());

        RequestBody body = RequestBody.create(mediaType, jsonObj.toString());
        Request request = new Request.Builder()
                .url(URLPRODUCTIONSALES)
                .addHeader("MerchantId", MERCHANT_ID)
                .addHeader("Content-Type", "application/json")
                .addHeader("MerchantKey", MERCHANT_KEY  )
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "bbaa8cc8-460a-4b3b-aed2-c539a8a3dc2a")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);

        return call;
    }

    public Call postEstorno(String payamentId, Callback callback){

        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url("https://api.cieloecommerce.cielo.com.br/1/sales/" + payamentId + "/void")
                .put(body)
                .addHeader("MerchantId", MERCHANT_ID)
                .addHeader("Content-Type", "text/json")
                .addHeader("MerchantKey", MERCHANT_KEY)
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "b6d68989-9fd3-42ac-98c5-cc046194e892")
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

        return call;
    }

    public Call postCaptura(Callback callback){

        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url("https://api.cieloecommerce.cielo.com.br/1/sales/" + getIdCieloPedido() +"/capture")
                .put(body)
                .addHeader("MerchantId", MERCHANT_ID)
                .addHeader("Content-Type", "text/json")
                .addHeader("MerchantKey", MERCHANT_KEY)
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "b6d68989-9fd3-42ac-98c5-cc046194e892")
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

        return call;
    }

/*    public void salvarIdComanda(){
        databaseReference.child("pedido")
                .child(getIdPedido())
                    .child(getIdComanda())
                        .setValue(true);
    }

    public void salvarIdProduto(){
        databaseReference.child("pedido")
                .child(getIdPedido())
                    .child(getIdProduto())
                        .setValue(true);
    }*/

    public String getComanda() {
        return comanda;
    }

    public void setComanda(String comanda) {
        this.comanda = comanda;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getQtdeProduto() {
        return qtdeProduto;
    }

    public void setQtdeProduto(String qtdeProduto) {
        this.qtdeProduto = qtdeProduto;
    }

    public String getObsProduto() {
        return obsProduto;
    }

    public void setObsProduto(String obsProduto) {
        this.obsProduto = obsProduto;
    }


    public int getSituacaoPedido() {
        return situacaoPedido;
    }

    public void setSituacaoPedido(int situacaoPedido) {
        this.situacaoPedido = situacaoPedido;
    }

    public Double getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(Double valorPedido) {
        this.valorPedido = valorPedido;
    }

    public String getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(String horaPedido) {
        this.horaPedido = horaPedido;
    }

    @Exclude
    public String getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(String idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    @Exclude
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }


    public String getTokenCardPedido() {
        return tokenCardPedido;
    }

    public void setTokenCardPedido(String tokenCardPedido) {
        this.tokenCardPedido = tokenCardPedido;
    }
    @Exclude
    public String getBandeiraCartaoPedido() {
        return bandeiraCartaoPedido;
    }

    public void setBandeiraCartaoPedido(String bandeiraCartaoPedido) {
        this.bandeiraCartaoPedido = bandeiraCartaoPedido;
    }

    @Exclude
    public String getCvvPedido() {
        return cvvPedido;
    }

    public void setCvvPedido(String cvvPedido) {
        this.cvvPedido = cvvPedido;
    }

    public String getIdCieloPedido() {
        return idCieloPedido;
    }

    public void setIdCieloPedido(String idCieloPedido) {
        this.idCieloPedido = idCieloPedido;
    }

    @Exclude
    public String getTipoPagamentoPedido() {
        return tipoPagamentoPedido;
    }

    public void setTipoPagamentoPedido(String tipoPagamentoPedido) {
        this.tipoPagamentoPedido = tipoPagamentoPedido;
    }
}
