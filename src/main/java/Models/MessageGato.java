package Models;

public class MessageGato extends Message {
    private String action;
    private String gameId;
    private int position;
    private String symbol;
    private String boardState;
    private String status;
    private GatoJuego juego;

    public MessageGato(String tipo, String emisor, String receptor, String mensaje, GatoJuego juego,
            String action, int position, String symbol, String boardState, String status) {
        super(tipo, emisor, receptor, mensaje);
        this.action = action;
        this.position = position;
        this.juego = juego;
        this.symbol = symbol;
        this.boardState = boardState;
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public String getGameId() {
        return gameId;
    }

    public int getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBoardState() {
        return boardState;
    }

    public String getStatus() {
        return status;
    }

    public GatoJuego getJuego() {
        return juego;
    }

    public void setJuego(GatoJuego juego) {
        this.juego = juego;
    }

    
}
