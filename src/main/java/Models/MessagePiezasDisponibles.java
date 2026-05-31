package Models;

public class MessagePiezasDisponibles extends Message{
    private Pieza[] piezasDisponibles;

    public MessagePiezasDisponibles(String tipo, String emisor, String receptor, String mensaje, Pieza[] piezasDisponibles) {
        super(tipo, emisor, receptor, mensaje);
        this.piezasDisponibles = piezasDisponibles;
        //TODO Auto-generated constructor stub
    }

    public Pieza[] getPiezasDisponibles() {
        return piezasDisponibles;
    }

    public void setPiezasDisponibles(Pieza[] piezasDisponibles) {
        this.piezasDisponibles = piezasDisponibles;
    }
    


}
