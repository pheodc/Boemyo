package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 06/01/2018.
 */

public class Comanda {

    private String idEstabelecimento;
    private String idComanda;
    private String idPedido;
    private String idUsuario;
    private String numMesa;
    private Boolean situacaoComanda;
    private Double subTotal;
    private String dataAbertura;
    private String horaAbertura;
    private DatabaseReference databaseReference = FirebaseInstance.getFirebase();
    public Comanda() {
    }

    public void salvarFirebase(){

        databaseReference.child("comanda")
                    .child(getIdComanda())
                        .setValue(this);

    }

    public void salvarEstabelecimento(){
        databaseReference.child("comanda")
                .child(getIdComanda())
                    .child(getIdEstabelecimento())
                        .setValue(true);
    }

    public void salvarUsuario(){
        databaseReference.child("comanda")
                .child(getIdComanda())
                    .child(getIdUsuario())
                        .setValue(true);
    }

    public void salvarComandaUsuario(){
        databaseReference.child("usuario")
                .child(getIdUsuario())
                .child("comandas")
                    .child(getIdComanda())
                        .setValue(true);
    }

    public void salvarComandaEstabelecimento(){
        databaseReference.child("estabelecimento")
                .child(getIdEstabelecimento())
                .child("comandas")
                    .child(getIdComanda())
                        .setValue(true);
    }

    public void salvarPedidoComanda(){
        databaseReference.child("comanda")
                .child(getIdComanda())
                        .child("pedidos")
                            .child(getIdPedido())
                                .setValue(true);
    }

    @Exclude
    public String getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(String idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
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

    public Boolean getSituacaoComanda() {
        return situacaoComanda;
    }

    public void setSituacaoComanda(Boolean situacaoComanda) {
        this.situacaoComanda = situacaoComanda;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(String horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }
}
