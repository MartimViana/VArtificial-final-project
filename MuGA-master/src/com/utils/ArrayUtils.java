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

import com.evolutionary.problem.Individual;
import java.util.Random;

/**
 * Created on 5/out/2015, 18:28:34
 *
 * @author zulu - computer
 */
public class ArrayUtils {

    // Implementing Fisher Yates shuffle
    public static void shuffleArray(Individual[] array) {
        Random rnd = array[0].solver.random;
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Individual a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510051828L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
