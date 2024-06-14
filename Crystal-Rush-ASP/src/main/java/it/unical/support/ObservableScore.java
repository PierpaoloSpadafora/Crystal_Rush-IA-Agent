package it.unical.support;

import java.util.Observable;

public class ObservableScore extends Observable {
    private int score;

    public ObservableScore(){this.score = 0;}

    public void incrementScore(){
        this.score++;
        setChanged();
        notifyObservers(score);
    }

    public int getScore(){return this.score;}
}
