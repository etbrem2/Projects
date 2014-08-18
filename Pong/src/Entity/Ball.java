package Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Ball extends Entity {

	int size = 10;
	public int maxSpeed = 8;
	double curveRate = 0.3;
	public boolean isCurving = false;

	public Ball() {
		super();

		vector.speedX = 5;
		vector.speedY = 3;

		display = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		Graphics g = display.getGraphics();

		g.setColor(Color.white);
		g.fillOval(0, 0, size, size);

	}

	public void curve(final double d) {
		isCurving = true;

		Thread curve = new Thread(new Runnable() {

			public void run() {
				boolean decided = false;
				boolean up = false;

				
				while (isCurving) {
					if (!decided) {
						if (vector.speedY > 0)
							up = true;
						decided = true;
					} else {
						if (up)
							vector.speedY -= curveRate * (d * 0.5);
						else
							vector.speedY += curveRate * (d * 0.5);
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		curve.start();
	}
}
