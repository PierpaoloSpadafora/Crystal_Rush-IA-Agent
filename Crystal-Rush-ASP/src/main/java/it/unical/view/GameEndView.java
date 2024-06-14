package it.unical.view;

import javax.swing.*;

public class GameEndView extends JFrame {
    public GameEndView(String message){
        String[] options = {"New game", "Exit"};
        int choose = JOptionPane.showOptionDialog(this, message, "FINE PARTITA", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);


        if(choose == JOptionPane.YES_OPTION) new IntroView();
        else System.exit(0);
    }

}
