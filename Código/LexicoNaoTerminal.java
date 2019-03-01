/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author dheiso
 */
public class LexicoNaoTerminal {

    private String lexema = null;
    private String tipo = null;
    private String classe = null;

    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public String getClasse() {
        return classe;
    }

    

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    

    public void reset() {
        this.classe = null;
        this.lexema = null;
        this.tipo = null;
    }

    public void desserializar(String linha) {
        String dados[] = linha.split("#");
        
        this.lexema = dados[0];
        this.tipo = dados[1];
        this.classe = dados[2];

    }

}
