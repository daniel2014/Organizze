package br.com.danielrsoares.organizze.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;
import br.com.danielrsoares.organizze.helper.Base64Custom;
import br.com.danielrsoares.organizze.helper.DateCustom;
import br.com.danielrsoares.organizze.model.Movimentacao;
import br.com.danielrsoares.organizze.model.Usuario;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase(); //Recupeando a referência do FireBaseDataBase
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao(); //Recupeando a referência do FirebaseAutenticacao
    private Double despesaTotal; // É a despesa que já vem sendo acumulada
    //private Double despesaGerada; // É a despesa que o usuário colocou no campo Valor
    private Double despesaAtualizada; // É a soma da despesaTotal já acumulado + despesaGerada

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

        //Recuperando os dados da Despesa Total ao carregar essa tela DespesaActivity antes do usuário cadastrar a despesa
        recuperarDespesaTotal();
    }

    //Movimentações serão salvas no FireBase
    public void salvarDespesa(View view) {
        if (validarCamposDespesas()) {

            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());// É o valor digitado pelo usuário
            movimentacao.setValor(valorRecuperado); //Pegando o Valor digitado pelo usuário
            movimentacao.setCategoria(campoCategoria.getText().toString());//Pegando Categoria informada pelo usuário
            movimentacao.setDescricao(campoDescricao.getText().toString()); //Pegando pegando a descrição informada pelo usuário
            movimentacao.setData(data); //Pegando a Data informada pelo usuário
            movimentacao.setTipo("d"); //Configura o Tipo de Movimentação (d) = Despesa
            //Antes de Salvar a movimentação temos que atualizar os valores de movomentação
            despesaAtualizada = despesaTotal - valorRecuperado;
            atualizarDespesa(despesaAtualizada); //Método atualizarDespesa();

            movimentacao.salvar(data); //Esse Método pegará todas as informações definida na Classe Movimentacao para salvar no FireBase
            finish(); //Fecha a Tela Despesa após clicar no Botão Salvar
        }
    }

    //Método para Validar os Campos Preenchidos na Despesa antes de Salvar - Incluído dentro do método salvar
    public Boolean validarCamposDespesas() {

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescrição = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()) { // Testa se os campos foi preenchidos
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoDescrição.isEmpty()){
                        return true;// Padrão é retornar verdadeiro (true) ele servirá como um disparador caso os campos estejam todos preenchidos
                        //dentro do método salvarDespesa irá salvar caso for verdadeiro

                    }else {
                        Toast.makeText(DespesaActivity.this,
                                "Descrição não foi preenchido",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(DespesaActivity.this,
                            "Categoria não foi preenchida",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(DespesaActivity.this,
                        "Data não foi preenchido",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(DespesaActivity.this,
                    "Valor não foi preenchido,",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //return true; foi colocado esse retorno aqui apenas para não dar erro na hora de contruir esse código de validação.
    }

    public void recuperarDespesaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail(); //Aqui Recupera o E-mail do usuário cadastrado
        //Recuperando Id do usuário em Base64 do Firebase por meio do E-mail
        String idUsuario = Base64Custom.codificarBase64(emailUsuario); // Aqui converte o e-mail em Base64 para poder acessar o usuario do FireBase
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario); //Acessa o Nó .child("usuarios") e em seguida Acessa o Id do usuário .child(idUsuario) por meio do E-mail codificado em Base64 para acessar os dados usuário no FireBase

        //Criando um Ouvinte para buscar os dados do usuário neste caso recuperar a despesa Total
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /* == Recuperando os Dados do usuario ==
                 *Ao passar a classe Usuario.clas o getValue ele vai converter o retorno do FireBase em um Objeto do Tipo Usuario.class
                 * ou seja os dados que estão salvo no FireBase  como nome, email, despesa total e receita total podedemos agora fazer o inverso
                 *passar a Classe Usuario.class para o getValue e ele vai retornar um Objeto Usuario já preenchido com todos os dados do usuário do fireBase para nossa classe de modelo Usuario.class
                 */
                 Usuario usuario = dataSnapshot.getValue(Usuario.class);
                 despesaTotal = usuario.getDespesaTotal();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // == Método = Atualizar Despesa ==
    public void atualizarDespesa(Double despesa){
        String emailUsuario = autenticacao.getCurrentUser().getEmail(); //Aqui Recupera o E-mail do usuário cadastrado
        //Recuperando Id do usuário em Base64 do Firebase por meio do E-mail
        String idUsuario = Base64Custom.codificarBase64(emailUsuario); // Aqui converte o e-mail em Base64 para poder acessar o usuario do FireBase
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario); // Acessa o Nó .child("usuarios") e em seguida Acessa o Id do usuário .child(idUsuario) por meio do E-mail codificado em Base64 para acessar os dados usuário no FireBase

        usuarioRef.child("despesaTotal").setValue(despesa); //Recupera a despesa Total do FireBase

    }
}
