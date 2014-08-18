package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class PortHandler {

	ServerGUI gui;
	static ArrayList<ClientHandler> clients;
	static ArrayList<String> users;
	PortHandler handler = this;
	Thread tryLogin;
	static Socket temp;

	public PortHandler(ServerGUI GUI) {
		gui = GUI;
		clients = new ArrayList<ClientHandler>();
		users = new ArrayList<String>();
	}

	public void start() {

		Runnable login = new Runnable() {
			public void waitForConnection() {
				try {

					Connection t = new Connection(temp);

					if (authenticate(t) >= 0) {
						clients.add(new ClientHandler(users.size() - 1,
								handler, t));

						sys.p("Client connected");

					} else {
						t.socket.close();
						sys.p("Client refused");
					}
				} catch (Exception e) {
				}
			}

			public void run() {
				waitForConnection();
			}
		};

		while (gui.running) {
			sys.p("Listining for incoming connection on port " + gui.portNum);
			try {
				System.gc();

				temp = gui.port.accept();

				Thread tryLogin = new Thread(login);
				tryLogin.start();

				sys.p("Client found (" + temp.getInetAddress().toString() + ")");

				Thread.currentThread().sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public int authenticate(Connection connection) {

		ObjectInputStream in = connection.in;
		ObjectOutputStream out = connection.out;

		Socket connection1 = connection.socket;
		try {

			sys.p("Connecting streams with "
					+ connection1.getInetAddress().toString());

			sys.p("Streams set up");

			out.writeObject("Please enter username:");
			out.flush();

			String user = (String) in.readObject();
			if (sys.caseSensitive)
				user = user.toUpperCase();

			if (!users.contains(user) && sys.acceptableUsername(user)
					&& !sys.bannedIP(connection1.getInetAddress().toString())) {

				if (sys.userHasAccount(user)) {
					out.writeObject("Please enter password:");
					out.flush();

					String password = (String) in.readObject();

					if (sys.authenticateLogin(user, password)) {
						users.add(user);
						sys.p("Account login:\nUser: " + user + " Pass: "
								+ password);
						return 0;
					} else {
						// WRONG PASSWORD
						sys.strike(connection1.getInetAddress().toString());
						return -1;
					}
				} else {
					out.writeObject("Would you like to register for an account?\nY/N");
					String answer;
					answer = (String) (in.readObject()).toString()
							.toUpperCase();

					if (answer.charAt(0) == 'Y') {
						boolean passMatch = false;

						String password = "";

						while (!passMatch) {
							String st = "";
							if (password.length() != 0)
								st = "Inputed password is invalid.";

							out.writeObject(st + "Please enter password:");
							out.flush();

							password = (String) in.readObject();

							out.writeObject("Confirm password:");
							out.flush();

							String temp = (String) in.readObject();

							if (password.equals(temp))
								passMatch = true;

							if (password.length() < 3)
								passMatch = false;
						}
						sys.addAccount(user, password);
						sys.p("Account created:\nUser: " + user + " Pass: "
								+ password);

						users.add(user);
						return 0;
					} else {
						out.writeObject("Without a registered account you cannot enter chatrooms."
								+ "\nDo you want to quit? Y/N");
						out.flush();

						answer = (String) in.readObject();
						answer = answer.toUpperCase();
						
						if (answer.charAt(0) == 'Y')
							return -1;
						else
							return authenticate(connection);
					}
				}
			} else {
				if (users.contains(user)) {
					sys.p(user + " is already connected on ");
					out.writeObject(user + " is already connected ");
				}
				if (!sys.acceptableUsername(user))
					sys.p(user + " is an unacceptable username");

				if (sys.bannedIP(connection1.getInetAddress().toString()))
					sys.p(connection1.getInetAddress().toString()
							+ " is a banned IP address");
				if (sys.bannedIP(connection1.getInetAddress().toString()))
					sys.p(connection1.getInetAddress().toString()
							+ " is a banned IP address");

				return -1;
			}
		} catch (Exception e) {
			sys.p("Authentication error");
			e.printStackTrace();
			return -1;
		}
	}

	public void disconnect(String name) {
		if (sys.caseSensitive)
			name = name.toUpperCase();
		
		int i = users.indexOf(name);

		if (i ==-1)
			return;

		try {
			clients.get(i).disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized static void clearClient(int i) {
		users.set(i, null);
		users.remove(i);
		clients.set(i, null);
		clients.remove(i);
	}

	public synchronized String getIPOfUser(String name) {
		int i = -1;

		for (i = 0; i < clients.size(); i++)
			if (users.get(i).equals(name))
				break;
		if (clients.size() > i) {
			// sys.p("1------------");
			if (clients.get(i) != null) {
				// sys.p("1--------------------");
				if (clients.get(i).connection != null) {
					// sys.p("1-------------------");
					return clients.get(i).connection.socket.getInetAddress()
							.toString();
				}
			}
		}
		return "Error getting ip";
	}

	public ArrayList<String> getUserOfIP(String ip) {
		ArrayList<String> names = new ArrayList<String>();

		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).connection.socket.getInetAddress().toString()
					.equals(ip))
				if (!users.get(i).equals(""))
					names.add(users.get(i));
		}
		return names;
	}
}
