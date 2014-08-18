package Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Entity {

	int speed = 10;

	public boolean up, down;

	public Rectangle paddle;

	final public int sizeX = 10;
	final public int sizeY = 200;

	public Player() {
		super();

		display = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

		Graphics g = display.getGraphics();

		paddle = new Rectangle((int) vector.x, (int) vector.y, sizeX, sizeY);

		g.setColor(Color.white);
		g.fillRect(paddle.x, paddle.y, (int) paddle.getWidth(),
		        (int) paddle.getHeight());
	}

	public void move() {
		if (up)
			vector.speedY = -speed;
		else if (down)
			vector.speedY = speed;
		else
			vector.speedY = 0;

		if (up || down)
			vector.y += vector.speedY;

		paddle = new Rectangle((int) vector.x, (int) vector.y, sizeX, sizeY);
	}

}
