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
package com.evolutionary.report.html;

import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.report.ReportSolverArray;
import com.evolutionary.report.StatisticElement;
import com.evolutionary.report.compareMeans.Tstudent;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solverUtils.EAExperiment;
import com.evolutionary.solverUtils.EAsolverArray;
import com.evolutionary.solverUtils.FileSolver;
import com.utils.MyFile;
import com.utils.MyStatistics;
import com.utils.Ranking;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;

/**
 * Created on 28/jan/2019, 18:45:29
 *
 * @author zulu - computer
 */
public class WwwExperimentReport {

    public static void main(String[] args) throws Exception {
        EAExperiment ex = new EAExperiment();
//        EAsolver s = new GA();
//        s.numberOfRun = 12;
//
//        s.recombination = new NPointCrossover();
//        s.recombination.setParameters("0.75");
//        EAsolverArray mp = new EAsolverArray(s);
//        mp.setSolverName("MP_X2");
//        mp.solve(true);
//        mp.report.saveObject();
//        ex.solvers.add(mp);
//
//        s.recombination = new NPointCrossover();
//        s.recombination.setParameters("0.75 4");
//        s.numberOfRun = 12;
//        mp = new EAsolverArray(s);
//        mp.setSolverName("MP_X4");
//        mp.solve(true);
//        mp.report.saveObject();
//        ex.solvers.add(mp);
//
//        s.recombination = new Uniform();
//        s.numberOfRun = 18;
//        mp = new EAsolverArray(s);
//        mp.setSolverName("MP_U");
//        mp.solve(true);
//        mp.report.saveObject();
//        ex.solvers.add(mp);
//
//        s.parents = new SimplePopulation();
//        s.numberOfRun = 42;
//        mp = new EAsolverArray(s);
//        mp.setSolverName("SGA");
//        mp.solve(true);
//        mp.report.saveObject();
//        ex.solvers.add(mp);

//
//        s.parents = new SimplePopulation();
//        mp = new EAsolverArray(s);
//        mp.setSolverName("SP_U");
//        mp.solve(true);
//        mp.report.saveObject();
//        ex.solvers.add(mp);
//
//        s.recombination = new NPointCrossover();
//        s.recombination.setParameters("0.75");
//        s.parents = new SimplePopulation();
//        mp = new EAsolverArray(s);
//        mp.setSolverName("SP_x");
//        mp.solve(true);
//        mp.report.saveObject();
//        ex.solvers.add(mp);
//        ex.solvers.add(EAsolver.load("MP_U.muga"));
//        ex.solvers.add(EAsolver.load("SP_U.muga"));
//        ex.solvers.add(EAsolver.load("MP_x.muga"));
//        ex.solvers.add(EAsolver.load("SP_x.muga"));
//         ex = EAExperiment.loadObjectExperiment("AdaptiveCeiling_onesMax.muga");
        ex.loadObjectExperiment("MP_X2.muga");
        ex.loadObjectExperiment("MP_X4.muga");
        System.out.println(ex.solvers.size());

        System.out.println(ex.getFinalReport());

//
//        sp1.report.updateStatisticsTemplates(templateStats);
        WwwExperimentReport.save(ex, "thesys/ceiling/ac0-3.muga", new JFrame());
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static void save(EAExperiment experiment, String fileName, Component window) {
        try {
            new WwwExperimentReport(experiment).save(fileName, window);
        } catch (Exception ex) {
            Logger.getLogger(WwwExperimentReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static String getIndexFile(EAExperiment report) {
//        return "index.html";
//    }
//
//    public static String getHomeFile(EAExperiment experiment) {
//        return MyFile.getFileNameOnly(experiment.getFileName()) + ".html";
//    }
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    static String indexHorizontalTemplate = MyFile.readResource("/com/evolutionary/report/html/horizontalIndex.html");
    static String pageTemplate = MyFile.readResource("/com/evolutionary/report/html/template.html");
    static String menuHorizontalTemplate = MyFile.readResource("/com/evolutionary/report/html/horizontalMenu.html");
    static String menuVerticalTemplate = MyFile.readResource("/com/evolutionary/report/html/menu.html");
    static String indexVerticalTemplate = MyFile.readResource("/com/evolutionary/report/html/index.html");

    private static String getHome(EAExperiment experiment, String template, String frameDestination) throws Exception {
        String experienceName = MyFile.getFileNameOnly(experiment.getFileName()); // name only
        String path = MyFile.getPath(experiment.getFileName()); // path with /
        StringBuilder html = new StringBuilder();

        ArrayList<Map<String, String>> maps = new ArrayList<>();
        for (EAsolver solver : experiment.solvers) {
            maps.add(FileSolver.toMap(solver));
        }
        Set<String> keys = maps.get(0).keySet();

        html.append("\n<table>\n");

        ArrayList<String> listElem = new ArrayList<>(keys);
        for (int index = 1; index < listElem.size(); index++) {
            html.append("\n<tr> <td> " + listElem.get(index) + "</td>\n<td> <table>");
            HashSet<String> elems = new HashSet<>();
            for (Map<String, String> map : maps) {
                elems.add(map.get(listElem.get(index)));
            }

            for (String elem : elems) {
                if (elems.size() > 1) {
                    html.append("\n\t<tr><td bgcolor=\"#80ff80\">" + elem + "</td></tr>");
                } else {
                    html.append("\n\t<tr><td >" + elem + "</pre></td></tr>");
                }
            }
            html.append("\n </table></td></tr>");
        }

        html.append("\n<tr> <td> Statistics </td>\n<td> <table>");
        ArrayList<AbstractStatistics> stats = experiment.getCommonStats();
        for (AbstractStatistics stat : stats) {
            html.append("\n\t<tr><td><ul><li><a href=\"" + experienceName + "_" + stat.getSimpleName() + ".html\"> "
                    + stat.getSimpleName() + "</a></td></tr>");
        }
        html.append("\n </table></td></tr>");
        html.append("\n</table>");

        String fileName = experienceName + "." + FileSolver.FILE_EXTENSION_MUGA;
        String config = template.replace("_TITLE_", HTMLutils.getPageTitle("Configuration of " + experienceName, fileName));

        config = config.replace("_DATA_", html.toString());
        MyFile.saveToFile(config, path + experienceName + "_config.html");

        experiment.saveObjectExperiment(path + fileName);

        //::::::::::::::::  population.html :::::::::::::::::::::::::::                  
        Population hall = new MultiPopulation();
        for (EAsolver solver : experiment.solvers) {
            if (hall.getPopulationSize() < EAsolver.MAX_HALL_OF_FAME * 10) {
                hall.addAll(solver.hallOfFame);
            }
        }
        List<Individual> lst = hall.getGenomes();
        Collections.sort(lst);

        StringBuilder menuOfSolver = new StringBuilder();
        menuOfSolver.append("\n<tr><td><a href=\"" + experienceName + "_config.html"
                + "\" target=\"" + frameDestination + "\"> Configuration </a></td></tr>\n");

        String templatePop = HTMLutils.pageTemplate.replace("_COPYRIGHT_ ", HTMLutils.getCopyright());
        menuOfSolver.append(WWWSolverSaveReport.savePopulation(path, experienceName, hall, "Hall of Fame", templatePop, frameDestination));

        return menuOfSolver.toString();
    }

    private static String getStatistics(EAExperiment experiment, String template, String frameDestination) {

        String experienceName = MyFile.getFileNameOnly(experiment.getFileName()); // name only
        String path = MyFile.getPath(experiment.getFileName()); // path with /
        StringBuilder html = new StringBuilder();
        StringBuilder csv = new StringBuilder();
        ArrayList<StatisticElement[]> solversResult = new ArrayList<>();
        for (EAsolver s : experiment.solvers) {
            solversResult.add(((ReportSolverArray) s.report).getLastValuesStatistcs());
        }
        ArrayList<AbstractStatistics> stats = experiment.getCommonStats();
        csv.append("Statistics of " + experienceName + "\n");
        html.append("<table>");
        html.append("<tr><td></td>");
        csv.append(" ; ");
        //stats.size()-1 REMOVE FunctionCalls  statistics from display
        for (int index = 0; index < stats.size() - 1; index++) {
            html.append("<th colspan=\"2\" border = \"2px\">"
                    + "<a href=\"" + experienceName + "_" + stats.get(index).getSimpleName() + ".html"
                    + "\" target=\"" + frameDestination + "\">" + stats.get(index).getSimpleName() + " </a>"
                    + "</th>\n");
            csv.append(stats.get(index).getSimpleName() + " ; ;");
        }

        html.append("<tr><td></td>"); // empty cell
        csv.append("\nSolver;");
        //stats.size()-1 REMOVE FunctionCalls  statistics from display
        for (int index = 0; index < stats.size() - 1; index++) {
            html.append("<th>" + StatisticElement.getMeanLabel() + "</th>");
            html.append("<th>" + StatisticElement.getStdLabel() + "</th>");
            csv.append(StatisticElement.getMeanLabel() + ";");
            csv.append(StatisticElement.getStdLabel() + ";");
        }

        html.append("</tr> <tr>");
        csv.append("\n");

        for (int is = 0; is < experiment.solvers.size(); is++) {
            EAsolver s = experiment.solvers.get(is);
            html.append("<tr><th>"
                    + "<a href=\"" + s.getSolverName() + "/index.html"
                    + "\" target=\"" + frameDestination + "\">" + s.getSolverName() + " </a>"
                    + "</th>");

            csv.append(experiment.solvers.get(is).getSolverName() + "; ");
            StatisticElement[] data = solversResult.get(is);
            for (int i = 0; i < data.length - 1; i++) {
                html.append("<td><pre>" + HTMLutils.getNumber(data[i].getMean(), Locale.ENGLISH) + "</pre></td>");
                html.append("<td><pre>" + HTMLutils.getNumber(data[i].getStd(), Locale.ENGLISH) + "</pre></td>");
                csv.append(String.format("%f", data[i].getMean()) + HTMLutils.CSV_COMMA);
                csv.append(String.format("%f", data[i].getStd()) + HTMLutils.CSV_COMMA);
            }
            html.append("</tr>");
            csv.append("\n");
        }
        html.append("</table>");

        MyFile.saveToFile(csv.toString(), path + experienceName + ".csv");
        MyFile.saveToFile(experiment.getFinalReport()
                + "\n\n" + HTMLutils.getCopyright(), path + experienceName + ".txt");

        String statsHtml = template.replace("_TITLE_", HTMLutils.getPageTitle("Statistics of " + experienceName,
                experienceName + ".csv", experienceName + ".txt"));

        statsHtml = statsHtml.replace("_DATA_", html.toString());

        MyFile.saveToFile(statsHtml.toString(), path + experienceName + ".html");

        //::::::::::::::::::::::::::::::::::::::: INDIVIDUAL STATS ::::::::::::::
        //::::::::::::::::::::::::::::::::::::::: INDIVIDUAL STATS ::::::::::::::
        //::::::::::::::::::::::::::::::::::::::: INDIVIDUAL STATS ::::::::::::::
        //::::::::::::::::::::::::::::::::::::::: INDIVIDUAL STATS ::::::::::::::
        for (int index = 0; index < stats.size(); index++) {
            StringBuilder csvStat = new StringBuilder();
            AbstractStatistics stat = stats.get(index);
            csvStat.append(stat.getTitle() + " - " + experienceName + "\n");
            csvStat.append(";");
            for (String title : StatisticElement.header) {
                csvStat.append(title + " ;");
            }
            for (EAsolver s : experiment.solvers) {
                csvStat.append("\n" + s.getSolverName() + ";");
                for (Double value : ((ReportSolverArray) s.report).getLastValuesStatistcs(stat)) {
                    csvStat.append(String.format("%f", value) + HTMLutils.CSV_COMMA);
                }

            }
            //::::::::::::::::::::::::::::::::::::::: RANK ::::::::::::::
            double[][] values = Tstudent.getValuesMeans(experiment.solvers, stat);
            char[][] compare95 = MyStatistics.compareMeansTtest(values, 0.05, stat.higherIsBetter);
            int[] score95 = MyStatistics.sumMeanComparations(compare95);
            int[] rank95 = Ranking.rank(score95);
            csvStat.append("\n\nComparison of means - confidence 95% \n");
            csvStat.append("\n ; ");
            for (EAsolver s : experiment.solvers) {
                csvStat.append(s.getSolverName() + "; ");
            }
            csvStat.append("Score ; Rank\n");
            for (int i = 0; i < experiment.solvers.size(); i++) {
                csvStat.append(experiment.solvers.get(i).getSolverName() + "; ");
                for (char ch : compare95[i]) {
                    csvStat.append(ch + "; ");
                }
                csvStat.append(score95[i] + ";" + rank95[i] + "\n");
            }
            //::::::::::::::::::::::::::::::::::::::: DATA ::::::::::::::
//            csvStat.append("\nDATA");
//            for (EAsolver s : experiment.solvers) {
//                EAsolverArray sarray = (EAsolverArray) s;
//                csvStat.append("\n" + s.getSolverName() + ";");
//                for (EAsolver solver : sarray.arrayOfSolvers) {
//                    csvStat.append(String.format("%f", solver.report.getLastValue(stat)) + HTMLutils.CSV_COMMA);
//                }
//            }
            csvStat.append("\nDATA");
//            for (EAsolver s : experiment.solvers) {
//                EAsolverArray sarray = (EAsolverArray) s;
//                csvStat.append("\n" + s.getSolverName() + ";");
//                for (EAsolver solver : sarray.arrayOfSolvers) {
//                    csvStat.append(String.format("%f", solver.report.getLastValue(stat)) + HTMLutils.CSV_COMMA);
//                }
//            }
            //::::::::::::: TITLE :::::::::::::::::::::::::
            csvStat.append("\n" + stat.getSimpleName());
            int maxSolvers = 0;
            for (EAsolver s : experiment.solvers) {
                EAsolverArray sarray = (EAsolverArray) s;
                csvStat.append(HTMLutils.CSV_COMMA + sarray.getSolverName());
                maxSolvers = Math.max(maxSolvers, sarray.arrayOfSolvers.length);
            }
            //::::::::::: DATA ::::::::::::
            for (int indexSolver = 0; indexSolver < maxSolvers; indexSolver++) {
                csvStat.append("\nSolver " + indexSolver);
                for (EAsolver s : experiment.solvers) {
                    EAsolverArray sarray = (EAsolverArray) s;
                    if (indexSolver < sarray.arrayOfSolvers.length) {
                        csvStat.append(HTMLutils.CSV_COMMA + String.format("%f", sarray.arrayOfSolvers[indexSolver].report.getLastValue(stat)));
                    } else {
                        csvStat.append(HTMLutils.CSV_COMMA);
                    }
                }
            }

            MyFile.saveToFile(csvStat.toString(), path + experienceName
                    + "_" + stat.getSimpleName() + ".csv");
        }

        return "\n<tr><td><a href=\"" + experienceName + ".html"
                + "\" target=\"" + frameDestination
                + "\"> Statistics </a></td></tr>\n";

    }

    private static String getResultValues(EAExperiment experiment, AbstractStatistics stat) {
        StringBuilder html = new StringBuilder();
        html.append("<table>\n");
        html.append("<tr><th></th>\n");
        for (String title : StatisticElement.header) {
            html.append("<th>" + title + "</th>");
        }

        for (EAsolver s : experiment.solvers) {
            html.append("\n<tr align=\"right\">\n");
            html.append("<th>" + s.getSolverName() + "</th>");
            for (Double value : ((ReportSolverArray) s.report).getLastValuesStatistcs(stat)) {
                html.append("<td ><pre>" + HTMLutils.getNumber(value, Locale.ENGLISH) + "</pre></td>");
            }
            html.append("\n</tr>\n");
        }
        html.append("</table>\n");
        return html.toString();
    }

    private static String getResultRanks(EAExperiment experiment, AbstractStatistics stat) {
        String colorGood = " bgcolor=\"#9999ff\"";
        String colorBad = " bgcolor=\"#ff9999\"";
        String colorNeut = " bgcolor=\"#ffff99\"";

        StringBuilder html = new StringBuilder();
        double[][] values = Tstudent.getValuesMeans(experiment.solvers, stat);
        char[][] compare95 = MyStatistics.compareMeansTtest(values, 0.05, stat.higherIsBetter);
        int[] score95 = MyStatistics.sumMeanComparations(compare95);
        int[] rank95 = Ranking.rank(score95);
        html.append("<h3> Comparison of means - confidence 95% </h3>");
        html.append("<table>\n");
        html.append("<tr>\n\t<th></th>\n");
        for (EAsolver s : experiment.solvers) {
            html.append("\t<th>" + s.getSolverName() + "</th>\n");
        }
        html.append("\t<th>Score</th>\n");
        html.append("\t<th>Rank</th>\n");
        for (int i = 0; i < experiment.solvers.size(); i++) {
            html.append("<tr>\n\t<th align=\"right\">" + experiment.solvers.get(i).getSolverName() + "</th>\n");

            for (char ch : compare95[i]) {
                html.append("<td align=\"center\" ");
                if (ch == '+') {
                    html.append(colorGood);
                } else if (ch == '-') {
                    html.append(colorBad);
                } else {
                    html.append(colorNeut);
                }
                html.append("> " + ch + "</td>\n");
            }

            html.append("<td align=\"center\">" + score95[i] + "</td>");
            html.append("<td align=\"center\">" + rank95[i] + "</td>");
        }

        html.append("</table>\n");
        return html.toString();
    }

    private static String saveEvolution(EAExperiment experiment, String template, String frameDestination) throws Exception {
        StringBuilder menuVerticalItens = new StringBuilder("<tr><td style=\"text-align: center\"><h3>Statistics</h3></td></tr>");
        String experienceName = MyFile.getFileNameOnly(experiment.getFileName()); // name only
        String path = MyFile.getPath(experiment.getFileName()); // path with /
        int width = WWWSolverSaveReport.IMG_WIDTH;
        int height = WWWSolverSaveReport.IMG_HEIGHT;
        int width2 = width / 2;
        int height2 = height / 2;

        ArrayList<ChartPanel> box = experiment.getBoxAndWhiskersCharts();
        ArrayList<ChartPanel> ranks = experiment.getRankCharts();
        ArrayList<ChartPanel> evolution = experiment.getEvolutionCharts();
        ArrayList<AbstractStatistics> stats = experiment.getCommonStats();

        for (int i = 0; i < stats.size(); i++) {
            String fileName = experienceName + "_" + stats.get(i).getSimpleName();
            StringBuilder statisticData = new StringBuilder();

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path + fileName + "_evol.jpg"));
            ChartUtilities.writeChartAsJPEG(out, evolution.get(i).getChart(), width, height);

            out = new BufferedOutputStream(new FileOutputStream(path + fileName + "_evol2.jpg"));
            ChartUtilities.writeChartAsJPEG(out, evolution.get(i).getChart(), width2, height2);

            out = new BufferedOutputStream(new FileOutputStream(path + fileName + "_box.jpg"));
            ChartUtilities.writeChartAsJPEG(out, box.get(i).getChart(), width, height);

            out = new BufferedOutputStream(new FileOutputStream(path + fileName + "_box2.jpg"));
            ChartUtilities.writeChartAsJPEG(out, box.get(i).getChart(), width2, height2);

            out = new BufferedOutputStream(new FileOutputStream(path + fileName + "_rank.jpg"));
            ChartUtilities.writeChartAsJPEG(out, ranks.get(i).getChart(), width, height);

            out = new BufferedOutputStream(new FileOutputStream(path + fileName + "_rank2.jpg"));
            ChartUtilities.writeChartAsJPEG(out, ranks.get(i).getChart(), width2, height2);
            //statisticData.append(" <font face=\"courier\" size=4>");

            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::: EVOLUTION ::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::                        
            statisticData.append("<h2> Evolution of " + stats.get(i).getSimpleName() + "</h2>");
            statisticData.append("<hr>\n<table>\n<tr valign=\"top\">\n");

            statisticData.append("<td><img src=\"" + fileName + "_evol.jpg\"></td>\n");

            statisticData.append("<td>\n<table><tr>");
            statisticData.append("<td><img src=\"" + fileName + "_box2.jpg\"></td>");
            statisticData.append("<td><img src=\"" + fileName + "_rank2.jpg\"></td> \n");
            statisticData.append("</tr>\n");
            statisticData.append("<tr><td colspan=\"2\"><plain>" + stats.get(i).getInformation() + "</plain></td></tr>\n");
            statisticData.append("</table></td></tr>\n");
            statisticData.append("</table>");
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::: STATISTICS ::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::                        
            statisticData.append("<h2> Statistics  of last generation of " + stats.get(i).getSimpleName() + "</h2>");
            statisticData.append("<hr>\n<table>\n<tr valign=\"top\">\n");
            statisticData.append("<td><img src=\"" + fileName + "_box.jpg\"></td>\n");
            statisticData.append("<td>\n<table><tr>");
            statisticData.append("<td><img src=\"" + fileName + "_evol2.jpg\"></td>\n");
            statisticData.append("<td><img src=\"" + fileName + "_rank2.jpg\"></td>\n");
            statisticData.append("</tr>\n");
            statisticData.append("<tr><td colspan=\"2\">" + getResultValues(experiment, stats.get(i)) + "</td></tr>\n");
            statisticData.append("</table></td></tr>\n");
            statisticData.append("</table>");
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::: RANKS ::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::                        
            statisticData.append("<h2> Ranks of " + stats.get(i).getSimpleName() + "</h2>");
            statisticData.append("<hr>\n<table>\n<tr valign=\"top\">\n");

            statisticData.append("<tr valign=\"top\">\n");
            statisticData.append("<td><img src=\"" + fileName + "_rank.jpg\"></td>\n");
            statisticData.append("<td>\n<table><tr>");
            statisticData.append("<td><img src=\"" + fileName + "_box2.jpg\"></td>\n");
            statisticData.append("<td><img src=\"" + fileName + "_evol2.jpg\"></td>\n");
            statisticData.append("</tr>\n");
            statisticData.append("<tr><td colspan=\"2\"><pre>" + getResultRanks(experiment, stats.get(i)) + "</pre></td></tr>\n");
            statisticData.append("</table></td></tr>\n");
            statisticData.append("\n");
            statisticData.append("\n");
            statisticData.append("</table>\n");
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::: DATA ::::::::::::::::::::::::::
            //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
            statisticData.append("<h2>" + stats.get(i).getSimpleName() + "Solvers  data </h2>");
            statisticData.append("<hr>\n<table>\n");
            statisticData.append("<tr align=\"center\" ><th >Solver</th>");
            int maxSolvers = 0;
            for (EAsolver s : experiment.solvers) {
                EAsolverArray sarray = (EAsolverArray) s;
                statisticData.append("<th >" + sarray.getSolverName() + "</th>");
                maxSolvers = Math.max(maxSolvers, sarray.arrayOfSolvers.length);
            }
            statisticData.append("</tr>");
            //::::::::::: DATA ::::::::::::
            for (int indexSolver = 0; indexSolver < maxSolvers; indexSolver++) {
                statisticData.append("\n<tr align=\"center\" ><td>Solver " + indexSolver + "</td>");
                for (EAsolver s : experiment.solvers) {
                    EAsolverArray sarray = (EAsolverArray) s;
                    if (indexSolver < sarray.arrayOfSolvers.length) {
                        statisticData.append("<td><pre>" + HTMLutils.getNumber(
                                sarray.arrayOfSolvers[indexSolver].report.getLastValue(stats.get(i)),
                                Locale.ENGLISH) + "</pre></td>");

                    } else {
                        statisticData.append("<td></td> ");
                    }
                }
                statisticData.append("</tr>");
            }

            statisticData.append("\n");
            statisticData.append("\n");
            statisticData.append("</table>\n");

            String data = template.replace("_TITLE_", HTMLutils.getPageTitle(stats.get(i).getTitle(), fileName + ".csv"));
            data = data.replace("_DATA_", statisticData.toString());

            menuVerticalItens.append("\n<tr><td><a href=\"" + fileName
                    + ".html\" target=\"" + frameDestination + "\"> " + stats.get(i).getSimpleName() + " </a></td></tr>\n");

            MyFile.saveToFile(data, path + fileName + ".html");

        }

        return menuVerticalItens.toString();

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static void saveExperimentIndex(EAExperiment experiment) throws Exception {

        String experienceName = MyFile.getFileNameOnly(experiment.getFileName()); // name only
        StringBuilder solversDefs = new StringBuilder("\n<table>");
        for (EAsolver solverArray : experiment.solvers) {
            EAsolver solver = ((EAsolverArray) solverArray).template;
            solversDefs.append("\n<tr>");
            solversDefs.append("\n     <td>" + solverArray.getSolverName() + "</td>");
            solversDefs.append("\n     <td>" + solver.getSimpleName() + "</td>");
            solversDefs.append("\n     <td>" + solver.parents.getSimpleName() + " " + solver.parents.getParameters() + "</td>");
            solversDefs.append("\n     <td>" + solver.problem.getSimpleName() + " " + solver.problem.getParameters() + "</td>");
            solversDefs.append("\n     <td>" + solver.selection.getSimpleName() + " " + solver.selection.getParameters() + "</td>");
            solversDefs.append("\n     <td>" + solver.recombination.getSimpleName() + " " + solver.recombination.getParameters() + "</td>");
            solversDefs.append("\n     <td>" + solver.mutation.getSimpleName() + " " + solver.mutation.getParameters() + "</td>");
            solversDefs.append("\n     <td>" + solver.replacement.getSimpleName() + " " + solver.replacement.getParameters() + "</td>");
            solversDefs.append("\n     <td>" + solver.rescaling.getSimpleName() + " " + solver.rescaling.getParameters() + "</td>");
            solversDefs.append("\n</tr>");
        }
        solversDefs.append("\n</table>");
        String menuEntry = "\n</ul><p><ul>\n<!-- ::::::::::    " + experienceName + " ::::::::::::::::::::  -->\n"
                + "<li><a class=\"active\" href=\"_FOLDER_index.html\" target=\"_blank\" >\n"
                + " <strong> " + experienceName + " </strong> </a> \n"
                + "description of " + experienceName + "\n"
                + solversDefs.toString()
                + "\n<ref>"
                + "<a href=\"_FOLDER_index.html\">\n"
                + " link to page </a></ref>\n"
                + "\n"
                + "\n</li>\n"
                + "<!-- ::::::::::::::::::::::::::::::::::::::::::::::  -->\n";
        addEntryInFile(new File(experiment.getFileName()), menuEntry, "", 8);

    }

    private static void addEntryInFile(File f, String menuEntry, String folder, int level) {
        if (level <= 0) {
            return;
        }
        String html = "";
        // search for muga.html
        String path = f.getParentFile().getAbsolutePath();
        String myFolder = path.substring(path.lastIndexOf(File.separatorChar) + 1);
        try {
            f = new File(path).getParentFile();
            f = new File(f.getCanonicalFile() + "/muga.html");
            if (f.exists()) { // file exists
                html = MyFile.readFile(f.getAbsolutePath());
            } else if (folder.isEmpty()) { // first level
                html = HTMLutils.menuSimulations;
            } else { // skip file
                addEntryInFile(f, menuEntry, myFolder + "/" + folder, level - 1);
                return;
            }
            //save file
            String startMenu = html.substring(0, html.indexOf("</ul>"));
            String endMenu = html.substring(html.indexOf("</ul>"));

            String entry = menuEntry.replaceAll("_FOLDER_", myFolder + "/" + folder);
            MyFile.saveToFile(startMenu + entry + endMenu, f.getAbsolutePath());
            addEntryInFile(f, menuEntry, myFolder + "/" + folder, level - 1);

        } catch (Exception e) {
            return;
        }

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    class SaveReportTask extends SwingWorker<Void, String> {

        @Override
        public Void doInBackground() {
            try {

                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::::::::::::::  B E G I N ::::::::::::::::::::::
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//                for (EAsolver solver : experiment.solvers) {
//                    solver.report.sortStatistics();
//                }
                publish("Initializing Report ");
                String path = MyFile.getPath(experiment.getFileName()); // path with /
                String experienceName = MyFile.getFileNameOnly(experiment.getFileName()); // name only
                //:::::::::::::::::::::::: B U I L D   P A G E     :::::::::::::

                String targetFrameHorizontal = "experimentMenu"; //target name of frame in links
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::  index.html  :::::::::::::::::::::::::::::::
                MyFile.saveToFile(HTMLutils.css, path + "style.css");
                String index = indexHorizontalTemplate.replaceAll("_HOME_", experienceName + "_index.html");
                index = index.replaceAll("_FRAME.NAME_", targetFrameHorizontal);
                MyFile.saveToFile(index, path + "index.html");
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::  menu.html  :::::::::::::::::::::::::::::::                
                //:::::::::::::::: S O L V E R S  :::::::::::::::::::::::::::::::   
                StringBuilder menuHorizontalItens = new StringBuilder();
                menuHorizontalItens.append("\n<li><a href=\"" + experienceName + "_index.html"
                        + "\" target=\"" + targetFrameHorizontal + "\"> " + experienceName + " </a></li>\n");

                for (EAsolver solver : experiment.solvers) {
                    menuHorizontalItens.append("\n<li><a href=\"" + solver.getSolverName() + "/index.html"
                            + "\" target=\"" + targetFrameHorizontal + "\"> " + solver.getSolverName() + " </a></li>\n");
                }
                String menu = menuHorizontalTemplate.replace("_MENU_", menuHorizontalItens.toString());

                MyFile.saveToFile(menu, path + "menu_horizontal.html");

                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::  experiment.html  ::::::::::::::::::::::::::::::: 
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                publish("save experiment page");
                index = indexVerticalTemplate.replace("_HOME.PAGE_", experienceName + "_config.html"); // homepage
                index = index.replaceAll("_TARGET.NAME_", experienceName);
                index = index.replaceAll("_SOLVER.NAME_", experienceName);
                MyFile.saveToFile(index, path + experienceName + "_index.html");

                String template = pageTemplate.replace("_COPYRIGHT_", HTMLutils.getCopyright());
                //::::::::::::::::  menuVertical.html  :::::::::::::::::::::::::::::::
                StringBuilder menuVerticalItens = new StringBuilder();
                publish("Saving configuration");
                menuVerticalItens.append(getHome(experiment, template, experienceName));

                publish("Saving statistics");
                menuVerticalItens.append(getStatistics(experiment, template, experienceName));

                publish("Saving evolution");
                menuVerticalItens.append(saveEvolution(experiment, template, experienceName));

                //::::::::::::::::  statistics.html  :::::::::::::::::::::::::::::::
                menuVerticalItens.append("<tr><td style=\"text-align: center\"><h3>Solvers</h3></td></tr>");
                for (int i = 0; i < experiment.solvers.size(); i++) {
                    publish("Saving " + (i + 1) + " of " + experiment.solvers.size() + " - " + experiment.solvers.get(i).getSolverName());
                    WWWSolverSaveReport.save(experiment.solvers.get(i).report,
                            experiment.solvers.get(i).report.path + "/" + experiment.solvers.get(i).getSolverName(),
                            null);
                    menuVerticalItens.append("\n<tr><td>"
                            + "<a href=\"" + experiment.solvers.get(i).getSolverName() + "/"
                            + "index.html"
                            + "\" target=\"" + experienceName + "\">"
                            + experiment.solvers.get(i).getSolverName() + " </a></td></tr>");
                }

                menu = menuVerticalTemplate.replace("_MENU_", "\n" + menuVerticalItens.toString());
                menu = menu.replace("_TITLE_", "<h3>" + experienceName + "</h3>");

                MyFile.saveToFile(menu, path + experienceName + "_menu.html");

                //save experiments Index
                saveExperimentIndex(experiment);
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::::::::::::::  E N D !:::::::::::::::::::::::::
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                File url = new File(path + "index.html");
                java.awt.Desktop.getDesktop().browse(url.toURI());

            } catch (Exception ex) {
                publish(ex.getMessage());
                ex.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dispose();
            }
            return null;
        }
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        @Override
        protected void process(List<String> chunks) {
            if (text != null) {
                for (String txt : chunks) {
                    text.setText(txt + "\n" + text.getText());
                }
                text.setCaretPosition(0);
                chunks.clear();
            } else {
                System.out.println(chunks.get(chunks.size() - 1));
            }
        }
        private JTextArea text;

        public SaveReportTask(JTextArea text) {
            this.text = text;
        }
    }

//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    JDialog progressDialog = null; //dialog to be closed by the task
    EAExperiment experiment;           // report to save
    //  Component window;

    private WwwExperimentReport(EAExperiment report) {
        this.experiment = report;
    }

    private void save(String fileName, Component window) throws Exception {
        if (window != null) {
            experiment.setFileName(fileName); // update path and solver name
            //        this.window = window;
            progressDialog = new JDialog();
            progressDialog.setModal(true);

            progressDialog.setTitle(experiment.getFileName());
            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());
            contentPane.setPreferredSize(new Dimension(300, 200));
            final JProgressBar bar = new JProgressBar(0, 100);
            bar.setIndeterminate(true);
            contentPane.add(bar, BorderLayout.NORTH);
            JTextArea txt = new JTextArea();
            txt.setBackground(Color.BLACK);
            txt.setForeground(Color.GREEN);
            txt.setEditable(false);
            contentPane.add(new JScrollPane(txt), BorderLayout.CENTER);
            progressDialog.setContentPane(contentPane);
            progressDialog.pack();

            progressDialog.setLocationRelativeTo(window);
            progressDialog.requestFocusInWindow(); // in front of all
            txt.setForeground(Color.GREEN);

            SaveReportTask task = new SaveReportTask(txt);
            task.execute();
            progressDialog.setVisible(true);
            progressDialog.setLocationRelativeTo(window);
            progressDialog.requestFocusInWindow(); // in front of all
            txt.setForeground(new Color(255, 255, 255));
            progressDialog.setLocation(progressDialog.getLocation().x + 200, progressDialog.getLocation().y + 200);
            task.get();

        } else {
            SaveReportTask task = new SaveReportTask(null);
            task.execute();
            task.get();
        }

    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201901281845L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
