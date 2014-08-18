package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import Server.sys;

public class ClientGUI extends JFrame {

	static ClientGUI window;
	Options options;

	Socket connection;
	ObjectInputStream in;
	ObjectOutputStream out;

	String ip = "127.0.0.1";
	int port = 45681;

	int lastX, lastY;
	boolean notify = false;
	boolean playSound;
	boolean running = false;
	String message, lastMsg = "", sounded;

	JLabel title, color;
	JButton exit, opt;
	JTextArea history;
	JTextField chatBar;
	JScrollPane scroll;

	Color defaultColor;
	Clip sound;

	// this Runnable first calls the setup method and then waits for input for
	// input stream
	// displays the text from the input stream
	Runnable refresh = new Runnable() {
		public void run() {
			try {				
				if (!running)
					setup();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			while (running) {
				try {
					message = (String) in.readObject();
					history.setText(message);

					notify = !(window.hasFocus() || chatBar.hasFocus()
							|| history.hasFocus() || opt.hasFocus() || options
							.hasFocus());

					if (lastMsg.equals(message))
						notify = false;
					else
						playSound = true;

					if (!notify)
						lastMsg = message;

					if (sounded != null && message != null)
						if (sounded.equals(message))
							playSound = false;

				} catch (Exception e) {
					e.printStackTrace();
					sys.p("Input error");
					running = false;
					if (history != null)
						history.setText("Error occured.");
				}
			}
		}
	};

	Thread tryRefresh = new Thread(refresh);

	public ClientGUI() {
		super("Chat program");

		setLayout(new FlowLayout());

		// getHost();

		if (ip.length() == 0)
			System.exit(0);

		options = new Options(this);

		opt = new JButton("Show/hide options");
		exit = new JButton("Exit");
		history = new JTextArea(20, 50);
		chatBar = new JTextField(20);
		title = new JLabel("Et's Chat Program");
		color = new JLabel("#default color");

		defaultColor = getBackground();

		// Add Listeners

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
				options.setLocation(window.getX() + window.getWidth() - 150,
						window.getY());
			}

			public void mouseMoved(MouseEvent e) {
			}
		});
		opt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				options.setVisible(!options.isVisible());
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					connection.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					System.exit(0);
				}
			}
		});
		chatBar.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = chatBar.getText();
					try {
						send(message);
					} catch (Exception e1) {
						e1.printStackTrace();
						try {
							connection.close();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						System.exit(0);
					}
				}
			}

			public void keyReleased(KeyEvent e) {

			}
		});

		// End of Add Listeners

		title.setFont(new Font("Arial", Font.BOLD, 20));
		title.setForeground(Color.DARK_GRAY);

		history.setEditable(false);
		history.setLineWrap(true);

		scroll = new JScrollPane(history);

		add(title, BorderLayout.NORTH);
		add(opt, BorderLayout.NORTH);
		add(scroll, BorderLayout.NORTH);
		add(chatBar, BorderLayout.CENTER);
		add(exit, BorderLayout.SOUTH);
		add(color, BorderLayout.SOUTH);

		setUndecorated(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		setSize(600, 400);
		setResizable(false);
		setLocation(500, 50);

		tryRefresh.start();

		requestFocus();
		
		chatBar.requestFocus();

	}

	public void setup() {
		// sets up connection and i/o streams.
		// if connection fails tries to connect to local host (/127.0.0.1)

		// <EXTRA>
		// creates notify color changer task
		TimerTask changeColor = new TimerTask() {
			public void run() {
				if (notify)
					startNotify();
				else
					stopNotify();
			}
		};
		java.util.Timer changeColors = new java.util.Timer();
		changeColors.scheduleAtFixedRate(changeColor, 200, 1000);
		// </EXTRA>
				
		
		try {
			options.setLocation(window.getX() + window.getWidth() - 150,
					window.getY());
			
			// tries to connect to the ip retrieved from database
					
			sys.p("Awaiting server");

			connection = new Socket(InetAddress.getByName(ip), port);

			sys.p("Server found");

			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());

			sys.p("Done setup");

			running = true;
		} catch (Exception e) {
			try {
				// tries to connect to local host

				sys.p("Retrying with local host as server");

				ip = "127.0.0.1";

				sys.p("Awaiting server");

				connection = new Socket(InetAddress.getByName(ip), port);

				sys.p("Server found");

				out = new ObjectOutputStream(connection.getOutputStream());
				in = new ObjectInputStream(connection.getInputStream());

				sys.p("Done setup");

				running = true;
			} catch (Exception e1) {
				e1.printStackTrace();
				sys.p("Error connecting");
				history.setText("Error connecting. Server might be down.");
			}
		}

	}

	public void getHost() {
		// gets the server's host ip address and port number from online
		// database

		try {
			String user = "etbrem";
			String pass = "abc123";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con;
			con = DriverManager.getConnection(
					"jdbc:mysql://db4free.net:3306/etdb", user, pass);

			Statement st = con.createStatement();
			String sql = ("SELECT * FROM `HOST` WHERE 1");
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				port = Integer.parseInt(rs.getString("PORT"));
				ip = rs.getString("IP");
			}

			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String txt) throws Exception {
		// sends chatBar.text through the output stream

		out.writeObject(txt);
		out.flush();

		chatBar.setText("");
	}

	public static void main(String[] args) throws Exception {
		window = new ClientGUI();
		
	}

	// <EXTRA>

	public String generateColor() {
		// randomly generates a string representing a 6 digit hex value

		String color = "";
		int[] part = new int[6];

		for (int i = 0; i < part.length; i++) {
			part[i] = (int) (Math.random() * (16));
		}
		for (int i = 0; i < part.length; i++) {
			if (part[i] < 10)
				color += part[i];
			else
				switch (part[i]) {
				case 10:
					color += "A";
					break;
				case 11:
					color += "B";
					break;
				case 12:
					color += "C";
					break;
				case 13:
					color += "D";
					break;
				case 14:
					color += "E";
					break;
				case 15:
					color += "F";
					break;
				}
		}
		window.color.setText("#" + color);
		return color;
	}

	public void changeColor() {
		// changes the color of the title label and window background

		int color = Integer.parseInt(generateColor(), 16);

		title.setForeground(new Color(color));

		color = Integer.parseInt(generateColor(), 16);

		window.getContentPane().setBackground(new Color(color));
	}

	public void resetColors() {
		// resets the color of the title label and window background to default

		title.setForeground(Color.black);
		window.getContentPane().setBackground(defaultColor);
		color.setText("#default color");
	}

	public void playSound(String path) {
		try {
			AudioInputStream in = AudioSystem
					.getAudioInputStream(getClass()
							.getResourceAsStream(path));

			sound = AudioSystem.getClip();
			sound.open(in);
			sound.setMicrosecondPosition(550000L);
			sound.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startNotify() {
		if (options.color.isSelected())
			changeColor();
		if (options.sound.isSelected())
			if (playSound) {
				playSound("/Sounds/notif.wav");

				sounded = message;
				playSound = false;
			}

	}

	public void stopNotify() {
		resetColors();
		playSound = true;
	}
	// </EXTRA>
}
