
import java.io.*;

/**
 * The main controller of the tic-tac-toe game.
 * Implements Runnable to allow the server to run multiple games at once.
 */
public class Game implements Constants, Runnable {

	/**
	 *  The Board object used to play the game
	 */
	private Board theBoard;

	/**
	 * The Referee object that sets up and begins the game
	 */
	private Referee theRef;

	private PrintWriter socketOutX;
	private BufferedReader socketInX;
	private PrintWriter socketOutO;
	private BufferedReader socketInO;

	/**
	 * A constructor for Game objects
	 */
    public Game(PrintWriter socketOut1, BufferedReader socketIn1, PrintWriter socketOut2, BufferedReader socketIn2) {
        theBoard  = new Board();
		this.socketInX = socketIn1;
		this.socketOutX = socketOut1;
		this.socketInO = socketIn2;
		this.socketOutO = socketOut2;

	}

	/**
	 * Appoints the Referee and prompts it to begin the game
	 * @param r The Referee object
	 * @throws IOException
	 */
    public void appointReferee(Referee r) throws IOException {
        theRef = r;
    	theRef.runTheGame();
    }

	/**

	 * @param args
	 * @throws IOException
	 */

	/**
	 * The driver of the tic-tac-toe game.
	 * Prompts players to enter their names, and constructs the Player objects.
	 * Constructs a Referee object, which starts the game.
	 */
	@Override
	public void run() {
    	try {
			Referee theRef;
			Player xPlayer, oPlayer;
			socketOutX.println("Please enter the name of the \'X\' player:");
			String name = socketInX.readLine();
			while (name == null) {
				socketOutX.print("Please try again: ");
				name = socketInX.readLine();
			}

			xPlayer = new Player(name, LETTER_X, socketOutX, socketInX);
			xPlayer.setBoard(this.theBoard);

			socketOutO.println("Please enter the name of the \'O\' player:");
			name = socketInO.readLine();
			while (name == null) {
				socketOutO.print("Please try again: ");
				name = socketInO.readLine();
			}

			oPlayer = new Player(name, LETTER_O, socketOutO, socketInO);
			oPlayer.setBoard(this.theBoard);

			theRef = new Referee();
			theRef.setBoard(this.theBoard);
			theRef.setoPlayer(oPlayer);
			theRef.setxPlayer(xPlayer);

			this.appointReferee(theRef);

			socketOutX.println("Thank you for playing");
			socketOutO.println("Thank you for playing");
		}
		catch (IOException e) {
    		e.printStackTrace();
		}

	}
}
