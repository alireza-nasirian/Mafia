package roles;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Die hard class.
 * @author Alireza Nasirian
 * @version 1.0
 */
public class DieHard extends Citizen{

    private int inquiry;

    /**
     * Instantiates a new Die hard.
     *
     * @param role     the role
     * @param username the username
     * @param output   the output
     * @param input    the input
     */
    public DieHard(Roles role, String username, DataOutputStream output, DataInputStream input) {
        super(role, username, output, input);
        inquiry = 2;
        alive = 2;
    }

    /**
     * Gets inquiry.
     *
     * @return the inquiry
     */
    public int getInquiry() {
        return inquiry;
    }
}
