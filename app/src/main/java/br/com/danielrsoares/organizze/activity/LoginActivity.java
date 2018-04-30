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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.danielrsoares.organizze.R;
import br.com.danielrsoares.organizze.config.ConfiguracaoFirebase;
import br.com.danielrsoares.organizze.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoEntrar = findViewById(R.id.buttonEntrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = campoEmail.getText().toString();
                String texSenha = campoSenha.getText().toString();

                //Validar se os campos foram preenchidos
                if (!textEmail.isEmpty()) {
                    if (!texSenha.isEmpty()) {

                        usuario = new Usuario();
                        usuario.setEmail(textEmail);
                        usuario.setSenha(texSenha);
                        validarLogin();

                    } else
                        Toast.makeText(LoginActivity.this,
                                "Preencha a Senha",
                                Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(LoginActivity.this,
                            "Preencha o E-mail",
                            Toast.LENGTH_SHORT).show();
            }

        });

    }

    //Método => Validar Login
    public void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Abrindo Tela Principal após Login
                   abrirTelaPrincipal();

                } else {

                    //Link https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth?authuser=0#exceptions_1
                    //Tratando exceção
                    String execao = "";
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e) { // Quando o e-mail do usuário não existe ou foi desabilitado
                        execao = "Usuário não esta cadastrado";

                    }catch (FirebaseAuthInvalidCredentialsException e){ //Quando o usuário digita E-mail ou Senha errada
                        execao = "Senha não corresponde ao Usuário cadastrado";

                    }catch (Exception e){ //Exceção Generica
                        execao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace(); //para printar essa exeção no nosso log
                    }
                    Toast.makeText(LoginActivity.this,
                            execao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Método => Verificar usuário Logado
    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioCadastrado();
    }

    //Método => Verificar usuário Logado
    public void verificarUsuarioCadastrado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut(); //Delogar Usuário =======
        if (autenticacao.getCurrentUser() != null){ //Verifica se usuário esta logado

            abrirTelaPrincipal();
        }
    }

    //Método => Abrindo Tela Principal após Logim do usuário
    public void abrirTelaPrincipal(){
        startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
        finish(); // finish() para fechar activity de login
    }

    public void btFazerCadastro(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
