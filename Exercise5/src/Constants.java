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
	 * Defining a substring to indicate that a server response has multiple lines
	 */
	static final String printingDelimiter = "@KEEP_PRINTING@";

	/**
	 * Defining a substring to indicate that a server response is waiting on a response from another client
	 */
	static final String waitingDelimiter = "@WAITING@";

	static final String nameDelimiter = "@ENTER_NAME@";

	static final String moveSuccess = "@MARK_PLACED@";

	static final String opponentMove = "@OPPONENT_MOVE@";

	static final String messageIndicator = "@DISPLAY_MESSAGE@";

	static final String markIndicator = "@MARK_INDICATOR@";
}
