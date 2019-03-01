package compilador;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Lexico {

    private final int TOTAL_SIMBOLOS = 22; //quantidade total de símbolos possíveis
    private final int ESTADOS = 21; //total de estados do AFD
    private String[] simbolos = new String[TOTAL_SIMBOLOS]; //tipos de símbolos possíveis
    private String[][] afd = new String[ESTADOS][TOTAL_SIMBOLOS]; //tabela do AFD
    private FileInputStream arquivoEntrada; //variável do arquivo de entrada
    private InputStreamReader leitorEntrada; //buffer de leitura do arquivo de entrada
    private int cont_linhas = 1; //contador de linhas lidas
    private int cont_colunas = 0; //contador de colunas lidas
    private int e_anterior = 0; //guarda o estado anterior
    private int e_atual = 0; //guarda o estado atual
    private ArrayList<EstadoFinal> estadosFinais = new ArrayList<>(); //lista de estados finais
    private TabelaHash tabela = new TabelaHash(); //tabela hash para guardar símbolos identificados
    private String lexema = ""; //variável que recebe o lexema identificado
    private Lexema e_lexema = new Lexema(); // estrutura que abriga as informações de um lexema ou um erro
    private int lido;
    
    public Lexico() {
        carregarSimbolos();
        carregarAFD();
        carregarEstadosFinais();
        carregarPalavrasChave();
        abrirArquivo();
    }

    //métodos get e set
    public int getCont_linhas() {
        return cont_linhas;
    }

    public void setCont_linhas(int cont_linhas) {
        this.cont_linhas = cont_linhas;
    }

    public int getCont_colunas() {
        return cont_colunas;
    }

    public void setCont_colunas(int cont_colunas) {
        this.cont_colunas = cont_colunas;
    }

    public int getLido() {
        return lido;
    }

    public void setLido(int lido) {
        this.lido = lido;
    }

    //carrega os símbolos possíveis
    private void carregarSimbolos() {
        try {
            FileReader arquivo = new FileReader("/home/dheiso/NetBeansProjects/Compilador/Arquivos/simbolos.txt");
            BufferedReader leitor = new BufferedReader(arquivo);
            String dados = leitor.readLine();
            String E_S[] = dados.split("#");
            for (int i = 0; i < TOTAL_SIMBOLOS; i++) {
                simbolos[i] = E_S[i];             
            }
            System.out.println("");
            leitor.close();
            arquivo.close();

        } catch (FileNotFoundException ex) {
            System.out.println("ERRO: Arquivo de simbolos nao encontrado.  \n");
        } catch (IOException ex) {
            System.out.println("ERRO: Arquivo de simbolos nao pode ser lido/escrito.  \n");
        }
    }

    //carrega o AFD do compilador
    private void carregarAFD() {
        try {
            FileReader arquivo = new FileReader("/home/dheiso/NetBeansProjects/Compilador/Arquivos/tabela.txt");
            BufferedReader leitor = new BufferedReader(arquivo);
            String dados;
            String E_S[];
            for (int i = 0; i < ESTADOS; i++) {
                dados = leitor.readLine();
                E_S = dados.split("#");
                for (int j = 0; j < TOTAL_SIMBOLOS; j++) {
                    afd[i][j] = E_S[j];
                }
                System.out.println("");
            }
            leitor.close();
            arquivo.close();
        } catch (FileNotFoundException ex) {
            System.out.println("ERRO: Arquivo da tabela do AFD nao encontrado.  \n");
        } catch (IOException ex) {
            System.out.println("ERRO: Arquivo da tabela do AFD nao pode ser lido/escrito.  \n");
        }
    }

    //carrega os estados finais para identificacao de lexemas
    private void carregarEstadosFinais() {
        FileReader arquivo;
        try {
            arquivo = new FileReader("/home/dheiso/NetBeansProjects/Compilador/Arquivos/estadosfinais.txt");
            BufferedReader leitor = new BufferedReader(arquivo);
            String linha = leitor.readLine();

            while (!(linha == null)) {
                EstadoFinal novo = new EstadoFinal();
                novo.desserializar(linha);
                estadosFinais.add(novo);
                linha = leitor.readLine();
            }
            leitor.close();
            arquivo.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERRO: Arquivo de estados finais nao encontrado.  \n");
        } catch (IOException e) {
            System.out.println("ERRO: Arquivo de estados finais nao pode ser lido/escrito.  \n");
        }
    }

    //carrega na tabela hash as palavras chave da linguagem
    private void carregarPalavrasChave() {
        tabela.adicionar("inicio#Palavra reservada#Delimita o início do programa");
        tabela.adicionar("varinicio#Palavra reservada#Delimita o início da declaração de variáveis");
        tabela.adicionar("varfim#Palavra reservada#Delimita o fim da declaração de variáveis");
        tabela.adicionar("escreva#Palavra reservada#Imprime na saída padrão");
        tabela.adicionar("leia#Palavra reservada#Lê da saída padrão");
        tabela.adicionar("se#Palavra reservada#Estrutura condicional");
        tabela.adicionar("entao#Palavra reservada#Elemento de estrutura condicional");
        tabela.adicionar("fimse#Palavra reservada#Elemento de estrutura condicional");
        tabela.adicionar("fim#Palavra reservada#Delimita o fim do programa");
        tabela.adicionar("inteiro#Palavra reservada#Tipo de dado");
        tabela.adicionar("literal#Palavra reservada#Tipo de dado");
        tabela.adicionar("real#Palavra reservada#Tipo de dado");
    }

    //abre arquivo a ser compilado
    private void abrirArquivo() {
        try {
            arquivoEntrada = new FileInputStream("/home/dheiso/NetBeansProjects/Compilador/Arquivos/fonte.alg");
            leitorEntrada = new InputStreamReader(arquivoEntrada);
        } catch (FileNotFoundException ex) {
            System.out.println("ERRO: Arquivo a ser compilado nao encontrado.  \n");
        }
    }

    //fecha arquivo a ser compilado
    private void FecharArquivo() {
        try {
            arquivoEntrada.close();
            leitorEntrada.close();
        } catch (IOException ex) {
            System.out.println("ERRO: Arquivo a ser compilado nao pode ser lido/escrito.  \n");
        }
    }

    //idenfica e devolve erro
    public String mostrarErro(String erro) {
        String msg = "";
        switch (erro.charAt(1)) {
            case '1': {
                msg = "ERRO: símbolo inesperado: ";
                if (erro.charAt(2) == 'l') {
                    msg = msg + "literal.";
                } else {
                    msg = msg + erro.charAt(2) + ".";
                }
                break;
            }
            case '2': {
                msg = "ERRO: número de ponto flutuante incompleto.";
                break;
            }
            case '3': {
                msg = "ERRO: valor numérico inconsistente.";
                break;
            }
            case '4': {
                msg = "ERRO: expoente nao encontrado.";
                break;
            }
            case '5': {
                msg = "ERRO: constante literal inconsistente";
                break;
            }
            case '6': {
                msg = "ERRO: comentário inconsistente";
                break;
            }
        }
        return msg + "Linha " + cont_linhas + ", coluna " + cont_colunas + ".";
    }

    //verifica se o estado é final
    public boolean isEstadoFinal(int e) {
        for (EstadoFinal estado : estadosFinais) {
            if (estado.getEstado() == e) {
                return true;
            }
        }
        return false;
    }

    //verifica se dado lexema é uma palavra chave
    public String testarPalavraChave(String lex) {
        switch (lex) {
            case "inicio": {
                return "#Palavra reservada#Delimita o início do programa";
            }
            case "varinicio": {
                return "#Palavra reservada#Delimita o início da declaração de variáveis";
            }
            case "varfim": {
                return "#Palavra reservada#Delimita o fim da declaração de variáveis";
            }
            case "escreva": {
                return "#Palavra reservada#Imprime na saída padrão";
            }
            case "leia": {
                return "#Palavra reservada#Lê da saída padrão";
            }
            case "se": {
                return "#Palavra reservada#Estrutura condicional";
            }
            case "entao": {
                return "#Palavra reservada#Elemento de estrutura condicional";
            }
            case "fimse": {
                return "#Palavra reservada#Elemento de estrutura condicional";
            }
            case "fim": {
                return "#Palavra reservada#Delimita o fim do programa";
            }
            case "inteiro": {
                return "#Palavra reservada#Tipo de dado";
            }
            case "literal": {
                return "#Palavra reservada#Tipo de dado";
            }
            case "real": {
                return "#Palavra reservada#Tipo de dado";
            }
        }
        return "";
    }

    //identificar lexema
    public String identificarLexema(int e) {
        for (EstadoFinal estado : estadosFinais) {
            if (estado.getEstado() == e) {
                return "#" + estado.getToken() + "#" + estado.getDescrição();
            }
        }
        return null;
    }

    //verificar se a transicao vai para um estado existente no AFD
    public boolean isTransicaoEstado(int transicao) {
        return (afd[e_atual][transicao].charAt(0) != '!' && afd[e_atual][transicao].charAt(0) != 'e');
    }

    //obtém o lexema
    public Lexema obterLexema() throws IOException, LexicalException {
        String l;
        int transicao = 0;
        boolean encontrado = false;
        int lido = arquivoEntrada.read();
        char c = (char) lido;
        cont_colunas++;
        while (lido != -1) {
            //ler as aspas
            if (lido == 226) {
                lido = lido + arquivoEntrada.read();
                lido = lido + arquivoEntrada.read();

                if (lido == 510) {
                    lido = 8220;
                } else if (lido == 511) {
                    lido = 8221;
                }
                c = (char) lido;
            }
            //fim da leitura de aspas
            if (Character.isLetter(c)) {
                c = 'L';
            } else if (Character.isDigit(c)) {
                c = 'D';
            } else if (System.getProperty("line.separator").charAt(0) == c) {
                c = 'N';
            } else if (c == '\t') {
                c = 'T';
            } 
            
            for (int i = 0; !encontrado && i < TOTAL_SIMBOLOS; i++) {
                if (c == simbolos[i].charAt(0)) {
                    transicao = i;
                    encontrado = true;
                }
            }
            if (!encontrado) { //se o símbolo nao pertencer ao conjunto de símbolos possíveis
                if (e_atual == 7 || e_atual == 10) { //verificar se o simbolo está inserido em um literal ou comentário
                    //se o símbolo pertence a um literal ou comentário, continuar
                    e_anterior = e_atual;
                    if (isTransicaoEstado(transicao)) {
                        e_atual = Integer.parseInt(afd[e_atual][transicao]);
                    }
                    lexema = lexema + (char) lido;
                    lido = arquivoEntrada.read();
                    c = (char) lido;
                    cont_colunas++;
                    encontrado = false;
                } else //senao, devolver mensagem de erro.
                {
                    e_lexema.reset();
                    e_lexema.setErro("ERRO: Símbolo nao pertencente à linguagem. Linha " + cont_linhas + ", coluna " + cont_colunas + ".");
                    return e_lexema;
                }
            } else //se o símbolo foi encontrado
            if (afd[e_atual][transicao].charAt(0) == 'e') { //se for um erro
                e_lexema.reset();
                e_lexema.setErro(mostrarErro(afd[e_atual][transicao]));
                return e_lexema; //mostrar o erro
            } else //se achou símbolo e nao deu erro
            if (afd[e_atual][transicao].charAt(0) == '!' | c == 'N' | c == 'T' | c == ' ') { //condicoes para verificar lexema
                if (isEstadoFinal(e_anterior) && c == 'N' | c == 'T' | c == ' ') { //verificar se é estado final
                    //identificar lexema e botar na tabela hash se for um identificador
                    String id = testarPalavraChave(lexema);
                    if (id != "") {
                        l = lexema + id;
                    } else {
                        l = lexema + identificarLexema(e_anterior);
                    }
                    if (e_anterior == 9) {
                        tabela.adicionar(l);
                    }
                    if (c == 'N' && e_anterior != 7 && e_anterior != 10) {//contar linha e continuar leitura
                        cont_linhas++;
                        cont_colunas = 0;
                    }
                    lexema = "";
                    e_anterior = 0;
                    e_atual = 0;
                    e_lexema.setErro(null);
                    e_lexema.desserializar(l);
                    return e_lexema;
                } else if (isEstadoFinal(e_atual) && afd[e_atual][transicao].charAt(0) == '!') {
                    //identificar lexema e botar na tabela hash se for um identificador
                    String id = testarPalavraChave(lexema);
                    if (id != "") {
                        l = lexema + id;
                    } else {
                        l = lexema + identificarLexema(e_atual);
                    }
                    if (e_atual == 9) {
                        tabela.adicionar(l);
                    }
                    if (c == 'N' && e_anterior != 7 && e_anterior != 10) {//contar linha e continuar leitura
                        cont_linhas++;
                        cont_colunas = 0;
                    }
                    lexema = "";
                    if (c != 'N' & c != 'T' & c != ' ') {
                        lexema += (char) lido;
                    }
                    cont_colunas++;
                    e_anterior = 0;
                    e_atual = 0;
                    if (isTransicaoEstado(transicao)) {
                        e_atual = Integer.parseInt(afd[e_atual][transicao]);
                    }
                    e_lexema.setErro(null);
                    e_lexema.desserializar(l);
                    return e_lexema;
                } else {//se nao for estado final, 
                    if (c == 'N' && e_anterior != 7 && e_anterior != 10) {//contar linha e continuar leitura
                        cont_linhas++;
                        cont_colunas = 0;
                    }
                    e_anterior = e_atual;
                    if (isTransicaoEstado(transicao)) {
                        e_atual = Integer.parseInt(afd[e_atual][transicao]);
                    }
                    lexema = lexema + (char) lido;
                    lido = arquivoEntrada.read();
                    c = (char) lido;
                    cont_colunas++;
                    encontrado = false;
                }
            } else { //se nao identificou lexema nem erro, continua leitura
                if (c == 'N' && e_anterior != 7 && e_anterior != 10) {//contar linha e continuar leitura
                    cont_linhas++;
                    cont_colunas = 0;
                }
                e_anterior = e_atual;
                if (isTransicaoEstado(transicao)) {
                    e_atual = Integer.parseInt(afd[e_atual][transicao]);
                }
                lexema = lexema + (char) lido;
                lido = arquivoEntrada.read();
                c = (char) lido;
                cont_colunas++;
                encontrado = false;
            }
        }
        if (lido == -1) { //verificar se algum lexema ficou incompleto no fim do arquivo
            switch (e_atual) { //se tiver ficado, retorna mensagem de erro
                case 2: { //float incompleto
                    e_lexema.reset();
                    e_lexema.setErro(mostrarErro("e2")); 
                    return e_lexema;
                }
                case 4: { //expoente nao encontrado
                    e_lexema.reset();
                    e_lexema.setErro(mostrarErro("e4"));
                    return e_lexema;
                }
                case 5: { //expoente nao encontrado
                    e_lexema.reset();
                    e_lexema.setErro(mostrarErro("e4"));
                    return e_lexema;
                }
                case 7: { //constante literal inconsistente
                    e_lexema.reset();
                    e_lexema.setErro(mostrarErro("e5"));
                    return e_lexema;
                }
                case 10: { //comentario inconsistente
                    e_lexema.reset();
                    e_lexema.setErro(mostrarErro("e6"));
                    return e_lexema;
                }
            }
        }
        return null;
    }
}