package compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Sintatico {
    private Stack<String> pilha = new Stack<>(); //pilha do compilador
    private final int ESTADOS = 60; //total de estados do autômato
    private final int ACOES_TRANSICOES = 39; //numero total de acoes e transicoes possiveis
    private String[][] automato = new String[ESTADOS][ACOES_TRANSICOES]; //autômato
    private final int REGRAS = 30; //número de regras da gramática
    private final int ELEM_REGRAS = 4; //número de elementos para manipulação da regra
    private String[][] regras = new String[REGRAS][ELEM_REGRAS]; //conjunto de regras
    private final int TOTAL_ERROS = 31; //quantidade de erros que podem ser gerados
    private final int ELEM_ERROS = 2; //número de elementos para manipulação de erros
    private String[][] erros = new String[TOTAL_ERROS][ELEM_ERROS];
    private Lexico l = new Lexico();
    private Lexema dados;
    private String acao;
    private Semantico semantico = new Semantico();
    
    public Sintatico(){
        carregarTabela();
        carregarRegras();
        carregarErros();
    }
    
    private void carregarTabela(){
        try {
	            FileReader arquivo = new FileReader("/home/dheiso/NetBeansProjects/Compilador/Arquivos/tabela_sintatica.txt");
	            BufferedReader leitor = new BufferedReader(arquivo);
	            String dados;
                    String E_S[];
	            for (int i = 0; i < ESTADOS; i++){
	                dados = leitor.readLine();
	                E_S = dados.split(",");
	                for (int j = 0; j < ACOES_TRANSICOES; j++) {
	                    automato[i][j] = E_S[j];
	                }
	            }
	            leitor.close();
	            arquivo.close();

	        } catch (FileNotFoundException ex) {
	            System.out.println("ERRO: Arquivo do autômato da gramática nao encontrado.  \n");
	        } catch (IOException ex) {
	        	System.out.println("ERRO: Arquivo do autômato da gramática nao pode ser lido/escrito.  \n");
	        }
    }
    
    private void carregarRegras(){
        try {
	            FileReader arquivo = new FileReader("/home/dheiso/NetBeansProjects/Compilador/Arquivos/regras.txt");
	            BufferedReader leitor = new BufferedReader(arquivo);
	            String dados;
                String E_S[];
	            for (int i = 0; i < REGRAS; i++){
	                dados = leitor.readLine();
	                E_S = dados.split(",");
	                for (int j = 0; j < ELEM_REGRAS; j++) {
	                    regras[i][j] = E_S[j];
	                }
	            }
	            leitor.close();
	            arquivo.close();

	        } catch (FileNotFoundException ex) {
	            System.out.println("ERRO: Arquivo de regras da gramática nao encontrado.  \n");
	        } catch (IOException ex) {
	        	System.out.println("ERRO: Arquivo de regras da gramática nao pode ser lido/escrito.  \n");
	        }
    }
    
    private void carregarErros(){
        try {
	            FileReader arquivo = new FileReader("/home/dheiso/NetBeansProjects/Compilador/Arquivos/erros.txt");
	            BufferedReader leitor = new BufferedReader(arquivo);
	            String dados;
                    String E_S[];
	            for (int i = 0; i < TOTAL_ERROS; i++){
	                dados = leitor.readLine();
	                E_S = dados.split("#");
	                for (int j = 0; j < ELEM_ERROS; j++) {
	                    erros[i][j] = E_S[j];
	                }
	            }
	            leitor.close();
	            arquivo.close();

	        } catch (FileNotFoundException ex) {
	            System.out.println("ERRO: Arquivo de erros da gramática nao encontrado.  \n");
	        } catch (IOException ex) {
	        	System.out.println("ERRO: Arquivo de erros da gramática nao pode ser lido/escrito.  \n");
	        }
    }
    
    //devolve estado de shift ou regra reduce
    private String obterER(String acao){ 
        String er = acao.substring(1);
        return er;
    }
    
    //obtém numero de elementos do beta da regra
    private int obterTamanhoBeta(String regra){ 
        for(int i = 0; i < REGRAS;  i++){
            if(regras[i][0].equals(regra)){
                return Integer.parseInt(regras[i][1]);
            }
        }
        return 0;
    }
    
    private void imprimirRegra(String acao){
        String regra = obterER(acao);
        for(int i = 0; i < REGRAS; i++){
            if(regras[i][0].equals(regra)){
                System.out.println("PRODUÇÃO " + regra + ": " + regras[i][2]
                        + " -> " + regras[i][3]);
                break;
            }
        }
    }

    private int retornarIndice(String token){
        String[] vetor = new String[10];
        vetor[0] = "id";
        vetor[1] = "RCB";
        vetor[2] = "OPR";
        vetor[3] = "OPM";
        vetor[4] = "Num";
        vetor[5] = "literal";
        vetor[6] = "AB_P";
        vetor[7] = "FC_P";
        vetor[8] = "PT_V";
        vetor[9] = "inteiro";
        for(int i = 0; i < 10; i++){
            if(vetor[i].equals(token))
                return i;
        }
        return -1;
    }
    
    private String classificarLexema(Lexema lex){
        if(lex == null)
            return "$";
        String token = lex.getToken();
        
        if(lex.getToken().equals("Palavra reservada")){
            token = lex.getLexema();
        }
        else if(lex.getToken().equals("Literal"))
            token = "literal";
        switch(retornarIndice(token)){
            case 0: { //0
                return "id";
            }
            case 1:{
                return "rcb";
            }
            case 2:{
                return "opr";
            }
            case 3:{
                return "opm";
            }
            case 4: {
                return "num";
            }
            case 5: {
                    if(acao.equals("s18"))
                        return "lit";
                    else if(acao.equals("s11"))
                        return "literal";
            }
            case 6:{
                return "(";
            }
            case 7:{
                return ")";
            }
            case 8:{
                return ";";
            }
            case 9:{
                return "int";
            }
            default:{
                return lex.getLexema();
            }
        }
    }
    
    private String obterAcao(String lexema, String estado){
        for(int i = 1; i < ESTADOS; i++){
            if(automato[i][0].equals(estado)){
                for(int j = 1; j < ACOES_TRANSICOES; j++)
                    if(automato[0][j].equals(lexema)){ 
                        return automato[i][j];
                    }                   
            }
        }
        return null;
    }
    
    private String obterNaoTerminal(String regra){
        for(int i = 0; i < REGRAS; i++){
            if(regras[i][0].equals(regra)){
                return regras[i][2];
            }
        }
        return null;
    }
    
    public void mostrarErro(String acao){
        for(int i = 0; i < TOTAL_ERROS; i++){
            if(acao.equals(erros[i][0]))
                System.out.println(erros[i][1] + ". Linha " + l.getCont_linhas() + ", coluna " + l.getCont_colunas() + ".");
        }
    }
    
    public void analisarSintaxe() throws IOException, LexicalException{
        String lexema, naoterminal, topo;
        boolean erro = false;
        dados = new Lexema();
        dados = l.obterLexema();
        semantico.empilha_L(dados);//para analise semantica
        if(dados != null && dados.getErro() != null)
            throw new LexicalException(dados.getErro());
        else
            lexema = classificarLexema(dados);
        pilha.push("00");
       // System.out.println("Insere na pilha: " + pilha.peek()); 
        while(!erro){
                acao = obterAcao(lexema, pilha.peek());
               // System.out.println("Ação: " + acao);
                if(acao.charAt(0) == 's'){ //se for shift
                    pilha.push(lexema); //empilhar estado e lexema e ler novo
                   // System.out.println("Insere na pilha: " + pilha.peek());
                    semantico.empilha_L(dados);//para analise semantica
                    pilha.push(obterER(acao));
                   // System.out.println("Insere na pilha: " + pilha.peek());
                    dados = new Lexema();
                    dados = l.obterLexema();
                 
                   
                    if(dados != null && dados.getErro() != null)
                        throw new LexicalException(dados.getErro());
                    else
                        lexema = classificarLexema(dados);
                       
                }
                else{
                   
                    if(acao.charAt(0) == 'r'){ //se for reduce
                      
                       // imprimirRegra(acao);
                        for(int i = 1; i <= 2 * obterTamanhoBeta(obterER(acao)); i++){
                        //    System.out.println("Remove da pilha: " + pilha.peek());
                            pilha.pop();
                        }
                        naoterminal = obterNaoTerminal(obterER(acao));
                      
                        topo = pilha.peek();
                        pilha.push(naoterminal);
                      //  System.out.println("Insere na pilha: " + pilha.peek());
                        pilha.push(obterER(obterAcao(naoterminal, topo))); 
                      //  System.out.println("Insere na pilha: " + pilha.peek());
                     
                        semantico.empilha_NT(obterER(acao), naoterminal,l.getCont_linhas());//para analise semantica
                    }
                    else{
                        if(acao.equals("ACC")){
                            semantico.gerarObjeto();
                            return;
                        }else{
                            erro = true;
                            mostrarErro(acao);
                            return;
                        }
                    }
                }
            }
    }
}