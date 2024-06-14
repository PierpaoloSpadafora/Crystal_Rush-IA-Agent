package it.unical.ai.GRUPPO_BELLO;

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
import java.util.Arrays;
import java.util.List;

public class TeamLogicGRUPPO_BELLO {
    private Team team;
    private World world;
    private InputProgram encoding;

    private int GemmeTrovate;
    ArrayList<String> cellaMineList = new ArrayList<>(Arrays.asList(
            "cellaMine(0,16).",
            "cellaMine(1,17).",
            "cellaMine(2,16).",
            "cellaMine(3,17).",
            "cellaMine(4,16).",
            "cellaMine(5,17).",
            "cellaMine(6,16).",
            "cellaMine(7,17).",
            "cellaMine(8,16).",
            "cellaMine(9,17).",
            "cellaMine(10,16).",
            "cellaMine(11,17).",
            "cellaMine(12,16).",
            "cellaMine(13,17).",
            "cellaMine(14,16)."
    ));



    int k=0;
    int x=0;
    public TeamLogicGRUPPO_BELLO(Team team, InputProgram encoding) {
        this.team = team;
        this.world = World.getInstance();
        this.encoding = encoding;



    }

    public List<MossaASP> executeLogic(Handler handler) {
        encoding.addFilesPath("./src/main/java/it/unical/ai/Gruppo_Bello/GruppoBello.asp");
        handler.addProgram(encoding);
        OptionDescriptor opt = new OptionDescriptor("--printonlyoptimum");
        handler.addOption(opt);

        // Stampa tutti i fatti del programma
        System.out.println("Fatti del programma:");


        for (Robot r : this.team.getRobots()) {
            String robotFact = String.format("robot(%d,%s, %d, %d, %s).", r.getIdentifier(), this.team.getColor(), r.getXMossaCorrente(), r.getYMossaCorrente(), r.getItem().toString());
            System.out.println(robotFact);
            encoding.addProgram(robotFact);
        }


        OndataMine();
        if (world.getCell(2, 8).isVisitedByTeam(team.getTeamNumber())) {
            GemmeTrovate = TrovaMinerale(0, 4, 6, 10);
            if (GemmeTrovate == 0) {
                if (world.getCell(6, 8).isVisitedByTeam(team.getTeamNumber())) {
                    GemmeTrovate = TrovaMinerale(4, 8, 6, 10);
                    if (GemmeTrovate == 0) {
                        if (world.getCell(12, 8).isVisitedByTeam(team.getTeamNumber())) {
                            GemmeTrovate = TrovaMinerale(10, 14, 6, 10);
                            if (GemmeTrovate == 0) {
                                if (world.getCell(2, 20).isVisitedByTeam(team.getTeamNumber())) {
                                    GemmeTrovate = TrovaMinerale(0, 4, 18, 22);
                                    if (GemmeTrovate == 0) {
                                        if (world.getCell(2, 27).isVisitedByTeam(team.getTeamNumber())) {
                                            GemmeTrovate = TrovaMinerale(0, 4, 25, 29);
                                            if (GemmeTrovate == 0) {
                                                if (world.getCell(7, 20).isVisitedByTeam(team.getTeamNumber())) {
                                                    GemmeTrovate = TrovaMinerale(5, 9, 18, 22);
                                                    if (GemmeTrovate == 0) {
                                                        if (world.getCell(7, 27).isVisitedByTeam(team.getTeamNumber())) {
                                                            GemmeTrovate = TrovaMinerale(5, 9, 25, 29);
                                                            if (GemmeTrovate == 0) {
                                                                if (world.getCell(12, 20).isVisitedByTeam(team.getTeamNumber())) {
                                                                    GemmeTrovate = TrovaMinerale(10, 14, 18, 22);
                                                                    if (GemmeTrovate == 0) {
                                                                        if (world.getCell(12, 27).isVisitedByTeam(team.getTeamNumber())) {
                                                                            GemmeTrovate = TrovaMinerale(10, 14, 25, 29);

                                                                        } else {
                                                                            encoding.addProgram("cellaRadar(12,27).");
                                                                        }
                                                                    }
                                                                } else {
                                                                    encoding.addProgram("cellaRadar(12,20).");
                                                                }
                                                            }

                                                        } else {
                                                            encoding.addProgram("cellaRadar(7,27).");
                                                        }
                                                    }

                                                } else {
                                                    encoding.addProgram("cellaRadar(7,20).");
                                                }
                                            }
                                        } else {
                                            encoding.addProgram("cellaRadar(2,27).");
                                        }
                                    }
                                } else {
                                    encoding.addProgram("cellaRadar(2,20).");
                                }
                            }
                        } else {
                            encoding.addProgram("cellaRadar(12,8).");
                        }
                    }
                } else {
                    encoding.addProgram("cellaRadar(6,8).");
                }
            }

        }else {
            encoding.addProgram("cellaRadar(2,8).");
        }

        System.out.println(encoding.getPrograms());
        Output o = handler.startSync();
        AnswerSets answers = (AnswerSets) o;
        List<MossaASP> mosse = new ArrayList<>();

        for (AnswerSet a : answers.getAnswersets()) {
            try {
                for (Object obj : a.getAtoms()) {
                    if (obj instanceof MossaASP m) {
                        System.out.println(m);
                        mosse.add(m);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        return mosse;
    }


    private int TrovaMinerale(int xI, int xF, int yI, int yF) {
        int GemmeTrovate=0;
        for (int r = xI; r <= xF; r++) {
            for (int c = yI; c <= yF; c++) {
                if (world.getCell(r, c).isVisitedByTeam(team.getTeamNumber()) && world.getCell(r, c).containsGem()) {
                    encoding.addProgram("cellaMinerale(" + r + "," + c + ").");
                    System.out.println("cellaMinerale(" + r + "," + c + ").");
                    GemmeTrovate++;

                }

                if (GemmeTrovate == 1) {
                    break;
                }
            }
            if (GemmeTrovate == 1) {
                break;
            }


        }
        return GemmeTrovate;
    }

    private void OndataMine() {
        if (world.getCell(x, 16).containsHole()) {
            x++;

            if (world.getCell(x, 17).containsHole()) {
                x++;
                if (world.getCell(x, 16).containsHole()) {
                    x++;
                    if (world.getCell(x, 17).containsHole()) {
                        x++;
                        if (world.getCell(x, 16).containsHole()) {
                            x++;
                            if (world.getCell(x, 17).containsHole()) {
                                x++;
                                if (world.getCell(x, 16).containsHole()) {
                                    x++;
                                    if (world.getCell(x, 17).containsHole()) {
                                        x++;
                                        if (world.getCell(x, 16).containsHole()) {
                                            x++;
                                            if (world.getCell(x, 17).containsHole()) {
                                                x++;
                                                if (world.getCell(x, 16).containsHole()) {
                                                    x++;
                                                    if (world.getCell(x, 17).containsHole()) {
                                                        x++;
                                                        if (world.getCell(x, 16).containsHole()) {
                                                            x++;
                                                            if (world.getCell(x, 17).containsHole()) {
                                                                x++;
                                                                if (world.getCell(x, 16).containsHole()) {
                                                                    encoding.addProgram("mineFinite(1).");
                                                                } else {
                                                                    encoding.addProgram(cellaMineList.get(x));
                                                                }
                                                            } else {
                                                                encoding.addProgram(cellaMineList.get(x));
                                                            }
                                                        } else {
                                                            encoding.addProgram(cellaMineList.get(x));
                                                        }
                                                    } else {
                                                        encoding.addProgram(cellaMineList.get(x));
                                                    }
                                                } else {
                                                    encoding.addProgram(cellaMineList.get(x));
                                                }
                                            } else {
                                                encoding.addProgram(cellaMineList.get(x));
                                            }
                                        } else {
                                            encoding.addProgram(cellaMineList.get(x));
                                        }
                                    } else {
                                        encoding.addProgram(cellaMineList.get(x));
                                    }
                                } else {
                                    encoding.addProgram(cellaMineList.get(x));
                                }
                            } else {
                                encoding.addProgram(cellaMineList.get(x));
                            }
                        } else {
                            encoding.addProgram(cellaMineList.get(x));
                        }
                    } else {
                        encoding.addProgram(cellaMineList.get(x));
                    }
                } else {
                    encoding.addProgram(cellaMineList.get(x));
                }
            } else {
                encoding.addProgram(cellaMineList.get(x));
            }
        } else {
            encoding.addProgram(cellaMineList.get(x));
        }
    }







}
