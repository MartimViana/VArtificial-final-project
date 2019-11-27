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

package com.utils.staticallib.Distributions;

import java.util.Random;

/**
 * Wrapper of functions for the Exponential distribution.
 */
public class Laplace {
    static Random rnd = new Random();
    public static double random( double m, double b) {
        double U = rnd.nextDouble() - 0.5;
        if (U < 0) {
            return m + b*Math.log(1 + 2 * U);
        }
        return m - b*Math.log(1 - 2 * U);

    }
}
