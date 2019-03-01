/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author rafaella
 */
public class Lexema {
    
    private String lexema = null;
    private String token = null;
    private String descrição = null;
    private String erro = null;

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
    
    
    
    public String getLexema() {
        return lexema;
    }


    public String getToken() {
        return token;
    }


    public String getDescrição() {
        return descrição;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDescrição(String descrição) {
        this.descrição = descrição;
    }
    
    
    
    public void reset(){
        this.descrição = null;
        this.lexema = null;
        this.token = null;
    }
    
    public String serializa(){
   
        return this.lexema + "#" + this.token + "#" + this.descrição;
    }

    
    public void desserializar(String linha){
       String dados[] = linha.split("#");
       //System.out.println(linha);
       this.lexema = dados[0];
       this.token = dados[1];
       this.descrição = dados[2];
        
    }
}
