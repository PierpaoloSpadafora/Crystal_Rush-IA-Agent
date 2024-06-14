package it.unical.ai.XFORZA;

public class Position {

    private int idRobot;
    private int iteration;
    private int x;
    private int y;

    public Position(int x, int y, int iteration) {
        this.x = x;
        this.y = y;
        this.iteration = iteration;
    }

    public int idRobot() {
        return idRobot;
    }

    public void setIdRobot(int idRobot) {
        this.idRobot = idRobot;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position clone() {
        return new Position(x, y, iteration);
    }

    public int distance(Position p) {
        return Math.abs(x - p.getX()) + Math.abs(y - p.getY());
    }

    public boolean isAdjacent(Position p) {
        return distance(p) == 1;
    }

    public boolean isIterationOlder(int iteration){
        return this.iteration < iteration;
    }

    public boolean isIterationNewer(int iteration){
        return this.iteration > iteration;
    }

    public boolean equals(Position p) {
        return this.x == p.getX() && this.y == p.getY();
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }






}
