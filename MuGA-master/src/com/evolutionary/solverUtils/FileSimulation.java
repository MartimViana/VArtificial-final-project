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
import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.stopCriteria.StopCriteria;
import com.utils.MyFile;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Created on 26/mar/2016, 10:16:15
 *
 * @author zulu - computer
 */
public class FileSimulation extends FileSolver {

    public static final String SIMULATION_FILE = "simulation file";

    public static ArrayList<EAsolver> loadSimulation(String path) throws Exception {
        //::::::::::::::::::: READ OBJ ::::::::::::::::::::::::::::::::::::::::::
        if (path.endsWith("." + FileSolver.FILE_EXTENSION_MUGA)) {
            ArrayList<EAsolver> list = new ArrayList<>();
            System.err.println("Loading Object " + path);
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
            Object obj = in.readObject();
            if (obj instanceof EAExperiment) {
                list.addAll(((EAExperiment) obj).solvers);
            } else if (obj instanceof ReportSolver) {
                list.add(((ReportSolver) obj).solver);
            } else if (obj instanceof EAsolver) {
                list.add((EAsolver) obj);
            }
            for (EAsolver eAsolver : list) {
                eAsolver.updateSolverAtributes();
                System.err.println("\t " + eAsolver.getClass().getSimpleName() + " Loaded " + eAsolver.getSolverName() + " ( " + eAsolver.numberOfRun + " )");
            }
            return list;
        }
        //::::::::::::::::::: READ TXT ::::::::::::::::::::::::::::::::::::::::::
        System.err.println("Loading TXT " + path);
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
    public static ArrayList<Genetic> getGenetic(Collection<String> txtGenetic) {
        ArrayList<Genetic> geneticElems = new ArrayList<>();
        for (String geneticElem : txtGenetic) {
            try {
                Genetic g = Genetic.createNew(geneticElem);
                geneticElems.add(g);
            } catch (Exception e) {
            }
        }
        return geneticElems;
    }

    public static String getName(Genetic g, Map<String, String> map) {
        return map.get(g.getCanonicalFullDefinition()) != null ? map.get(g.getCanonicalFullDefinition()) : "";
    }

    public static ArrayList<EAsolver> loadtxtSimulation(String txtFile, String simulFileName) {
        String path = MyFile.getPath(simulFileName);
        //defined solvers
        Map<String, String> txtSolvers = loadGeneticWithNames(txtFile, SOLVER_TYPE, SOLVER_TYPE_LIB);
        ArrayList<Genetic> solvers = getGenetic(txtSolvers.keySet());
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
        Map<String, String> txtStop = loadGeneticWithNames(txtFile, STOP_CRITERIA, STOP_CRITERIA_LIB);
        ArrayList<Genetic> stop = getGenetic(txtStop.keySet());
        //--------------------------- ARRAY of Possibilities -------------------------------- 
        Map<String, String> txtProblems = loadGeneticWithNames(txtFile, PROBLEM_NAME, PROBLEM_NAME_LIB);
        ArrayList<Genetic> problems = getGenetic(txtProblems.keySet());

        Map<String, String> txtPopulations = loadGeneticWithNames(txtFile, POPULATION_TYPE, POPULATION_TYPE_LIB);
        ArrayList<Genetic> populations = getGenetic(txtPopulations.keySet());

        Map<String, String> txtSelections = loadGeneticWithNames(txtFile, OPERATOR_SELECTION, OPERATOR_SELECTION_LIB);
        ArrayList<Genetic> selections = getGenetic(txtSelections.keySet());

        Map<String, String> txtRecombinations = loadGeneticWithNames(txtFile, OPERATOR_RECOMBINATION, OPERATOR_RECOMBINATION_LIB);
        ArrayList<Genetic> recombinations = getGenetic(txtRecombinations.keySet());

        Map<String, String> txtMutations = loadGeneticWithNames(txtFile, OPERATOR_MUTATION, OPERATOR_MUTATION_LIB);
        ArrayList<Genetic> mutations = getGenetic(txtMutations.keySet());

        Map<String, String> txtReplacement = loadGeneticWithNames(txtFile, OPERATOR_REPLACEMENT, OPERATOR_REPLACEMENT_LIB);
        ArrayList<Genetic> replacement = getGenetic(txtReplacement.keySet());

        Map<String, String> txtRescaling = loadGeneticWithNames(txtFile, OPERATOR_RESCALING, OPERATOR_RESCALING_LIB);
        ArrayList<Genetic> rescaling = getGenetic(txtRescaling.keySet());

        Map<String, String> txtStatistics = loadGeneticWithNames(txtFile, STATISTIC, STATISTIC_LIB);
        ArrayList<Genetic> statistics = getGenetic(txtStatistics.keySet());
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
            //get stop critera
            if (!stop.isEmpty()) {
                solver.stop = (StopCriteria) stop.get(0);
            }
            // other combinations
            if (lst.get(1) != null) {
                solver.problem = ((Individual) lst.get(1)).getClone();
            }
            if (lst.get(2) != null) {
                solver.parents = ((Population) lst.get(2)).getCleanClone();
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

            solver.randomSeed = randomSeed;
            //update name of the solver
            solver.setSolverName(solverName);
            if (perms.size() > 1) {
                solver.setSolverName(solverName + "_" + String.format("%03d", i + 1));// MyNumber.convertIntToAlphabet(i));
            }

            //Try to load Simulation
            //------------------------------------------------------------------
            solver.report.setFileName(path + solver.getSolverName()); // update path of report
            solver.numberOfRun = numberOfRun;

            //convert to array solver
            if (solver.numberOfRun > 1) {
                solver = new EAsolverArray(solver);
            }

            //link atributes of solver
            solver.updateSolverAtributes();
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
            s = FileSolver.loadEvolution(s, txtSim);
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
            // information about evolution ?
            if (line.startsWith(STATISTIC_KEY) || line.startsWith(SOLVER_KEY)) {
                break;
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
