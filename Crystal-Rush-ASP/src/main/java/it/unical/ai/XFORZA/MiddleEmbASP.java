package it.unical.ai.XFORZA;

import it.unical.ai.MossaASP;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import it.unical.model.Robot;
import it.unical.model.Team;
import it.unical.model.World;
import it.unical.support.ItemEnum;
import it.unical.utility.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiddleEmbASP {

    private static String path_to_dlv = "./src/main/java/lib/dlv-2.1.2-win64.exe";
    private DesktopService service;
    private Handler handler;
    private HashMap<Object, Object> robotVerifier;
    private Team team;
    private World world;

    public MiddleEmbASP(Team team) {
        this.service = new DLV2DesktopService(path_to_dlv);
        this.handler = new DesktopHandler(service);
        this.team = team;
        this.world = World.getInstance();

        try {
            ASPMapper.getInstance().registerClass(ObjectiveASP.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e1) {
            e1.printStackTrace();
        }
    }


    public void standardPredicates(InputProgram encoding){
        encoding.addProgram("row(0.."+ (Settings.PLANET_ROW-1) +").");
        encoding.addProgram("col(0.."+ (Settings.PLANET_COLUMN-1) +").");
        encoding.addProgram("cell(X,Y) :- row(X), col(Y).");

        encoding.addProgram("action(dig).");
        encoding.addProgram("action(place).");
        encoding.addProgram("action(move).");
        encoding.addProgram("action(deliver).");
        encoding.addProgram("action(none).");

        encoding.addProgram("item(none).");
        encoding.addProgram("item(gem).");
        encoding.addProgram("item(radar).");
        encoding.addProgram("item(mine).");
    }

    public void possedimentiRobot(InputProgram encoding){

        this.robotVerifier = new HashMap<>();
        ArrayList<Robot> robots = this.team.getRobots();
        for(Robot r: robots){
            if(r.getItem() == ItemEnum.mine){
                this.robotVerifier.put(r.getIdentifier(),ItemEnum.mine);
            }
            else if(r.getItem() == ItemEnum.radar){
                this.robotVerifier.put(r.getIdentifier(),ItemEnum.radar);
            }
            encoding.addProgram("robot("+r.getIdentifier()+","+this.team.getColor()+", "+r.getXMossaCorrente()+", "+r.getYMossaCorrente()+", "+r.getItem().toString()+").");
        }
    }

    public int isAGoodRadarPlace(int x, int y) {
        int countLegaliPerPiazzare = 0;
        int countNonViste = 0;
        // controlla se troppo vicino ai bordi
        if (x < 2 || x >= Settings.PLANET_ROW-1 - 2 || y < 3 || y >= Settings.PLANET_COLUMN-1 - 2) {
            return 25;
        }

        // controlla se c'è un radar nelle vicinanze
        for (int i = x - 2; i <= x + 2; i++) {
            for (int j = y - 2; j <= y + 2; j++) {
                if (this.world.isMoveInMap(i, j) && j!=0){
                    countLegaliPerPiazzare++;
                    if(!(this.world.getCell(i, j).isVisitedByTeam(this.team.getTeamNumber())) ) {
                        countNonViste++;
                    }
                }
            }
        }
        //
        return countLegaliPerPiazzare-countNonViste;
    }



    public void buonPostoPerRadar(InputProgram encoding){
        for(int x = 0; x< Settings.PLANET_ROW; x++){
            for(int y=0; y<Settings.PLANET_COLUMN; y++){
                int convenienzaRadar = isAGoodRadarPlace(x, y);
                encoding.addProgram("goodRadarPlace("+x+","+y+","+convenienzaRadar+").");
            }
        }
    }

    public List<ObjectiveASP> decideObjective(InputProgram encodingMiddle){

        String filePath = "./src/main/java/it/unical/ai/XFORZA/findObjective.asp";

        standardPredicates(encodingMiddle);
        possedimentiRobot(encodingMiddle);
        buonPostoPerRadar(encodingMiddle);

        encodingMiddle.addProgram("teamColor("+team.getColor()+").");

        encodingMiddle.addFilesPath(filePath);
        handler.addProgram(encodingMiddle);


        OptionDescriptor opt = new OptionDescriptor("--printonlyoptimum");
        handler.addOption(opt);

        System.out.println("Fatti del programma "+ team.getColor().toUpperCase() +" mentre decide l'obiettivo:");
        System.out.println(encodingMiddle.getPrograms());

        Output o = handler.startSync();
        AnswerSets answers = (AnswerSets) o;


        List<ObjectiveASP> objectives = new ArrayList<ObjectiveASP>();

        //System.out.println("\nRisultati:" + answers.getAnswersets().size());

        for(AnswerSet a : answers.getAnswersets()){
            try{
                if (a.getAtoms().isEmpty()) {
                    //System.out.println("No atoms in this answer set.");
                }
                else {
                    // prendi tutti gli atomi e, quelli che corrispondono a Piena, li aggiungi alla lista
                    for(Object obj:a.getAtoms()){
                        if(obj instanceof ObjectiveASP objective){
                            System.out.println(objective + " - " + this.team.getColor());
                            objectives.add(objective);
                        }
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        handler.removeAll();

        return objectives;

    }



}
