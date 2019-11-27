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


import com.utils.problems.CEC2008.F01_Shifted_Sphere;

/**
 * Created on 25/jun/2017, 19:41:08
 *
 * @author zulu - computer
 */
public class F01_Sphere extends CEC2008 {

    public F01_Sphere() {
        super(-F01_Shifted_Sphere.LIMIT, F01_Shifted_Sphere.LIMIT);
         isLogScale = true;
    }

    @Override
    protected double evaluate(double[] var) {
        return F01_Shifted_Sphere.function(var);
    }

    public double[] getOptimum() {
        return F01_Shifted_Sphere.sphere;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201706251941L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::

}
