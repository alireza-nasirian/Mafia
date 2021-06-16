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

    /**
     * save a mafia that drLecter said.
     *
     * @return the person that has been saved.
     * @throws IOException .
     */
    public Person lecterSave() throws IOException {
        if (!inGame(drLecter)) {
            return null;
        }
        drLecter.getOutput().writeUTF("choose one of the mafias to save.\nyou can only save yourself once.");
        printList(mafias, drLecter);
        drLecter.getOutput().writeUTF("write the name of the mafia you want to save.");
        String name = drLecter.getInput().readUTF();
        Person choose = search(mafias, name);
        if (choose == null) {
            return null;
        } else if (choose == drLecter) {
            if (drLecter.getSave_himself() == 0) {
                drLecter.getOutput().writeUTF("you have already saved yourself.\nno mafia will be saved tonight");
                return null;
            } else {
                drLecter.setAlive(1);
                drLecter.setSave_himself(0);
                return drLecter;
            }
        } else {
            choose.setAlive(1);
            return choose;
        }
    }


    /**
     * save a person that cityDoctor said.
     *
     * @return the person that has been saved.
     * @throws IOException .
     */
    public Person cityDoctorSave() throws IOException {
        if (!inGame(cityDoctor)) {
            return null;
        }
        cityDoctor.getOutput().writeUTF("choose one of the persons to save.\nyou can only save yourself once.");
        printList(alive_persons, cityDoctor);
        cityDoctor.getOutput().writeUTF("type the name of the player you want to save.");
        String name = cityDoctor.getInput().readUTF();
        Person choose = search(alive_persons, name);
        if (choose == null) {
            return null;
        } else if (choose == cityDoctor) {
            if (cityDoctor.getSave_himself() == 0) {
                cityDoctor.getOutput().writeUTF("you have already saved yourself.\nno one will be saved tonight");
                return null;
            } else {
                cityDoctor.setAlive(1);
                cityDoctor.setSave_himself(0);
                return cityDoctor;
            }
        } else {
            if (choose.getAlive() == 0) {
                choose.setAlive(1);
            }
            return choose;
        }
    }

    /**
     * send role of a player that detective asked.
     *
     */
    public void inquire() throws IOException {
        if (!inGame(detective)) {
            return;
        }
        printList(alive_persons, detective);
        detective.getOutput().writeUTF("\nwrite the name of a person tp inquire:");
        String name = detective.getInput().readUTF();
        Person choose = search(alive_persons, name);
        if (choose == null) {
            return;
        }
        if (choose.getIsMafia()) {
            detective.getOutput().writeUTF(choose.getUsername() + " is mafia");
        } else {
            detective.getOutput().writeUTF(choose.getUsername() + " is not mafia");
        }
    }


    /**
     * asks a professional if he want to shoot.
     * if receive yes, professional shoot will happen.
     *
     * @return the person that has been killed.
     * @throws IOException .
     */
    public Person professionalShoot() throws IOException {
        if (!inGame(professional)) {
            return null;
        }
        professional.getOutput().writeUTF("do you want to shoot tonight?\n(write yes or no");
        String shoot = professional.getInput().readUTF();
        if (shoot.equalsIgnoreCase("yes")) {
            printList(alive_persons, professional);
            professional.getOutput().writeUTF("\nwrite the name of one of the players to shoot.");
            String name = professional.getInput().readUTF();
            Person choose = search(alive_persons, name);
            if (choose == null) {
                return null;
            } else if (mafias.contains(choose)) {
                choose.setAlive(0);
                return choose;
            } else {
                professional.setAlive(0);
                alive_persons.remove(professional);
                citizens.remove(professional);
                dead.add(professional);
                deadRoles = deadRoles + professional.getUsername();
                leaveGame(professional);
                return professional;
            }
        } else {
            return null;
        }
    }

    /**
     * Talk down a person that psychologist said.
     *
     * @return the person that has been talked down.
     * @throws IOException the io exception
     */
    public Person talkDown() throws IOException {
        if (!inGame(psychologist)) {
            return null;
        }
        printList(alive_persons, psychologist);
        psychologist.getOutput().writeUTF("\nwrite the name of a person tp talk down:");
        String name = psychologist.getInput().readUTF();
        return search(alive_persons, name);
    }

    /**
     * die hard inquire.
     *
     * @return the string that contain roles of dead players.
     * @throws IOException .
     */
    public String inquireDead() throws IOException {
        if (!inGame(dieHard) || (dieHard.getInquiry() == 0)) {
            return null;
        }
        dieHard.getOutput().writeUTF("do you want to inquire roles of dead people?\n(write yes or no)");
        String inquire = dieHard.getInput().readUTF();
        if (inquire.equalsIgnoreCase("yes")) {
            StringBuilder res = new StringBuilder("die hard inquired last night.\nthis is the result:");
            Collections.shuffle(dead);  //The order of the people who left should not be known
            for (Person person : dead) {
                res.append("\n").append(person.getRole().toString());
            }
            res.append("\nhave left the game");
            return res.toString();
        }
        return null;
    }


    /**
     * checks that if the max element of list is repetitious or no.
     *
     * @param integers is given list.
     * @return false is max value of list is unique.
     */
    private boolean repetitiousMax(ArrayList<Integer> integers) {
        int max = Collections.max(integers);
        System.out.println(max);
        int i = 0;
        for (Integer integer : integers) {
            if (integer == max) {
                i++;
            }
        }
        System.out.println(i);
        return (i != 1);
    }

    /**
     * start voting and return selected player.
     *
     * @return a player that has the most number votes.
     * @throws IOException the io exception.
     */
    public Person voting() throws IOException {
        chatServer.setListenerUsers(observers);
        votes = new ArrayList<Integer>(alive_persons.size());
        for (int i = 0; i < alive_persons.size(); i++) {
            votes.add(i, 0);
        }
        votingServer.StartVoting(chatServer, alive_persons, votes, VOTING_TIME);
        checkVotingFinished();
        System.out.println(votingServer.votes);
        if (repetitiousMax(votes)) {
            return null;
        }
        return alive_persons.get(votes.indexOf(Collections.max(votes)));
    }

    /**
     * asks mayor if he wants to cancel the voting or no.
     *
     * @return true if mayor cancel the voting.
     */
    public boolean cancelVoting() {
        if (!inGame(mayor)) {
            return false;
        }
        try {
            mayor.getOutput().writeUTF("do you want to cancel the voting?\nwrite yes or no.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            kickOut(mayor);
        }
        String choice = null;
        try {
            choice = mayor.getInput().readUTF();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            kickOut(mayor);
        }
        if (choice == null) {
            return false;
        }
        return choice.equalsIgnoreCase("yes");
    }

    /**
     * votes and send result to observers.
     */
    public void doVoting() {
        Person person = null;
        try {
            person = voting();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ((person != null) && !cancelVoting()) {
            chatServer.setListenerUsers(observers);
            chatServer.broadcast(person.getUsername() + " will leave the game.");
            alive_persons.remove(person);
            if (person instanceof Mafia) {
                mafias.remove(person);
            } else {
                citizens.remove(person);
            }
            leaveGame(person);
        }
    }


    /**
     * checks if game is finished or no.
     *
     * @return true if game was finished.
     */
    public boolean endOfGame() {
        return (mafias.size() >= citizens.size()) || (mafias.isEmpty());
    }


    /**
     * checks if all players typed "ready" pr no.
     */
    public void checkReady() {
        boolean ready;
        do {
            ready = true;
            for (UserMaker userMaker : userMakers) {
                if (!userMaker.isReady()) {
                    ready = false;
                    break;
                }
            }
            String s = ready + "a";
        } while (!ready);
    }

    /**
     * Check chat finished.
     */
    public void checkChatFinished(ArrayList<Person> people) {
        boolean finish;
        do {
            finish = true;
            for (Person person : people) {
                if (person == silentPlayer) {
                    continue;
                }
                if (!person.getUserThread().isFinish()) {
                    finish = false;
                    break;
                }
            }
            String s = finish + "a";
        } while (!finish);
    }

    /**
     * Check voting finished.
     */
    public void checkVotingFinished() {
        boolean finish;
        do {
            finish = true;
            for (Person person : alive_persons) {
                if (!votingServer.getThreads().get(alive_persons.indexOf(person)).isFinished()) {
                    finish = false;
                    break;
                }
            }
            String s = finish + "a";
        } while (!finish);
    }







}
