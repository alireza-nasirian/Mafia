package roles;

import server.chat.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The  Dr lecter class.
 *
 * @author Alireza Nasirian
 * @version 1.0
 */
public class DrLecter extends Mafia {

    private int save_himself;

    /**
     * Instantiates a new Dr lecter.
     *
     * @param role       the role
     * @param username   the username
     * @param output     the output
     * @param input      the input
     * @param chatServer the chat server
     */
    public DrLecter(Roles role, String username, DataOutputStream output, DataInputStream input, ChatServer chatServer) {
        super(role, username, output, input, chatServer);
        this.save_himself = 1;
    }

    /**
     * Gets save himself.
     *
     * @return the save himself
     */
    public int getSave_himself() {
        return save_himself;
    }

    /**
     * sets save himself
     *
     * @param save_himself is save himself
     */
    public void setSave_himself(int save_himself) {
        this.save_himself = save_himself;
    }
}
