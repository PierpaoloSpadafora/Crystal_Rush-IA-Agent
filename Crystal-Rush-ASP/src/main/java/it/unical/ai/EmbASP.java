package it.unical.ai;

import it.unical.ai.BOGO.TeamLogicBOGO;
import it.unical.ai.XFORZA.TeamLogicXFORZA;
import it.unical.ai.GRUPPO_BELLO.TeamLogicGRUPPO_BELLO;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.*;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import it.unical.model.*;
import it.unical.support.ActionEnum;
import it.unical.support.ItemEnum;
import it.unical.utility.Settings;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmbASP {


    private static String path_to_dlv = "./src/main/java/lib/dlv-2.1.2-win64.exe";
    private DesktopService service;
    private Handler handler;
    private InputProgram encoding;
    private Team team;

    private HashMap<Integer, ItemEnum> robotVerifier;

    public EmbASP(Team team) {
        this.service = new DLV2DesktopService(path_to_dlv);
        this.handler = new DesktopHandler(service);
        this.team = team;

        try {
            ASPMapper.getInstance().registerClass(MossaASP.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e1) {
            e1.printStackTrace();
        }

        this.encoding = new ASPInputProgram();
    }


    public void standardPredicates(){
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
    public void possedimentiRobot(){

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

    public List<MossaASP> TeamLogic(){
        List<MossaASP> mosse = null;
        switch (this.team.getTeamName()){
            case "BOGO" -> {
                TeamLogicBOGO teamLogic = new TeamLogicBOGO(this.team, encoding);
                mosse = teamLogic.executeLogic(handler);
            }
            case "XFORZA" -> {
                TeamLogicXFORZA teamLogic = new TeamLogicXFORZA(this.team, encoding);
                mosse = teamLogic.executeLogic(handler);
            }
            case "GRUPPO_BELLO" -> {
                TeamLogicGRUPPO_BELLO teamLogic = new TeamLogicGRUPPO_BELLO(this.team, encoding);
                mosse = teamLogic.executeLogic(handler);
            }
            case "CIGNI_HI_TECH" -> {
                //TeamLogicGRUPPO_BELLO teamLogic = new TeamLogicGRUPPO_BELLO(this.team, encoding);
                //mosse = teamLogic.executeLogic(handler);
            }
            case "PIO" -> {
                //TeamLogicGRUPPO_BELLO teamLogic = new TeamLogicGRUPPO_BELLO(this.team, encoding);
                //mosse = teamLogic.executeLogic(handler);
            }
        }
        return mosse;

    }

    public HashMap<Integer,Command> compiAzioneTeam(){

        HashMap<Integer,Command> commandsMap = new HashMap<>();

        standardPredicates();
        possedimentiRobot();

        List<MossaASP> mosse = TeamLogic();

        // assicurati che il team gestisca bene il cooldown di mine e radar
        for (MossaASP m : mosse){

            if(m.getItem().equals("mine")){
                if(!robotVerifier.containsKey(m.getIdentifier())){
                    if(!(robotVerifier.get(m.getIdentifier()) == ItemEnum.mine)){
                        this.team.mineUsed();
                    }
                }
            }
            else if(m.getItem().equals("radar")){
                if(!robotVerifier.containsKey(m.getIdentifier())){
                    if(!(robotVerifier.get(m.getIdentifier()) == ItemEnum.radar)){
                        this.team.radarUsed();
                    }
                }
            }

            Command c = new Command.Builder(m.getX(), m.getY())
                    .withIdentifier(m.getIdentifier())
                    .withAction(ActionEnum.valueOf(m.getAction()))
                    .withItem(ItemEnum.valueOf(m.getItem()))
                    .withColor(team.getColor())
                    .build();
            commandsMap.put(m.getIdentifier(),c);
        }

        // resetta handler e encoding
        handler.removeAll();
        encoding = new ASPInputProgram();


        return commandsMap;
    }




}
