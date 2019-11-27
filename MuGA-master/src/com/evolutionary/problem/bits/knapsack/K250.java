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
public class K250 extends AbstractKnapSack {

    private static final double W[] = {
        92.0, 11.0, 10.0, 86.0, 86.0, 30.0, 11.0, 7.0, 28.0, 33.0, 8.0, 65.0, 13.0, 69.0, 88.0, 62.0, 70.0, 65.0, 61.0, 51.0,
        3.0, 46.0, 74.0, 62.0, 25.0, 100.0, 50.0, 47.0, 77.0, 13.0, 88.0, 96.0, 14.0, 19.0, 30.0, 31.0, 98.0, 7.0, 72.0, 86.0,
        94.0, 80.0, 32.0, 29.0, 71.0, 63.0, 72.0, 80.0, 57.0, 5.0, 82.0, 9.0, 34.0, 30.0, 91.0, 22.0, 17.0, 47.0, 60.0, 87.0,
        18.0, 79.0, 19.0, 60.0, 8.0, 17.0, 44.0, 60.0, 34.0, 1.0, 52.0, 80.0, 47.0, 77.0, 69.0, 66.0, 67.0, 31.0, 10.0, 22.0,
        73.0, 38.0, 100.0, 72.0, 91.0, 65.0, 96.0, 22.0, 99.0, 51.0, 22.0, 43.0, 55.0, 92.0, 71.0, 48.0, 86.0, 46.0, 49.0, 34.0,
        18.0, 76.0, 82.0, 75.0, 94.0, 24.0, 18.0, 3.0, 41.0, 97.0, 49.0, 56.0, 74.0, 39.0, 85.0, 87.0, 72.0, 18.0, 59.0, 77.0,
        21.0, 60.0, 84.0, 10.0, 29.0, 4.0, 22.0, 45.0, 32.0, 64.0, 6.0, 64.0, 76.0, 70.0, 53.0, 37.0, 60.0, 77.0, 47.0, 35.0,
        57.0, 77.0, 32.0, 77.0, 29.0, 11.0, 13.0, 1.0, 63.0, 2.0, 7.0, 10.0, 29.0, 17.0, 28.0, 12.0, 58.0, 75.0, 14.0, 45.0,
        23.0, 94.0, 100.0, 98.0, 27.0, 82.0, 59.0, 70.0, 10.0, 26.0, 33.0, 25.0, 82.0, 34.0, 67.0, 62.0, 48.0, 10.0, 12.0, 5.0,
        39.0, 78.0, 60.0, 99.0, 17.0, 7.0, 98.0, 18.0, 29.0, 44.0, 20.0, 63.0, 49.0, 24.0, 24.0, 77.0, 41.0, 52.0, 80.0, 56.0,
        83.0, 74.0, 55.0, 86.0, 93.0, 60.0, 53.0, 59.0, 49.0, 28.0, 30.0, 53.0, 11.0, 53.0, 35.0, 68.0, 53.0, 26.0, 100.0, 36.0,
        5.0, 26.0, 44.0, 34.0, 88.0, 50.0, 47.0, 5.0, 33.0, 10.0, 8.0, 67.0, 1.0, 63.0, 46.0, 56.0, 98.0, 15.0, 27.0, 91.0,
        84.0, 1.0, 69.0, 75.0, 54.0, 39.0, 92.0, 53.0, 3.0, 79.0
    };
    private static final double P[] = {
        140.0, 12.0, 54.0, 135.0, 44.0, 74.0, 27.0, 21.0, 29.0, 74.0, 34.0, 94.0, 25.0, 118.0, 123.0, 24.0, 74.0, 93.0, 108.0, 59.0,
        49.0, 95.0, 81.0, 101.0, 33.0, 112.0, 89.0, 28.0, 105.0, 37.0, 118.0, 113.0, 50.0, 57.0, 36.0, 39.0, 103.0, 2.0, 116.0, 64.0,
        51.0, 68.0, 49.0, 45.0, 98.0, 59.0, 44.0, 115.0, 84.0, 48.0, 108.0, 9.0, 11.0, 16.0, 50.0, 51.0, 21.0, 9.0, 65.0, 66.0,
        41.0, 104.0, 40.0, 107.0, 11.0, 62.0, 39.0, 33.0, 21.0, 6.0, 74.0, 62.0, 55.0, 114.0, 99.0, 74.0, 27.0, 78.0, 56.0, 18.0,
        67.0, 38.0, 139.0, 107.0, 81.0, 62.0, 111.0, 30.0, 148.0, 92.0, 43.0, 68.0, 101.0, 63.0, 56.0, 7.0, 124.0, 57.0, 54.0, 83.0,
        33.0, 31.0, 123.0, 120.0, 133.0, 65.0, 25.0, 29.0, 44.0, 122.0, 7.0, 43.0, 102.0, 49.0, 109.0, 79.0, 43.0, 15.0, 102.0, 105.0,
        69.0, 40.0, 44.0, 40.0, 16.0, 19.0, 66.0, 21.0, 45.0, 69.0, 26.0, 104.0, 77.0, 112.0, 11.0, 84.0, 107.0, 89.0, 30.0, 32.0,
        93.0, 98.0, 65.0, 125.0, 2.0, 3.0, 36.0, 16.0, 16.0, 35.0, 9.0, 41.0, 10.0, 17.0, 41.0, 39.0, 106.0, 29.0, 20.0, 39.0,
        15.0, 105.0, 144.0, 146.0, 29.0, 84.0, 89.0, 64.0, 10.0, 64.0, 58.0, 56.0, 97.0, 30.0, 61.0, 76.0, 97.0, 18.0, 24.0, 31.0,
        63.0, 54.0, 78.0, 87.0, 30.0, 23.0, 49.0, 24.0, 74.0, 84.0, 11.0, 50.0, 73.0, 48.0, 24.0, 32.0, 90.0, 7.0, 61.0, 51.0,
        96.0, 105.0, 44.0, 126.0, 121.0, 91.0, 37.0, 56.0, 30.0, 65.0, 8.0, 29.0, 24.0, 82.0, 77.0, 74.0, 91.0, 56.0, 144.0, 16.0,
        13.0, 24.0, 2.0, 46.0, 109.0, 88.0, 41.0, 3.0, 50.0, 28.0, 53.0, 27.0, 40.0, 45.0, 95.0, 70.0, 108.0, 22.0, 50.0, 70.0,
        70.0, 43.0, 95.0, 70.0, 64.0, 34.0, 102.0, 33.0, 22.0, 64.0
    };
    //sum of all weights
    private static final double TOTAL_WEIGHT = sumOfAllWeights(W);
    //capacity ratio of the sack 
    private static double SACK_RATIO = 0.5;
    private static double SACK_CAPACITY = 6133.5;
    //best fitness value to sack
    private static double OPTIMUM_FITNESS = 10353.0;

    private double bestFitness;
    private double sackCapacity;

    public K250() {
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
    public K250(K250 original) {
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
    public K250 getClone() {
        return new K250(this);
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
