import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DateClient {

    // A socket
    private Socket socket;

    // A socket used to send messages to the server
    private PrintWriter socketOut;

    // A socket used to read messages from the server
    private BufferedReader socketIn;

    // Used to read input from console
    private BufferedReader stdIn;

    /**
     * Creates a DateClient object, listening on the specified port number
     * @param serverName
     * @param portNumber
     */
    public DateClient(String serverName, int portNumber) {
        try {
            socket = new Socket(serverName, portNumber);
            stdIn = new BufferedReader (new InputStreamReader(System.in));
            socketIn = new BufferedReader (new InputStreamReader (socket.getInputStream()));
            socketOut = new PrintWriter (socket.getOutputStream(), true);
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Facilitates communication between client and server
     */
    public void communicate() {
        String line;
        String response;

        boolean run = true;

        while (run) {
            try {
                System.out.println("Please select an option (DATE/TIME)");

                // Read line from console
                line = stdIn.readLine();

                if (line.equals("QUIT")) {
                    run = false;
                    // Send QUIT to server
                    socketOut.println(line);
                }
                else {
                    // Send line to server
                    socketOut.println(line);
                    // Receive response from the server
                    response = socketIn.readLine();
                    // Print server response to client console
                    System.out.println(response);
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }

        closeConnection();
    }

    /**
     * Closes sockets to end communication
     */
    public void closeConnection() {
        try {
            stdIn.close();
            socketIn.close();
            socketOut.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the connection between client and server, and begins communication
     * @param args
     * @throws IOException
     */
    public static void main(String [] args) throws IOException {
        DateClient client = new DateClient ("localhost", 9090);
        client.communicate();

    }
}
