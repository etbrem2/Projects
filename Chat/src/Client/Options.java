package Client;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class Options extends JFrame{

	ClientGUI gui;
	int lastX,lastY;
	Options window;
	
	JToggleButton sound,color;
	
	public Options(ClientGUI gui){
		this.gui = gui;
		
		window = this;
		
		setLayout(new FlowLayout());
		
		addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent e) {
				// remembers position of mouse on JFrame in order to
				// drag and move the JFrame from that point
				lastX = e.getX();
				lastY = e.getY();
			}

			public void mouseReleased(MouseEvent arg0) {
			}

		});

		addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				// changes the window location

				window.setLocation(e.getXOnScreen() - lastX, e.getYOnScreen()
						- lastY);
			}

			public void mouseMoved(MouseEvent e) {
			}
		});
		
		sound = new JToggleButton("Sound notification",true);
		color = new JToggleButton("Color notifictation",true);
		
		add(color);
		add(sound);
	
		setUndecorated(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(false);
		setSize(200, 400);
		setResizable(false);
	}
}
