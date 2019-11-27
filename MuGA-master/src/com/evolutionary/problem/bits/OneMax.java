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
public class OneMax extends BinaryString {

    private static int DEFAULT_SIZE = 128;

    public OneMax() {
        super(DEFAULT_SIZE, Optimization.MAXIMIZE);
    }

    public OneMax(int size) {
        super(size, Optimization.MAXIMIZE);
    }

    public OneMax(String genes) {
        super(genes.length(), Optimization.MAXIMIZE);
        setBits(genes);
    }

    @Override
    public boolean isOptimum() {
        return ones(getBitsGenome()) == getNumGenes();
    }

    /**
     * used to test operators
     * @param fitness 
     */
    public void setGenomeFitness(int fitness) {
        this.value = fitness;
        boolean[] genome = getBitsGenome();
        for (int i = 0; i < fitness; i++) {
            genome[i] = true;
        }
        for (int i = fitness; i < genome.length; i++) {
            genome[i] = false;
        }
    }

    @Override
    protected double evaluate(boolean[] genome) {
        return (double) ones(genome);
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
        txt.append("\n");
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
