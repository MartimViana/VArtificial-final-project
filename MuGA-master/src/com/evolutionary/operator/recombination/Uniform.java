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

import com.evolutionary.Genetic;
import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.Individual;
import java.util.Random;

/**
 * Created on 2/out/2015, 8:38:28
 *
 * @author zulu - computer
 */
public class Uniform extends Recombination {

    private double pSwap = 0.5;

    @Override
    public Individual[] recombine(Individual... parents) {
        if (random.nextDouble() < pCrossover) {
            doCrossover((BinaryString) parents[0], (BinaryString) parents[1], pSwap, random);
        }
        return parents;
    }

    protected static void doCrossover(BinaryString i1, BinaryString i2, double pSwap, Random random) {
        boolean[] g1 = i1.getBitsGenome();
        boolean[] g2 = i2.getBitsGenome();
        for (int i = 0; i < g1.length; i++) {
            if ((g1[i] != g2[i]) && random.nextDouble() < pSwap) { //different bits 
                g1[i] = !g1[i]; //inverse bit
                g2[i] = !g2[i]; //inverse bit
            }
        }
        i1.setNotEvaluated();
        i2.setNotEvaluated();

    }

    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder(Genetic.getFullName(this) + "\n");
        buf.append("\nRecombine parents whith uniform crossover ");
        buf.append("\n\n" + getSimpleName() + "( P_Crossover , P_Swap )");
        buf.append("\n\t<P_Crossover> : probability to make recombinations with parents");
        buf.append("\n\t<P_Swap> : probability to swap one bit");
        return buf.toString();
    }

    @Override
    public String getParameters() {
        return pCrossover + " " + pSwap;
    }

    public void setParameters(String params) throws RuntimeException { //update parameters by string values
        //split string by withe chars
        String[] aParams = params.split("\\s+");
        try {
            pCrossover = Double.parseDouble(aParams[0]);
        } catch (Exception e) {
        }
        try {
            pSwap = Double.parseDouble(aParams[1]);
        } catch (Exception e) {
        }
    }//----------------------------------------------------------------------------------------------------------

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510020838L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
