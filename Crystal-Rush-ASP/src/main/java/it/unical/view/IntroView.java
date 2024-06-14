package it.unical.view;

import it.unical.GameHandler;
import it.unical.model.NameTeam;
import it.unical.model.Team;
import it.unical.utility.ImageGetter;
import it.unical.utility.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class IntroView extends JFrame {
    private JComboBox<NameTeam> teamRedComboBox;
    private JComboBox<NameTeam> teamBlueComboBox;
    private JButton startButton;

    public IntroView() {
        ImageIcon redRobot = new ImageIcon(ImageGetter.getInstance().getRedRobot());
        ImageIcon blueRobot = new ImageIcon(ImageGetter.getInstance().getBlueRobot());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setBackground(new Color(37, 37, 37));
        add(panel);


        // Team Red
        JPanel redPanel = new JPanel();
        redPanel.setBackground(new Color(37, 37, 37));
        JLabel redIcon = new JLabel(redRobot);
        teamRedComboBox = new JComboBox<>(NameTeam.values());
        teamRedComboBox.setSelectedIndex(1);
        teamRedComboBox.setBounds(25, 50, 350, 40);
        teamRedComboBox.setBackground(new Color(250, 251, 254));
        redPanel.add(redIcon);
        redPanel.add(teamRedComboBox);
        panel.add(redPanel);

        // Team Blue
        JPanel bluePanel = new JPanel();
        bluePanel.setBackground(new Color(37, 37, 37));
        JLabel blueIcon = new JLabel(blueRobot);
        teamBlueComboBox = new JComboBox<>(NameTeam.values());
        // imposta il valore predefinito a 1
        teamBlueComboBox.setSelectedIndex(2);
        teamBlueComboBox.setBounds(25, 110, 350, 40);
        teamBlueComboBox.setBackground(new Color(250, 251, 254));
        bluePanel.add(blueIcon);
        bluePanel.add(teamBlueComboBox);
        panel.add(bluePanel);


        startButton = new JButton("START");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(37, 37, 37));
        buttonPanel.add(startButton);
        startButton.setBounds(25, 170, 350, 40);
        startButton.setForeground(new Color(74, 181, 37));
        startButton.setFocusPainted(false);
        startButton.setFont(startButton.getFont().deriveFont(Font.BOLD));
        startButton.addActionListener(
            e -> {
                dispose();

                ArrayList<Team> teams = new ArrayList<>();

                ScoreBoard scoreboard = new ScoreBoard();

                Team team1 = new Team("red", Settings.ROBOT_NUMBER, teamRedComboBox.getItemAt(teamRedComboBox.getSelectedIndex()), 1, scoreboard);
                Team team2 = new Team("blue", Settings.ROBOT_NUMBER, teamBlueComboBox.getItemAt(teamBlueComboBox.getSelectedIndex()), 2, scoreboard);

                scoreboard.setTeam1(team1);
                scoreboard.setTeam2(team2);

                teams.add(team1);
                teams.add(team2);

                GameHandler gameHandler = new GameHandler(teams, scoreboard);
                gameHandler.start();
            }
        );
        panel.add(buttonPanel);


        setTitle("Crystal Rush");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

}
