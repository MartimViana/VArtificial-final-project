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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 5/out/2015, 18:52:43
 *
 * @author zulu - computer
 */
public class MultiPopulation extends Population {

    /**
     * Hashtable with individuals [key = individual] {Value = Individual with
     * number of copies ]
     *
     * KEY - genome of individual VALUE - complete individual with number of
     * copies
     *
     *
     */
    protected HashMap<Individual, Individual> multiset = new HashMap<>((int) (maximumSize * 1.5));
    int numberOfIndividuals = 0;

    /**
     * number of individuals in the population
     *
     * @return
     */
    @Override
    public int getNumberOfIndividuals() {
        return numberOfIndividuals;
    }

    public void setCopies(Individual ind, int copies) {
        Individual mInd = multiset.get(ind);
        numberOfIndividuals -= mInd.getNumberOfCopies() - copies;
        mInd.setNumberOfCopies(copies);
    }

    public int getCopies(Individual ind) {
        return multiset.get(ind).getNumberOfCopies();
    }

    /**
     * add one individual to the population if the individual exists in the
     * population the number of copies is increased
     *
     * @param ind
     */
    @Override
    public void addIndividual(Individual ind) {
        addIndividual(ind, 1);
    }

    @Override
    public void addIndividual(Individual ind, int copies) {

        Individual mInd = multiset.get(ind);
        if (mInd != null) {
            if (ind.isEvaluated()) {
                mInd.setFitness(ind.getFitness());
            }
            mInd.addCopies(copies);
        } else {
            ind.setNumberOfCopies(copies);
            multiset.put(ind, ind);
        }

        numberOfIndividuals += copies;
    }

    /**
     * remove one individual to the population if the individual exists in the
     * population the number of copies is decreased
     *
     * @param ind
     */
    @Override
    public void removeIndividual(Individual ind) {
        Individual mInd = multiset.get(ind);
        if (mInd.getNumberOfCopies() > 1) {
            mInd.addNumberOfCopies(-1);
        } else {
            multiset.remove(ind);
        }
        numberOfIndividuals--;
    }

    /**
     * remove one individual to the population if the individual exists in the
     * population the number of copies is decreased
     *
     * @param ind
     */
    @Override
    public void removeGenome(Individual ind) {
        Individual mInd = multiset.get(ind);
        if (mInd == null) {
            throw new RuntimeException("Individual not found " + ind);
        }
        multiset.remove(ind);
        numberOfIndividuals -= mInd.getNumberOfCopies();
    }

    /**
     * expand multi individuals to array of single individuals
     *
     * @return
     */
    @Override
    public List<Individual> getIndividualsList() {
        List<Individual> lst = new ArrayList<>(getNumberOfIndividuals());
        for (Individual mInd : multiset.values()) {
            int copies = mInd.getNumberOfCopies();
            Individual ind = mInd.getClone();
            ind.setNumberOfCopies(1); // update number of copies           
            lst.add(ind);
            for (int i = 1; i < copies; i++) {
                lst.add(ind.getClone());
            }
        }
        return lst;
    }

    public Individual getIndex(int indIndex) {
        int index = 0;
        for (Individual mInd : multiset.values()) {
            index += mInd.getNumberOfCopies();
            if (indIndex <= index) {
                if (mInd.getNumberOfCopies() > 1) {
                    mInd = mInd.getClone();
                }
                mInd.setNumberOfCopies(1);
                return mInd;
            }
        }
        throw new RuntimeException("getIndividualInPlainIndex - Index not defined " + index);
    }

    @Override
    public Individual getRandomIndividual() {
        int index = random.nextInt(getNumberOfIndividuals());
        return getIndex(index);
    }

    @Override
    public Individual removeRandomIndividual() {
        Individual rnd = getRandomIndividual();
        removeIndividual(rnd);
        return rnd;
    }

    /**
     * ad one individual to the population
     *
     * @param ind
     */
    @Override
    public void addAll(Collection<Individual> pop) {
        for (Individual ind : pop) {
            if (contains(ind)) {
                addIndividual(ind, 1);
            } else {
                addIndividual(ind.getClone(), 1);
            }
        }
    }

    /**
     * size of population
     *
     * @return
     */
    @Override
    public int getPopulationSize() {
        return multiset.size();
    }

    @Override
    public boolean isEmpty() {
        return multiset.isEmpty();
    }

    /**
     * number of allels of the individuals in the population
     *
     * @return
     */
    @Override
    public int getIndividualsSize() {
        return multiset.keySet().iterator().next().getNumGenes();
    }

    @Override
    public boolean contains(Individual ind) {
        return multiset.containsKey(ind);
    }

    /* Array of individuals (not clones)
     *
     * @return Array of individuals
     */
    @Override
    public List<Individual> getGenomes() {
        return new ArrayList(multiset.values());
    }

    /* Array of individuals (not clones)
     *
     * @return Array of individuals
     */
    public Collection<Integer> getCopies() {
        ArrayList<Integer> lst = new ArrayList<>(multiset.values().size());
        for (Individual mInd : multiset.values()) {
            lst.add(mInd.getNumberOfCopies());
        }
        return lst;
    }

    /**
     * gets the individual in the position i
     *
     * @param i position to get
     * @return individual in the position i
     */
    public Individual getIndividual(int indIndex) {
        int index = 0;
        for (Individual mInd : multiset.values()) {
            index += mInd.getNumberOfCopies();
            if (indIndex <= index) {
                if (mInd.getNumberOfCopies() > 1) {
                    mInd = mInd.getClone();
                }
                mInd.setNumberOfCopies(1);
                return mInd;
            }
        }
        throw new RuntimeException("getIndividualInPlainIndex - Index not defined " + index);

    }

    public Individual getGenome(int i) {
        Iterator<Individual> it = multiset.values().iterator();
        for (int j = 0; j < i; j++) {
            it.next();
        }
        return it.next();
    }

//    }
    /**
     * textual representation
     *
     * @return text
     */
    @Override
    public String toString() {
        if (multiset.isEmpty()) {
            return "Empty Population";
        }
        // sort();
        StringBuilder txt = new StringBuilder(getClass().getSimpleName() + " ");
        txt.append(" [" + multiset.size() + "]");
        txt.append(multiset.keySet().iterator().next().getName());
        txt.append(" ->  " + getNumberOfIndividuals() + "\n");

        int i = 0;
        for (Individual s : multiset.keySet()) {
            txt.append((i++) + "\t" + s.toStringIndividual() + "\n");
//            txt.append((i++) + "\t[");
//            txt.append(String.format("%3d", multiset.get(s)) + "] " + s.toStringIndividual() + "\n");
        }
        return txt.toString();
    }

    /**
     * get best individual of the population
     *
     * @return best individual
     */
    @Override
    public Individual getBest() {
        Iterator<Individual> it = multiset.values().iterator();
        Individual best = it.next();
        while (it.hasNext()) {
            Individual s = it.next();
            if (best.compareTo(s) < 0) {
                best = s;
            }
        }
        return best;
    }

    /**
     * get best individual of the population
     *
     * @return best individual
     */
    @Override
    public ArrayList<Individual> getAllBest() {
        ArrayList<Individual> bestPop = new ArrayList<>();
        Iterator<Individual> it = multiset.values().iterator();
        Individual best = it.next();
        bestPop.add(best);
        while (it.hasNext()) {
            Individual s = it.next();
            int compare = best.compareTo(s);
            if (compare < 0) {
                bestPop.clear();
                best = s;
                bestPop.add(s);
            }
            if (compare == 0) {
                bestPop.add(s);
            }
        }
        return bestPop;
    }

    /**
     * gets an population object clone without individuals
     *
     * @return
     */
    @Override
    public MultiPopulation getCleanClone() {
        MultiPopulation pop = (MultiPopulation) Genetic.getClone(this);
        pop.maximumSize = maximumSize;
        return pop;
    }

    /**
     * gets a population clone
     *
     * @return
     */
    @Override
    public MultiPopulation getClone() {

        MultiPopulation clone = getCleanClone();
        clone.maximumSize = maximumSize;
        clone.numberOfIndividuals = numberOfIndividuals;
        for (Individual s : multiset.keySet()) {
            clone.multiset.put(s, multiset.get(s));
        }
        return clone;
    }

    @Override
    public void clear() {
        this.multiset.clear();
        this.numberOfIndividuals = 0;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290908L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
