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

/**
 * Created on 19/jan/2016, 12:08:40
 *
 * @author zulu - computer
 */
public class FuncsToOptimumN extends AbstractStatistics {

    int numOptimal = 1;

    public FuncsToOptimumN() {
        super("Number of avaluations for N optimal");
        this.higherIsBetter = false;
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    @Override
    public double execute(EAsolver s) {
        double value = s.report.getLastValue(this);
        Individual best = s.hallOfFame.iterator().next();
        if (best.isOptimum()) {
            if (value == 0 && s.hallOfFame.size() == numOptimal) {
                return s.numEvaluations;
            } else {
                return value;
            }
        }
        return 0;
    }

    @Override
    public String getInformation() {
        return "Number of calls to the evaluation function\n to find 'N' optimal solutions";
    }

    public void setParameters(String params) throws RuntimeException {
        try {
            numOptimal = Integer.parseInt(params);
        } catch (Exception e) {
        }
        this.numOptimal = numOptimal < 1 ? 1 : numOptimal;
    }

    public String getParameters() {
        return numOptimal + "";
    }
    
       /**
     * getTitle of the statistic
     *
     * @return getTitle
     */
    public String getTitle() {
        return title + " " + numOptimal;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
