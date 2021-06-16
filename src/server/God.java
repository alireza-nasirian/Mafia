package server;

import roles.*;
import server.chat.ChatServer;
import server.voting.VotingServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * God class manges game.
 *
 * @author Alireza Nasirian
 * @version 1.6
 */
public class God {

    private static final int CHAT_TIME = 5;
    private static final int VOTING_TIME = 30;
    private static final int MAFIAS_CHAT_TIME = 20;

    private ChatServer chatServer = new ChatServer(this);
    private VotingServer votingServer = new VotingServer();
    private ArrayList<UserMaker> userMakers;
    public ArrayList<Person> alive_persons = new ArrayList<>();
    public ArrayList<Person> observers = new ArrayList<>();    // these players can see chats and game information
    public ArrayList<Person> mafias = new ArrayList<>();
    public ArrayList<Person> dead = new ArrayList<>();
    public ArrayList<Person> citizens = new ArrayList<>();
    private ArrayList<Integer> votes = new ArrayList<>();

    private Godfather godfather = null;
    private DrLecter drLecter = null;
    private Mafia simpleMafia = null;
    private CityDoctor cityDoctor = null;
    private Citizen detective = null;
    private Citizen professional = null;
    private Citizen mayor = null;
    private Citizen psychologist = null;
    private DieHard dieHard = null;
    private Citizen simpleCitizen = null;

    private Person silentPlayer = null;      // psychologist talked this player down.
    private String deadRoles = null;        // this string contains the roles that died last night.
    private String inquiredRoles = null;   // if die hard inquire roles of dead players this string will contain those roles.


    /**
     * Instantiates a new God.
     *
     * @param users the userMakers that server made.
     */
    public God(ArrayList<UserMaker> users) {
        this.userMakers = users;
    }


}
