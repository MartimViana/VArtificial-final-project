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
//::                                                               (c)2015   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.operator.recombination.real;

import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;

/**
 * Created on 2/out/2018, 8:38:28
 *
 * @author zulu - computer
 */
public class MPX1 extends PX {

   
    protected double getAlpha(Individual i1, Individual i2) {
        int fact = (i1.getNumberOfCopies() + i2.getNumberOfCopies()) / 2;
        double beta = random.nextDouble() * random.nextDouble();
        return (1 + EXPAND * 2 * fact) * beta - EXPAND * fact;
    }


    public Individual[] executePX(RealVector x1, RealVector x2) {
        if (x1.compareTo(x2) > 0) {
            RealVector aux = x1;
            x1 = x2;
            x2 = aux;
        }
        //get genome values      
        final double[] v0 = x1.getGenome();
        final double[] v1 = x2.getGenome();
        double alpha;
        //calculate new genes
        for (int i = 0; i < v0.length; i++) {
            //get a break point in the line of expansion
            alpha = getAlpha(x1, x2);
            //Crossover individuals           
            double aux = alpha * v0[i] + (1.0 - alpha) * v1[i];
            //Crossover individuals           
           // alpha = uniform(-EXPAND*x1.getNumberOfCopies(), 1 + EXPAND*x2.getNumberOfCopies());
            v1[i] = (1.0 - alpha) * v0[i] + alpha * v1[i];
            v0[i] = aux;
        }
        //set gene values - Nomarlize values outside of boundaries
        x1.setDoubleValues(reflect(x1, v0));
        x2.setDoubleValues(reflect(x2, v1));
        //return descendants 
        return new RealVector[]{x1, x2};
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201905151223L;

    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
}
