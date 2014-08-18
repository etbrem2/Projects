package Main;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class GUI extends JFrame {

	boolean hardMode = false;
	boolean stop = false, esc = false, skipLine = false;
	GUI window;
	Scanner reader = new Scanner(System.in);

	static Info info;

	File file;
	String code;
	JFileChooser fileBrowser;

	JTextArea src, display;
	JTextField in;
	JButton run;
	JLabel inp;
	sys sys;

	int[] cell = new int[30000];
	int p = 0;
	int open = 0, close = 0;
	int sps = 0;
	String input = "";
	String output = "";

	Thread run1 = new Thread();
	Runnable magic = new Runnable() {

		public void run() {
			cell = new int[30000];
			for (int i = 0; i < cell.length; i++)
				cell[i] = 0;
			p = 0;
			open = 0;
			close = 0;
			input = "";
			output = "";
			stop = false;
			esc = false;
			display.setText("");
			inp.setText("Input: ");

			if (info != null)
				info.dispose();

			info = new Info(window);
			runCode(src.getText());

			display.setText(output);

			info.finish();
			info.repaint();
		}

	};

	public GUI(String str) {
		this();
		this.src.setText(str);
		this.run.doClick();
	}

	public GUI() {
		super("Brainfuck IDE");

		setLayout(new FlowLayout());

		window = this;

		sys = new sys(this);

		run = new JButton("Run code");
		src = new JTextArea(12, 50);
		in = new JTextField(20);
		inp = new JLabel("Input: ");
		display = new JTextArea(12, 50);
		display.setEditable(false);

		run.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				in.requestFocus();

				if (run1.isAlive()) {
					stop = true;
					new GUI(src.getText());
					dispose();
				}
				run1 = new Thread(magic);

				run1.start();

			}
		});

		add(new JScrollPane(src));
		add(run);
		add(in);
		add(inp);
		add(new JScrollPane(display));

		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void runCode(String txt) {
		int j = 0;

		for (int i = 0; i < txt.length(); i++) {

			char c = txt.charAt(i);

			if (stop) { return; }

			if (c != '[') {
				doAction(c);
				if(skipLine){
					skipLine = false;
					i = txt.indexOf("\n",i);
					System.out.println(txt.charAt(i));
					continue;
				}
			} else {
				open++;
				j = i + 1;
				while (open != close) {
					if (txt.charAt(j) == '[')
						open++;
					if (txt.charAt(j) == ']')
						close++;
					j++;
				}
				String minicode = txt.substring(i + 1, j - 1);

				// sys.pln("mini: "+minicode);
				while (cell[p] != 0 && !esc) {
					runCode(minicode);
				}
				esc = false;
				i = j - 1;
			}
		}
	}

	public void doAction(char c) {
		// sys.pln(c+" "+p+" "+cell[p]);

		if (stop)
			return;

		info.setCmd(c);
		info.repaint();

		switch (c) {
			case '/':
				skipLine = true;
				break;
			case '>':
				p++;
				break;
			case '<':
				p--;
				break;
			case '.':
				// if (cell[p] == 160) { return; }
				if (cell[p] == 8319) {
					output += "\n";
					cell[p] = 10;
				} else
					sys.p((char) cell[p]);
				display.setText(output);
				break;
			case ',':
				while (in.getText().length() == 0 && input.length() == 0)
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}
				if (in.getText().length() > 0)
					while (input.length() == 0) {
						input = in.getText().charAt(0) + "";
						in.setText("");
					}
				cell[p] = (int) input.charAt(0);
				if (cell[p] == 8319)
					cell[p] = 10;
				inp.setText(inp.getText() + (char) cell[p]);
				if (input.length() > 0)
					input = input.substring(1);
				in.setText("");
				if (cell[p] == 160) {
					esc = true;
					cell[p] = 0;
				}

				break;
			case '+':
				cell[p]++;
				break;
			case '-':
				cell[p]--;
				break;
		}
		if (!hardMode) {
			if (p == cell.length)
				p = 0;
			if (p < 0)
				p = cell.length - 1;
		}
		info.repaint();
		if (!stop)
			try {
				Thread.sleep(1000 / sps);
			} catch (Exception e) {
			}
	}

	public static void main(String[] args) {
		new GUI();
	}

}
