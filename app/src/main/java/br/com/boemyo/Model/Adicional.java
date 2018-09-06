package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.boemyo.Configure.FirebaseInstance;

public class Adicional {


    private String idAdicional;
    private String nomeAdicional;
    private int valorAdicional;



    public Adicional() {
    }

    /*public void salvarAdicionalFirebase(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("usuario").child(getIdUsuario()).setValue(this);

    }*/


    @Exclude
    public String getIdAdicional() {
        return idAdicional;
    }

    public void setIdAdicional(String idAdicional) {
        this.idAdicional = idAdicional;
    }

    public String getNomeAdicional() {
        return nomeAdicional;
    }

    public void setNomeAdicional(String nomeAdicional) {
        this.nomeAdicional = nomeAdicional;
    }

    public int getValorAdicional() {
        return valorAdicional;
    }

    public void setValorAdicional(int valorAdicional) {
        this.valorAdicional = valorAdicional;
    }


}
