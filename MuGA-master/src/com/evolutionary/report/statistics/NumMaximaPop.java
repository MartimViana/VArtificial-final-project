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
package com.evolutionary.report.statistics;

import com.evolutionary.problem.Individual;
import com.evolutionary.solver.EAsolver;
import java.util.Collection;

/**
 * Created on 19/jan/2016, 12:08:40
 *
 * @author zulu - computer
 */
public class NumMaximaPop extends AbstractStatistics {

    public NumMaximaPop() {
        super("Number of maxima in parents");
        higherIsBetter = true;
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    public double execute(EAsolver s) {
        Collection<Individual> best = s.parents.getAllBest();

        if (best.iterator().next().isOptimum()) {
           // if (s.parents instanceof MultiPopulation) {
                int total = 0;
                for (Individual individual : best) {
                    total += individual.getNumberOfCopies();
                }
                return total;
//            } else {
//                return best.size();
//            }
        }
        return 0;
    }

    @Override
    public String getInformation() {
        return "Number of individuals with best fitness "
                + "\nin parent population";
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
