
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GUI extends JFrame {

    /**
     * The main window of the GUI. Contains the tic tac toe board
     */
    JFrame mainWindow;

    /**
     * Width of the main window
     */
    int width;

    /**
     * Height of the main window
     */
    int height;

    /**
     * An array of JButtons, representing the spaces in the tic tac toe grid
     */
    ArrayList<Pair<String, JButton>> buttons;

    /**
     * A JTextArea, used to display messages from the server
     */
    JTextArea messageBox;

    /**
     * A JLabel, used to display turn information
     */
    JLabel turnIndicator;

    /**
     * The name of the player using the GUI
     */
    String playerName;

    /**
     * The mark of the player using the GUI
     */
    String mark;

    /**
     * A socket
     */
    private Socket socket;

    /**
     * Socket used to write to the server
     */
    private PrintWriter socketOut;

    /**
     * Socket used to receive messages from the server
     */
    private BufferedReader socketIn;

    // Used to read user input from the console
    /**
     * Used to read user input from the console
     */
    private BufferedReader stdIn;

    /**
     * Constructs a GUI object
     * @param w the width of the main window
     * @param h the height of the main window
     * @param serverName the name of the server to connect to
     * @param portNumber the port to listen on
     */
    public GUI(int w, int h, String serverName, int portNumber) {
        width = w;
        height = h;

        // Setting up the main window
        mainWindow = new JFrame();
        mainWindow.setTitle("Tic Tac Toe");
        mainWindow.setSize(width, height);
        mainWindow.setVisible(true);

        // Creating the tic tac toe grid
        buttons = new ArrayList<>();
        createButtons();

        // Setting up the message box
        messageBox = new JTextArea();
        messageBox.setEditable(false);
        messageBox.setBounds(10,50, 100, 100);
        messageBox.setColumns(20);
        messageBox.setRows(10);
        messageBox.setLineWrap(true);

        // Setting up the turn label
        turnIndicator = new JLabel(" ");

        // Set up sockets
        try {
            socket = new Socket(serverName, portNumber);
            stdIn = new BufferedReader(new InputStreamReader (System.in));
            socketIn = new BufferedReader(new InputStreamReader (socket.getInputStream()));
            socketOut = new PrintWriter (socket.getOutputStream(), true);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives messages from the client and sends the user's input back, until it receives the message to stop.
     */
    public void communicate() {
        String line = "";
        String response = "";

        while (!line.equals("QUIT")) {
            try {
                response = socketIn.readLine();
                if (waiting(response)) {
                    // Display waiting panel
                    displayWaiting(response);
                }
                else if (requestName(response)) {
                    // Launch popup to get name
                    displayNameForm(response);
                }
                else if (opponentMove(response)) {
                    // Update the tic tac toe board with the oppoent's move
                    displayOpponentMove(response);
                }
                else if (receivedMessage(response)) {
                    // Display the message from the server in the message window
                    displayMessage(response);
                }
                else if (receivedMark(response)) {
                    // Sets the player's mark
                    setMark(response);
                }
                else if (receivedTurn(response)) {
                    // Sets the turn indicator message, and enables or disables the grid buttons,
                    // depending on whether or not it is the player's turn
                    setTurn(response);
                }
                else {
                    // Show the main window
                    displayGame();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        closeSockets();
    }

    /**
     * Looks for the waitingDelimiter substring in a server response
     * @param response the server response
     * @return true if the delimiter is found, false otherwise
     */
    private boolean waiting(String response) {
        if (response.contains(Constants.waitingDelimiter)) {
            return true;
        }
        return false;
    }

    /**
     * Adds a JLabel to the main window to display the message from the server
     * @param response the response from the server
     */
    private void displayWaiting(String response) {
        mainWindow.getContentPane().removeAll();
        response = response.replaceAll(Constants.waitingDelimiter, "");
        JLabel waitingText = new JLabel(response);
        waitingText.setBounds(10,10, 300, 30);
        mainWindow.add(waitingText);
        mainWindow.repaint();
    }

    /**
     * Determines whether or not a server response contains the Constants.nameDelimiter substring
     * @param response the response from the server
     * @return true if substring found, false otherwise
     */
    private boolean requestName(String response) {
        if (response.contains(Constants.nameDelimiter)) {
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not a server response contains the Constants.moveSuccess substring
     * @param response the response from the server
     * @return true if substring found, false otherwise
     */
    private boolean moveSuccess(String response) {
        if (response.contains(Constants.moveSuccess)) {
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not a server response contains the Constants.opponentMove substring
     * @param response the response from the server
     * @return true if substring found, false otherwise
     */
    private boolean opponentMove(String response) {
        if (response.contains(Constants.opponentMove)) {
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not a server response contains the Constants.messageIndicator substring
     * @param response the response from the server
     * @return true if substring found, false otherwise
     */
    private boolean receivedMessage(String response) {
        if (response.contains(Constants.messageIndicator)) {
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not a server response contains the Constants.markIndicator substring
     * @param response the response from the server
     * @return true if substring found, false otherwise
     */
    private boolean receivedMark(String response) {
        if (response.contains(Constants.markIndicator)) {
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not a server response contains the Constants.turnIndicator substring
     * @param response the response from the server
     * @return true if substring found, false otherwise
     */
    private boolean receivedTurn(String response) {
        if (response.contains(Constants.turnIndicator)) {
            return true;
        }
        return false;
    }

    /**
     * Open a input dialogue to prompt the user to enter their name
     * @param response the prompt from the server
     */
    private void displayNameForm(String response) {
        response = response.replaceAll(Constants.nameDelimiter, "");
        String name = JOptionPane.showInputDialog(response);
        playerName = name;
        socketOut.println(name);
    }

    /**
     * Display the main window, with a tic tac toe board and a message window
     */
    private void displayGame() {
        mainWindow.getContentPane().removeAll();
        mainWindow.setLayout(new GridLayout(1, 2, 50, 50));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Set up left panel, containing the board
        JPanel left = new JPanel();
        left.setLayout(new GridBagLayout());

        JLabel nameLabel = new JLabel("Player name: " + playerName);
        nameLabel.setBounds(10,10, 100, 100);
        constraints.gridx = 0;
        constraints.gridy = 0;
        left.add(nameLabel, constraints);
        JLabel markLabel = new JLabel("Mark: " + mark);
        markLabel.setBounds(10,30, 100, 100);
        constraints.gridx = 0;
        constraints.gridy = 1;
        left.add(markLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.ipady = 20;
        left.add(turnIndicator, constraints);
        JPanel board = createBoard();
        board.setBounds(10, 70, 100, 100);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.ipady = 0;
        left.add(board, constraints);

        // Set up right panel, containing the message box
        JPanel right = new JPanel();
        right.setLayout(new GridBagLayout());

        JLabel messageTitle = new JLabel("Message Window");
        messageTitle.setBounds(10,10, 100, 100);
        constraints.gridx = 0;
        constraints.gridy = 0;
        right.add(messageTitle, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        right.add(messageBox, constraints);

        mainWindow.add(left);
        mainWindow.add(right);
        mainWindow.repaint();
        mainWindow.setVisible(true);
    }

    /**
     * Creates the tic tac toe board to display
     * @return
     */
    private JPanel createBoard() {
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(Constants.ROW_MAX, Constants.COL_MAX));
        populateButtons(board);
        return board;
    }

    /**
     * Attaches the buttons from the button array to a JPanel for display
     * @param board the JPanel to display
     */
    private void populateButtons(JPanel board) {
        for (int i = 0; i < buttons.size(); i++) {
            board.add(buttons.get(i).getValue());
        }
    }

    /**
     * Creates ROW_MAX * COL_MAX buttons to act as the tic tac toe grid
     */
    private void createButtons() {
        for (int i = 0; i < Constants.ROW_MAX; i++) {
            for (int j = 0; j < Constants.COL_MAX; j++) {
                JButton b = new JButton();
                String buttonKey = Integer.toString(i) + "," + Integer.toString(j);
                buttons.add(new Pair(buttonKey, b));
                b.setPreferredSize(new Dimension(50, 50));
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Send the move to the server
                        sendRowCol(b);

                        try {
                            String response = socketIn.readLine();
                            if (moveSuccess(response)) {
                                // Mark the button and disable it
                                b.setText(mark);
                                b.setEnabled(false);
                            }
                        }
                        catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * Parses the message from the server and updates the tic tac toe button array to reflect the opponent's move
     * @param response the response from the server
     */
    private void displayOpponentMove(String response) {
            String[] opponentMove = response.split(":");
            String mark = opponentMove[0];
            String coordinates = opponentMove[1];

            for (Pair buttonPair : buttons) {
                if (buttonPair.getKey().equals(coordinates)) {
                    JButton button = (JButton) buttonPair.getValue();
                    button.setText(mark);
                    button.setEnabled(false);
                }
            }
    }

    /**
     * Displays the message from the server in the GUI's message window
     * @param response the response from the server
     */
    private void displayMessage(String response) {
        response = response.replaceAll(Constants.messageIndicator, "");
        messageBox.setText(response);
    }

    /**
     * Sets the mark member variable to the contents of the server's message
     * @param response the response from the server
     */
    private void setMark(String response) {
        response = response.replaceAll(Constants.markIndicator, "");
        mark = response;
    }

    /**
     * Displays the turn indicator, and enables or disables the tic tac toe buttons,
     * depending on whether or not it is the player's turn
     * @param response
     */
    private void setTurn(String response) {
        response = response.replaceAll(Constants.turnIndicator, "");

        if (response.contains(Constants.yourTurn)) {
            response = response.replaceAll(Constants.yourTurn, "");
            setButtonsEnabled(true);
        }
        else {
            setButtonsEnabled(false);
        }

        turnIndicator.setText(response);
    }

    /**
     * Sets the enabled status of all buttons without a mark
     * @param enabled whether or not the buttons should be enabled
     */
    private void setButtonsEnabled(boolean enabled) {
        for (Pair button : buttons) {
            JButton b = (JButton)button.getValue();
            if (b.getText().equals("")) {
                b.setEnabled(enabled);
            }
        }
    }

    /**
     * Send the coordinates of the player's move to the server
     * @param b the button that was pressed
     */
    private void sendRowCol(JButton b) {
        for (Pair buttonPair : buttons) {
            if (buttonPair.getValue().equals(b)) {
                String key = (String)buttonPair.getKey();
                String[] keyValues = key.split(",");

                for (int i = 0; i < keyValues.length; i++) {
                    socketOut.println(keyValues[i]);
                }
                break;
            }
        }

    }

    /**
     * Closes sockets to terminate server connection.
     */
    private void closeSockets() {
        try {
            stdIn.close();
            socketIn.close();
            socketOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a GUI object and initiates a connection to the server
     * @param args
     */
    public static void main (String [] args) {
        GUI client = new GUI (500, 300, "localhost", 9000);
        client.communicate();
    }
}
