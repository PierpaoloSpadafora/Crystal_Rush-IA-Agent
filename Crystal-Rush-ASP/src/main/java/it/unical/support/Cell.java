package it.unical.support;

public class Cell {

    private int gemNumber;
    private boolean hole;
    private int x, y, occupantIndex;
    private boolean visitedByFirstTeam, visitedBySecondTeam;

    public Cell(){
        this.gemNumber = 0;
        this.hole = false;
        this.occupantIndex = -1;
    }

    public Cell(int x, int y){
        this.gemNumber = 0;
        this.hole = false;
        this.x = x;
        this.y = y;
        this.occupantIndex = -1;
    }

    public boolean collectGem(){
        digHole();
        if(this.gemNumber > 0){
            this.gemNumber--;
            return true;
        }
        return false;
    }

    public void digHole(){
        setHole(true);
    }

    public void addGem(){ this.gemNumber++; }

    public boolean containsGem(){ return this.gemNumber > 0; }


    //---------------   GETTERS e SETTERS   ---------------//

    public int getGemNumber(){return this.gemNumber;}
    public void setGemNumber(int gemNumber) {
        this.gemNumber = gemNumber;
    }

    public boolean containsHole() {
        return this.hole;
    }
    public void setHole(boolean hole) {
        this.hole = hole;
    }

    public int getX(){return this.x;}
    public void setX(int x){this.x = x;}

    public int getY(){return this.y;}
    public void setY(int y){this.y = y;}

    public synchronized boolean setOccupied(int id){
        if(this.occupantIndex == -1){
            this.occupantIndex = id;
            return true;
        }
        return false;
    }
    public synchronized boolean setFree(int id) {
        if(this.occupantIndex == id){
            this.occupantIndex = -1;
            return true;
        }
        return false;
    }

    public boolean isVisitedByTeam(int teamNumber) {
        if(teamNumber == 1){
            return visitedByFirstTeam;
        }
        else if(teamNumber == 2){
            return visitedBySecondTeam;
        }
        return false;
    }

    public boolean isVisitedByFirstTeam() {
        return visitedByFirstTeam;
    }
    public void setVisitedByFirstTeam(boolean visitedByFirstTeam) {
        this.visitedByFirstTeam = visitedByFirstTeam;
    }

    public boolean isVisitedBySecondTeam() {
        return visitedBySecondTeam;
    }
    public void setVisitedBySecondTeam(boolean visitedBySecondTeam) {
        this.visitedBySecondTeam = visitedBySecondTeam;
    }

}
