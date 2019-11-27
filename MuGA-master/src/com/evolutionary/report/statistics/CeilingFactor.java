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

import com.evolutionary.operator.GeneticOperator;
import com.evolutionary.operator.rescaling.AdaptiveCeiling;
import com.evolutionary.operator.rescaling.FixedCeiling;
import com.evolutionary.solver.EAsolver;

/**
 * Created on 19/jan/2016, 12:08:40
 *
 * @author zulu - computer
 */
public class CeilingFactor extends AbstractStatistics {

    public CeilingFactor() {
        super("Ceiling Factor");
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
        GeneticOperator r = s.rescaling;
        if (r instanceof AdaptiveCeiling) {
            return ((AdaptiveCeiling) r).getDivisionFator(s.parents);
        }
        if (r instanceof FixedCeiling) {
            return ((FixedCeiling) r).getFator(s.parents);
        }
        return 1;
    }

    @Override
    public String getInformation() {
        return "Rescaling Factor";
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
