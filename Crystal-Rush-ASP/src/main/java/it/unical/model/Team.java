package it.unical.model;


import it.unical.ai.EmbASP;
import it.unical.support.ItemEnum;
import it.unical.support.ObservableScore;
import it.unical.support.ObserverLabel;
import it.unical.utility.Settings;
import it.unical.view.ScoreBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Team extends Thread {

    private ArrayList<Robot> robots;
    private HashMap<Integer, Command> commands;

    private World world;
    private EmbASP embASP = new EmbASP(this);
    private ObservableScore score = new ObservableScore();

    private String color, teamName;
    private int teamNumber, numRobots;

    private boolean mineAvailable = false, radarAvailable = false;

    private long cooldownMine = 0, cooldownRadar = 0;
    private int idMine = 5_000, idRadar = 10_000;

    private ScoreBoard scoreBoard;
    private int iteration=0;

    private void initializeRobots(int numRobot) {
        robots = new ArrayList<>();
        for (int i = 0; i < numRobot; i++) {
            Robot robot = world.addRobotPosition(color);
            robots.add(robot);
        }

        richiestaASPSquadra();
    }

    public Team(String color, int numRobots, NameTeam name, int teamNumber, ScoreBoard scoreBoard) {
        this.color = color;
        this.teamName = name.name();
        this.world = World.getInstance();
        this.world.addTeam(this);
        this.teamNumber = teamNumber;
        this.numRobots = numRobots;

        this.scoreBoard = scoreBoard;

        initializeRobots(numRobots);
        System.out.println("Sono il team " + color + " e sto per creare " + numRobots + " robot.");
    }


    public void richiestaASPSquadra() {
        this.iteration++;
        this.commands = this.embASP.compiAzioneTeam();

        for(Robot r : robots) {
            Command c = commands.get(r.getIdentifier());
            r.setMossaDaFare(c);
            r.setItem(c.getItem());
        }
    }


    private void updateCooldownTimers() {
        long currentTime = System.currentTimeMillis();

        if (!mineAvailable) {
            int originalValue = (int) (currentTime - cooldownMine);
            int normalizedValue = ((originalValue * 100) / Settings.MINE_COOLDOWN)-1;
            scoreBoard.updateLabelMine(normalizedValue, this.getTeamNumber());
            if (currentTime - cooldownMine > Settings.MINE_COOLDOWN) {
                idMine++;
                mineAvailable = true;
            }
        }

        if (!radarAvailable) {
            int originalValue = (int) (currentTime - cooldownRadar);
            int normalizedValue = ((originalValue * 100) / Settings.RADAR_COOLDOWN)-1;
            scoreBoard.updateLabelRadar(normalizedValue, this.getTeamNumber());
            if (currentTime - cooldownRadar > Settings.RADAR_COOLDOWN) {
                idRadar++;
                radarAvailable = true;
            }
        }


        scoreBoard.updateRobotsAlive(this.getTeamNumber());

    }

    public void run() {
        while (!world.isRunning()) {
            dormi(Settings.TEAM_TICK);
        }
        cooldownMine = System.currentTimeMillis();
        cooldownRadar = System.currentTimeMillis();
        while (world.isRunning()) {
            updateCooldownTimers();
            richiestaASPSquadra();
            compiAzioneSquadra();
            dormi(Settings.TEAM_TICK);
        }
    }


    private void dig(Robot r) {
        this.world.digCell(r.getXMossaDaFare(), r.getYMossaDaFare(), this.getColor());

        if(this.world.getCell(r.getMossaDaFare().getX(), r.getMossaDaFare().getY()).collectGem()){
            r.setItem(ItemEnum.gem);
        }
    }
    private void move(Robot r) {
        this.world.updateRobotPosition(r.getXMossaDaFare(), r.getYMossaDaFare(), r.getIdentifier());
    }
    private void place(Robot r) {
        this.world.digCell(r.getXMossaDaFare(), r.getYMossaDaFare(), this.getColor());
        this.world.addItemPosition(r.getXMossaCorrente(), r.getYMossaCorrente(), this.color, this.teamNumber, r.giveItem());
        r.setItem(ItemEnum.none);
    }
    private void deliver(Robot r) {
        updateScore();
        this.world.updateScore(this.teamNumber);
        r.giveItem();
    }
    private void none(Robot r) {
        //System.out.println("Il robot (" + r.getIdentifier() + ") non ha azioni da compiere");
    }

    private void compiAzioneRobot(Robot r) {
        r.setMossaCorrente(r.getMossaDaFare());

        switch(r.getMossaDaFare().getAction()){
            case dig -> dig(r);
            case move -> move(r);
            case place -> place(r);
            case deliver -> deliver(r);
            case none -> none(r);
        }

        if(!this.world.getCell(r.getXMossaCorrente(), r.getYMossaCorrente()).setFree(r.getIdentifier())){
            System.out.println("Il robot (" + r.getIdentifier() + ") non è riuscito a liberare la cella(" + r.getXMossaCorrente() + "," + r.getYMossaCorrente() + ")");
        }

        r.setMossaDaFare(null);

    }

    private void compiAzioneSquadra() {
        // Crea una lista temporanea per evitare ConcurrentModificationException
        List<Robot> tempRobots = new ArrayList<>(this.robots);

        for (Robot r : tempRobots) {
            synchronized (this) {
                // Controlla se il robot esiste ancora nella lista originale
                if (this.robots.contains(r)) {
                    if (r.getMossaDaFare() != null) {
                        // Se la cella è libera la occupa, se non è libera va nell'else
                        if (this.world.getCell(r.getXMossaDaFare(), r.getYMossaDaFare()).setOccupied(r.getIdentifier())) {
                            compiAzioneRobot(r);
                        } else {
                            System.out.println("ROBOT-" + this.color + "-" + r.getIdentifier() + ": e la cella è occupata, aspetterò il prossimo turno");
                        }
                    } else {
                        System.out.println("ROBOT-" + this.color + "-" + r.getIdentifier() + ": e non ho azioni da compiere, aspetterò il prossimo turno");
                    }
                }
            }
        }
    }



    public synchronized void killRobot(int id) {
        Iterator<Robot> iterator = robots.iterator();
        while (iterator.hasNext()) {
            Robot r = iterator.next();
            if (r.getIdentifier() == id) {
                iterator.remove();
                System.out.println("Robot (" + id + "," + this.color + ") ESPLOSO MALEDETTO in pos (" + r.getXMossaCorrente() + "," + r.getYMossaCorrente() + ")");
                break;
            }
        }

        this.commands.remove(id);
        this.world.removeRobotPosition(id);
        this.world.updateTeam(this);
    }

    public boolean isStillAlive() {
        return !robots.isEmpty();
    }

    public synchronized int getNumRobAlive() {
        return robots.size();
    }

    public boolean isGemNumberEnoughToWin(){
        return this.score.getScore() > this.world.getGemsTotalValue() / 2;
    }

    public synchronized void updateScore() {
        this.score.incrementScore();
    }

    public synchronized void mineUsed() {
        if(mineAvailable) {
            mineAvailable = false;
            cooldownMine = System.currentTimeMillis();
        }
    }

    public synchronized void radarUsed() {

        if(radarAvailable) {
            radarAvailable = false;
            cooldownRadar = System.currentTimeMillis();
        }
    }

    public void linkLabel(ObserverLabel label){
        this.score.addObserver(label);
    }

    private void dormi(int millisecondi){
        try {
            Thread.sleep(millisecondi); // Controlla ogni secondo
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    //---------------   GETTERS e SETTERS   ---------------//

    public synchronized int getIteration() {
        return iteration;
    }
    public synchronized void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public synchronized int getRadarId() {
        return idRadar;
    }
    public synchronized boolean isRadarReady() {
        return radarAvailable;
    }
    public synchronized int getMineId() {
        return idMine;
    }
    public synchronized boolean isMineReady() {
        return mineAvailable;
    }

    public synchronized ArrayList<Robot> getRobots() {
        return robots;
    }
    public void setRobots(ArrayList<Robot> robots) {
        this.robots = robots;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }

    public EmbASP getEmbASP() {
        return embASP;
    }
    public void setEmbASP(EmbASP embASP) {
        this.embASP = embASP;
    }

    public int getScore() {
        return score.getScore();
    }
    public void setScore(ObservableScore score) {
        this.score = score;
    }

    public int getTeamNumber() {
        return teamNumber;
    }
    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getNumRobots() {
        return numRobots;
    }
    public void setNumRobots(int numRobots) {
        this.numRobots = numRobots;
    }

}
