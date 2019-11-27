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

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created on 4/out/2015, 22:26:44
 *
 * @author zulu - computer
 */
public class MyUtils {

    public static long getSeed(Random random) {
        long theSeed;
        try {
            Field field = Random.class.getDeclaredField("seed");
            field.setAccessible(true);
            AtomicLong scrambledSeed = (AtomicLong) field.get(random);   //this needs to be XOR'd with 0x5DEECE66DL
            theSeed = scrambledSeed.get();
            //https://docs.oracle.com/javase/7/docs/api/java/util/Random.html
            return theSeed ^ 0x5DEECE66DL;
        } catch (Exception e) {
            //handle exception
        }
        return 0;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510042226L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args) {
        Random rnd = new Random(1234);
        System.out.println("seed " + getSeed(rnd));
    }
}
