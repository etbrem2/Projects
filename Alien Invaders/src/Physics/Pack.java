package Physics;

import Entity.Alien;

public class Pack {

	Alien[][] aliens;
	int numOfAlienTypes = 3;

	int rows, columns;
	boolean hasLeft, hasRight;

	public Pack(int rows, int columns, int ... specifications) {
		this.rows = rows;
		this.columns = columns;

		aliens = new Alien[rows][columns];

		for (int i = 0; i < specifications.length; i++)
			for (int j = 0; j < columns; j++)
				aliens[i][j]= new Alien(specifications[i]);

		for (int i = 0; i < rows; i++)
			if (aliens[i][0] == null) {
				int type = (int) (Math.random() * numOfAlienTypes);
				for (int j = 0; j < columns; j++)
					aliens[i][j]= new Alien(type);
			}
	}

	public Alien getLeftColider() {

		for (int c = 0; c < columns; c++)
			for (int r = 0; r < rows; r++) {
				if (aliens[r][c] != null)
					return aliens[r][c];
			}
		return null;
	}

	public Alien getRightColider() {

		for (int c = columns - 1; c >= 0; c--)
			for (int r = 0; r < rows; r++) {
				if (aliens[r][c] != null)
					return aliens[r][c];
			}
		return null;
	}
}
