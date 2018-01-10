package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.sql.Date;
import java.sql.Time;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 13/11/2017.
 */

public class Pedido {

    private String idPedido;
    private String codQrCode;
    private String idUsuario;
    private String idComanda;
    private String idProduto;
    private String qtdeProduto;
    private String obsProduto;
    private int situacaoPedido;
    private Double valorPedido;
    private String horaPedido;

    public Pedido() {
    }

    public void salvarFirebase(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("comanda")
                .child(getCodQrCode())
                    .child(getIdComanda())
                        .child(getIdUsuario())
                            .child(String.valueOf(getIdPedido())).setValue(this);

    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
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

    @Exclude
    public String getCodQrCode() {
        return codQrCode;
    }

    public void setCodQrCode(String codQrCode) {
        this.codQrCode = codQrCode;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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
}
