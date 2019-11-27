/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.problems.CEC2008;

import com.utils.problems.CEC2008.fractal.FastFractal;

/**
 *
 * @author manso
 */
public class F7_Fast_Fractal {

    public static double MIN = -1;
    public static double MAX = 1;
    public static FastFractal ff = null;

    public static void initFractalFunction(int numGenes) {
        try {
            ff = new FastFractal("com.utils.problems.CEC2008.fractal.DoubleDip", 3, 1, 1, numGenes);
        } catch (Exception e) {
            System.out.println("ERROR :" + e.getMessage());
        }
    }

    public static double evaluate(double[] x) {
        if(ff == null ||  x.length != ff.getDimensions() ){
            initFractalFunction(x.length);
        }
        return ff.evaluate(x);
    }

}
