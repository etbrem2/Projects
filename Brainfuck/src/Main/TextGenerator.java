package Main;

import java.util.ArrayList;
import java.util.Scanner;

public class TextGenerator {

	static Scanner reader = new Scanner(System.in);
	static String txt;
	static String commands = "";
	static int last;
	static boolean clear = true;

	public static void main(String[] args) {
		getInput();
		/*
		 * int num = 7;
		 * 
		 * ArrayList<Integer> dividables = getListOfDividables(num, 2, new
		 * ArrayList<Integer>()); int dividable = getClosestPair(num,
		 * dividables);
		 * 
		 * System.out.println(dividable);
		 */

	}

	public static void smartPrint(char a) {
		int num = a;

		String cmd = "";

		ArrayList<Integer> dividables = getListOfDividables(num, 2,
		        new ArrayList<Integer>());
		int dividable = getClosestPair(num, dividables);

		if (dividable == 0 && num > 80) {
			int removed = 0;
			int n = num;
			int d1 = 0;
			while (removed < num && d1 == 0) {
				removed++;
				n--;

				ArrayList<Integer> dl = getListOfDividables(n, 2,
				        new ArrayList<Integer>());
				d1 = getClosestPair(n, dl);
			}
			if (removed == num) {
				print(a);
			} else {
				cmd += ">";
				for (int i = 0; i < removed; i++)
					cmd += "+";
				cmd += "<";

				int div2 = n / d1;

				if (clear)
					cmd += "[-]";

				for (int i = 0; i < d1; i++)
					cmd += "+";
				cmd += "[->";
				for (int i = 0; i < div2; i++)
					cmd += "+";
				cmd += "<]>.";

				if (clear)
					cmd += " [-<+>]<";

				last = num;

				commands += cmd + "\n";
			}
			return;
		}

		if (last == a) {
			cmd += ".";

			commands += cmd + "\n";
		} else if (dividable == 0) {
			print(a);
		} else {
			int div2 = num / dividable;

			if (clear)
				cmd += "[-]";

			for (int i = 0; i < dividable; i++)
				cmd += "+";

			cmd += "[->";

			for (int i = 0; i < div2; i++)
				cmd += "+";
			cmd += "<]>.";

			if (clear)
				cmd += " [-<+>]<";

			last = num;

			commands += cmd + "\n";
		}
	}

	public static void getInput() {

		txt = reader.nextLine();

		for (int i = 0; i < txt.length(); i++) {
			smartPrint(txt.charAt(i));

			if (!clear) {
				commands += ">";
				last = 0;
			}
		}

		if (clear)
			commands += "\n[-]";
		
		System.out.println(commands);
	}

	public static void print(char c) {
		int n = c;

		String cmd = "";

		if (last == n) {
			n = 0;
		} else if (last < n) {
			n = n - last;
			last = last + n;
		} else if (last > n) {
			for (int i = 0; i < last - n; i++)
				cmd += "-";
			last = n;
			n = 0;
		}

		for (int i = 0; i < n; i++)
			cmd += "+";

		cmd += ".";

		commands += cmd + "\n";
	}

	public static int getDivide(int num, int i) {
		System.out.println(num + " " + i);
		if (i == num)
			return 0;
		if (num % i == 0) {
			int diff1 = Math.abs(num / 2 - i);
			int diff2 = Math.abs(num / 2 - getDivide(num, i + 1));

			System.out.println(diff1 + " <-> " + diff2);

			if (diff1 <= diff2)
				return i;
			else
				return getDivide(num, i + 1);
		} else
			return getDivide(num, i + 1);
	}

	public static ArrayList<Integer> getListOfDividables(int num, int i,
	        ArrayList<Integer> list) {
		if (i == num)
			return list;
		if (num % i == 0)
			list.add(i);
		return getListOfDividables(num, i + 1, list);
	}

	public static int getClosestPair(int num, ArrayList<Integer> list) {
		if (list.size() == 0)
			return 0;

		ArrayList<Integer> opposites = new ArrayList<Integer>();

		for (int i : list)
			opposites.add(num / i);

		int smallest = 0;
		int difference = Math.abs(list.get(0) - opposites.get(0));

		for (int i = 0; i < list.size(); i++) {
			int n1 = list.get(i);
			int n2 = opposites.get(i);

			if (Math.abs(n1 - n2) < difference) {
				smallest = i;
				difference = Math.abs(n1 - n2);
			}
		}

		return list.get(smallest);
	}
}
