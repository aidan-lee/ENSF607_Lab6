import java.util.Scanner;

/**
 * Defines all the attributes and capabilities of a Player.
 */

public class Player {
    /**
     * The name of the player
     */
    private String name;
    /**
     * An object of type Board
     */
    private Board board;
    /**
     * An object of type Player, the opponent
     */
    private Player opponent;
    /**
     * The player's mark (an X or an O)
     */
    private char mark;

    /**
     * A constructor for Player objects
     * @param name The player's name
     * @param mark The player's mark
     */
    public Player(String name, char mark) {
        this.name = name;
        this.board = null;
        this.opponent = null;
        this.mark = mark;
    }

    /**
     * Calls makeMove and displays the tic-tac-toe board.
     * After each turn, it checks if the board is full or a player has won.
     * If neither have occurred, it passes the turn to the opposing player.
     */
    public void play() {

        makeMove();
        board.display();

        if (board.isFull()) {
            System.out.println("The board is full, the game is over. It was a tie!");
        }
        else if (board.oWins() || board.xWins()) {
            System.out.println("The game is over.  " + this.name + " is the winner!");
        }
        else {
            opponent.play();
        }
    }

    /**
     * Prompts the player to enter a row and column number.
     * If the space is open, it calls the board's addMark function.
     * If the space is not open or the player's input was invalid, the player is prompted to re-enter the row
     * and column numbers.
     */
    public void makeMove() {
        Integer row;
        Integer col;

        String rowString;
        String colString;

        boolean invalidResponse = true;

        Scanner scanner = new Scanner(System.in);

        while (invalidResponse) {
            System.out.println(this.name + ", what row should your next " + this.mark + " be placed in?");
            rowString = scanner.nextLine();
            System.out.println(this.name + ", what column should your next " + this.mark + " be placed in?");
            colString = scanner.nextLine();

            row = Integer.parseInt(rowString);
            col = Integer.parseInt(colString);

            if (row < Constants.ROW_MAX && col < Constants.COL_MAX) {
                boolean isSpaceFilled = board.getMark(row, col) == Constants.SPACE_CHAR ? false : true;
                if (!isSpaceFilled) {
                    board.addMark(row, col, this.mark);
                    invalidResponse = false;
                }
                else {
                    System.out.println("That space is occupied. Please try again, " + this.name + ".");
                }
            }
            else {
                System.out.println("Invalid input. Please try again, " + this.name + ".");
            }
        }
    }

    /**
     * Sets the Player object's opponent field
     * @param opponent The opponent Player object
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * Sets the Player object's board field
     * @param theBoard A Board object
     */
    public void setBoard(Board theBoard) {
        this.board = theBoard;
    }



}
