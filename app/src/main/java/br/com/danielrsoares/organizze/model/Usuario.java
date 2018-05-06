package br.com.danielrsoares.organizze.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    //Ao Clicar Criar Cadastro esse Método Cria no firebase as referência desse método
    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios") //Cria Nó usuário
                .child(this.idUsuario) //Cria Nó ID do usuário em base64
                .setValue(this); //Salvando o Objeto Usuário ou seja os dados
    }

    @Exclude //Anotação do Firebase remove esse dado na hora de Salvar o Objeto
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude //Anotação do Firebase remove esse dado na hora de Salvar o Objeto
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
