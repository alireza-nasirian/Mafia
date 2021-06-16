package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * The Read thread.
 * @author Alireza Nasirian
 * @version 1.0
 */
public class ReadThread extends Thread{

    private DataInputStream input;
    private Socket socket;
    private Client client;

    /**
     * Instantiates a new Read thread.
     *
     * @param socket the socket
     * @param client the client
     */
    public ReadThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream inputStream = socket.getInputStream();
            input = new DataInputStream(inputStream);
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * read message from socket and print it
     */
    public void run() {
        while (true) {
            try {
                String response = input.readUTF();
                System.out.println(response);
            }
            catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                System.out.println("game is finished");
                break;
            }
        }
    }
}

