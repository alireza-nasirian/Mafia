package Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * The Client class.
 *
 * @author Alireza Nasirian
 * @version 1.2
 */
public class Client {

    private static final String hostName = "127.0.0.1";
    private String userName;
    private static final Scanner sc = new Scanner(System.in);

    /**
     * run read an write thread.
     */
    public void execute() {
        System.out.println("enter the port number to connect:");
        int port = sc.nextInt();
        try {
            Socket socket = new Socket(hostName, port);
            System.out.println("Connected to the server");
            System.out.println("type your username:");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    /**
     * Sets user name.
     *
     * @param userName is user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    String getUserName() {
        return this.userName;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.execute();
    }
}
