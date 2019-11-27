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
import com.evolutionary.problem.bits.BinaryString;
import com.utils.MyString;

/**
 * Created on 29/set/2015, 9:30:30
 *
 * @author zulu - computer
 */
public class R1 extends BinaryString {

    public static int SIZE_OF_BLOCK = 8;
    public static int NUMBER_OF_BLOCKS = 16;

    protected int sizeOfBlocks;

    public R1() {
        super(SIZE_OF_BLOCK * NUMBER_OF_BLOCKS, Optimization.MAXIMIZE);
        this.sizeOfBlocks = SIZE_OF_BLOCK;
    }

    public R1(R1 other) {
        super(other);
        this.sizeOfBlocks = other.sizeOfBlocks;
    }

    public R1 getClone() {//-------------------------------------------- get clone
        return new R1(this);
    }

    @Override
    public boolean isOptimum() {
        return value == getNumGenes();
    }

    @Override
    protected double evaluate(boolean[] genome) {
        int value = 0;
        int numberOfBlocks = genome.length / sizeOfBlocks;
        for (int i = 0; i < numberOfBlocks; i++) {
            int block = 0;
            for (int j = 0; j < sizeOfBlocks; j++) {
                if (genome[i * sizeOfBlocks + j]) {
                    block++;
                }
            }
            if (block == sizeOfBlocks) {
                value += block;
            }
        }
        return value;
    }

    public String toStringPhenotype() {//--------------------------------------- textual representation of phenotype
        boolean[] genes = getBitsGenome();
        StringBuilder txt = new StringBuilder(genes.length + genes.length / sizeOfBlocks);
        int numberOfBlocks = genes.length / sizeOfBlocks;
        for (int i = 0; i < numberOfBlocks; i++) {
            for (int j = 0; j < sizeOfBlocks; j++) {
                txt.append(genes[i * sizeOfBlocks + j] ? '1' : '0');
            }
            txt.append(' ');
        }
        return txt.toString();
    }

    @Override
    public String getInformation() {
        // public String getInfo() {
        StringBuilder txt = new StringBuilder(super.getInformation());
        txt.append("\nCount the bits of correct blocks");
        txt.append("\nParameters <NUMBER_OF_BLOCKS><SIZE_OF_BLOCK>");
        txt.append("\n           <NUMBER_OF_BLOCKS>  number of Blocks");
        txt.append("\n           <SIZE_OF_BLOCK>  number of bits in each block");
        txt.append("\n\nMitchell, M., Forrest, S., and Holland, J. H. (1992) The royal road for");
        txt.append("\ngenetic algorithms: Fitness landscapes and GA performance");
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
                NUMBER_OF_BLOCKS = Integer.parseInt(p[0]);
                //normalize 
                NUMBER_OF_BLOCKS = NUMBER_OF_BLOCKS < 1 ? 1 : NUMBER_OF_BLOCKS;
            } catch (Exception e) {
            }
            try {
                SIZE_OF_BLOCK = Integer.parseInt(p[1]);
                //Normalize
                SIZE_OF_BLOCK = SIZE_OF_BLOCK < 2 ? 2 : SIZE_OF_BLOCK;
            } catch (Exception e) {
            }
            this.sizeOfBlocks = SIZE_OF_BLOCK;
            this.genome = new boolean[this.sizeOfBlocks * NUMBER_OF_BLOCKS];
            setNotEvaluated();
        } catch (Exception e) {
            //something wrong happen
        }
    }

    @Override
    public String getParameters() {
        return NUMBER_OF_BLOCKS + " " + SIZE_OF_BLOCK;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290930L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        R1 r = new R1();
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
