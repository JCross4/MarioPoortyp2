package GUI;

public class BoardFrame extends javax.swing.JFrame{

    private int casillasDibujadas;
    private final int anchoCasilla = 45;
    private final int altoCasilla = 37;

    
    public BoardFrame() {
    }


    public int getCasillasDibujadas() {
        return casillasDibujadas;
    }


    public void setCasillasDibujadas(int casillasDibujadas) {
        this.casillasDibujadas = casillasDibujadas;
    }


    public int getAnchoCasilla() {
        return anchoCasilla;
    }


    public int getAltoCasilla() {
        return altoCasilla;
    }



    

}
