package br.com.danielrsoares.organizze.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao; //Atributo

    //Criar um método statico que retorno o FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao() {

        if (autenticacao == null){ //verifica se existe uma instância caso ao contrario
            autenticacao = FirebaseAuth.getInstance(); //irá recuperar no getInstance
        }
        return autenticacao; // retorna a autenticação
    }

}
