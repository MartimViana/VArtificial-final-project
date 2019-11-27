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

package com.utils;

import java.util.Locale;
import org.apache.commons.math3.stat.inference.TTest;

/**
 * Created on 23/mar/2016, 15:22:48
 *
 * @author zulu - computer
 */
public class MyStatistics {

    public static double mean(double[] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum / a.length;
    }

    //--------------------------------------------------------------------------
    //calculo da variancia 
    private static double variance(double[] v) {
        double x = 0;
        double x2 = 0.0;
        int n = v.length;
        for (int i = 0; i < n; i++) {
            x2 += v[i] * v[i];
            x += v[i];
        }
        return (x2 - (x * x) / n) / (n - 1);
    }

    public static double std(double[] a) {
        return Math.sqrt(variance(a));
    }

    public static double min(double[] a) {
        double m = Double.POSITIVE_INFINITY;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < m) {
                m = a[i];
            }
        }
        return m;
    }

    public static double max(double[] a) {
        double m = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > m) {
                m = a[i];
            }
        }
        return m;
    }

    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    /**
     * calcule the p-values of pairs of samples
     *
     * @param v array os samples
     * @return p-values
     */
    public static double[][] p_values(double[][] v) {
        //T-Test
        TTest tTest = new TTest();
        //number of tests
        int size = v.length;
        //matrtix of comparisons
        double[][] res = new double[size][size];
        //for all tests
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //calculate p-value
                res[i][j] = tTest.tTest(v[i], v[j]);
            }
        }
        return res;
    }

    /**
     * compares the mean sample pairs
     *
     * @param v array os samples
     * @param significance significance of the test ( 0.05 = 95% , 0.01 = 99%)
     * @return true if means are Equals and false otherwise
     */
    public static boolean[][] meansAreEquals(double[][] v, double significance) {
        TTest tTest = new TTest();
        //number of tests
        int size = v.length;
        //p_values
        boolean[][] res = new boolean[size][size];
        //for all tests
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //calculate test i-j
                res[i][j] = !tTest.tTest(v[i], v[j], significance);
            }
        }
        return res;
    }

    /**
     * calcule the p-values of pairs of samples
     *
     * @param v array os samples
     * @param significance significance of the test ( 0.05 = 95 % , 0.01 = 99%)
     * @param higherIsBetter the high mean values are better ?
     * @return - (worst) .(equal) + (better)
     */
    public static char[][] compareMeansTtest(double[][] v, double significance, boolean higherIsBetter) {
        TTest tTest = new TTest();
        //number of tests
        int size = v.length;
        //p_values
        char[][] res = new char[size][size];
        //for all tests
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //calculate test i-j
                if (!tTest.tTest(v[i], v[j], significance)) {
                    res[i][j] = '.';
                } else {
                    double m1 = mean(v[i]);
                    double m2 = mean(v[j]);
                    if (higherIsBetter) {
                        res[i][j] = m1 > m2 ? '+' : '-';
                    } else {
                        res[i][j] = m1 < m2 ? '+' : '-';
                    }
                }
            }
        }
        return res;
    }

    /**
     * calcule the rank of pairs of samples
     *
     * @param v array os samples
     * @param significance significance of the test ( 0.05 = 95 % , 0.01 = 99%)
     * @param higherIsBetter the high mean values are better ?
     * @return array of ranks (better = 1 , worst = n)
     */
    public static int[] sumMeanComparations(char[][] test) {
        int[] r = new int[test.length];

        //--------------------------------------------
        //calculate sums of + and -
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test.length; j++) {
                if (test[i][j] == '.') {
                    continue;
                }
                r[i] += test[i][j] == '+' ? 1 : -1;
            }
        }
        return r;
    }

    /**
     * calcule the rank of pairs of samples
     *
     * @param v array os samples
     * @param significance significance of the test ( 0.05 = 95 % , 0.01 = 99%)
     * @param higherIsBetter the high mean values are better ?
     * @return array of ranks (better = 1 , worst = n)
     */
    public static int[] rank(double[][] v, double significance, boolean higherIsBetter) {
        int[] r = new int[v.length];
        char[][] test = compareMeansTtest(v, significance, higherIsBetter);
        //--------------------------------------------
        //calculate sums of + and -
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test.length; j++) {
                if (test[i][j] == '.') {
                    continue;
                }
                r[i] += test[i][j] == '+' ? 1 : -1;
            }
        }
        //--------------------------------------------
        // normalize ranks 
        return Ranking.rank(r);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603231522L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        //https://martinsdeveloperworld.wordpress.com/2015/07/30/evaluating-performance-measurements-with-apaches-commons-math3/
        double[] x = {9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4};
        double[] sample1 = {154.3, 191.0, 163.4, 168.6, 187.0, 200.4, 162.5};
        double[] sample2 = {230.4, 202.8, 202.8, 216.8, 192.9, 194.4, 211.7};
        double[] sample3 = {154.3, 191.0, 163.4, 168.6, 187.0, 200.4, 162.5};
        double[] sample4 = {155.3, 163.7, 200.1, 177.5, 188.3, 198.7, 201.7};

        double[][] test = {
            sample1,
            sample2,
            sample3,
            sample4
        };
        String label[] = {"s1", "s2", "s3", "s4"};
        System.out.println("\nP_Values");
        double[][] p = p_values(test);
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                System.out.printf(Locale.US, " %4.4f", p[i][j]);
            }
            System.out.println();
        }
        System.out.println("\nT_Test 95%");
        boolean[][] d = meansAreEquals(test, 0.05);
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                System.out.print(" " + (d[i][j] ? "1" : "0"));
            }
            System.out.println();
        }

        System.out.println("\nResultt 95%");
        char[][] s = compareMeansTtest(test, 0.05, true);
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                System.out.print(" " + s[i][j]);
            }
            System.out.println();
        }

        System.out.println("\nRank 95%");
        int[] r = Ranking.rank(sumMeanComparations(s));
        for (int i = 0; i < p.length; i++) {
            System.out.print(" " + r[i]);
        }
        System.out.println("\nRank Labels 95%");
        Ranking.sortByRank(label, r);
        for (int i = 0; i < label.length; i++) {
            System.out.println(r[i] + " = " + label[i]);

        }

    }
}
