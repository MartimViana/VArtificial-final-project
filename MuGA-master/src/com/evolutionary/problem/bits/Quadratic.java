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
public class Quadratic extends BinaryString {

    public Quadratic() {
        super(32, Optimization.MAXIMIZE);
    }

    @Override
    public boolean isOptimum() {
        return getFitness() == getNumGenes() / 2;
    }

    @Override
    public double evaluate(boolean[] genome) {
        double fit = 0;
        for (int i = 0; i < genome.length;) { //Empty increment
            boolean a = genome[i++];
            boolean b = genome[i++];
            if (a == true && b == true) {
                fit += 1;
            }
            if (a == false && b == false) {
                fit += 0.9;
            }
        }
        return fit;
    }

    @Override
    public String getInformation() {
        // public String getInfo() {
        StringBuilder buf = new StringBuilder(super.getInformation());
        buf.append("\n\nQuadratic Problem ");
        buf.append("\n\nParameters <SIZE>");
        buf.append("\n   <SIZE>  number of bits ");

        buf.append("\n\n Sum group of two genes");
        buf.append("\n\t  00 -> 0.9");
        buf.append("\n\t  11 -> 1");
        buf.append("\n\t  10 -> 0");
        buf.append("\n\t  01 -> 0");
        return buf.toString();

    }

    @Override
    public String toStringPhenotype() {
        boolean[] genome = getBitsGenome();
        StringBuilder txt = new StringBuilder();
        for (int i = 0; i < genome.length; i += 2) {
            if (genome[i] && genome[i + 1]) {
                txt.append("*");
            } else if (!genome[i] && !genome[i + 1]) {
                txt.append("+");
            } else {
                txt.append(".");
            }
        }
        return txt.toString();
    }

    /**
     * update the size of the genome
     *
     * @param params size of the genome
     */
    @Override
    public void setParameters(String params) {//-------------------------------- set parameters
        try {
            //update size
            int size = Integer.parseInt(params);
            if (size % 2 != 0) {
                size++;
            }
            genome = new boolean[size];
            setNotEvaluated();
        } catch (Exception e) {
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290930L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
