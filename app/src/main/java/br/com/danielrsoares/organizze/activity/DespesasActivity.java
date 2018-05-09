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

    //Movimentações serão salvas no FireBase
    public void salvarDespesa(View view) {
        if (validarCamposDespesas()) {

            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            movimentacao.setValor(Double.parseDouble(campoValor.getText().toString())); //Pegando o Valor digitado pelo usuário
            movimentacao.setCategoria(campoCategoria.getText().toString());//Pegando Categoria informada pelo usuário
            movimentacao.setDescricao(campoDescricao.getText().toString()); //Pegando pegando a descrição informada pelo usuário
            movimentacao.setData(data); //Pegando a Data informada pelo usuário
            movimentacao.setTipo("d"); //Configura o Tipo de Movimentação (d) = Despesa

            movimentacao.salvar(data); //Esse Método pegará todas as informações definida na Classe Movimentacao para salvar no FireBase

            Toast.makeText(getApplicationContext(), "Salvando...", Toast.LENGTH_LONG).show();
        }
    }

    //Método para Validar os Campos Preenchidos na Despesa antes de Salvar - Incluído dentro do método salvar
    public Boolean validarCamposDespesas() {

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescrição = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()) {
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoDescrição.isEmpty()){
                        return true;// Padrão é retornar verdadeiro (true) ele servirá como um disparador caso os campos estejam todos preenchidos
                        //dentro do método salvarDespesa irá salvar caso for verdadeiro

                    }else {
                        Toast.makeText(DespesasActivity.this,
                                "Descrição não foi preenchido",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(DespesasActivity.this,
                            "Categoria não foi preenchida",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(DespesasActivity.this,
                        "Data não foi preenchido",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(DespesasActivity.this,
                    "Valor não foi preenchido,",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //return true; foi colocado esse retorno aqui apenas para não dar erro na hora de contruir esse código de validação
    }
}
