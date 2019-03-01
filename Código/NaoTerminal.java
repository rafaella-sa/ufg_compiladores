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
public class NaoTerminal {

    private String lexema = null;
    private String tipo = null;
    private String classe = null;
   


    public String getLexema() {
        return lexema;
    }

    public String getToken() {
        return tipo;
    }

    public String getDescrição() {
        return classe;
    }

    public void reset() {
        this.classe = null;
        this.lexema = null;
        this.tipo = null;
    }

    public void desserializar(String linha) {
        String dados[] = linha.split("#");
        //System.out.println(linha);
        this.lexema = dados[0];
        this.tipo = dados[1];
        this.classe = dados[2];

    }

}
