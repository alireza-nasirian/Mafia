package roles;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Mafia class.
 *  @author Alireza Nasirian
 *  @version 1.0
 */
public class Mafia extends Person {

    /**
     * Instantiates a new Mafia.
     *
     * @param role     the role
     * @param username the username
     * @param output   the output
     * @param input    the input
     */
    public Mafia(Roles role, String username, DataOutputStream output, DataInputStream input) {
        super(role, username, output, input);
        isMafia = true;
    }
}
