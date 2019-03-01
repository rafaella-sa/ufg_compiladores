package compilador;

public class EstadoFinal {
	  
    private int estado;
    private String token;
    private String descricao;

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescrição() {
        return descricao;
    }

    public void setDescrição(String descrição) {
        this.descricao = descrição;
    }
    
    public void desserializar(String linha){
       String dados[] = linha.split("#");
       
       this.estado = Integer.parseInt(dados[0]);
       this.token = dados[1];
       this.descricao = dados[2];
       
       
    }
}