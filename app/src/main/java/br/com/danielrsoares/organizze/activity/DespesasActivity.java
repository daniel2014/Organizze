package br.com.danielrsoares.organizze.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        movimentacao.setValor(Double.parseDouble(campoValor.getText().toString()));
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setData(campoData.getText().toString());
        movimentacao.setTipo("d"); //Configura o Tipo de Movimentação (d) = Despesa

        movimentacao.salvar(); //Adicionando esse conteudo para o Método salvar() da Classe Movimentação
    }
}
