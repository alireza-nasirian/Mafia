package roles;

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
     * Instantiates a new Die hard.
     *
     * @param role     the role
     * @param username the username
     * @param output   the output
     * @param input    the input
     */
    public Godfather(Roles role, String username, DataOutputStream output, DataInputStream input) {
        super(role, username, output, input);
        isMafia = false;
    }
}
