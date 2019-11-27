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

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.bits.nkLandscapes.NKLandscapeAbstract;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class DisplayNK_Population extends Graphics01Genome {

    @Override
    public String toString() {
        return "NK Population";
    }

    @Override
    public boolean isValid(Individual ind) {
        return ind instanceof NKLandscapeAbstract;
    }

    @Override
    public void display(Population pop, Graphics gr, Rectangle bounds) {
        // get optimum  
        boolean[] optimum = ((NKLandscapeAbstract) pop.getTemplate()).getOptimumGenome();
        //----------------------------------------------------------------
        //clean area
        gr.setColor(Color.LIGHT_GRAY);
        gr.fill3DRect(bounds.x, bounds.y, bounds.width, bounds.height, true);
        //----------------------------------------------------------------
        //dimensions of Line of bits
        double dimY = (double) bounds.height / (pop.getPopulationSize() + 2); // include optimum
        int y = 0;
        int matrixCols = ((NKLandscapeAbstract) pop.getTemplate()).getNumGenes();
        //string to display row
        StringBuilder txtRow = new StringBuilder(matrixCols);
        for (int i = 0; i < matrixCols; i++) {
            txtRow.append('0');
        }

        if (optimum != null) {
            for (int i = 0; i < optimum.length; i++) {
                if (optimum[i]) {
                    txtRow.setCharAt(i, '1');
                }
            }
            displayIndividual(gr, txtRow.toString(), true, bounds.x,
                    bounds.y + (int) (dimY * y++), bounds.width, (int) dimY);
        }
        List<Individual> individuals = new ArrayList<>(pop.getGenomes());
        Collections.sort(individuals);
        Collections.reverse(individuals);
        y++;
        //----------------------------------------------------------------
        for (Individual ind : individuals) {
            if (optimum == null || ind.isOptimum()) { // optimum not avaiable
                displayIndividual(gr, ind, bounds.x,
                        bounds.y + (int) (dimY * y++), bounds.width, (int) dimY);
                continue;
            }
            boolean genome[] = ((BinaryString) ind).getBitsGenome();

            for (int i = 0; i < genome.length; i++) {
                if (genome[i]) {
                    if (optimum[i]) {
                        txtRow.setCharAt(i, '1'); // correct
                    } else {
                        txtRow.setCharAt(i, '#');
                    }
                } else if (optimum[i]) {
                    txtRow.setCharAt(i, '@');
                } else {
                    txtRow.setCharAt(i, '0');//correct
                }

            }
            displayNKIndividual(gr, txtRow.toString(), bounds.x,
                    bounds.y + (int) (dimY * y++), bounds.width, (int) dimY);
        }
    }

    protected void displayNKIndividual(Graphics gr, String genome, int px, int py, int sizex, int sizey) {
        double dx = sizex / (double) (genome.length());
        //gap between bits
        int gap = 2;
        if (dx < 4) {
            gap = 0;
        }
        //height of allel
        if (sizey > 4) {
            sizey = (int) (sizey * 0.8); // 80% of height

        }
        //position of the allel
        double posx = px;
        for (int i = 0; i < genome.length(); i++) {
            switch (genome.charAt(i)) {
                case '0':
                    gr.setColor(getColor(false));
                    break;
                case '@':
                    gr.setColor(getDeceptive(false));
                    break;
                case '1':
                    gr.setColor(getColor(true));
                    break;
                case '#':
                    gr.setColor(getDeceptive(true));
                    break;
            }
            gr.fillRoundRect((int) posx, py, (int) (dx - gap), sizey, sizey, sizey);
            if (dx > 3) {
                gr.setColor(Color.DARK_GRAY);
                gr.drawRoundRect((int) posx, py, (int) (dx - gap), sizey, sizey, sizey);
            }

            posx += dx;
        }
    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
