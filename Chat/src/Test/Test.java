package Test;

import java.io.FileNotFoundException;

import Client.ClientGUI;
import Client.Options;
import Server.ServerGUI;
import Server.sys;

public class Test {
	
	static String[]args;
	
	static Runnable run = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			try {
				ServerGUI.main(args);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	public static void main(String[]args1) throws FileNotFoundException{
		args = args1;
		try {
			Thread runn = new Thread(run);
			runn.start();
			
			
			Thread.sleep(1000);
			
			ClientGUI.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
