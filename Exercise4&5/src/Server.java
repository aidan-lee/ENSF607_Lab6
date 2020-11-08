import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {

    private Socket socket1;
    private Socket socket2;
    private ServerSocket serverSocket;
    private PrintWriter socketOut1;
    private BufferedReader socketIn1;
    private PrintWriter socketOut2;
    private BufferedReader socketIn2;

    private ExecutorService pool;

    public Server() {
        try {
            serverSocket = new ServerSocket(9000);
            pool = Executors.newCachedThreadPool();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void runServer() {

        try {
            while (true) {
                socket1 = serverSocket.accept();
                System.out.println("Console at Server side says: Connection accepted by the server!");
                socketIn1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                socketOut1 = new PrintWriter(socket1.getOutputStream(), true);

                System.out.println("Console at Server side says: Connection accepted by the server!");
                socket2 = serverSocket.accept();
                socketIn2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
                socketOut2 = new PrintWriter(socket2.getOutputStream(), true);

                Game game = new Game(socketOut1, socketIn1, socketOut2, socketIn2);
                pool.execute(game);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        try {
            socketIn1.close();
            socketOut1.close();
            socketIn2.close();
            socketOut2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {

        Server myServer = new Server();
        myServer.runServer();
    }

}
