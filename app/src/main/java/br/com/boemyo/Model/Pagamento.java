package br.com.boemyo.Model;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import br.com.boemyo.Activitys.AdicionaNovoCartaoActivity;
import br.com.boemyo.Configure.Base64Custom;
import br.com.boemyo.Configure.FirebaseInstance;
import cieloecommerce.sdk.Merchant;
import cieloecommerce.sdk.ecommerce.Card;
import cieloecommerce.sdk.ecommerce.CieloEcommerce;
import cieloecommerce.sdk.ecommerce.Customer;
import cieloecommerce.sdk.ecommerce.Environment;
import cieloecommerce.sdk.ecommerce.Payment;
import cieloecommerce.sdk.ecommerce.Sale;
import cieloecommerce.sdk.ecommerce.request.CieloRequestException;
import cieloecommerce.sdk.ecommerce.request.UpdateSaleRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Phelipe Oberst on 27/12/2017.
 */


public class Pagamento {
    private final String MERCHANT_ID = "ae999df1-e980-4437-84cd-27f5bc5a8bc6";
    private final String MERCHANT_ID_SANDBOX = "e1212049-e60c-43c9-a570-5636ce5a7fe1";
    private final String MERCHANT_KEY = "6T90iOa5b8IPUZUSkqPQuLvshK7vY9SMLLWgvX36";
    private final String MERCHANT_KEY_SANDBOX = "DUKPGIRXJOFGUQMIEYERVQBCPZSWLFIXYBDYWULA";
    private final String URLPRODUCTIONCARDTOKEN = "https://api.cieloecommerce.cielo.com.br/1/card";
    private final String URLSANDCARDTOKEN = "https://apisandbox.cieloecommerce.cielo.com.br/1/card";
    private String cardToken;
    private String idPagamento;
    private String idUsuario;
    private String nomeUsuario;
    private String numCartao;
    private String dataValidaMes;
    private String dataValidaAno;
    private String cvv;
    private String bandeira;
    private String tipoPagamento;
    private OkHttpClient client = new OkHttpClient();
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    public Pagamento() {
    }


    public void salvarFirebase(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("Pagamento")
                .child(getIdUsuario())
                .child(getIdPagamento()).setValue(this);

    }

    Call postSales(Callback callback) throws JSONException {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("CustomerName", getNomeUsuario());
        jsonObj.put("CardNumber", getNumCartao());
        jsonObj.put("ExpirationDate", getDataValidaMes()+"/"+getDataValidaAno());
        jsonObj.put("Brand", getBandeira());
        jsonObj.put("Holder", getNomeUsuario());

        RequestBody body = RequestBody.create(mediaType, jsonObj.toString());
        Request request = new Request.Builder()
                .url(URLPRODUCTIONCARDTOKEN)
                .addHeader("MerchantId", MERCHANT_ID)
                .addHeader("Content-Type", "application/json")
                .addHeader("MerchantKey", MERCHANT_KEY  )
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "74cf7477-07cd-42d0-a58c-7857c9edade0")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);


        return call;

    }

    public void salvarCartaoTokenizado(final Context context) throws JSONException {

        postSales(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();

                    try {
                        JSONObject jsonOResponse = new JSONObject(jsonData);

                        setCardToken(jsonOResponse.getString("CardToken"));
                        Log.i("LOG_CARD_TOKEN", getCardToken());

                        String numCartaoEdit = getNumCartao().substring(getNumCartao().length() -4);
                        DatabaseReference databaseReference = FirebaseInstance.getFirebase();

                        databaseReference.child("carteira")
                                            .child(getIdUsuario())
                                                .child(getCardToken())
                                                    .child("cvv")
                                                        .setValue(Base64Custom.codificarBase64(getCvv()));

                        databaseReference.child("carteira")
                                            .child(getIdUsuario())
                                                .child(getCardToken())
                                                    .child("numCartao")
                                                        .setValue(Base64Custom.codificarBase64(numCartaoEdit));

                        databaseReference.child("carteira")
                                            .child(getIdUsuario())
                                                .child(getCardToken())
                                                    .child("bandeira")
                                                        .setValue(getBandeira());

                        databaseReference.child("carteira")
                                                    .child(getIdUsuario())
                                                        .child(getCardToken())
                                                            .child("tipoPagamento")
                                                                .setValue(getTipoPagamento());


                        ((AdicionaNovoCartaoActivity)context).finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Request not successful
                }
            }
        });

    }

    public String getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(String idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getNumCartao() {
        return numCartao;
    }

    public void setNumCartao(String numCartao) {
        this.numCartao = numCartao;
    }

    public String getDataValidaMes() {
        return dataValidaMes;
    }

    public void setDataValidaMes(String dataValidaMes) {
        this.dataValidaMes = dataValidaMes;
    }

    public String getDataValidaAno() {
        return dataValidaAno;
    }

    public void setDataValidaAno(String dataValidaAno) {
        this.dataValidaAno = dataValidaAno;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}
