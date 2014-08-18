package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Update {

	//
	static boolean update = false;
	static boolean updated = true;

	static int port = -1;
	static String ip = "";


	static String user = "etbrem";
	static String pass = "abc123";
	
	public static void set() {

		try {
			//ENTER IP TO UPDATE HERE \/
			
			String ip = "79.176.195.110";
			int port = 45681;

			Connection con = DriverManager.getConnection(
					"jdbc:mysql://db4free.net:3306/etdb", user, pass);
			String sql = String.format(
					"UPDATE `HOST` SET `IP`='%s',`PORT`=%d WHERE ID=1", ip,
					port);
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			updated = false;
			e.printStackTrace();
		}
	}

	public static void get() {
		try {

			Connection con = DriverManager.getConnection(
					"jdbc:mysql://db4free.net:3306/etdb", user, pass);

			Statement st = con.createStatement();
			String sql = ("SELECT * FROM `HOST` WHERE ID=1");
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				port = rs.getInt("PORT");
				ip = rs.getString("IP");
			}

			con.close();
		} catch (Exception e) {
			updated = false;
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		if (update)
			set();
		else
			get();

		if (update)
			sys.p("Update" + (updated ? "d succesfully" : " failed"));
		else
			sys.p((port == -1) ? "Failed to fetch info" : "Succesfully fetched host info:\n\nip: "+ip+"\nport: "+port);
	}
}
