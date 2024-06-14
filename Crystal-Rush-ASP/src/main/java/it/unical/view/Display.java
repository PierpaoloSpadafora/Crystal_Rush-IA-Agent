package it.unical.view;

import it.unical.utility.Settings;
import it.unical.model.World;

import javax.swing.*;
import java.awt.*;

public class Display extends Thread {

    private World world;
    private JFrame window;
    private GameView gameView = new GameView();
    private ScoreBoard scoreBoard;

    public Display(ScoreBoard scoreBoard){
        this.world =  World.getInstance();
        this.scoreBoard = scoreBoard;
        setupWindow();
    }

    public void run() {
        while(!world.isRunning()) {
            dormi(Settings.DISPLAY_TICK);
        }

        //System.out.println("Display ha iniziato l'esecuzione");

        while(world.isRunning()) {
            gameView.repaint();
            dormi(Settings.DISPLAY_TICK);
        }

        //System.out.println("Display ha terminato l'esecuzione");
    }

    private void setupWindow() {
        window = new JFrame("Game Window");
        window.setSize(Settings.PLANET_WIDTH+15, Settings.PLANET_HEIGHT + Settings.LOWERBOARD_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setLocationRelativeTo(null);
        window.add(gameView, BorderLayout.CENTER);
        window.add(scoreBoard, BorderLayout.NORTH);
        window.setVisible(true);

    }

    // stampa

    private void dormi(int millisecondi) {
        try {
            Thread.sleep(millisecondi);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ScoreBoard getScoreBoard(){return this.scoreBoard;}
}
