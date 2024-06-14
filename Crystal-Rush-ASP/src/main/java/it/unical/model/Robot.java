package it.unical.model;

import it.unical.support.ActionEnum;
import it.unical.support.ItemEnum;

public class Robot {

    private final int identifier; // Identificativo del robot
    private Command mossaDaFare;
    private Command mossaCorrente;
    private ItemEnum item;

    public Robot(int x, int y, int identifier) {
        this.identifier = identifier;
        this.mossaCorrente = new Command.Builder(x, y)
                .withIdentifier(identifier)
                .build();
        this.mossaDaFare = null;
        this.item = ItemEnum.none;
    }

    // ---------------- GETTERS AND SETTERS ----------------
    public int getIdentifier() {
        return identifier;
    }
    public void setIdentifier(int identifier) {
        this.mossaCorrente.setIdentifier(identifier);
    }

    public Command getMossaDaFare() {
        return mossaDaFare;
    }
    public int getXMossaDaFare() {
        return this.mossaDaFare.getX();
    }
    public int getYMossaDaFare() {
        return this.mossaDaFare.getY();
    }

    public void setMossaDaFare(Command mossaDaFare) {
        this.mossaDaFare = mossaDaFare;
    }
    public void setXMossaDaFare(Command mossaDaFare) {
        this.mossaDaFare.setX(mossaDaFare.getX());
    }
    public void setYMossaDaFare(Command mossaDaFare) {
        this.mossaDaFare.setY(mossaDaFare.getY());
    }

    public Command getMossaCorrente() {
        return mossaCorrente;
    }
    public int getXMossaCorrente() {
        return this.mossaCorrente.getX();
    }
    public int getYMossaCorrente() {
        return this.mossaCorrente.getY();
    }

    public void setMossaCorrente(Command mossaCorrente) {
        this.mossaCorrente = mossaCorrente;
    }
    public void setXMossaCorrente(Command mossaCorrente) {
        this.mossaCorrente.setX(mossaCorrente.getX());
    }
    public void setYMossaCorrente(Command mossaCorrente) {
        this.mossaCorrente.setY(mossaCorrente.getY());
    }

    public ItemEnum getItem() {
        return item;
    }
    public void setItem(ItemEnum item) {
        this.item = item;
    }
    public ItemEnum giveItem() {
        ItemEnum temp = this.item;
        this.item = ItemEnum.none;
        return temp;
    }

}
