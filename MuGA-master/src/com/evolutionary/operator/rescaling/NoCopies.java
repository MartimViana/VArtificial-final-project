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

package com.evolutionary.operator.rescaling;

import com.evolutionary.Genetic;
import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;

/**
 * Created on 3/out/2015, 16:30:30
 *
 * @author zulu - computer
 */
public class NoCopies extends Rescaling {
   
    @Override
    public Population execute(Population pop) {
        //verify type of population
        if (pop instanceof MultiPopulation) {
            MultiPopulation mpop = (MultiPopulation) pop;
            for (Individual ind : pop.getGenomes()) {
                mpop.setCopies(ind, 1);
            }
        }
        return pop;
    }
     @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder(Genetic.getFullName(this) + "\n");
        buf.append("\nNo Copies");
        buf.append("\nAll individuals hava one copie");

        return buf.toString();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031630L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
