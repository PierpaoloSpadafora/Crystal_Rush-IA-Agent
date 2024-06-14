package it.unical.support;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class ObserverLabel extends JLabel implements Observer{
    public ObserverLabel(String text){super(text);}
    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof Integer) {
            this.setText(Integer.toString((Integer) o));
        }
    }
}
