package server.voting;

import roles.Person;
import server.chat.ChatServer;

import java.util.ArrayList;

/**
 * The type Voting server.
 * @author Alireza Nasirian
 * @version 1.1
 */
public class VotingServer {


    protected ArrayList<Person> voters;
    public ArrayList<Integer> votes;
    protected ChatServer chatServer;
    private int seconds;
    private ArrayList<VotingThread> threads;

    /**
     * Start voting.
     *
     * @param chatServer the chat server
     * @param people     the voters
     * @param votes      the votes
     * @param seconds    voting duration
     */
    public void StartVoting(ChatServer chatServer, ArrayList<Person> people, ArrayList<Integer> votes ,int seconds){
        this.chatServer = chatServer;
        this.threads = new ArrayList<>();
        this.voters = people;
        this.votes = votes;
        this.seconds = seconds;
        for (Person person : voters){
            VotingThread votingThread = new VotingThread(this, person);
            threads.add(votingThread);
            votingThread.start();
        }
    }

}
