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

import com.evolutionary.problem.Individual;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.report.statistics.BestFitness;
import com.evolutionary.report.statistics.FunctionCalls;
import com.evolutionary.report.statistics.FuncsCallsToOptimum;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import com.evolutionary.solverUtils.EAsolverArray;
import com.evolutionary.solverUtils.FileReportArray;
import com.evolutionary.solverUtils.FileSolver;
import static com.evolutionary.solverUtils.FileSolver.SOLVER_KEY;
import com.evolutionary.stopCriteria.OptimumFound;
import com.utils.MyStatistics;
import com.utils.MyString;
import java.io.File;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 23/mar/2016, 14:21:34
 *
 * @author zulu - computer
 */
public class ReportSolverArray extends ReportSolver {

    /**
     * Result of statistics in each generation of the solvers if the solvers
     * dont have the generation, last generation of the solver is used
     *
     */
    public ArrayList<StatisticElement[]> evolutionGeneration = new ArrayList<>(); // Result of statistics in each generation
    public ArrayList<Double[]> solversResult = new ArrayList<>(); // Result of statistics in last generation of each solver

    public ReportSolverArray() {
    }

    /**
     * build a report array to solver
     *
     * @param solver
     */
    public ReportSolverArray(EAsolver solver) {
        this.path = solver.report.path;
        this.solver = solver;
        this.verbose = solver.report.verbose;
        //clone statistics template
        for (AbstractStatistics s : solver.report.stats) {
            this.stats.add(s.getClone());
        }
    }

    @Override
//    protected void sortStatistics() {
//        int indexFC = stats.indexOf(new FunctionCalls());
//
//        AbstractStatistics auxStat = stats.get(stats.size() - 1);
//        stats.set(stats.size() - 1, stats.get(indexFC));
//        stats.set(indexFC, auxStat);
//
//        Double[] auxVal = evolution.get(indexFC);
//        evolution.set(evolution.size() - 1, evolution.get(indexFC));
//        evolution.set(indexFC, auxVal);
//
//        Double[] auxRes = solversResult.get(indexFC);
//        solversResult.set(solversResult.size() - 1, solversResult.get(indexFC));
//        solversResult.set(indexFC, auxRes);
//
//        StatisticElement[] auxGen = evolutionGeneration.get(indexFC);
//        evolutionGeneration.set(evolutionGeneration.size() - 1, evolutionGeneration.get(indexFC));
//        evolutionGeneration.set(indexFC, auxGen);
//        
//        

    public void sortStatistics() {
        //put functions call in the last position
        int indexFC = stats.indexOf(new FunctionCalls());
        swap(stats, indexFC, stats.size() - 1);
        swap(evolution, indexFC, evolution.size() - 1);
        swap(solversResult, indexFC, solversResult.size() - 1);
        swap(evolutionGeneration, indexFC, evolutionGeneration.size() - 1);
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
                swap(solversResult, i, min);
                swap(evolutionGeneration, i, min);
            }
        }
    }

    /**
     * gets the name of the reportname of solver i
     *
     * @param i index of solver
     * @return report file name
     */
    public String getFileName(int i) {
        String file = (new File(path)).getAbsolutePath();
        if (!path.endsWith(File.separatorChar + "")) {
            file += File.separatorChar;
        }
        //Add folder
        file += solver.getSolverName() + File.separatorChar;
        //add file
        file += solver.getSolverName() + String.format("_%03d.", i) + FileSolver.FILE_EXTENSION_CONFIG;
        return file;
    }

    /**
     * gets the name of the reportname of solver i
     *
     * @param i index of solver
     * @return report file name
     */
    public static String getReportFileName(String path, String solverName, int i) {
        String file = (new File(path)).getParentFile().getAbsolutePath();
        if (!path.endsWith(File.separatorChar + "")) {
            file += File.separatorChar;
        }
        //Add folder
        file += solverName + File.separatorChar;
        //add file
        file += solverName + String.format("_%03d.", i) + FileSolver.FILE_EXTENSION_CONFIG;
        return file;
    }

    /**
     * Start the report statistics of to the solver
     *
     * @param solver solver of the statistics
     * @param verbose verbose ?
     */
    @Override
    public void startStats(EAsolver solver, boolean verbose) {
        evolutionGeneration.clear();
        solversResult.clear();
        evolution.clear();
        super.startStats(solver, verbose);

    }

    /**
     * gets the last statistic of the evolution
     *
     * @return StatisticElement of last generation evolution
     */
    public StatisticElement[] getLastValuesStatistcs() {
        return evolutionGeneration.get(evolutionGeneration.size() - 1);
    }

    /**
     * gets the last statistic of the evolution
     *
     * @return StatisticElement of last generation evolution
     */
    public double[] getLastValuesStatistcs(AbstractStatistics stat) {
        StatisticElement[] last = evolutionGeneration.get(evolutionGeneration.size() - 1);
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).getClass().equals(stat.getClass())) {
                return last[i].getData();
            }

        }
        return null;

    }

    public void loadFromFile() {
        loadFromFile(getFileName());
    }

    public void loadFromFile(String fileName) {
        ReportSolverArray report = FileReportArray.loadReportFromFile(fileName);
        this.evolution = report.evolution;
        this.path = report.path;
        this.runningTime = report.runningTime;
        this.startTime = report.startTime;
        this.stats = report.stats;
        this.evolutionGeneration = report.evolutionGeneration;
        this.solversResult = report.solversResult;
    }

    /**
     * save report using report fileanme
     */
    @Override
    public void save() {
        save(((EAsolverArray) solver).arrayOfSolvers, true);
    }

    @Override
    public void save(boolean saveAll) {
        save(((EAsolverArray) solver).arrayOfSolvers, saveAll);
    }

    /**
     * save array of solvers
     *
     * @param arraySolvers array of solvers
     */
    private void save(EAsolver[] arraySolvers, boolean allSolvers) {
        try {
            if (allSolvers) {
                for (int i = 0; i < arraySolvers.length; i++) {
                    if (!arraySolvers[i].report.evolution.isEmpty()) {
                        arraySolvers[i].report.save(getFileName(i)); // save individual solvers
                    }
                }
            }

            (new File(path)).mkdirs(); //create path
            PrintWriter file = new PrintWriter(getFileName());
            //solver information
            file.println(MyString.toFileString(MyString.LINE));
            file.println(MyString.toFileString(MyString.toComment(" Solver Array Configuration")));
            file.println(MyString.toFileString(MyString.LINE));
            //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: solver setup
            file.println(MyString.toFileString(solver.toString()));
            file.println(MyString.toFileString(MyString.LINE));
            file.println(MyString.toFileString(MyString.toComment(" Simulation Results")));
            file.println(MyString.toFileString(MyString.LINE));
            //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: result of simulation            
            file.println(MyString.toFileString(getEvolutionString()));
            file.println(MyString.toFileString(MyString.LINE));
            file.println(MyString.toFileString(MyString.toComment(solver.problem.getInformation())));
            file.println(MyString.toFileString(MyString.LINE));
            file.println("\n" + MyString.toFileString(MyString.getCopyright()));

            file.close();

        } catch (Exception ex) {
            Logger.getLogger(ReportSolver.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateEvolutionStatistics(EAsolver[] arraySolvers) {
        //reset stats information
        solver.report.startStats(solver, false);
        //  solversResult.clear();
        startTime = arraySolvers[0].report.startTime;
        //----------------------------------------------------------------------        
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: mean time of running solvers
        runningTime = 0;
        for (int i = 0; i < arraySolvers.length; i++) {
            //update hall of fame
            solver.updateHallOfFame(arraySolvers[i].parents);
            //last stat
            solversResult.add(arraySolvers[i].report.evolution.get(arraySolvers[i].report.evolution.size() - 1));
            runningTime += arraySolvers[i].report.runningTime;
        }
        //mean of running time
        runningTime /= arraySolvers.length;
        //----------------------------------------------------------------------
        //calculate statistic of each statistic - LAST VALUE OF SIMULATION
        solversResult.clear();
        for (EAsolver solver : arraySolvers) {
            solversResult.add(solver.report.getLastValues());
        }

        //calcule evolution of the mean
        evolution.clear();
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: calulate maxGeneration
        int maxGen = -1;
        for (EAsolver solver : arraySolvers) {
            if (solver.report.evolution.size() > maxGen) {
                maxGen = solver.report.evolution.size();
            }
        }
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: calculate means of each generation
        for (int i = 0; i < maxGen; i++) {
            //mean ( seted to inherited evolution )
            evolution.add(getMeanAtGeneration(arraySolvers, i));
            //complete statistic
            evolutionGeneration.add(getValuesAtGeneration(arraySolvers, i));
        }
    }

    /**
     * gets the statistic of the statistics in one generation
     *
     * @param arraySolvers array of solvers
     * @param gen generation number
     * @return mean statistics
     */
    private StatisticElement[] getValuesAtGeneration(EAsolver[] arraySolvers, int gen) {
        StatisticElement[] elem = new StatisticElement[arraySolvers[0].report.stats.size()];
        //for each startistic
        for (int stat = 0; stat < arraySolvers[0].report.stats.size(); stat++) {
            //value of each solver
            double[] values = new double[arraySolvers.length];
            for (int i = 0; i < arraySolvers.length; i++) {
                if (gen < arraySolvers[i].report.evolution.size()) // generation defined
                {
                    values[i] = arraySolvers[i].report.evolution.get(gen)[stat];
                } else //last generation 
                {
                    values[i] = arraySolvers[i].report.evolution.get(arraySolvers[i].report.evolution.size() - 1)[stat]; // las generation
                }            //acumulate values         
            }
            //build statistic
            elem[stat] = new StatisticElement(values);
        }
        return elem;
    }

    /**
     * gets the mean of the statistics in one generation
     *
     * @param arraySolvers array of solvers
     * @param gen generation number
     * @return mean statistics
     */
    private Double[] getMeanAtGeneration(EAsolver[] arraySolvers, int gen) {
        //sum of each statistic
        double[] sum = new double[arraySolvers[0].report.stats.size()];
        //for each solver
        for (int i = 0; i < arraySolvers.length; i++) {
            //get statistic
            Double[] stat = null;
            if (gen < arraySolvers[i].report.evolution.size()) // generation defined
            {
                stat = arraySolvers[i].report.evolution.get(gen);
            } else //last generation 
            {
                stat = arraySolvers[i].report.evolution.get(arraySolvers[i].report.evolution.size() - 1); // last generation
            }            //acumulate values
            for (int j = 0; j < stat.length; j++) {
                sum[j] += stat[j];

            }
        }
        Double[] mean = new Double[sum.length];
        //calculate mean
        for (int j = 0; j < sum.length; j++) {
            mean[j] = new Double(sum[j] / arraySolvers.length);

        }
        return mean;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public String getEvolutionString() {

        if (evolutionGeneration.isEmpty()) {
            return "Evolution not excuted";
        }
        StringBuffer txt = new StringBuffer(getStatisticsHeader("Result") + "\n");

        StatisticElement[] lasStat = evolutionGeneration.get(evolutionGeneration.size() - 1);

        //print stats      
        txt.append(MyString.setSize(StatisticElement.getMeanLabel() + " ", ReportSolver.FIELD_SIZE));
        for (int j = 0; j < stats.size(); j++) {
            txt.append(" " + MyString.alignRight(lasStat[j].getMean(), ReportSolver.FIELD_SIZE));
        }
        txt.append(MyString.setSize("\n" + StatisticElement.getStdLabel() + " ", ReportSolver.FIELD_SIZE + 1)); // +1 because \n
        for (int j = 0; j < stats.size(); j++) {
            txt.append(" " + MyString.alignRight(lasStat[j].getStd(), ReportSolver.FIELD_SIZE));
        }
        txt.append(MyString.setSize("\n" + StatisticElement.getMinLabel() + " ", ReportSolver.FIELD_SIZE + 1));
        for (int j = 0; j < stats.size(); j++) {
            txt.append(" " + MyString.alignRight(lasStat[j].getMin(), ReportSolver.FIELD_SIZE));
        }
        txt.append(MyString.setSize("\n" + StatisticElement.getMedianLabel() + " ", ReportSolver.FIELD_SIZE + 1));
        for (int j = 0; j < stats.size(); j++) {
            txt.append(" " + MyString.alignRight(lasStat[j].getMedian(), ReportSolver.FIELD_SIZE));
        }
        txt.append(MyString.setSize("\n" + StatisticElement.getMaxLabel() + " ", ReportSolver.FIELD_SIZE + 1));
        for (int j = 0; j < stats.size(); j++) {
            txt.append(" " + MyString.alignRight(lasStat[j].getMax(), ReportSolver.FIELD_SIZE));
        }
        //print solvers info             
        txt.append("\n\n");
        //update information about solvers
        if (solversResult.isEmpty() && ((EAsolverArray) solver).arrayOfSolvers != null) {
            for (EAsolver solver : ((EAsolverArray) solver).arrayOfSolvers) {
                solversResult.add(solver.report.getLastValues());
            }
        }
        for (int i = 0; i < solversResult.size(); i++) {
            //last stat
            Double[] v = solversResult.get(i);
            //solver number
            txt.append(MyString.setSize(SOLVER_KEY + " " + i, ReportSolver.FIELD_SIZE));
            //other stats
            for (int j = 0; j < v.length; j++) {
                txt.append(" " + MyString.alignRight("" + v[j], ReportSolver.FIELD_SIZE));
            }
            txt.append("\n");
        }

        if (solver != null) {
            txt.append("\n\n" + FileSolver.HALL_OF_FAME_KEY + "\n");
            for (Individual ind : solver.hallOfFame) {
                txt.append(ind + "\n");
            }
        }

        txt.append(MyString.toFileString(MyString.LINE) + "\n");
        txt.append(MyString.toFileString(MyString.toComment(" Evolution Statistics in generations")) + "\n");
        txt.append(MyString.toFileString(MyString.LINE) + "\n");
//        //means
//        txt.append(super.getEvolutionString());
//        
        txt.append("\n\n" + getArrayEvolutionString());
        return txt.toString();
    }

    public String getArrayEvolutionString() {
        StringBuilder txt = new StringBuilder();

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        // HEADER
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        String header = StatisticElement.getHeaderString(); // mean min , max , etc
        txt.append(MyString.setSize("", ReportSolver.FIELD_SIZE));//empty
        for (int i = 0; i < stats.size(); i++) {
            txt.append(MyString.alignCenter(stats.get(i).getSimpleName(), header.length(), '_') + " \t\t");//statistics name            
        }
        txt.append("\n" + MyString.setSize("", ReportSolver.FIELD_SIZE));//empty
        for (int i = 0; i < stats.size(); i++) {// mean min , max , etc
            txt.append(header + " \t\t");
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        // DATA
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        for (int i = 0; i < evolutionGeneration.size(); i++) {
            txt.append("\n" + MyString.setSize(FileSolver.STATISTIC_KEY + " " + i, ReportSolver.FIELD_SIZE));//statistic number
            StatisticElement[] stat = evolutionGeneration.get(i);
            for (StatisticElement statisticElement : stat) {
                txt.append(statisticElement.getDataString() + " \t\t");
            }
        }

        return txt.toString();

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::      S T A T I S T I C S    R E S U L T S      :::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private StatisticElement getLastStat(int index) {
        return evolutionGeneration.get(evolutionGeneration.size() - 1)[index];
    }

    public double getNumberOfEvalutions(int generation) {
        if (evolutionGeneration.isEmpty()) {
            return 0;
        }
        if (generation >= evolutionGeneration.size()) {
            generation = evolutionGeneration.size() - 1;
        }
        StatisticElement[] values = evolutionGeneration.get(generation);
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).getClass().equals(FunctionCalls.class
            )) {
                return values[i].getMax();
            }
        }
        return 0;
    }

    //--------------------------------------------------------------------------mean
    public double getMean(int index) {
        return getLastStat(index).getMean();
    }

    public double[] getMeans() {
        double[] v = new double[stats.size()];
        for (int index = 0; index < v.length; index++) {
            v[index] = MyStatistics.mean(getStatisticResult(index));
        }
        return v;
    }

    //--------------------------------------------------------------------------std
    public double getSTD(int index) {
        return getLastStat(index).getStd();
        //return MyStatistics.std(getStatisticResult(index));
    }

    public double[] getSTDs() {
        double[] v = new double[stats.size()];
        for (int index = 0; index < v.length; index++) {
            v[index] = MyStatistics.std(getStatisticResult(index));
        }
        return v;
    }

    //--------------------------------------------------------------------------min
    public double getMin(int index) {
        return getLastStat(index).getMin();
        //return MyStatistics.min(getStatisticResult(index));
    }

    //--------------------------------------------------------------------------max
    public double getMax(int index) {
        return getLastStat(index).getMax();
        //return MyStatistics.max(getStatisticResult(index));
    }

    //--------------------------------------------------------------------------max
    public double getMedian(int index) {
        return getLastStat(index).getMedian();
    }

    //--------------------------------------------------------------------------std
    /**
     * gets the values of the statistics in one evolution simulation
     *
     * @param index index of statistic
     * @return
     */
    public double[] getStatisticResult(int index) {
        double[] values = new double[solversResult.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = solversResult.get(i)[index];
        }
        return values;
    }

    /**
     * gets the values of the statistics in the evolution
     *
     * @param stat statistic
     * @return
     */
    public double[] getStatisticResult(AbstractStatistics stat) {
        for (int i = 0; i < stats.size(); i++) {
            if (stat.getClass().getCanonicalName().equals(stats.get(i).getClass().getCanonicalName())) {
                return getStatisticResult(i);
            }
        }
        return null;
    }

    /**
     * add data to statistics
     *
     * @param data string with number separated by spaces
     */
    public void addDataToStatistics(String data) {
        try {
            String[] d = MyString.splitByWhite(data); // split string
            Double[] evol = new Double[stats.size()];
            int indexValue = 0;

            //all statistics ( evaluations, sucess, . . .)
            StatisticElement[] elem = new StatisticElement[stats.size()];
            for (int i = 0; i < stats.size(); i++) {
                elem[i] = new StatisticElement();

                elem[i].setMean(Double.parseDouble(d[indexValue++]));
                elem[i].setStd(Double.parseDouble(d[indexValue++]));
                elem[i].setMin(Double.parseDouble(d[indexValue++]));
                elem[i].setMedian(Double.parseDouble(d[indexValue++]));
                elem[i].setMax(Double.parseDouble(d[indexValue++]));
                elem[i].setQ1(Double.parseDouble(d[indexValue++]));
                elem[i].setQ3(Double.parseDouble(d[indexValue++]));
                evol[i] = elem[i].getMean();

            }
            this.evolutionGeneration.add(elem); // add statistics elements
            evolution.add(evol); // add mean to evolution
        } catch (Exception e) {
        }

    }

    /**
     * add data to statistics
     *
     * @param data string with number separated by spaces
     */
    public void addDataToResult(String line) {
        //getNumbers
        String[] nTxt = MyString.splitByWhite(line);
        //ignore first number (generation number)
        Double[] v = new Double[nTxt.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = Double.parseDouble(nTxt[i]);
        }
        solversResult.add(v);
    }

    public void updateStats(EAsolver[] arraySolvers) {
        //EAsolver[] arraySolvers = ((EAsolverArray)solver).arrayOfSolvers;
        //mean ( seted to inherited evolution )
        evolution.add(getMeanAtGeneration(arraySolvers, Integer.MAX_VALUE)); // last value
        //complete statistic
        evolutionGeneration.add(getValuesAtGeneration(arraySolvers, Integer.MAX_VALUE));// last value
        //----------------------------------------------------------------------verbose
        if (verbose) {
            System.out.println(getStatisticsData(evolution.size() - 1));
        }
        //----------------------------------------------------------------------verbose
    }

    /**
     * get a clone of the object without data
     *
     * @return
     */
    public ReportSolver getCleanClone() {
        ReportSolverArray clone = new ReportSolverArray();
        clone.path = path;
        clone.verbose = verbose;
        //clone statistics template
        for (AbstractStatistics s : stats) {
            AbstractStatistics sClone = (AbstractStatistics) s.getClone();
            sClone.setSolver(solver);
            clone.stats.add(sClone);
        }
        return clone;
    }

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
        //  public ArrayList<StatisticElement[]> evolutionGeneration = new ArrayList<>(); // Result of statistics in each generation
        //  remove values from  evolutionGeneration
        ArrayList<StatisticElement[]> newEvolutionGeneration = new ArrayList<>(); // evolution of each statistic 
        for (int generation = 0; generation < evolutionGeneration.size(); generation++) {
            StatisticElement[] gen = new StatisticElement[validIndexes.size()];
            int index = 0;//pointer to newEvolutionGeneration
            for (int indexStat = 0; indexStat < evolutionGeneration.get(generation).length; indexStat++) {
                if (validIndexes.contains(indexStat)) {
                    gen[index++] = evolutionGeneration.get(generation)[indexStat];
                }
            }
            newEvolutionGeneration.add(gen);
        }
        // public ArrayList<Double[]> solversResult = new ArrayList<>(); // Result of statistics in last generation of each solver
        //remove values from last generation
        ArrayList<Double[]> newSolversResult = new ArrayList<>();
        for (int indexStat = 0; indexStat < solversResult.size(); indexStat++) {
            if (validIndexes.contains(indexStat)) {
                newSolversResult.add(solversResult.get(indexStat));
            }
        }
        //update references to new values        
        this.evolutionGeneration = newEvolutionGeneration;
        this.solversResult = newSolversResult;

        //remove from base report      
        super.updateStatisticsTemplates(templateStats);

        //update array of solvers
        EAsolverArray array = (EAsolverArray) this.solver;
        for (EAsolver solver : array.arrayOfSolvers) {
            solver.report.updateStatisticsTemplates(templateStats);
        }

    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void setPath(String path) {
        if (this.solver == null) {
            return;
        }
        this.path = path;
        EAsolverArray array = (EAsolverArray) this.solver;
        if (array.arrayOfSolvers != null) {
            for (EAsolver solver : array.arrayOfSolvers) {
                solver.report.setPath(path + solver.getSolverName() + "/");
            }
        }
    }

    @Override
    public void redimStatisticsEvolution(int newSize) {
        if (evolution.size() <= newSize) {
            return;
        }
        super.redimStatisticsEvolution(newSize);
        ArrayList<StatisticElement[]> smallEvolGen = new ArrayList<>(); // Result of statistics in each generation        
        //step of iterator in the evolutions array
        double step = (double) evolutionGeneration.size() / (double) newSize;
        for (int i = 0; i < newSize - 1; i++) { // -1 = last one is seted            
            smallEvolGen.add(evolutionGeneration.get((int) (i * step)));
        }
        //add last record
        smallEvolGen.add(evolutionGeneration.get(evolutionGeneration.size() - 1));
        evolutionGeneration = smallEvolGen;
        for (EAsolver solver : ((EAsolverArray) solver).arrayOfSolvers) {
            solver.report.redimStatisticsEvolution(newSize);
        }
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603231421L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        EAsolver s = new GA();
        s.stop = new OptimumFound();
        s.numberOfRun = 4;
        s = new EAsolverArray(s);
        
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
}
