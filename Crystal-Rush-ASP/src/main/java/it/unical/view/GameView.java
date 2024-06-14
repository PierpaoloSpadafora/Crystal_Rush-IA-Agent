package it.unical.view;

import it.unical.model.*;
import it.unical.model.Robot;
import it.unical.support.Cell;
import it.unical.support.ItemEnum;
import it.unical.utility.ImageGetter;
import it.unical.utility.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameView extends JPanel {

    private final World world;
    private Cell[][] map = World.getInstance().getMap();
    private ArrayList<Command> robotsPositions;
    private ArrayList<Team> teams;

    public GameView(){
        this.world =  World.getInstance();
        this.robotsPositions = this.world.getRobotsPositions();
        this.teams = this.world.getTeams();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.robotsPositions = this.world.getRobotsPositions();
        this.teams = this.world.getTeams();
        printMap(g);
        printObjectsOnMap(g);
        printRobots(g);
    }

    private void printMap(Graphics g) {
        this.map = this.world.getMap();
        for(int x = 0; x< Settings.PLANET_ROW; x++) {
            for(int y=0; y<Settings.PLANET_COLUMN; y++) {
                this.drawCellBorder(g, x, y); // Disegna il bordo di ogni cella
                if(y==0){
                    this.drawHomeBase(g, x, y);
                }
                else{
                    drawEmptyTile(g, x, y);
                    printGemsOnMap(g, x, y);
                }

                //  TODO DA RIMUOVERE PER LA COMPETITION
                drawVisibility(g,x,y);

            }
        }

    }

    private void printGemsOnMap(Graphics g, int x, int y) {
        if(this.map[x][y].containsHole()) {
            this.drawHole(g, x, y);
        }
        if(this.map[x][y].getGemNumber() == 1) {
            this.drawSingleGem(g, x, y);
        }
        if(this.map[x][y].getGemNumber() == 2) {
            this.drawMultipleGem(g, x, y);
        }
    }


    private void printObjectsOnMap(Graphics g) {
        List<Item> itemsCopy;
        synchronized (this.world.getItemsPositions()) {
            itemsCopy = new ArrayList<>(this.world.getItemsPositions());
        }
        for (Item i : itemsCopy) {
            if (i.isRadar()) {
                this.drawRadarPlaced(g, i.getX(), i.getY(), i.getTeamColor());
            } else if (i.isMine()) {
                this.drawMinePlaced(g, i.getX(), i.getY(), i.getTeamColor());
            }
        }
    }


    private void printRobots(Graphics g) {
        ArrayList<Command> robotsCopy;
        ArrayList<Team> teamsCopy;
        robotsCopy = new ArrayList<>(this.robotsPositions);
        teamsCopy = new ArrayList<>(this.teams);

        HashMap<Integer, Command> commands = new HashMap<>();
        for(Team t : teamsCopy) {
            for(Robot r : t.getRobots()) {
                for(Command c : robotsCopy) {
                    if(c.getIdentifier() == r.getIdentifier()) {
                        c.setItem(r.getItem());
                    }
                }
            }
        }

        for(Command c : robotsCopy) {
            this.drawRobot(g, c.getX(), c.getY(), c.getColor());
            if(c.getItem() == ItemEnum.mine){
                this.drawMineOnRobot(g, c.getX(), c.getY());
            }
            else if(c.getItem() == ItemEnum.radar){
                this.drawRadarOnRobot(g, c.getX(), c.getY());
            }
            else if(c.getItem() == ItemEnum.gem){
                this.drawGemOnRobot(g, c.getX(), c.getY());
            }
        }
    }


    // ------- PUBLIC METHODS ------- //

    public void drawVisibility(Graphics g, int x, int y) {
        if(this.world.isCellVisitedByTeam(x, y, 1)){
            if(this.world.isCellVisitedByTeam(x, y, 2)){
                g.setColor(new Color(172, 255, 0, 70));
                g.fillRect(y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH, Settings.CELL_HEIGHT);
            }
            else{
                g.setColor(new Color(255, 0, 0, 70));
                g.fillRect(y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH, Settings.CELL_HEIGHT);
            }
        }
        else if (this.world.isCellVisitedByTeam(x, y, 2)){
            g.setColor(new Color(0, 0, 255, 70));
            g.fillRect(y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH, Settings.CELL_HEIGHT);
        }
    }



    public void drawHole(Graphics g, int x, int y) {
        g.drawImage(ImageGetter.getInstance().getHole(), y * Settings.CELL_HEIGHT, x * Settings.CELL_WIDTH, null);
    }
    public void drawEmptyTile(Graphics g, int x, int y) {
        g.drawImage(ImageGetter.getInstance().getGround(), y * Settings.CELL_HEIGHT, x * Settings.CELL_WIDTH, null);
    }
    public void drawHomeBase(Graphics g, int x, int y) {
        g.drawImage(ImageGetter.getInstance().getGreenZone(), y * Settings.CELL_HEIGHT, x * Settings.CELL_WIDTH, null);
    }

    public void drawSingleGem(Graphics g, int x, int y) {
        if(this.world.getCell(x, y).containsHole())this.drawHole(g, x, y);
        g.drawImage(ImageGetter.getInstance().getMidMineral(), y * Settings.CELL_HEIGHT, x * Settings.CELL_WIDTH, null);
    }
    public void drawMultipleGem(Graphics g, int x, int y) {
        g.drawImage(ImageGetter.getInstance().getFullMineral(), y * Settings.CELL_HEIGHT, x * Settings.CELL_WIDTH, null);
    }
    public void drawGemOnRobot(Graphics g, int x, int y) {
        g.drawImage(ImageGetter.getInstance().getMineralOnRobot(), y * Settings.CELL_WIDTH + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), x * Settings.CELL_HEIGHT + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), null);
    }

    public void drawRadarPlaced(Graphics g, int x, int y, String color) {
        g.drawImage(ImageGetter.getInstance().getRadarPlaced(), y * Settings.CELL_HEIGHT, x * Settings.CELL_WIDTH, null);

        if(color.equals("red")){
            g.setColor(new Color(255, 0, 0, 70));
            g.fillRect(y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH, Settings.CELL_HEIGHT);
        }
        else{
            g.setColor(new Color(0, 0, 255, 70));
            g.fillRect(y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH, Settings.CELL_HEIGHT);
        }
    }
    public void drawRadarOnRobot(Graphics g, int x, int y) {
        g.drawImage(ImageGetter.getInstance().getRadarOnRobot(), y * Settings.CELL_WIDTH + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), x * Settings.CELL_HEIGHT + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), null);
    }

    public void drawMinePlaced(Graphics g, int x, int y, String color) {
        g.drawImage(ImageGetter.getInstance().getMinePlaced(), y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, null);

        if(color.equals("red")){
            g.setColor(new Color(255, 0, 0, 70));
            g.fillRect(y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH, Settings.CELL_HEIGHT);
        }
        else{
            g.setColor(new Color(0, 0, 255, 70));
            g.fillRect(y * Settings.CELL_HEIGHT, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH, Settings.CELL_HEIGHT);
        }

    }
    public void drawMineOnRobot(Graphics g, int x, int y) {
        g.drawImage(ImageGetter.getInstance().getMineOnRobot(),  y * Settings.CELL_WIDTH + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), x * Settings.CELL_HEIGHT + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), null);
    }
    
    public void drawRobot(Graphics g, int x, int y, String color) {
        Image robot = color.equals("red") ? ImageGetter.getInstance().getRedRobot() : ImageGetter.getInstance().getBlueRobot();
        g.drawImage(robot, y * Settings.CELL_WIDTH + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), x * Settings.CELL_HEIGHT + ((Settings.CELL_HEIGHT-Settings.ROBOT_SIZE)/2), null);
    }

    private void drawCellBorder(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.fillRect(y * Settings.CELL_WIDTH, x * Settings.CELL_HEIGHT, Settings.CELL_WIDTH + 1, Settings.CELL_HEIGHT + 1);
    }


}
