package it.unical.utility;

import java.awt.*;

public class Settings {

    //ottieni larghezza schermo
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final static double SCREEN_WIDTH = screenSize.getWidth()*0.7;

    public final static int NOMTR = 1; // number of move to remember

    public final static int PLANET_ROW = 15;
    public final static int PLANET_COLUMN = 30;
    public final static int CELL_WIDTH = (int)SCREEN_WIDTH/PLANET_COLUMN;
    public final static int CELL_HEIGHT = CELL_WIDTH;
    public final static int PLANET_WIDTH = PLANET_COLUMN * CELL_WIDTH;
    public final static int PLANET_HEIGHT = PLANET_ROW * CELL_HEIGHT;


    public final static int SCOREBOARD_HEIGHT = 2 * CELL_HEIGHT;
    public final static int LOWERBOARD_HEIGHT = (int)(2.8 * CELL_WIDTH);
    public final static int ROBOT_NUMBER = 3;
    public final static int ROBOT_SIZE = (int)(0.8 * CELL_WIDTH);
    public final static int ITEM_SIZE = (int)(0.6 * CELL_WIDTH);

    // Thread speeds
    public final static int GAME_TIMER = 100_000; // durata partita
    public final static int GAME_TICK = 500;
    public final static int TEAM_TICK = 100; // "durata turno"
    public final static int DISPLAY_TICK = TEAM_TICK/2;

    //Cooldowns
    public final static int MINE_COOLDOWN = 3_000;
    public final static int RADAR_COOLDOWN = 3_000;

}
