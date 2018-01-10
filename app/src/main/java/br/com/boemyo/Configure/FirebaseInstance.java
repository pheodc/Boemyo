package br.com.boemyo.Configure;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Phelipe Oberst on 24/09/2017.
 */

public final class FirebaseInstance {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseStorage firebaseStorage;

    public static DatabaseReference getFirebase(){

        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();

        }

        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAuth(){

        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }

        return firebaseAuth;
    }

    public static FirebaseStorage getFirebaseStorage(){

        if(firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance();
        }

        return firebaseStorage;
    }

}