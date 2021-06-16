package server.chat;

import roles.Mafia;
import roles.Person;

import java.io.IOException;
import java.util.ArrayList;

import server.God;

/**
 * The  Chat server class.
 *
 * @author Alireza Nasirian
 * @version 1.3
 */
public class ChatServer {

    private static ArrayList<Person> talkerUsers = new ArrayList<>();
    private static ArrayList<Person> listenerUsers = new ArrayList<>();
    private int seconds;
    protected God god;

    public ChatServer(God god) {
        this.god = god;
    }

    /**
     * Start chat method starts chat with given members and time.
     *
     * @param talkers   is members to chat
     * @param listeners can read chats
     * @param seconds   is time of chatting
     */
    public void startChat(ArrayList<Person> talkers, ArrayList<Person> listeners, int seconds) {

        this.seconds = seconds;

        setTalkerUsers(talkers);
        setListenerUsers(listeners);
        ArrayList<UserThread> userThreads = new ArrayList<>();
        for (Person person : talkers) {
            UserThread userThread = new UserThread(this, person.getOutput(), person.getInput(), person.getUsername());
            person.userThread = userThread;
            userThread.start();
        }
    }

    /**
     * Broadcast method send a message to members.
     *
     * @param message is message to be sent
     */
    public void broadcast(String message) {
        for (Person person : listenerUsers) {
            try {
                person.getUserThread().sendMessage(message);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                god.alive_persons.remove(person);
                god.observers.remove(person);
                if (person instanceof Mafia) {
                    god.mafias.remove(person);
                } else {
                    god.citizens.remove(person);
                }
                god.dead.add(person);
            }
        }
    }

    /**
     * @return time of chat.
     */
    public int getSeconds() {
        return seconds;
    }


    /**
     * Sets listener users.
     *
     * @param listeners the listeners
     */
    public void setListenerUsers(ArrayList<Person> listeners) {
        listenerUsers = new ArrayList<>();
        listenerUsers = listeners;
    }


    /**
     * Sets talker users.
     *
     * @param talkers the talkers
     */
    public void setTalkerUsers(ArrayList<Person> talkers) {
        talkerUsers = new ArrayList<>();
        talkerUsers = talkers;
    }
}
