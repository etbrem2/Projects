package Server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.sql.DriverManager;

import javax.swing.*;

public class ServerGUI extends JFrame {

	int portNum;
	boolean running = true;
	int counter = 0;
	int lastX,lastY;
	
	ServerSocket port;
	PortHandler handler;
	
	JTextArea chat;
	JTextField cmd;
	JButton close;
	Thread refresh;

	static ServerGUI window;
	ServerGUI serverWindow;
	OnlineList list;

	public ServerGUI(int port) throws Exception {
		//
		super("ServerGUI");

		setLayout(new FlowLayout());

		serverWindow = this;
		this.portNum = port;
		this.port = new ServerSocket(port, 5);

		close = new JButton("Close");
		chat = new JTextArea(26, 50);
		cmd = new JTextField(26);

		chat.setLineWrap(true);
		chat.setEditable(false);

		// this Runnable refreshes the chat text
		// the counter is used to leave the chat text as it is for several
		// refresh cycles
		// (useful to keep the help page
		Runnable getTxt = new Runnable() {
			public void run() {
				while (running) {
					try {
						if (counter == 0)
							chat.setText(sys.read(sys.getMainChatroom()));
						else
							counter--;

						Thread.currentThread().sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		list = new OnlineList(this);

		JScrollPane scroll = new JScrollPane(chat);
		add(scroll, BorderLayout.NORTH);
		add(cmd, BorderLayout.CENTER);
		add(close, BorderLayout.SOUTH);

		cmd.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String com = cmd.getText();
					list.doCommand(list.findCommand(com), com);

					cmd.setText("");
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					running = false;
					disconnectAll();
					System.exit(0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		
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

				serverWindow.setLocation(e.getXOnScreen() - lastX, e.getYOnScreen()
						- lastY);
				list.setLocation(getX()+getWidth(), getY());
			}

			public void mouseMoved(MouseEvent e) {
			}
		});
		
		setTitle("ServerGUI" + " port: " + this.port.getLocalPort());

		setUndecorated(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(600, 470);
		setVisible(true);
		setResizable(false);

		cmd.requestFocus();

		refresh = new Thread(getTxt);
		refresh.start();

		handler = new PortHandler(this);
		handler.start();

	}

	public static void main(String[] args) throws Exception {
		window = new ServerGUI(45681);
	}

	public void disconnectAll() throws Exception {
		// disconnects all open connections
		for (int i = 0; i < PortHandler.users.size(); i++) {
			handler.disconnect(PortHandler.users.get(i));
		}

	}
}
