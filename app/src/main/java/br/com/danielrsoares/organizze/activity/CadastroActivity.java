package br.com.danielrsoares.organizze.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;
import br.com.danielrsoares.organizze.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

            campoNome = findViewById(R.id.editNome);
            campoEmail = findViewById(R.id.editEmail);
            campoSenha = findViewById(R.id.editSenha);
            botaoCadastrar = findViewById(R.id.buttonCadastrar);


            //Método Botão Cadastrar
            botaoCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String textNome = campoNome.getText().toString();
                    String textEmail = campoEmail.getText().toString();
                    String texSenha = campoSenha.getText().toString();

                    //Verificando se Campos foram Preenchidos
                    if (!textNome.isEmpty()){ // ! é para negar o retorno do isEmpty()
                        if (!textEmail.isEmpty()){
                            if (!texSenha.isEmpty()){

                                usuario = new Usuario();
                                usuario.setNome(textNome);
                                usuario.setEmail(textEmail);
                                usuario.setSenha(texSenha);
                                cadastrarUsuario();//Método cadastrar usuário

                            }else
                                Toast.makeText(CadastroActivity.this,
                                        "Preencha a Senha",
                                        Toast.LENGTH_SHORT).show();

                        }else
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha o E-mail",
                                    Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CadastroActivity.this,
                                "Preencha o Nome",
                                Toast.LENGTH_SHORT).show();
                    }

                }
                //Método  Statico Cadastrar Usuário
                public void cadastrarUsuario(){
                    autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao(); //Objeto Autnticação > Pata Config > ConfiguracaoFirebase
                    autenticacao.createUserWithEmailAndPassword(
                            usuario.getEmail(), usuario.getSenha()
                    ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {//Validando os Dados
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                finish(); //Fechar tela de cadastro após cadastrado com sucesso

                            }else {

                                //Link https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth?authuser=0#exceptions_1
                                //Tratando exceção
                                String execao = "";
                                try {
                                     throw task.getException();
                                }catch (FirebaseAuthWeakPasswordException e){ //Informa se a senha não for forte o suficiente
                                    execao = "Digite uma senha mais forte";
                                }catch (FirebaseAuthInvalidCredentialsException e){ //Informa se o endereço de email estiver mal formado
                                    execao = "Por favor, digite um E-mail válido";
                                }catch (FirebaseAuthUserCollisionException e){ //Informa se já existir uma conta com o endereço e-mail já cadastrado
                                    execao = "E-mail já cadastrado";
                                }catch (Exception e){ //Exceção Generica
                                    execao = "Erro ao cadastrar usuário: " + e.getMessage();
                                    e.printStackTrace(); //para printar essa exeção no nosso log
                                }

                                Toast.makeText(CadastroActivity.this,
                                        execao,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

        }

}
