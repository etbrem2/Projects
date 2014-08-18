package Server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextArea;
import java.util.Timer;
import java.util.TimerTask;

// onlineList is a JFrame which displays the usernames of online users.
// it also holds the commands for the server (different than admin commands)
public class OnlineList extends JFrame {

	ServerGUI server;
	String online;
	JTextArea list;
	JLabel title;
	
	ArrayList<String> commandList;
	Timer timer;

	OnlineList window;
	int lastX,lastY;
	
	public OnlineList(ServerGUI gui) {
		super("Online on server ()");
		
		setLayout(new FlowLayout());
		server = gui;
		window = this;
		
		commandList = sys.commandList();

		timer = new Timer();
		timer.schedule(refresh, 3000, 2000);

		title = new JLabel("Online on server()");
		list = new JTextArea(20, 20);
		list.setEditable(false);

		JScrollPane scroll = new JScrollPane(list);


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
		
		add(title);
		add(scroll,BorderLayout.NORTH);
		
		setUndecorated(true);
		setSize(260, 350);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(600, 0);
	}

	TimerTask refresh = new TimerTask() {
		@Override
		public void run() {
			online = "";
			int counter = 0;

			for (int i = 0; i < server.handler.users.size(); i++)
				if (server.handler.users.get(i).length() > 0) {

					String st = "";

					try {
						st = " @"
								+ server.handler
										.getIPOfUser(server.handler.users
												.get(i));
					} catch (Exception e) {
						e.printStackTrace();
					}

					online += server.handler.users.get(i) + st + "\n";
					counter++;
				}

			title.setText("Online on server (" + counter + ")");
			list.setText(online);
		}
	};

	public void init() {
		timer = new Timer();
	}

	public int findCommand(String txt) {
		if (!txt.startsWith("/"))
			return -1;

		int c = -1;

		for (int i = 0; i < commandList.size(); i++) {
			if (txt.toUpperCase().startsWith(commandList.get(i)))
				c = i;
		}

		return c;
	}

	public void doCommand(int cmd, String txt) {
		String params = txt.substring(txt.indexOf(" ") + 1);
		if (sys.caseSensitive)
			params = params.toUpperCase();

		if (cmd == -1) {
			// refuse
			try {
				if (txt.length() > 0)
					sys.w("Server says: " + txt, sys.getMainChatroom());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (cmd == 0) {
			// help
			try {
				String help = sys.read(sys.getHelpFile());
				help += "\n\nWait for automatic dismissal";

				server.counter = 4; // delays the refresh by 4 cycles
				// around 4 * 500 ms = 2 seconds

				server.chat.setText(help);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (cmd == 1) {
			// quit
			try {
				server.disconnectAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmd == 2) {
			// remove username1,2
			String[] users = params.split(",");
			for (String name : users)
				try {
					server.handler.disconnect(name);
					if (server.handler.users.contains(name))
						sys.w("Server removed " + name, sys.getMainChatroom());
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		if (cmd == 3) {
			// ban user1,2
			String[] users = params.split(",");

			for (String name : users)
				try {
					sys.w(name, sys.getBanListFile());
					sys.w("Server banned " + name, sys.getMainChatroom());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			for (String name : users)
				server.handler.disconnect(name);
		}
		if (cmd == 4) {
			// banIP /127.0.0.1,/ip
			String[] ips = params.split(",");

			for (int i = 0; i < ips.length; i++)
				if (!ips[i].startsWith("/"))
					ips[i] = "/" + ips[i];

			for (String ip : ips)
				try {
					sys.w(ip, sys.getBannedIPFile());
				} catch (Exception e) {
					e.printStackTrace();
				}

			doCommand(5, txt);
		}
		if (cmd == 5) {
			// removeIP /127.0.0.1,/ip

			String[] ips = params.split(",");

			for (int i = 0; i < ips.length; i++)
				if (!ips[i].startsWith("/"))
					ips[i] = "/" + ips[i];

			for (String ip : ips) {
				String st = "";
				ArrayList<String> names = server.handler.getUserOfIP(ip);

				for (String name : names)
					st += name + ",";

				doCommand(2, "/remove " + st);
			}
		}
		if (cmd == 6) {
			// clear
			sys.clear(sys.getMainChatroom());
		}
		if (cmd == 7) {
			// show
		}
		if (cmd == 8) {
			// warn user
			// warn ip

			String[] toWarn = params.split(",");

			for (int i = 0; i < toWarn.length; i++)
				try {
					sys.w(toWarn[i] + " has been warned", sys.getMainChatroom());
					sys.strike(toWarn[i]);

					if (sys.shouldBeKicked(toWarn[i]))
						doCommand(findCommand("/ban " + toWarn[i]), "/ban "
								+ toWarn[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
}
