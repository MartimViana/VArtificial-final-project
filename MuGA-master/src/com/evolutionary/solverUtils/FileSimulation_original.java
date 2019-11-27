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
import com.evolutionary.operator.GeneticOperator;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.stopCriteria.StopCriteria;
import com.utils.MyFile;
import com.utils.MyNumber;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created on 26/mar/2016, 10:16:15
 *
 * @author zulu - computer
 */
public class FileSimulation_original extends FileSolver {

    public static final String SIMULATION_FILE = "simulation file";

    public static ArrayList<EAsolver> loadSimulation(String path) {
        String myPath = MyFile.getPath(path);
        String myFile = MyFile.getFileName(path);
        String simulFileName = myPath + myFile;
        String txtFile = MyFile.readFile(path);
        if (txtFile == null) {
            //System.out.println("No Simulation loaded!");
            return new ArrayList<EAsolver>();
        }
        ArrayList<EAsolver> sim = loadtxtSimulation(txtFile, simulFileName);
        ArrayList<EAsolver> files = loadFile(txtFile, myPath);
        sim.addAll(files);
        return sim;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::  L O A D   S I M U L A T I O N  F I L E   ::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static ArrayList<EAsolver> loadtxtSimulation(String txtFile, String simulFileName) {
        //defined solvers
        ArrayList<Genetic> solvers = loadGenetic(txtFile, SOLVER_TYPE, SOLVER_TYPE_LIB);
        //no defined solvers
        if (solvers.isEmpty()) {
            return new ArrayList<EAsolver>();
        }
        //---------------------------------------------------------------------------
        //read information about solvers
        //---------------------------------------------------------------------------
        String solverName = loadString(txtFile, SOLVER_NAME);

        //random seed
        long randomSeed = loadInteger(txtFile, RANDOM_SEED);
        //random generator
        Random rnd = randomSeed != 0 ? new Random(randomSeed) : new Random();
        //number of runs
        int numberOfRun = (int) loadInteger(txtFile, NUMBER_OF_RUNS);
        numberOfRun = numberOfRun > 0 ? numberOfRun : 1;
        //stop simulation
        ArrayList<Genetic> stop = loadGenetic(txtFile, STOP_CRITERIA, STOP_CRITERIA_LIB);
        //--------------------------- ARRAY of Possibilities -------------------------------- 
        ArrayList<Genetic> problems = loadGenetic(txtFile, PROBLEM_NAME, PROBLEM_NAME_LIB);
        ArrayList<Genetic> populations = loadGenetic(txtFile, POPULATION_TYPE, POPULATION_TYPE_LIB);
        ArrayList<Genetic> selections = loadGenetic(txtFile, OPERATOR_SELECTION, OPERATOR_SELECTION_LIB);
        ArrayList<Genetic> recombinations = loadGenetic(txtFile, OPERATOR_RECOMBINATION, OPERATOR_RECOMBINATION_LIB);
        ArrayList<Genetic> mutations = loadGenetic(txtFile, OPERATOR_MUTATION, OPERATOR_MUTATION_LIB);
        ArrayList<Genetic> replacement = loadGenetic(txtFile, OPERATOR_REPLACEMENT, OPERATOR_REPLACEMENT_LIB);
        ArrayList<Genetic> rescaling = loadGenetic(txtFile, OPERATOR_RESCALING, OPERATOR_RESCALING_LIB);
        ArrayList<Genetic> statistics = loadGenetic(txtFile, STATISTIC, STATISTIC_LIB);
        //-----------------------------------------------------------------------------------
        // Generate array of solvers
        //-----------------------------------------------------------------------------------
        ArrayList<List> all = new ArrayList<>();
        all.add(solvers);
        all.add(problems);
        all.add(populations);
        all.add(selections);
        all.add(recombinations);
        all.add(mutations);
        all.add(replacement);
        all.add(rescaling);
        //-------------------------------------------------------------------------------------
        //calculate list of all combinations  
        List<List> perms = combinations(all);
        //generate solvers 
        ArrayList<EAsolver> allSolvers = new ArrayList<>();

        for (int i = 0; i < perms.size(); i++) {
            List lst = perms.get(i);
            //0 - solver 
            EAsolver solver = ((EAsolver) lst.get(0)).getClone();
            //update name of the solver
            solver.setSolverName(solverName);
            if (perms.size() > 1) {
                solver.setSolverName(solver.getSolverName() + "_" + MyNumber.convertIntToAlphabet(i));
            }

            // other combinations
            if (lst.get(1) != null) {
                solver.problem = ((Individual) lst.get(1)).getClone();
            }
            if (lst.get(2) != null) {
                solver.parents = ((Population) lst.get(2)).getClone();
            }
            if (lst.get(3) != null) {
                solver.selection = ((GeneticOperator) lst.get(3)).getClone();
            }
            if (lst.get(4) != null) {
                solver.recombination = ((GeneticOperator) lst.get(4)).getClone();
            }
            if (lst.get(5) != null) {
                solver.mutation = ((GeneticOperator) lst.get(5)).getClone();
            }
            if (lst.get(6) != null) {
                solver.replacement = ((GeneticOperator) lst.get(6)).getClone();
            }
            if (lst.get(7) != null) {
                solver.rescaling = ((GeneticOperator) lst.get(7)).getClone();
            }
            //add AbstractStatistics
            for (Genetic stat : statistics) {
                solver.report.addStatistic((AbstractStatistics) stat);
            }
            //::-----------------------------------------------------------------
            //-------------------------- update properties -----  

            //update randomSeed
            if (perms.size() > 1) { // multiple solvers
                solver.randomSeed = rnd.nextLong(); // generate new random seed to simulation
            } else { //one solver
                solver.randomSeed = randomSeed; // random seed of the file
            }
            //get stop critera
            if (!stop.isEmpty()) {
                solver.stop = (StopCriteria) stop.get(0);
            }
           
            //Try to load Simulation
            //------------------------------------------------------------------
            solver.report.setFileName(simulFileName); // update path of report
            solver.numberOfRun = numberOfRun;

            //convert to array solver
            if (solver.numberOfRun > 1) {
                solver = new EAsolverArray(solver);
            }
            
             //link atributes of solver
            solver.updateSolverAtributes();

            //try to load solver
            EAsolver file = FileSolver.load(solver.report.getFileName());
            if (file != null) {
                solver.report = file.report;
            }
            //------------------------------------------------------------------
            //::-----------------------------------------------------------------
            allSolvers.add(solver);
        }
        return allSolvers;
    }

    public static ArrayList<EAsolver> loadFile(String txt, String path) {
        List<String> files = loadFileContents(txt, SIMULATION_FILE, path);
        ArrayList<EAsolver> allSolvers = new ArrayList<>();
        for (String txtSolver : files) {
            String txtSim = MyFile.readFile(txtSolver);
            EAsolver s = FileSolver.loadSolver(txtSim, txtSolver);
            allSolvers.add(s);
        }
        return allSolvers;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::  L O A D   F I L E S       :::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static List<String> loadFileContents(String txt, String KEY, String path) {
        List<String> files = new ArrayList<>();
        Scanner file = new Scanner(txt);
        String line;
        while (file.hasNext()) {
            line = file.nextLine().trim();
            if (line.startsWith(KEY)) {
                line = line.substring(line.indexOf("=") + 1).trim();
                try {
                    File f = new File(path + line);
                    if (f.exists()) {
                        files.add(f.getAbsolutePath());
                    }
                } catch (Exception e) {
                }
            }
        }
        return files;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::: C O M B I N A T I O N S ::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    static List<List> all = new ArrayList();
    static List<List> data;

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
    public static synchronized List<List> combinations(List<List> list) {
        all = new ArrayList();
        data = list;
        doCombine(new ArrayList(), 0);
        return all;
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
    private static void doCombine(List current, int index) {
        //end 
        if (index == data.size()) {
            all.add(current);
        } else if (data.get(index).isEmpty()) { // list empty
            current.add(null);
            doCombine(current, index + 1);
        } else {
            for (Object object : data.get(index)) { //iterate all elements
                List copy = new ArrayList(current); // clone actual list
                copy.add(object);
                doCombine(copy, index + 1);//combine next
            }
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603261016L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        EAExperiment ex = new EAExperiment();
        ex.loadSolver("simulation/sim1.txt");
        ex.solve(false);
        ex.saveAs("simulation/result2/files.txt");
    }
}
