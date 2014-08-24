package Entity;

import java.awt.image.BufferedImage;

import Physics.Hitbox;

public class Entity {
	public double x,y;
	double speedX,speedY;
	double accelerationX,accelerationY;
	double maxSpeedx,maxSpeedY;
	
	public Hitbox hitbox;
	
	public BufferedImage display;
	
	public boolean left,down,right,up;
	
	public Entity(){
		hitbox = new Hitbox(this);
	}
}
