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
package com.evolutionary.problem;

import com.evolutionary.Genetic;
import com.evolutionary.solver.EAsolver;
import com.utils.MyNumber;
import com.utils.MyString;
import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * Created on 29/set/2015, 9:08:08
 *
 * @author zulu - computer
 */
public abstract class Individual extends Genetic implements Comparable<Individual>, Comparator<Individual> {

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * calculate the fitness of the invidual this method increase the number of
     * evaluations
     *
     * @return fitness value
     */
    protected abstract double fitness(); //------------------------------------- calculate fitness

    /**
     * verify if the individual is optimum
     *
     * @return
     */
    public abstract boolean isOptimum(); //------------------------------------- calculate fitness

    /**
     * Randomize genome of the individual
     */
    public abstract void randomize(); //--------------------------------------- randomize genome    

    /**
     * calculate the distance between two individuals
     *
     * @param ind distance
     * @return
     */
    public abstract double distanceTo(Individual ind); //------------------------- distance between individuals
    
    
    public boolean isLogScale =false; //fitness in log scale 

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public EAsolver solver = null; //------------------------------------------- solver that evolve this individual
    protected Object genome = null;  //---------------------------------------- genome representation
    protected double value = Double.NaN; //------------------------------------- value of genome
    protected int numberOfCopies = 1; //------------------------------------------ number of genome copys (used in multipopulations)
    protected Optimization optimizationType = Optimization.MAXIMIZE; //--------- type of optimization

    /**
     * constructor
     *
     * @param genome genome object
     * @param optimization type of optimization { Optimization.MAXIMIZE ,
     * Optimization.MINIMIZE }
     */
    protected Individual(Object genome, Optimization optimization) {
        this.genome = genome;
        optimizationType = optimization;
    }

    /**
     * @return the numberOfCopies
     */
    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    /**
     * @param numberOfCopies the numberOfCopies to set
     */
    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    /**
     * @param numberOfCopies the numberOfCopies to set
     */
    public void addNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies += numberOfCopies;
    }

    /**
     * @param nCopys the numberOfCopies to set
     */
    public void addCopies(int nCopys) {
        this.numberOfCopies += nCopys;
    }

    /**
     * @return the optimizationType
     */
    public Optimization getOptimizationType() {
        return optimizationType;
    }

    public void setSolver(EAsolver solver) {
        this.solver = solver;
        if (solver != null) {
            this.random = solver.random;
        }
    }

    /**
     * Evaluate individual
     */
    public double evaluate() {
        if (!isEvaluated()) {
            value = fitness(); // calculate fitness
            if (solver != null) {
                solver.numEvaluations++; //--------------------------------------------- FITNESS CALLS
            }
        }
        return value;
    }

    /**
     * Evaluate individual
     */
    public void setFitness(double fitness) {
        value = fitness;
    }

    /**
     * number of allels
     *
     * @return number of allels
     */
    public int getNumGenes() {
        if (genome.getClass().isArray()) {
            return Array.getLength(genome); // array of genes
        }
        return 1; // by default have 1 gene
    }

    public boolean isMaximize() {
        return optimizationType == Optimization.MAXIMIZE;
    }

    /**
     * evaluate the individual and increase fitness calls
     *
     * @return
     */
    public double getFitness() {
        if (!isEvaluated()) {
            evaluate();//------------------------------------------------------- call evaluate and update fitness calls
        }
        return value;
    }

    public boolean isEvaluated() {
        return !Double.isNaN(value); // not defined
    }

    public void setNotEvaluated() {
        value = Double.NaN; // not defined
    }

    public Object getGenome() {
        return genome;
    }

    public void setGenome(Object newGenome) {
        genome = newGenome;
        setNotEvaluated();
    }

    /**
     * gets the name of the problem
     *
     * @return
     */
    @Override
    public String getName() {
        return getClass().getCanonicalName();
    }

    @Override
    public String toString() {
        return toStringIndividual();
    }

    /**
     * textual representation of the individual
     *
     * @return
     */
    public String toStringGenome() { //----------------------------------------  textual representation of genome
        return genome.toString();
    }

    public String toStringPhenotype() {//--------------------------------------- textual representation of phenotype
        return toStringGenome();
    }

    public String toStringIndividual() {
        StringBuilder txt = new StringBuilder();
        if (numberOfCopies == 1) {
            txt.append("    ");
        } else {
            txt.append(String.format("[%2d]", getNumberOfCopies()));
        }
        if (isEvaluated()) {
            txt.append(MyNumber.numberToString(value, 16) + " = ");
        } else {
            txt.append(MyString.setSize("NOT EVAL", 16) + " = ");
        }
        txt.append(toStringGenome());
        txt.append("\t(" + getClass().getSimpleName() + ")");

        if (isOptimum()) {
            txt.append(" OPTIMUM ");
        }

        return txt.toString();
    }

    public String toStringPhenotypeIndividual() {
        StringBuilder txt = new StringBuilder();
        if (numberOfCopies == 1) {
            txt.append("    ");
        } else {
            txt.append(String.format("[%2d]", getNumberOfCopies()));
        }
        if (isEvaluated()) {
            txt.append(MyNumber.numberToString(value, 12) + " = ");
        } else {
            txt.append(MyString.setSize("NOT EVAL", 12) + " = ");
        }
        txt.append(toStringPhenotype());
        txt.append("\t(" + getClass().getSimpleName() + ")");

        if (isOptimum()) {
            txt.append(" OPTIMUM ");
        }

        return txt.toString();
    }

     @Override
    public int compare(Individual first, Individual second) {
        return first.compareTo(second);
    }
    
    @Override
    public int compareTo(Individual individual) { //----------------------------------- compare To

        if (!individual.isEvaluated()) { // evaluate parameter
            individual.evaluate();
        }
        if (!isEvaluated()) { // evaluate this
            evaluate();
        }

        if (getOptimizationType() == Optimization.MAXIMIZE) {//----------------- MAXIMIZE
            if (this.value > individual.value) {
                return Integer.MAX_VALUE;
            } else if (this.value < individual.value) {
                return Integer.MIN_VALUE;
            }
            return 0;
        } else //------------------------------------------------------------- MINIMIZE
        {
            if (this.value > individual.value) {
                return Integer.MIN_VALUE;
            } else if (this.value < individual.value) {
                return Integer.MAX_VALUE;
            }
            return 0;
        }

    }

    /**
     * compare the genomes of the individuals
     *
     * @param other
     * @return
     */
    public boolean equals(Object other) {//------------------------------------- equals
        return genome.equals(((Individual) other).genome);
    }

    /**
     * creates a raw copy of the individual
     *
     * @return raw copy of this
     */
    public Individual getClone() {//-------------------------------------------- get clone
        Individual ind = (Individual) Genetic.getClone(this);
        ind.setSolver(solver);
        ind.value = value;
        ind.numberOfCopies = numberOfCopies;
        ind.optimizationType = optimizationType;
        return ind;
    }

   

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public enum Optimization { //----------------------------------------------- type of optimization
        MAXIMIZE, // maximize optimization
        MINIMIZE //minimize optimization
    };

    public boolean getAllele(int i) {
        if (genome instanceof Boolean[] && ((boolean[]) genome)[i]) {
            return true;
        }
        return false;
    }

//    public char getAlleleChar(int i) {
//        return getAllele(i) ? '1' : '0';
//    }
    @Override
    public int hashCode() {
        return genome.hashCode();
    }

    public String getInformation() {
        return getProblemDefinition();
    }

    public String getProblemDefinition() {//------------------------------------------ get information
        // public String getInfo() {
        StringBuilder txt = new StringBuilder();
        String p = getParameters();
        p = p.replaceAll(" ", " , ");
        txt.append(getClass().getSimpleName() + "(" + p + ") : ");
        txt.append(getOptimizationType() == Individual.Optimization.MAXIMIZE ? "MAXIMIZE" : "MINIMIZE");
        txt.append(" Genome size " + getNumGenes());
        return txt.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290908L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
