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

package com.evolutionary.stopCriteria;

import com.evolutionary.solver.EAsolver;

/**
 * Created on 3/out/2015, 18:46:24
 *
 * @author zulu - computer
 */
public class MaxEvaluations extends StopCriteria {

    public MaxEvaluations() {
    }

    public MaxEvaluations(double max) {
        this.stopValue = (int) max;
    }
    
    public double completedRatio(EAsolver solver) {
        return solver.numEvaluations/(double) stopValue;
    }

    @Override
    public boolean isDone(EAsolver solver) {
        return solver.numEvaluations >= stopValue;
    }

    public String getInformation() {
        StringBuilder buf = new StringBuilder(getSimpleName() + "\n");
        buf.append(this.getClass().getSimpleName());
        buf.append("Terminates when executed <MAX> evaluations");
        buf.append("\nParameter <MAX> : max evaluations");
        return buf.toString();
    }
//----------------------------------------------------------------------------------------------------------
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031846L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
