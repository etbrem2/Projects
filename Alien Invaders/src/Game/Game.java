package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Game extends JFrame{

	boolean running = false;
	
	public Game(){
		super("Alien Invaders");
		
		addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Thread gameLoop = new Thread(new Runnable(){
			
			public void run() {
				running = true;
			
				while(running){
					
				}
			}
			
		});
		
		
		setVisible(true);
		setSize(600,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		gameLoop.start();
	}
}
