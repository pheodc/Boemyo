package br.com.boemyo.Model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 23/09/2017.
 */

public class Usuario {

    private String idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String passwordUsuario;
    private String dataNascimentoUsuario;
    private String generoUsuario;
    private String imagemUsuario;
    private double latUsuario;
    private double longUsuario;


    public Usuario() {
    }

    public void salvarFirebase(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("usuario").child(getIdUsuario()).setValue(this);

    }
    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    @Exclude
    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {this.passwordUsuario = passwordUsuario;}

    public String getDataNascimentoUsuario() {
        return dataNascimentoUsuario;
    }

    public void setDataNascimentoUsuario(String dataNascimentoUsuario) {this.dataNascimentoUsuario = dataNascimentoUsuario;}

    public String getGeneroUsuario() {
        return generoUsuario;
    }

    public void setGeneroUsuario(String generoUsuario) {
        this.generoUsuario = generoUsuario;
    }

    @Exclude
    public double getLatUsuario() {return latUsuario;}

    public void setLatUsuario(double latUsuario) {
        this.latUsuario = latUsuario;
    }

    @Exclude
    public double getLongUsuario() {
        return longUsuario;
    }

    public void setLongUsuario(double longUsuario) {
        this.longUsuario = longUsuario;
    }

    public String getImagemUsuario() {
        return imagemUsuario;
    }

    public void setImagemUsuario(String imagemUsuario) {
        this.imagemUsuario = imagemUsuario;
    }
}
