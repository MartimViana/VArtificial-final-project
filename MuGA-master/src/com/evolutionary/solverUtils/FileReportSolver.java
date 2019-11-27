//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::     Antonio Manso                        Luis Correia                   ::
//::     manso@ipt.pt                   Luis.Correia@ciencias.ulisboa.pt     ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::                                                                         ::
//::     Instituto Politécnico de Tomar                                      ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                             (c) 2019    ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.solverUtils;

import com.evolutionary.Genetic;
import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.statistics.AbstractStatistics;
import static com.evolutionary.solverUtils.FileSolver.RUNNING_TIME;
import static com.evolutionary.solverUtils.FileSolver.SOLVER_KEY;
import static com.evolutionary.solverUtils.FileSolver.START_TIME;
import static com.evolutionary.solverUtils.FileSolver.STATISTIC_KEY;
import com.utils.MyFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created on 2/fev/2017, 12:14:11
 *
 * @author zulu - computer
 */
public class FileReportSolver {

    public static ReportSolver loadReportFromFile(String file) {
        return loadFromTxt(MyFile.readFile(file));
    }

    public static ReportSolver loadFromTxt(String txt) {
        ReportSolver report = new ReportSolver();
        report.setStatistics(loadStatisticsElements(txt));
        loadEvolution(txt, report);
        return report;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::  L O A D   E L E M E N T S :::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static void loadEvolution(String txt, ReportSolver report) {
        //----------------------------------------------------------------------
        //load simulation data                               
        //----------------------------------------------------------------------
        Scanner file = new Scanner(txt);
        String line;
        while (file.hasNext()) {
            line = file.nextLine().trim();
            if (line.startsWith(START_TIME)) {
                report.startTime = new Date(Long.parseLong(line.substring(START_TIME.length()).trim()));
            } else if (line.startsWith(RUNNING_TIME)) {
                report.runningTime = Long.parseLong(line.substring(RUNNING_TIME.length()).trim());
            } //----------------  Statistic
            else if (line.startsWith(STATISTIC_KEY)) {
                //remove key         
                line = line.substring(STATISTIC_KEY.length()).trim();
                //ignore first number (generation number)
                line = line.substring(line.indexOf(" "));
                report.addDataToStatistics(line);
            } //Solver          
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::  L O A D   S T A T I S T I C S   E L E M E N T S :::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static ArrayList<AbstractStatistics> loadStatisticsElements(String txt) {
        String KEY = FileSolver.STATISTIC;
        String KEY_LIB = FileSolver.STATISTIC_LIB;

        //get elements of key  
        ArrayList<String> elemSet = new ArrayList<>(); // unique elements
        Scanner file = new Scanner(txt);
        String line;
        while (file.hasNext()) {
            line = file.nextLine().trim();
            if (line.startsWith(KEY)) {
                //remove key
                line = line.substring(KEY.length()).trim();
                // bad definition
                if (line.indexOf("=") != 0) {
                    continue;
                }
                //remove =
                line = line.substring(1).trim();
                elemSet.add(KEY_LIB + line);
            }
            // information about evolution ?
            if (line.startsWith(STATISTIC_KEY) || line.startsWith(SOLVER_KEY)) {
                break;
            }
        }

        ArrayList<AbstractStatistics> stats = new ArrayList<>();
        for (String geneticElem : elemSet) {
            try {//try to build a genetic alement
                Genetic g = Genetic.createNew(geneticElem);
                stats.add((AbstractStatistics) g);
            } catch (Exception e) {
                //not a genetic element
            }
        }
        return stats;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201702021214L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        ReportSolver report = loadReportFromFile("D:/Cloud/Dropbox/PHD/code/GIT/MuGA_03_05/simulation/muGA/muGA_000.txt");
        System.out.println(report.getEvolutionString());

    }
}
