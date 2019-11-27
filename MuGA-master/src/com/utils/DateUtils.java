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
 * Created on 3/out/2015, 20:08:51
 *
 * @author zulu - computer
 */
public class DateUtils {

    public static String difTime(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        long dmilis = diff % 1000;
        long dsecs = (diff / 1000)%60;
        long dminutes = (diff / (60 * 1000)) %60;
        long dhours = (diff / (60 * 60 * 1000)) %24;
        long ddays = diff / (24 * 60 * 60 * 1000);
        return String.format("%d %02d:%02d:%02d.%03d", ddays,dhours,dminutes,dsecs,dmilis);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510032008L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
