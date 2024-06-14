package it.unical.view;

import it.unical.model.Team;
import it.unical.model.World;
import it.unical.support.ObserverLabel;
import it.unical.utility.ImageGetter;
import it.unical.utility.Settings;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ScoreBoard extends JPanel implements Runnable{
    private JLabel nameLabel1;
    private JLabel radarLabel1;
    private JLabel mineLabel1;
    private ObserverLabel pointsLabel1;

    private JLabel nameLabel2;
    private JLabel radarLabel2;
    private JLabel mineLabel2;
    private ObserverLabel pointsLabel2;
    private JLabel timerLabel;
    private int timer = Settings.GAME_TIMER / 1000;

    private Team team1;
    private Team team2;

    public ScoreBoard(){
        this.setLayout(new GridLayout(1, 3));
        this.setPreferredSize(new Dimension(Settings.PLANET_WIDTH, Settings.SCOREBOARD_HEIGHT));
        this.setBackground(Color.DARK_GRAY);

        this.nameLabel1 = new JLabel();
        this.radarLabel1 = new JLabel();
        this.mineLabel1 = new JLabel();
        this.pointsLabel1 = new ObserverLabel("0");

        this.nameLabel2 = new JLabel();
        this.radarLabel2 = new JLabel();
        this.mineLabel2 = new JLabel();
        this.pointsLabel2 = new ObserverLabel("0");
    }

    public void updateLabelRadar(int value, int teamNumber){
        if(teamNumber == 1){
            this.radarLabel1.setText(value + "%");
        } else {
            this.radarLabel2.setText(value + "%");
        }
    }

    public void updateLabelMine(int value, int teamNumber){
        if(teamNumber == 1){
            this.mineLabel1.setText(value + "%");
        } else {
            this.mineLabel2.setText(value + "%");
        }
    }

    public void updateRobotsAlive(int teamNumber){
        if(teamNumber == 1){
            this.nameLabel1.setText(this.team1.getTeamName() + " - " + this.team1.getNumRobAlive() + " in vita");
        }
        else{
            this.nameLabel2.setText(this.team2.getTeamName() + " - " + this.team2.getNumRobAlive() + " in vita");
        }
    }



    public void initializeScoreboard(){
        JPanel p1ScoreBoard = this.makePlayerScoreBoard(this.nameLabel1, this.radarLabel1, this.mineLabel1, this.pointsLabel1, Color.RED);
        JPanel p2ScoreBoard = this.makePlayerScoreBoard(this.nameLabel2, this.radarLabel2, this.mineLabel2, this.pointsLabel2, Color.BLUE);
        JPanel timerPanel = this.makeTimerPanel();
        this.add(p1ScoreBoard);
        this.add(timerPanel);
        this.add(p2ScoreBoard);
    }

    private JPanel makePlayerScoreBoard(JLabel nameLabel, JLabel radarPercentage, JLabel minePercentage, JLabel points, Color color){

        nameLabel.setForeground(Color.WHITE);
        radarPercentage.setForeground(Color.WHITE);
        minePercentage.setForeground(Color.WHITE);
        points.setForeground(Color.WHITE);

        Border border = BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(199, 199, 199));

        //pannello col nome del player
        JPanel playerNamePanel = new JPanel();
        playerNamePanel.setPreferredSize(new Dimension(180, 80));
        playerNamePanel.setBackground(new Color(100, 100, 100));
        playerNamePanel.add(nameLabel);
        playerNamePanel.setBorder(border);

        //pannello con il caricamento delle mine e dei radar
        JPanel objectPanel = new JPanel();
        objectPanel.setPreferredSize(new Dimension(180, 80));
        objectPanel.setBackground(new Color(100, 100, 100));
        ImageIcon radar = new ImageIcon(ImageGetter.getInstance().getRadar());
        JLabel radarLabel = new JLabel(radar);
        ImageIcon mine = new ImageIcon(ImageGetter.getInstance().getMine());
        JLabel mineLabel = new JLabel(mine);
        objectPanel.add(radarPercentage);
        objectPanel.add(radarLabel);
        objectPanel.add(minePercentage);
        objectPanel.add(mineLabel);
        objectPanel.setBorder(border);

        //pannello che contiene i pannelli col nome del player, mine e radar
        JPanel playerPanel = new JPanel();
        GridLayout verticalLayout = new GridLayout(2, 1, 0, 5);
        playerPanel.setLayout(verticalLayout);
        playerPanel.setPreferredSize(new Dimension(180, 80));
        playerPanel.add(playerNamePanel);
        playerPanel.add(objectPanel);
        playerPanel.setBackground(new Color(59, 59, 59));

        //Pannello per il punteggio
        JPanel pointsPanel = new JPanel();
        pointsPanel.setPreferredSize(new Dimension(50, 50));
        pointsPanel.setBackground(color);
        pointsPanel.add(points);
        pointsPanel.setBorder(border);

        JPanel playerScoreBoard = new JPanel();
        playerScoreBoard.setBackground(new Color(59, 59, 59));
        playerScoreBoard.add(playerPanel);
        playerScoreBoard.add(pointsPanel);

        return playerScoreBoard;
    }

    private JPanel makeTimerPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(59, 59, 59));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.CENTER;

        this.timerLabel = new JLabel(Integer.toString(this.timer));
        Font font = this.timerLabel.getFont();
        float size = font.getSize() + 10.0f; // Aumenta la dimensione del carattere di 5 punti
        this.timerLabel.setFont(font.deriveFont(size));
        this.timerLabel.setForeground(Color.ORANGE);
        panel.add(this.timerLabel, gbc);

        gbc.gridy = 1;
        JLabel gems = new JLabel("TOTAL GEMS: " + World.getInstance().getGemsTotalValue());
        gems.setForeground(Color.WHITE);
        panel.add(gems, gbc);

        return panel;
    }

    public ObserverLabel getPointsLabel1(){return this.pointsLabel1;}
    public ObserverLabel getPointsLabel2(){return this.pointsLabel2;}

    @Override
    public void run() {
        while(this.timer > 0 && World.getInstance().isRunning()) {
            this.timer--;
            this.timerLabel.setText(Integer.toString(this.timer));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTeam1(Team team1){
        this.team1 = team1;
    }

    public void setTeam2(Team team2){
        this.team2 = team2;
    }

}
