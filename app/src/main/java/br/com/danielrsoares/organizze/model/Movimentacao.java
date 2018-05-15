package br.com.danielrsoares.organizze.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;
import br.com.danielrsoares.organizze.helper.Base64Custom;
import br.com.danielrsoares.organizze.helper.DateCustom;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo; // Tipo da Movimentação
    private double valor;
    private String key;

    public Movimentacao() {
    }

    // Método => Para salvar no Firebase
    public void salvar(String dataEscolhida){ // dataEscolhida recebe do parâmetro data
        //Utilizando a Base64 para usar como um identificador
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();//Recupeando email do usuário
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail()); //Codificando E-mail para Base64
        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);
        
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao") // Nó movimentacao
                .child(idUsuario) // Nó identificador de Usuário usando E-mail em Base64
                .child(mesAno) // Nó Mês da movimentação
                .push() // Cria o ID único do FireBase para cada incrementação ou seja cada vez que for salvo as informações ele gere um ID para aquele salvamento
                .setValue(this); // Pega os valor dos Atributos
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
