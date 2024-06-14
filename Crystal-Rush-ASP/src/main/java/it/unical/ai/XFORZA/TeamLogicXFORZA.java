package it.unical.ai.XFORZA;

import it.unical.ai.MossaASP;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.model.Item;
import it.unical.model.Robot;
import it.unical.model.Team;
import it.unical.model.World;
import it.unical.utility.Settings;

import java.util.*;

public class TeamLogicXFORZA {

    private Team team;
    private World world;
    private InputProgram encoding;
    ArrayList<Item> items;
    ArrayList<Robot> robots;

    HashMap<Integer, ArrayList<Position>> posizioniPassate;


    private MiddleEmbASP middleEmbASP;

    public TeamLogicXFORZA(Team team, InputProgram encoding){
        this.team = team;
        this.world = World.getInstance();
        this.encoding = encoding;
        this.middleEmbASP = new MiddleEmbASP(team);
        this.posizioniPassate = new HashMap<>();
    }

    private boolean isCellAnEnemyMine(int x, int y){
        return this.world.getCell(x, y).isVisitedByTeam(this.team.getTeamNumber()) && this.world.isEnemyMine(x, y, this.team.getColor());
    }


    private int calculateReachingCost(int robotX, int robotY, int destinationX, int destinationY) {
        //  Distanza di Chebyshev
        return Math.max(Math.abs(robotX - destinationX), Math.abs(robotY - destinationY));
    }


    private void pulisciPosizioniPiuVecchieDi3(){
        for (int id : posizioniPassate.keySet()){
            ArrayList<Position> posizioni = posizioniPassate.get(id);
            posizioni.removeIf(p -> this.team.getIteration() - p.getIteration() > Settings.NOMTR);
        }
    }

    private void calcolaCostiIntornoCella(int id, int robotX, int robotY, int destinationX, int destinationY){
        for(int x = robotX-1; x<=robotX+1; x++){
            for(int y = robotY-1; y<=robotY+1; y++){
                if(this.world.isMoveInMap(x,y)){
                    ArrayList<Position> posizioni = posizioniPassate.get(id);
                    if (posizioni == null) {
                        posizioni = new ArrayList<>();
                        posizioniPassate.put(id, posizioni);
                    }
                    for(Position p : posizioni){
                        if(p.equals(new Position(x,y,0))){
                            int cost = calculateReachingCost(x, y, destinationX, destinationY);
                            cost = cost + (Settings.NOMTR-(this.team.getIteration()-p.getIteration()));
                            encoding.addProgram("costOfNextMove("+id+","+x+","+y+","+cost+").");
                        }
                        else{
                            int cost = calculateReachingCost(x, y, destinationX, destinationY);
                            encoding.addProgram("costOfNextMove("+id+","+x+","+y+","+cost+").");
                        }
                    }
                    posizioni.add(new Position(x,y,this.team.getIteration()));
                }
            }
        }
        pulisciPosizioniPiuVecchieDi3();
    }


    public void visibilitaMappaECosti(InputProgram encoding){
        items = this.world.getItemsPositions();
        robots = this.team.getRobots();

        for(int x = 0; x< Settings.PLANET_ROW; x++){
            for(int y=0; y<Settings.PLANET_COLUMN; y++){


                if(this.world.getCell(x,y).isVisitedByTeam(this.team.getTeamNumber())){
                    if(this.world.getCell(x,y).getGemNumber()==0){
                        encoding.addProgram("emptyGem("+x+","+y+").");
                    }
                    else{
                        encoding.addProgram("presentGem("+x+","+y+","+this.world.getCell(x,y).getGemNumber()+").");
                    }

                    if(this.world.isEnemyMine(x,y,this.team.getColor())){
                        encoding.addProgram("enemyMine("+x+","+y+").");
                    }
                    if(this.world.isEnemyRadar(x,y,this.team.getColor())){
                        encoding.addProgram("enemyRadar("+x+","+y+").");
                    }
                }
            }
        }

        for (Item i : items){
            if(Objects.equals(i.getTeamColor(), this.team.getColor())){
                if(i.isMine()){
                    encoding.addProgram("allyMine("+i.getX()+","+i.getY()+").");
                }
                if(i.isRadar()){
                    encoding.addProgram("allyRadar("+i.getX()+","+i.getY()+").");
                }
            }
        }
        
    }
    public void itemAvailable(InputProgram encoding){

        int count = 0;
        if(this.team.isMineReady()){
            count++;
            encoding.addProgram("itemAvailable(mine).");
        }
        if (this.team.isRadarReady()){
            count++;
            encoding.addProgram("itemAvailable(radar).");
        }
        for (int i = 0; i<this.team.getNumRobots()-count; i++){
            encoding.addProgram("itemAvailable(none).");
        }

    }
    public void objectives(InputProgram encoding){
        encoding.addProgram("objective(nothing).");
        encoding.addProgram("objective(placeMine).");
        encoding.addProgram("objective(placeRadar).");
        encoding.addProgram("objective(deliverGem).");
        encoding.addProgram("objective(digEnemyRadar).");
        encoding.addProgram("objective(pickGem).");
        encoding.addProgram("objective(goReceiveItem).");
        encoding.addProgram("objective(comeCloserToAllyRadar).");
    }

    public AnswerSets risolviAspFile(Handler handler, String filePath){
        encoding.addFilesPath(filePath);
        encoding.addProgram("teamColor("+team.getColor()+").");
        //objectives(encoding);
        itemAvailable(encoding);
        visibilitaMappaECosti(encoding);

        handler.addProgram(encoding);

        OptionDescriptor opt = new OptionDescriptor("--printonlyoptimum");
        handler.addOption(opt);

        System.out.println("\nFatti del programma "+ team.getColor().toUpperCase() +" mentre decide la mossa:");
        System.out.println(encoding.getPrograms());

        Output o = handler.startSync();
        AnswerSets answers = (AnswerSets) o;

        return answers;
    }

    public List<ObjectiveASP> decideObjective(Handler handler){

        InputProgram encodingMiddle = new ASPInputProgram();

        //objectives(encodingMiddle);
        itemAvailable(encodingMiddle);
        visibilitaMappaECosti(encodingMiddle);

        List<ObjectiveASP> objectives = this.middleEmbASP.decideObjective(encodingMiddle);
        encodingMiddle = new ASPInputProgram();

        return objectives;
    }



    public List<MossaASP> decideMove(Handler handler, List<ObjectiveASP> objectives){

        List<Robot> robotsAssist = new ArrayList<Robot>();
        for(ObjectiveASP o : objectives){
            synchronized (robots){
                for (Robot r : robots){
                    if(o.getIdentifier() == r.getIdentifier()){
                        encoding.addProgram("objectiveIs("+o.getIdentifier()+","+o.getX()+","+o.getY()+","+o.getAction()+").");
                        if(!(o.getAction().equals("move") || o.getAction().equals("none"))) {
                            posizioniPassate.put(r.getIdentifier(), new ArrayList<Position>());
                        }
                        robotsAssist.add(r);
                    }
                }
            }

            for (Robot r : robotsAssist){
                calcolaCostiIntornoCella(o.getIdentifier(), r.getXMossaCorrente(), r.getYMossaCorrente(), o.getX(), o.getY());
            }
        }

        AnswerSets answers = risolviAspFile(handler, "./src/main/java/it/unical/ai/XFORZA/findMove.asp");

        List<MossaASP> mosse = new ArrayList<MossaASP>();

        for(AnswerSet a : answers.getAnswersets()){
            try{
                // prendi tutti gli atomi e, quelli che corrispondono a Piena, li aggiungi alla lista
                for(Object obj:a.getAtoms()){
                    if(obj instanceof MossaASP m){
                        System.out.println(m + " - " + this.team.getColor());
                        mosse.add(m);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return mosse;
    }


    public List<MossaASP> executeLogic(Handler handler) {

        //System.out.println("TEAM: "+this.team.getTeamName() + " - "+this.team.getTeamNumber() + " - " + this.team.getColor() + " - Sta per decidere l'obiettivo");

        List<ObjectiveASP> objectives = decideObjective(handler);
        return decideMove(handler, objectives);
    }

}
