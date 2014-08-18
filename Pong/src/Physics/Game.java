package Physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sun.audio.AudioStream;
import Entity.*;
import Map.Map;

public class Game extends JFrame {

	Game window;

	Ball ball;
	Player p1, p2;
	Map map;

	int score1, score2;
	long lastPoint = 0;
	double friction = 0.8;
	boolean stopCurve = false;

	DrawThingy panel;
	Clip sound;

	boolean running = false;

	public Game() {
		super("Pong");

		window = this;

		panel = new DrawThingy();

		p1 = new Player();
		p2 = new Player();

		ball = new Ball();

		ball.vector.x = 400;
		ball.vector.y = 300;

		map = new Map(panel);

		setVisible(true);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		add(panel);
		addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						p1.up = true;
						p1.down = false;
						break;
					case KeyEvent.VK_DOWN:
						p1.down = true;
						p1.up = false;
						break;
					case KeyEvent.VK_W:
						p2.up = true;
						p2.down = false;
						break;
					case KeyEvent.VK_S:
						p2.down = true;
						p2.up = false;
						break;
					case KeyEvent.VK_RIGHT:
						sound.setMicrosecondPosition(sound
						        .getMicrosecondPosition() + 1000000);
						break;
					case KeyEvent.VK_LEFT:
						sound.setMicrosecondPosition(sound
						        .getMicrosecondPosition() - 1000000 * 2);
						break;
				}
			}

			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						p1.up = false;
						break;
					case KeyEvent.VK_DOWN:
						p1.down = false;
						break;
					case KeyEvent.VK_W:
						p2.up = false;
						break;
					case KeyEvent.VK_S:
						p2.down = false;
						break;
					case KeyEvent.VK_SPACE:
						ball.vector.x = window.getWidth() / 2;
						ball.vector.y = window.getHeight() / 2;
						break;
					case KeyEvent.VK_R:
						ball.vector.speedY = 0;
						break;
				}
			}

		});

		final Thread update = new Thread(new Runnable() {

			public void run() {
				running = true;

				while (running) {
					update();

					try {
						Thread.sleep(17);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		Thread draw = new Thread(new Runnable() {

			public void run() {
				while (getBufferStrategy() == null)
					createBufferStrategy(3);

				running = true;

				update.start();

				while (running) {
					repaint();

					try {
						Thread.sleep(17);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		draw.start();

		startMusic();
	}

	public void startMusic() {
		Thread playMusic = new Thread(new Runnable() {

			public void run() {
				try {
					AudioInputStream in = AudioSystem
					        .getAudioInputStream(getClass()
					                .getResourceAsStream(
					                        "/Music/Armin van Buuren - Ping Pong (Original Mix) [Armada Music].wav"));

					sound = AudioSystem.getClip();
					sound.open(in);
					sound.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		playMusic.start();
	}

	public void update() {
		map.update();
		p1.vector.x = window.getWidth() - 2.5 * p1.sizeX;

		p1.update();
		p2.update();
		ball.update();

		if (checkCollision()) {
		}

		p1.move();
		p2.move();
		ball.move();

		if (ball.vector.x < 0) {
			ball.vector.x = map.wallThickness * 2;
			ball.vector.speedX = ball.maxSpeed;
		}
		if (ball.vector.x > window.getWidth()) {
			ball.vector.x = window.getWidth() - map.wallThickness * 4;
			ball.vector.speedX = -ball.maxSpeed;
		}

		if (ball.vector.y - ball.vector.speedY < 0) {
			ball.vector.y = map.wallThickness * 2;
			ball.vector.speedY = ball.maxSpeed;
		}
		if (ball.vector.y + ball.vector.speedY > window.getHeight()) {
			ball.vector.y = window.getHeight() - map.wallThickness * 4;
			ball.vector.speedY = -ball.maxSpeed;
		}
	}

	public boolean checkCollision() {
		boolean hit = false;

		double speedX = ball.vector.speedX;
		double speedY = ball.vector.speedY;

		if (ball.down.intersects(map.bottom) || ball.up.intersects(map.top)) {
			hit = true;
			ball.vector.speedY = -speedY;
			if (ball.down.intersects(map.bottom)) {
				ball.vector.y = map.bottom.y - map.bottom.getHeight() * 3;
			}
			if (ball.up.intersects(map.top)) {
				ball.vector.y = map.top.getHeight() * 3;
			}
		}
		if (ball.right.intersects(map.right) || ball.left.intersects(map.left)) {
			hit = true;
			ball.vector.speedX = -speedX;
			if (ball.left.intersects(map.left)) {
				ball.vector.x = map.left.getWidth() * 4;
			}
			if (ball.right.intersects(map.right)) {
				ball.vector.x = map.right.x - map.right.getWidth() * 4;
			}
		}
		if (ball.left.intersects(p2.paddle)) {
			hit = true;

			ball.curve(p2.vector.speedY * friction);

			ball.vector.speedX = -speedX;
			ball.vector.speedY += p2.vector.speedY * friction;

			if (!ball.isCurving) {
				if (ball.vector.speedY > ball.maxSpeed)
					ball.vector.speedY = ball.maxSpeed;
				if (ball.vector.speedY < -ball.maxSpeed)
					ball.vector.speedY = -ball.maxSpeed;
			}
		}

		if (ball.right.intersects(p1.paddle)) {
			hit = true;

			ball.curve(p1.vector.speedY * friction);

			ball.vector.speedX = -speedX;
			ball.vector.speedY += p1.vector.speedY * friction;

			if (!ball.isCurving) {
				if (ball.vector.speedY > ball.maxSpeed)
					ball.vector.speedY = ball.maxSpeed;
				if (ball.vector.speedY < -ball.maxSpeed)
					ball.vector.speedY = -ball.maxSpeed;
			}
		}

		if (ball.right.intersects(map.goal1))
			if (!ball.right.intersects(p1.paddle))
				if (System.currentTimeMillis() - lastPoint > 400) {
					score2++;
					lastPoint = System.currentTimeMillis();
				}
		if (ball.left.intersects(map.goal2))
			if (!ball.left.intersects(p2.paddle))
				if (System.currentTimeMillis() - lastPoint > 400) {
					score1++;
					lastPoint = System.currentTimeMillis();
				}

		if (stopCurve && hit) {
			ball.isCurving = false;
			stopCurve = false;
		}

		if (hit && ball.isCurving)
			stopCurve = true;

		return hit;
	}

	public class DrawThingy extends JPanel {

		public DrawThingy() {}

		public void paint(Graphics g) {
			g.setColor(Color.black);
			g.fillRect(0, 0, window.getWidth(), window.getHeight());

			g.setColor(Color.orange);
			g.drawString(score2 + "", 100, 100);
			g.drawString(score1 + "", window.getWidth() - 100, 100);

			map.draw(g);

			p1.draw(g);
			p2.draw(g);

			ball.draw(g);

		}
	}

	public static void main(String[] args) {
		new Game();
	}
}
