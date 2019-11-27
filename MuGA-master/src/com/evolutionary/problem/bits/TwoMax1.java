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
public class TwoMax1 extends OneMax {

    @Override
    public boolean isOptimum() {        
        return fitness() == this.getNumGenes()+1;
    }

    @Override
    protected double evaluate(boolean[] genome) {
        int nOnes = ones(genome);
        int gift = nOnes == genome.length ? 1:0 ;;
        return Math.max(nOnes, genome.length - nOnes) + gift;
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
        txt.append("\n the optimum is located in the ones");
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
