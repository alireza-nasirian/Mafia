package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server of mafia game.
 * gets input and output stream from sockets and make userMaker with them
 * and make a god with list of userMakers
 */
public class Server {

    private static ArrayList<UserMaker> userMakers = new ArrayList<>();
    private static ArrayList<String> usernames = new ArrayList<>();

    private static final int MAX_PLAYERS = 3;
    private static final int JOINING_TIME = 120;


    public static void main(String[] args) {

        int num = 0;
        long start = System.currentTimeMillis();
        long end = start + JOINING_TIME * 1000;
        System.out.println("waiting for clients...");

        try (ServerSocket serverSocket = new ServerSocket(8989)) {

            while (num < MAX_PLAYERS) {
                Socket socket = serverSocket.accept();
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());
                UserMaker userMaker = new UserMaker(output, input, usernames);
                userMakers.add(userMaker);
                userMaker.start();
                num++;
                if ((System.currentTimeMillis() > end) && (num >= 3)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        God god = new God(userMakers);
        System.out.println("new game has been made.");
        god.gameLoop();
    }
}