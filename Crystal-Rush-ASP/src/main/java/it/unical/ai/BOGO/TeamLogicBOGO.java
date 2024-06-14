package it.unical.ai.BOGO;

import it.unical.ai.MossaASP;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.model.Robot;
import it.unical.model.Team;
import it.unical.model.World;

import java.util.ArrayList;
import java.util.List;

public class TeamLogicBOGO {

    private Team team;
    private World world;
    private InputProgram encoding;

    public TeamLogicBOGO(Team team, InputProgram encoding){
        this.team = team;
        this.world = World.getInstance();
        this.encoding = encoding;
    }

    public List<MossaASP> executeLogic(Handler handler){

        encoding.addFilesPath("./src/main/java/it/unical/ai/BOGO/bogo.asp");
        handler.addProgram(encoding);
        OptionDescriptor opt = new OptionDescriptor("--printonlyoptimum");
        handler.addOption(opt);

        // stampa tutti i fatti del programma
        System.out.println("Fatti del programma:");
        System.out.println(encoding.getPrograms());

        for(Robot r : this.team.getRobots()){
            int randomX = (int) (Math.random() * 3) - 1;
            int randomY = (int) (Math.random() * 3) - 1;
            while(!(this.world.isMoveInMap(r.getXMossaCorrente()+randomX, r.getYMossaCorrente()+randomY))){
                randomX = (int) (Math.random() * 3) - 1;
                randomY = (int) (Math.random() * 3) - 1;
            }
            encoding.addProgram("coords("+r.getIdentifier()+","+ (r.getXMossaCorrente()+randomX) +","+ (r.getYMossaCorrente()+randomY) +").");
        }

        Output o = handler.startSync();
        AnswerSets answers = (AnswerSets) o;

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





}
