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

import com.evolutionary.solver.EAsolver;

/**
 * Created on 19/jan/2016, 12:08:40
 *
 * @author zulu - computer
 */
public class BestFitness extends AbstractStatistics {

    public BestFitness() {
        super("Best Fitness");        
        logScaleEnabled = true; // enable log scale in statistics view
        
    }

       /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    @Override
    public double execute(EAsolver s) {        
        return s.parents.getBest().getFitness();
        
    }
     public void setSolver(EAsolver s){
         higherIsBetter = s.problem.isMaximize();
     }
    
    @Override
    public String getInformation() {
       return "Best Fitness in parents population\n";
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
