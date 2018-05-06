package br.com.danielrsoares.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    //Método => Retorna a Data Atual
    public static String dataAtual() {
        //Usando a propria data do Sistema
        long data = System.currentTimeMillis(); //Método retorna um número do tipo Long | Criando varável 'date' do tipo 'long' para receber esse método
        //Formatando um Padrão de Data | Link Info: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        //"d = Dia/M = Mês /yyyy = Ano hh = Hora :mm = Minutos :ss = Segundos" ex 06/05/2018 12:40 22s | Para zero a direita é só adicionar mais uma letra Ex dd = 01
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy hh:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }
}
