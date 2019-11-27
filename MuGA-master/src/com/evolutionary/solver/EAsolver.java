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
package com.evolutionary.solver;

import com.evolutionary.solverUtils.FileSolver;
import com.evolutionary.Genetic;
import com.evolutionary.operator.GeneticOperator;
import com.evolutionary.operator.mutation.DefaultMutation;
import com.evolutionary.operator.mutation.real.Gauss;
import com.evolutionary.operator.recombination.DefaultRecombination;
import com.evolutionary.operator.recombination.real.AX;
import com.evolutionary.operator.replacement.Gerational;

import com.evolutionary.operator.rescaling.AdaptiveCeiling;
import com.evolutionary.operator.rescaling.NoRescaling;
import com.evolutionary.operator.selection.MTournamentSelection;

import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.bits.OneMax;
import com.evolutionary.problem.real.CEC2008.F01_Sphere;

import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.stopCriteria.MaxEvaluations;
import com.evolutionary.stopCriteria.StopCriteria;
import com.utils.MyFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 3/out/2015, 12:39:44
 *
 * @author zulu - computer
 */
public abstract class EAsolver extends Genetic {

    public abstract void iterate(); //performs an iteration of algorithm

    protected String solverName = this.getClass().getSimpleName(); // name of the solver
    public long randomSeed = 123; // seed to random   0 => random
    public int numberOfRun = 1; // number of runs
    public int numEvaluations = 0; // number of evaluations (updated by genetic operators)
    public int numGeneration = 0;  // number of generations (updated by iterate)

    public Individual problem = new OneMax();// problem to solve
    // Genetic operators
    public GeneticOperator selection = new MTournamentSelection();
    public GeneticOperator recombination = new DefaultRecombination();
    public GeneticOperator mutation = new DefaultMutation();
    public GeneticOperator replacement = new Gerational();
    public GeneticOperator rescaling = new AdaptiveCeiling();

    public StopCriteria stop = new MaxEvaluations((long) 5E4); // stop criteria

    public Population parents = new MultiPopulation(); // population type
    public Population selected = new MultiPopulation(); // population type

    public List<Individual> hallOfFame = new ArrayList<>(); // hall of fame
    public ReportSolver report = new ReportSolver();  // report of evolution

    public EAsolver() {
        if (!(parents instanceof MultiPopulation)) {
            rescaling = new NoRescaling();
        }
        // initialization by definition
        updateSolverAtributes();
    }

    /**
     * initialization by copy
     *
     * @param solver original
     */
    public EAsolver(EAsolver solver) {
        solverName = solver.solverName;

        numberOfRun = solver.numberOfRun;

        randomSeed = solver.randomSeed;
        random = new Random(randomSeed);

        problem = solver.problem.getClone();
        parents = solver.parents.getCleanClone();

        selection = (GeneticOperator) Genetic.getClone(solver.selection);
        recombination = (GeneticOperator) Genetic.getClone(solver.recombination);
        mutation = (GeneticOperator) Genetic.getClone(solver.mutation);
        replacement = (GeneticOperator) Genetic.getClone(solver.replacement);
        rescaling = (GeneticOperator) Genetic.getClone(solver.rescaling);
        stop = (StopCriteria) Genetic.getClone(solver.stop);
        report = solver.report.getCleanClone();

        updateSolverAtributes();

        setParameters(solver.getParameters());
    }

    /**
     * initialize all the components of the evolution
     *
     * @param verbose display messages in console ?
     */
    public void InitializeEvolution(boolean verbose) {
        this.numEvaluations = 0;
        this.numGeneration = 0;
        // init random generator
        initalizeRandomGenerator(verbose);
        //update atribute solver of operators to this
        updateSolverAtributes();
        //create random population
        parents.createRandom(problem);
        parents.evaluate();
        //start statistics
        report.startStats(this, verbose);
        //compute statistics
        updateEvolutionStats();
    }

    /**
     * initialize random generator
     */
    public void initalizeRandomGenerator(boolean verbose) {
        long seed = randomSeed;
        // init random generator
        if (randomSeed == 0) {
            seed = new Random().nextLong();
            random.setSeed(seed); // pure random
        } else {
            random.setSeed(randomSeed); // initialization by seed
        }
        if (verbose) {
            System.out.println("Random seed " + seed);
        }
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void finishEvolution() {
        //finish report
        report.finishStats();
    }

    public void solve(boolean verbose) {
        InitializeEvolution(verbose);
        while (!stop.isDone(this)) {
            iterate();
        }
        finishEvolution();
    }

    public void setParents(Population pop) {
        this.parents = pop;
        if (!pop.isEmpty()) {
            this.problem = pop.getIndividual(0);
        }
    }

    /**
     * connect solver to the inner components
     */
    public final void updateSolverAtributes() {
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //reload objects - useful to update old objects saved in file       
        problem = problem.getClone();        
        selection = selection.getClone();
        recombination = recombination.getClone();
        mutation = mutation.getClone();
        replacement = replacement.getClone();
        rescaling = rescaling.getClone();
        //reload stats
        ArrayList<AbstractStatistics> lststat= new ArrayList<>();
        for (AbstractStatistics stat : report.stats) {
            lststat.add(stat.getClone());
        }
        report.stats=lststat;
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        
        problem.setSolver(this);
        
        parents.setRandomGenerator(this.random);
        selection.setSolver(this);
        recombination.setSolver(this);
        mutation.setSolver(this);
        replacement.setSolver(this);
        rescaling.setSolver(this);
        report.setSolver(this);
    }

    public void updateEvolutionStats() {
        updateHallOfFame(parents);
        report.updateStats();
        numGeneration++;
    }

    public void updateHallOfFame(Population pop) {
        if (hallOfFame.isEmpty()) {
            Collection<Individual> best = pop.getAllBest();
            for (Individual individual : best) {
                if (!hallOfFame.contains(individual)) {
                    hallOfFame.add(individual.getClone());
                }
            }
        } else {
            //update best individuals
            Individual bestOfAllOfFame = hallOfFame.iterator().next();
            Individual populationBest = pop.getBest();
            if (populationBest.compareTo(bestOfAllOfFame) > 0) {
                //new best - clear old individuals
                hallOfFame.clear();
            }
            //insert best individuals
            if (populationBest.compareTo(bestOfAllOfFame) >= 0) {

                if (hallOfFame.size() >= pop.getPopulationSize() && !populationBest.isOptimum()) {
                    return;
                }
                Collection<Individual> best = pop.getAllBest();
                for (Individual individual : best) {
                    if (!hallOfFame.contains(individual) && hallOfFame.size() < MAX_HALL_OF_FAME) {
                        hallOfFame.add(individual.getClone());
                    }
                }
            }
        }

    }

    public String toString() {
        return FileSolver.toString(this);
    }

    public String toHtml() {
        return FileSolver.toHtml(this);
    }

    public String getInformation() {
        return "Solver Info";
    }

    public EAsolver getClone() {
        EAsolver solver = (EAsolver) getClone(this);
        solver.setSolverName(getSolverName());

        solver.numberOfRun = this.numberOfRun;

        solver.randomSeed = this.randomSeed;
        solver.random = new Random(randomSeed);

        solver.problem = problem.getClone();
        solver.parents = this.parents.getCleanClone();

        solver.selection = (GeneticOperator) Genetic.getClone(this.selection);
        solver.recombination = (GeneticOperator) Genetic.getClone(this.recombination);
        solver.mutation = (GeneticOperator) Genetic.getClone(this.mutation);
        solver.replacement = (GeneticOperator) Genetic.getClone(this.replacement);
        solver.rescaling = (GeneticOperator) Genetic.getClone(this.rescaling);
        solver.stop = (StopCriteria) Genetic.getClone(this.stop);

        solver.report = report.getCleanClone();

        solver.updateSolverAtributes();
        return solver;
    }

    public void setSolver(EAsolver other) {
        this.numberOfRun = other.numberOfRun;
        this.randomSeed = other.randomSeed;

        this.problem = other.problem.getClone();
        this.parents = other.parents.getCleanClone();

        this.selection = (GeneticOperator) Genetic.getClone(other.selection);
        this.recombination = (GeneticOperator) Genetic.getClone(other.recombination);
        this.mutation = (GeneticOperator) Genetic.getClone(other.mutation);
        this.replacement = (GeneticOperator) Genetic.getClone(other.replacement);
        this.rescaling = (GeneticOperator) Genetic.getClone(other.rescaling);
        this.stop = (StopCriteria) Genetic.getClone(other.stop);

        this.report = other.report.getCleanClone();

        this.updateSolverAtributes();

    }

    /**
     * verify if the solve is done
     *
     * @return
     */
    public boolean isDone() {
        return stop.isDone(this);
    }

    public void setSolverName(String newName) {
        solverName = newName;
    }

    /**
     * @return the solverName
     */
    public String getSolverName() {
        return solverName;
    }

    public void loadReport() {
        report.loadFromFile();
        report.solver = this;
    }

    public void loadReport(String filename) {
        report.loadFromFile(filename);
        report.solver = this;
    }

    public void save() {
        save(report.path + "/" + solverName + "." + FileSolver.FILE_EXTENSION_MUGA);
    }

    public void save(String fileName) {
        try {
            String path = MyFile.getPath(fileName);
            new File(path).mkdirs();
            String name = MyFile.getFileNameOnly(fileName);

            PrintStream configOut = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(path + name + "_config." + FileSolver.FILE_EXTENSION_CONFIG)));
            configOut.println(this.toString());
            configOut.close();

            //resize stats 
            report.redimStatisticsEvolution();

            configOut = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(path + name + "_statistics." + FileSolver.FILE_EXTENSION_CONFIG)));
            configOut.println(this.report.solverToFullString());
            configOut.close();
            //:::::::::::::::::::::::::::
            //save object
            //:::::::::::::::::::::::::::

            //save Objecto
            ObjectOutputStream fileOut = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(path + name + "." + FileSolver.FILE_EXTENSION_MUGA)));
            fileOut.writeObject(this);
            fileOut.close();

        } catch (Exception ex) {
            Logger.getLogger(EAsolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static EAsolver load(String fileName) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        return (EAsolver) in.readObject();
    }
    
    public static int MAX_HALL_OF_FAME = 1000; // maximum individuals in hall of Fame
}
