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
package com.evolutionary.problem.bits.RoyRoad;

import com.evolutionary.problem.Individual;

/**
 * Created on 29/set/2015, 9:30:30
 *
 * @author zulu - computer
 */
public class R2 extends R1 {

    public R2() {
        super();
    }

    public R2(R2 other) {
        super(other);
    }

    public R2 getClone() {//-------------------------------------------- get clone
        return new R2(this);
    }

    @Override
    public boolean isOptimum() {
        int total = 0;
        int schemaSize = this.getNumGenes();
        int lenght = this.getNumGenes();
        while (schemaSize >= sizeOfBlocks) {
            total += lenght ;
            schemaSize /= 2;
        }
        return this.value == total;
    }

    @Override
    protected double evaluate(boolean[] genome) {
        int value = 0;
        int size = genome.length;
        while (size >= sizeOfBlocks) {
            value += evaluate(genome, size);
            size /= 2;
        }
        return value;
    }

    protected double evaluate(boolean[] genome, int schemaSize) {
        int value = 0;
        int numberOfBlocks = genome.length / schemaSize;
        for (int i = 0; i < numberOfBlocks; i++) {
            int block = 0;
            for (int j = 0; j < schemaSize; j++) {
                if (genome[i * schemaSize + j]) {
                    block++;
                }
            }
            if (block == schemaSize) {
                value += block;
            }
        }
        return value;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290930L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        R2 r = new R2();
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
