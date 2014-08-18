package Entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import Physics.Vector;

public class Entity {

	public Vector vector;
	public Rectangle up, down, left, right;

	BufferedImage display;

	public Entity(){
		vector = new Vector();
	}
	
	public void update() {
		double x, y;
		double width = 3, height = 5;

		x = vector.x + display.getWidth() / 2 - width / 2;
		y = vector.y;
		
		up = new Rectangle((int)x,(int)y,(int)width,(int)height);
		
		y = y + display.getHeight() - height;
		
		down = new Rectangle((int)x,(int)y,(int)width,(int)height);
		
		x = vector.x;
		y = vector.y + display.getHeight() /2 - width/2;
		
		left = new Rectangle((int)x,(int)y,(int)height,(int)width);
		
		x = x + display.getWidth() - height;
		
		right =  new Rectangle((int)x,(int)y,(int)height,(int)width);
	}

	public void move(){
		vector.x+=vector.speedX;
		vector.y+=vector.speedY;
	}
	
	public void draw(Graphics g) {
		g.drawImage(display, (int) vector.x, (int) vector.y, null);
	}
}
