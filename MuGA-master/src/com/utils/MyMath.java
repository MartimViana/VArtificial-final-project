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

import java.util.Date;

/**
 *
 * @author zulu
 */
public class MyMath {

    public static int log2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    public static int log(int x, int base) {
        return (int) (Math.log(x) / Math.log(base));
    }

    public static long pow2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        return (long) Math.pow(2, n);
    }

    /**
     * Get a diff between two dates
     *
     * @param date1 the oldest date
     * @param date2 the newest date
     * @return the diff value
     */
    public static Date getDateDiff(Date date1, Date date2) {
        return new Date(date2.getTime() - date1.getTime());
    }

}
