package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class OutputHandler implements Runnable {

	ClientHandler handler;
	ObjectOutputStream out;
	boolean wait = false;

	public OutputHandler(ClientHandler handler) throws IOException {
		this.handler = handler;
		out = handler.connection.out;
	}

	public void run() {
		// TODO Auto-generated method stub

		while (handler.running) {
			try {
				if (!wait) {
					out.writeObject(sys.read(handler.currentChatroom));
					out.flush();
				}
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		try {
			handler.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
