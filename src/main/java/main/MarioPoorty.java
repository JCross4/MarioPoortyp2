package main;

import GUI.ClientFrame;
import GUI.ServerFrame;

/**
 * Hello world!
 *
 */
public class MarioPoorty 
{

    //TODOS: Minijuegos - Comodines -- quitar souts
    //1° Comodines
    public static void main( String[] args )
    {
        ServerFrame sf = new ServerFrame();
        sf.setVisible(true);
        ClientFrame cf = new ClientFrame();
        cf.setVisible(true);
    }
}
