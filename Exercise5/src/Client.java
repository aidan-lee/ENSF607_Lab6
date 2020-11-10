import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Handles receiving and displaying messages from the server, and sending messages from the client back to the server
 */
public class Client {

    // A socket
    private Socket socket;

    // Socket used to write to the server
    private PrintWriter socketOut;

    // Socket used to receive messages from the server
    private BufferedReader socketIn;

    // Used to read user input from the console
    private BufferedReader stdIn;

    /**
     * Creates a Client object
     * @param serverName name of the server
     * @param portNumber port number the socket should listen on
     */
    public Client (String serverName, int portNumber) {
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
    public void communicate () {
        String line = "";
        String response = "";

        while (!line.equals("QUIT")) {
            try {
                response = socketIn.readLine();
                if (keepPrinting(response)) {
                    getEntireResponse(response);
                }
                else if (waiting(response)) {
                    waitForResponse(response);
                }
                else {
                    System.out.println(response);
                }
                line = stdIn.readLine();
                socketOut.println(line);
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
                getEntireResponse(response);
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
     * Creates a Client object and initiates a connection to the server
     * @param args
     */
    public static void main (String [] args) {
        Client client = new Client ("localhost", 9000);
        client.communicate();
    }

}
