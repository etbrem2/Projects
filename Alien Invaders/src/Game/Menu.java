package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.io.*;

import com.sun.glass.events.KeyEvent;

public class Menu {	
	
	
	Intro intro;
	int screen = 0;

	String[] options = { "Start", "Options", "Highscores", "Exit" };
	int selected = 0;

	Game window;
	boolean updated = true;

	public Menu(Game window) {
		this.window = window;
		intro = new Intro(window);
	}

	public void draw(Graphics g) {
		if(!intro.hasPlayed)
			intro.draw(g);
		else
			drawMainMenu(g);
	}

	public void drawMainMenu(Graphics g) {
		int spacing = 30;

		int width = window.getWidth();
		int height = window.getHeight();

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, width, height);

		int fontSize = g.getFont().getSize();

		for (int i = 0; i < options.length; i++) {
			if (i == selected)
				g.setColor(Color.red);
			else
				g.setColor(Color.yellow);

			g.drawString(options[i],
					window.getWidth() / 2 - (options[i].length() / 2)
							* fontSize / 2, window.getHeight() / 3 + i
							* spacing);
		}

		updated = false;
	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_UP)
			selected--;
		if (k == KeyEvent.VK_DOWN)
			selected++;

		if (selected < 0)
			selected = options.length - 1;
		if (selected >= options.length)
			selected = 0;

		if(k == KeyEvent.VK_R)
			intro = new Intro(window);
		
		updated = true;
	}

	public void keyReleased(int k) {
		updated = true;
	}
}
