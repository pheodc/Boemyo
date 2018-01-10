package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 27/11/2017.
 */

public class Favorito {

    private String idQrCode;
    private String nomeFavorito;
    private String imgFundoFavorito;
    private String idUsuario;

    public Favorito() {
    }

    public void salvarFavorito(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference.child("favorito").child(getIdUsuario()).child(getIdQrCode()).setValue(this);

    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdQrCode() {
        return idQrCode;
    }

    public void setIdQrCode(String idQrCode) {
        this.idQrCode = idQrCode;
    }

    public String getNomeFavorito() {
        return nomeFavorito;
    }

    public void setNomeFavorito(String nomeFavorito) {
        this.nomeFavorito = nomeFavorito;
    }

    public String getImgFundoFavorito() {
        return imgFundoFavorito;
    }

    public void setImgFundoFavorito(String imgFundoFavorito) {
        this.imgFundoFavorito = imgFundoFavorito;
    }
}
