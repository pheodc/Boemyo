package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 06/01/2018.
 */

public class Comanda {

    private String idQrCode;
    private String idComanda;
    private String numMesa;
    private String situacaoComanda;
    private String subTotal;

    public Comanda() {
    }

    public void salvarFirebase(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("comanda")
                .child(getIdQrCode())
                .child(getIdComanda())
                .child("info")
                .setValue(this);

    }
    @Exclude
    public String getIdQrCode() {
        return idQrCode;
    }

    public void setIdQrCode(String idQrCode) {
        this.idQrCode = idQrCode;
    }

    @Exclude
    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }

    public String getNumMesa() {
        return numMesa;
    }

    public void setNumMesa(String numMesa) {
        this.numMesa = numMesa;
    }

    public String getSituacaoComanda() {
        return situacaoComanda;
    }

    public void setSituacaoComanda(String situacaoComanda) {
        this.situacaoComanda = situacaoComanda;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }
}
