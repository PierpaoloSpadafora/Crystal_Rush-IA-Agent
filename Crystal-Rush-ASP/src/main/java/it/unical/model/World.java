package it.unical.model;

import it.unical.support.ActionEnum;
import it.unical.support.Cell;
import it.unical.support.ItemEnum;
import it.unical.utility.Settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class World {


    private static World instance = null;
    private Cell[][] mappa = new Cell[Settings.PLANET_ROW][Settings.PLANET_COLUMN];

    private final ArrayList<Team> teams;
    private final ArrayList<Command> robotsPositions;
    private final ArrayList<Item> itemsPositions;

    private boolean started = false, stopped = false;
    private int gemsTotalValue = 0, scoreTeam1 = 0, scoreTeam2 = 0; // red è il team 1 e blu è il team 2

    private long timer = 0;     // TimerItem che scorre durante la partita


    public boolean isMoveInMap(int x, int y) {
        return x >= 0 && x < Settings.PLANET_ROW && y >= 0 && y < Settings.PLANET_COLUMN;
    }

    private void addGems(Cell cell, int column, Random random) {
        if (column > 3) {
            int percentage = random.nextInt(100);
            if (percentage > 70) {
                cell.addGem();
                this.gemsTotalValue++;
                if (percentage > 80) {
                    cell.addGem();
                    this.gemsTotalValue++;
                }
            }
        }
    }

    private void initializeMap() {
        Random random = new Random();
        mappa = new Cell[Settings.PLANET_ROW][Settings.PLANET_COLUMN];

        for (int i = 0; i < Settings.PLANET_ROW; i++) {
            for (int j = 0; j < Settings.PLANET_COLUMN; j++) {
                Cell cell = new Cell(i, j);
                addGems(cell, j, random);
                mappa[i][j] = cell;
            }
        }
    }

    private World(){
        initializeMap();
        teams = new ArrayList<>();
        robotsPositions = new ArrayList<>();
        itemsPositions = new ArrayList<>();
    }

    private boolean isPositionOccupied(int x, int y) {
        for (Command c : robotsPositions) {
            if (c.getX() == x && c.getY() == y) {
                return true;
            }
        }
        return false;
    }

    // ------- PUBLIC METHODS ------- //

    public synchronized Robot addRobotPosition(String color){
        Random random = new Random();
        int x;
        int y = 0;
        do{
            x = random.nextInt(Settings.PLANET_ROW);
        }while(isPositionOccupied(x, y));

        Robot robot = new Robot(x, y, x);

        Command commands = new Command.Builder(x, y)
                .withIdentifier(x)
                .withColor(color)
                .withAction(ActionEnum.none)
                .withItem(ItemEnum.none)
                .build();

        robotsPositions.add(commands);

        return robot;
    }

    public synchronized void removeRobotPosition(int identifier){
        robotsPositions.removeIf(r -> r.getIdentifier() == identifier);
    }

    public synchronized ArrayList<Robot> getRobotOfTeam(int teamNumber){
        ArrayList<Robot> robots = new ArrayList<>();
        for(Team t : teams){
            if(t.getTeamNumber() == teamNumber){
                robots.addAll(t.getRobots());
            }
        }
        return robots;
    }

    public synchronized void updateTeam(Team team){
        for(int i = 0; i < teams.size(); i++){
            if(teams.get(i).getColor().equals(team.getColor())){
                teams.set(i, team);
                return;
            }
        }
    }


    public synchronized int countFreeCells(int x, int y, String teamColor) {
        int freeCells = 0;
        if(y==0){
            if(x==0 || x==Settings.PLANET_ROW-1)
                freeCells = 5;
            else
                freeCells = 5;
            freeCells = 3;
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (this.isMoveInMap(x + dx, y + dy) && !this.isEnemyMine(x + dx, y + dy, teamColor)) {
                    freeCells++;
                }
            }
        }
        return freeCells;
    }

    public synchronized void setVisitedCells(int x, int y, String color){
        if(color.equals("red")){
            setVisitedCells(x, y, 1);
        }
        else if(color.equals("blue")){
            setVisitedCells(x, y, 2);
        }
    }

    // setta tutte le celle nel vicinato di 2 di una coordinata x,y come visitate dal team teamNumber
    public synchronized void setVisitedCells(int x, int y, int teamNumber){
        for(int i = x-2; i <= x+2; i++){
            for(int j = y-2; j <= y+2; j++){
                if(isMoveInMap(i, j)){
                    setCellVisitedByTeam(i, j, teamNumber);
                }
            }
        }
    }

    public synchronized void addItemPosition(int x, int y, String color, int teamNumber, ItemEnum item){
        Item i = new Item.Builder(x, y)
                .withTeamColor(color)
                .withItem(item)
                .build();

        itemsPositions.add(i);

        if(i.isRadar()){
            setVisitedCells(x, y, teamNumber);
        }
    }

    private synchronized void killRobot(int id, String color) {
        for (Team t : teams) {
            if (t.getColor().equals(color)) {
                System.out.println("Team " + t.getColor() + " ha " + t.getNumRobAlive()+ " robot vivi");
                t.killRobot(id);
                System.out.println("Team " + t.getColor() + " ha " + t.getNumRobAlive()+ " robot vivi");
                return;
            }
        }
    }

    public synchronized int getNumRobAliveOfTeam(String color){
        int count = 0;
        for(Team t : teams){
            if(t.getColor().equals(color)){
                count = t.getNumRobAlive();
            }
        }
        return count;
    }


    // tutti i robot che si trovano sulle coordinate (x,y) o nel vicinato a croce muoiono
    //  se nel vicinato ci sono altre mine scoppieranno a catena
    public void mineExplosion(int x, int y) {
        synchronized (robotsPositions) {
            Iterator<Command> iterator = robotsPositions.iterator();
            while (iterator.hasNext()) {
                Command c = iterator.next();
                boolean sameColumn = c.getX() == x;
                boolean sameRow = c.getY() == y;
                boolean adjacentColumn = (x > 0 && c.getX() == x - 1) || (x < Settings.PLANET_ROW - 1 && c.getX() == x + 1);
                boolean adjacentRow = (y > 0 && c.getY() == y - 1) || (y < Settings.PLANET_COLUMN - 1 && c.getY() == y + 1);

                if ((sameColumn && sameRow) || (sameColumn && adjacentRow) || (adjacentColumn && sameRow)) {
                    iterator.remove();
                    this.killRobot(c.getIdentifier(), c.getColor());
                }
            }
        }
    }


    public synchronized void refreshRadarVisibility(int teamNumber){

        for(int i = 0; i < Settings.PLANET_ROW; i++){
            for(int j = 0; j < Settings.PLANET_COLUMN; j++){
                    mappa[i][j].setVisitedByFirstTeam(false);
                    mappa[i][j].setVisitedBySecondTeam(false);
            }
        }
        for(Item i : itemsPositions){
            if(i.isRadar()){
                setVisitedCells(i.getX(), i.getY(), i.getTeamColor());
            }
        }
    }


    public synchronized void distruggiRadar(int x, int y, String color) {
        String colorToDelete = "";
        int teamNumber = 0;
        if (color.equals("red")) {
            colorToDelete = "blue";
            teamNumber = 2;
        } else if (color.equals("blue")) {
            colorToDelete = "red";
            teamNumber = 1;
        }

        synchronized (itemsPositions) {
            Iterator<Item> iterator = itemsPositions.iterator();
            while (iterator.hasNext()) {
                Item i = iterator.next();
                if (i.getX() == x && i.getY() == y && i.isRadar() && i.getTeamColor().equals(colorToDelete)) {
                    iterator.remove();
                }
            }
        }
        refreshRadarVisibility(teamNumber);
    }


    public synchronized void digCell(int x, int y, String color){
        getCell(x, y).digHole();
        if(hasRadar(x, y)){
            distruggiRadar(x, y, color);
        }
    }

    public void controllaSeHaPestatoUnaMina(int x, int y, String color) {
        for(Item i : itemsPositions){
            if(i.getX() == x && i.getY() == y && i.isMine() && !(i.getTeamColor().equals(color))){
                this.mineExplosion(x, y);
                itemsPositions.remove(i);
                return;
            }
        }
    }

    public synchronized void updateRobotPosition(int x, int y, int identifier){
        for(Command c : robotsPositions){
            if(c.getIdentifier() == identifier){
                c.setX(x);
                c.setY(y);
                controllaSeHaPestatoUnaMina(x, y, c.getColor());

                //System.out.println("ROBOT-"+ c.getColor() + identifier + ": si è spostato in (" + x + " " + y+")");

                return;
            }
        }
    }

    public synchronized void updateScore(int teamNumber){
        if(teamNumber == 1){
            scoreTeam1++;
        }
        else if(teamNumber == 2){
            scoreTeam2++;
        }
    }

    public boolean hasRadar(int x, int y){
        return itemsPositions.stream().anyMatch(i -> i.getX() == x && i.getY() == y && i.isRadar());
    }


    //---------------   GETTERS e SETTERS   ---------------//
    public static World getInstance(){
        if(instance == null){
            instance = new World();
        }
        return instance;
    }

    public synchronized Cell[][] getMap(){
        return mappa;
    }
    public synchronized Cell getCell(int x, int y){
        return mappa[x][y];
    }
    public Cell getRandomCell() {
        Random random = new Random();
        int i = random.nextInt(Settings.PLANET_COLUMN);
        while (i == 0) {
            i = random.nextInt(Settings.PLANET_COLUMN);
        }
        int j = random.nextInt(Settings.PLANET_ROW);
        return mappa[j][i];
    }
    public synchronized boolean setCellOccupied(int x, int y, int id){
        return mappa[x][y].setOccupied(id);
    }
    public synchronized void setCellFree(int x, int y, int id){
        mappa[x][y].setFree(id);
    }

    public synchronized boolean isCellVisitedByTeam(int x, int y, int TeamIndex){
        if(TeamIndex == 1){
            return mappa[x][y].isVisitedByFirstTeam();
        }
        else if(TeamIndex == 2){
            return mappa[x][y].isVisitedBySecondTeam();
        }
        return false;
    }
    public synchronized void setCellVisitedByTeam(int x, int y, int TeamIndex){
        if(TeamIndex == 1){
            mappa[x][y].setVisitedByFirstTeam(true);
        }
        else if(TeamIndex == 2){
            mappa[x][y].setVisitedBySecondTeam(true);
        }
    }

    public synchronized ArrayList<Team> getTeams() {
        return teams;
    }
    public synchronized Team getTeam(String color){
        return teams.stream().filter(t -> t.getColor().equals(color)).findFirst().orElse(null);
    }
    public synchronized Team getTeam(int index){
        return teams.get(index);
    }
    public synchronized void addTeam(Team team) {
        this.teams.add(team);
    }

    public synchronized ArrayList<Command> getRobotsPositions() {
        return robotsPositions;
    }
    public synchronized Command getRobotPosition(int identifier){
        return robotsPositions.stream().filter(r -> r.getIdentifier() == identifier).findFirst().orElse(null);
    }
    public synchronized Command getRobotPosition(int x, int y){
        return robotsPositions.stream().filter(r -> r.getX() == x && r.getY() == y).findFirst().orElse(null);
    }
    public synchronized Command getRobotPosition(int x, int y, String color){
        return robotsPositions.stream().filter(r -> r.getX() == x && r.getY() == y && r.getColor().equals(color)).findFirst().orElse(null);
    }

    public synchronized ArrayList<Item> getItemsPositions() {
        return itemsPositions;
    }
    public synchronized Item getItemPosition(int x, int y){
        return itemsPositions.stream().filter(i -> i.getX() == x && i.getY() == y).findFirst().orElse(null);
    }
    public synchronized Item getItemPosition(int x, int y, String color){
        return itemsPositions.stream().filter(i -> i.getX() == x && i.getY() == y && i.getTeamColor().equals(color)).findFirst().orElse(null);
    }
    public synchronized Item getItemPosition(int x, int y, ItemEnum item){
        return itemsPositions.stream().filter(i -> i.getX() == x && i.getY() == y && i.getItem().equals(item)).findFirst().orElse(null);
    }

    public synchronized boolean isStarted() {
        return started;
    }
    public synchronized void setRunning() {
        this.started = true;
    }

    public synchronized boolean isFinished() {
        return stopped;
    }
    public synchronized void setFinished() {
        this.stopped = true;
    }

    public synchronized boolean isRunning() {
        return started && !stopped;
    }

    public synchronized int getGemsTotalValue() {
        return gemsTotalValue;
    }
    public synchronized void setGemsTotalValue(int gemsTotalValue) {
        this.gemsTotalValue = gemsTotalValue;
    }

    public synchronized int getScoreTeam1() {
        return scoreTeam1;
    }
    public synchronized void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public synchronized int getScoreTeam2() {
        return scoreTeam2;
    }
    public synchronized void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public synchronized void setTime(long time){
        this.timer = time;
    }

    public synchronized boolean isAllyMine(int x, int y, String color){
        return itemsPositions.stream().anyMatch(i -> i.getX() == x && i.getY() == y && i.isMine() && i.getTeamColor().equals(color));
    }
    public synchronized boolean isAllyRadar(int x, int y, String color){
        return itemsPositions.stream().anyMatch(i -> i.getX() == x && i.getY() == y && i.isRadar() && i.getTeamColor().equals(color));
    }

    public synchronized boolean isEnemyMine(int x, int y, String color){
        return itemsPositions.stream().anyMatch(i -> i.getX() == x && i.getY() == y && i.isMine() && !i.getTeamColor().equals(color));
    }
    public synchronized boolean isEnemyRadar(int x, int y, String color){
        return itemsPositions.stream().anyMatch(i -> i.getX() == x && i.getY() == y && i.isRadar() && !i.getTeamColor().equals(color));
    }



}
