package it.unical.utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ImageGetter {

    private static Image hole;
    private static Image ground;
    private static Image greenZone;

    private static Image midMineral;
    private static Image fullMineral;
    private static Image mineralOnRobot;

    private static Image radar;
    private static Image radarOnRobot;
    private static Image radarPlaced;

    private static Image mine;
    private static Image mineOnRobot;
    private static Image minePlaced;

    private static Image blueRobot;
    private static Image redRobot;

    private ImageGetter(){}

    private static ImageGetter instance = null;

    public static ImageGetter getInstance(){
        try{
            if(instance == null){
                instance = new ImageGetter();

                hole = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/transparent/hole.png")));
                ground = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/tiles/empty.png")));
                greenZone = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/tiles/green-zone.png")));

                midMineral = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/tiles/mid-mineral.png")));
                fullMineral = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/transparent/full-mineral.png")));
                mineralOnRobot = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResource("/images/cart/mineral.png")));

                radar = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/transparent/radar-logo.png")));
                radarOnRobot = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResource("/images/cart/radar.png")));
                radarPlaced = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResource("/images/tiles/radar.png")));

                mine = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/transparent/mine-logo.png")));
                mineOnRobot = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResource("/images/cart/mine.png")));
                minePlaced = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResource("/images/tiles/mine.png")));

                blueRobot = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/cart/robot-blue.png")));
                redRobot = ImageIO.read(Objects.requireNonNull(ImageGetter.class.getResourceAsStream("/images/cart/robot-red.png")));


                hole = hole.getScaledInstance(Settings.CELL_WIDTH, Settings.CELL_HEIGHT, Image.SCALE_SMOOTH);
                ground = ground.getScaledInstance(Settings.CELL_WIDTH, Settings.CELL_HEIGHT, Image.SCALE_SMOOTH);
                greenZone = greenZone.getScaledInstance(Settings.CELL_WIDTH, Settings.CELL_HEIGHT, Image.SCALE_SMOOTH);

                midMineral = midMineral.getScaledInstance(Settings.CELL_WIDTH, Settings.CELL_HEIGHT, Image.SCALE_SMOOTH);
                fullMineral = fullMineral.getScaledInstance(Settings.CELL_WIDTH, Settings.CELL_HEIGHT, Image.SCALE_SMOOTH);
                mineralOnRobot = mineralOnRobot.getScaledInstance(Settings.ROBOT_SIZE, Settings.ROBOT_SIZE, Image.SCALE_SMOOTH);

                radar = radar.getScaledInstance(Settings.ITEM_SIZE, Settings.ITEM_SIZE, Image.SCALE_SMOOTH);
                radarOnRobot = radarOnRobot.getScaledInstance(Settings.ROBOT_SIZE, Settings.ROBOT_SIZE, Image.SCALE_SMOOTH);
                radarPlaced = radarPlaced.getScaledInstance(Settings.CELL_WIDTH, Settings.CELL_HEIGHT, Image.SCALE_SMOOTH);

                mine = mine.getScaledInstance(Settings.ITEM_SIZE, Settings.ITEM_SIZE, Image.SCALE_SMOOTH);
                mineOnRobot = mineOnRobot.getScaledInstance(Settings.ROBOT_SIZE, Settings.ROBOT_SIZE, Image.SCALE_SMOOTH);
                minePlaced = minePlaced.getScaledInstance(Settings.CELL_WIDTH, Settings.CELL_HEIGHT, Image.SCALE_SMOOTH);

                blueRobot = blueRobot.getScaledInstance(Settings.ROBOT_SIZE, Settings.ROBOT_SIZE, Image.SCALE_SMOOTH);
                redRobot = redRobot.getScaledInstance(Settings.ROBOT_SIZE, Settings.ROBOT_SIZE, Image.SCALE_SMOOTH);
            }
        }catch(IOException e){}

        return instance;
    }

    public Image getHole(){return hole;}
    public Image getGround(){return ground;}
    public Image getGreenZone(){return greenZone;}

    public Image getMidMineral(){return midMineral;}
    public Image getFullMineral(){return fullMineral;}
    public Image getMineralOnRobot(){return mineralOnRobot;}

    public Image getRadar(){return radar;}
    public Image getRadarOnRobot(){return radarOnRobot;}
    public Image getRadarPlaced(){return radarPlaced;}

    public Image getMine(){return mine;}
    public Image getMineOnRobot(){return mineOnRobot;}
    public Image getMinePlaced(){return minePlaced;}

    public Image getBlueRobot(){return blueRobot;}
    public Image getRedRobot() {return redRobot;}
}
