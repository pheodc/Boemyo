package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.Date;

import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Preferencias;

/**
 * Created by Phelipe Oberst on 02/12/2017.
 */

public class IndiqueEtabelecimentos {

    private String idUsuario;
    private String idIndicado;
    private String nomeIndicado;
    private String cidadeIndicado;
    private String telefoneIndicado;
    private String obsIndicado;


    public void salvarIndicado(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("indiqueEstabelecimentos").child(getIdUsuario()).child(getIdIndicado()).setValue(this);

    }

    public IndiqueEtabelecimentos() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdIndicado() {
        return idIndicado;
    }

    public void setIdIndicado(String idIndicado) {
        this.idIndicado = idIndicado;
    }

    public String getNomeIndicado() {
        return nomeIndicado;
    }

    public void setNomeIndicado(String nomeIndicado) {
        this.nomeIndicado = nomeIndicado;
    }

    public String getCidadeIndicado() {
        return cidadeIndicado;
    }

    public void setCidadeIndicado(String cidadeIndicado) {
        this.cidadeIndicado = cidadeIndicado;
    }

    public String getTelefoneIndicado() {
        return telefoneIndicado;
    }

    public void setTelefoneIndicado(String telefoneIndicado) {
        this.telefoneIndicado = telefoneIndicado;
    }

    public String getObsIndicado() {
        return obsIndicado;
    }

    public void setObsIndicado(String obsIndicado) {
        this.obsIndicado = obsIndicado;
    }
}
