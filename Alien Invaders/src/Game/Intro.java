package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Intro {

	boolean hasPlayed = false, shrinking = true;

	BufferedImage ship;
	BufferedImage title;
	BufferedImage earth;
	BufferedImage background;
	
	Color bg = Color.LIGHT_GRAY;

	Game window;

	int x = 100, y = -20;
	double scale = 18;
	double shrinkBy = 0.2;

	int earthX = -50, earthY = 300;
	double earthScale = 0.3;

	public Intro(Game window) {
		this.window = window;

		try {
			ship = ImageIO.read(getClass().getResourceAsStream(
					"/Alien3/Alien3.png"));
			title = ImageIO.read(getClass().getResourceAsStream("/Title.png"));
			earth = ImageIO.read(getClass().getResourceAsStream("/Earth.png"));
			background = ImageIO.read(getClass().getResourceAsStream("/Background.png"));
		} catch (Exception e) {
		}

		try {
			Thread.sleep(500);
			moveBack.start();
			reveal.start();
		} catch (Exception e) {
		}
	}

	Thread moveBack = new Thread(new Runnable() {
		public void run() {
			int width = ship.getWidth();
			int height = ship.getHeight();

			while (!hasPlayed) {

				double tempx = (width * scale) - (width * (scale - shrinkBy));
				double tempy = (height * scale) - (height * (scale - shrinkBy));

				x += tempx / 2;
				y += tempy;

				scale -= shrinkBy;

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (scale <= 0) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					hasPlayed = true;
				}
			}
			shrinking = false;
			window.menuScreen = true;
		}
	});

	Thread reveal = new Thread(new Runnable() {
		public void run() {
			while (!hasPlayed) {
				x -= 8;

				try {
					Thread.sleep(17);
				} catch (Exception e) {
				}
			}
		}
	});

	public void draw(Graphics g) {
		
		g.drawImage(background,0,0,window.getWidth(),window.getHeight(),null);

		g.drawImage(title, (window.getWidth() - title.getWidth()) / 2 - 100,
				(window.getHeight() - title.getHeight()) / 2 - 100,
				title.getWidth() * 5, title.getHeight() * 5, null);

		g.drawImage(earth, earthX, earthY,
				(int) (earth.getWidth() * earthScale),
				(int) (earth.getWidth() * earthScale), null);

	//	if (scale > 1)
			g.drawImage(ship, x, y, (int) (ship.getWidth() * scale),
					(int) (ship.getHeight() * scale), null);
	}
}
