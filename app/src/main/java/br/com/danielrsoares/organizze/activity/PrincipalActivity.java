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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView saudacao, saldo;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze"); // Definindo Nome do Título da ActivityPrincipal
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        saudacao = findViewById(R.id.textSaudacao);
        saldo = findViewById(R.id.textSaldo);

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

    // == Exibindo MENU Toolbar - Sobreescrevendo o Método ===>
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu); // É um Objeto que Vei converter o XML em uma View
        return super.onCreateOptionsMenu(menu);
    }
    // == Botão DESLOGAR do App ===>
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair: // Esse ID vem do XML menu_principal
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
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
