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

import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.ReportSolverArray;
import static com.evolutionary.solverUtils.FileSolver.RUNNING_TIME;
import static com.evolutionary.solverUtils.FileSolver.SOLVER_KEY;
import static com.evolutionary.solverUtils.FileSolver.START_TIME;
import static com.evolutionary.solverUtils.FileSolver.STATISTIC_KEY;
import com.utils.MyFile;
import java.util.Date;
import java.util.Scanner;

/**
 * Created on 2/fev/2017, 12:14:11
 *
 * @author zulu - computer
 */
public class FileReportArray extends FileReportSolver {

    public static ReportSolverArray loadReportFromFile(String file) {
        return loadReportFromTxt(MyFile.readFile(file));
    }

    public static ReportSolverArray loadReportFromTxt(String txt) {
        ReportSolverArray report = new ReportSolverArray();
        report.setStatistics(loadStatisticsElements(txt));
        loadEvolution(txt, report);        
        return report;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::  L O A D   E L E M E N T S :::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static void loadEvolution(String txt, ReportSolverArray report) {
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
            else if (line.startsWith(SOLVER_KEY)) {
                //remove key
                line = line.substring(SOLVER_KEY.length()).trim();
                //ignore first number (generation number)
                line = line.substring(line.indexOf(" "));
                report.addDataToResult(line);
            }
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201702021214L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        ReportSolverArray report = loadReportFromFile("D:/Cloud/Dropbox/PHD/code/GIT/MuGA_03_05/simulation/muGA.txt");
        System.out.println(report.getEvolutionString());
        
    }
}
