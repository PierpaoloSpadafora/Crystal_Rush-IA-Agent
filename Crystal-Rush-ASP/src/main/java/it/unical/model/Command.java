package it.unical.model;

import it.unical.support.ActionEnum;
import it.unical.support.ItemEnum;

public class Command {
    private int x;
    private int y;
    private int identifier;
    private String color;
    private ActionEnum action;
    private ItemEnum item;

    /* ESEMPIO DI CREAZIONE OGGETTO

    Command coordinates = new Command.Builder(10, 20)
    .withColor("red")
    .withAction(ActionEnum.none)
    .withItem(ItemEnum.none)
    .withIdentifier(1)
    .build();

     */

    private Command(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.identifier = builder.identifier;
        this.color = builder.color;
        this.action = builder.action;
        this.item = builder.item;
    }

    public static class Builder {
        private int x;
        private int y;
        private int identifier;
        private String color = "";
        private ActionEnum action = ActionEnum.none;
        private ItemEnum item = ItemEnum.none;

        public Builder(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Builder withIdentifier(int identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder withColor(String color) {
            this.color = color;
            return this;
        }

        public Builder withAction(ActionEnum action) {
            this.action = action;
            return this;
        }

        public Builder withItem(ItemEnum item) {
            this.item = item;
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }


    // ---------------- GETTERS AND SETTERS ----------------
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

    public int getIdentifier() {
        return identifier;
    }
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public ActionEnum getAction() {
        return action;
    }
    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public ItemEnum getItem() {
        return item;
    }
    public void setItem(ItemEnum item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Command{" +
                "x=" + x +
                ", y=" + y +
                ", identifier=" + identifier +
                ", color='" + color + '\'' +
                ", action=" + action +
                ", item=" + item +
                '}';
    }
}
