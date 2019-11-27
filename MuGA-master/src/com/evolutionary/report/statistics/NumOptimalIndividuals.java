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
import java.util.ArrayList;

/**
 * Created on 19/jan/2016, 12:08:40
 *
 * @author zulu - computer
 */
public class NumOptimalIndividuals extends AbstractStatistics {

    public NumOptimalIndividuals() {
        super("Number of optimal individuals in population");
        higherIsBetter = false;
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    @Override
    public double execute(EAsolver s) {
        ArrayList<Individual> best = s.parents.getAllBest();
        if (!best.get(0).isOptimum()) {
            return 0;
        }
        int total = 0;
        for (Individual individual : best) {
            total += individual.getNumberOfCopies();
        }
        return total;
    }

    @Override
    public String getInformation() {
        return "Number of optimal individuals in the parents Population"
                + "\n Count the clones of the best individuals ";
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
