package roles;

import server.chat.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Citizen class.
 *
 * @author Alireza Nasirian
 * @version 1.1
 */
public class Citizen extends Person {

    /**
     * Instantiates a new Citizen.
     *
     * @param role       the role
     * @param username   the username
     * @param output     the output
     * @param input      the input
     * @param chatServer the chat server
     */
    public Citizen(Roles role, String username, DataOutputStream output, DataInputStream input, ChatServer chatServer) {
        super(role, username, output, input, chatServer);
        this.isMafia = false;
    }
}
