import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket aSocket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private BufferedReader stdIn;

    public Client (String serverName, int portNumber) {

        try {
            aSocket = new Socket (serverName, portNumber);
            //keyboard input stream
            stdIn = new BufferedReader (new InputStreamReader (System.in));
            socketIn = new BufferedReader (new InputStreamReader (aSocket.getInputStream()));
            socketOut = new PrintWriter (aSocket.getOutputStream(), true);
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void communicate () {
        String line = "";
        String response = "";

        while (!line.equals("QUIT")) {
            try {
                response = socketIn.readLine();
                if (keepPrinting(response)) {
                    getEntireResponse(response);
//                    System.out.println(response);
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


//            System.out.println("Enter a word to capitalize or type QUIT to end:");
//            try {
//                line = stdIn.readLine();
//                socketOut.println(line);
//                response = socketIn.readLine();  //read response form the socket
//                System.out.println("The response is: "+ response);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } //reading the input from the user (i.e. the keyboard);

        }
        closeSocket ();

    }

    private void getEntireResponse(String response) {
        try {
            while (keepPrinting(response)) {
                response = response.replaceAll(Constants.printingDelimiter, "");
                response += "\n" + socketIn.readLine();
            }
//            System.out.println(response);
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

    private void waitForResponse(String response) {
        try {
            while (waiting(response)) {
                response = response.replaceAll(Constants.waitingDelimiter, "");
                System.out.println(response);
                response = socketIn.readLine();
            }
            if (keepPrinting(response)) {
                getEntireResponse(response);
//                System.out.println(response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean keepPrinting(String response) {
        if (response.contains(Constants.printingDelimiter)) {
            return true;
        }
        return false;
    }

    private boolean waiting(String response) {
        if (response.contains(Constants.waitingDelimiter)) {
            return true;
        }
        return false;
    }

    private void closeSocket () {

        try {
            stdIn.close();
            socketIn.close();
            socketOut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public static void main (String [] args) throws IOException {

        Client aClient = new Client ("localhost", 9000);
        aClient.communicate();

    }

}
