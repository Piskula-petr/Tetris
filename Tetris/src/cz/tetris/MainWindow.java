package cz.tetris;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * 	Metoda Main
	 */
	public static void main(String[] args) {
		
    	new MainWindow().setVisible(true);
    }
	
	/**
	 * 	Hlavní okno
	 */
	public MainWindow(){
		
		// Nastavení parametrů
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        GameWindow gameWindow = new GameWindow();
        add(gameWindow);
        pack();
        
        // Nastaví okno na střed obrazovky
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        setLocation(screenSize.width / 2 - this.getSize().width / 2,
                    screenSize.height / 2 - this.getSize().height / 2);
    }
	
}
