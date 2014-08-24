package Physics;

import Entity.Entity;
import com.sun.javafx.geom.Rectangle;

public class Hitbox {

	Entity entity;
	
	Rectangle[] hitbox = new Rectangle[4];

	final int LEFT = 0;
	final int UP = 1;
	final int DOWN = 2;
	final int RIGHT = 3;

	boolean hasLeft, hasDown, hasRight, hasUp;
	
	public Hitbox(Entity ent){
		entity = ent;
	}
	
	public Rectangle left(){
		return hitbox[LEFT];
	}
	public Rectangle right(){
		return hitbox[RIGHT];
	}
	public Rectangle up(){
		return hitbox[UP];
	}
	public Rectangle down(){
		return hitbox[DOWN];
	}
	
	public void update(){
		
		double x, y;
		double width = 3, height = 5;

		x = entity.x + entity.display.getWidth() / 2 - width / 2;
		y = entity.y;
		
		hitbox[UP] = new Rectangle((int)x,(int)y,(int)width,(int)height);
		
		y = y + entity.display.getHeight() - height;
		
		hitbox[DOWN] = new Rectangle((int)x,(int)y,(int)width,(int)height);
		
		x = entity.x;
		y = entity.y + entity.display.getHeight() /2 - width/2;
		
		hitbox[LEFT] = new Rectangle((int)x,(int)y,(int)height,(int)width);
		
		x = x + entity.display.getWidth() - height;
		
		hitbox[RIGHT] =  new Rectangle((int)x,(int)y,(int)height,(int)width);
	}
}
