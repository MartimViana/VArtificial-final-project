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
package com.evolutionary.problem.bits;

/**
 * Created on 29/set/2015, 9:30:30
 *
 * @author zulu - computer
 */
public class TwoMax extends OneMax {

    @Override
    public boolean isOptimum() {
        int numOnes = ones(getBitsGenome());
        return numOnes * (getNumGenes() - numOnes) == 0;
    }

    @Override
    protected double evaluate(boolean[] genome) {
        double nOnes = ones(genome);
        return Math.max(nOnes, genome.length - nOnes);
    }

    public static int ones(boolean[] genome) {
        int ones = 0;
        for (boolean gene : genome) {
            if (gene == true) {
                ones++;
            }
        }
        return ones;
    }

    @Override
    public String getInformation() {
        // public String getInfo() {
        StringBuilder txt = new StringBuilder(super.getInformation());
        txt.append("\n count the maximum of ones or zeros");
        txt.append("\nParameters <SIZE>");
        txt.append("\n           <SIZE>  size of genome");
        txt.append("\n\nCount the number of ones in the genome");
        return txt.toString();

    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290930L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
