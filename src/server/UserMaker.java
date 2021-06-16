package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * User maker is a temporary class for transferring information from server to god class.
 *
 * @author Alireza Nasirian
 * @version 1.0
 */
public class UserMaker extends Thread {

    private DataOutputStream output;
    private DataInputStream input;
    private String username;
    ArrayList<String> usernames;
    private boolean ready = false;

    /**
     * Instantiates a new User maker.
     *
     * @param output    is the output stream
     * @param input     is the input stream
     * @param usernames the usernames
     */
    public UserMaker(DataOutputStream output, DataInputStream input, ArrayList<String> usernames) {
        this.output = output;
        this.input = input;
        this.usernames = usernames;
    }

    /**
     * checks that a username is repetitious or no.
     * @param username is name to check.
     * @param usernames is list of chosen names.
     * @return true if the given name is repetitious.
     */
    private boolean checkUsername(String username, ArrayList<String> usernames) {
        boolean state = false;
        for (String name : usernames) {
            if (name.equalsIgnoreCase(username)) {
                state = true;
                break;
            }
        }
        return state;
    }


}
