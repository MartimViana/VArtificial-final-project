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
package com.evolutionary.report.statistics;

import com.evolutionary.population.Population;
import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.permutation.Permutations;
import com.evolutionary.problem.real.RealVector;
import com.evolutionary.solver.EAsolver;
import static com.utils.Shannon.log;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A. Manso, L. Correia Genetic Algorithms using Populations based on Multisets
 * EPIA'2009 - Fourteenth Portuguese Conference on Artificial Intelligence
 * Aveiro , Portugal, Outubro 2009
 *
 * Created on 15/mar/2016, 15:47:45
 *
 * @author zulu - computer
 */
public class GeneticDiversityLog extends AbstractStatistics {

    public GeneticDiversityLog() {
        super("Genetic diversity");
        higherIsBetter = true;
        logScaleEnabled = true; // enable log scale in statistics view
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    @Override
    public double execute(EAsolver s) {
        Population pop = s.parents;

        if (pop.getTemplate() instanceof BinaryString) {
            return binaryDiversity(pop);
        } else if (pop.getTemplate() instanceof Permutations) {
            return permutDiversity(pop);
        } else if (pop.getTemplate() instanceof RealVector) {
            return realDiversity(pop);
        }
        return 0;
    }

    public double binaryDiversity(Population pop) {
        //calculate histogram of population
        int numGenes = pop.getTemplate().getNumGenes();
        List<Individual> genomes = pop.getGenomes();
        int hist[] = new int[numGenes];
        boolean[] bits;
        for (Individual ind : genomes) {
            bits = ((BinaryString) ind).getBitsGenome();
            for (int i = 0; i < bits.length; i++) {
                if (bits[i]) {
                    //include number of copies
                    hist[i] += pop.getCopies(ind);
                }

            }
        }
        //calculate statistic
        double val = 0.0;
        //number of individuals
        double numIndividuals = pop.getNumberOfIndividuals();
        for (int i = 0; i < hist.length; i++) {
            val += getBinaryEntropy(hist[i] / numIndividuals);
        }
        return val / numGenes;

    }

    public double permutDiversity(Population pop) {

        int numGenes = pop.getTemplate().getNumGenes();
        List<Individual> genomes = pop.getGenomes();

        //matrix of genes
        double matrix[][] = new double[numGenes][genomes.size()];
        //convert genes to matrix values

        for (int i = 0; i < genomes.size(); i++) {
            int[] values = ((Permutations) genomes.get(i)).getIndexesGenome();
            //put individual genes in the column column
            for (int j = 0; j < values.length; j++) {
                matrix[j][i] = values[j];
            }
        }
        //calculate statistic
        double val = 0.0;
        int numIndividuals = pop.getNumberOfIndividuals();
        for (int i = 0; i < matrix.length; i++) {
            val += getIntervalEntropy(matrix[i], 0, numGenes - 1, numIndividuals);
        }        
        return val / matrix.length;

    }

    public double realDiversity(Population pop) {
        int numGenes = pop.getTemplate().getNumGenes();
        List<Individual> genomes = pop.getGenomes();
        //matrix of genes
        double matrix[][] = new double[numGenes][genomes.size()];
        //convert genes to matrix values
        for (int i = 0; i < genomes.size(); i++) {
            double[] values = ((RealVector) genomes.get(i)).getGenome();
            //put individual genes in the column column
            for (int j = 0; j < values.length; j++) {
                matrix[j][i] = values[j];
            }
        }
        //calculate statistic
        double val = 0.0;
        double min = ((RealVector) pop.getTemplate()).getMinValue();
        double max = ((RealVector) pop.getTemplate()).getMaxValue();
        int numIndividuals = pop.getNumberOfIndividuals();
        for (int i = 0; i < matrix.length; i++) {
            val += getIntervalEntropy(matrix[i], min, max, numIndividuals);
        }
        return val / matrix.length;

    }

    @Override
    public String getInformation() {
        StringBuffer txt = new StringBuffer();
        txt.append("Genetic diversity of the parents population\n");
        txt.append("\nUsing log scale");
        return txt.toString();

    }

    /**
     * calculates the entropy of the values in the interval based on the shannon
     * metric
     *
     * @param values array of values
     * @param min minimum of the interval
     * @param max maximum of the interval
     * @param base number of genes
     * @return entropy
     */
    public static double getIntervalEntropy(double[] values, double min, double max, int base) {
        double intevalDim = max - min;
        Arrays.sort(values);
        double sum = 0;
        for (int i = 1; i < values.length; i++) {
            sum += pLopP((values[i] - values[i - 1]) / intevalDim, base);
        }
        return sum;
    }

    public static double pLopP(double p, double base) {
        if (p == 0) {
            return 0;
        }
        return -p * log(p, base);
    }

    public static double getBinaryEntropy(double p) {
//        return getIntervalEntropy(new double[]{0, p, 1}, 0, 1,2);        
        if (p == 0 || p == 1) {
            return 0;
        }
        return -(p * log(p, 2) + (1 - p) * log(1 - p, 2));
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603151547L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////   

    public static void main(String[] args) {
        double v[] = {3, 3, 3, 4, 3};
        System.out.println("GD = " + getIntervalEntropy(v, 0, v.length - 1, v.length - 1) + " => " + Arrays.toString(v));
//        Random rnd = new Random();
//        for (int i = 0; i < 100; i++) {
//            for (int j = 0; j < v.length; j++) {
//                v[j] = rnd.nextInt(v.length);
//
//            }
//            System.out.println("GD = " + getIntervalEntropy(v, 0, v.length, v.length) + " " + Arrays.toString(v));
//
//        }

    }
}
