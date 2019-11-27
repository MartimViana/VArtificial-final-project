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

package com.evolutionary.operator;

import com.evolutionary.Genetic;
import com.evolutionary.population.Population;
import com.evolutionary.solver.EAsolver;
import java.io.Serializable;

/**
 * Created on 1/out/2015, 10:56:43
 *
 * @author zulu - computer
 */
public abstract class GeneticOperator extends Genetic implements Serializable {

    public EAsolver solver;

    public abstract Population execute(Population... pop); //variable number of parameters population

    public void setSolver(EAsolver s) {
        this.solver = s;
        this.random = s.random;
    }
    
    public GeneticOperator getClone(){
        return (GeneticOperator) Genetic.getClone(this);
    }
    
 
   

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510011056L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
