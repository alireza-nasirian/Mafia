package roles;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The  Dr lecter class.
 * @author Alireza Nasirian
 * @version 1.0
 */
public class DrLecter extends Mafia{

    private int save_himself;

    /**
     * Instantiates a new Dr lecter.
     *
     * @param role     the role
     * @param username the username
     * @param output   the output
     * @param input    the input
     */
    public DrLecter(Roles role, String username, DataOutputStream output, DataInputStream input) {
        super(role, username, output, input);
        save_himself = 1;
    }

    /**
     * Gets save himself.
     *
     * @return the save himself
     */
    public int getSave_himself() {
        return save_himself;
    }
}
