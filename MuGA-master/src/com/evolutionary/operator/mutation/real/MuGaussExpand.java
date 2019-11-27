//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2015   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.operator.mutation.real;

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import java.util.List;

/**
 * Created on 2/out/2015, 7:50:41
 *
 * @author zulu - computer
 */
public class MuGaussExpand extends Gauss {

    /**
     * gets the dimension of the domain contracted by the individual number of
     * copy
     *
     * @param ind
     * @return
     */
    @Override
    public double getScaledDomain(RealVector ind) {
        return ((ind.getMaxValue() - ind.getMinValue()) * FACTOR)
                /  ind.getNumberOfCopies();
    }
    
     /**
     * probability of mutation
     *
     * @param ind multi.individual
     * @return probability increased by the number of copies
     */
    public double getMutProbability(Individual ind) {
        if (pMutation == -1) {
            return (0.1 * ind.getNumberOfCopies()) / ind.getNumGenes(); // 1.0 / 20.0            
        }
        return pMutation;
    }

    @Override
    public Population execute(Population pop) {
        //----------------------------------------------------------------
        List<Individual> children = pop.getGenomes();
        //make new population
        Population offspring = pop.getCleanClone();
        for (Individual descendant : children) {
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //multiset population
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            for (int i = 0; i < descendant.getNumberOfCopies(); i++) {
                //clone de individual and update copynumber
                Individual clone = descendant.getClone();
                clone.setNumberOfCopies(i + 1);
                //mutate the clone
                mutate(clone);
                //add clone to the new population
                offspring.addIndividual(clone, 1);
            }
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            // Simple INDIVIDUAL  - single mutation
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::          
            //add to the new population
            offspring.addIndividual(descendant, 1);
            //add to the new population
        }
        return offspring;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 2019050181402L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
