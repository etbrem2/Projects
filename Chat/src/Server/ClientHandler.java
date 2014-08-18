package Server;

import java.io.File;
import java.net.Socket;

public class ClientHandler {

	boolean running = true;
	boolean admin = false;
	PortHandler handler;
	Connection connection;
	Thread inThread, outThread;
	InputHandler in;
	OutputHandler out;
	File currentChatroom;
	
	String username;

	public ClientHandler(int u, PortHandler h, Connection con) {
		handler = h;

		username = handler.users.get(u);

		this.connection = con;
		try {
			setup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setup() {
		try {
			
			currentChatroom = sys.getMainChatroom();

			admin = sys.isAdmin(username);


			out = new OutputHandler(this);
			in = new InputHandler(this);


			outThread = new Thread(out);
			inThread = new Thread(in);

			inThread.start();
			outThread.start();

			String st="";
			if(admin)
				st= " (as admin)";
			
			sys.w(username + " (" + connection.socket.getInetAddress().toString()
					+ ") has joined!"+st, currentChatroom);
			sys.p(username + " (" + connection.socket.getInetAddress().toString()
					+ ") has connected to main chatroom"+st);
		} catch (Exception e) {e.printStackTrace();
		}
	}

	public synchronized void disconnect() {
		if (running) {
			try {
				connection.socket.close();
				
				handler.clearClient(handler.users.indexOf(username));
				
				in = null;
				out = null;


				sys.p(username + " (" + connection.socket.getInetAddress()
						+ ") has disconnected");
				sys.w(username + " (" + connection.socket.getInetAddress()
						+ ") has disconnected", currentChatroom);

				running = false;
			} catch (Exception e) {
				sys.p("Connection with "
						+ connection.socket.getInetAddress().toString()
						+ " is already closed");
			}
			
		}
	}
}
