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
import com.evolutionary.population.Population;

/**
 * Created on 3/out/2015, 16:30:30
 *
 * @author zulu - computer
 */
public class NoRescaling extends Rescaling {
   
    @Override
    public Population execute(Population pop) {
        return pop;
    }
     @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder(Genetic.getFullName(this) + "\n");
        buf.append("\nNo Rescaling");
        buf.append("\nNot control the number of copies");

        return buf.toString();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031630L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
