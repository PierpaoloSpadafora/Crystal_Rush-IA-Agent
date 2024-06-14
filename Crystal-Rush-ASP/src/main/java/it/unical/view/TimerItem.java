package it.unical.view;

import it.unical.model.World;
import it.unical.utility.Settings;

import javax.swing.*;

public class TimerItem extends Thread{
    private JLabel label;
    private int value;
    private final int threshold = 4;
    private int objectUsed = 0;

    public TimerItem(JLabel label){
        this.value = Settings.DISPLAY_TICK;
        this.label = label;
        this.label.setText(value + "%");
    }

    @Override
    public void run(){
        while(true){
            if(this.objectUsed == 1){
                this.refreshTimer();
                this.label.setText(this.value + "%");
                dormi(Settings.DISPLAY_TICK);
            }
        }
    }

    public int getValue(){return this.value;}
    public synchronized void refreshTimer(){
        if(this.value == 100)
            this.value = 0;
    }


    private void dormi(int millisecondi){
        try {
            Thread.sleep(millisecondi); // Controlla ogni secondo
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}


