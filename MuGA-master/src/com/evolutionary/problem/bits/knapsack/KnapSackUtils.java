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

import com.utils.staticallib.Distributions.uniform;

/**
 * Created on 31/jan/2017, 9:45:14 
 * @author zulu - computer
 */
public abstract class KnapSackUtils {


    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     public static double valueOfSack(double[] profit, boolean[] bits) {
        double value = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                value += profit[i];
            }

        }
        return value;
    }

    public static double weightOfSack(double[] weight, boolean[] bits) {
        double value = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                value += weight[i];
            }
        }
        return value;
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * create uncorrelated problem <br>
     * weight[i] = random [1..size] <br>
     * profit[i] = random [1..size]<br>
     *
     * @param size number of itens
     * @return problem[0] = weights <br> problem[1] = profits;
     */
    public static double[][] uncorrelated(int size) {
        double problem[][] = new double[2][size];
        for (int i = 0; i < size; i++) {
            problem[0][i] = (int) uniform.random(1, size);
            problem[1][i] = (int) uniform.random(1, size);
        }
        return problem;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * create weakly Correlated problem <br>
     * weight[i] = random [1..size] <br>
     * profit[i] = weight[i] + random [-weight[i]/2 ... weight[i]/2]<br>
     *
     *
     * @param size number of itens
     * @return problem[0] = weights <br> problem[1] = profits;
     */
    public static double[][] weaklyCorrelated(int size) {
        double problem[][] = new double[2][size];
        for (int i = 0; i < size; i++) {
            problem[0][i] = (int) uniform.random(1, size);
            problem[1][i] = problem[0][i] + (int) uniform.random(-problem[0][i] / 2, problem[0][i] / 2);
        }
        return problem;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * create strong Correlated problem <br>
     * r = random(size / 2, size) <br>
     * weight[i] = random [1..size] <br>
     * profit[i] = weight[i] + r  <br>
     *
     *
     * @param size number of itens
     * @return problem[0] = weights <br> problem[1] = profits;
     */
    public static double[][] strongCorrelated(int size) {
        double problem[][] = new double[2][size];
        int r = (int) uniform.random(size / 2, size);
        for (int i = 0; i < size; i++) {
            problem[0][i] = (int) uniform.random(1, size);
            problem[1][i] = problem[0][i] + r;
        }
        return problem;
    }
    /**
     * calculate the sum of the weight of all itens
     * @param weight itens
     * @return total of sum of wights
     */
    public static double sumOfAllWeights(double[] weight) {
        double total = 0;
        for (double value : weight) {
            total += value;
        }
        return total;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static double penaltyLog(double size, double capacity) {
        if (size <= capacity) {
            return 0.0;
        }
        return Math.log10(1.0 + (size - capacity)) / Math.log10(2);
    }
//----------------------------------------------------------------------------------

    public static double penaltyLinear(double size, double capacity) {
        if (size <= capacity) {
            return 0.0;
        }
        return size - capacity;
    }
//----------------------------------------------------------------------------------

    public static double penaltyQuadratic(double size, double capacity) {
        if (size <= capacity) {
            return 0.0;
        }
        return Math.pow(size - capacity, 2.0);
    }
      
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201701310945L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}