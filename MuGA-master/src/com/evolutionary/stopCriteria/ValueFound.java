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
public class ValueFound extends StopCriteria {

    public ValueFound() {
    }    
    public ValueFound(double value) {
        this.stopValue = value;
    }

    
    public double completedRatio(EAsolver solver) {
        return 0.0; //is not possible to compute
    }


    @Override
    public boolean isDone(EAsolver solver) {

        if (solver.problem.isMaximize()) {
            return solver.parents.getBest().getFitness() >= this.stopValue;
        }else{
            return solver.parents.getBest().getFitness() <= this.stopValue;
        }        
    }

    public String getInformation() {
        StringBuilder buf = new StringBuilder(getSimpleName() + "\n");
        buf.append("Terminates when the evolution reaches the value");
        buf.append("\nParameter <VALUE> : value to stop evolutions ");
        return buf.toString();
    }

    @Override
    public Genetic getClone() {
        OptimumFound stop = new OptimumFound();
        stop.stopValue = stopValue;
        return stop;
    }

    //----------------------------------------------------------------------------------------------------------
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031846L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
