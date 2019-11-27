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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class DisplayNK_Rules extends Graphics01Genome {

    @Override
    public String toString() {
        return "NK Rules";
    }

    @Override
    public boolean isValid(Individual ind) {
        return ind instanceof NKLandscapeAbstract;
    }

    @Override
    public void display(Population pop, Graphics gr, Rectangle bounds) {
        List<Individual> individuals = new ArrayList<>(pop.getGenomes());
        Collections.sort(individuals);
        Collections.reverse(individuals);
        // get matrix  
        List< Pair< int[], double[]>> data = ((NKLandscapeAbstract) pop.getTemplate()).getNK_Matrix();
        //----------------------------------------------------------------
        //clean area
        gr.setColor(Color.LIGHT_GRAY);
        gr.fill3DRect(bounds.x, bounds.y, bounds.width, bounds.height, true);
        //----------------------------------------------------------------
        //number of rules
        int dimX = bounds.width / (data.size());
        //number of individuals
        int dimY = bounds.height / pop.getPopulationSize();
        //calculate optimum of each rule
        double maxValues[] = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            double[] v = data.get(i).getRight();
            maxValues[i] = v[0];
            //calculate optimum value  in the rule
            for (int j = 1; j < v.length; j++) {
                if (v[j] > maxValues[i]) {
                    maxValues[i] = v[j];
                }
            }
        }
        for (int k = 0; k < individuals.size(); k++) {
            boolean[] genome = (boolean[]) individuals.get(k).getGenome();
            boolean[] rule = new boolean[data.size()];
            for (int i = 0; i < data.size(); i++) {
                int[] bitsInTheGenome = data.get(i).getLeft();
                //calculate the value of the bits int the bitsInTheGenome positions
                int indexOfRuleValue = 0;
                for (int j = 0; j < bitsInTheGenome.length; ++j) {
                    indexOfRuleValue <<= 1;
                    indexOfRuleValue |= genome[bitsInTheGenome[j]] ? 1 : 0;
                }
                rule[i] = data.get(i).getRight()[indexOfRuleValue] == maxValues[i];
            }
            displayIndividual(gr, individuals.get(k).isOptimum(), rule, 0, dimY * k, bounds.width, dimY);
        }

    }

    protected void displayIndividual(Graphics gr, boolean isOptimum, boolean[] genome, int px, int py, int sizex, int sizey) {
        double dx = sizex / (double) (genome.length);
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
            if (isOptimum) {
                gr.setColor(getBestColor(true));
            } else if (genome[i]) {
                gr.setColor(getBestColor(false));
            } else { // draw normal allel
                gr.setColor(getDeceptive(false));
            }
            gr.fillRoundRect((int) (posx + dx * i), py, (int) (dx - gap), sizey, sizey, sizey);
            if (dx > 3) {
                gr.setColor(Color.DARK_GRAY);
                gr.drawRoundRect((int) (posx + dx * i), py, (int) (dx - gap), sizey, sizey, sizey);
            }
        }
        posx += dx;
    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
