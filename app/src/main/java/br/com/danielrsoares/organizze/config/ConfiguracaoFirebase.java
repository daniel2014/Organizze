package br.com.danielrsoares.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static DatabaseReference firebase;
    private static FirebaseAuth autenticacao; //Atributo

    //Retorna a instancia do FirebaseDatabase Banco de Dados RealTime
    public static DatabaseReference getFirebaseDatabase(){

        if (firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    //Criar um método statico que retorno o FirebaseAuth Login
    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null){ //verifica se existe uma instância caso ao contrario
            autenticacao = FirebaseAuth.getInstance(); //irá recuperar no getInstance
        }
        return autenticacao; // retorna a autenticação
    }

}
