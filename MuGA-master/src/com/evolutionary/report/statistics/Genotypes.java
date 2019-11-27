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
import com.evolutionary.solver.EAsolver;
import java.util.TreeSet;

/**
 * Created on 19/jan/2016, 12:08:40
 *
 * @author zulu - computer
 */
public class Genotypes extends AbstractStatistics {

    public Genotypes() {
        super("Genotypes");
        higherIsBetter=false;
    }

       /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    public double execute(EAsolver s) {
        if( s.parents instanceof MultiPopulation)
        return s.parents.getPopulationSize();
        
        MultiPopulation mp = new MultiPopulation();
        mp.addAll(s.parents.getIndividualsList());
        return mp.getPopulationSize();
    }
    @Override
    public String getInformation() {        
        return "Number of Genotypes in parents population\n";        
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
