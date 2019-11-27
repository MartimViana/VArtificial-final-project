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

import com.evolutionary.Genetic;
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
public class ClearingReplacementElit extends Replacement {

    @Override
    public Population execute(Population parents, Population offspring) {
        //size of population
        int parentsSize = parents.getPopulationSize();
        //join population  
        offspring.addAll(parents.getIndividualsList());
        //list of individuals - no duplicates - HASH SET
        List<Individual> allPop = new ArrayList<>(new HashSet<>(offspring.getGenomes()));
        //sort descending
        Collections.shuffle(allPop, random); // randomize elements
        Collections.sort(allPop);    // sort is stable so shuffle is necessary to avoid bias
        Collections.reverse(allPop); // descending order

        //clear similars 
        int indexWinner = 0;//winner index
        int indexSimilar = 0; // similar index
        double dist, minDist; //distances
        //while have genomes to slaught
        while (allPop.size() > parentsSize && indexWinner < parentsSize) {
            indexSimilar = allPop.size() - 1;//most similar is the next one   
            minDist = dist = allPop.get(indexWinner).distanceTo(allPop.get(indexSimilar));
            //teste similarity of the others individulas 
            for (int index = allPop.size()-2; index > indexWinner; index--) {
                dist = allPop.get(indexWinner).distanceTo(allPop.get(index));
                if (dist < minDist
                        && allPop.get(index).compareTo(allPop.get(indexWinner)) < 0) {
                    //save most similar  
                    minDist = dist;
                    indexSimilar = index;
                }
            }
            allPop.remove(indexSimilar);//remove most similar
            indexWinner++; // choose next Winner
        }

        //copy individuals to the new population
        parents.clear();
        for (int i = 0; i < parentsSize; i++) {
            parents.addIndividual(allPop.get(i), allPop.get(i).getNumberOfCopies());
        }

        return parents;
    }

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(Genetic.getFullName(this));

        txt.append("\nReplaces a generation using Clearing \n");
        txt.append("\n");
        txt.append("\nallPop = sort ( parents + offspring )");
        txt.append("\nindexWinner = 0");
        txt.append("\nWhile allPop.size > parents.size and indexWiner < parents.size");
        txt.append("\n    winner = allPop[indexWinner]");
        txt.append("\n    indexWinner = indexWinner + 1");
        txt.append("\n    looser =  mostSimilar(winner, allPop[indexWinner, allPop.size] )");
        txt.append("\n    allPop.remove(looser)");
        txt.append("\n");
        txt.append("\nparents = allPop[0, parents.size]");

        return txt.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201904171215L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
