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
public class LexicalException extends Exception{

    /**
     *
     * @param e
     * @return
     */
    private String msg;
    public LexicalException(String msg){
        super(msg);
        this.msg = msg;
    }
    
    public String getMessage(){
        return msg;
    }
}
