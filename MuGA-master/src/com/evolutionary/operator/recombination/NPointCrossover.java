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
package com.evolutionary.operator.recombination;

import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.Individual;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created on 2/out/2015, 8:38:28
 *
 * @author zulu - computer
 */
public class NPointCrossover extends Recombination {

    protected int numberOfCuts = 1;// Number of cuts

    @Override
    public Individual[] recombine(Individual... parents) {
        if (random.nextDouble() < pCrossover) {
            doCrossover((BinaryString) parents[0], (BinaryString) parents[1], numberOfCuts, random);
        }
        return parents;
    }

    public static void doCrossover(BinaryString i1, BinaryString i2, int cuts, Random random) {
        boolean[] g1 = i1.getBitsGenome();
        boolean[] g2 = i2.getBitsGenome();
        //create mask with cutpoints
        boolean[] mask = createMask(i1.getNumGenes(), cuts, random);
        for (int i = 0; i < mask.length; i++) {
            if ((g1[i] != g2[i]) && mask[i]) { //different bits and mask=1
                g1[i] = !g1[i]; //inverse bit
                g2[i] = !g2[i]; //inversebit
                i1.setNotEvaluated();
                i2.setNotEvaluated();
            }
        }

    }

    public String getInformation() {
        StringBuilder txt = new StringBuilder(super.getNameWithParameters() + "\n");
        txt.append("\n" + getClass().getSimpleName() + "( P_Crossover, N_Cuts )\n");
        txt.append("\nRecombine parents whith multiple crossover points");
        txt.append("\n\nParameters:");
        txt.append("\n    <P_Crossover> : probability to make recombinations with parents");
        txt.append("\n    <N_Cuts> : number of cuts in genome");
        return txt.toString();
    }

    @Override
    public String getParameters() {
        return pCrossover + " " + numberOfCuts;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * update parameters od the operator
     *
     * @param params parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    @Override
    public void setParameters(String params) throws RuntimeException { //update parameters by string values
        //split string by withe chars
        String[] aParams = params.split("\\s+");
        try {
            pCrossover = Double.parseDouble(aParams[0]);
        } catch (Exception e) {
        }
        try {
            numberOfCuts = Integer.parseInt(aParams[1]);
        } catch (Exception e) {
        }
    }//----------------------------------------------------------------------------------------------------------

    /**
     * creates a mask to exchange genes
     *
     * @param size
     * @param cuts
     * @return
     */
    public static boolean[] createMask(int size, int cuts, Random random) {
        TreeSet<Integer> crossPoints = new TreeSet<>(); // cut point positions
        int tryCut = size * 2; //number of tryes
        do {
            crossPoints.add(random.nextInt(size - 1) + 1);
            tryCut--;
        } while (crossPoints.size() < cuts && tryCut > 0);// Choose exactly nCrossover cross points.
        if (crossPoints.size() % 2 != 0) // If the number of cross points is odd then Problem.n is the "last" cross point.
        {
            crossPoints.add(size);
        }
        boolean[] mask = new boolean[size];
        while (!crossPoints.isEmpty()) {// Perform the swapping for all positions between K_odd - K_even.
            int k1 = crossPoints.pollFirst();
            int k2 = crossPoints.pollFirst();
            for (int k = k1; k < k2; k++) {
                mask[k] = true;
            }
        }
        return mask;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510020838L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
