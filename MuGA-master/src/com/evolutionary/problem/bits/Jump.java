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

import com.evolutionary.problem.Individual;

import com.utils.MyString;

/**
 * Created on 29/set/2015, 9:30:30
 *
 * @author zulu - computer
 */
public class Jump extends BinaryString {

    public static int N = 128;
    public static int M = 3;

    protected int m;

    public Jump() {
        super(N, Optimization.MAXIMIZE);
        this.m = M;
    }

    public Jump(Jump other) {
        super(other);
        this.m = other.m;
    }

    public Jump getClone() {//-------------------------------------------- get clone
        return new Jump(this);
    }

    @Override
    public boolean isOptimum() {
        return value == getNumGenes();
    }

    @Override
    protected double evaluate(boolean[] genome) {
        double ones = OneMax.ones(genome);
        if (ones == genome.length || ones < genome.length - M) {
            return ones;
        }
        return genome.length - ones;
    }

    @Override
    public String getInformation() {
        // public String getInfo() {
        StringBuilder txt = new StringBuilder(super.getInformation());
        txt.append("\nCount the Ones bits  with Jump ");

        txt.append("\nParameters <N><M>");
        txt.append("\n           <N>  number of Bits");
        txt.append("\n           <M>  Size of jump");
        txt.append("\nThe Analysis of Evolutionary Algorithms");
        txt.append("\n—A Proof That Crossover Really Can Help");
        txt.append("\nT. Jansen2 and I. Wegener2");
        return txt.toString();

    }

    /**
     * update the size of the genome
     *
     * @param params size of the genome
     */
    @Override
    public void setParameters(String params) {
        try {
            String[] p = MyString.splitByWhite(params);
            try {
                N = Integer.parseInt(p[0]);
                //normalize 
                N = N < 8 ? 8 : N;
            } catch (Exception e) {
            }
            try {
                M = Integer.parseInt(p[1]);
                //Normalize
                M = M < 2 ? 2 : M;
            } catch (Exception e) {
            }
            this.m = M;
            this.genome = new boolean[N];
            setNotEvaluated();
        } catch (Exception e) {
            //something wrong happen
        }
    }

    @Override
    public String getParameters() {
        return N + " " + M;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290930L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        Jump r = new Jump();
        r.setParameters("5 4");
        System.out.println(r.toString());

        Individual s = r.getClone();
        r.randomize();
        r.evaluate();
        System.out.println("Clone " + s.toString());
        System.out.println("original " + r.toString());
        System.out.println("original " + r.toStringPhenotype());

    }

}
