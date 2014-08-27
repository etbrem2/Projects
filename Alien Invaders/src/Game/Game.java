package Game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Game extends JFrame {

	boolean running = false;
	boolean menuScreen = true;
	Menu menu;

	Game window;

	public Game() {
		super("Space Invaders");

		window = this;

		menu = new Menu(this);

		addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (menuScreen) {
					menu.keyPressed(e.getKeyCode());
				} else {

				}
			}

			public void keyReleased(KeyEvent e) {
				if (menuScreen) {
					menu.keyReleased(e.getKeyCode());
				} else {

				}
			}

			public void keyTyped(KeyEvent e) {
				if (menuScreen) {

				} else {

				}
			}
		});

		Thread gameLoop = new Thread(new Runnable() {

			public void run() {
				running = true;

				createBufferStrategy(3);
				while (running) {
					repaint();
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});

		setVisible(true);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		gameLoop.start();
	}

	public void paint(Graphics g1) {

		Graphics g = null;

		if (getBufferStrategy() != null)
			g = getBufferStrategy().getDrawGraphics();
		else
			return;

		if (menuScreen && menu.updated)
			menu.draw(g);

		getBufferStrategy().show();
	}

	public static void main(String[] args) {
		new Game();
	}
}
