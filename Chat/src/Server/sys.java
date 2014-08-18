package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class sys {

	// should actually be notCaseSensetive
	// if true then all usernames are uppercased automatically
	public static final boolean caseSensitive = true;

	public static void p(String text) {
		System.out.println(text);
	}

	public static void w(String text, File loc) throws Exception {
		// write text into file

		FileWriter fw = new FileWriter(loc, true);
		BufferedWriter writer = new BufferedWriter(fw);

		writer.newLine();
		writer.write(text);

		writer.close();
		fw.close();
	}

	public static String read(File loc) throws FileNotFoundException {
		// returns all the text from a file

		String st = "";

		Scanner x = new Scanner(loc);

		while (x.hasNextLine()) {
			st += x.nextLine() + "\n";
		}

		x.close();

		return st;
	}

	public static ArrayList<String> commandList() {
		// returns list of all the commands

		ArrayList<String> commands = new ArrayList<String>();

		commands.add("/HELP");
		commands.add("/QUIT");
		commands.add("/REMOVE");
		commands.add("/BAN");
		commands.add("/BANIP");
		commands.add("/REMOVEIP");
		commands.add("/CLEAR");
		commands.add("/SHOW");
		commands.add("/WARN");

		return commands;
	}

	public static boolean userHasAccount(String user) throws Exception {
		// returns true if username appears in the accounts file

		boolean check = false;

		File file = getAccountFile();
		Scanner x = new Scanner(file);

		while (x.hasNextLine()) {
			if (x.nextLine().equals(user)) {
				check = true;

				break;
			}
			if (x.hasNextLine())
				x.nextLine();
		}
		x.close();
		file = null;

		return check;
	}

	public static boolean authenticateLogin(String user, String password)
			throws FileNotFoundException {
		// returns true if the inputed password matches the user's pass
		// in the account file; the password of a user is the text one line
		// under the username

		boolean check = false;

		File file = getAccountFile();
		Scanner x = new Scanner(file);

		while (x.hasNextLine()) {

			if (x.nextLine().equals(user))
				if (x.nextLine().equals(password)) {
					check = true;

					break;
				}

			if (x.hasNextLine())
				x.nextLine();
		}
		x.close();
		file = null;

		return check;
	}

	public static void addAccount(String user, String password)
			throws IOException {
		// appends the username into the account file and password one line
		// under the username

		File file = getAccountFile();

		FileWriter fw = new FileWriter(file, true);
		BufferedWriter writer = new BufferedWriter(fw);

		writer.newLine();
		writer.write(user);

		writer.newLine();
		writer.write(password);

		writer.close();
		fw.close();
		file = null;
	}

	public static boolean acceptableUsername(String user)
			throws FileNotFoundException {
		// returns true if username is found acceptable
		// each unacceptable word is represented as a single line in the ban
		// file
		// if the username contains an unacceptable work returns false
		
		String allowed = "et\n";

		if (caseSensitive)
			allowed = allowed.toUpperCase();
		try {
			if (allowed.contains(user))
				return true;
			if (user.length() < 3)
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean check = true;
		String temp1 = user.toUpperCase();

		String banList = read(getBanListFile());
	//	p(banList);
		String[] notAllowed = banList.split("\\n");

		for (int j = 0; j < notAllowed.length; j++) {
			String x = notAllowed[j];
			if (temp1.contains(x))
			{
				check = false;
				break;
			}
		}

		return check;
	}

	public static boolean bannedIP(String ip) throws FileNotFoundException {
		String x = read(getBannedIPFile());

		if (x.contains(ip))
			return true;

		return false;
	}

	public static boolean bannedUser(String user) throws Exception {
		String x = read(getBanListFile());

		if (x.contains(user))
			return true;

		return false;
	}

	public static boolean isAdmin(String user) throws FileNotFoundException {
		String admins = read(getAdminFile());
		if (caseSensitive) {
			admins = admins.toUpperCase();
			user = user.toUpperCase();
		}
		if (admins.contains(user))
			return true;

		return false;
	}

	public static void clear(File loc) {
		try {
			FileWriter fw = new FileWriter(loc, false);
			BufferedWriter writer = new BufferedWriter(fw);

			writer.write("Start of Chatroom");
			writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void strike(String toWarn) throws Exception {
		String bip = sys.read(getBannedIPFile());
		bip += "\n" + sys.read(getBanListFile());

		if (bip.contains(toWarn))
			return;

		String s1 = sys.read(getStrike1File());
		String s2 = sys.read(getStrike2File());

		int strike = 0;

		if (s1.contains(toWarn))
			strike++;
		if (s2.contains(toWarn))
			strike++;

		if (strike == 0)
			sys.w(toWarn, getStrike1File());
		if (strike == 1)
			sys.w(toWarn, getStrike2File());
		if (strike == 2) {
			File f = (toWarn.startsWith("/")) ? getBannedIPFile()
					: getBanListFile();

			sys.w(toWarn, f);
		}
	}

	public static boolean shouldBeKicked(String toCheck) throws Exception {
		String x = read(getBanListFile());
		x += "\n" + read(getBannedIPFile());

		return x.contains(toCheck);
	}

	public static File getHelpFile() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\Help.txt");
	}

	public static File getAdminFile() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\Admins.txt");
	}

	public static File getBannedIPFile() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\BanIPList.txt");
	}

	public static File getBanListFile() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\BanList.txt");
	}

	public static File getMainChatroom() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\Chatrooms\\MAIN.txt");
	}

	public static File getAccountFile() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\Accounts.txt");
	}

	public static File getStrike1File() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\Strikes\\1.txt");
	}

	public static File getStrike2File() {
		return new File(
				"C:\\Users\\Et\\Desktop\\Et'stuff\\Computer programming\\Chat Resources\\Strikes\\2.txt");
	}
}
