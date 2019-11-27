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

import GUI.statistics.StatisticsBoxAndWhisker;
import GUI.statistics.StatisticsChartExperiment;
import GUI.statistics.StatisticsChartSolver;
import GUI.statistics.StatisticsRankChart;
import com.evolutionary.report.ReportSimulation;
import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.ReportSolverArray;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import static com.evolutionary.solverUtils.FileSolver.*;
import com.utils.MyFile;
import com.utils.MyString;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.jfree.chart.ChartPanel;

/**
 * Created on 27/mar/2016, 6:09:37
 *
 * @author zulu - computer
 */
public class EAExperiment implements Serializable {

    public static String DEFAULT_NAME = "simulation";
    public static String DEFAULT_RESULT = "_stats";
    public static String DEFAULT_FILES = "_setup";
    public ArrayList<EAsolver> solvers = new ArrayList<>(); // list of solvers
    String fileName = DEFAULT_NAME + "." + FileSolver.FILE_EXTENSION_CONFIG;

    public boolean isEvolutionCompletedFlag = false; // to show only once in the API onEvolutionComplete(EAsolver source)

    public EAExperiment getClone() {
        EAExperiment clone = new EAExperiment();
        clone.fileName = this.fileName;
        for (EAsolver solver : this.solvers) {
            clone.solvers.add(solver.getClone());
        }
        return clone;
    }

    /**
     * get the common statistics of all solvers
     *
     * @return
     */
    public ArrayList<AbstractStatistics> getCommonStats() {
        CopyOnWriteArrayList<AbstractStatistics> commonStats = new CopyOnWriteArrayList<>();
        //get stats of the first
        for (AbstractStatistics firstStat : solvers.get(0).report.stats) {
            commonStats.add(firstStat);
        }
        //for the rest of the solvers
        for (int i = 1; i < solvers.size(); i++) {
            ArrayList<AbstractStatistics> solverStats = solvers.get(i).report.stats;
            //for all stats
            for (AbstractStatistics stat : commonStats) {
                //verify if the solver is common
                if (!solverStats.contains(stat)) {
                    commonStats.remove(stat);
                }
            }

        }
        return new ArrayList<>(commonStats);
    }

    /**
     * normalize common statistics of the solvers
     */
    public void updateCommonStatisticsOfSolvers() {
        ArrayList<AbstractStatistics> common = getCommonStats();
        for (int i = 0; i < solvers.size(); i++) {
            solvers.get(i).report.updateStatisticsTemplates(common);
        }
    }

    /**
     * sets the file name to save simulation and update the path of solvers
     * reports
     *
     * @param file filename of simulation
     */
    public void setFileName(String file) {
        String path = MyFile.getPath(file);
        String name = MyFile.getFileNameOnly(file);

        if (name.isEmpty()) {
            name = DEFAULT_NAME;
        }

        fileName = path + name;

        for (int i = 0; i < solvers.size(); i++) {
            solvers.get(i).report.setPath(path + solvers.get(i).getSolverName() + "/");
        }
    }

    public String getFileName() {
        return MyFile.getFullFileName(fileName);
    }

    /**
     * loads a solver to the simulation
     *
     * @param simulationFileName file name of the solver
     */
    public void loadSolver(String simulationFileName) {
        try {
            ArrayList<EAsolver> fileSolvers = FileSimulation.loadSimulation(simulationFileName);
            if (fileSolvers.isEmpty()) { // no solvers to loadSolver
                return;
            } else {

                for (EAsolver solver : fileSolvers) {
                    System.out.println("Loading " + solver.report.getFileName() + " Evolution: " + solver.report.evolution.size());
                    solvers.add(solver);
                }
            }
            setFileName(MyFile.getPath(simulationFileName) + "Simulation");
        } catch (Exception ex) {
            Logger.getLogger(EAExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * run the simulation : restart all solvers
     *
     * @param verbose
     */
    public void restartEvolution(boolean verbose) {
        System.out.println("\nRestart Simulation  at " + MyString.toString(new Date()) + "\n");
        for (int i = 0; i < solvers.size(); i++) {
            System.out.println("Solving " + solvers.get(i).getSolverName());
            solvers.get(i).solve(verbose);
        }

        System.out.println("\nSimulation completed at " + MyString.toString(new Date()) + "\n");
    }

    /**
     * run the simulation : run the solvers with no evolution data
     *
     * @param verbose
     */
    public void solve(boolean verbose) {
        System.out.println("\nSimulation starts at " + MyString.toString(new Date()) + "\n");
        for (int i = 0; i < solvers.size(); i++) {
            if (solvers.get(i).report.evolution.isEmpty()) {
                System.out.println("Solving " + solvers.get(i).getSolverName());
                solvers.get(i).solve(verbose);
            }
        }

        System.out.println("\nSimulation completed at " + MyString.toString(new Date()) + "\n");
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * verify if all the solvers are executed i.e. all solvers are evolution
     * data.
     *
     * @return all solvers are executed
     */
    public boolean isEvolutionExecuted() {
        for (EAsolver solver : solvers) {
            if (solver.report.evolution.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Initialize the solvers to performs evolution clean evolution data
     */
    public void initializeEvolution() {
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        // All the solvers needs the same statistics        
        //normalize statistics getting stats to the solvers
        ReportSolver stats = new ReportSolver(); // to store unique statistics
        for (EAsolver solver : solvers) {
            //add the stats of the solver
            for (AbstractStatistics stat : solver.report.getStatistics()) {
                stats.addStatistic(stat); //uniqueness garanteed by the method                
            }
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        String path = MyFile.getPath(fileName);
        for (int i = 0; i < solvers.size(); i++) {
            solvers.get(i).report.setStatistics(stats.getStatistics()); // normalize stats
            solvers.get(i).report.setPath(path);
            solvers.get(i).InitializeEvolution(false);
        }
    }

    /**
     * gets the final report of the simulation
     *
     * @return txt of final report
     */
    public String getFinalReport() {
        StringBuilder txt = new StringBuilder();
        if (!isEvolutionExecuted()) {
            for (EAsolver solver : solvers) {
                txt.append(solver.getName() + " Generations " + solver.report.evolution.size() + "\n");
            }
            return txt.toString();
        }

        txt.append(ReportSimulation.getReportMeans(solvers) + "\n");
        txt.append(MyString.LINE + "\n");
        txt.append(ReportSimulation.getReportSTD(solvers) + "\n");
        txt.append(MyString.LINE + "\n");
        txt.append(ReportSimulation.getReportData(solvers) + "\n");
        txt.append(MyString.LINE + "\n");
        txt.append(MyString.toComment(ReportSimulation.getSolversInfo(solvers)) + "\n");
        txt.append(MyString.LINE + "\n");
        return txt.toString();
    }

    /**
     * gets the final report of the simulation
     *
     * @return txt of final report
     */
    public String getRanks() {
        StringBuffer txt = new StringBuffer();
        txt.append(MyString.LINE + "\n");
        txt.append("Confidence 95%\n");
        txt.append(MyString.LINE + "\n");
        txt.append(ReportSimulation.getReportRanks(solvers, 0.95) + "\n\n");

        txt.append(MyString.LINE + "\n");
        txt.append("Confidence 99%\n");
        txt.append(MyString.LINE + "\n");
        txt.append(ReportSimulation.getReportRanks(solvers, 0.99) + "\n");
        txt.append(MyString.LINE + "\n");
        return txt.toString();
    }

    public void saveRanksReport() {
        saveRanksReport(fileName);
    }

    /**
     * saves final report
     */
    public void saveRanksReport(String file) {
        //:::::::::::::::::::::::: write report :::::::::::::::
        PrintWriter out = null;
        try {
            out = new PrintWriter(file + DEFAULT_RESULT + "." + FILE_EXTENSION_CONFIG);
            out.println(MyString.toFileString(getRanks()));
            out.println(MyString.toFileString("\n" + MyString.LINE + "\n"));
            out.println(MyString.toFileString(getFinalReport()));
            out.print(MyString.toFileString(MyString.toComment(MyString.getCopyright())));

        } catch (Exception ex) {
            Logger.getLogger(EAExperiment.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    public void saveFinalReport() {
        saveFinalReport(fileName);
    }

    /**
     * saves final report
     */
    public void saveFinalReport(String filename) {
        try {
            PrintWriter out = new PrintWriter(filename + DEFAULT_RESULT + "." + FILE_EXTENSION_CONFIG);
            out.println(MyString.toFileString(getFinalReport()));
            out.print(MyString.toFileString(MyString.toComment(MyString.getCopyright())));
            out.close();
            saveObjectExperiment(filename);
        } catch (Exception ex) {
            Logger.getLogger(EAExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * saves final report
     */
    public void saveAs(String newFileName) {
        String path = MyFile.getPath(newFileName);
        String name = MyFile.getFileNameOnly(newFileName);
        //save Solvers
        for (EAsolver solver : solvers) {
            solver.save(path + name + "_" + solver.getSolverName());
        }
        saveObjectExperiment(newFileName);

        if (isEvolutionExecuted()) {
            saveRanksReport(path + name);
        }

    }

    /**
     * saves final report
     */
    public void saveExperiment(String file) {
        setFileName(file);
        String path = MyFile.getPath(file);
        String name = MyFile.getFileNameOnly(file);
        String ext = MyFile.getFileExtension(file);

        if (name.isEmpty()) {
            name = DEFAULT_NAME;
        }
        if (name.endsWith(DEFAULT_RESULT)) {
            name = name.substring(0, name.length() - DEFAULT_RESULT.length()) + DEFAULT_FILES;
        }
        if (!name.endsWith(DEFAULT_FILES)) {
            name += DEFAULT_FILES;
        }
        if (ext.isEmpty()) {
            ext = FileSolver.FILE_EXTENSION_CONFIG;
        }

        PrintWriter txtFile = null;
        try {
            file = path + name + "." + ext;
            MyFile.createPath(file);
            txtFile = new PrintWriter(new File(file));

            txtFile.print(MyString.toFileString(MyString.toComment(MyString.getCopyright() + "\n")));
            txtFile.print(MyString.toFileString("\n\n"));

            for (EAsolver solver : solvers) {
                //solver.report.setFileName(file);
                txtFile.println(FileSimulation.SIMULATION_FILE + " = " + solver.getSolverName() + "/" + MyFile.getFileName(solver.report.getFileName()));
            }

            txtFile.print(MyString.toFileString("\n\n"));
            for (EAsolver solver : solvers) {
                txtFile.println(MyString.toFileString(MyString.toComment(MyString.LINE)));
                txtFile.println(MyString.toFileString(MyString.toComment(solver.toString())));
            }
        } catch (Exception ex) {
        } finally {
            txtFile.close();
        }

    }

    /**
     * Saves report in a fileName with txt formar
     *
     * @param fileName
     */
    public void saveAsCSV(String fileName) {
        try {
            this.fileName = fileName;
            ReportSimulation.saveStatisticsCSV(solvers, fileName);
            ReportSimulation.saveStatisticsCSV(solvers, fileName, Locale.getDefault(), ";");
        } catch (Exception e) {
        }

    }

    /**
     * verify if all the solvers are done
     *
     * @return all the solvers are done ?
     */
    public boolean isDone() {
        for (EAsolver s : solvers) {
            if (!s.isDone()) {
                return false;
            }
        }
        return true;
    }

    public JTabbedPane getBoxAndWhiskerTabs() {
        JTabbedPane tabs = new JTabbedPane();
        ArrayList<ChartPanel> list = getBoxAndWhiskersCharts();
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.getStatistics();
        // Textual report
        JTextArea txt = new JTextArea(getFinalReport());
        txt.setFont(new Font("Courier new", Font.PLAIN, 14));
        tabs.addTab("Resume", new JScrollPane(txt));
        //charts
        for (int i = 0; i < list.size(); i++) {
            tabs.addTab(stats.get(i).getSimpleName(), list.get(i));

        }
        return tabs;
    }

    public ArrayList<ChartPanel> getBoxAndWhiskersCharts() {
        ArrayList<ChartPanel> list = new ArrayList<>();
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.getStatistics();
        String[] solversName = new String[solvers.size()];
        for (int i = 0; i < solversName.length; i++) {
            solversName[i] = solvers.get(i).getSolverName();
        }
        //charts
        for (AbstractStatistics stat : stats) {
            double[][] result = new double[solvers.size()][];
            for (int j = 0; j < solvers.size(); j++) {
                result[j] = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(stat);
            }
            //      list.add(StatisticsBoxAndWhisker.createBoxAndWhiskerChart(result, solversName, stat, StatisticsChartSolver.getChartTitle(solvers.get(0), stat)));

            String title = solvers.get(0).problem.getSimpleName() + " "
                    + solvers.get(0).problem.getParameters() + " (" + stat.getSimpleName()
                    + " " + solvers.get(0).numberOfRun + " runs)";
            list.add(StatisticsBoxAndWhisker.createBoxAndWhiskerChart(result, solversName, stat, title));
        }
        return list;
    }

    public JTabbedPane getRankTabs() {
        JTabbedPane tabs = new JTabbedPane();
        //        // Textual report
        JTextArea txt = new JTextArea(getRanks());
        txt.setFont(new Font("Courier new", Font.PLAIN, 14));
        tabs.addTab("Resume", new JScrollPane(txt));

        ArrayList<AbstractStatistics> stats = solvers.get(0).report.getStatistics();
        ArrayList<ChartPanel> list = getRankCharts();
        for (int i = 0; i < list.size(); i++) {
            tabs.addTab(stats.get(i).getSimpleName(), list.get(i));

        }
        return tabs;
    }

    public ArrayList<ChartPanel> getRankCharts() {
        ArrayList<ChartPanel> list = new ArrayList<>();
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.getStatistics();
        String[] solversName = new String[solvers.size()];
        for (int i = 0; i < solversName.length; i++) {
            solversName[i] = solvers.get(i).getSolverName();
        }

        //charts
        for (AbstractStatistics stat : stats) {
            double[][] result = new double[solvers.size()][];
            for (int j = 0; j < solvers.size(); j++) {
                result[j] = ((ReportSolverArray) solvers.get(j).report).getStatisticResult(stat);
            }
            list.add(StatisticsRankChart.getRankChart(result, solversName, stat));
        }
        return list;
    }

    public ArrayList<ChartPanel> getEvolutionCharts() {
        StatisticsChartExperiment statsPanel;
        statsPanel = new StatisticsChartExperiment(solvers);
        ArrayList<ChartPanel> list = new ArrayList<>();
        for (int i = 0; i < statsPanel.charts.size(); i++) {
            list.add(new ChartPanel(statsPanel.charts.get(i)));
        }
        return list;
    }

    /**
     * saves evolution solver
     *
     * @param solver the solver
     */
    public static void saveEvolutionSolver(EAsolver solver) {
        solver.report.save(false); // do not save array of solvers
    }

    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append(getRanks() + "\n");
        txt.append("\n" + MyString.LINE + "\n");
        txt.append(getFinalReport() + "\n");
        txt.append("\n" + MyString.LINE + "\n");
        txt.append(MyString.getCopyright());
        return txt.toString();
    }

    public String getSolversInfo() {
        int fieldSize = 20;
        StringBuilder txt = new StringBuilder();

        txt.append(MyString.alignCenter("solver name", fieldSize) + " | ");
        txt.append(MyString.alignCenter("solver type", fieldSize) + " | ");
        txt.append(MyString.alignCenter("problem name", fieldSize) + " | ");
        txt.append(MyString.alignCenter("population type", fieldSize) + " | ");

        txt.append(MyString.alignCenter("selection", fieldSize) + " | ");
        txt.append(MyString.alignCenter("recombination", fieldSize) + " | ");
        txt.append(MyString.alignCenter("mutation", fieldSize) + " | ");
        txt.append(MyString.alignCenter("replacement", fieldSize) + " | ");
        txt.append(MyString.alignCenter("rescaling", fieldSize) + " | ");

        txt.append(MyString.alignCenter("random seed", fieldSize) + " | ");
        txt.append(MyString.alignCenter("number of runs", fieldSize) + " | ");
        txt.append(MyString.alignCenter("stop criteria", fieldSize) + " | ");
        txt.append(MyString.alignCenter("solver name", fieldSize) + " | ");

        for (EAsolver solver : solvers) {
            txt.append("\n" + MyString.alignCenter("", 15 * fieldSize, '-') + "\n");
            txt.append(MyString.alignCut(solver.getSolverName(), fieldSize) + " | ");
            txt.append(MyString.alignCut(getSolverType(solver), fieldSize) + " | ");
            txt.append(MyString.alignCut(getSimpleGenetic(PROBLEM_NAME_LIB, solver.problem), fieldSize) + " | ");
            txt.append(MyString.alignCut(getSimpleGenetic(POPULATION_TYPE_LIB, solver.parents), fieldSize) + " | ");

            txt.append(MyString.alignCut(getSimpleGenetic(OPERATOR_SELECTION_LIB, solver.selection), fieldSize) + " | ");
            txt.append(MyString.alignCut(getSimpleGenetic(OPERATOR_RECOMBINATION_LIB, solver.recombination), fieldSize) + " | ");
            txt.append(MyString.alignCut(getSimpleGenetic(OPERATOR_MUTATION_LIB, solver.mutation), fieldSize) + " | ");
            txt.append(MyString.alignCut(getSimpleGenetic(OPERATOR_REPLACEMENT_LIB, solver.replacement), fieldSize) + " | ");
            txt.append(MyString.alignCut(getSimpleGenetic(OPERATOR_RESCALING_LIB, solver.rescaling), fieldSize) + " | ");

            txt.append(MyString.alignCut("" + solver.randomSeed, fieldSize) + " | ");
            txt.append(MyString.alignCut("" + solver.numberOfRun, fieldSize) + " | ");
            txt.append(MyString.alignCut(getSimpleGenetic(STOP_CRITERIA_LIB, solver.stop), fieldSize) + " | ");
            txt.append(MyString.alignCut(solver.getSolverName(), fieldSize) + " | ");
        }

        return txt.toString();
    }

    public static EAExperiment load(String fileName) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        return (EAExperiment) in.readObject();
    }

    public void saveObjectExperiment(String file) {

        if (!file.endsWith("." + FILE_EXTENSION_MUGA)) {
            String path = MyFile.getPath(file);
            String name = MyFile.getFileNameOnly(file);
            file = path + name + "." + FILE_EXTENSION_MUGA;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeObject(this);
        } catch (Exception ex) {
            Logger.getLogger(EAExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadObjectExperiment(String file) throws Exception {
        if (!file.endsWith("." + FileSolver.FILE_EXTENSION_MUGA)) {
            file += "." + FileSolver.FILE_EXTENSION_MUGA;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            EAExperiment obj = (EAExperiment) in.readObject();
            for (EAsolver objSolver : obj.solvers) {
                this.solvers.add(objSolver);
            }
            in.close();
        } catch (Exception e) {
            EAsolver solver = EAsolver.load(file);
            if (solver instanceof EAsolverArray) {
                this.solvers.add(solver);
            }
        }
    }

    public void resizeStatisticsElements() {
        resizeStatisticsElements(ReportSolver.NUMBER_OF_STATS_EVOLUTION);
    }

    public void resizeStatisticsElements(int maxSize) {
        for (EAsolver solver : solvers) {
            solver.report.redimStatisticsEvolution(maxSize);
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603270609L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
