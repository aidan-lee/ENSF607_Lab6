/**
 * Defines all the attributes and capabilities of a Referee.
 */
public class Referee {
    /**
     * The Player object using the X mark
     */
    private Player xPlayer;
    /**
     * The Player object using the O mark
     */
    private Player oPlayer;
    /**
     * The Board object used to store X and O marks
     */
    private Board board;

    /**
     * A constructor for Referee objects
     */
    public Referee() {
        xPlayer = null;
        oPlayer = null;
        board = null;
    }

    /**
     * Sets the players of the game, and initializes gameplay.
     */
    public void runTheGame() {
        xPlayer.setOpponent(oPlayer);
        oPlayer.setOpponent(xPlayer);

        board.display();
        xPlayer.play();
    }

    /**
     * Sets the Referee object's board field
     * @param board A Board object
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Sets the Referee object's oPlayer field
     * @param oPlayer A Player object with an O mark
     */
    public void setoPlayer(Player oPlayer) {
        this.oPlayer = oPlayer;
    }

    /**
     * Sets the Referee object's xPlayer field
     * @param xPlayer A Player object with an X mark
     */
    public void setxPlayer(Player xPlayer) {
        this.xPlayer = xPlayer;
    }
}
