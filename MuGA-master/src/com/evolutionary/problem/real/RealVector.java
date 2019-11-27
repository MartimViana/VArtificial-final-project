//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manso  &  Luis Correia                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2015   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.problem.real;

import com.evolutionary.problem.Individual;
import com.utils.MyNumber;
import java.util.Arrays;

/**
 * Created on 29/set/2015, 9:27:32
 *
 * @author zulu - computer
 */
public abstract class RealVector extends Individual {

    /**
     * evaluate binary string
     *
     * @param genome array of booleans
     * @return fitness of booleans
     */
    protected abstract double evaluate(double[] genome);//--------------------- calculate fitness

    public double[] getOptimum() {//--------------------- ----------------------optimum of function
        return null;
    }

    public static int DEFAULT_SIZE = 8; //-------------------------------------- default size

    protected double minValue;
    protected double maxValue;

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected RealVector(int size, double min, double max, Optimization type) {
        super(new double[size], type);
        randomize();
        this.minValue = min;
        this.maxValue = max;
    }

    /**
     * contructor of maximize problems
     *
     * @param size size of individual
     */
    protected RealVector(RealVector real) {
        //create a copy of genome
        super(Arrays.copyOf(real.getGenome(), real.getNumGenes()), real.getOptimizationType());
        this.solver = real.solver;
        this.random = real.random;
        this.value = real.value;
        this.minValue = real.minValue;
        this.maxValue = real.maxValue;
        this.numberOfCopies = real.numberOfCopies;
    }

    /**
     * convert to array of booleans from genome Object
     *
     * @return array of booleans
     */
    public double[] getGenome() {
        return (double[]) genome;
    }

    /**
     * gets the number of allels
     *
     * @return
     */
    @Override
    public int getNumGenes() {
        return ((double[]) genome).length;
    }

    /**
     * calculate the fitness of the invidual this method increase the number of
     * evaluations
     *
     * @return fitness value
     */
    @Override
    public double fitness() {
        return evaluate(getGenome());
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setDoubleValuesRaw(double[] x) {
        this.genome = Arrays.copyOf(x, x.length);
    }

    public void setDoubleValues(double[] x) {
        double v[] = getGenome();
        if (v.length != x.length) {
            v = new double[x.length];
            setGenome(genome);
        }
        for (int i = 0; i < v.length; i++) {
//            if (x[i] < minValue) {
//                x[i] = maxValue - (minValue - x[i]);
//                i--;
//                continue;
//            }
//            if (x[i] > maxValue) {
//                x[i] = minValue + (x[i] - maxValue);
//                i--;
//                continue;
//            }

            v[i] = x[i] < minValue ? minValue : x[i] > maxValue ? maxValue : x[i];
        }
        setNotEvaluated();
    }

    public void setIndividual(RealVector real) {
        this.genome = Arrays.copyOf(real.getGenome(), real.getNumGenes());
        this.solver = real.solver;
        this.random = real.random;
        this.value = real.value;
        this.minValue = real.minValue;
        this.maxValue = real.maxValue;
        this.numberOfCopies = real.numberOfCopies;
    }

    /**
     * converte genome to string
     *
     * @return string genome
     */
    @Override
    public String toStringGenome() {
        return doubleArrayToString(getGenome());
    }

    /**
     * converte genome to string
     *
     * @return string genome
     */
    @Override
    public String toStringPhenotype() {
        StringBuilder txt = new StringBuilder();
        double[] value = getGenome();
        for (double val : value) {
            txt.append(MyNumber.DoubleToString(val, 20) + " ");
        }
        return txt.toString();
    }

    /**
     * randomize genome
     */
    public void randomize() {//-------------------------------------------------randomize
        //create random bits
        double[] genes = getGenome();
        for (int j = genes.length - 1; j >= 0; j--) {
            genes[j] = minValue + (maxValue - minValue) * random.nextDouble();
        }
        //clear evaluation
        setNotEvaluated();
    }

    /**
     * Euclidean Distance
     *
     * @param ind other individual
     * @return Euclidean Distance
     */
    @Override
    public double distanceTo(Individual ind) {//---------------------------------- Hamming Distance
        double[] thisGenome = getGenome();
        double[] otherGenome = ((RealVector) ind).getGenome();
        double dist = 0;
        for (int i = 0; i < thisGenome.length; i++) {
            dist += Math.pow(thisGenome[i] - otherGenome[i], 2);
        }
        return Math.sqrt(dist);
    }

    /**
     * deep copy
     *
     * @return
     */
    @Override
    public RealVector getClone() {//-------------------------------------------- get clone
        RealVector clone = (RealVector) super.getClone();
        //clone genome
        double[] values = getGenome();
        double[] newValues = new double[values.length];
        System.arraycopy(values, 0, newValues, 0, values.length);
        clone.genome = newValues;
        clone.maxValue = maxValue;
        clone.minValue = minValue;        
        return clone;
    }

    /**
     * verify the equality of the genomes
     *
     * @param ind ind to test
     * @return
     */
    public boolean equals(Object ind) {//--------------------------------------- equals
        double[] thisGenome = getGenome();
        double[] otherGenome = ((RealVector) ind).getGenome();
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
            DEFAULT_SIZE = Integer.parseInt(params);
            genome = new double[DEFAULT_SIZE];
            randomize();
            setNotEvaluated();
        } catch (Exception e) {
        }
    }

    @Override
    public String getParameters() {//------------------------------------------- get parameters
        return getNumGenes() + "";
    }

    public int hashCode() {
        return Arrays.hashCode(getGenome());
    }

    /**
     * Convert a boolean array to String
     *
     * @param bits boolean array
     * @return String with '0' and '1'
     */
    public static String doubleArrayToString(double[] value) {//--------------- boolean array to string
        StringBuilder txt = new StringBuilder();
        for (double val : value) {
            txt.append(MyNumber.DoubleToString(val, 10) + " ");
        }
        return txt.toString();
    }

    public static double[] reorganizeGenes(double[] genes, int[] order) {
        double[] genesOrdered = new double[genes.length];
        for (int i = 0; i < genesOrdered.length; i++) {
            genesOrdered[i] = genes[order[i]];
        }
        return genesOrdered;
    }

    public String getProblemDefinition() {//------------------------------------------ get information
        // public String getInfo() {
        StringBuilder txt = new StringBuilder(super.getProblemDefinition());
        txt.append("\nInterval     [ " + minValue);
        txt.append(" , " + maxValue + " ]");
        return txt.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290927L;

    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
//        OneMax i = new OneMax(8);
//        i.setBits("00001111");
//        //int[] order = {0,2,4,6,1,3,5,7};
//        int[] order = {7, 6, 5, 4, 3, 2, 1, 0};
//        boolean[] genes = reorganizeGenes(i.getBitsGenome(), order);
//        System.out.println(booleanArrayToString(genes));

    }

}
