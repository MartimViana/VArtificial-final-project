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
import java.util.Random;

/**
 * Created on 22/fev/2017, 16:49:51
 *
 * @author zulu - computer
 */
public class SimplePopulation extends Population {

    protected ArrayList<Individual> simplePop = new ArrayList<>();


    /**
     * ad one individual to the population
     *
     * @param ind
     */
    public void addIndividual(Individual ind) {
        ind.setNumberOfCopies(1);
        simplePop.add(ind);
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

    /**
     * remove one individual to the population
     *
     * @param ind
     */
    public void removeIndividual(Individual ind) {
        simplePop.remove(ind);
    }

    /**
     * remove one individual to the population
     *
     * @param ind
     */
    public void removeGenome(Individual ind) {
        simplePop.remove(ind);
    }

    /**
     * size of population
     *
     * @return
     */
    public int getPopulationSize() {
        return simplePop.size();
    }

    public boolean isEmpty() {
        return simplePop.isEmpty();
    }

    /**
     * number of individuals in the population
     *
     * @return
     */
    public int getNumberOfIndividuals() {
        return simplePop.size();
    }

    /**
     * number of allels of the individuals in the population
     *
     * @return
     */
    public int getIndividualsSize() {
        return simplePop.get(0).getNumGenes();
    }

    /**
     * index of the individual in the population
     *
     * @param ind individual to search
     * @return index of individual or -1 if not found
     */
    public int indexOf(Individual ind) {
//        for (int i = 0; i < simplePop.size(); i++) {
//            if (simplePop.get(i).equals(ind)) {
//                return i;
//            }
//        }
//        return -1;
        return simplePop.indexOf(ind);
    }

    @Override
    public boolean contains(Individual ind) {
        return simplePop.contains(ind);
    }

    /**
     * list of individuals (clones)
     *
     * @return List of individuals
     */
    @Override
    public List<Individual> getIndividualsList() {
        return getGenomes();
    }

    /* Array of individuals (not clones)
     *
     * @return Array of individuals
     */
    public List<Individual> getGenomes() {
        return new ArrayList<>(simplePop);
    }

    /**
     * gets the individual in the position i
     *
     * @param i position to get
     * @return individual in the position i
     */
    public Individual getIndividual(int i) {
        return simplePop.get(i);
    }

    public Individual getGenome(int i) {
        return simplePop.get(i);
    }

    /**
     * textual representation
     *
     * @return text
     */
    @Override
    public String toString() {
        if (simplePop.isEmpty()) {
            return maximumSize + " Empty Population";
        }
        // sort();
        StringBuilder txt = new StringBuilder(maximumSize + getClass().getSimpleName() + " ");
        txt.append(" " + getNumberOfIndividuals());
        txt.append(" Individuals  ");
        txt.append(simplePop.get(0).getName() + " ");
        txt.append(" genome size : " + simplePop.get(0).getNumGenes() + "\n");

        for (int i = 0; i < simplePop.size(); i++) {
            txt.append(i + "\t" + simplePop.get(i).toStringIndividual() + "\n");
        }
        return txt.toString();
    }

 
    /**
     * get best individual of the population
     *
     * @return best individual
     */
    public Individual getBest() {
        int max = 0;
        for (int i = 1; i < simplePop.size(); i++) {
            if (simplePop.get(i).compareTo(simplePop.get(max)) > 0) {
                max = i;
            }
        }
        return simplePop.get(max);
    }

    /**
     * get best individual of the population
     *
     * @return best individual
     */
    public ArrayList<Individual> getAllBest() {
        ArrayList<Individual> bestPop = new ArrayList<>();
        double bestValue = getBest().getFitness();
        for (int i = 0; i < simplePop.size(); i++) {
            if (simplePop.get(i).getFitness() == bestValue) {
                bestPop.add(simplePop.get(i));
            }
        }
        return bestPop;
    }

    public Individual getRandomIndividual() {
        return simplePop.get(random.nextInt(simplePop.size()));
    }

    public Individual removeRandomIndividual() {
        Individual rnd = getRandomIndividual();
        removeIndividual(rnd);
        return rnd;
    }

    /**
     * gets an population object clone without individuals
     *
     * @return
     */
    public SimplePopulation getCleanClone() {
        SimplePopulation pop = (SimplePopulation) Genetic.getClone(this);
        pop.maximumSize = maximumSize;
        return pop;
    }

    /**
     * gets a population clone
     *
     * @return
     */
    public SimplePopulation getClone() {
        SimplePopulation clone = getCleanClone();
        clone.maximumSize = maximumSize;
        for (Individual ind : simplePop) {
            clone.simplePop.add(ind.getClone());
        }
        return clone;
    }

    /**
     * update parameters definition
     *
     * @param params individual parameters
     */
    @Override
    public void setRandomGenerator(Random rnd) {
        super.setRandomGenerator(rnd);
        for (Individual individual : simplePop) {
            individual.setRandomGenerator(rnd);
        }
    }

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
        //do nothing
        return "" + maximumSize;
    }

    public void clear() {
        this.simplePop.clear();
    }

   

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201702221649L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Individual getIndex(int index) {
        return simplePop.get(index);
    }
}
