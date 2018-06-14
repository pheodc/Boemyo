package br.com.boemyo.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.boemyo.Configure.FirebaseInstance;

/**
 * Created by Phelipe Oberst on 27/11/2017.
 */

public class Favorito {

    private String idEstabelecimento;
    private String nomeFavorito;
    private String imgFundoFavorito;
    private String idUsuario;

    public Favorito() {
    }

    public void salvarFavoritoEstabelecimento(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference
                .child("estabelecimento")
                    .child(getIdEstabelecimento())
                        .child("favorito")
                            .child(getIdUsuario())
                .setValue(true);

    }

    public void salvarFavoritoUsuario(){

        DatabaseReference databaseReference = FirebaseInstance.getFirebase();
        databaseReference
                .child("usuario")
                    .child(getIdUsuario())
                        .child("favorito")
                            .child(getIdEstabelecimento())
                .setValue(true);

    }

    public String getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(String idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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
