
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
import java.util.Arrays;

public class GUI extends JFrame {

    JFrame mainWindow;
    int width;
    int height;
//    Board board;
//    Player player;

    String playerName;
    String mark = Character.toString(Constants.LETTER_X);

    // A socket
    private Socket socket;

    // Socket used to write to the server
    private PrintWriter socketOut;

    // Socket used to receive messages from the server
    private BufferedReader socketIn;

    // Used to read user input from the console
    private BufferedReader stdIn;

    ArrayList<Pair<String, JButton>> buttons;
    JTextArea messageBox;
    JLabel turnIndicator;


    public GUI(int w, int h, String serverName, int portNumber) {
        width = w;
        height = h;

        mainWindow = new JFrame();
        mainWindow.setTitle("Tic Tac Toe");
        mainWindow.setSize(width, height);
        mainWindow.setVisible(true);

        buttons = new ArrayList<>();
        createButtons();

        messageBox = new JTextArea();
        messageBox.setEditable(false);
        messageBox.setBounds(10,50, 100, 100);
        messageBox.setColumns(20);
        messageBox.setRows(10);
        messageBox.setLineWrap(true);

        turnIndicator = new JLabel(" ");



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

    public static String getPlayerName() {
        String name = JOptionPane.showInputDialog("Please enter your name");
        System.out.println("Your name is " + name);
        return name;
    }

    public void communicate() {
        String line = "";
        String response = "";

        while (!line.equals("QUIT")) {
            try {
                response = socketIn.readLine();
                System.out.print(response);
                if (waiting(response)) {
                    displayWaiting(response);
                }
                else if (requestName(response)) {
                    displayNameForm(response);
//                    line = stdIn.readLine();
//                    System.out.println("Read " + line);
//                    socketOut.println(line);
                }
                else if (opponentMove(response)) {
                    displayOpponentMove(response);
                }
                else if (receivedMessage(response)) {
                    displayMessage(response);
                }
                else if (receivedMark(response)) {
                    setMark(response);
                }
                else if (receivedTurn(response)) {
                    setTurn(response);
                }
                else {
                    displayGame(response);
                }
//                if (keepPrinting(response)) {
//                    getEntireResponse(response);
//                }
//                else if (waiting(response)) {
//                    waitForResponse(response);
//                }
//                else {
//                    System.out.println(response);
//                }
//                line = stdIn.readLine();
//                socketOut.println(line);
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

    private void displayWaiting(String response) {
        mainWindow.getContentPane().removeAll();
        response = response.replaceAll(Constants.waitingDelimiter, "");
        JLabel waitingText = new JLabel(response);
        waitingText.setBounds(10,10, 300, 30);
        mainWindow.add(waitingText);
        mainWindow.repaint();
    }

    private boolean requestName(String response) {
        if (response.contains(Constants.nameDelimiter)) {
            return true;
        }
        return false;
    }

    private boolean moveSuccess(String response) {
        if (response.contains(Constants.moveSuccess)) {
            return true;
        }
        return false;
    }

    private boolean opponentMove(String response) {
        if (response.contains(Constants.opponentMove)) {
            return true;
        }
        return false;
    }


    private boolean receivedMessage(String response) {
        if (response.contains(Constants.messageIndicator)) {
            return true;
        }
        return false;
    }

    private boolean receivedMark(String response) {
        if (response.contains(Constants.markIndicator)) {
            return true;
        }
        return false;
    }

    private boolean receivedTurn(String response) {
        if (response.contains(Constants.turnIndicator)) {
            return true;
        }
        return false;
    }

    private void displayNameForm(String response) {
        response = response.replaceAll(Constants.nameDelimiter, "");
        String name = JOptionPane.showInputDialog(response);
        playerName = name;
        socketOut.println(name);


//        mainWindow.getContentPane().removeAll();
//        response = response.replaceAll(Constants.nameDelimiter, "");
//        JLabel prompt = new JLabel(response);
//        prompt.setBounds(10,10, 300, 30);
//        mainWindow.add(prompt);
//
//        JTextField textField = new JTextField();
//        textField.setBounds(30,30, 150,20);
//        mainWindow.add(textField);
//
//        JButton submitName = new JButton("Find IP");
//        submitName.setBounds(50,150,95,30);
//        submitName.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String name = textField.getText();
//                socketOut.println(name);
//
////                mainWindow.getContentPane().removeAll();
////                mainWindow.repaint();
//                return;
//            }
//        });
//        mainWindow.add(submitName);
//        mainWindow.repaint();
    }

    private void displayGame(String turn) {
        mainWindow.getContentPane().removeAll();
        mainWindow.setLayout(new GridLayout(1, 2, 50, 50));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Set up left panel, containing the board
        JPanel left = new JPanel();
//        left.setLayout(new GridLayout());
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
//        JLabel turnIndicator = new JLabel(turn);
//        turnIndicator.setBounds(10,60, 100, 100);

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
//        right.setLayout(new GridLayout(2, 1));
//        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        //        right.setAlignmentX(Component.CENTER_ALIGNMENT);

        right.setLayout(new GridBagLayout());

        JLabel messageTitle = new JLabel("Message Window");
        messageTitle.setBounds(10,10, 100, 100);
        constraints.gridx = 0;
        constraints.gridy = 0;
        right.add(messageTitle, constraints);
//        pane.add(button, c);
//        right.add(messageTitle);
        constraints.gridx = 0;
        constraints.gridy = 1;
        right.add(messageBox, constraints);

        mainWindow.add(left);
        mainWindow.add(right);
        mainWindow.repaint();
        mainWindow.setVisible(true);

    }

    private JPanel createBoard() {
        System.out.println("creating board");
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(Constants.ROW_MAX, Constants.COL_MAX));
//        JButton b1 = new JButton();

        populateButtons(board);

//        int buttonNum = Constants.ROW_MAX * Constants.COL_MAX;


        return board;
    }

    private void populateButtons(JPanel board) {
        for (int i = 0; i < buttons.size(); i++) {
            board.add(buttons.get(i).getValue());
        }
    }

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
                        sendRowCol(b);

                        try {
                            String response = socketIn.readLine();
                            if (moveSuccess(response)) {
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

    private void displayMessage(String response) {
        response = response.replaceAll(Constants.messageIndicator, "");
        messageBox.setText(response);
    }

    private void setMark(String response) {
        response = response.replaceAll(Constants.markIndicator, "");
        mark = response;
    }

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

    private void setButtonsEnabled(boolean enabled) {
        for (Pair button : buttons) {
            JButton b = (JButton)button.getValue();
            if (b.getText().equals("")) {
                b.setEnabled(enabled);
            }
        }
    }

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

    public static void main (String [] args) {
        GUI client = new GUI (500, 300, "localhost", 9000);
        client.communicate();
    }

}
