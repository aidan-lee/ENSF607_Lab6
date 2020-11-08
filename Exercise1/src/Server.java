import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // A socket
    private Socket socket;

    // Socket used to connect to the client
    private ServerSocket serverSocket;

    // Used to print to the client's console
    private PrintWriter socketOut;

    // Used to read what the client has entered on their console
    private BufferedReader socketIn;

    /**
     * Creates a Server object, listening on port 8099
     */
    public Server() {
        try {
            serverSocket = new ServerSocket (8099);
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Facilitates communication between client and server
     */
    public void communicate() {
        String line ;
        boolean run = true;

        while (run) {
            try {
                line = socketIn.readLine();
                System.out.println("Received " + line + " from client");

                if (line.equals("QUIT")) {
                    line = "Goodbye client!";
                    socketOut.println(line);
                    run = false;
                }

                boolean isPalindrome = checkPalindrome(line);

                String response = (line + " is");
                if (!isPalindrome) {
                    response += " not";
                }
                response += " a palindrome";

                socketOut.println(response);

            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the passed string is a palindrome
     * @param check The string to check
     * @return A boolean: true if the string is a palindrome, false otherwise
     */
    public boolean checkPalindrome(String check) {
        int length = check.length();
        for (int i = 0; i < length; i++) {
            char ch1 = check.charAt(i);
            char ch2 = check.charAt(length - i - 1);

            if (ch1 != ch2) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets up the connection between client and server, begins the palindrome checking, and cleans up afterward.
     * @param args
     */
    public static void main(String [] args) {
        Server server = new Server();
        System.out.println("Server is now running...");

        try {
            // Setting up the connection between client and server
            server.socket = server.serverSocket.accept();
            System.out.println("Connection accepted by the server!");
            server.socketIn = new BufferedReader (new InputStreamReader(server.socket.getInputStream()));
            server.socketOut = new PrintWriter (server.socket.getOutputStream(), true);

            server.communicate();

            // Closing connection
            server.socketIn.close();
            server.socketOut.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
