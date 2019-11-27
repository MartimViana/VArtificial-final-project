//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2019   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.problem.bits.knapsack;

import java.util.Locale;
import java.util.Random;

/**
 * Created on 24/fev/2019, 6:16:58
 *
 * @author zulu - computer
 */
public class KnapSack extends AbstractKnapSack {

    public static long RANDOM_SEED = 11; // to make the same problems
    protected static double[] WEIGHT;
    protected static double[] PROFIT;
    protected static double SACK_CAPACITY;
    protected static double BEST_VALUE;

    static {
        generateProblem(128, 0.5);
    }

    protected double[] weight;
    protected double[] profit;
    protected double sackCapacity;
    protected double bestValue;

    public KnapSack() {
        super(WEIGHT.length);
        this.weight = WEIGHT;
        this.profit = PROFIT;
        this.sackCapacity = SACK_CAPACITY;
        this.bestValue = BEST_VALUE;

    }

    public KnapSack(KnapSack k) {
        super(k);
        this.weight = k.weight;
        this.profit = k.profit;
        this.sackCapacity = k.sackCapacity;
        this.bestValue = k.bestValue;
    }

    @Override
    public double[] getWeight() {
        return this.weight;
    }

    @Override
    public double[] getProfit() {
        return this.profit;
    }

    @Override
    public double getSackCapacity() {
        return this.sackCapacity;
    }

    @Override
    protected double evaluate(boolean[] genome) {
        return fitnessLinearPenalty(weight, profit, sackCapacity);
    }

    @Override
    public boolean isOptimum() {
        return getFitness() == bestValue;
    }

    @Override
    public String getInformation() {
        return getInformation(weight, profit, bestValue, sackCapacity);
    }

    public String toStringPhenotype() {//--------------------------------------- textual representation of phenotype
        double percent = (weightOfSack(weight) * 100.0) / sackCapacity;
        return toStringGenome() + String.format("%8.2f", percent) + "%";
    }

    /**
     * update the size of the genome
     *
     * @param params height of the tree ( power of 2 )
     */
    @Override
    public void setParameters(String params) {

        String p[] = params.split(" ");
        int param_SIZE = weight.length;
        double param_PERCENTAGE = +sackCapacity / sumOfAllWeights(weight);
        try {
            param_SIZE = Integer.parseInt(p[0]);
        } catch (Exception e) {
        }
        try {
            param_PERCENTAGE = Double.parseDouble(p[1]);
            param_PERCENTAGE = param_PERCENTAGE > 1 ? 0.5 : param_PERCENTAGE; // maximum 1
            param_PERCENTAGE = param_PERCENTAGE < 0 ? 0.5 : param_PERCENTAGE; // maximum 1
        } catch (Exception e) {
        }
        generateProblem(param_SIZE, param_PERCENTAGE);
        this.genome = new boolean[param_SIZE];
        this.weight = WEIGHT;
        this.profit = PROFIT;
        this.sackCapacity = SACK_CAPACITY;
        this.bestValue = BEST_VALUE;
    }

    public static void generateProblem(int size, double capacity) {
        //introduces size and capacity in random sedd
        Random rnd = new Random((long) (RANDOM_SEED * size * capacity));
        WEIGHT = new double[size];
        PROFIT = new double[size];
        for (int i = 0; i < size; i++) {
            WEIGHT[i] = rnd.nextInt(size) + 1;
            //strongly correlated
            PROFIT[i] = WEIGHT[i] + rnd.nextInt((int) (WEIGHT[i] / 10 + 1));
        }
        SACK_CAPACITY = (int) (sumOfAllWeights(WEIGHT) * capacity);
        BEST_VALUE = KnapSackBestValue.getBestknapSack01Value(SACK_CAPACITY, WEIGHT, PROFIT);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.US, "%d %4.2f ", weight.length, sackCapacity / sumOfAllWeights(weight));
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201902240616L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
