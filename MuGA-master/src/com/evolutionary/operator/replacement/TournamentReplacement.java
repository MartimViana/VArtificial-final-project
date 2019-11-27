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

package com.evolutionary.operator.replacement;

import com.evolutionary.Genetic;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import java.util.List;

/**
 * Created on 1/out/2015, 12:40:50
 *
 * @author zulu - computer
 */
public class TournamentReplacement extends Replacement {

    @Override
    public synchronized Population execute(Population parents, Population offspring) {
        offspring.evaluate();
        //clean population
        Population newPop = parents.getCleanClone();
        //size of population
        int SIZE = parents.getPopulationSize();
        //list of offsprings
        List<Individual> childs = offspring.getIndividualsList();
        //list of parents
        List<Individual> progen = parents.getIndividualsList();
        // while new population is not completed
        while (newPop.getPopulationSize() < SIZE && !childs.isEmpty()) {
            //get random child
            Individual c = childs.get(random.nextInt(childs.size()));
            //get random parent
            Individual p = progen.get(random.nextInt(progen.size()));
            //select best one
            if (p.compareTo(c) > 0) {
                newPop.addIndividual(p);
                progen.remove(p);
            } else {
                newPop.addIndividual(c);
                childs.remove(c);
            }
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        // complete population if necessary        
        if (newPop.getPopulationSize() < SIZE) {            
            //complete population if needed
            while (newPop.getPopulationSize() < SIZE) {
                //get random
                 newPop.addIndividual(progen.remove(random.nextInt(progen.size())));
            }
        }

        return newPop;
    }

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(Genetic.getFullName(this));
        txt.append("\n" + getClass().getSimpleName() + "( )\n");

        txt.append("\n\nTournament selection:");
        txt.append("\nSelect make Torunament between parents and offspring");
        txt.append("\nGet a random parent and a random offspring");
        txt.append("\nSelect the best one");
        return txt.toString();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510011240L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
