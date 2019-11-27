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

package com.evolutionary.problem.bits;

import com.evolutionary.problem.Individual;
import com.utils.MyString;
import java.util.Arrays;

/**
 * Created on 29/set/2015, 9:27:32
 *
 * @author zulu - computer
 */
public abstract class BinaryString extends Individual {

    /**
     * evaluate binary string
     *
     * @param genome array of booleans
     * @return fitness of booleans
     */
    protected abstract double evaluate(boolean[] genome);//--------------------- calculate fitness

    public static int DEFAULT_SIZE = 8; //-------------------------------------- default size

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected BinaryString(Optimization type) {
        super(new boolean[DEFAULT_SIZE], type);
        //setParameters(DEFAULT_SIZE + "");
    }

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected BinaryString(int size, Optimization type) {
        super(new boolean[size], type);
        // setParameters(size + "");
    }

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected BinaryString(BinaryString bin) {
        //create a copy of genome
        super(Arrays.copyOf(bin.getBitsGenome(), bin.getNumGenes()), bin.getOptimizationType());
        this.solver = bin.solver;
        this.random = bin.random;
        this.value = bin.value;
        this.numberOfCopies = bin.numberOfCopies;
    }

    /**
     * convert to array of booleans from genome Object
     *
     * @return array of booleans
     */
    public boolean[] getBitsGenome() {
        return (boolean[]) genome;
    }

    /**
     * gets the number of allels
     *
     * @return
     */
    @Override
    public int getNumGenes() {
        return ((boolean[]) genome).length;
    }

    /**
     * calculate the fitness of the invidual this method increase the number of
     * evaluations
     *
     * @return fitness value
     */
    @Override
    public double fitness() {
        return evaluate(getBitsGenome());
    }

    /**
     * converte genome to string
     *
     * @return string genome
     */
    @Override
    public String toStringGenome() {
        return booleanArrayToString(getBitsGenome());
    }

    /**
     * update genome with bit string with '0' and '1' characters there are not
     * '0' or '1' are ignored. this method change the lenght of the genome
     *
     * @param bits binary string
     */
    public void setBits(String bits) {//---------------------------------------- setBits
        bits = MyString.removeNot01(bits); // remove characters that are not 0 or 1
        boolean genes[] = new boolean[bits.length()];
        for (int i = genes.length - 1; i >= 0; i--) {
            genes[i] = bits.charAt(i) == '1' ? true : false;

        }
        setGenome(genes);
    }

    /**
     * update genome with bit string with '0' and '1' characters there are not
     * '0' or '1' are ignored. this method change the lenght of the genome
     *
     * @param bits binary string
     */
    public void setBits(char bits[]) {//---------------------------------------- setBits
        boolean genes[] = new boolean[bits.length];
        for (int i = genes.length - 1; i >= 0; i--) {
            genes[i] = bits[i] == '1' ? true : false;

        }
        setGenome(genes);
    }

    /**
     * randomize genome
     */
    public void randomize() {//-------------------------------------------------randomize
        //create random bits
        boolean[] genes = getBitsGenome();
        for (int j = genes.length - 1; j >= 0; j--) {
            genes[j] = random.nextBoolean(); // Genetic random
        }
        //clear evaluation
        setNotEvaluated();
    }

    /**
     * hamming distance
     *
     * @param ind other individual
     * @return hamming distance
     */
    @Override
    public double distanceTo(Individual ind) {//---------------------------------- Hamming Distance
        boolean[] thisGenome = getBitsGenome();
        boolean[] otherGenome = ((BinaryString) ind).getBitsGenome();
        double dist = 0;
        for (int i = 0; i < thisGenome.length; i++) {
            if (thisGenome[i] != otherGenome[i]) {
                dist++;
            }
        }
        return dist;
    }

    /**
     * deep copy
     *
     * @return
     */
    @Override
    public BinaryString getClone() {//-------------------------------------------- get clone
        BinaryString clone = (BinaryString) super.getClone();
        //clone genome
        boolean[] bits = getBitsGenome();
        boolean[] newBits = new boolean[bits.length];
        System.arraycopy(bits, 0, newBits, 0, bits.length);
        clone.genome = newBits;
        return clone;
    }

    /**
     * verify the equality of the genomes
     *
     * @param ind ind to test
     * @return
     */
    public boolean equals(Object ind) {//--------------------------------------- equals
        boolean[] thisGenome = getBitsGenome();
        boolean[] otherGenome = ((BinaryString) ind).getBitsGenome();
        for (int i = 0; i < otherGenome.length; i++) {
            if (thisGenome[i] != otherGenome[i]) {
                return false;
            }
        }
        return true;
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
            genome = new boolean[size];
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
     * compute the number of ones int he genome
     *
     * @param ini start position (inclusive)
     * @param fin end position (exclusive)
     * @return number of ones between ini and fin
     */
    protected int unitation(int ini, int fin) {//------------------------------- unitation
        boolean[] genome = getBitsGenome();
        int ones = 0;
        for (int i = ini; i < fin; i++) {
            ones += genome[i] ? 1 : 0;
        }
        return ones;
    }

    public int hashCode() {
        return Arrays.hashCode(getBitsGenome());
    }

    public boolean isAllZeros() {
        boolean[] genome = getBitsGenome();
        for (boolean b : genome) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllOnes() {
        boolean[] genome = getBitsGenome();
        for (boolean b : genome) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert a boolean array to String
     *
     * @param bits boolean array
     * @return String with '0' and '1'
     */
    public static String booleanArrayToString(boolean[] bits) {//--------------- boolean array to string
        StringBuilder txt = new StringBuilder();
        for (boolean bit : bits) {
            txt.append(bit ? "1" : "0");
        }
        return txt.toString();
    }

    public static boolean[] reorganizeGenes(boolean[] genes, int[] order) {
        boolean[] genesOrdered = new boolean[genes.length];
        for (int i = 0; i < genesOrdered.length; i++) {
            genesOrdered[i] = genes[order[i]];
        }
        return genesOrdered;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290927L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        OneMax i = new OneMax(8);
        i.setBits("00001111");
        //int[] order = {0,2,4,6,1,3,5,7};
        int[] order = {7,6,5,4,3,2,1,0};
        boolean[] genes = reorganizeGenes(i.getBitsGenome(), order);
        System.out.println( booleanArrayToString(genes));
        
    }
    
}
