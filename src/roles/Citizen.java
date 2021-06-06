package roles;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Citizen class.
 *  @author Alireza Nasirian
 *  @version 1.0
 */
public class Citizen extends Person{

    /**
     * Instantiates a new Citizen.
     *
     * @param role     the role
     * @param username the username
     * @param output   the output
     * @param input    the input
     */
    public Citizen(Roles role, String username, DataOutputStream output, DataInputStream input) {
        super(role, username, output, input);
        isMafia = false;
    }
}
