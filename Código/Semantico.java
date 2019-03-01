/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dheiso
 */
public class Semantico {

    private Stack<LexicoNaoTerminal> pilha = new Stack<>(); //pilha do compilador
    private ArrayList<LexicoNaoTerminal> identificadores = new ArrayList<>();
    private FileWriter arquivo;
    private BufferedWriter escreve;
    private int contador = -1;//variavel usada para contar a quantida de variaveis temporarias
    private int linhaErro = 0;
    
    public Semantico() {
        abrirArquivo();
    }

    //metodo responsavel por abrir o aquivo onde sera escrito o codigo objeto
    private void abrirArquivo() {
        try {
            arquivo = new FileWriter("/home/dheiso/NetBeansProjects/Compilador/Arquivos/auxiliar.txt");
            escreve = new BufferedWriter(arquivo);

        } catch (IOException ex) {
            System.out.println("Erro ao abrir arquivo para escrita " + ex.getLocalizedMessage());
        }

    }

    //metodo responsavel por fechar o arquivo onde esta escrito o codigo objeto ao final da analise semantica
    public void fecharArquivo() {
        try {
            escreve.close();
            arquivo.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar arquivo " + ex.getLocalizedMessage());
        }
    }

    //este metodo recebe um lexema ou um Não terminal de uma produção e empilha, se nescessario organiza alguns atributos internos do lexema
    public void empilha_L(Lexema arg1) {

        LexicoNaoTerminal item = new LexicoNaoTerminal();
        //conversão basica dos atributos para o padrão do código
        if (arg1.getLexema().equals("inteiro")) {
            arg1.setLexema("int");
            arg1.setToken(arg1.getLexema());
        }

        if (arg1.getLexema().equals("real")) {
            arg1.setLexema("Double");
            arg1.setToken(arg1.getLexema());
        }

        if (arg1.getLexema().equals("literal")) {
            arg1.setLexema("literal");
            arg1.setToken(arg1.getLexema());
        }

        if (arg1.getToken().equals("Literal")) {
            arg1.setToken("literal");
        }

        if (arg1.getToken().equals("Num")) {

            try {

                int t1 = Integer.parseInt(arg1.getLexema());
                arg1.setToken("int");

            } catch (Exception e) {
                arg1.setToken("Double");
            }

        }

        if (arg1.getToken().equals("RCB")) {
            arg1.setLexema("=");
        }
        
        //fim das conversões

        //se for um lexima especifico para a analise semanica vai para a pilha
        if (arg1.getToken().equals("id") || arg1.getToken().equals("OPR") || arg1.getToken().equals("OPM") || arg1.getToken().equals("Num") || arg1.getToken().equals("literal") || arg1.getToken().equals("int") || arg1.getToken().equals("Double") || arg1.getToken().equals("RCB")) {

            item.desserializar(arg1.serializa());

            pilha.push(item);
        }

    }

    //metodo responsavel por empilhar os não terminais e chamar a função que aplica as regras semanticas
    public void empilha_NT(String id, String NT,int linha) {
        
        this.linhaErro = linha;
        int NG = Integer.parseInt(id);
        LexicoNaoTerminal item = new LexicoNaoTerminal();
        item.setLexema(NT);

        switch (NG) {

            case 5:
                imprimir(NG);
                break;
            case 6:
                imprimir(NG);
                break;
            case 7:
                pilha.push(item);
                imprimir(NG);
                break;
            case 8:
                pilha.push(item);
                imprimir(NG);
                break;
            case 9:
                pilha.push(item);
                imprimir(NG);
                break;
            case 11:
                imprimir(NG);
                break;
            case 12:
                imprimir(NG);
                break;
            case 13:
                pilha.push(item);
                imprimir(NG);
                break;
            case 14:
                pilha.push(item);
                imprimir(NG);
                break;
            case 15:
                pilha.push(item);
                imprimir(NG);
                break;
            case 17:
                imprimir(NG);
                break;
            case 18:
                pilha.push(item);
                imprimir(NG);
                break;
            case 19:
                pilha.push(item);
                imprimir(NG);
                break;
            case 20:
                pilha.push(item);
                imprimir(NG);
                break;
            case 21:
                pilha.push(item);
                imprimir(NG);
                break;
            case 23:
                imprimir(NG);
                break;
            case 24:
                imprimir(NG);
                break;
            case 25:
                pilha.push(item);
                imprimir(NG);
                break;

        }

    }

    //insere os identificadores na lista para serem consultados posteriormente
    private boolean inserirNaLista(LexicoNaoTerminal item) {

        if (verificaItemNaLista(item)) {
            return true;
        } else {
            identificadores.add(item);
            return false;
        }

    }
    
    //verifica se um identificador esta na lista
    private boolean verificaItemNaLista(LexicoNaoTerminal item) {
        for (LexicoNaoTerminal item_L : identificadores) {
            if (item_L.getLexema().equals(item.getLexema())) {
                //System.out.println(item_L.getLexema());
                return true;
            }
        }
        return false;
    }
    
    //recupera um identificador da lista para obeter seus atributos
    private LexicoNaoTerminal recuperaItemNaLista(LexicoNaoTerminal item) {
        for (LexicoNaoTerminal item_L : identificadores) {
            if (item.getLexema().equals(item_L.getLexema())) {
                return item_L;
            }
        }
        return null;
    }
    
    //gera o programa objeto inserindo código padrão e o código gerado pela aplicação das regras semanticas
    public void gerarObjeto() {

        fecharArquivo();

        try {
            FileReader arquivo = new FileReader("/home/dheiso/NetBeansProjects/Compilador/Arquivos/auxiliar.txt");
            BufferedReader leitor = new BufferedReader(arquivo);

            FileWriter arquivo1 = new FileWriter("/home/dheiso/NetBeansProjects/Compilador/Arquivos/programa.c");
            BufferedWriter escritor = new BufferedWriter(arquivo1);

            escritor.write("#include<stdio.h>\n");
            escritor.write("\n");
            escritor.write("typedef char literal[256];\n");
            escritor.write("void main (void)\n");
            escritor.write("{\n");

            escritor.write("/*----Variaveis temporarias----*/\n");
            for (int i = 0; i < contador; i++) {
                escritor.write("int T" + i + "\n");
            }
            escritor.write("/*------------------------------*/\n");

            String linha = leitor.readLine();
            while (linha != null) {

                escritor.write(linha + "\n");

                linha = leitor.readLine();
            }

            escritor.write("}\n");

            leitor.close();
            arquivo.close();
            escritor.close();
            arquivo1.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao abrir arquivo para leitura" + ex.getLocalizedMessage());
            System.exit(-1);
        } catch (IOException ex) {
            System.out.println("Erro ao abrir arquivo para escrita" + ex.getLocalizedMessage());
            System.exit(-1);
        }

    }

    //função que imprime as regras semanticas
    public void imprimir(int id) {

        LexicoNaoTerminal LNT1, LNT2, LNT3, LNT4; // variaveis usadas para guarda os valores dos lexicos ou dos não terminais passados como parametros

        switch (id) {


            case 5://imprime três linhas em branco no arquivo

                try {
                    for (int i = 0; i < 3; i++) {
                        escreve.write("\n");
                    }
                } catch (IOException ex) {
                    System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());
                    System.exit(-1);
                }

                break;

            case 6://regra semântica de numero 6

                LNT2 = pilha.pop();//recebe TIPO como parametro
                LNT1 = pilha.pop();//recebe id como parametro

                LNT1.setTipo(LNT2.getTipo());// id.tipo <-- TIPO.tipo

                if (inserirNaLista(LNT1)) {

                    System.out.println("Erro linha " + this.linhaErro + ": variavel já declarada");
                    System.exit(-1);
                } else {

                    {
                        try {
                            escreve.write(LNT2.getTipo() + " " + LNT1.getLexema() + "\n");//escreve no arquivo TIPO.tipo id.lexema
                        } catch (IOException ex) {
                            System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());
                            System.exit(-1);
                        }
                    }
                }
                break;

            case 7:

                LNT2 = pilha.pop();//recebe TIPO como parametro

                LNT1 = pilha.pop();//recebe inteiro como parametro

                LNT2.setTipo(LNT1.getTipo());// TIPO.tipo <-- inteiro.tipo

                pilha.push(LNT2);

                break;

            case 8:

                LNT2 = pilha.pop();//recebe TIPO como parametro

                LNT1 = pilha.pop();//recebe real como parametro

                LNT2.setTipo(LNT1.getTipo());// TIPO.tipo <-- inteiro.tipo

                pilha.push(LNT2);
                break;

            case 9:

                LNT2 = pilha.pop();//recebe TIPO como parametro

                LNT1 = pilha.pop();//recebe literal como parametro

                LNT2.setTipo(LNT1.getTipo());// TIPO.tipo <-- inteiro.tipo

                pilha.push(LNT2);
                break;

            case 10:
                break;

            case 11:
                LNT1 = pilha.pop();//recebe id como parametro

                if (verificaItemNaLista(LNT1)) {//verifica se o campo tipo do identificador está preenchido indicando a declaração do identificador

                    LNT1 = recuperaItemNaLista(LNT1);

                    try {

                        if (LNT1.getTipo().equals("literal")) {//se for literal imprime  no arquivo scanf(“%s”, id.lexema);
                            escreve.write("scanf(“%s”," + LNT1.getLexema() + ");\n");
                        }

                        if (LNT1.getTipo().equals("int")) {//se for inteiro imprime no arquivo scanf(“%d”, &id.lexema);
                            escreve.write("scanf(“%d”,&" + LNT1.getLexema() + ");\n");
                        }

                        if (LNT1.getTipo().equals("Double")) {//se for real imprime no arquivo scanf(“%lf”, &id.lexema);
                            escreve.write("scanf(“%lf”,&" + LNT1.getLexema() + ");\n");
                        }

                    } catch (IOException ex) {
                        System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());
                        System.exit(-1);
                    }
                }

                break;

            case 12:
                LNT1 = pilha.pop();//recebe ARG como parametro
                 {
                    try {
                        if (verificaItemNaLista(LNT1)) {

                            if (LNT1.getTipo().equals("literal")) {//se for literal imprime  no arquivo scanf(“%s”, id.lexema);
                                escreve.write("printf(“%s”," + LNT1.getLexema() + ");\n");
                            }

                            if (LNT1.getTipo().equals("int")) {//se for inteiro imprime no arquivo scanf(“%d”, &id.lexema);
                                escreve.write("printf(“%d”," + LNT1.getLexema() + ");\n");
                            }

                            if (LNT1.getTipo().equals("Double")) {//se for real imprime no arquivo scanf(“%lf”, &id.lexema);
                                escreve.write("printf(“%lf”," + LNT1.getLexema() + ");\n");
                            }

                        } else {
                            escreve.write("printf(" + LNT1.getLexema() + ");\n");
                        }

                    } catch (IOException ex) {
                        System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());
                        System.exit(-1);
                    }
                }

                break;

            case 13://copia todos os atributos de literal para ARG

                LNT2 = pilha.pop();//recebeu ARG como argumento
                LNT1 = pilha.pop();//recebeu literal como arumento

                LNT2.setClasse(LNT1.getClasse());
                LNT2.setLexema(LNT1.getLexema());
                LNT2.setTipo(LNT1.getTipo());

                pilha.push(LNT2);

                break;

            case 14://copia todos os atributos de num para ARG

                LNT2 = pilha.pop();//recebeu ARG como argumento 
                LNT1 = pilha.pop();//recebeu num como arumento

                LNT2.setClasse(LNT1.getClasse());
                LNT2.setLexema(LNT1.getLexema());
                LNT2.setTipo(LNT1.getTipo());

                pilha.push(LNT2);

                break;

            case 15://copia todos os atributos de id para ARG
                LNT1 = pilha.pop();//recebeu ARG como arumento
                LNT2 = pilha.pop();//recebeu id como argumento

                if (verificaItemNaLista(LNT2)) {

                    LNT2 = recuperaItemNaLista(LNT2);
                    LNT1.setClasse(LNT2.getClasse());
                    LNT1.setLexema(LNT2.getLexema());
                    LNT1.setTipo(LNT2.getTipo());

                    pilha.push(LNT1);

                } else {

                    System.out.println("Erro linha " + this.linhaErro + ": variavel não declarada");
                    System.exit(-1);
                }
                break;

            case 17:
                LNT3 = pilha.pop();//recebe LD como parametro
                LNT2 = pilha.pop();//recebe rcb como parametro
                LNT1 = pilha.pop();//recebe id como parametro

                if (verificaItemNaLista(LNT1)) {//verifica se id foi declarado
                    LNT1 = recuperaItemNaLista(LNT1);
                    if (LNT1.getTipo().equals(LNT3.getTipo())) {//verificação de tipo entre id e LD

                        try {
                            escreve.write(LNT1.getLexema() +  LNT2.getLexema() +  LNT3.getLexema() + "\n");
                        } catch (IOException ex) {
                            System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());
                            System.exit(-1);
                        }

                    } else {

                        System.out.println("Erro linha " + this.linhaErro + ": tipos diferentes para atribuição");
                        System.exit(-1);
                    }

                } else {

                    System.out.println("Erro linha " + this.linhaErro + ": variavel não declarada");
                    System.exit(-1);
                }

                break;

            case 18:
                LNT1 = pilha.pop();//recebe LD
                LNT2 = pilha.pop();//recebe OPRD
                LNT3 = pilha.pop();//recebe opm
                LNT4 = pilha.pop();//recebe OPRD

                if (!(LNT2.getTipo().equals("literal")) && !(LNT4.getTipo().equals("literal"))) {

                    this.contador++;
                    try {
                        LNT1.setLexema("T" + this.contador);

                        if (LNT2.getTipo().equals("real") || LNT4.getTipo().equals("real")) {
                            LNT1.setTipo("Double");
                        } else {
                            LNT1.setTipo("int");
                        }

                        LNT1.setClasse(LNT2.getClasse());

                        pilha.push(LNT1);
                        escreve.write("T" + this.contador + "=" + LNT2.getLexema() + LNT3.getLexema() + LNT4.getLexema() + "\n");
                    } catch (IOException ex) {
                        System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());
                        System.exit(-1);
                    }

                } else {

                    System.out.println("Erro linha " + this.linhaErro + ": operadores com tipos incompativeis");
                    System.exit(-1);
                }

                break;

            case 19:
                LNT1 = pilha.pop();//recebe LD como parametro
                LNT2 = pilha.pop();//recebe OPRD como parametro

                LNT1.setClasse(LNT2.getClasse());
                LNT1.setLexema(LNT2.getLexema());
                LNT1.setTipo(LNT2.getTipo());

                pilha.push(LNT1);

                break;

            case 20:
                LNT1 = pilha.pop();//recebe OPRD como parametro
                LNT2 = pilha.pop();//recebe id como parametro

                if (verificaItemNaLista(LNT2)) {//copia todos os atributos de id para OPRD

                    LNT2 = recuperaItemNaLista(LNT2);

                    LNT1.setClasse(LNT2.getClasse());
                    LNT1.setLexema(LNT2.getLexema());
                    LNT1.setTipo(LNT2.getTipo());

                    pilha.push(LNT1);

                } else {

                    System.out.println("Erro linha " + this.linhaErro + ": variavel não declarada");
                    System.exit(-1);
                }

                break;

            case 21:
                LNT1 = pilha.pop();//recebe OPRD como parametro
                LNT2 = pilha.pop();//recebe num como parametro

                LNT1.setClasse(LNT2.getClasse());
                LNT1.setLexema(LNT2.getLexema());
                LNT1.setTipo(LNT2.getTipo());

                pilha.push(LNT1);

                break;

            case 23: {
                try {
                    escreve.write("}\n");
                } catch (IOException ex) {
                    System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());

                }
            }

            break;

            case 24:
                LNT1 = pilha.pop();
                 {
                    try {
                        escreve.write("if(" + LNT1.getLexema() + "){\n");
                    } catch (IOException ex) {
                        System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());

                    }
                }

                break;
            case 25:

                LNT1 = pilha.pop();//recebe EXP_R
                LNT2 = pilha.pop();//recebe OPRD
                LNT3 = pilha.pop();//recebe opr
                LNT4 = pilha.pop();//recebe OPRD

                if (!(LNT2.getTipo().equals("literal")) && !(LNT4.getTipo().equals("literal"))) {

                    this.contador++;
                    try {
                        LNT1.setLexema("T" + this.contador);

                        pilha.push(LNT1);
                        escreve.write("T" + this.contador + "=" + LNT4.getLexema() + LNT3.getLexema() + LNT2.getLexema() + "\n");
                    } catch (IOException ex) {
                        System.out.println("Erro ao escrever no arquivo " + ex.getLocalizedMessage());
                        System.exit(-1);
                    }

                } else {

                    System.out.println("Errolinha " + this.linhaErro + ": operadores com tipos incompativeis");
                    System.exit(-1);
                }

                break;


        }

    }

}
