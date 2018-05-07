package br.com.danielrsoares.organizze.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.helper.DateCustom;
import br.com.danielrsoares.organizze.model.Movimentacao;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //Preencha o Campo Data com a Data Atual
        //campoData.setText("06/05/2018");// Dessa forma seria uma data Fixa
        campoData.setText(DateCustom.dataAtual());
    }

    public void salvarDespesa(View view){
        movimentacao = new Movimentacao();
        String data = campoData.getText().toString();
        movimentacao.setValor(Double.parseDouble(campoValor.getText().toString())); //Pegando o Valor digitado pelo usuário
        movimentacao.setCategoria(campoCategoria.getText().toString());//Pegando Categoria informada pelo usuário
        movimentacao.setDescricao(campoDescricao.getText().toString()); //Pegando pegando a descrição informada pelo usuário
        movimentacao.setData(data); //Pegando a Data informada pelo usuário
        movimentacao.setTipo("d"); //Configura o Tipo de Movimentação (d) = Despesa

        movimentacao.salvar(data); //Esse Método pegará todas as informações definida na Classe Movimentacao para salvar no FireBase

        Toast.makeText(getApplicationContext(),"Salvando...", Toast.LENGTH_LONG).show();
    }
}
