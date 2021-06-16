package roles;

import server.chat.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Mafia class.
 *
 * @author Alireza Nasirian
 * @version 1.1
 */
public class Mafia extends Person {

    /**
     * Instantiates a new Mafia.
     *
     * @param role       the role
     * @param username   the username
     * @param output     the output
     * @param input      the input
     * @param chatServer the chat server
     */
    public Mafia(Roles role, String username, DataOutputStream output, DataInputStream input, ChatServer chatServer) {
        super(role, username, output, input, chatServer);
        this.isMafia = true;
    }
}
