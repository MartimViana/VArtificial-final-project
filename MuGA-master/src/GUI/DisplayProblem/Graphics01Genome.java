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

package GUI.DisplayProblem;

import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.Individual;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class Graphics01Genome extends DisplayPopulation {

    public String toString() {
        return "Genome 01";
    }

    public boolean isValid(Individual ind) {
        return ind instanceof BinaryString;
    }

    @Override
    protected void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey) {
        displayIndividual(gr, ind.toStringGenome(), ind.isOptimum(), px, py, sizex, sizey);
    }
    
   
    protected void displayIndividual(Graphics gr, String genome, boolean isOptimum, int px, int py, int sizex, int sizey) {
        double dx = sizex / (double) (genome.length());
        //gap between bits
        int gap = 2;
        if (dx < 4) {
            gap = 0;
        }
        //height of allel
        if (sizey > 4) {
           sizey = sizey > dx ? (int) dx : (sizey * 8) / 10; // y gap =  0.8
        }
        //position of the allel
        double posx = px;
        for (int i = 0; i < genome.length(); i++) {
            if (genome.charAt(i) == '0' || genome.charAt(i) == '1') {
                //draw optimum alell
                if (isOptimum) {
                    gr.setColor(getBestColor(genome.charAt(i) == '1'));
                    
                } else { // draw normal allel
                    gr.setColor(getColor(genome.charAt(i) == '1'));
                }
                gr.fillRoundRect((int) posx, py, (int) (dx - gap), sizey, sizey, sizey);
                if (dx > 3) {
                    gr.setColor(Color.DARK_GRAY);
                    gr.drawRoundRect((int) posx, py, (int) (dx - gap), sizey, sizey, sizey);
                }

                //increase px
            }
            posx += dx;
        }
    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
