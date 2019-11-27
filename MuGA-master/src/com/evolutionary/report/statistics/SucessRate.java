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
 * Created on 15/mar/2016, 16:24:09
 *
 * @author zulu - computer
 */
public class SucessRate extends AbstractStatistics {

    public SucessRate() {
        super("Sucess Rate");
        higherIsBetter = true;
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    public double execute(EAsolver s) {
        return s.hallOfFame.iterator().next().isOptimum() ? 1 : 0;
    }

    @Override
    public String getInformation() {
        return "Sucess Rate"
                + "\nverify if the evolution found optimal value";
    }
    
    

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603151624L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
