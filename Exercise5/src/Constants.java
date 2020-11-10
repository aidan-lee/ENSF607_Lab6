/**
 * Defines various constant values used throughout the application
 */
public interface Constants {
	/**
	 * A space character (ASCII value 32)
	 */
	static final char SPACE_CHAR = ' ';
	/**
	 *  An O character (ASCII value 81)
	 */
	static final char LETTER_O = 'O';
	/**
	 * An X character (ASCII value 88)
	 */
	static final char LETTER_X = 'X';

	/**
	 * Defining the maximum number of rows in the tic-tac-toe game
	 */
	static final int ROW_MAX = 3;

	/**
	 * Defining the maximum number of columns in the tic-tac-toe game
	 */
	static final int COL_MAX = 3;

	/**
	 * Defining a String to indicate that a server response has multiple lines
	 */
	static final String printingDelimiter = "@KEEP_PRINTING@";

	/**
	 * Defining a String to indicate that a server response is waiting on a connection from another client
	 */
	static final String waitingDelimiter = "@WAITING@";

	/**
	 * Defining a String to indicate to the GUI that the server is requesting the names of the players
	 */
	static final String nameDelimiter = "@ENTER_NAME@";

	/**
	 * Defining a String to indicate to the GUI that the server is succeeded in making the player's move
	 */
	static final String moveSuccess = "@MARK_PLACED@";

	/**
	 * Defining a String to indicate to the GUI that the server is sending the opponent's move
	 */
	static final String opponentMove = "@OPPONENT_MOVE@";

	/**
	 * Defining a String to indicate to the GUI that the server is sending a message that should be displayed
	 */
	static final String messageIndicator = "@DISPLAY_MESSAGE@";

	/**
	 * Defining a String to indicate to the GUI that the server is sending the player's mark
	 */
	static final String markIndicator = "@MARK_INDICATOR@";

	/**
	 * Defining a String to indicate to the GUI that the server is sending a turn indicator message
	 */
	static final String turnIndicator = "@TURN_INDICATOR@";

	/**
	 * Defining a String to indicate to the GUI that it is the player's turn
	 */
	static final String yourTurn = "@YOUR_TURN@";


}
