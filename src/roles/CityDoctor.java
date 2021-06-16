package roles;

import server.chat.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The City doctor class.
 *
 * @author Alireza Nasirian
 * @version 1.1
 */
public class CityDoctor extends Citizen {

    private int save_himself;

    /**
     * Instantiates a new City doctor.
     *
     * @param role       the role
     * @param username   the username
     * @param output     the output
     * @param input      the input
     * @param chatServer the chat server
     */
    public CityDoctor(Roles role, String username, DataOutputStream output, DataInputStream input, ChatServer chatServer) {
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
