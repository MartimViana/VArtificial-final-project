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

import com.evolutionary.population.MultiPopulation;
import com.evolutionary.problem.Individual;
import com.evolutionary.solver.EAsolver;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created on 19/jan/2016, 12:08:40
 *
 * @author zulu - computer
 */
public class NumOptimalGenomesPop extends AbstractStatistics {

    public NumOptimalGenomesPop() {
        super("Number of optimal genomes in population");
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
        if (s.parents instanceof MultiPopulation) {
            return best.size();
        }
        MultiPopulation pop = new MultiPopulation();
        pop.addAll(best);
        return pop.getPopulationSize();
        //count different genotypes
        
    }

    @Override
    public String getInformation() {
        return "Number of different optimal genomes in the parents Population"
                + "\n";
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
