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
package com.evolutionary.problem.real;

/**
 * Created on 25/jun/2017, 19:41:08
 *
 * @author zulu - computer
 */
public class Sphere extends RealVector {

    double optimum[] = {0};

    public Sphere() {
        super(DEFAULT_SIZE, -1, 1, Optimization.MINIMIZE);
        isLogScale = true;
    }
     public Sphere(double []values, double min, double max) {
         super(values.length, min, max, Optimization.MINIMIZE);
         setDoubleValues(values);
     }

    @Override
    protected double evaluate(double[] var) {
        double f = 0;
        for (int i = 0; i < var.length; i++) {
            f += var[i] * var[i];
        }
        return f;
    }

    @Override
    public boolean isOptimum() {
        return fitness() == 0.0;
    }

    public double[] getOptimum() {
        return optimum;
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201706251941L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        Sphere s = new Sphere();
        s.setParameters("10");
        s.randomize();
        s.evaluate();
        System.out.println(s);
    }

   
}
