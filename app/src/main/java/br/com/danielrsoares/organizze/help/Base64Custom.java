package br.com.danielrsoares.organizze.help;

import android.util.Base64;

// ======= CLASSE DE CODIFICAÇÂO DE BASE64 ========//
public class Base64Custom {

    public static String codificarBase64(String texto){
        // Usado base64 do android.util | .replaceAll("") para substituir alguns caracteres especiais para evitar erros
        //Substituir por vazio removendo espaços no comeco e final do texto, garantindo que a base64 seja montado apenas com caractere válidos
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("\\n|\\r","");
    }

    public static String decodificarBase64(String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
