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

import com.evolutionary.problem.Individual;
import com.evolutionary.problem.permutation.Permutations;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class GraphicsPermutation extends Graphics01Genome {

    public String toString() {
        return "Permutation";
    }

    public boolean isValid(Individual ind) {
        return ind instanceof Permutations;
    }

    @Override
    protected void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey) {
        displayIndividual(gr, (Permutations) ind, true, px, py, sizex, sizey);
    }

    protected void displayIndividual(Graphics gr, Permutations permInd, boolean isOptimum, int px, int py, int sizex, int sizey) {
        int[] genome = permInd.getIndexesGenome();

        double dx = sizex / ((double) (genome.length));
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

        for (int i = 0; i < genome.length; i++) {

            if (permInd.isOptimum()) {
                // drawBestBit(true, gr, (int) posx, py, (int) dx - gap, sizey);
                gr.setColor(getBestColor(true));
            } else {
                gr.setColor(getBestColor(false));
                // drawBlock(gr, Color.getHSBColor((float) genome[i] / (float) genome.length, 1f, 1f), (int) posx, py, (int) dx - gap, sizey);
            }

            gr.fillRect((int) posx, py, (int) dx - gap, sizey);
            drawBlock(gr, Color.getHSBColor((float) genome[i] / (float) genome.length, 1f, 1f), (int) posx, py, (int) dx - gap, sizey);
            posx += dx;
        }

    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
