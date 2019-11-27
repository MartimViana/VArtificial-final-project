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

import java.util.Arrays;

/**
 * Created on 28/mar/2016, 19:58:07
 *
 * @author zulu - computer
 */
public class Ranking {

    /**
     * Sot elements by the ranking provided by array of values (descending sort)
     *
     * @param elems elements to sort
     * @param values  value of each element
     */
    public static void sortByRank(Object[] elems, int[] values) {
        //clone values to preserve order
        int []v = Arrays.copyOf(values, values.length);        
        int max = v[0];
        for (int i = 1; i < v.length; i++) {
            if (v[i] > max) {
                max = v[i];
            }
        }
        for (int i = 0; i < v.length; i++) {
            v[i] = max - v[i] + 1;
        }

        //sort ranks
        int aux;
        Object auxObj;
        for (int j = 0; j < elems.length; j++) {
            for (int i = 0; i < elems.length - 1; i++) {
                if (v[i] > v[i + 1]) {
                    //sort ranks
                    aux = v[i + 1];
                    v[i + 1] = v[i];
                    v[i] = aux;
                    //change index
                    auxObj = elems[i + 1];
                    elems[i + 1] = elems[i];
                    elems[i] = auxObj;
                }
            }
        }//sort               
    }

    public static int[] rank(int[] values) {
        //clone values to preserve order
        int []sum = Arrays.copyOf(values, values.length);        
        int[] index = new int[sum.length];
        //compute indexes
        for (int i = 0; i < sum.length; i++) {
            index[i] = i;
        }
        //sort sums
        int aux;
        for (int j = 0; j < index.length; j++) {
            for (int i = 0; i < index.length - 1; i++) {
                if (sum[i] < sum[i + 1]) {
                    //sort sums
                    aux = sum[i + 1];
                    sum[i + 1] = sum[i];
                    sum[i] = aux;
                    //change index
                    aux = index[i + 1];
                    index[i + 1] = index[i];
                    index[i] = aux;
                }
            }
        }//sort
//        System.out.println("SORT " + Arrays.toString(sum));
        int[] rank = new int[sum.length];
        int[] rnumbers = normalizeRanks(sum);
        for (int i = 0; i < rnumbers.length; i++) {
            rank[index[i]] = rnumbers[i];
        }
        return rank;
    }

    private static int[] normalizeRanks(int[] v) {
        int[] r = new int[v.length];
        r[0] = 1;
        for (int i = 1; i < r.length; i++) {
            if (v[i] == v[i - 1]) {
                r[i] = r[i - 1];
            } else {
                r[i] = i + 1;
            }
        }
        return r;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603281958L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
