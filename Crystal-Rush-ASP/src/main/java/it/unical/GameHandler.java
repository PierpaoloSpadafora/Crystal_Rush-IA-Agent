package it.unical;


import it.unical.model.Team;
import it.unical.model.World;
import it.unical.utility.Settings;
import it.unical.view.Display;
import it.unical.view.GameEndView;
import it.unical.view.ScoreBoard;

import java.util.ArrayList;

public class GameHandler extends Thread {

    private World world;
    private ArrayList<Team> teams;
    private ScoreBoard scoreBoard;

    public GameHandler(ArrayList<Team> teams, ScoreBoard scoreBoard) {
        Display display = new Display(scoreBoard);
        this.scoreBoard = display.getScoreBoard();
        this.world = World.getInstance();
        this.teams = teams;

        Team teamRed = null;
        Team teamBlue = null;
        for (Team team : teams) {
            if(team.getColor().equals("red")){
                teamRed = team;
            }
            else if(team.getColor().equals("blue")){
                teamBlue = team;
            }
        }

        teamRed.linkLabel(display.getScoreBoard().getPointsLabel1());
        teamBlue.linkLabel(display.getScoreBoard().getPointsLabel2());
        display.getScoreBoard().initializeScoreboard();

        display.start();
        startTeams();
    }

    public void run() {
        world = World.getInstance();
        world.setRunning();

        Thread scoreBoardThread = new Thread(this.scoreBoard);
        scoreBoardThread.start();

        ConditionsEndGame();



        Team teamRed = null;
        Team teamBlue = null;
        for (Team team : teams) {
            if(team.getColor().equals("red")){
                teamRed = team;
            }
            else if(team.getColor().equals("blue")){
                teamBlue = team;
            }
        }

        int scoreBlu = teamRed.getScore();
        int scoreRed = teamBlue.getScore();

        if(scoreBlu > scoreRed){
            new GameEndView( teamBlue.getTeamName() + " - " + teamBlue.getColor() + " won the game!");
        } else if(scoreBlu < scoreRed){
            new GameEndView(teamRed.getTeamName() + " - " + teamRed.getColor() + " won the game!");
        } else {
            new GameEndView("Draw!");
        }
    }


    public void ConditionsEndGame(){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < Settings.GAME_TIMER) {
            if(oneTeamAlive()) {
                world.setFinished();
                for (Team team : teams) {
                    if (team.isStillAlive()) {
                        new GameEndView(team.getTeamName() + " - " + team.getColor() + " won the game!");
                        return;
                    }
                }
            }
            else {
                for(Team team : teams)
                    if (team.isGemNumberEnoughToWin()) {
                        world.setFinished();
                        new GameEndView(team.getTeamName() + " - " + team.getColor() + " won the game!");
                        return;
                    }
            }
            this.dormi(Settings.GAME_TICK);
        }
        world.setFinished();
    }

    public void startTeams() {
        for (Team team : teams) {
            team.start();
        }
    }

    public boolean oneTeamAlive() {
        int count = 0;
        for (Team team : teams) {
            if (team.isStillAlive()) {
                count++;
            }
        }
        return count == 1;
    }

    private void dormi(int millisecondi) {
        try {
            Thread.sleep(millisecondi);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
