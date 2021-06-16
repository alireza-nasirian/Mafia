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


}
