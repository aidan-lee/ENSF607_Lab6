
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class GUI extends JFrame {

    JFrame mainWindow;
    int width;
    int height;
//    Board board;
    Player player;

    // A socket
    private Socket socket;

    // Socket used to write to the server
    private PrintWriter socketOut;

    // Socket used to receive messages from the server
    private BufferedReader socketIn;

    // Used to read user input from the console
    private BufferedReader stdIn;

    public GUI(int w, int h, String serverName, int portNumber) {
        width = w;
        height = h;

        mainWindow = new JFrame();
        mainWindow.setTitle("Tic Tac Toe");
        mainWindow.setSize(width, height);
        mainWindow.setVisible(true);

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
     * Prints a multi-line server response to the client's console
     * (ie. When printing the tic tac toe board)
     * @param response the first line of the server response
     */
    private void getEntireResponse(String response) {
        try {
            while (keepPrinting(response)) {
                response = response.replaceAll(Constants.printingDelimiter, "");
                response += "\n" + socketIn.readLine();
            }
            if (waiting(response)) {
                waitForResponse(response);
            }
            else {
                System.out.println(response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints multiple server responses to the client's console
     * (ie. When waiting for the other player to make their move)
     * @param response
     */
    private void waitForResponse(String response) {
        try {
            while (waiting(response)) {
                response = response.replaceAll(Constants.waitingDelimiter, "");
                System.out.println(response);
                response = socketIn.readLine();
            }
            if (keepPrinting(response)) {
                displayNameForm(response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Looks for the printingDelimiter substring in a server response
     * @param response the server response
     * @return true if the delimiter is found, false otherwise
     */
    private boolean keepPrinting(String response) {
        if (response.contains(Constants.printingDelimiter)) {
            return true;
        }
        return false;
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

    private void displayNameForm(String response) {

        String name = JOptionPane.showInputDialog(response);
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
