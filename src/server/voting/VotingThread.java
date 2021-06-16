package server.voting;

import roles.Person;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The type Voting thread.
 * @author Alireza Nasirian
 * @version 1.2
 */
public class VotingThread extends Thread{

    private final VotingServer votingServer;
    private Person voter;
    private boolean finished = false;

    /**
     * Instantiates a new Voting thread.
     *
     * @param votingServer the voting server
     * @param voter        the voter
     */
    public VotingThread(VotingServer votingServer, Person voter) {
        this.votingServer = votingServer;
        this.voter = voter;
    }

    @Override
    public void run() {
        finished = false;
        try {
            String vote;
            int i ;
            for (Person person : votingServer.voters){
                if (person == voter){
                    continue;
                }
                voter.getOutput().writeUTF(person.getUsername());
            }
            voter.getOutput().writeUTF("which player is mafia? type hes/her name.");

            long start = System.currentTimeMillis();
            long end = start + votingServer.getSeconds() * 1000L;


            vote = voter.getInput().readUTF();
            if (System.currentTimeMillis() > end){
                finished = true;
                return;
            }
            Person person = search(votingServer.voters, vote);
            if (person != null){
                i = votingServer.voters.indexOf(person);
                votingServer.votes.set(i, votingServer.votes.get(i) + 1);
                votingServer.chatServer.broadcast(voter.getUsername() + " voted for " + person.getUsername());
            }
            finished = true;

        }catch (IOException ex){
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * find a player with given username in given list
     * returns null if a player didnt exist in list with given username
     *
     * @param people is given list
     * @param name   is given username
     * @return found person
     */
    private Person search(ArrayList<Person> people, String name) {
        for (Person person : people) {
            if (name.equalsIgnoreCase(person.getUsername())) {
                return person;
            }
        }
        return null;
    }


}
