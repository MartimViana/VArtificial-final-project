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


import com.utils.problems.CEC2008.F5_Shifted_Griewank;

/**
 * Created on 25/jun/2017, 19:41:08
 *
 * @author zulu - computer
 */
public class F05_Griewank extends CEC2008 {

    public F05_Griewank() {
        super(-F5_Shifted_Griewank.LIMIT, F5_Shifted_Griewank.LIMIT);
    }

    @Override
    protected double evaluate(double[] var) {
        return F5_Shifted_Griewank.function(var);
    }
     public double[] getOptimum() {
         return F5_Shifted_Griewank.griewank;
     }

 
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201706251941L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::

}
