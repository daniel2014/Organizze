package br.com.danielrsoares.organizze.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.helper.DateCustom;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private TextView campoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);

        //Preencha o Campo Data com a Data Atual
        //campoData.setText("06/05/2018");// Dessa forma seria uma data Fixa
        campoData.setText(DateCustom.dataAtual());
    }
}
