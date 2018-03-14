package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 10/03/2018.
 */

public class DataComanda {

    private String dataComanda;
    private String idEstabelecimento;
    private String idComanda;

    public DataComanda() {
    }

    public void salvarFirebase(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("dataComanda").child(getDataComanda())
                    .child(getIdEstabelecimento())
                        .child(getIdComanda())
                            .setValue(true);

    }

    public String getDataComanda() {
        return dataComanda;
    }

    public void setDataComanda(String dataComanda) {
        this.dataComanda = dataComanda;
    }

    public String getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(String idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }
}
