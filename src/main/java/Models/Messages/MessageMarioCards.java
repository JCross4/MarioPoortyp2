package Models.Messages;

import Models.Juegos.MarioCardsJuego;

public class MessageMarioCards extends Message {
    private static final long serialVersionUID = 1L;

    private MarioCardsJuego juego;
    private String action; // "start", "carta", "result"
    private int cardIndex;
    private String cardInfo; // Información de la carta
    private String result; // Resultado del juego

    public MessageMarioCards(String tipo, String emisor, String destinatario, String descripcion,
            MarioCardsJuego juego, String action, int cardIndex, String cardInfo, String result) {
        super(tipo, emisor, destinatario, descripcion);
        this.juego = juego;
        this.action = action;
        this.cardIndex = cardIndex;
        this.cardInfo = cardInfo;
        this.result = result;
    }

    public MarioCardsJuego getJuego() {
        return juego;
    }

    public void setJuego(MarioCardsJuego juego) {
        this.juego = juego;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
