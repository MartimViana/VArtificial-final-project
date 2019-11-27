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
package com.evolutionary.problem.permutation;

import com.evolutionary.problem.Individual;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created on 29/set/2015, 9:27:32
 *
 * @author zulu - computer
 */
public abstract class Permutations extends Individual {

    public static int DEFAULT_SIZE = 128; //-------------------------------------- default size

    /**
     * evaluate permutation of indexes
     *
     * @param genome array of indexes
     * @return fitness of indexes
     */
    protected abstract double evaluate(int[] genome);//--------------------- calculate fitness

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected Permutations(Optimization type) {
        this(DEFAULT_SIZE, type);
    }

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected Permutations(int size, Optimization type) {
        super(new int[size], type);
        randomize();
    }

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected Permutations(Permutations bin) {
        //create a copy of genome
        super(Arrays.copyOf(bin.getIndexesGenome(), bin.getNumGenes()), bin.getOptimizationType());
        //copy atributes
        this.solver = bin.solver;
        this.random = bin.random;
        this.value = bin.value;
        this.numberOfCopies = bin.numberOfCopies;
    }

    /**
     * convert to array of int from genome Object
     *
     * @return array of ints
     */
    public int[] getIndexesGenome() {
        return (int[]) genome;
    }

    /**
     * gets the number of allels
     *
     * @return
     */
    @Override
    public int getNumGenes() {
        return ((int[]) genome).length;
    }

    /**
     * calculate the fitness of the invidual this method increase the number of
     * evaluations
     *
     * @return fitness value
     */
    @Override
    public double fitness() {
        return evaluate(getIndexesGenome());
    }

    /**
     * converte genome to string
     *
     * @return string genome
     */
    @Override
    public String toStringGenome() {
        return indexArrayToString(getIndexesGenome());
    }

    /**
     * randomize genome
     */
    public void randomize() {//-------------------------------------------------randomize

        int[] genome = getIndexesGenome();
        //create indexes
        for (int i = 0; i < genome.length; i++) {
            genome[i] = i;
        }

        //create random permutation
        int index = genome.length - 1;
        int i, aux;
        while (index > 0) {
            i = random.nextInt(index + 1);
            aux = genome[i];
            genome[i] = genome[index];
            genome[index] = aux;
            index--;
        }
        //clear evaluation
        setNotEvaluated();
    }

    /**
     * count different valuees in the same positions
     *
     * @param ind other individual
     * @return number of values that not match
     */
    @Override
    public double distanceTo(Individual ind) {//---------------------------------- Levenstein Distance
        int[] thisGenome = getIndexesGenome();
        int[] otherGenome = ((Permutations) ind).getIndexesGenome();
        int sum = 0;
        for (int i = 0; i < otherGenome.length; i++) {
            if (otherGenome[i] != thisGenome[i]) {
                sum++;
            }

        }
        return sum;
        //return Distances.computeLevenshteinDistance(thisGenome, otherGenome);
    }

    /**
     * deep copy
     *
     * @return
     */
    @Override
    public Permutations getClone() {//-------------------------------------------- get clone
        Permutations clone = (Permutations) super.getClone();
        //clone genome
        int[] indexes = getIndexesGenome();
        int[] newIndexes = new int[indexes.length];
        System.arraycopy(indexes, 0, newIndexes, 0, indexes.length);
        clone.genome = newIndexes;
        return clone;
    }

    /**
     * verify the equality of the genomes
     *
     * @param ind ind to test
     * @return
     */
    public boolean equals(Object ind) {//--------------------------------------- equals
        int[] thisGenome = getIndexesGenome();
        int[] otherGenome = ((Permutations) ind).getIndexesGenome();
        for (int i = 0; i < otherGenome.length; i++) {
            if (thisGenome[i] != otherGenome[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getIndexesGenome());
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
            int size = Integer.parseInt(params);
            genome = new int[size];
            randomize();
            setNotEvaluated();
        } catch (Exception e) {
        }
    }

    @Override
    public String getParameters() {//------------------------------------------- get parameters
        return getNumGenes() + "";
    }

    @Override
    public String getInformation() {//------------------------------------------ get information
        // public String getInfo() {
        StringBuilder txt = new StringBuilder();
        String p = getParameters();
        p = p.replaceAll(" ", " , ");
        txt.append(getClass().getSimpleName() + "(" + p + ") : ");
        txt.append(getOptimizationType() == Optimization.MAXIMIZE ? "MAXIMIZE" : "MINIMIZE");
        return txt.toString();
    }

  

    /**
     * Convert a boolean array to String
     *
     * @param indexes boolean array
     * @return String with '0' and '1'
     */
    public static String indexArrayToString(int[] indexes) {//--------------- boolean array to string
        StringBuilder txt = new StringBuilder();
        int size = (int) Math.log10(indexes.length) + 1;
        for (int index : indexes) {
            txt.append(String.format("%" + size + "d ", index));
        }
        return txt.toString();
    }

    public static int[] reorganizeGenes(int[] genes, int[] order) {
        int[] genesOrdered = new int[genes.length];
        for (int i = 0; i < genesOrdered.length; i++) {
            genesOrdered[i] = genes[order[i]];
        }
        return genesOrdered;
    }

    @Override
    public int compare(Individual first, Individual second) {
        if (first.getFitness() == second.getFitness()) {
            return super.compare(first, second);
        }
        int[] mGenes = getIndexesGenome();
        int[] oGenes = ((Permutations) second).getIndexesGenome();
        for (int i = 0; i < oGenes.length; i++) {
            if (oGenes[i] != mGenes[i]) {
                return mGenes[i] > oGenes[i] ? 1 : -1;
            }

        }
        return 0;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290927L;

    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
