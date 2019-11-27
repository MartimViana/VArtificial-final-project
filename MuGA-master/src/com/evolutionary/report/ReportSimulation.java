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

package com.evolutionary.report;

import com.evolutionary.report.compareMeans.Tstudent;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.utils.MyFile;
import com.utils.MyStatistics;
import com.utils.MyString;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 28/mar/2016, 13:06:37
 *
 * @author zulu - computer
 */
public class ReportSimulation {

    public static String getReportMeans(ArrayList<EAsolver> solvers) {
        StringBuffer txt = new StringBuffer();
        txt.append(solvers.get(0).report.getStatisticsHeader("AVG"));
        for (int index = 0; index < solvers.size(); index++) {
            // ReportSolverArray report = (ReportSolverArray) solvers.get(index).report;
            double[] v = ((ReportSolverArray) solvers.get(index).report).getMeans();
            txt.append("\n" + MyString.setSize(solvers.get(index).getSolverName(), ReportSolver.FIELD_SIZE));
            for (int i = 0; i < v.length; i++) {
                txt.append(" " + MyString.alignRight(v[i], ReportSolver.FIELD_SIZE));
            }
        }
        return txt.toString() + "\n";
    }

    public static String getReportSTD(ArrayList<EAsolver> solvers) {
        StringBuffer txt = new StringBuffer();
        txt.append(solvers.get(0).report.getStatisticsHeader("STD"));
        for (int index = 0; index < solvers.size(); index++) {
            ReportSolverArray report = (ReportSolverArray) solvers.get(index).report;
            double[] v = ((ReportSolverArray) solvers.get(index).report).getSTDs();
            txt.append("\n" + MyString.setSize(solvers.get(index).getSolverName(), ReportSolver.FIELD_SIZE));
            for (int i = 0; i < v.length; i++) {
                txt.append(" " + MyString.alignRight(v[i], ReportSolver.FIELD_SIZE));
            }
        }
        return txt.toString() + "\n";
    }

    public static String getReportData(ArrayList<EAsolver> solvers) {

        StringBuffer txt = new StringBuffer();
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.stats;
        //for all statistics
        for (int indexStat = 0; indexStat < stats.size(); indexStat++) {
            txt.append(MyString.LINE + "\n");
            txt.append(MyString.alignCenter(stats.get(indexStat).toString().toUpperCase(), MyString.LINE.length()) + "\n");
            txt.append(MyString.LINE + "\n");
            txt.append(getReportDataVertical(solvers, indexStat) + "\n");
            txt.append(getReportData(solvers, indexStat) + "\n");
            txt.append(stats.get(indexStat) + " : Compare Means  using Student's t-test\n");
            txt.append(Tstudent.getReportCompareMeans(solvers, stats.get(indexStat)) + "\n");
        }//all stats    
        return txt.toString();
    }

    public static String getReportRanks(ArrayList<EAsolver> solvers, double confidence) {
        StringBuffer txt = new StringBuffer();
        int[][] ranks = Tstudent.getStatisticsRanks(solvers, confidence);
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.stats;

        txt.append(MyString.alignCenter("Solver ", ReportSolver.FIELD_SIZE));
        for (AbstractStatistics stat : stats) {
            txt.append(MyString.alignCenter(stat.getSimpleName(), ReportSolver.FIELD_SIZE));
        }
        for (int s = 0; s < solvers.size(); s++) {
            txt.append("\n" + MyString.alignCenter(solvers.get(s).getSolverName(), ReportSolver.FIELD_SIZE));
            for (int i = 0; i < stats.size(); i++) {
                txt.append(MyString.alignCenter(ranks[i][s] + "", ReportSolver.FIELD_SIZE));
            }

        }//all stats    
        return txt.toString();
    }

    public static String getReportData(ArrayList<EAsolver> solvers, int indexStat) {
        StringBuffer txt = new StringBuffer();
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.stats;
        //::::::: HEADER :::::::::::
        txt.append(MyString.setSize(stats.get(indexStat).toString(), ReportSolver.FIELD_SIZE));
        txt.append(" " + MyString.alignRight("Mean", ReportSolver.FIELD_SIZE));
        txt.append(" " + MyString.alignRight("STD", ReportSolver.FIELD_SIZE));
        int max = getMaxRuns(solvers); // maximum number of runs
        for (int j = 0; j < max; j++) {
            txt.append(" " + MyString.alignRight("Solver " + j, ReportSolver.FIELD_SIZE));
        }
        txt.append("\n");
        //::::::::::::::::::::::: DATA ::::::::::::::::::::::::::
        for (int j = 0; j < solvers.size(); j++) {
            //name of simulation
            txt.append(MyString.alignRight(solvers.get(j).getSolverName(), ReportSolver.FIELD_SIZE));
            //data
            double[] v = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(indexStat);
            txt.append(" " + MyString.alignRight(MyStatistics.mean(v), ReportSolver.FIELD_SIZE));
            txt.append(" " + MyString.alignRight(MyStatistics.std(v), ReportSolver.FIELD_SIZE));
            for (int s = 0; s < v.length; s++) {
                txt.append(" " + MyString.alignRight(v[s], ReportSolver.FIELD_SIZE));
            }
            txt.append("\n");
        }
        txt.append("\n");
        return txt.toString();
    }

    public static void saveStatisticsCSV(ArrayList<EAsolver> solvers, String fileName) {
        String SEPARATOR = ";";
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.stats;
        //for all statistics
        for (int indexStat = 0; indexStat < stats.size(); indexStat++) {
            try {
                PrintWriter out = new PrintWriter(fileName + "_" + stats.get(indexStat).toString() + ".csv");
                StringBuilder txt = new StringBuilder("sep=" + SEPARATOR + "\n"); // excel 2010
                //::::::: HEADER :::::::::::
                txt.append(MyString.setSize(stats.get(indexStat).toString(), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " "); // stat
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(MyString.alignRight(solvers.get(j).getSolverName(), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: MEAN :::::::
                txt.append("\n" + MyString.alignRight("Mean", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(MyString.alignRight(
                            ((ReportSolverArray) solvers.get(j).report).getMean(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: STD :::::::
                txt.append("\n" + MyString.alignRight("Std", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(MyString.alignRight(
                            ((ReportSolverArray) solvers.get(j).report).getSTD(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: MAX :::::::
                txt.append("\n" + MyString.alignRight("Minimum", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(MyString.alignRight(
                            ((ReportSolverArray) solvers.get(j).report).getMin(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: Median :::::::
                txt.append("\n" + MyString.alignRight("Median", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(MyString.alignRight(
                            ((ReportSolverArray) solvers.get(j).report).getMedian(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: STD :::::::
                txt.append("\n" + MyString.alignRight("Maximum", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(MyString.alignRight(
                            ((ReportSolverArray) solvers.get(j).report).getMax(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                txt.append("\n");
                //:::::::::::::::::: GET DATA :::::::::::::::::::::::
                double[][] data = new double[solvers.size()][];
                for (int j = 0; j < solvers.size(); j++) {
                    data[j] = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(indexStat);
                }
                //::::::::::::::::::::::: DATA ::::::::::::::::::::::::::
                int max = getMaxRuns(solvers); // maximum number of runs
                for (int numSolver = 0; numSolver < max; numSolver++) {
                    txt.append("\n" + MyString.alignRight("Solver " + numSolver, ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                    for (int numExperiment = 0; numExperiment < solvers.size(); numExperiment++) {
                        if (numSolver < data[numExperiment].length) { // data avaiable
                            txt.append(MyString.alignRight(data[numExperiment][numSolver], ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                        } else {
                            txt.append(MyString.alignRight("", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                        }
                    }
                }
                //write string to file
                out.println(MyString.toFileString(txt.toString()));

                out.println(MyString.toFileString(MyString.LINE));
                out.println(MyString.toFileString(MyString.toComment(solvers.get(0).problem.getInformation())));
                out.println(MyString.toFileString(MyString.LINE));

                //write copyright
                out.print(MyString.toFileString(MyString.toComment(MyString.getCopyright())));
                //close file
                out.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReportSimulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//all stats    
    }

    public static void saveStatisticsCSV(ArrayList<EAsolver> solvers, String fileName, Locale local, String SEPARATOR) {
        String path = MyFile.getPath(fileName);
        String name = MyFile.getFileNameOnly(fileName); // remove extension
        fileName = path + name + "_" + local.getCountry() + "_";
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.stats;
        //for all statistics
        for (int indexStat = 0; indexStat < stats.size(); indexStat++) {
            try {
                PrintWriter out = new PrintWriter(fileName + stats.get(indexStat).toString() + ".csv");
                StringBuilder txt = new StringBuilder("sep=" + SEPARATOR + "\n"); // excel 2010
                //::::::: HEADER :::::::::::
                txt.append(MyString.setSize(stats.get(indexStat).toString(), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " "); // stat
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(MyString.alignRight(solvers.get(j).getSolverName(), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: MEAN :::::::
                txt.append("\n" + MyString.alignRight("Mean", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(String.format(local, "%f",
                            ((ReportSolverArray) solvers.get(j).report).getMean(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: STD :::::::
                txt.append("\n" + MyString.alignRight("Std", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(String.format(local, "%f",
                            ((ReportSolverArray) solvers.get(j).report).getSTD(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: MAX :::::::
                txt.append("\n" + MyString.alignRight("Minimum", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(String.format(local, "%f",
                            ((ReportSolverArray) solvers.get(j).report).getMin(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: Median :::::::
                txt.append("\n" + MyString.alignRight("Median", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(String.format(local, "%f",
                            ((ReportSolverArray) solvers.get(j).report).getMedian(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                //::::: STATISTICS ::::::: STD :::::::
                txt.append("\n" + MyString.alignRight("Maximum", ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                for (int j = 0; j < solvers.size(); j++) { // name of solvers
                    txt.append(String.format(local, "%f",
                            ((ReportSolverArray) solvers.get(j).report).getMax(indexStat), ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                }
                txt.append("\n");
                //:::::::::::::::::: GET DATA :::::::::::::::::::::::
                double[][] data = new double[solvers.size()][];
                for (int j = 0; j < solvers.size(); j++) {
                    data[j] = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(indexStat);
                }
                //::::::::::::::::::::::: DATA ::::::::::::::::::::::::::
                int max = getMaxRuns(solvers); // maximum number of runs
                for (int numSolver = 0; numSolver < max; numSolver++) {
                    txt.append("\n" + MyString.alignRight("Solver " + numSolver, ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                    for (int numExperiment = 0; numExperiment < solvers.size(); numExperiment++) {
                        if (numSolver < data[numExperiment].length) { // data avaiable
                            txt.append(String.format(local, "%f",
                                    data[numExperiment][numSolver], ReportSolver.FIELD_SIZE) + " " + SEPARATOR + " ");
                        } else {
                            txt.append(" " + SEPARATOR + " ");
                        }
                    }
                }
                //write string to file
                out.println(MyString.toFileString(txt.toString()));

                out.println(MyString.toFileString(MyString.LINE));
                out.println(MyString.toFileString(MyString.toComment(solvers.get(0).problem.getInformation())));
                out.println(MyString.toFileString(MyString.LINE));
                //write copyright

                out.print(MyString.toFileString(MyString.toComment(MyString.getCopyright())));
                //close file
                out.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReportSimulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//all stats    
    }

    public static String getReportDataVertical(ArrayList<EAsolver> solvers, int indexStat) {
        //-----------------------------------------------------------------------
        //evolution not executed
        for (int j = 0; j < solvers.size(); j++) { // name of solvers
            if (solvers.get(j).report.evolution.isEmpty()) {
                return solvers.get(j).getSolverName() + " not Evolved!";
            }
        }
        //-----------------------------------------------------------------------
        StringBuffer txt = new StringBuffer();
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.stats;
        //::::::: HEADER :::::::::::
        txt.append(MyString.setSize(stats.get(indexStat).toString(), ReportSolver.FIELD_SIZE) + " "); // stat
        for (int j = 0; j < solvers.size(); j++) { // name of solvers
            txt.append(MyString.alignRight(solvers.get(j).getSolverName(), ReportSolver.FIELD_SIZE) + " ");
        }
        //::::: STATISTICS ::::::: MEAN :::::::
        txt.append("\n" + MyString.alignRight("Mean", ReportSolver.FIELD_SIZE) + " ");
        for (int j = 0; j < solvers.size(); j++) { // name of solvers
            txt.append(MyString.alignRight(
                    ((ReportSolverArray) solvers.get(j).report).getMean(indexStat), ReportSolver.FIELD_SIZE) + " ");
        }
        //::::: STATISTICS ::::::: STD :::::::
        txt.append("\n" + MyString.alignRight("Std", ReportSolver.FIELD_SIZE) + " ");
        for (int j = 0; j < solvers.size(); j++) { // name of solvers
            txt.append(MyString.alignRight(
                    ((ReportSolverArray) solvers.get(j).report).getSTD(indexStat), ReportSolver.FIELD_SIZE) + " ");
        }
        //::::: STATISTICS ::::::: MAX :::::::
        txt.append("\n" + MyString.alignRight("Minimum", ReportSolver.FIELD_SIZE) + " ");
        for (int j = 0; j < solvers.size(); j++) { // name of solvers
            txt.append(MyString.alignRight(
                    ((ReportSolverArray) solvers.get(j).report).getMin(indexStat), ReportSolver.FIELD_SIZE) + " ");
        }
        //::::: STATISTICS ::::::: Median :::::::
        txt.append("\n" + MyString.alignRight("Median", ReportSolver.FIELD_SIZE) + " ");
        for (int j = 0; j < solvers.size(); j++) { // name of solvers
            txt.append(MyString.alignRight(
                    ((ReportSolverArray) solvers.get(j).report).getMedian(indexStat), ReportSolver.FIELD_SIZE) + " ");
        }
        //::::: STATISTICS ::::::: STD :::::::
        txt.append("\n" + MyString.alignRight("Maximum", ReportSolver.FIELD_SIZE) + " ");
        for (int j = 0; j < solvers.size(); j++) { // name of solvers
            txt.append(MyString.alignRight(
                    ((ReportSolverArray) solvers.get(j).report).getMax(indexStat), ReportSolver.FIELD_SIZE) + " ");
        }
        txt.append("\n");
        //:::::::::::::::::: GET DATA :::::::::::::::::::::::
        double[][] data = new double[solvers.size()][];
        for (int j = 0; j < solvers.size(); j++) {
            data[j] = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(indexStat);
        }
        //::::::::::::::::::::::: DATA ::::::::::::::::::::::::::
        int max = getMaxRuns(solvers); // maximum number of runs
        for (int numSolver = 0; numSolver < max; numSolver++) {
            txt.append("\n" + MyString.alignRight("Solver " + numSolver, ReportSolver.FIELD_SIZE) + " ");
            for (int numExperiment = 0; numExperiment < solvers.size(); numExperiment++) {
                if (numSolver < data[numExperiment].length) { // data avaiable
                    txt.append(MyString.alignRight(data[numExperiment][numSolver], ReportSolver.FIELD_SIZE) + " ");
                } else {
                    txt.append(MyString.alignRight("", ReportSolver.FIELD_SIZE) + " "); // data not avaiable
                }
            }
        }

        txt.append("\n");
        return txt.toString();
    }

    public static String getSolversInfo(ArrayList<EAsolver> solvers) {
        StringBuffer txt = new StringBuffer();
        for (EAsolver solver : solvers) {
            txt.append(MyString.LINE + "\n");
            txt.append(solver.report.getFileName() + "\n");
            txt.append(MyString.LINE + "\n");
            txt.append(solver.toString() + "\n");
        }
        return txt.toString();
    }

    public static int getMaxRuns(ArrayList<EAsolver> solvers) {
        int max = 0;
        for (EAsolver solver : solvers) {
            max = Math.max(max, solver.numberOfRun);
        }
        return max;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603270609L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
