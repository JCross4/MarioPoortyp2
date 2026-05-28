package Models;

import java.io.Serializable;

public class Message implements Serializable{
    //NOTA enum
    public String tipo;
    public String emisor;
    public String receptor;
    public String mensaje;

    public Message(String tipo, String emisor, String receptor, String mensaje) {
        this.tipo = tipo;
        this.emisor = emisor;
        this.receptor = receptor;
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Message{" + "tipo=" + tipo + ", emisor=" + emisor + ", receptor=" + receptor + ", mensaje=" + mensaje + '}';
    }
}
