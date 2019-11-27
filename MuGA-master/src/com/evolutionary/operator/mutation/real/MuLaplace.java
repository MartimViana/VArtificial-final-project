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
package com.evolutionary.operator.mutation.real;

import com.utils.RandomVariable;

/**
 * Created on 2/out/2015, 7:50:41
 *
 * @author zulu - computer
 */
public class MuLaplace extends MuGauss {

    private static double mean = 0.0;
    private static double scale = 1.0;

    public double getDistribution() {
        //http://en.wikipedia.org/wiki/Laplace_distribution
        double u = random.nextDouble() - 0.5;       //Uniform -0.5 .. 0.5
        if (u <= 0) {
            return mean + scale * Math.log((1.0 + 2 * u));
        }
        return mean - scale * Math.log((1.0 - 2 * u));
    }

    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder();
        buf.append(toString());
        buf.append("\nMutation Using Gaussian( 0, 1) distribution");
        buf.append("\nFactor of mutation <" + FACTOR + ">");
        buf.append("\nParameters <Probability><DIM>");
        buf.append("\n<Probability> to mutate one gene");
        buf.append("\n<DIM> dimension of mutation [0,1]");
        return buf.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 2019050181402L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
