package compilador;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class TabelaHash {
	//tabela hash como lista de listas
		private List<List<String>> tabela = new ArrayList<List<String>>();
		//tamanho total da lista
		private int tamanho = 0;
		
		//criacao da tabela
		public TabelaHash() {
			for (int i = 0; i < 26; i++) {
			      LinkedList<String> lista = new LinkedList<String>();
			      tabela.add(lista);
			    }
		}
		
		//devolve o tamanho da lista
		public int tamanho() {
		    return this.tamanho;
		}
		
		//calcula o indice da tabela com base nas letras do alfabeto
		private int calcularIndice(String palavra){
			  return palavra.toLowerCase().charAt(0) % 26;
		}
		
		//verificar se um símbolo já foi inserido
		public boolean contem(String simbolo) {
			  int indice = this.calcularIndice(simbolo);
			  List<String> lista = this.tabela.get(indice);
			  
			  return lista.contains(simbolo);
			}
		
		//adicionar simbolo na tabela
		public void adicionar(String simbolo) {
			  if (!this.contem(simbolo)) {
			    int indice = this.calcularIndice(simbolo);
			    List<String> lista = this.tabela.get(indice);
			    lista.add(simbolo);
			    this.tamanho++;
			  }
			}
		
		//remover símbolo da tabela
		public void remover(String simbolo) {
			  if (this.contem(simbolo)) {
			    int indice = this.calcularIndice(simbolo);
			    List<String> lista = this.tabela.get(indice);
			    lista.remove(simbolo);
			    this.tamanho--;
			  }
			}
		
		//recupera todos os elementos da tabela de símbolos
		public List<String> recuperarSimbolos() {
			  List<String> simbolos = new ArrayList<String>();

			  for (int i = 0; i < this.tabela.size(); i++) {
			    simbolos.addAll(this.tabela.get(i));
			  }

			  return simbolos;
			}
		
		//mostra todos os elementos na tabela de símbolos
		public void mostrarSimbolos() {
			List<String> simbolos = this.recuperarSimbolos();
			for (String simbolo : simbolos) {
			      System.out.println(simbolo);
			    }
		}
		
		//mostra um elemento
		public void mostrarSimbolo(String lexema) {
			List<String> simbolos = recuperarSimbolos();
			String dados;
	        String E_S[];
	        for (String simbolo: simbolos){
	        	E_S = simbolo.split("#");
	            if(E_S[0].equals(lexema))
	            	System.out.println(simbolo);
	            	break;
	        }
	}
}