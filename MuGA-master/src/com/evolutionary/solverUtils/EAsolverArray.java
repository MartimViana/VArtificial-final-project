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
import com.evolutionary.population.MultiPopulation;
import com.evolutionary.problem.Individual;
import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.ReportSolverArray;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import com.evolutionary.stopCriteria.StopCriteria;
import com.utils.MyString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Solver array is an solver that contains array of solvers Template solver
 * conatains the atribute template.numberOfRun that produces an individual
 * solver.
 *
 * Report of this solver contains the statistics of the array of solvers
 *
 *
 * Created on 30/mar/2016, 7:55:17
 *
 * @author zulu - computer
 */
public class EAsolverArray extends EAsolver {

    public static int DEFAULT_SIZE = 8;

    public EAsolver[] arrayOfSolvers = null; // array of solver 
    public EAsolver template = null; // template solver

    public EAsolverArray() {
    }

    public EAsolverArray(EAsolverArray template) {
        this(template.template);
    }

    public EAsolverArray(EAsolver template) {
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::        
        //copy information from template
        this.template = template;
        //update number of runs
        if (template.numberOfRun < 2) {
            template.numberOfRun = DEFAULT_SIZE;
        }
        this.numberOfRun = template.numberOfRun;

        this.randomSeed = template.randomSeed;
        // init random generator
        this.random = new Random(randomSeed);
        this.problem = template.problem.getClone();
        this.parents = template.parents.getCleanClone();

        this.selection = (GeneticOperator) Genetic.getClone(template.selection);
        this.recombination = (GeneticOperator) Genetic.getClone(template.recombination);
        this.mutation = (GeneticOperator) Genetic.getClone(template.mutation);
        this.replacement = (GeneticOperator) Genetic.getClone(template.replacement);
        this.rescaling = (GeneticOperator) Genetic.getClone(template.rescaling);
        this.stop = (StopCriteria) Genetic.getClone(template.stop);
        //new type of report 
        this.report = new ReportSolverArray(template);
        this.updateSolverAtributes();
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        arrayOfSolvers = createArrayOfSolvers(template);
        //update names of solver
        setSolverName(template.getSolverName());
    }

    @Override
    public void setSolverName(String newName) {
        solverName = newName;
        template.setSolverName(newName);
        for (int i = 0; i < arrayOfSolvers.length; i++) {
            arrayOfSolvers[i].setSolverName(template.getSolverName() + String.format("_%03d", i));
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::: S O L V E R   A R R A Y     ::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * creates an array of solvers based on a template random seeds of solvers
     * are generate by random object of templeta
     *
     * @param template solver to clonate
     * @return array of smart cloned templates
     */
    public static EAsolver[] createArrayOfSolvers(EAsolver template) {
        EAsolver[] arrayOfSolvers = new EAsolver[template.numberOfRun];
        //initialize solver
        template.initalizeRandomGenerator(false);
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //build solvers
        for (int i = 0; i < arrayOfSolvers.length; i++) {
            arrayOfSolvers[i] = template.getClone();
            //one run
            arrayOfSolvers[i].numberOfRun = 1;
            // random seed initialized by template.random
            arrayOfSolvers[i].randomSeed = template.random.nextLong();
            arrayOfSolvers[i].updateSolverAtributes();
            //create simple report
            arrayOfSolvers[i].report = new ReportSolver();
            //update solver file Name
            arrayOfSolvers[i].report.setFileName(
                    ReportSolverArray.getReportFileName(template.report.getFileName(), template.getSolverName(), i));

            arrayOfSolvers[i].updateSolverAtributes();
        }
        return arrayOfSolvers;
    }

    /**
     * solve the array of solvers in parallell
     *
     * @param verbose
     */
    public void solve(boolean verbose) {
        InitializeEvolution(verbose); // reset solvers
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //running solvers
        ThreadRunSolver[] thr = new ThreadRunSolver[arrayOfSolvers.length];
        for (int i = 0; i < arrayOfSolvers.length; i++) {
            final int index = i;
            thr[i] = new ThreadRunSolver(arrayOfSolvers[index]);
            thr[i].start();
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //waiting to solvers finish
        for (Thread thread : thr) {
            try {
                thread.join();
            } catch (Throwable ex) {
                Logger.getLogger(EAsolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //save report
        //report = new ReportSolverArray(this);
        ((ReportSolverArray) report).updateEvolutionStatistics(arrayOfSolvers);

        if (verbose) {
            System.out.println(((ReportSolverArray) report).getArrayEvolutionString());
        }
        System.out.println(MyString.toString(new Date()) + " : Simulation " + report.getFileName() + " Complete!");

    }

    /**
     * evolve Array of solvers solvers in sequencial (no MultiThread)
     */
    @Override
    public void iterate() {
        this.numEvaluations = 0;
        this.numGeneration = 0;
        MultiPopulation multiPopHF = new MultiPopulation();
        Individual best = problem; // template problem

        //evolve all solvers
        for (EAsolver s : arrayOfSolvers) {
            if (!s.stop.isDone(s)) {
                s.iterate(); // iterate solver  
                updateHallOfFame(s.parents);
            }
            //::::::::::::: HALL OF FAME :::::::::::::::::::
            List<Individual> hf = s.hallOfFame;
            if (best.compareTo(hf.get(0)) < 0) { // new best
                best = hf.get(0);
                multiPopHF.clear();
            }
            if (best.compareTo(hf.get(0)) == 0 && multiPopHF.getPopulationSize() < MAX_HALL_OF_FAME*5) {
                multiPopHF.addAll(s.hallOfFame);
            }
            //::::::::::::: HALL OF FAME :::::::::::::::::::

            this.numEvaluations += s.numEvaluations;
            this.numGeneration += s.numGeneration;

        }
        this.numEvaluations /= arrayOfSolvers.length;
        this.numGeneration /= arrayOfSolvers.length;

        this.parents.clear();
        parents.addAll(multiPopHF.getGenomes());
        this.hallOfFame = multiPopHF.getGenomes();
        //update stastistics
        ((ReportSolverArray) report).updateStats(arrayOfSolvers);
    }

    /**
     * evolve Array of solvers solvers in sequencial (no MultiThread)
     */
    public void iterateParallel() {
        this.numEvaluations = 0;
        this.numGeneration = 0;
        parents.clear();
        MultiPopulation multiPopHF = new MultiPopulation();
        Individual best = problem; // template problem
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //running solvers
        ThreadIterateSolver[] thr = new ThreadIterateSolver[arrayOfSolvers.length];
        for (int i = 0; i < arrayOfSolvers.length; i++) {
            final int index = i;
            thr[i] = new ThreadIterateSolver(arrayOfSolvers[index]);
            if (!arrayOfSolvers[index].stop.isDone(arrayOfSolvers[index])) {
                thr[i].start();
            }
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //waiting to solvers finish
        for (ThreadIterateSolver thread : thr) {
            try {
                thread.join();

                //::::::::::::: HALL OF FAME :::::::::::::::::::
                List<Individual> hf = thread.solver.hallOfFame;
                if (best.compareTo(hf.get(0)) < 0) { // new best
                    best = hf.get(0);
                    multiPopHF.clear();
                }
                if (best.compareTo(hf.get(0)) == 0 && multiPopHF.getPopulationSize() < MAX_HALL_OF_FAME * 5) {
                    multiPopHF.addAll(thread.solver.hallOfFame);
                }
                //::::::::::::: HALL OF FAME :::::::::::::::::::

                this.numEvaluations += thread.solver.numEvaluations;
                this.numGeneration += thread.solver.numGeneration;

            } catch (Throwable ex) {
                Logger.getLogger(EAsolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.hallOfFame = multiPopHF.getGenomes();
        parents.addAll(multiPopHF.getGenomes());
        this.numEvaluations /= arrayOfSolvers.length;
        this.numGeneration /= arrayOfSolvers.length;
        //update stastistics
        ((ReportSolverArray) report).updateStats(arrayOfSolvers);
    }

    /**
     * verify if the solve is done
     *
     * @return
     */
    public boolean isDone() {
        for (EAsolver s : arrayOfSolvers) {
            if (!s.stop.isDone(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * start sequencial evolution
     *
     * @param verbose
     */
    public void InitializeEvolution(boolean verbose) {
        //initalize random generator
        initalizeRandomGenerator();
        //arrayOfSolvers = createArrayOfSolvers(this);        
        //create random population
        parents = parents.getCleanClone();
        parents.createRandom(problem);
        parents.evaluate();

        for (EAsolver s : arrayOfSolvers) {
            s.report.setStatistics(report.getStatistics());
            s.InitializeEvolution(false); // solvers runs in silence
        }
        //start statistics
        report.startStats(this, false);// solvers runs in silence        
        updateEvolutionStats();
    }

    public void initalizeRandomGenerator() {
        super.initalizeRandomGenerator(false);
        // init random generator
        for (EAsolver s : arrayOfSolvers) {
            s.randomSeed = random.nextLong();
            s.initalizeRandomGenerator(false);
        }
    }

    @Override
    public void updateEvolutionStats() {

        //parents contains haal of fame of solvers
        parents.clear();
        for (EAsolver s : arrayOfSolvers) {
            updateHallOfFame(s.parents);
            parents.addAll(s.hallOfFame);
        }
        report.updateStats();

    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::: T H R E A D  ::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private class ThreadRunSolver extends Thread {

        EAsolver solver;

        public ThreadRunSolver(EAsolver solver) {
            this.solver = solver;
        }

        public void run() {
            System.out.println("Start " + Thread.currentThread().getName() + " -> " + solver.getSolverName());
            solver.random.setSeed(solver.randomSeed);
            solver.solve(solver.report.verbose);
            System.out.println("Stop " + Thread.currentThread().getName() + " -> " + solver.getSolverName());
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::: T H R E A D  ::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private class ThreadIterateSolver extends Thread {

        EAsolver solver;

        public ThreadIterateSolver(EAsolver solver) {
            this.solver = solver;
        }

        public void run() {
            solver.iterate();
        }
    }
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public EAsolver getClone() {
        EAsolverArray solver = (EAsolverArray) super.getClone();
        solver.template = this.template;
        solver.arrayOfSolvers = new EAsolver[arrayOfSolvers.length];
        //clone array of solvers
        for (int i = 0; i < arrayOfSolvers.length; i++) {
            solver.arrayOfSolvers[i] = arrayOfSolvers[i].getClone();
        }
        return solver;
    }

    public void loadReport() {
        report.loadFromFile();
        ReportSolverArray ra = (ReportSolverArray) report;
        for (int i = 0; i < arrayOfSolvers.length; i++) {
            arrayOfSolvers[i].loadReport(ra.getFileName(i));
        }
        report.solver = this;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603300755L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        //EAsolver g = new Crowding();
        EAsolver g = new GA();
        g.numberOfRun = 10;
        EAsolverArray a = new EAsolverArray(g);
        EAsolver clone = a.getClone();
        clone.setSolverName("Clone_CROWDING");
        clone.solve(true);
        a.solve(true);
    }

}
