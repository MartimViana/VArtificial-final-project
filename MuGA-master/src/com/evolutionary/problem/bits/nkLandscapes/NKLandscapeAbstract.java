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

package com.evolutionary.problem.bits.nkLandscapes;

import com.evolutionary.problem.bits.BinaryString;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created on 22/mar/2017, 11:36:08
 *
 * @author zulu - computer
 */
public abstract class NKLandscapeAbstract extends BinaryString {

    /**
     * definition pof the problema - List of Rules { int[] , double[] } RULE :
     * [bits number] [ values for each value] Example of RULE; [1 ,3 ] [0.1 ,
     * 0.3, 0.2 ,0.8] bits 1 and 3 in the genome Values 00=0.1 01=0.3 10=0.2
     * 11=0.8 RULE(0011) = [0*1*] = 0.3
     *
     */
    protected List< Pair< int[], double[]>> problem; // problem data of individual 

    public List< Pair< int[], double[]>> getNK_Matrix() {
        return problem;
    }

    /**
     * gets optimum genome by default is not avaiable
     *
     * @return optimum genome
     */
    public boolean[] getOptimumGenome() {
        return null;
    }

    /**
     * creates an individual of NK-Landscapes
     *
     * @param numberOfGenes number of genes
     * @param rules list of Rules
     */
    public NKLandscapeAbstract(int numberOfGenes, List< Pair< int[], double[]>> rules) {
        super(numberOfGenes, Optimization.MAXIMIZE);
        this.problem = rules;
    }

    /**
     * NK-landscape individual clone
     *
     * @param original original individual
     */
    public NKLandscapeAbstract(NKLandscapeAbstract original) {
        super(original);
        this.problem = original.problem;
    }

    @Override
    protected double evaluate(boolean[] genome) {
        return value(genome, problem);
    }

    /**
     * evaluate a NK-landscape genome
     *
     * @param genome genome
     * @param rules list of rules
     * @return fitness value
     */
    public static double value(boolean[] genome, List< Pair< int[], double[]>> rules) {
        double fitness = 0.0;
        //for each rule
        for (Pair< int[], double[]> rule : rules) {
            int[] bitsInTheGenome = rule.getLeft();
            //calculate the value of the bits int the bitsInTheGenome positions
            int indexOfRuleValue = 0;
            for (int j = 0; j < bitsInTheGenome.length; ++j) {
                indexOfRuleValue <<= 1;
                indexOfRuleValue |= genome[bitsInTheGenome[j]] ? 1 : 0;
            }
            //sum the value
            fitness += rule.getRight()[indexOfRuleValue];
        }
        return fitness;
    }

    /**
     * Change rules to suport optimum value The rules are changed to put the
     * maximum value int the positions done by the optimum genome
     *
     * @param optimumGenome optimum genome
     * @param rules list of rules
     * @param update replace or swap maximum value
     * <br>replace makes many optimas because are two maximum values in the rule
     * <br> in the current position and in the optimal position
     * <br>swap make one optima swaping the maximum value to the optimal
     * position
     *
     * @return value of the optimum genome
     */
    public static double setOptimumValue(boolean[] optimumGenome, List< Pair< int[], double[]>> rules) {
        double sum = 0;
        //
        for (int i = 0; i < rules.size(); ++i) {
            int[] varIndices = rules.get(i).getLeft();
            double[] varValues = rules.get(i).getRight();
//            //get index of maximum value
            int indexOfMax = 0;
            for (int index = 1; index < varValues.length; index++) {
                if (varValues[index] > varValues[indexOfMax]) {
                    indexOfMax = index;
                }
            }
            //calculate index
            int indexOfGenes = 0;
            for (int j = 0; j < varIndices.length; ++j) {
                indexOfGenes <<= 1;
                indexOfGenes |= optimumGenome[varIndices[j]] ? 1 : 0;
            }

            //swap maximum with position of the gene value
//            double max = varValues[indexOfMax];
//            varValues[indexOfMax] = varValues[indexOfGenes];
            varValues[indexOfGenes] = 1.0;

            sum += varValues[indexOfGenes];
        }
        return sum;
    }

    public static double getMaxValue(List< Pair< int[], double[]>> data) {
        double sum = 0;

        for (int i = 0; i < data.size(); ++i) {
            int[] varIndices = data.get(i).getLeft();
            double[] varValues = data.get(i).getRight();
            //get maximum value
            int indexMax = 0;
            for (int index = 1; index < varValues.length; index++) {
                if (varValues[index] > varValues[indexMax]) {
                    indexMax = index;
                }
            }
            //swap maximum with position of the gene value
            sum += varValues[indexMax];
        }
        return sum;
    }

    public String getProblemInfo() {
        StringBuilder txt = new StringBuilder("\n Rules data [" + problem.size() + "]");
        for (Pair<int[], double[]> pair : problem) {
            txt.append("\n[ ");
            for (int i : pair.getLeft()) {
                txt.append(String.format("%3d ", i));
            }
            txt.append("] - [ ");
            for (double d : pair.getRight()) {
                txt.append(String.format("%.03f ", d));
            }
            txt.append("]");
        }
        return txt.toString();
    }

}
