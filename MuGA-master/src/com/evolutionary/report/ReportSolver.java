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

import com.evolutionary.Genetic;
import com.evolutionary.problem.Individual;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.report.statistics.BestFitness;
import com.evolutionary.report.statistics.FunctionCalls;
import com.evolutionary.report.statistics.FuncsCallsToOptimum;
import com.evolutionary.report.statistics.Generation;
import com.evolutionary.report.statistics.GeneticDiversity;
import com.evolutionary.report.statistics.Genotypes;
import com.evolutionary.report.statistics.Individuals;
import com.evolutionary.report.statistics.MaxCopies;
import com.evolutionary.report.statistics.NumOptimalGenomesPop;
import com.evolutionary.report.statistics.NumOptimumsFound;
import com.evolutionary.report.statistics.NumMaximaPop;
import com.evolutionary.report.statistics.SelectedGenotypes;

import com.evolutionary.report.statistics.SucessRate;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import com.evolutionary.solverUtils.FileReportSolver;
import com.evolutionary.solverUtils.FileSolver;
import static com.evolutionary.solverUtils.FileSolver.HALL_OF_FAME_KEY;
import static com.evolutionary.solverUtils.FileSolver.STATISTIC_KEY;
import com.evolutionary.stopCriteria.OptimumFound;
import com.utils.MyFile;
import com.utils.MyString;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 20/mar/2016, 18:22:20
 *
 * @author zulu - computer
 */
public class ReportSolver implements Serializable {

    public static void main(String[] args) {
        EAsolver s = new GA();
        s.stop = new OptimumFound();

        s.solve(true);
        //   s.report.saveObject("arrayGA.muga");
        System.out.println(s.toString());

//        ReportSolver report = (ReportSolver) ReportSolver.loadReportObject("array.muga");
        ArrayList<AbstractStatistics> templateStats = new ArrayList<>();
        templateStats.add(new BestFitness());
        templateStats.add(new FuncsCallsToOptimum());

        s.report.updateStatisticsTemplates(templateStats);
        System.out.println(s.toString());
        System.out.println(s.report.getEvolutionString());

    }

    public static String DEFAULT_FILE_NAME = "DefaultSolver";

    public ArrayList<AbstractStatistics> stats = new ArrayList<>(); // template of statistics
    public ArrayList<Double[]> evolution = new ArrayList<>(); // evolution of each statistic    

    public Date startTime = new Date(); // start time of evolution
    public long runningTime = 0; // simulation time 
    public String path = ""; // filename of report

    public EAsolver solver; // solver to produce report
    public boolean verbose; // display information in the console 

    /**
     * sets the default statistics to the evolution NOTE: the order of the
     * statistist is the same in the graphics plots
     */
    public void setDefaultStatistics() {
        stats.add(new FunctionCalls());
        stats.add(new Individuals());
        stats.add(new BestFitness());
        stats.add(new FuncsCallsToOptimum());
        stats.add(new NumMaximaPop());
        stats.add(new SucessRate());
        stats.add(new SelectedGenotypes());
        stats.add(new GeneticDiversity());

        stats.add(new NumOptimalGenomesPop());
        stats.add(new Genotypes());
        stats.add(new NumOptimumsFound());
        stats.add(new MaxCopies());
        sortStatistics();
    }

    protected void swap(ArrayList array, int index1, int index2) {
        //verify indexes
        if (index1 < 0 || index1 >= array.size() || index2 < 0 || index2 >= array.size()) {
            return;
        }
        Object aux = array.get(index1);
        array.set(index1, array.get(index2));
        array.set(index2, aux);
    }

    public void sortStatistics() {
        //put functions call in the last position
        int indexFC = stats.indexOf(new FunctionCalls());
        swap(stats, indexFC, stats.size() - 1);
        swap(evolution, indexFC, evolution.size() - 1);
        //sort the rest of the array - Not the last position if is FunctionsCall
        int arraySize = indexFC >= 0 ? stats.size() - 1 : stats.size();
        //Selection sort
        int min;

        for (int i = 0; i < arraySize - 1; i++) {
            min = i;
            for (int j = i + 1; j < arraySize; j++) {
                if (stats.get(j).compareTo(stats.get(min)) < 0) {
                    min = j;
                }
            }
            if (i != min) {
                swap(stats, i, min);
                swap(evolution, i, min);
            }
        }        
    }

    public void updateStats() {
        updateStats(false); //not forced

    }

    /**
     * update statistics with the last values of the solver
     *
     * @param forceUpdate force the stats aupdate
     */
    public void updateStats(boolean forceUpdate) {
//        if (!forceUpdate) {
//            if (!checkSmartUpdate()) {
//                return;
//            }
//        }
        Double[] values = new Double[stats.size()];
        for (int i = 0; i < stats.size(); i++) {
            values[i] = stats.get(i).execute(solver);
        }
        evolution.add(values);
        //----------------------------------------------------------------------verbose
        if (verbose) {
            System.out.println(getStatisticsData(evolution.size() - 1));
        }
        //----------------------------------------------------------------------verbose
    }

    /**
     * reset stats to begins the evolution
     *
     * @param solver solver to report
     * @param verbose print output ?
     */
    public void startStats(EAsolver solver, boolean verbose) {
        sortStatistics();
        this.solver = solver;
        this.verbose = verbose;
        this.solver.numEvaluations = 0;
        this.solver.numGeneration = 0;
        solver.hallOfFame.clear();
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        if (stats.isEmpty()) {
            setDefaultStatistics();
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        // add functions call to produce charts        
        addStatistic(new Generation());
        // add generation to resize statistics
        addStatistic(new FunctionCalls());

        startTime = new Date();
        //initialize array of statistics
        evolution = new ArrayList<>();
        //:::::::::: PATH ::::::::::::::::::::::::::::::::::::::::::::::::
        String path = MyFile.getPath(this.path);
        File f = new File(path);
        f.mkdirs();
        //:::::::::: PATH ::::::::::::::::::::::::::::::::::::::::::::::::
        if (verbose) {
            //TITLE OF STATS
            System.out.println("\n" + getStatisticsHeader("Solver"));
        }
    }

    /**
     * resize evolution arrays delete statistics records recorded in the
     * evolution The purpose is to shrink the size of data stored
     */
    public void redimStatisticsEvolution() {
        redimStatisticsEvolution(NUMBER_OF_STATS_EVOLUTION);
    }

    /**
     * resize evolution
     *
     * @param newSize desired size of statistics
     */
    public void redimStatisticsEvolution(int newSize) {
        if (evolution.size() <= newSize) {
            return;
        }

        ArrayList<Double[]> smallEvolution = new ArrayList<>(newSize);
        //step of iterator in the evolutions array
        double step = (double) evolution.size() / (double) (newSize - 1); // -1 = last stat
        for (int i = 0; i < newSize - 1; i++) { // -1 = last one is seted               
            smallEvolution.add(evolution.get((int) (i * step)));
        }
        //add last record
        smallEvolution.add(evolution.get(evolution.size() - 1));
        evolution = smallEvolution;
    }

    /**
     * gets de name of the file to save report
     *
     * path/solvername.txt
     *
     * @return filename
     */
    public String getFileName() {
        String file = MyFile.getPath(path); // path with /
        file += solver != null ? solver.getSolverName() : DEFAULT_FILE_NAME;
        file += "." + FileSolver.FILE_EXTENSION_CONFIG;
        return file;
    }

    /**
     * change the filename and the path of the report
     *
     * @param fileName new filename with path ./folder/folder/file.ext
     */
    public void setFileName(String fileName) {

        String newPath = MyFile.getPath(fileName);
        String newName = MyFile.getFileNameOnly(fileName);
        if (this.solver != null) {
            this.solver.setSolverName(newName);
        }
        setPath(newPath);
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * update the solver of the report
     *
     * @param solver
     */
    public void setSolver(EAsolver solver) {
        this.solver = solver;
    }

    /**
     * get a clone of the object without data
     *
     * @return
     */
    public ReportSolver getCleanClone() {
        ReportSolver clone = new ReportSolver();
        clone.path = path;
        clone.verbose = verbose;
        //clone statistics template
        for (AbstractStatistics s : stats) {
            AbstractStatistics statisticClone = (AbstractStatistics) s.getClone();
            statisticClone.setSolver(solver);
            clone.stats.add(statisticClone);
        }
        return clone;
    }

    /**
     * append one statistic to the report if the statistic not exists yet
     *
     * @param stat string with name of the class
     */
    public void addStatistic(String stat) {
        addStatistic((AbstractStatistics) Genetic.createNew(stat));
    }

    /**
     * append one statistic to the report if the statistic not exists yet
     *
     * @param stat statistic object
     */
    public void addStatistic(AbstractStatistics stat) {
        //verify if the statistics is already in the report
        for (AbstractStatistics stat1 : stats) {
            if (stat1.getClass().equals(stat.getClass())) {
                return;
            }
        }
        //Generation is always the first
        if (stat.equals(new Generation())) {
            stats.add(0, stat);

        } //Functions Call is the last
        else if (stat.equals(new FunctionCalls())) {
            stats.add(stats.size(), stat);
        } else { // other stats is in the middle
            stats.add(stat);
        }
    }

    /**
     * remove statistic of the report
     *
     * @param index index of statistic
     */
    public void removeStatistic(int index) {
        stats.remove(index);
    }

    public ArrayList<AbstractStatistics> getStatistics() {
        return stats;
    }

    public void setStatistics(ArrayList<AbstractStatistics> arrayOfStats) {
        stats = new ArrayList<>();
        for (AbstractStatistics stat : arrayOfStats) {
            stats.add(stat.getClone());
        }
    }

    /**
     * last values of all statistics
     *
     * @return
     */
    public Double[] getLastValues() {
        return evolution.get(evolution.size() - 1);
    }

    /**
     * last values of the statistics
     *
     * @param stat statistic
     * @return
     */
    public double getLastValue(AbstractStatistics stat) {
        try {
            int index = stats.indexOf(stat);
            Double[] values = getLastValues();
            return values[index];
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * finish stats - calculate running time
     */
    public void finishStats() {
        runningTime = (new Date()).getTime() - startTime.getTime();
        updateStats(true); // force last statistic
        if (verbose) {
            save();
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * return a string with the name of statistics
     *
     * @param title getTitle to header
     * @return
     */
    public String getStatisticsHeader(String title) {
        StringBuilder txt = new StringBuilder();
        txt.append((MyString.setSize(title, FIELD_SIZE))); // name of the line
        for (int i = 0; i < stats.size(); i++) {
            txt.append(" " + MyString.alignRight(stats.get(i).toString(), FIELD_SIZE));
        }
        return txt.toString();
    }

    /**
     * gets the statistic in the generation
     *
     * @param generation
     * @return
     */
    public String getStatisticsData(int generation) {
        StringBuilder txt = new StringBuilder();
        txt.append(MyString.setSize(STATISTIC_KEY + " " + generation, FIELD_SIZE));
        if (generation >= evolution.size()) {
            generation = evolution.size() - 1;
        }
        Double[] values = evolution.get(generation);
        for (Double value : values) {
            txt.append(" " + MyString.alignRight("" + value, FIELD_SIZE));
        }
        txt.append(" " + solver.parents.getBest());
        return txt.toString();
    }

    /**
     * gets the statistic in the generation
     *
     * @param generation
     * @return
     */
    public double getStatisticsData(int generation, AbstractStatistics stat) {
        Double[] values = evolution.get(generation);
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).getClass().equals(stat.getClass())) {
                return values[i];
            }
        }
        return 0;
    }

    public void loadFromFile() {
        loadFromFile(getFileName());
    }

    public void loadFromFile(String fileName) {
        ReportSolver report = FileReportSolver.loadReportFromFile(fileName);
        this.evolution = report.evolution;
        this.path = report.path;
        this.runningTime = report.runningTime;
        this.startTime = report.startTime;
        this.stats = report.stats;
    }

    public void saveObject() {
        try {
            File f = new File(path);
            saveObject(f.getAbsolutePath() + File.separatorChar + solver.getSolverName());
        } catch (Exception ex) {
            Logger.getLogger(ReportSolver.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * save the report in new File
     *
     * @param fileName new file of the report
     */
    public void saveObject(String fileName) throws Exception {
        redimStatisticsEvolution();
        //remove termination
        if (fileName.endsWith("." + FileSolver.FILE_EXTENSION_MUGA)) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        File file = new File(fileName);
        file = new File(file.getAbsolutePath());
        //make dirs        
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //update path and solver name
        solver.setSolverName(file.getName());
        this.path = file.getParent();
        System.err.println("Saving " + file.getAbsolutePath());
        //save report
        ObjectOutputStream fileOut = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(file.getAbsolutePath() + "." + FileSolver.FILE_EXTENSION_MUGA)));
        fileOut.writeObject(this);
        fileOut.close();
    }

    /**
     * save the report in new File
     *
     * @param fileName new file of the report
     */
    public static ReportSolver loadReportObject(String fileName) throws Exception {
        //remove termination
        if (!fileName.endsWith("." + FileSolver.FILE_EXTENSION_MUGA)) {
            fileName += "." + FileSolver.FILE_EXTENSION_MUGA;
        }
        ObjectInputStream file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        ReportSolver obj = (ReportSolver) file.readObject();
        file.close();
        return obj;
    }

    /**
     * save the report in new File
     *
     * @param path new file of the report
     */
    public void save(String path) {
        setFileName(path);
        solver.setSolverName(MyFile.getFileNameOnly(path));
        save();
    }

    public void save(boolean saveAll) { // ignore - used in array solver
        save();
    }

    /**
     * save the report in current fileName
     */
    public void save() {
        PrintWriter file = null;
        try {
            //make path
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            file = new PrintWriter(path + File.separatorChar + solver.getSolverName() + ".txt");
            file.println(solverToFullString());
            file.close();

        } catch (Exception ex) {
            Logger.getLogger(ReportSolver.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            file.close();
        }

    }

    public String solverToFullString() {
        StringBuilder txt = new StringBuilder();

        txt.append(MyString.toFileString(MyString.LINE));
        txt.append("\n" + MyString.toFileString(MyString.toComment(" Solver Configuration")));
        txt.append("\n" + MyString.toFileString(MyString.LINE));
        txt.append("\n" + MyString.toFileString(solver.toString()));
        txt.append("\n" + MyString.toFileString(MyString.LINE));
        txt.append("\n" + MyString.toFileString(MyString.toComment(" Solver Evolution ")));
        txt.append("\n" + MyString.toFileString(MyString.LINE));
        txt.append("\n" + MyString.toFileString(getEvolutionString()));
        txt.append("\n" + MyString.toFileString(MyString.LINE));
        txt.append("\n" + MyString.toFileString(MyString.toComment(solver.parents.toString())));
        txt.append("\n" + MyString.toFileString(MyString.LINE));
        txt.append("\n" + MyString.toFileString(MyString.toComment(solver.problem.getInformation())));
        txt.append("\n" + MyString.toFileString(MyString.LINE));
        txt.append("\n" + MyString.toFileString(MyString.toComment(MyString.getCopyright())));
        txt.append("\n");
        return txt.toString();
    }

    protected void save(PrintWriter txtSimulation) {
        try {
            txtSimulation.println("#random seed   = " + solver.randomSeed);
            txtSimulation.println("#START_TIME    = " + MyString.toString(startTime));
            txtSimulation.println("#FINISH_TIME   = " + MyString.toString(new Date(startTime.getTime() + runningTime)));
            txtSimulation.println("#RUNNING_TIME  = " + runningTime);

            //TITLE OF STATS
            txtSimulation.println(getStatisticsHeader("Result"));
            for (int i = 0; i < evolution.size(); i++) {
                //stats data
                txtSimulation.println(getStatisticsData(i));
            }
            txtSimulation.println();
            // txtSimulation.print(NEWLINE + NEWLINE + "Main Population" + NEWLINE + solver.parents.toString().replaceAll("\n", NEWLINE));

        } catch (Exception ex) {
            Logger.getLogger(ReportSolver.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * calculate the string result of the evolution:
     *
     * @return
     */
    public String getEvolutionString() {
        StringBuffer txt = new StringBuffer();
        try {
            txt.append(MyString.toComment(FileSolver.START_TIME + " " + MyString.toString(startTime)) + "\n");
            txt.append(FileSolver.START_TIME + " " + startTime.getTime() + "\n");
            txt.append(FileSolver.RUNNING_TIME + " " + runningTime + "\n");

            //TITLE OF STATS
            txt.append(MyString.setSize("", FIELD_SIZE));
            for (int i = 0; i < stats.size(); i++) {
                txt.append(" " + MyString.alignRight(stats.get(i).toString(), FIELD_SIZE));
            }
            //Stats data 
            for (int i = 0; i < evolution.size(); i++) {
                txt.append("\n");
                Double[] v = evolution.get(i);
                txt.append("" + MyString.setSize(STATISTIC_KEY + " " + i, FIELD_SIZE));
                for (int j = 0; j < v.length; j++) {
                    txt.append(" " + MyString.alignRight(v[j], FIELD_SIZE));
                }
            }
            txt.append("\n\n" + HALL_OF_FAME_KEY + "\n");
            if (solver != null) {
                for (Individual ind : solver.hallOfFame) {
                    txt.append(ind + "\n");

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(ReportSolver.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return txt.toString();
    }

    /**
     * add data to statistics
     *
     * @param data string with number separated by spaces
     */
    public void addDataToStatistics(String data) {
        try {
            String[] d = MyString.splitByWhite(data);
            Double[] elem = new Double[d.length];
            for (int i = 0; i < elem.length; i++) {
                elem[i] = Double.parseDouble(d[i]);
            }
            evolution.add(elem);
        } catch (Exception e) {
        }

    }

    /**
     * gets the index of the statistic
     *
     * @param stat statistic
     * @return
     */
    public int indexOf(AbstractStatistics stat) {
        for (int i = 0; i < stats.size(); i++) {
            if (stat.getClass().equals(stats.get(i).getClass())) {
                return i;
            }
        }
        return -1;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603201822L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    public static int FIELD_SIZE = 20; // number of chars to print filds 
    //public static String NEWLINE = "\r\n"; //new to line to text files   

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * remove statistics from evolution used to compare solvers with the same
     * statistics
     *
     * remove statistics from stats template remove evolution values from
     * evolution values
     *
     * @param templateStats statistics to ramain in report
     */
    public void updateStatisticsTemplates(ArrayList<AbstractStatistics> templateStats) {
        //add function evals
        // is needed to build chart
        if (!templateStats.contains(new FunctionCalls())) {
            templateStats.add(new FunctionCalls());
        }
        //calculate index to remove
        ArrayList<Integer> validIndexes = new ArrayList<>();
        for (int i = 0; i < stats.size(); i++) {
            if (templateStats.contains(stats.get(i))) {
                validIndexes.add(i);
            }
        }
        //build new Statistics with correct indexes
        ArrayList<AbstractStatistics> newStats = new ArrayList<>();
        for (int i = 0; i < stats.size(); i++) {
            if (validIndexes.contains(i)) {
                newStats.add(stats.get(i));
            }
        }
        //build evolution values  
        ArrayList<Double[]> newEvolution = new ArrayList<>(); // evolution of each statistic 
        for (int i = 0; i < evolution.size(); i++) {
            Double[] generationValues = new Double[validIndexes.size()];
            int index = 0;
            for (int j = 0; j < evolution.get(i).length; j++) {
                if (validIndexes.contains(j)) {
                    generationValues[index++] = evolution.get(i)[j];
                }
            }
            newEvolution.add(generationValues);
        }
        //update references to new values
        this.stats = newStats;
        this.evolution = newEvolution;
        //copy values
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public static int NUMBER_OF_STATS_EVOLUTION = 300;
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

}
