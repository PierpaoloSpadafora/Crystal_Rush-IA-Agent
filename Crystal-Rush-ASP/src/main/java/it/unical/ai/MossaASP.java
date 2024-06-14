package it.unical.ai;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("mossa")
public class MossaASP {

    @Param(0)
    private int identifier;
    // identificativo del robot

    @Param(1)
    private int x;
    // -1
    // 0
    // 1

    @Param(2)
    private int y;
    // -1
    // 0
    // 1

    @Param(3)
    private String action;
    // dig
    // place
    // move
    // deliver
    // none

    @Param(4)
    private String item;
    // mine
    // radar
    // gem
    // none

    public MossaASP(int identifier, int x, int y, String action, String item) {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
        this.action = action;
        this.item = item;
    }

    public MossaASP() {
    }

    // ---------------- GETTERS AND SETTERS ----------------
    public int getIdentifier() {
        return identifier;
    }
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "MossaASP{" +
                "identifier=" + identifier +
                ", x=" + x +
                ", y=" + y +
                ", action='" + action + '\'' +
                ", item='" + item + '\'' +
                '}';
    }
}