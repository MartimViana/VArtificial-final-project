//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2019   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.utils;

import java.util.Arrays;

/**
 * Created on 23/fev/2019, 6:26:18
 *
 * @author zulu - computer
 */
public class Shannon {

    /**
     * calculates the entropy of the values in the interval based on the shannon 
     * metric
     * @param values array of values
     * @param min minimum of the interval
     * @param max maximum of the interval
     * @return  entropy
     */
    public static double getIntervalEntropy(double[] values, double min, double max) {
        double sum = 0;
        double DIM = max - min;
        Arrays.sort(values);
        for (int i = 1; i < values.length; i++) {
            double p = (values[i] - values[i - 1]) / DIM;
            //prevent the logarithm of zero
            if (p != 0.0) {
                sum += -p * log(p, values.length - 1);
            }
        }
        return sum;
    }

    public static double getBinaryEntropy(double p) {
    //return getIntervalEntropy(new double[]{0, p, 1}, 0, 1);
        if (p == 0) {
            return 0;
        }
        return -(p * log(p, 2) + (1 - p) * log(1 - p, 2));
    }

    public static double log(double num, double base) {
        return Math.log10(num) / Math.log10(base);
    }

    public static void main(String[] args) {
//        double[] v = {0, 0.5, 1};
//
//        double e = getIntervalEntropy(v, 0, 1);
//        System.out.println(Arrays.toString(v) + "e = " + e);
//
//        for (double p = 0; p <= 1.0; p += 0.05) {
//            System.out.println(p + " manso = " + (p * (1 - p) * 4) + " \t e = " + getBinaryEntropy(p));
//
//        }

        for (double p = 0; p <= 1.0; p += 0.005) {
            double[] prob = {0, p, 1};

            double en = getIntervalEntropy(prob, 0, 1);
            System.out.printf("\n  probability %.4f = entropy = %4f ", p,en );

        }

//        double[] v1 = {1, 1, 2, 3, 4, 5, 6, 7, 8, 9,9};
//        System.out.println("Entr " + getEntropy(v1));
//
//
//        double[] v2 = {1, 5,5,5,5,5,5,5,5,5,5,9};
//        System.out.println("Entr " + getEntropy(v2));
//        
//        double[] v3 = {1,4, 5,5,5,5,5,5,5,5,5,9};
//        System.out.println("Entr " + getEntropy(v3));
//        
//        double[] v4 = {1,2, 3,3,3,3,3,3,3,3,3,9};
//        System.out.println("Entr " + getEntropy(v4));
//        
//        double[] v5 = {1,5, 5.1,5.2,5.3,5.4,5.6,5.8,5.9,6,9};
//        System.out.println("Entr " + getEntropy(v5));
//        
//         double[] v6 = {1,5, 6,6,6,6,6,6,6,6,9};
//        System.out.println("Entr " + getEntropy(v6));
//
//
//        double[] v2 = {-5, -5, -5, 0, 0, 0, 0, 5, 5, 5, 5};
//        System.out.println("Entr " + getEntropy(v2, 10));
//
//        Population p = new SimplePopulation();
//        SimpleRC r1 = new SimpleRC(3);
//        double v1[] = {1, 2, 3};
//        r1.setValues(v1);
//        p.addIndividual(r1);
//        SimpleRC r2 = new SimpleRC(3);
//        double v2[] = {2, 3, 4};
//        r2.setValues(v2);
//        p.addIndividual(r2);
//
//        SimpleRC r3 = new SimpleRC(3);
//        double v3[] = {5, 6, 7};
//        r3.setValues(v3);
//        p.addIndividual(r3);
//
//        System.out.println("Sh" + Shannon.calculateEntropy(p));
//        System.out.println("Pop\n" + p);
//        System.out.println("Entrophy" + calculateEntropy(p));
//        F1_Shifted_Sphere f = new F1_Shifted_Sphere();
//        f.setParameters("10");
//        f = new F1_Shifted_Sphere();
//        p.createRandomPopulation(40, f);
//        System.out.println("Pop\n" + p);
//        System.out.println("Entrophy" + calculateEntropy(p));
//          int g[][] = {
//              {0,0,0,0},
//              {1,1,1,1},
//              {2,2,2,2},
//              {3,3,3,3},              
//          };
//        
//         int g[][] = {
//              {0,1,2,3,4},
//              {1,0,2,3,4},
//              {2,1,0,3,4},
//              {3,2,1,0,4},
//              {4,2,1,0,4},
//          };
//          SimplePopulation p = new SimplePopulation();
//          for (int i = 0; i < g.length; i++) {
//            p.addGenotype( new SimplePermutation(g[i]) );
//        }
//          System.out.println(p);
//          System.out.println("Entrophy" + calculateEntropy(p));
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201902230626L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
