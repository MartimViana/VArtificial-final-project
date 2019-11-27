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
package com.evolutionary.population;

import com.evolutionary.Genetic;
import com.evolutionary.problem.Individual;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Population - Collection of individuals
 *
 * @author zulu
 */
public abstract class Population extends Genetic {

    protected int maximumSize = 64; // maximum of individuals

    /**
     * ad one individual to the population
     *
     * @param ind
     */
    public abstract void addIndividual(Individual ind);

    /**
     * gets the individual in the position i
     *
     * @param i position to get
     * @return individual in the position i
     */
    public abstract Individual getIndividual(int i);

    /**
     * gets the individual in the position i
     *
     * @param i position to get
     * @return individual in the position i
     */
    public abstract Individual getGenome(int i);

    /**
     * remove one individual to the population
     *
     * @param ind
     */
    public abstract void removeIndividual(Individual ind);

    public abstract void removeGenome(Individual ind);

    /**
     * size of population
     *
     * @return number of elements
     */
    public abstract int getPopulationSize();

    public abstract boolean isEmpty();

    /**
     * number of individuals in the population
     *
     * @return
     */
    public abstract int getNumberOfIndividuals();

    /**
     * number of allels of the individuals in the population
     *
     * @return
     */
    public abstract int getIndividualsSize();

  
    /**
     * get the individual in the index
     *
     * @param index number of the infex
     * @return Individual
     */
    public abstract Individual getIndex(int index);

    /**
     * verify if the population constains the individual
     *
     * @param ind individual
     * @return true if the population contains the individual
     */
    public abstract boolean contains(Individual ind);

    /**
     * list of individuals (expanded)
     *
     * @return List of individuals
     */
    public abstract List<Individual> getIndividualsList();

    /* Array of individuals (not expanded)
     *
     * @return Array of individuals
     */
    public abstract List<Individual> getGenomes();

    public int getMaximumSize() {
        return maximumSize;
    }

//    /**
//     * create random individuals
//     *
//     * @param template instance of proble
//     * @param N number of individuals in the population
//     */
//    public abstract void createRandom(Individual template);
    /**
     * create random individuals
     *
     * @param template instance of proble
     */
    public void createRandom(Individual template) {
        createRandom(template, maximumSize);
    }

    /**
     * create random individuals
     *
     * @param template instance of proble
     */
    public void createRandom(Individual template, int size) {
        clear(); // clear population
        this.random = template.random;
        Individual tmp = template.getClone();
        int tries = 3 * size; // number of tries to create unique individuals
        while (getPopulationSize() < size && tries-- > 0) {
            tmp.randomize(); // randomize genome
            if (!contains(tmp)) {
                addIndividual(tmp.getClone());
            }
        }
        maximumSize = getPopulationSize();
    }

    /**
     * ad one individual to the population
     *
     * @param ind
     */
    public void addAll(Collection<Individual> pop) {
        for (Individual ind : pop) {
            addIndividual(ind);
        }
    }

    public void addIndividual(Individual ind, int copies) {
        addIndividual(ind);
        for (int i = 1; i < copies; i++) {
            addIndividual(ind.getClone());
        }
    }

    /**
     * gets the number of copies of one individuao
     *
     * @param ind
     * @return
     */
    public int getCopies(Individual ind) {
        return 1; // by default is one
    }

    public Individual getMostSimilar(Individual sol) {
        Collection<Individual> genomes = getGenomes();
        double dif, maxDif = Double.POSITIVE_INFINITY;
        Individual best = null;
        for (Individual genome : genomes) {
            dif = sol.distanceTo(genome);
            if (dif == 0.0) { // found genome
                return genome;
            }
            if (dif < maxDif) { // compare dif
                best = genome;
                maxDif = dif;
            }
        }
        return best;
    }

    /**
     * Evaluate all the individuals in the population this method increase the
     * number of fitness evaluations of solver
     */
    public void evaluate() {
        for (Individual ind : getGenomes()) {
            ind.evaluate();
        }
    }

    /**
     * get best individual of the population
     *
     * @return best individual
     */
    public abstract Individual getBest();

    /**
     * get best individual of the population
     *
     * @return best individual
     */
    public abstract ArrayList<Individual> getAllBest();

    public abstract Individual getRandomIndividual();

    public abstract Individual removeRandomIndividual();

    public Individual getTemplate() {
        return getRandomIndividual();
    }

    /**
     * gets an population object clone without individuals
     *
     * @return
     */
    public abstract Population getCleanClone();

    /**
     * update parameters definition
     *
     * @param params individual parameters
     */
    public void setParameters(String params) throws RuntimeException {
        try {
            //update maximum size
            maximumSize = Integer.parseInt(params);
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param params individual parameters
     */
    public String getParameters() {
        return "" + maximumSize;
    }

    public abstract void clear();

    public abstract Population getClone();

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290908L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
