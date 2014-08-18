package Main;

public class sys {

	static GUI gui;

	public sys(GUI g) {
		gui = g;
	}

	public void p(char c) {
		gui.output += c;
	}
}
