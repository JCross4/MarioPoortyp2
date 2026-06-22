package Models.Messages;

public class MessageRequest extends Message{
    private String tipoRequest;

    public MessageRequest(String tipo, String emisor, String receptor, String mensaje, String tipoRequest) {
        super(tipo, emisor, receptor, mensaje);
        this.tipoRequest = tipoRequest;
        //TODO Auto-generated constructor stub
    }

    public String getTipoRequest() {
        return tipoRequest;
    }

    public void setTipoRequest(String tipoRequest) {
        this.tipoRequest = tipoRequest;
    }
    
    
}
