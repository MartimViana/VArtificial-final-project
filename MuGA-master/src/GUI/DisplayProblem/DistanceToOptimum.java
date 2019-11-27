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
//::                                                               (c)2016   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package GUI.DisplayProblem;

import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class DistanceToOptimum extends DisplayPopulation {

    public String toString() {
        return "Distance to Optimum";
    }

    public boolean isValid(Individual ind) {
        return getOptimum(ind) != null;
    }

    public double[] getOptimum(Individual ind) {
       
        if (ind instanceof RealVector) {
            return ((RealVector) ind).getOptimum();
        }
        return null;
    }

    public double[] getValues(Individual ind) {
        
        if (ind instanceof RealVector) {
            return ((RealVector) ind).getGenome();
        }
        return null;
    }

    @Override
    protected void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey) {
        double[] value = getValues(ind);
        double[] best = getOptimum(ind);
        double dx = (double) sizex / value.length;
        for (int i = 0; i < value.length; i++) {
            gr.setColor(getLogColor(best[i % best.length] - value[i]));
            gr.fill3DRect(px + (int) (i * dx), py, px + (int) (dx + 1), sizey, true);
        }
    }

    public static Color getLogColor(double value) {
        if (value > 0) {
            int l = (int) (128 - Math.log(value) * 10);
            if (l > 255) {
                l = 255;
            } else if (l < 0) {
                l = 0;
            }
            return new Color(255, l, l);
        } else {
            int l = (int) (128 - Math.log(-value) * 10);
            if (l > 255) {
                l = 255;
            } else if (l < 0) {
                l = 0;
            }
            return new Color(l, l, 255);
        }
    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
