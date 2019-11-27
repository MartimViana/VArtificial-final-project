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
import com.evolutionary.operator.selection.Selection;
import com.evolutionary.problem.Individual;

public class CrowdingStep extends Crowding {

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
            // C A L C U L A T E    D I S T A N C E S
            double m1 = child1.distanceTo(parent1) + child2.distanceTo(parent2);
            double m2 = child1.distanceTo(parent2) + child2.distanceTo(parent1);
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::           
            //replace parents based on distances
            if (m1 < m2) {
                replaceByCrowding(parent1, child1);
                replaceByCrowding(parent2, child2);
            } else if (m1 > m2) {
                replaceByCrowding(parent1, child2);
                replaceByCrowding(parent2, child1);
            }

        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //rescale copies
        parents = rescaling.execute(parents);
        updateEvolutionStats();
    }

    public String getInformation() {
        StringBuilder str = new StringBuilder();
        str.append("\n Deterministic Crowding ");
        str.append("\n create random POP");
        str.append("\n evaluate POP");
        str.append("\n until STOP criteria");
        str.append("\n    Select two parents P1 and P2");
        str.append("\n    C1,C2 = recombine(P1,P2)");
        str.append("\n    C1,C2 = mutate(C1,C2)");
        str.append("\n    evaluate C1 and C2");
        str.append("\n    empair C and P based in distance");
        str.append("\n    replace C with P if is better");
        return str.toString();
    }

}
