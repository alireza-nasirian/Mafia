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

    @Override
    public void run() {
        String name = "";
        while (true) {
            try {
                name = input.readUTF();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (!checkUsername(name, usernames)) {
                break;
            } else {
                try {
                    output.writeUTF("this username is already chosen. try another username:");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        usernames.add(name);
        this.username = name;
        String s = "";
        try {
            output.writeUTF("type \"ready\" to start the game.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!s.equalsIgnoreCase("ready")) {
            try {
                s = input.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (s.equalsIgnoreCase("ready")) {
                ready = true;
                break;
            }
        }
        System.out.println(getUsername() + " is ready");
    }


    /**
     * Gets output.
     *
     * @return the output
     */
    public DataOutputStream getOutput() {
        return output;
    }

    /**
     * Gets input.
     *
     * @return the input
     */
    public DataInputStream getInput() {
        return input;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return true if user type "ready"
     */
    public boolean isReady() {
        return ready;
    }


}
