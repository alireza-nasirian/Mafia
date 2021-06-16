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

    /**
     * @param person is player to be checked
     * @return false if player is dead or null.
     */
    private boolean inGame(Person person) {
        return (person != null) && (alive_persons.contains(person));
    }


    /**
     * send a usernames of given list to given player.
     *
     * @param people is given list.
     * @param person is given player.
     */
    private void printList(ArrayList<Person> people, Person person) {
        int i = 0;
        for (Person person1 : people) {
            try {
                person.getOutput().writeUTF(++i + "- " + person1.getUsername());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                kickOut(person);
            }
        }
    }

    /**
     * shuffle userMakers and play the roles randomly and Instantiates players.
     */
    public void playRoles() {
        Collections.shuffle(userMakers);
        Iterator iterator = userMakers.iterator();
        UserMaker userMaker = (UserMaker) iterator.next();
        detective = new Citizen(Roles.DETECTIVE, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
        alive_persons.add(detective);
        citizens.add(detective);
        userMaker = (UserMaker) iterator.next();
        cityDoctor = new CityDoctor(Roles.CITY_DOCTOR, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
        alive_persons.add(cityDoctor);
        citizens.add(cityDoctor);
        userMaker = (UserMaker) iterator.next();
        godfather = new Godfather(Roles.GODFATHER, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
        alive_persons.add(godfather);
        mafias.add(godfather);
        if (iterator.hasNext()) {
            userMaker = (UserMaker) iterator.next();
            professional = new Citizen(Roles.PROFESSIONAL, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
            alive_persons.add(professional);
            citizens.add(professional);
        }
        if (iterator.hasNext()) {
            userMaker = (UserMaker) iterator.next();
            dieHard = new DieHard(Roles.DIE_HARD, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
            alive_persons.add(dieHard);
            citizens.add(dieHard);
        }
        if (iterator.hasNext()) {
            userMaker = (UserMaker) iterator.next();
            drLecter = new DrLecter(Roles.DR_LECTER, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
            alive_persons.add(drLecter);
            mafias.add(drLecter);
        }
        if (iterator.hasNext()) {
            userMaker = (UserMaker) iterator.next();
            mayor = new Citizen(Roles.MAYOR, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
            alive_persons.add(mayor);
            citizens.add(mayor);
        }
        if (iterator.hasNext()) {
            userMaker = (UserMaker) iterator.next();
            psychologist = new Citizen(Roles.PSYCHOLOGIST, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
            alive_persons.add(psychologist);
            citizens.add(psychologist);
        }
        if (iterator.hasNext()) {
            userMaker = (UserMaker) iterator.next();
            simpleMafia = new Mafia(Roles.SIMPLE_MAFIA, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
            alive_persons.add(simpleMafia);
            mafias.add(simpleMafia);
        }
        if (iterator.hasNext()) {
            userMaker = (UserMaker) iterator.next();
            simpleCitizen = new Citizen(Roles.SIMPLE_CITIZEN, userMaker.getUsername(), userMaker.getOutput(), userMaker.getInput(), chatServer);
            alive_persons.add(simpleCitizen);
            citizens.add(simpleCitizen);
        }
        observers.addAll(alive_persons);
    }

    /**
     * remove a player that disconnected
     * @param person is disconnected person
     */
    public void kickOut(Person person) {

        alive_persons.remove(person);
        observers.remove(person);
        if (person instanceof Mafia) {
            mafias.remove(person);
        } else {
            citizens.remove(person);
        }
        dead.add(person);

    }


    /**
     * send the role of every player to himself.
     * introduce mafias to each other and introduce city doctor to mayor.
     */
    public void introduction() {
        for (Person person : alive_persons) {
            try {
                person.getOutput().writeUTF("you are " + person.getRole());
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                kickOut(person);
            }
        }
        if (mayor != null && cityDoctor != null) {
            try {
                mayor.getOutput().writeUTF(mayor.getUsername() + " is city doctor.");
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                kickOut(mayor);
            }
        }
        if (simpleMafia != null) {
            try {
                godfather.getOutput().writeUTF(simpleMafia.getUsername() + " is Simple Mafia");
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                kickOut(godfather);
            }
            try {
                simpleMafia.getOutput().writeUTF(godfather.getUsername() + " is God Father");
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                kickOut(simpleMafia);
            }
            if (drLecter != null) {
                try {
                    drLecter.getOutput().writeUTF(simpleMafia.getUsername() + " is Simple Mafia");
                } catch (IOException ioException) {
                    System.out.println(ioException.getMessage());
                    kickOut(drLecter);
                }
                try {
                    simpleMafia.getOutput().writeUTF(drLecter.getUsername() + " is DrLecter");
                } catch (IOException ioException) {
                    System.out.println(ioException.getMessage());
                    kickOut(simpleMafia);
                }
            }
        }
        if (drLecter != null) {
            try {
                godfather.getOutput().writeUTF(drLecter.getUsername() + "is DrLecter");
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                kickOut(godfather);
            }
            try {
                drLecter.getOutput().writeUTF(godfather.getUsername() + " is God Father");
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                kickOut(drLecter);
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mafias chat with each other and shoot to someone.
     *
     * @return the person that has been killed.
     */
    public Person mafiaShoot() {
        for (Person mafia : mafias) {
            try {
                mafia.getOutput().writeUTF("start chatting for " + MAFIAS_CHAT_TIME +" seconds. Finally the godfather chooses the victim.\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                kickOut(mafia);
            }
        }
        chatServer.startChat(mafias, mafias, MAFIAS_CHAT_TIME);
        checkChatFinished(mafias);
        Person chooser;
        if (inGame(godfather)) {
            chooser = godfather;
        } else if (inGame(drLecter)) {
            chooser = drLecter;
        } else if (inGame(simpleMafia)) {
            chooser = simpleMafia;
        } else {
            return null;
        }
        try {
            chooser.getOutput().writeUTF("write the name of one player to shoot:");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            kickOut(chooser);
        }
        printList(citizens, chooser);
        String name = null;
        try {
            name = chooser.getInput().readUTF();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            kickOut(chooser);
        }
        Person person = search(citizens, name);
        if (person == null) {
            return null;
        } else {
            person.setAlive((person.getAlive() - 1));
            return person;
        }
    }





}
