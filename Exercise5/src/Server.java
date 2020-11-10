import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Handles setting up connections to clients and beginning new tic tac toe game.
 * Starts a new game for every 2 client connections.
 */
public class Server {
    /**
     * A socket for the X player
     */
    private Socket socketX;

    /**
     * A socket for the O player
     */
    private Socket socketO;

    /**
     * Socket used to connect to clients
     */
    private ServerSocket serverSocket;

    /**
     * Socket used to send messages to the X player
     */
    private PrintWriter socketOutX;

    /**
     * Socket used to receive messages from the X player
     */
    private BufferedReader socketInX;

    /**
     * Socket used to send messages to the O player
     */
    private PrintWriter socketOutO;

    /**
     * Socket used to receive messages from the O player
     */
    private BufferedReader socketInO;

    /**
     * A thread pool
     */
    private ExecutorService pool;

    /**
     * Creates a Server object
     */
    public Server() {
        try {
            serverSocket = new ServerSocket(9000);
            pool = Executors.newCachedThreadPool();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up connection between the server and clients.
     * Starts a new tic tac toe game for every two clients that connect.
     */
    public void runServer() {
        try {
            while (true) {
                socketX = serverSocket.accept();

                System.out.println("Client connection accepted by the server!");
                socketInX = new BufferedReader(new InputStreamReader(socketX.getInputStream()));
                socketOutX = new PrintWriter(socketX.getOutputStream(), true);
                socketOutX.println("Waiting for another player to connect" + Constants.waitingDelimiter);

                socketO = serverSocket.accept();
                System.out.println("Client connection accepted by the server!");
                socketInO = new BufferedReader(new InputStreamReader(socketO.getInputStream()));
                socketOutO = new PrintWriter(socketO.getOutputStream(), true);

                System.out.println("Beginning new game...");
                Game game = new Game(socketOutX, socketInX, socketOutO, socketInO);
                pool.execute(game);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        closeSockets();
    }

    /**
     * Closes sockets to terminate server connection.
     */
    private void closeSockets() {
        try {
            socketInX.close();
            socketOutX.close();
            socketInO.close();
            socketOutO.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a Server object and starts the server
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Server myServer = new Server();
        System.out.println("Server is running...");
        myServer.runServer();
    }

}
