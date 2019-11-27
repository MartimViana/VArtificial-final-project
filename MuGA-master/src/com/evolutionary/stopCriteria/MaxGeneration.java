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
 * Created on 3/out/2015, 18:46:24
 *
 * @author zulu - computer
 */
public class MaxGeneration extends StopCriteria {

    int maxGen = 100;

    /**
     * updats parameters
     *
     * @param params maximum number of evaluations
     * @throws RuntimeException
     */
    @Override
    public void setParameters(String params) throws RuntimeException {
        try {
            maxGen = Integer.parseInt(params.trim());
            //validate max evaluations
            if (maxGen < 1) {
                throw new RuntimeException("invalid number of evaluations : " + stopValue);
            }
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
        return maxGen + "";
    }

    @Override
    public boolean isDone(EAsolver solver) {
        return solver.numGeneration >= maxGen;
    }
    
    public double completedRatio(EAsolver solver) {
        return solver.numGeneration / (double) maxGen;
    }

    public String getInformation() {
        StringBuilder buf = new StringBuilder(getSimpleName() + "\n");
        buf.append(this.getClass().getSimpleName());
        buf.append("Terminates when executed <MAX> Generations");
        buf.append("\nParameter <MAX> : max generations");
        return buf.toString();
    }

    @Override
    public Genetic getClone() {
        MaxGeneration stop = new MaxGeneration();
        stop.maxGen = maxGen;
        return stop;
    }
//----------------------------------------------------------------------------------------------------------
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031846L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
