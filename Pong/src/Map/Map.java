package Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class Map {

	final boolean drawGoals = true;

	JPanel window;
	int goalSize = 300;
	public final int wallThickness = 3;
	public Rectangle goal1, goal2;

	public Rectangle top, bottom, left, right;

	public Map(JPanel p) {
		window = p;

		update();
	}

	public void update() {

		// like in original pong, the goal will be all of the side
		goalSize = window.getHeight();

		int x, y, width, height;
		x = 0;
		y = 0;

		width = window.getWidth();
		height = wallThickness;

		top = new Rectangle(x, y, width, height);

		y = window.getHeight() - height;

		bottom = new Rectangle(x, y, width, height);

		y = wallThickness;
		width = wallThickness;

		height = window.getHeight() - 2 * wallThickness;

		left = new Rectangle(x, y, width, height);

		x = window.getWidth() - width;

		right = new Rectangle(x, y, width, height);

		y = window.getHeight() / 2 - goalSize / 2;
		height = goalSize;

		goal1 = new Rectangle(x - width * 2, y, width * 3, height);

		x = 0;

		goal2 = new Rectangle(x, y, width * 3, height);
	}

	public void draw(Graphics g) {
		g.setColor(Color.white);

		g.drawRect(top.x, top.y, (int) top.getWidth(), (int) top.getHeight());
		g.drawRect(bottom.x, bottom.y, (int) bottom.getWidth(),
		        (int) bottom.getHeight());
		g.drawRect(left.x, left.y, (int) left.getWidth(),
		        (int) left.getHeight());
		g.drawRect(right.x, right.y, (int) right.getWidth(),
		        (int) right.getHeight());

		if (drawGoals) {
			g.setColor(Color.red);
			g.fillRect(goal1.x, goal1.y, (int) goal1.getWidth(),
			        (int) goal1.getHeight());
			g.fillRect(goal2.x, goal2.y, (int) goal2.getWidth(),
			        (int) goal2.getHeight());
		}
	}
}
