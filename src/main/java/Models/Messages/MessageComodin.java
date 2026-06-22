package Models.Messages;

public class MessageComodin extends Message{

    private String tipoComodin;
    private String objetivo;

    public MessageComodin(String tipo, String emisor, String receptor, String mensaje, String tipoComodin, String objetivo) {
        super(tipo, emisor, receptor, mensaje);
        this.tipoComodin = tipoComodin;
    }

    public String getTipoComodin() {
        return tipoComodin;
    }

    public void setTipoComodin(String tipoComodin) {
        this.tipoComodin = tipoComodin;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    

}
