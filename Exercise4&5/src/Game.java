
import java.io.*;

//STUDENTS SHOULD ADD CLASS COMMENTS, METHOD COMMENTS, FIELD COMMENTS 

/**
 * The main controller of the tic-tac-toe game.
 * Contains the main method.
 */
public class Game implements Constants {

	/**
	 *  The Board object used to play the game
	 */
	private Board theBoard;

	/**
	 * The Referee object that sets up and begins the game
	 */
	private Referee theRef;

	/**
	 * A constructor for Game objects
	 */
    public Game( ) {
        theBoard  = new Board();
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
	 * The controller of the tic-tac-toe game.
	 * Prompts players to enter their names, and constructs the Player objects.
	 * Constructs a Referee object
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Referee theRef;
		Player xPlayer, oPlayer;
		BufferedReader stdin;
		Game theGame = new Game();
		stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("\nPlease enter the name of the \'X\' player: ");
		String name= stdin.readLine();
		while (name == null) {
			System.out.print("Please try again: ");
			name = stdin.readLine();
		}

		xPlayer = new Player(name, LETTER_X);
		xPlayer.setBoard(theGame.theBoard);
		
		System.out.print("\nPlease enter the name of the \'O\' player: ");
		name = stdin.readLine();
		while (name == null) {
			System.out.print("Please try again: ");
			name = stdin.readLine();
		}
		
		oPlayer = new Player(name, LETTER_O);
		oPlayer.setBoard(theGame.theBoard);
		
		theRef = new Referee();
		theRef.setBoard(theGame.theBoard);
		theRef.setoPlayer(oPlayer);
		theRef.setxPlayer(xPlayer);
        
        theGame.appointReferee(theRef);
	}
	

}
