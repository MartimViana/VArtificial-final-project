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

package GUI.statistics;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Created on 6/mar/2017, 14:02:55 
 * @author zulu - computer
 */
public class StrokeChart {
    
    
    public static Stroke getStroke(int index){
        return new BasicStroke(4.0f,                      // Width
                           BasicStroke.CAP_SQUARE,    // End cap
                           BasicStroke.JOIN_MITER,    // Join style
                           10.0f,                     // Miter limit
                           new float[] {16.0f*index,20.0f}, // Dash pattern
                           0.0f);                     // Dash phase
    }


    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201703061402L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}