

//STUDENTS SHOULD ADD CLASS COMMENTS, METHOD COMMENTS, FIELD COMMENTS 

import java.io.PrintWriter;

/**
 * Defines logic for marking the tic-tac-toe board and determining game-over conditions.
 */
public class Board implements Constants {
	private char theBoard[][];
	private int markCount;

	/**
	 * The constructor for the Board object
	 */
	public Board() {
		markCount = 0;
		theBoard = new char[3][];
		for (int i = 0; i < 3; i++) {
			theBoard[i] = new char[3];
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		}
	}

	/**
	 * Retrieves the mark stored in the specified row-column space
	 * @param row
	 * @param col
	 * @return
	 */
	public char getMark(int row, int col) {
		return theBoard[row][col];
	}

	/**
	 * Checks if the board has been completely filled with X and O marks
	 * @return True if the board is full, false otherwise
	 */
	public boolean isFull() {
		return markCount == 9;
	}

	/**
	 * Checks if the x player has won the game
	 * @return True if the x player has won, false otherwise
	 */
	public boolean xWins() {
		if (checkWinner(LETTER_X) == 1)
			return true;
		else
			return false;
	}

	/**
	 * Checks if the o player has won the game
	 * @return True if the o player has won, false otherwise
	 */
	public boolean oWins() {
		if (checkWinner(LETTER_O) == 1)
			return true;
		else
			return false;
	}

	/**
	 * Prints the tic-tac-toe board to the console, along with any marks that were placed.
	 */
	public void display() {
		displayColumnHeaders();
		addHyphens();
		for (int row = 0; row < 3; row++) {
			addSpaces();
			System.out.print("    row " + row + ' ');
			for (int col = 0; col < 3; col++)
				System.out.print("|  " + getMark(row, col) + "  ");
			System.out.println("|");
			addSpaces();
			addHyphens();
		}
	}

	public void display(PrintWriter socketOut) {
		displayColumnHeaders(socketOut);
		addHyphens();
		for (int row = 0; row < 3; row++) {
			addSpaces(socketOut);
			socketOut.print("    row " + row + ' ');
			for (int col = 0; col < 3; col++)
				socketOut.print("|  " + getMark(row, col) + "  ");
			socketOut.println("|");
			addSpaces(socketOut);
			addHyphens(socketOut);
		}
	}

	/**
	 * Adds the specified mark to the space with a matching row and column
	 * @param row The row number to add the mark to
	 * @param col The column number to add the mark to
	 * @param mark The mark to add to the board
	 */
	public void addMark(int row, int col, char mark) {
		
		theBoard[row][col] = mark;
		markCount++;
	}

	/**
	 * Removes all X and O marks in the board and replaces them with whitespace
	 */
	public void clear() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		markCount = 0;
	}

	/**
	 * Checks if the game has been won by the player with the specified mark
	 * @param mark The mark to check
	 * @return 1 if the player has won, 0 otherwise
	 */
	int checkWinner(char mark) {
		int row, col;
		int result = 0;

		for (row = 0; result == 0 && row < 3; row++) {
			int row_result = 1;
			for (col = 0; row_result == 1 && col < 3; col++)
				if (theBoard[row][col] != mark)
					row_result = 0;
			if (row_result != 0)
				result = 1;
		}

		
		for (col = 0; result == 0 && col < 3; col++) {
			int col_result = 1;
			for (row = 0; col_result != 0 && row < 3; row++)
				if (theBoard[row][col] != mark)
					col_result = 0;
			if (col_result != 0)
				result = 1;
		}

		if (result == 0) {
			int diag1Result = 1;
			for (row = 0; diag1Result != 0 && row < 3; row++)
				if (theBoard[row][row] != mark)
					diag1Result = 0;
			if (diag1Result != 0)
				result = 1;
		}
		if (result == 0) {
			int diag2Result = 1;
			for (row = 0; diag2Result != 0 && row < 3; row++)
				if (theBoard[row][3 - 1 - row] != mark)
					diag2Result = 0;
			if (diag2Result != 0)
				result = 1;
		}
		return result;
	}

	/**
	 * Prints the column headers at the top of the board
	 */
	void displayColumnHeaders() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("|col " + j);
		System.out.println();
	}

	void displayColumnHeaders(PrintWriter socketOut) {
		socketOut.print("          ");
		for (int j = 0; j < 3; j++)
			socketOut.print("|col " + j);
		socketOut.println();
	}

	/**
	 * Prints the horizontal lines of the board
	 */
	void addHyphens() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("+-----");
		System.out.println("+");
	}

	void addHyphens(PrintWriter socketOut) {
		socketOut.print("          ");
		for (int j = 0; j < 3; j++)
			socketOut.print("+-----");
		socketOut.println("+");
	}

	/**
	 * Prints the vertical lines of the board and the spaces between
	 */
	void addSpaces() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("|     ");
		System.out.println("|");
	}

	void addSpaces(PrintWriter socketOut) {
		socketOut.print("          ");
		for (int j = 0; j < 3; j++)
			socketOut.print("|     ");
		socketOut.println("|");
	}
}
