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

import com.utils.problems.KnapSack_DP;

/**
 * Created on 28/jan/2017, 18:26:34
 *
 * @author zulu - computer
 */
public class K50 extends AbstractKnapSack {

    public static final double W[] = {
        94, 70, 90, 97, 54, 31, 82, 97, 1, 58, 96, 96, 87, 53, 62, 89, 68, 58, 81, 83, 67, 41, 50, 58, 61,
        45, 64, 55, 12, 87, 32, 53, 25, 59, 23, 77, 22, 18, 64, 85, 14, 23, 76, 81, 49, 47, 88, 19, 74, 31
    };
    public static final double P[] = {
        3, 41, 22, 30, 45, 99, 75, 76, 79, 77, 41, 98, 31, 28, 58, 32, 99, 48, 20, 3, 81, 17, 3, 62, 39,
        76, 94, 75, 44, 63, 35, 11, 21, 45, 43, 46, 26, 2, 53, 37, 32, 78, 74, 66, 61, 51, 11, 85, 90, 40
    };
    //sum of all weights
    private static final double TOTAL_WEIGHT = sumOfAllWeights(W);
    //capacity ratio of the sack 
    private static double SACK_RATIO = 0.5;
    private static double SACK_CAPACITY = 1473.5;
    //best fitness value to ackRation
    private static double OPTIMUM_FITNESS = 1920;

    private double bestFitness;
    private double sackCapacity;

    public K50() {
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
    public K50(K50 original) {
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
    public K50 getClone() {
        return new K50(this);
    }
    
   public  double[] getWeight(){
       return W;
   }
    public  double[] getProfit(){
        return P;
    }
    public double getSackCapacity(){
        return sackCapacity;
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    protected double evaluate(boolean[] genome) {
        return fitnessLinearPenalty(W, P, sackCapacity);
    }

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
