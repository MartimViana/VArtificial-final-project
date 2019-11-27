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
import com.evolutionary.population.SimplePopulation;
import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.bits.OneMax;
import com.evolutionary.problem.permutation.Permutations;
import com.evolutionary.problem.real.RealVector;
import com.evolutionary.problem.real.Sphere;
import static com.evolutionary.report.statistics.GenDivDistance2Center.toString;
import com.evolutionary.solver.EAsolver;
import java.util.Arrays;
import java.util.List;

/**
 * A. Manso, L. Correia Genetic Algorithms using Populations based on Multisets
 * EPIA'2009 - Fourteenth Portuguese Conference on Artificial Intelligence
 * Aveiro , Portugal, Outubro 2009
 *
 * Created on 15/mar/2016, 15:47:45
 *
 * @author zulu - computer
 */
public class GenDivVolume extends AbstractStatistics {

    public GenDivVolume() {
        super("Population Volume");
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
            return binaryVolume(pop);
        } else if (pop.getTemplate() instanceof Permutations) {
            return permutVolume(pop);
        } else if (pop.getTemplate() instanceof RealVector) {
            return realVolume(pop);
        }
        return 0;
    }

    public double binaryVolume(Population pop) {
        int numGenes = pop.getTemplate().getNumGenes();
        List<Individual> genomes = pop.getGenomes();
        //matrix of genes        
        boolean[] values;//gene values of the individuals
        double[] min = new double[numGenes];//minimum Value
        double[] max = new double[numGenes];//minimum Value
        for (int i = 0; i < max.length; i++) {
            max[i] = Double.MIN_VALUE;
            min[i] = Double.MAX_VALUE;
        }
        for (int i = 0; i < genomes.size(); i++) {
            values = ((BinaryString) genomes.get(i)).getBitsGenome();
            //put individual genes in the column column
            for (int j = 0; j < values.length; j++) {
                double val = values[j] ? 1 : 0; //binary value
                if (min[j] > val) { //calculate min
                    min[j] = val;
                }
                if (max[j] < val) { //calculate max
                    max[j] = val;
                }
            }
        }
        //calculate center of mass
        double div = 1;
        for (int i = 0; i < max.length; i++) {
            div *= max[i] - min[i];
        }

        // System.out.println("MAX " + toString(max));
        //  System.out.println("MIN " + toString(min));
        return div;

    }

    public double permutVolume(Population pop) {
        int numGenes = pop.getTemplate().getNumGenes();
        List<Individual> genomes = pop.getGenomes();
        //matrix of genes
        double matrix[][] = new double[numGenes][genomes.size()];
        //convert genes to matrix values
        // normalized by the factor 
        for (int i = 0; i < genomes.size(); i++) {
            int[] values = ((Permutations) genomes.get(i)).getIndexesGenome();
            //put individual genes in the column column
            for (int j = 0; j < values.length; j++) {
                matrix[j][i] = values[j];
            }
        }
        //number of different values in the gene
        double difValues[] = new double[numGenes];

        for (int i = 0; i < matrix.length; i++) {
            Arrays.sort(matrix[i]);
            difValues[i] = 0;
            for (int j = 1; j < matrix[i].length; j++) {
                if (matrix[i] != matrix[i - 1]) {
                    difValues[i]++;
                }
            }
        }
        //calculate statistic        
        return getVolume(difValues, 0, numGenes);
    }

    public double realVolume(Population pop) {
        int numGenes = pop.getTemplate().getNumGenes();
        List<Individual> genomes = pop.getGenomes();
        //matrix of genes        
        double[] values;//gene values of the individuals
        double[] min = new double[numGenes];//minimum Value
        double[] max = new double[numGenes];//minimum Value
        for (int i = 0; i < max.length; i++) {
            max[i] = Double.MIN_VALUE;
            min[i] = Double.MAX_VALUE;
        }
        for (int i = 0; i < genomes.size(); i++) {
            values = ((RealVector) genomes.get(i)).getGenome();
            //put individual genes in the column column
            for (int j = 0; j < values.length; j++) {
                if (min[j] > values[j]) { //calculate min
                    min[j] = values[j];
                }
                if (max[j] < values[j]) { //calculate max
                    max[j] = values[j];
                }
            }
        }
        //calculate center of mass
        double div = 1;
        for (int i = 0; i < max.length; i++) {
            div *= max[i] - min[i];
        }

      //  System.out.println("MAX " + toString(max));
      //  System.out.println("MIN " + toString(min));
        RealVector template = (RealVector) pop.getIndex(0);
        double factor = Math.pow(template.getMaxValue() - template.getMinValue(),numGenes);
        return div / factor;

    }

    @Override
    public String getInformation() {
        StringBuffer txt = new StringBuffer();
        txt.append("Population Volume\n");
        txt.append("\nUsing log scale");
        return txt.toString();
    }

    public static double getVolume(double v[], double min, double max) {
        double total = 1;
        double dim = max - min;
        for (double d : v) {
            total *= d / dim;
        }
        return total;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603151547L;

    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////   
    public static void main(String[] args) {
        double inds[][] = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        //double inds[][] = {{1, 1, 1}, {2, 2, 2}, {3, 3, 3}};
        //double inds[][] = {{1, 1, 1}, {3, 3, 3}};

        SimplePopulation pop = new SimplePopulation();

        for (double[] ind : inds) {
            RealVector i1 = new Sphere(ind, 1, 3);
            pop.addIndividual(i1);
        }

        System.out.println(pop);
        GenDivVolume gd = new GenDivVolume();
        double value = gd.realVolume(pop);
        System.out.println("Volume= " + value);

    }

    public static void binaryPop() {
        String genes[] = {"1110", "1010", "1011", "1101"};
        //String genes[] = {"1111", "0000", "1010", "0101"};
        //String genes[] = {"1110", "1010", "1011", "1101"};
        //String genes[] = {"111111", "111111", "111111"};
        // String genes[] = {"10101010", "10101010", "10101010"};

        SimplePopulation pop = new SimplePopulation();
        OneMax i1;
        for (String gene : genes) {
            i1 = new OneMax(gene);
            pop.addIndividual(i1);
        }

        System.out.println(pop);
        GenDivVolume gd = new GenDivVolume();
        double value = gd.binaryVolume(pop);
        System.out.println("Distance to Center = " + value);

    }

    public static String toString(double[][] m) {
        StringBuilder txt = new StringBuilder();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                txt.append(String.format("%3.1f ", m[i][j]));
            }
            txt.append("\n");
        }
        return txt.toString();
    }

    public static String toString(double[] v) {
        StringBuilder txt = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            txt.append(String.format("%3.2f ", v[i]));
            //txt.append("\n");
        }
        return txt.toString();
    }
}
