package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Write thread.
 */
public class WriteThread extends Thread{

    private DataOutputStream output;
    private Socket socket;
    private Client client;

    /**
     * Instantiates a new Write thread.
     *
     * @param socket the socket
     * @param client the client
     */
    public WriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream outputStream = socket.getOutputStream();
            output = new DataOutputStream(outputStream);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * scan the input and send it to server via socket
     */
    public void run() {

        //Console console = System.console();
        Scanner sc = new Scanner(System.in);

        String text;

        do {
            text = sc.nextLine();//("[" + userName + "]: ");
            try {
                output.writeUTF(text);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
