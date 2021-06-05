package server.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The  User thread class.
 */
public class UserThread extends Thread {

    private ChatServer chatServer;
    private DataInputStream input;
    private DataOutputStream output;
    private String username;

    /**
     * Instantiates a new User thread.
     *
     * @param server   the server to connect
     * @param output   the output stream
     * @param input    the input stream
     * @param username the username of member
     */
    public UserThread(ChatServer server,  DataOutputStream output,DataInputStream input, String username){
        this.chatServer = server;
        this.input = input;
        this.output = output;
        this.username = username;
    }

}
