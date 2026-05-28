package main;

import GUI.ClientFrame;
import GUI.ServerFrame;

/**
 * Hello world!
 *
 */
public class MarioPoorty 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ServerFrame sf = new ServerFrame();
        sf.setVisible(true);
        ClientFrame cf = new ClientFrame();
        cf.setVisible(true);
    }
}
