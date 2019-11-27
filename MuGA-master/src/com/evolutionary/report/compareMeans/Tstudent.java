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

package com.evolutionary.report.compareMeans;

import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.ReportSolverArray;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.utils.MyStatistics;
import com.utils.MyString;
import com.utils.Ranking;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created on 13/abr/2016, 22:01:34
 *
 * @author zulu - computer
 */
public class Tstudent {

    public static double[][] getValuesMeans(ArrayList<EAsolver> solvers, AbstractStatistics stat) {
        double[][] result = new double[solvers.size()][];
        for (int j = 0; j < solvers.size(); j++) {
            result[j] = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(stat);
        }
        return result;
    }

    public static String getReportCompareMeans(ArrayList<EAsolver> solvers, AbstractStatistics stat) {
        stat.setSolver(solvers.get(0));
        
        double[][] result = getValuesMeans(solvers, stat);
        char[][] compare95 = MyStatistics.compareMeansTtest(result, 0.05, stat.higherIsBetter);
        int[] score95 = MyStatistics.sumMeanComparations(compare95);
        int[] rank95 = Ranking.rank(score95);

        char[][] compare99 = MyStatistics.compareMeansTtest(result, 0.01, stat.higherIsBetter);
        int[] score99 = MyStatistics.sumMeanComparations(compare99);
        int[] rank99 = Ranking.rank(score99);
        double[][] p_value = MyStatistics.p_values(result);

        //:::::::::::: HEADER :::::::::::::::::
        StringBuffer txt = new StringBuffer();
        txt.append(MyString.alignCenter("Confidence 95%", ReportSolver.FIELD_SIZE));
        for (int i = 0; i < solvers.size(); i++) {
            txt.append(" " + MyString.alignCenter(i + "", 4));
        }
        txt.append(" " + MyString.alignCenter("Score", 5));
        txt.append(" " + MyString.alignCenter("Rank", 5));

        txt.append(" " + MyString.alignCenter("Confidence 99%", ReportSolver.FIELD_SIZE));
        for (int i = 0; i < solvers.size(); i++) {
            txt.append(" " + MyString.alignCenter(i + "", 4));
        }
        txt.append(" " + MyString.alignCenter("Score", 5));
        txt.append(" " + MyString.alignCenter("Rank", 5));

        txt.append(" " + MyString.alignCenter("p-Values", ReportSolver.FIELD_SIZE));
        for (int i = 0; i < solvers.size(); i++) {
            txt.append(" " + MyString.alignCenter(i + "", 6));
        }
        txt.append("\n");
        //::::::::::::::::::::::::: DATA ::::::::::::::::::::::::::::::::::::::
        for (int i = 0; i < solvers.size(); i++) {
            txt.append(MyString.alignRight(solvers.get(i).getSolverName(), ReportSolver.FIELD_SIZE));
            for (int j = 0; j < compare95.length; j++) {
                txt.append(" " + MyString.alignCenter(compare95[i][j] + "", 4));
            }
            txt.append(" " + MyString.alignCenter(score95[i] + "", 5));
            txt.append(" " + MyString.alignCenter(rank95[i] + "", 5));
            txt.append(" " + MyString.alignRight(solvers.get(i).getSolverName(), ReportSolver.FIELD_SIZE));
            for (int j = 0; j < compare99.length; j++) {
                txt.append(" " + MyString.alignCenter(compare99[i][j] + "", 4));
            }
            txt.append(" " + MyString.alignCenter(score99[i] + "", 5));
            txt.append(" " + MyString.alignCenter(rank99[i] + "", 5));
            txt.append(" " + MyString.alignRight(solvers.get(i).getSolverName(), ReportSolver.FIELD_SIZE));
            for (int j = 0; j < p_value.length; j++) {
                txt.append(" " + MyString.alignRight(String.format(Locale.US, "%4.3f", p_value[i][j]), 6));

            }
            txt.append("\n");
        }
        txt.append("\n");
        return txt.toString();
    }

    public static int[] getStatisticRank(ArrayList<EAsolver> solvers, AbstractStatistics stat, double confidence) {
        double[][] result = new double[solvers.size()][];
        for (int j = 0; j < solvers.size(); j++) {
            result[j] = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(stat);
        }
        char[][] compare95 = MyStatistics.compareMeansTtest(result, 0.05, stat.higherIsBetter);
        int[] score95 = MyStatistics.sumMeanComparations(compare95);
        return Ranking.rank(score95);
    }

    public static int[][] getStatisticsRanks(ArrayList<EAsolver> solvers, double confidence) {
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.getStatistics();
        int[][] ranks = new int[stats.size()][];
        for (int i = 0; i < stats.size(); i++) {
            ranks[i] = getStatisticRank(solvers, stats.get(i), confidence);
        }
        return ranks;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201604132201L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
