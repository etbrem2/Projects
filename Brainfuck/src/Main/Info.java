package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class Info extends JFrame {

	Info window;
	GUI gui;
	String[] indexes;
	int xx,speed = 1;
	char currCmd;

	public Info(GUI g) {
		super("Info");

		window = this;

		gui = g;

		indexes = new String[gui.cell.length];

		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = "" + i;
		}

		setSize(600, 200);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(gui.getWidth(), 0);

		addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					xx-=speed;
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					xx+=speed;
				if(e.getKeyCode() == KeyEvent.VK_UP)
					speed++;
				if(e.getKeyCode()== KeyEvent.VK_DOWN)
					speed--;
				if(e.getKeyCode() == KeyEvent.VK_R)
					xx = 0;
				
				repaint();
			}

			public void keyReleased(KeyEvent e) {}
		});
	}

	public void setCmd(char c) {
		if (isCmd(c))
			currCmd = c;
	}

	public void finish() {
		currCmd = 'd';
	}

	public boolean isCmd(char c) {
		switch (c) {
			case '[':
			case '+':
			case '-':
			case '>':
			case '<':
			case ']':
			case '.':
			case ',':
				return true;
		}
		return false;
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, window.getWidth(), window.getHeight());

		int spacing = 3;
		int p = gui.p;
		int fs = g.getFont().getSize();
		int x = p * spacing * fs;
		int row1 = 100, row2 = 150;

		g.setColor(Color.black);

		String st = "";

		if (currCmd == 'd')
			st = "done";
		else
			st = currCmd + "";

		g.drawString("Current command: " + st, 20, 80);

		for (int i = 0; i < indexes.length; i++) {
			if (p == i)
				g.setColor(Color.orange);
			else
				g.setColor(Color.black);

			g.drawString(gui.cell[i] + "", (i + 2 - xx) * spacing * fs - x,
			        row1);
			g.drawString("("
			        + (gui.cell[i] == 10 ? "\\n" : (char) gui.cell[i] + "")
			        + ")", (i + 2 - xx) * spacing * fs - x, row1 + 20);
			g.drawString(indexes[i], (i + 2 - xx) * spacing * fs - x, row2);

		}
	}
}
