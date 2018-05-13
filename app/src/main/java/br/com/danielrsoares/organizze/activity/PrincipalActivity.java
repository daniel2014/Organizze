package br.com.danielrsoares.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.adapter.AdapterMovimentacao;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;
import br.com.danielrsoares.organizze.helper.Base64Custom;
import br.com.danielrsoares.organizze.model.Movimentacao;
import br.com.danielrsoares.organizze.model.Usuario;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;

    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase(); //Recuperando a referência do FireBaseDataBase
    private DatabaseReference usuarioRef; // É um Objeto
    private ValueEventListener valueEventListenerUsuario; //É um objeto que pode tratar e receber um valueEventListener
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>(); //Objeto
    private DatabaseReference movimentacaoRef; //Referência da Movimentação
    private String mesAnoSelecionado; //Atributo para acessar dentro de movimentações do FireBase Online

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze"); // Definindo Nome do Título da ActivityPrincipal
        setSupportActionBar(toolbar);

        textoSaudacao = findViewById(R.id.textSaudacao);
        textoSaldo = findViewById(R.id.textSaldo);
        calendarView = findViewById(R.id.calendarView);
        configuraCalendarView(); //Método Configura CalendarView

        recyclerView = findViewById(R.id.recyclerMovimentos);

        //Configurar Adapter para RecylerView
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //Configurar o RecyClerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    // ===== Recuperar Movimntações no Firebase ===== //
    public void recuperarMovimentacoes() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail(); //Aqui Recupera o E-mail do usuário cadastrado
        //Recuperando Id do usuário em Base64 do Firebase por meio do E-mail
        String idUsuario = Base64Custom.codificarBase64(emailUsuario); // Aqui converte o e-mail em Base64 para poder acessar o usuario do FireBase
        movimentacaoRef = firebaseRef.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado); //Desntro de movimentacoes do FireBase Online
        Log.i("dadosRetorno", "dados: " + mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() { //Recuperando os dados de movimentação
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear(); //Limpando as Listas das movimentações antes de o 'for' abaixo começar a começar a configurar valores para ela

                //getChildren ele pega o dataSnapshot e percorre o primeiro filho desse dataSnapshot
                for (DataSnapshot dados : dataSnapshot.getChildren()) { //Percorrendo cada uma das movimentações no Firebase
                    Log.i("dados", "retorno: " + dados.toString());

                    Movimentacao movimentacao = dados.getValue(Movimentacao.class); //Recuperando para => Pacote: model => Classe: Movimentacao.class
                    movimentacoes.add(movimentacao); //Criando um Array de List
                    Log.i("dadosRetorno", "dados: " + movimentacao.getCategoria());

                }
                //Método => Notifica O adapterMovimentacao que os dados mudaram
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("MES", "mês: " + mesAnoSelecionado);
    }


    // === Recuperar Resumo da exibição dentro da ActivityPrincipal do FireBase ===
    public void recuperarResumo() {

        String emailUsuario = autenticacao.getCurrentUser().getEmail(); //Aqui Recupera o E-mail do usuário cadastrado
        //Recuperando Id do usuário em Base64 do Firebase por meio do E-mail
        String idUsuario = Base64Custom.codificarBase64(emailUsuario); // Aqui converte o e-mail em Base64 para poder acessar o usuario do FireBase
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario); //Acessa o Nó .child("usuarios") e em seguida Acessa o Id do usuário .child(idUsuario) por meio do E-mail codificado em Base64 para acessar os dados usuário no FireBase

        //valueEventListenerUsuario irá acessar ValueEventListener para podemos para-lo pois ele fica executando ativamente sem necessidade nesse caso
        //ou seja vamos Remover o Evento do Listener
        Log.i("Evento", "Evento foi adicionado!");
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = despesaTotal + receitaTotal;

                //Limk: https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
                DecimalFormat decimalFormat = new DecimalFormat("0.##"); //Formatar o formato dos números com 2 casas decimais | Caso valor for 00 depois do ponto ele desconsidera essa casa decimal
                String resultadoFormatado = decimalFormat.format(resumoUsuario); //retona uma String com resumoUsuario formatado conforme configurado linha acima

                textoSaudacao.setText("Olá, " + usuario.getNome());
                textoSaldo.setText("R$ " + resultadoFormatado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // == Exibindo MENU Toolbar - Sobreescrevendo o Método ===>
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu); // É um Objeto que Vei converter o XML em uma View
        return super.onCreateOptionsMenu(menu);
    }

    // == Método para Tratar as Ações do Menu - Botão para DESLOGAR do App ===>
    //Pode incluir mais aitem de menu aqui
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair: // Esse ID vem do XML menu_principal
                autenticacao.signOut(); //Deslogando
                startActivity(new Intent(this, LoginActivity.class)); //Após deslogar enviar para Tela de Login
                finish(); // e Finaliza a tela PrincipalActivity
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // === Método => Boão adicionarDespesa ===/
    public void adicionarDespesa(View view) {
        startActivity(new Intent(this, DespesaActivity.class));

    }

    // === Método => Botão adicionarReceita ===/
    public void adicionarReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));

    }

    // ------------------------------------- CalendarView --------------------------------------------------------------//
    //OBS: Nesse método tem atributos repetido e deve seguir esse formato pois são métodos dentro do outro para funcionar corretamente
    public void configuraCalendarView() {

        //Configurando o Mês Selecionado
        CalendarDay dataAtual = calendarView.getCurrentDate();
        //É necessário fazer essa configuração adicioando um '0' pois no fireBase a data esta como 022018 e daforma que estava consultando era 22018 retornando null
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1)); //Definindo um formato com um '0' a esquerda
        //String.valueOf = Converte um Inteiro para uma String
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear()); //"" serve para não somar os valores pois se trata de Data

        //Esse Método => Somente é chamado quando é movimentado a barra do calendário
        //Método => Verifica quando navergar no calendário pega a data atual apenas navegando ex: 01/02/2018 de forma automática irá atualizar o mês navegado
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                //Log.i("data","valor: " + date); // mostra a data completa padrão inglês
                // Log.i("data", "valor: " + (date.getMonth() + 1) + "/" + date.getYear());
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1)); //% = queremos criar uma formatação | 02 = a quantidades de números | d = tipo digito
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear()); //Printando a Data selecionado pelo usuário

                //Antes de anexar novamente um evento é necessário remover o anterior com o código abaixo
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();

            }
        });
    }

    // === Recuperando o Resumo no estado onStart ou seja recupera Evento do Listener do método recuperarResumo abaixo ====/
    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes(); //recuperando movimentações
    }

    //Sobreescrever a Classe onStop / ele é chamado sempre que o app não estiver mais sendo utilizado.
    //ou seja desanexando o Resumo do Listener no estado onStop ou seja desanexa o Evento do Listener do método recuperarResumo bem acima
    //Removendo Evento do Listener
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Evento", "Evento foi removido!");
        usuarioRef.removeEventListener(valueEventListenerUsuario); // Remove EventListener de Referência de Usuário
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes); //Remove EventListener de Movimentação de Usuário
    }
}
