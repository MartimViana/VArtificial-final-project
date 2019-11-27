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
package com.evolutionary.operator.mutation;

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;

/**
 * Created on 15/mai/2019, 10:37:09
 *
 * @author zulu - computer
 */
public abstract class MultisetMutation extends Mutation {

    public void mutate(Individual offspring) {
        //do nothing
    }

    public abstract void mutate(int index, Individual offspring);

    protected double pMutation = -1; // default probability of mutation

    public Population execute(Population offspring) {
        //clean population  
        Population newPopulation = offspring.getCleanClone();
        //Mutate the individuals
        for (Individual solution : offspring.getIndividualsList()) {
            for (int i = 0; i < solution.getNumberOfCopies(); i++) {
                solution = solution.getClone();
                mutate(i, solution);
                newPopulation.addIndividual(solution);

            }

        }
        return newPopulation;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201905151037L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
