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
import com.evolutionary.problem.bits.RoyRoad.R1;
import com.evolutionary.problem.bits.knapsack.AbstractKnapSack;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class GraphicsRoyalRoad extends Graphics01Genome {

    public String toString() {
        return "RoyalRoad";
    }

    public boolean isValid(Individual ind) {
        return ind instanceof R1;
    }

    @Override
    protected void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey) {
        displayIndividual(gr, (R1) ind, true, px, py, sizex, sizey);
    }

    protected void displayIndividual(Graphics gr, R1 rr, boolean isOptimum, int px, int py, int sizex, int sizey) {
        boolean[] genome = rr.getBitsGenome();
        int sizeOfBlocks = R1.SIZE_OF_BLOCK;
        int numberOfBlocks = genome.length / sizeOfBlocks;

        double dx = sizex / ((double) (genome.length)+ numberOfBlocks);
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

        for (int i = 0; i < numberOfBlocks; i++) {
            int block = 0;
            boolean optimum = false;
            for (int j = 0; j < sizeOfBlocks; j++) {
                if (genome[i * sizeOfBlocks + j]) {
                    block++;
                }
            }
            if (block == sizeOfBlocks) {
                optimum = true;
            }

            for (int j = 0; j < sizeOfBlocks; j++) {
                if (optimum) {
                    drawBestBit(genome[i * sizeOfBlocks + j], gr, (int) posx, py, (int) (dx - gap), sizey);;
                } else {
                    drawBit(genome[i * sizeOfBlocks + j], gr, (int) posx, py, (int) (dx - gap), sizey);;
                }
                posx += dx;
            }
            posx += dx;
        }

    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
