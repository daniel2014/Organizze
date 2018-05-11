package br.com.danielrsoares.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;
import br.com.danielrsoares.organizze.helper.Base64Custom;
import br.com.danielrsoares.organizze.model.Usuario;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase(); //Recupeando a referência do FireBaseDataBase
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;


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
        recuperarResumo();

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Substitua por sua própria ação", Snackbar.LENGTH_LONG)
                        .setAction("Ação", null).show();
            }
        });*/
         // ------------------------------------- CalendarView --------------------------------------------------------------//
         //Por padrão esta configurado o calendário 200 anos antes e 200 anos depois
        //Para controlar isso segue o método da próxima linha
        /* calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2015, 1, 1)) //Definindo Data minima para calendário
                .setMaximumDate(CalendarDay.from(2018, (3 - 1), 1)) //Definindo Data máxima para calendário | subritrai o mês pois a data por padrão mostra um mês depois
                .commit();
         */

        //Método => Verifica quando navergar no calendário pega a data atual apenas navegando ex: 01/02/2018 de forma automática irá atualizar o mês navegado
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                //Log.i("data","valor: " + date); // mostra a data completa padrão inglês

                Log.i("data", "valor: " + (date.getMonth() + 1) + "/" + date.getYear());

            }
        });

    }

    //Recuperar Resumo da exibição dentro da ActivityPrincipal do FireBase
    public void recuperarResumo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail(); //Aqui Recupera o E-mail do usuário cadastrado
        //Recuperando Id do usuário em Base64 do Firebase por meio do E-mail
        String idUsuario = Base64Custom.codificarBase64(emailUsuario); // Aqui converte o e-mail em Base64 para poder acessar o usuario do FireBase
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario); //Acessa o Nó .child("usuarios") e em seguida Acessa o Id do usuário .child(idUsuario) por meio do E-mail codificado em Base64 para acessar os dados usuário no FireBase

        usuarioRef.addValueEventListener(new ValueEventListener() {
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
        switch (item.getItemId()){
            case R.id.menuSair: // Esse ID vem do XML menu_principal
                autenticacao.signOut(); //Deslogando
                startActivity(new Intent(this, LoginActivity.class)); //Após deslogar enviar para Tela de Login
                finish(); // e Finaliza a tela PrincipalActivity
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Método => Boão adicionarDespesa
    public void adicionarDespesa(View view){
        startActivity(new Intent(this, DespesaActivity.class));

    }

    //Método => Botão adicionarReceita
    public void adicionarReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));

    }

}
