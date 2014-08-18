package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.TimerTask;

public class InputHandler implements Runnable {

	ClientHandler handler;
	ObjectInputStream in;
	ArrayList<String> commandList;
	String t;

	public InputHandler(ClientHandler handler) throws Exception {
		this.handler = handler;

		in = handler.connection.in;
		commandList = sys.commandList();
	}

	public void run() {
		while (handler.running) {
			String txt;
			try {

				txt = (String) in.readObject();
				int cmd = findCommand(txt);

				if (cmd == -1) {
					if (txt.length() > 0)
						sys.w("@" + handler.username + ": " + txt,
								handler.currentChatroom);
				} else
					doCommand(cmd, txt);

			} catch (Exception e) {
				try {
					handler.disconnect();
				} catch (Exception e1) {
				}
			}
		}
		try {
			handler.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void doCommand(int cmd, String txt) throws Exception {
		String params = txt.substring(txt.indexOf(" ") + 1);

		if (sys.caseSensitive)
			params = params.toUpperCase();

		params.replaceAll(" ", ", ,");

		if (!handler.admin)
			cmd = -1;

		if (cmd == -1) {

			// refuse

			long start = System.currentTimeMillis();

			handler.out.wait = true;

			handler.connection.out
					.writeObject("You are not admin.\n\nType anything to dismiss.");
			handler.connection.out.flush();

			String ms = (String) in.readObject();
			handler.out.wait = false;
		}
		if (cmd == 0) {

			// help

			long start = System.currentTimeMillis();

			handler.out.wait = true;

			String help = sys.read(sys.getHelpFile());

			handler.connection.out.writeObject(help
					+ "\n\nType anything to dismiss.");
			handler.connection.out.flush();

			String ms = (String) in.readObject();
			handler.out.wait = false;
		}
		if (cmd == 1) {
			// quit

			handler.disconnect();
		}
		if (cmd == 2) {
			// remove user1,2

			String[] users = params.split(",");
			for (String user : users)
				try {
					handler.handler.disconnect(user);
					if (PortHandler.users.contains(user))
						sys.w(handler.username + " removed " + user,
								handler.currentChatroom);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		if (cmd == 3) {
			// ban user1,2

			String[] users = params.split(",");
			for (String user : users) {
				sys.w(user, sys.getBanListFile());

				sys.w(handler.username + " banned " + user,
						handler.currentChatroom);
			}
			doCommand(2, txt);
		}
		if (cmd == 4) {
			// banip /127.0.0.1,/ip

			String[] ips = params.split(",");

			for (int i = 0; i < ips.length; i++)
				if (!ips[i].startsWith("/"))
					ips[i] = "/" + ips[i];

			for (String ip : ips)
				sys.w(ip, sys.getBannedIPFile());

			for (String ip : ips)
				doCommand(5, "/remove " + ip);
		}
		if (cmd == 5) {
			// removeip /127.0.0.1,ip
			String[] ips = params.split(",");

			for (int i = 0; i < ips.length; i++)
				if (!ips[i].startsWith("/"))
					ips[i] = "/" + ips[i];

			for (String ip : ips) {
				String st = "";
				ArrayList<String> names = handler.handler.getUserOfIP(ip);

				for (String name : names)
					st += name + ",";

				doCommand(2, "/remove " + st);
			}
		}
		if (cmd == 6) {
			// clear
			sys.clear(handler.currentChatroom);
		}
		if (cmd == 7) {
			// show
			handler.out.wait = true;

			String list = handler.handler.gui.list.online;
			list += "\nType anything to dismiss";

			handler.out.out.writeObject(list);
			handler.out.out.flush();

			t = (String) in.readObject();

			handler.out.wait = false;
		}
		if (cmd == 8) {
			String[] toWarn = params.split(",");

			for (int i = 0; i < toWarn.length; i++) {
				sys.w(toWarn[i] + " has been warned", handler.currentChatroom);
				sys.strike(toWarn[i]);

				if (sys.shouldBeKicked(toWarn[i]))
					doCommand(findCommand("/ban " + toWarn[i]), "/ban "
							+ toWarn[i]);
			}
		}
	}

}
