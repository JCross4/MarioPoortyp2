package Models;

public class MessagePieza extends Message{

    private int nuevaPosicion;
    private Pieza pieza;
    private String nombre;

    public MessagePieza(String emisor, String receptor, int posicion, Pieza pieza, String nombre) {
        super("pieza", emisor, receptor, "");
        this.nuevaPosicion = posicion;
        this.pieza = pieza;
        this.nombre = nombre;
        //TODO Auto-generated constructor stub
    }

    public int getNuevaPosicion() {
        return nuevaPosicion;
    }

    public void setNuevaPosicion(int nuevaPosicion) {
        this.nuevaPosicion = nuevaPosicion;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    


}
