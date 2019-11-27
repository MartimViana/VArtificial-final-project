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
import com.evolutionary.problem.Individual;
import java.util.Collections;
import java.util.List;

public class CrowdingMafoud extends Crowding {

    @Override
    public void iterate() {
        List<Individual> individuals = parents.getIndividualsList();
        Collections.shuffle(individuals);

        Recombination crossover = (Recombination) recombination;
        Mutation variation = (Mutation) mutation;
        Individual parent1, parent2;
        //number of iterations before compute statistics
        for (int i = 0; i < individuals.size() - 1; i += 2) {
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //select two random individuals
            parent1 = individuals.get(i);
            parent2 = individuals.get(i + 1);
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

    public String getInformation() {
        StringBuilder str = new StringBuilder();
        str.append("\n Mafoud Crowding ");
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
