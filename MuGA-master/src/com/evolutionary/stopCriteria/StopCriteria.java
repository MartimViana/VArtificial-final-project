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

import com.evolutionary.Genetic;
import com.evolutionary.solver.EAsolver;

/**
 * Created on 3/out/2015, 18:43:46
 *
 * @author zulu - computer
 */
public abstract class StopCriteria extends Genetic{

    //value of stop critera
    protected double stopValue = (int) 1E5;

    public abstract boolean isDone(EAsolver solver);
    
    public abstract double completedRatio(EAsolver solver);
    
    
    
      /**
     * updats parameters
     *
     * @param params maximum number of evaluations
     * @throws RuntimeException
     */
    @Override
    public void setParameters(String params) throws RuntimeException {
        try {
            stopValue = Double.valueOf(params.trim());          
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * get parameters
     *
     * @return maximum number of evaluations
     */
    public String getParameters() {
        return stopValue + "";
    }
    
    public double getValue(){
        return stopValue;
    }
 
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031843L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
