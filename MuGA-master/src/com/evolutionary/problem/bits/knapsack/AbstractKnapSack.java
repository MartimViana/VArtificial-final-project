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

import com.evolutionary.problem.bits.BinaryString;
import com.utils.MyString;
import com.utils.staticallib.Distributions.uniform;
import java.util.Locale;

/**
 * Created on 29/set/2015, 9:30:30
 *
 * @author zulu - computer
 */
public abstract class AbstractKnapSack extends BinaryString {

    public abstract double[] getWeight();

    public abstract double[] getProfit();

    public abstract double getSackCapacity();

    public AbstractKnapSack(int size) {
        super(size, Optimization.MAXIMIZE);
    }

    public AbstractKnapSack(AbstractKnapSack k) {
        super(k);
    }

    public boolean isValid() {
        return getSackWeight() <= getSackCapacity();
    }

    public double valueOfSack(double[] profit) {
        boolean[] bits = getBitsGenome();
        double value = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                value += profit[i];
            }

        }
        return value;
    }

    public double weightOfSack(double[] weight) {
        boolean[] bits = getBitsGenome();
        double value = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                value += weight[i];
            }
        }
        return value;
    }

    public double maxWV(double[] weight, double[] profit) {

        double max = 0;
        for (int i = 0; i < weight.length; i++) {
            if (profit[i] / weight[i] > max) {
                max = profit[i] / weight[i];
            }
        }
        return max;
    }

    public double fitnessLinearPenalty(double[] w, double[] p, double capacity) {

        double sackW = weightOfSack(w);
        double sackV = valueOfSack(p);
        if (sackW <= capacity) {
            return sackV;
        }
        return valueOfSack(p) - Math.pow(sackW - capacity + 1, 2);
    }

    public double fitnessLogPenalty(double[] w, double[] p, double capacity) {
        return valueOfSack(p) - penaltyLog(weightOfSack(w), capacity);
    }

    public double fitnessQuadraticPenalty(double[] w, double[] p, double capacity) {
        return valueOfSack(p) - penaltyQuadratic(weightOfSack(w), capacity);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
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
        // add two to grow 
        return Math.pow((size - capacity + 2), 4.0);
    }

    public String getInformation(double[] w, double[] p, double best, double size) {//------------------------------------------ get information
        // public String getInfo() {
        StringBuilder txt = new StringBuilder();
        String par = getParameters();
        par = par.replaceAll(" ", " , ");
        txt.append(getClass().getSimpleName() + "(" + par + ") : ");
        txt.append(getOptimizationType() == Optimization.MAXIMIZE ? "MAXIMIZE" : "MINIMIZE");
        txt.append("\n");
        txt.append(MyString.alignRight("Problem Size", -15) + ": " + w.length + "\n");
        txt.append(MyString.alignRight("Sack Capacity", -15) + ": " + size + "\n");
        txt.append(MyString.alignRight("Optimum value", -15) + ": " + best + "\n\n");
        txt.append(MyString.alignRight("Item", 5) + "");
        txt.append(MyString.alignRight("Weight", 15) + "");
        txt.append(MyString.alignRight("Profit", 15) + "");
        for (int i = 0; i < p.length; i++) {
            txt.append("\n" + MyString.alignRight(i, 5));
            txt.append(String.format(Locale.US, "%15.01f", w[i]));
            txt.append(String.format(Locale.US, "%15.01f", p[i]));
        }

        return txt.toString();

    }

    public String toStringPhenotype(double[] w, double[] p, double best, double size) {

        return toStringGenome();
    }

    public double getSackWeight() {
        return weightOfSack(getWeight());
    }

    public double getTotalWeight() {
        return sumOfAllWeights(getWeight());
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290930L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
