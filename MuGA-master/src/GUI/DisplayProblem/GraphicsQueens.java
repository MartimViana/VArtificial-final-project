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
import com.evolutionary.problem.permutation.ChessQueens;
import com.evolutionary.problem.permutation.Permutations;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class GraphicsQueens extends Graphics01Genome {

    public String toString() {
        return "Queens";
    }

    public boolean isValid(Individual ind) {
        return ind instanceof Permutations;
    }

    @Override
    protected void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey) {
        displayIndividual(gr, (ChessQueens) ind, true, px, py, sizex, sizey);
    }

    protected void displayIndividual(Graphics gr, ChessQueens queen, boolean isOptimum, int px, int py, int sizex, int sizey) {
        int[] genome = queen.getIndexesGenome();

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
        double centerGap = dx/2 - sizey;
        
        for (int i = 0; i < genome.length; i++) {
            if (queen.isOptimum()) {                
                drawBlock(gr,getBestColor(true), (int) posx, py, (int) dx - gap, sizey);
            } else if (queen.getAtaks(i, genome) == 0) {
                drawBlock(gr,getBestColor(false), (int) posx, py, (int) dx - gap, sizey);
            } else {
                drawBlock(gr,getDeceptive(true), (int) posx, py, (int) dx - gap, sizey);
            }
            drawBlock(gr, Color.getHSBColor((float) genome[i] / (float) genome.length, 0.5f, 1f), (int) (posx +centerGap), py, sizey, sizey);
            posx += dx;
        }
    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
