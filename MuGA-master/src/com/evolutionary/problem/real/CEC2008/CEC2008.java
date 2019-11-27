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
//::                                                               (c)2017   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.problem.real.CEC2008;

import com.evolutionary.problem.real.RealVector;
import com.utils.MyNumber;

/**
 * Created on 5/jul/2017, 16:15:36
 *
 * @author zulu - computer
 */
public abstract class CEC2008 extends RealVector {

    static int NUM_VARS = 10;

    public CEC2008(double min, double max) {
        super(NUM_VARS, min, max, Optimization.MINIMIZE);
        isLogScale = true;
       
    }

    @Override
    public boolean isOptimum() {
        return fitness() == 0;// < 1E-10;
    }

    /**
     * update the size of the genome
     *
     * @param params size of the genome
     */
    @Override
    public void setParameters(String params) {//-------------------------------- set parameters
        try {
            super.setParameters(params);
            NUM_VARS = getNumGenes();
        } catch (Exception e) {
        }
    }
    
      /**
     * converte genome to string
     *
     * @return string genome
     */
    @Override
    public String toStringPhenotype() {
        double []opt = getOptimum();
        if( opt == null)
            return super.toStringPhenotype();
        StringBuilder txt = new StringBuilder("Dif. to optimum ");
        double []value = getGenome();
        for (int i =0 ; i < value.length ; i++) {
            txt.append( MyNumber.DoubleToString(value[i] - opt[i%opt.length], 20) + " ");
        }
        return txt.toString();
    }
        

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201707051615L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
