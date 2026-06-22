package Models.Messages;

import Models.Tablero.Board;

public class MessageBoard extends Message{
    
    private Board tablero;

    public MessageBoard(String tipo, String emisor, String receptor, String mensaje, Board tablero) {
        super(tipo, emisor, receptor, mensaje);
        setTablero(tablero);
        //TODO Auto-generated constructor stub
    }

    public Board getTablero() {
        return tablero;
    }

    public void setTablero(Board tablero) {
        this.tablero = tablero;
    }
    
    
}
