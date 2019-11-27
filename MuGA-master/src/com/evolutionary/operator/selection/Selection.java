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

package com.evolutionary.operator.selection;

import com.evolutionary.operator.GeneticOperator;
import com.evolutionary.population.Population;

/**
 * Created on 1/out/2015, 11:47:02
 *
 * @author zulu - computer
 */
public abstract class Selection extends GeneticOperator {

    /**
     * Selects a set of inviduals to reproduction
     *
     * @param parents parents population
     * @return parents selected to reproduction
     */
    public abstract Population execute(Population parents);
    /**
     * proportion of the parents that will be selected 1 - the same number of
     * parents
     */
    protected static double default_parentsProportion = 1;
    protected double parentsProportion;

    public Selection() {
        this(default_parentsProportion);
    }

    public Selection(double parentsProportion) {
        this.parentsProportion = parentsProportion;        
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * update parameters od the operator
     *
     * @param params parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    public void setParameters(String params) throws RuntimeException { //update parameters by string values
        super.setParameters(params);
        try {
            //split string by withe chars
            String[] aParams = params.split("\\s+");
            if (aParams.length > 0) {
                parentsProportion = Double.parseDouble(aParams[0]);
            }
            //validate parentsProportion 
            if (parentsProportion <= 0) {
                throw new RuntimeException("invalid parentsProportion of Selection : " + parentsProportion);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }//----------------------------------------------------------------------------------------------------------

    @Override
    public String getParameters() {
        return parentsProportion + "";
    }

    @Override
    public Population execute(Population... pop) {
        return execute(pop[0]);
    }
    
    
    public double getParentProportion() {
        return parentsProportion ;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510011147L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
