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
public class NumOptimumsFound extends AbstractStatistics {

    public NumOptimumsFound() {
        super("Number of Optimums found");
        higherIsBetter = true;
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    public double execute(EAsolver s) {
        try {
            if (s.hallOfFame.iterator().next().isOptimum()) {
                return s.hallOfFame.size();
            }
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public String getInformation() {
        return "Number of optimums found across evolution"
                + "\n Optimus are saved across evolution";
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
