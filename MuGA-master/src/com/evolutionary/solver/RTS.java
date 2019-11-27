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
package com.evolutionary.solver;

import com.evolutionary.operator.mutation.Mutation;
import com.evolutionary.operator.recombination.Recombination;
import com.evolutionary.operator.replacement.NoReplacement;
import com.evolutionary.operator.selection.NoSelection;
import com.evolutionary.operator.selection.Selection;
import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RTS extends EAsolver {

    double windowSize = 0.25;

    public RTS() {
        selection = new NoSelection();
        replacement = new NoReplacement();
    }

    @Override
    public void iterate() {
        Recombination crossover = (Recombination) recombination;
        Mutation variation = (Mutation) mutation;
        Individual parent1, parent2;
        int index1, index2;
        //number of iterations before compute statistics
        int maxIterations = (int) (((Selection) selection).getParentProportion() * parents.getPopulationSize()) / 2;
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //select two random individuals
            index1 = random.nextInt(parents.getNumberOfIndividuals());
            do {
                index2 = random.nextInt(parents.getNumberOfIndividuals());
            } while (index1 == index2);

            parent1 = parents.getIndex(index1);
            parent2 = parents.getIndex(index2);
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            // make clones
            Individual child1 = parent1.getClone();
            Individual child2 = parent2.getClone();
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //recombine
            crossover.recombine(child1, child2);
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //mutate
            variation.mutate(child1);
            variation.mutate(child2);
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //evaluate
            child1.evaluate();
            child2.evaluate();
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //replace parents
            replaceByRTS(parents, child1, windowSize, random);
            replaceByRTS(parents, child2, windowSize, random);

        } //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //rescale copies
        parents = rescaling.execute(parents);
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //update statistics
        updateEvolutionStats();
    }

    /**
     * replace
     *
     * @param pop
     * @param descendant
     */
    public static void replaceByRTS(Population pop, Individual descendant, double windowSize, Random random) {
        // is multipopulation
        if (pop instanceof MultiPopulation) {
            //population  contains descendant
            if (pop.contains(descendant)) {
                //add one copy
                pop.addIndividual(descendant);
                return;
            }
        }

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //create a window to select most similar
        //get individuals list
        List<Individual> tour = pop.getGenomes();
        //shuffle
        Collections.shuffle(tour,random); // shuffle population
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //size of tournament
        double tournamentSize = (tour.size() * windowSize);        
        //random individual
        Individual mostSimilar = tour.get(0); // first individual
        double distance = descendant.distanceTo(mostSimilar);
        
        //probe random individuals
        for (int i = 1; i < tournamentSize; i++) { //compare to other individuals
            Individual pivot = tour.get(i);
            if (pivot.distanceTo(descendant) < distance) {
                distance = pivot.distanceTo(descendant);
                mostSimilar = pivot;
            }
        }
        //if descendent is equal or better replace  parent
        if (descendant.compareTo(mostSimilar) >= 0) {
            pop.removeGenome(mostSimilar);
            pop.addIndividual(descendant);
        }

    }

    //----------------------------------------------------------------------------------------------------------
    @Override
    public String getParameters() {
        return windowSize + "";
    }

    /**
     * update parameters of the operator
     *
     * @param params parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    @Override
    public void setParameters(String params) throws RuntimeException {
        try {
            windowSize = Double.parseDouble(params);
        } catch (Exception e) {
        }
    }//----------------------------------------------------------------------------------------------------------

    public String getInformation() {
        StringBuilder str = new StringBuilder();
        str.append("\n RTS ");

        return str.toString();
    }

}
