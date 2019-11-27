//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2016   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.operator.replacement;

import com.evolutionary.Genetic;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import com.evolutionary.solver.RTS;
import java.util.List;

/**
 * Created on 5/abr/2016, 10:43:38
 *
 * @author zulu - computer
 */
public class RestrictedReplacement extends Replacement {

    double windowSize = 0.25;

    public Population execute(Population parents, Population offspring) {
        List<Individual> newIndividuals = offspring.getGenomes(); // list of offspring          
        for (Individual candidate : newIndividuals) {
            RTS.replaceByRTS(parents, candidate, windowSize, random);
        }
        return parents;
    }// END

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(Genetic.getFullName(this));
        txt.append("\n" + getClass().getSimpleName() + " (window size)\n");
        txt.append("\nRestricted Tournament ");
        txt.append("\nReplace similar parents with offprings");
        txt.append("\n\nForeach child in Offspring population");
        txt.append("\n    calculate the most similar parent in a window");
        txt.append("\n    replace parent to child if this is better ");
        txt.append("\n\n    similarity widows = min( individual.lenght, parentsPop.size");
        txt.append("\n\n   Martin Pelikan,Hierarchical Bayesian Optimization Algorithm,  p. 123");
        return txt.toString();
    }

    //----------------------------------------------------------------------------------------------------------
    @Override
    public String getParameters() {
        return windowSize + "";
    }

    /**
     * update parameters of the operator
     *
     * @param params parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    @Override
    public void setParameters(String params) throws RuntimeException {
        try {
            windowSize = Double.parseDouble(params);
        } catch (Exception e) {
        }
    }//----------------------------------------------------------------------------------------------------------
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201604050955L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////


}
