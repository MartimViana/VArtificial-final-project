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

package com.evolutionary.problem.bits.knapsack;

import com.evolutionary.problem.Individual;

import com.utils.problems.KnapSack_DP;

/**
 * Created on 28/jan/2017, 18:26:34
 *
 * @author zulu - computer
 */
public class K100 extends AbstractKnapSack {

    private static final double W[] = {
        24, 8, 8, 38, 33, 40, 50, 21, 24, 15, 33, 45, 16, 22, 7, 32, 33, 33, 16, 14,
        23, 25, 33, 14, 7, 32, 36, 46, 11, 50, 26, 38, 46, 49, 7, 36, 26, 19, 6, 38,
        10, 18, 45, 9, 50, 47, 38, 15, 42, 11, 24, 9, 49, 2, 13, 44, 31, 40, 28, 35,
        32, 5, 39, 7, 43, 35, 23, 48, 28, 8, 26, 4, 21, 47, 33, 2, 42, 11, 15, 49,
        16, 44, 12, 33, 33, 22, 15, 14, 28, 39, 31, 30, 37, 19, 14, 33, 39, 10, 46, 32};
    private static final double P[] = {
        26, 10, 13, 39, 34, 43, 52, 24, 27, 17, 35, 47, 18, 25, 8, 35, 36, 36, 21, 16,
        27, 26, 34, 15, 12, 35, 40, 47, 14, 55, 28, 40, 51, 51, 8, 41, 28, 20, 11, 42,
        11, 23, 49, 13, 54, 49, 40, 18, 45, 13, 28, 12, 51, 4, 15, 48, 33, 43, 32, 39,
        36, 7, 44, 11, 45, 36, 27, 52, 33, 11, 27, 6, 22, 52, 36, 7, 45, 13, 17, 52,
        19, 47, 15, 38, 35, 23, 16, 15, 30, 42, 34, 34, 39, 21, 18, 37, 41, 13, 48, 36};
    //sum of all weights
    private static final double TOTAL_WEIGHT = sumOfAllWeights(W);
    //capacity ratio of the sack 
    private static double SACK_RATIO = 0.5;
    private static double SACK_CAPACITY = TOTAL_WEIGHT * SACK_RATIO;
    //best fitness value to sack
    private static double OPTIMUM_FITNESS = 1561.0;

    private double bestFitness;
    private double sackCapacity;

    public K100() {
        super(W.length);
        sackCapacity = SACK_CAPACITY;
        bestFitness = OPTIMUM_FITNESS;
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * copy constructor
     *
     * @param original
     */
    public K100(K100 original) {
        super(original);
        sackCapacity = original.sackCapacity;
        bestFitness = original.bestFitness;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * optimization of cloning
     *
     * @return
     */
    @Override
    public AbstractKnapSack getClone() {
        return new K100(this);
    }

    public double[] getWeight() {
        return W;
    }

    public double[] getProfit() {
        return P;
    }
    
     public double getSackCapacity(){
        return sackCapacity;
    }

    @Override
    protected double evaluate(boolean[] genome) {
        return fitnessLinearPenalty(W, P, sackCapacity);
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public boolean isOptimum() {
        return getFitness() == bestFitness;
    }

    /**
     * update the size of the genome
     *
     * @param params size of the genome
     */
    @Override
    public void setParameters(String params) {//-------------------------------- set parameters
        try {
            //update size
            double ratio = Double.valueOf(params);

            if (SACK_RATIO == ratio) { //same ratio
                return;
            } else {
                SACK_RATIO = ratio;
            }
            SACK_CAPACITY = SACK_RATIO * TOTAL_WEIGHT;
            OPTIMUM_FITNESS = KnapSack_DP.getBestknapSack01Value(SACK_CAPACITY, W, P);
        } catch (Exception e) {
        }
    }

    @Override
    public String getParameters() {//------------------------------------------- get parameters
        return SACK_RATIO + "";
    }

    @Override
    public String getInformation() {
        return getInformation(W, P, OPTIMUM_FITNESS, SACK_CAPACITY);
    }

    public String toStringPhenotype() {//--------------------------------------- textual representation of phenotype
        double percent = (double) (weightOfSack(W) * 100 / sackCapacity);
        return toStringGenome() + String.format("%8.2f", percent) + "%";
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201701281826L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
