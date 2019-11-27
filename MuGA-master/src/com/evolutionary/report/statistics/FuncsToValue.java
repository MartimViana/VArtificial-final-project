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
public class FuncsToValue extends AbstractStatistics {

    double optimalValue = 0;
    

    public FuncsToValue() {
        super("Number of avaluations to Value");
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
        //value found
        if (value != 0) {
            return value;
        }
         
        if (best.isMaximize() && best.getFitness() >= this.optimalValue) {
            //update vale
            return s.numEvaluations;            
        } else if (!best.isMaximize()&& best.getFitness() <= this.optimalValue) {
            //update value
            return s.numEvaluations;            
        }
        
        return 0; //not found
    }

    @Override
    public String getInformation() {
        return "Number of calls to the evaluation function\n to find Value " + this.optimalValue;
    }

    public void setParameters(String params) throws RuntimeException {
        try {
            this.optimalValue = Double.parseDouble(params);
        } catch (Exception e) {
        }        
    }

    public String getParameters() {
        return optimalValue + "";
    }
    
    /**
     * getTitle of the statistic
     *
     * @return getTitle
     */
    public String getTitle() {
        return title + " " + optimalValue;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191208L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
