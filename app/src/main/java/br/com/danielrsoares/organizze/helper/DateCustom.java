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

    //Separando a Data para pegar apenas o Mês e Ano ex: 02/2018 = 022018 para criar um Nó de Mês no FareBase
    public static String mesAnoDataEscolhida(String data){
        /*
        * O Método 'split' recebe um parâmetro para separar a Data ou seja ele irá quebrar a String Data em um valor que queremos passar
        * Ex: 23/01/2018 ficaria 23[0], 01[1], 2018[2] ou seja o split irá pegar o primeiro valor e adcionar no 1º índice o Arrey
        * depois o mês no 2º Array e assssim por diante
        *O split ele quebra conforme o caracter informado ex: 23a05b2018 usaria para quebrar split("a")
        * Ex: 23/01/2018 */
        String retornoData[] = data.split("/"); // Array de Strings
        String dia = retornoData[0]; //posição dia 23
        String mes = retornoData[1]; //posição mês 01
        String ano = retornoData[2]; //posição ano 2018
        String mesAno = mes + ano; // Resultado 012018 o meês e data serão juntos para criar um Nó de Mês e Ano no FireBase
        return mesAno;
    }
}
