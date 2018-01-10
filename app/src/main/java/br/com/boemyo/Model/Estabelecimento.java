package br.com.boemyo.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Phelipe Oberst on 09/10/2017.
 */

public class Estabelecimento implements Serializable {

    private String idQRCODE;
    private String nomeEstabelecimento;
    private String tipoEstabelecimento;
    private String logoEstabelecimento;
    private String perfilEstabelecimento;
    private String descEstabelecimento;
    private String enderecoEstabelecimento;
    private String horarioEstabelecimento;
    private Double latEstabelecimento;
    private Double longEstabelecimento;

    public Estabelecimento() {
    }

    public String getIdQRCODE() {
        return idQRCODE;
    }

    public void setIdQRCODE(String idQRCODE) {
        this.idQRCODE = idQRCODE;
    }

    public String getNomeEstabelecimento() {
        return nomeEstabelecimento;
    }

    public void setNomeEstabelecimento(String nomeEstabelecimento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
    }

    public Double getLatEstabelecimento() {
        return latEstabelecimento;
    }

    public void setLatEstabelecimento(Double latEstabelecimento) {
        this.latEstabelecimento = latEstabelecimento;
    }

    public Double getLongEstabelecimento() {
        return longEstabelecimento;
    }

    public void setLongEstabelecimento(Double longEstabelecimento) {
        this.longEstabelecimento = longEstabelecimento;
    }

    public String getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }

    public void setTipEstabelecimento(String tipo_estabelecimento) {
        this.tipoEstabelecimento = tipo_estabelecimento;
    }

    public String getLogoEstabelecimento() {
        return logoEstabelecimento;
    }

    public void setLogoEstabelecimento(String logo_estabelecimento) {
        this.logoEstabelecimento = logo_estabelecimento;
    }

    public void setTipoEstabelecimento(String tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
    }

    public String getPerfilEstabelecimento() {
        return perfilEstabelecimento;
    }

    public void setPerfilEstabelecimento(String perfilEstabelecimento) {
        this.perfilEstabelecimento = perfilEstabelecimento;
    }

    public String getDescEstabelecimento() {
        return descEstabelecimento;
    }

    public void setDescEstabelecimento(String descEstabelecimento) {
        this.descEstabelecimento = descEstabelecimento;
    }

    public String getEnderecoEstabelecimento() {
        return enderecoEstabelecimento;
    }

    public void setEnderecoEstabelecimento(String enderecoEstabelecimento) {
        this.enderecoEstabelecimento = enderecoEstabelecimento;
    }

    public String getHorarioEstabelecimento() {
        return horarioEstabelecimento;
    }

    public void setHorarioEstabelecimento(String horarioEstabelecimento) {
        this.horarioEstabelecimento = horarioEstabelecimento;
    }
}