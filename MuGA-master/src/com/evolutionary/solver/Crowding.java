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
import com.evolutionary.problem.Individual;

public class Crowding extends EAsolver {

    public Crowding() {
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
            replaceByCrowding(parent1, child1);
            replaceByCrowding(parent2, child2);
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //rescale copies

        }
        //update statistics
        parents = rescaling.execute(parents);
        updateEvolutionStats();
    }

    public void replaceByCrowding(Individual parent, Individual descendant) {
        //the parent is worst than the descendant   
        if (descendant.compareTo(parent) >= 0) {
            // is multipopulation
            if (parents instanceof MultiPopulation) {
                //population not contains descendant - replace parent to descendant
                if (!parents.contains(descendant)) {
                    // parent could be not in the population
                    // because the same parent could be the same seed of 2 childrens
                    // and parent is eliminated by the first children replace
                    if (parents.contains(parent)) {
                        parents.removeGenome(parent);
                        parents.addIndividual(descendant);
                    }
                } else {
                    parents.addIndividual(descendant);
                }
            } else if (!(parents instanceof MultiPopulation)) {
                //replace parent by descendant
                parents.removeIndividual(parent);
                parents.addIndividual(descendant);
            }
        }

    }

    public String getInformation() {
        StringBuilder str = new StringBuilder();
        str.append("\n Crowding");
        str.append("\n create random POP");
        str.append("\n evaluate POP");
        str.append("\n until STOP criteria");
        str.append("\n    Select two parents P1 and P2");
        str.append("\n    C1,C2 = recombine(P1,P2)");
        str.append("\n    C1,C2 = mutate(C1,C2)");
        str.append("\n    evaluate C1 and C2");
        str.append("\n    Replace P1 by C1 if better");
        str.append("\n    Replace P2 by C2 if better");
        return str.toString();
    }

}
