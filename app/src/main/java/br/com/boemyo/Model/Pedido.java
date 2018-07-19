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
    private String comanda;
    private String produto;
    private String qtdeProduto;
    private String obsProduto;
    private int situacaoPedido;
    private Double valorPedido;
    private String horaPedido;
    private String idEstabelecimento;
    private DatabaseReference databaseReference = FirebaseInstance.getFirebase();
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
}
