package roles;

import server.chat.ChatServer;
import server.chat.UserThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Person class.
 *
 * @author Alireza Nasirian
 * @version 1.1
 */
public class Person {

    protected Roles role;
    protected boolean isMafia;
    protected String username;
    protected int alive;
    protected DataOutputStream output;
    protected DataInputStream input;
    public UserThread userThread;

    /**
     * Instantiates a new Person.
     *
     * @param role     the role
     * @param username the username
     */
    public Person(Roles role, String username, DataOutputStream output, DataInputStream input, ChatServer chatServer) {
        this.role = role;
        this.username = username;
        this.alive = 1;
        this.output = output;
        this.input = input;
        this.userThread = new UserThread(chatServer, output, input, username);
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public Roles getRole() {
        return role;
    }

    /**
     * Gets alive.
     *
     * @return the alive
     */
    public int getAlive() {
        return alive;
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
     * Gets is mafia.
     *
     * @return the is mafia
     */
    public boolean getIsMafia() {
        return isMafia;
    }

    /**
     * get output stream
     *
     * @return output
     */
    public DataOutputStream getOutput() {
        return output;
    }

    /**
     * get input stream
     *
     * @return input
     */
    public DataInputStream getInput() {
        return input;
    }

    /**
     * get user thread
     *
     * @return userThread
     */
    public UserThread getUserThread() {
        return userThread;
    }

    /**
     * Sets alive.
     *
     * @param alive the alive
     */
    public void setAlive(int alive) {
        this.alive = alive;
    }
}
