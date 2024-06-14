package it.unical.ai.XFORZA;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("objectiveIs")
public class ObjectiveASP {

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
    // nothing
    // placemine
    // placeradar
    // delivergem
    // digenemyradar
    // pickgem
    // goreceiveitem


    public ObjectiveASP(int identifier, int x, int y, String action) {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
        this.action = action;
    }

    public ObjectiveASP() {
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

    @Override
    public String toString() {
        return "ObjectiveASP{" +
                "identifier=" + identifier +
                ", x=" + x +
                ", y=" + y +
                ", action='" + action + '\'' +
                '}';
    }
}
