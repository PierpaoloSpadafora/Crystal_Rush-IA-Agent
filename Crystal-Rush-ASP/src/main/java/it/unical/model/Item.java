package it.unical.model;

import it.unical.support.ItemEnum;

import java.util.Objects;

public class Item { // sarà la classe principale con cui interagirà il robot con embasp
    private int x;
    private int y;
    private String teamColor;
    private final ItemEnum item;

    private Item(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.teamColor = builder.teamColor;
        this.item = builder.item;
    }

    public static class Builder {
        private int x;
        private int y;
        private String teamColor;
        private ItemEnum item;

        public Builder(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Builder withTeamColor(String teamColor) {
            this.teamColor = teamColor;
            return this;
        }

        public Builder withItem(ItemEnum item) {
            this.item = item;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

    public ItemEnum getItem() {
        return item;
    }

    public boolean isRadar(){
        return this.item == ItemEnum.radar;
    }

    public boolean isMine(){
        return this.item == ItemEnum.mine;
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

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item that = (Item) o;
        return x == that.x && y == that.y && Objects.equals(teamColor, that.teamColor) && Objects.equals(item, that.item);
    }

    @Override
    public String toString() {
        return "Item{" +
                "x=" + x +
                ", y=" + y +
                ", teamColor='" + teamColor + '\'' +
                ", item='" + item + '\'' +
                '}';
    }

}
