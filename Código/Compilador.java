package compilador;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compilador {

    public static void main(String[] args) {
		// TODO Auto-generated method stub
        Sintatico s = new Sintatico();
        try {
            s.analisarSintaxe();
            
        }catch(LexicalException e){
            System.err.println(e.getMessage());
        }
        catch (IOException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
/*Lexico l = new Lexico();
            try {
            Lexema x = l.obterLexema();
            while(x != null){
            if(x.getErro() == null){
            System.out.println(x.getLexema());
            System.out.println(x.getToken());
            System.out.println(x.getDescrição());
            System.out.println("__________________________________");
            x = l.obterLexema();
            }else{
            System.out.println(x.getErro());
            x = l.obterLexema();
            }
            };
            } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }*/
            //System.out.println("char quebra linha:" + System.getProperty("line.separator") + ". length:" + System.getProperty("line.separator").length());