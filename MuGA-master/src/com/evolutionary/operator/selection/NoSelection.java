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

import com.evolutionary.Genetic;
import com.evolutionary.population.Population;

/**
 * Created on 2/out/2015, 8:05:06
 *
 * @author zulu - computer
 */
public class NoSelection extends Selection {

    public NoSelection() {
    }
    

    @Override
    public Population execute(Population parents) {
        return parents.getClone();
    }
    public String getParameters() {
        return "";
    }
     public void setParameters(String params) throws RuntimeException { //update parameters by string values
         //do nothing
     }

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(Genetic.getFullName(this));
        txt.append("\n" + getClass().getSimpleName());
        txt.append("\n\nSelects all parents");
        return txt.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510020805L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
