package roles;

import server.chat.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Godfather class.
 *
 * @author Alireza Nasirian
 * @version 1.0
 */
public class Godfather extends Mafia {

    /**
     * Instantiates a new Dr lecter.
     *
     * @param role       the role
     * @param username   the username
     * @param output     the output
     * @param input      the input
     * @param chatServer the chat server
     */
    public Godfather(Roles role, String username, DataOutputStream output, DataInputStream input, ChatServer chatServer) {
        super(role, username, output, input, chatServer);
        this.isMafia = false;
    }
}
