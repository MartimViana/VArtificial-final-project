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
import com.evolutionary.problem.bits.knapsack.AbstractKnapSack;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class GraphicsKnapSack extends Graphics01Genome {

    public String toString() {
        return "KnapSack";
    }

    public boolean isValid(Individual ind) {
        return ind instanceof AbstractKnapSack;
    }

    @Override
    protected void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey) {
        int genomeX = (int) (sizex * 0.8);
        displayIndividual(gr, ind.toStringGenome(), ind.isOptimum(), px, py, genomeX, sizey);
        int sackX = sizex - genomeX - 10; // gap 
        displaySack(gr, (AbstractKnapSack) ind, px + genomeX, py, sackX, sizey);
    }

    protected void displaySack(Graphics gr, AbstractKnapSack ind, int px, int py, int sizex, int sizey) {
        
        
        double max = ind.getTotalWeight();
        double sack = ind.getSackCapacity();
        
        int pixelSack = (int)(sizex*sack / max);
        gr.draw3DRect(px, py, pixelSack, sizey, true);
        gr.draw3DRect(px+pixelSack, py, sizex-pixelSack, sizey, true);
        
        if (ind.isValid()) {
            gr.setColor(new Color(0f,0f,1f,0.75f));
        }else{
            gr.setColor(new Color(1f,0f,0f,0.75f));
        }
        gr.fillRect(px, py+2, (int) (sizex*ind.getSackWeight() / max), sizey-3);
        

    }

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
