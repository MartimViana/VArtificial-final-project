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
public class GenDivDistance2Center extends AbstractStatistics {

    public GenDivDistance2Center() {
        super("Distance to Center");
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
        double matrix[][] = new double[numGenes][genomes.size()];
        boolean[] values;//gene values of the individuals
        double[] center = new double[numGenes];//center of mass of the genes
        for (int i = 0; i < genomes.size(); i++) {
            values = ((BinaryString) genomes.get(i)).getBitsGenome();
            //put individual genes in the column column
            for (int j = 0; j < values.length; j++) {
                matrix[j][i] = values[j] ? 1 : 0; //binary value
                center[j] += matrix[j][i]; //acumulate to center
            }
        }
        //calculate center of mass
        for (int i = 0; i < center.length; i++) {
            center[i] /= genomes.size();
        }
        //calculate distance to center
        double sumDistances = 0;
        for (int i = 0; i < matrix.length; i++) {
            double sum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                sum += Math.abs(center[i] - matrix[i][j]);
            }
            sumDistances += sum / matrix[i].length;
        }

        // System.out.println("MATRIX\n" + toString(matrix));
        //calculate statistic  
        //  System.out.println("CENTER\n" + toString(center));
        return 2 * sumDistances / matrix.length;
    }

    public double[] getGeneDistance2Center(double[][] data) {
        double[] volume = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            volume[i] = 0;
            //sum data
            for (int j = 0; j < data[i].length; j++) {
                volume[j] += data[i][j];
            }
            //calc mean
            volume[i] /= data[i].length;
            //sum distances to center
            double dist = 0;
            for (int j = 0; j < data[i].length; j++) {
                dist += Math.abs(data[i][j] - volume[i]);
            }
            //update volume
            volume[i] = dist;
        }
        return volume;
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
        double sum = 0;
        for (int i = 0; i < difValues.length; i++) {
            sum += (difValues[i] - 1) / (numGenes - 1);
        }
        return sum / difValues.length;
    }

    public double realVolume(Population pop) {
        int numGenes = pop.getTemplate().getNumGenes();
        List<Individual> genomes = pop.getGenomes();
        //matrix of genes
        double matrix[][] = new double[numGenes][genomes.size()];
        double[] values;//gene values of the individuals
        double[] center = new double[numGenes];//center of mass of the genes
        for (int i = 0; i < genomes.size(); i++) {
            values = ((RealVector) genomes.get(i)).getGenome();
            //put individual genes in the column column
            for (int j = 0; j < values.length; j++) {
                matrix[j][i] = values[j];
                center[j] += matrix[j][i]; //acumulate to center
            }
        }
        //calculate center of mass
        for (int i = 0; i < center.length; i++) {
            center[i] /= genomes.size();
        }
        //calculate distance to center
        double sumDistances = 0;
        for (int i = 0; i < matrix.length; i++) {
            double sum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                sum += Math.abs(center[i] - matrix[i][j]);
            }
            sumDistances += sum / matrix[i].length;
        }

      //  System.out.println("MATRIX\n" + toString(matrix));
        //calculate statistic  
      //  System.out.println("CENTER\n" + toString(center));
        return sumDistances / matrix.length;

    }

    @Override
    public String getInformation() {
        StringBuffer txt = new StringBuffer();
        txt.append("Distance to the Center of Mass\n");
        txt.append("\nUsing log scale");
        return txt.toString();
    }

    public static double getDistance2Center(double v[], double min, double max) {
        double total = 0;
        for (double d : v) {
            total += (d - min) * (max - d);
        }
        return total;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603151547L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////   

    public static void main(String[] args) {
        //double inds[][] = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        //double inds[][] = {{1, 1, 1}, {2, 2, 2}, {3, 3, 3}};
        double inds[][] = {{1, 1, 1},  {3, 3, 3}};
        

        SimplePopulation pop = new SimplePopulation();

        for (double[] ind : inds) {
            RealVector i1 = new Sphere(ind, 1, 3);
            pop.addIndividual(i1);
        }

        System.out.println(pop);
        GenDivDistance2Center gd = new GenDivDistance2Center();
        double value = gd.realVolume(pop);
        System.out.println("Distance to Center = " + value);

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
        GenDivDistance2Center gd = new GenDivDistance2Center();
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
