package br.com.boemyo.Model;

public class Carteira {

    private String TokenCartao;
    private String numCartao;
    private String cvv;
    private String bandeira;
    private String tipoPagamento;


    public Carteira() {
    }

    public String getTokenCartao() {
        return TokenCartao;
    }

    public void setTokenCartao(String tokenCartao) {
        TokenCartao = tokenCartao;
    }

    public String getNumCartao() {
        return numCartao;
    }

    public void setNumCartao(String numCartao) {
        this.numCartao = numCartao;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}
