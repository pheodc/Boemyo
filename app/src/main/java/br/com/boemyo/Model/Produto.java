package br.com.boemyo.Model;

import java.io.Serializable;

/**
 * Created by Phelipe Oberst on 08/11/2017.
 */

public class Produto implements Serializable {

    private String idProduto;
    private String nomeProduto;
    private Double valorProduto;
    private String descProduto;
    private String categoria;
    private String urlImagemProduto;
    private Boolean ativoProduto;

    public Produto() {
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescProduto() {
        return descProduto;
    }

    public void setDescProduto(String descProduto) {
        this.descProduto = descProduto;
    }


    public String getUrlImagemProduto() {
        return urlImagemProduto;
    }

    public void setUrlImagemProduto(String urlImagemProduto) {
        this.urlImagemProduto = urlImagemProduto;
    }

    public Double getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(Double valorProduto) {
        this.valorProduto = valorProduto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getAtivoProduto() {
        return ativoProduto;
    }

    public void setAtivoProduto(Boolean ativoProduto) {
        this.ativoProduto = ativoProduto;
    }
}
