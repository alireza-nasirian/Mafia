package server.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The  User thread class.
 * @author Alireza Nasirian
 * @version 1.3
 */
public class UserThread extends Thread {

    private ChatServer chatServer;
    private DataInputStream input;
    private DataOutputStream output;
    private String username;
    private boolean finish = false;

    /**
     * Instantiates a new User thread.
     *
     * @param server   the server to connect
     * @param output   the output stream
     * @param input    the input stream
     * @param username the username of member
     */
    public UserThread(ChatServer server, DataOutputStream output, DataInputStream input, String username) {
        this.chatServer = server;
        this.input = input;
        this.output = output;
        this.username = username;
    }

    /**
     * receive a message from a user and send it to others
     */
    @Override
    public void run() {
        finish = false;
        try {
            String clientMessage;
            String serverMassage;
            long start = System.currentTimeMillis();
            long end = start + chatServer.getSeconds() * 1000L;

            while (System.currentTimeMillis() < end) {
                clientMessage = input.readUTF();
                serverMassage = "[" + username + "]: " + clientMessage;
                if (System.currentTimeMillis() > end){
                    serverMassage = serverMassage + " (last word)";
                }
                chatServer.broadcast(serverMassage);
            }
            finish = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * send a message to a client
     *
     * @param message is message to be sent
     */
    protected void sendMessage(String message) throws IOException {

            output.writeUTF(message);
    }

    public boolean isFinish() {
        return finish;
    }
}
