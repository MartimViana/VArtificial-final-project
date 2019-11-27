//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2017   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.solver;

import GUI.utils.MuGASystem;
import com.evolutionary.problem.real.RealVector;
import com.evolutionary.solver.differentialEvolution.DERand1Bin;
import com.evolutionary.solver.differentialEvolution.DEStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created on 27/jun/2017, 18:44:52
 *
 * @author zulu - computer
 */
public class DE extends EAsolver {

    static ArrayList<DEStrategy> strategies = DEStrategy.getStrategies();
    public static double FACTOR = -1; //random [0.5 , 1]
    public static double CROSSOVER = 0.9;
    public static int STRATEGY = 0;

    //dimension of change [0,1]  
    double F = FACTOR;
    //probability of crossover [0,1]
    double CR = CROSSOVER;
    //strategy
    DEStrategy deStrategy = new DERand1Bin();

    public EAsolver getClone() {
        DE clone = (DE) super.getClone();
        clone.F = this.F;
        clone.CR = this.CR;
        clone.deStrategy = (DEStrategy) MuGASystem.getClone(deStrategy);
        return clone;
    }

    /**
     * initialize all the components of the evolution
     *
     * @param verbose display messages in console ?
     */
    public void InitializeEvolution(boolean verbose) {
        if (!(problem instanceof RealVector)) {
            throw new RuntimeException("Differential Evolution is aplied only to RealVector");
        }
        // init random generator
        initalizeRandomGenerator(verbose);
        //update atribute solver of operators to this
        updateSolverAtributes();
        //create random population
        parents.createRandom(problem);
        parents.evaluate();
        report.startStats(this, verbose);
        updateEvolutionStats();
    }

    /**
     * get random unique indexes
     *
     * @param size number of indexes
     * @param limit index limit [0...limit-1]
     * @return array of unique indexes
     */
    public static int[] getUniqueIndex(int size, int limit, Random random) {
        int[] index = new int[size]; // array of unique indexes
        int i = 0, v;
        boolean isDuplicate;
        while (i < size) { // array not completed
            v = random.nextInt(limit); //random number
            //search for repeated value
            isDuplicate = false;
            for (int j = i - 1; j >= 0; j--) {
                if (index[j] == v) {
                    isDuplicate = true;
                    break;
                }
            }
            //not repetead
            if (!isDuplicate) {
                index[i++] = v; //add random number
            }
        }
        return index;
    }

    @Override
    public void iterate() {
        RealVector best = (RealVector) parents.getBest();
        //covert Individuals to RealVetor
        List<RealVector> pop = (List<RealVector>) (List<?>) parents.getIndividualsList();
        parents.clear();

        deStrategy.init(random); // init strategy
        for (int i = 0; i < pop.size(); i++) {
            //base vetor
            RealVector ind = pop.get(i).getClone();
            double f = F;
            double cr = CR;
            if (f < 0) {
                f = 0.1 + random.nextDouble();
            }
            if (cr < 0) {
                cr = random.nextDouble();
            }

            evolve(deStrategy, f, cr, ind, best, pop);

            if (ind.compareTo(pop.get(i)) < 0) {
                parents.addIndividual(pop.get(i));
            } else {
                parents.addIndividual(ind);
            }

        }
        updateEvolutionStats();
    }

    public static void evolve(DEStrategy deStrategy, double F, double CR, RealVector currentInd, RealVector bestInd, List<RealVector> pop) {
        try {
            //base vetor
            double[] x = currentInd.getGenome();
            //select individuals
            int[] index = getUniqueIndex(4, pop.size(), currentInd.random);
            double[][] g = new double[4][];
            g[0] = pop.get(index[0]).getGenome();
            g[1] = pop.get(index[1]).getGenome();
            g[2] = pop.get(index[2]).getGenome();
            g[3] = pop.get(index[3]).getGenome();
            double[] best = bestInd.getGenome();
            deStrategy.apply(F, CR, x.length, x, best, g);
            currentInd.setDoubleValues(x);
            currentInd.evaluate();
        } catch (Exception e) {
            evolve(deStrategy, F, CR, currentInd, bestInd, pop);
        }

    }

    public String getInformation() {
        StringBuilder str = new StringBuilder();
        str.append("\nDifferential Evolution");
        str.append("\n 1 - create POP");
        str.append("\n 2 - evaluate POP");
        str.append("\n 3 - until STOP criteria");
        str.append("\n    3.1 - Select base vector (x)");
        str.append("\n    3.2 - Select difference vector (d) (x1-x2, . . .)");
        str.append("\n    3.3 - Generate  m = x + F * d");
        str.append("\n    3.4 - Select individual v");
        str.append("\n    3.4 - u = Recombine m and v");
        str.append("\n    3.5 - Select between u and v");

        str.append("\n\nParameters: F CR S");
        str.append("\n    F  :Mutation Factor [0,1]  <-1> = random [0.5 .. 1]");
        str.append("\n    CR : Cossover Factor[0,1] ");
        str.append("\n    S  : number o DE strategie[0," + strategies.size() + "]");
        str.append("\n\nStrategies:");
        for (int i = 0; i < strategies.size(); i++) {
            str.append("\n").append(i).append(" - ").append(strategies.get(i).getClass().getSimpleName());
        }
        return str.toString();
    }

    @Override
    public String getParameters() {
        return F + " " + CR + " " + strategies.indexOf(deStrategy);
    }

    @Override
    public void setParameters(String str) {
        if (str == null || str.trim().length() == 0) {
            return;
        }
        StringTokenizer par = new StringTokenizer(str);
        try {
            FACTOR = Double.parseDouble(par.nextToken());
        } catch (Exception e) {
            FACTOR = -1;
        }
        try {
            CROSSOVER = Double.parseDouble(par.nextToken());
        } catch (Exception e) {
            CROSSOVER = 0.9;
        }
        try {
            STRATEGY = Integer.parseInt(par.nextToken());
        } catch (Exception e) {
            STRATEGY = 0;
        }
        if (STRATEGY < 0 || STRATEGY >= strategies.size()) {
            STRATEGY = 0;
        }

        this.F = FACTOR;
        this.CR = CROSSOVER;
        this.deStrategy = (DEStrategy) MuGASystem.getClone(strategies.get(STRATEGY));
    }

}
