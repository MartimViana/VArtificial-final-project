//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2019   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.operator.replacement;

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created on 17/abr/2019, 12:15:55
 *
 * @author zulu - computer
 */
public class ReplaceMostSimilar extends Replacement {

    @Override
    public Population execute(Population parents, Population offspring) {
        //size of population
        int SIZE = parents.getMaximumSize();

        parents.addAll(offspring.getIndividualsList());
        //list of individuals - no duplicates - HASH SET
        List<Individual> inds = new ArrayList<>(new HashSet<>(parents.getGenomes()));
        //sort descending
        Collections.shuffle(inds,random); // randomize elements
        Collections.sort(inds);    // sort is stable so randomize is necessary
        Collections.reverse(inds); // descencding order

        parents.clear();

        Individual winner;
        while (parents.getPopulationSize() < SIZE) {
            winner = inds.remove(0);
            parents.addIndividual(winner, winner.getNumberOfCopies());            
            if (parents.getPopulationSize() + inds.size() > SIZE) {
                removeMostSimilar(inds, winner);
            } else {
                parents.addAll(inds);
                break;
            }
        }

        return parents;
    }

    private void removeMostSimilar(List<Individual> pop, Individual winner) {
        //most similar is the first    
        Individual similar = pop.get(0);
        int index = 0;
        double dist, minDist = winner.distanceTo(similar);
        //calculate most similar
        for (int i = 1; i < pop.size(); i++) {
            dist = similar.distanceTo(pop.get(i));
            if (dist == 0) { // is the same individual
                break;
            }
            if (dist <= minDist) {// distance is smaller  (and is worst because pop is sorted)
                //save most similar  
                index = i;
                minDist = dist;
                similar = pop.get(i);
            }
        }
        //remove most similar
        pop.remove(index);

    }
    
   
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201904171215L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
