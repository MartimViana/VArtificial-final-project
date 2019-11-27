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
import com.evolutionary.problem.bits.nkLandscapes.NKLandscapeAbstract;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class DisplayNK_Problem extends Graphics01Genome {

    @Override
    public String toString() {
        return "NK Problem";
    }

    @Override
    public boolean isValid(Individual ind) {
        return ind instanceof NKLandscapeAbstract;
    }

    @Override
    public void display(Population pop, Graphics gr, Rectangle bounds) {
        // get matrix  
        List< Pair< int[], double[]>> data = ((NKLandscapeAbstract) pop.getTemplate()).getNK_Matrix();
        boolean[] optimum = ((NKLandscapeAbstract) pop.getTemplate()).getOptimumGenome();
        //----------------------------------------------------------------
        //clean area
        gr.setColor(Color.LIGHT_GRAY);
        gr.fill3DRect(bounds.x, bounds.y, bounds.width, bounds.height, true);
        //----------------------------------------------------------------
        //dimensions of Line of bits
        double dimY = (double) bounds.height / (data.size() + 2); // include optimum
        int matrixCols = ((NKLandscapeAbstract) pop.getTemplate()).getNumGenes();
        bounds.y += dimY / 8;
        //string to display row
        StringBuilder txtRow = new StringBuilder(matrixCols);
        for (int i = 0; i < matrixCols; i++) {
            txtRow.append('0');
        }

        int y = 0;

        if (optimum != null) {
            for (int i = 0; i < optimum.length; i++) {
                if (optimum[i]) {
                    txtRow.setCharAt(i, '1');
                }
            }
            displayIndividual(gr, txtRow.toString(), true, bounds.x,
                    bounds.y + (int) (dimY * y++), bounds.width * 3 / 4, (int) dimY);
        }
        y++;
        for (Pair< int[], double[]> row : data) {
            //reset string
            for (int i = 0; i < matrixCols; i++) {
                txtRow.setCharAt(i, '0');
            }
            for (int col : row.getLeft()) {
                txtRow.setCharAt(col, '1');
            }

            displayIndividual(gr, txtRow.toString(), false, bounds.x,
                    bounds.y + (int) (dimY * y), bounds.width * 3 / 4, (int) dimY);

            displayRule(gr, row.getRight(), bounds.width * 3 / 4 + 10, bounds.y + (int) (dimY * y),
                    bounds.width / 4 - 20, (int) dimY);

            y++;

        }
    }

    public void displayRule(Graphics gr, double[] v, int px, int py, int sizex, int sizey) {
        double optValue = Double.NEGATIVE_INFINITY;
        //calculate optimum value  in the rule
        for (int i = 0; i < v.length; i++) {
            if (v[i] > optValue) {
                optValue = v[i];
            }
        }
        int dx = sizex / v.length;
        //draw data 
        for (int i = 0; i < v.length; i++) {

            if (v[i] == optValue) {
                gr.setColor(Color.BLACK);
            } else {
                gr.setColor(new Color(1.0f, 1.0f, 1.0f, 1 - (float) v[i]));
            }
            gr.fillRect(px + dx * i, py, dx, sizey);
        }
    }

    @Override
    protected void drawInformation(Graphics gr, int px, int py) {
        //hide information about evolution
    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
