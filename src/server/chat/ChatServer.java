package server.chat;

import java.util.ArrayList;

/**
 * The  Chat server class.
 */
public class ChatServer {

    private ArrayList<UserThread> userThreads = new ArrayList<>();
    private int seconds;

    /**
     * Start chat method starts chat with given members and time.
     *
     * @param userThreads is members ti chat
     * @param seconds     is time of chatting
     */
    public void startChat(ArrayList<UserThread> userThreads, int seconds){

        this.seconds = seconds;
        this.userThreads = userThreads;
        for (UserThread u : userThreads){
            u.start();
        }
    }

}
