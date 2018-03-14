package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 27/12/2017.
 */

public class Pagamento {
    private String idPagamento;
    private String idUsuario;
    private String numCartao;
    private String dataValidaMes;
    private String dataValidaAno;
    private String cvv;
    private String bandeira;

    public Pagamento() {
    }


    public void salvarFirebase(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("Pagamento")
                .child(getIdUsuario())
                .child(getIdPagamento()).setValue(this);

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
}
