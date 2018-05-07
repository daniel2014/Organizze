package br.com.danielrsoares.organizze.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Double receitaTotal = 0.00;
    private Double despesaTotal = 0.00;

    public Usuario() {
    }

    //Ao Clicar Criar Cadastro esse Método Cria no firebase as referência desse método
    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios") //Cria Nó usuário
                .child(this.idUsuario) //Cria Nó ID do usuário em base64
                .setValue(this); //Salvando o Objeto Usuário ou seja os dados
        /*O que for criado aqui como get e seter ele irá automáticamente adicionar os parâmetros dentro do firebase
          mesmo que eu ainda não tenha criado os métodos de entrada de informações por que o método
          .setValue(this) lança todos os valores aqui criado dentro de um Nó do FireBase, salvo quando tem anotação @Exclude
          ai nesse caso ele não irá criar um setValue dentro de um Nó no FireBase pois ele ignora esse parâmetro
        */
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
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
