///****************************************************************************/
///****************************************************************************/
///****     Copyright (C) 2012                                             ****/
///****     Ant�nio Manuel Rodrigues Manso                                 ****/
///****     e-mail: manso@ipt.pt                                           ****/
///****     url   : http://orion.ipt.pt/~manso    manso@ipt.pt             ****/
///****     Instituto Polit�cnico de Tomar                                 ****/
///****     Escola Superior de Tecnologia de Tomar                         ****/
///****                                                                    ****/
///****************************************************************************/
///****     This software was build with the purpose of learning.          ****/
///****     Its use is free and is not provided any guarantee              ****/
///****     or support.                                                    ****/
///****     If you met bugs, please, report them to the author             ****/
///****                                                                    ****/
///****************************************************************************/
///****************************************************************************/
package com.evolutionary.problem.permutation;

/**
 *
 * @author ZULU
 */
public class ChessQueens extends Permutations {

    public ChessQueens() {
        super(DEFAULT_SIZE, Optimization.MINIMIZE);
    }

    @Override
    protected double evaluate(int[] genome) {
        int ataks = 0;
        for (int index = 0; index < genome.length; index++) {
            ataks += getAtaks(index, genome);
        }
        return ataks;
    }

    @Override
    public boolean isOptimum() {
        return fitness() == 0;
    }

    /**
     * gets atacks of the queen
     *
     * @param queen Position of genome
     * @return number of atacks
     */
    public int getAtaks(int queen, int genes[]) {
        int numAtaks = 0;
        for (int index = 0; index < genes.length; index++) {
            if (index != queen && java.lang.Math.abs(queen - index)
                    == java.lang.Math.abs(genes[queen] - genes[index])) {
                numAtaks++;
            }
        }
        return numAtaks;
    }
    
      @Override
    public String toStringPhenotype() {
        int []genome = this.getIndexesGenome();
        StringBuilder result = new StringBuilder();
         //number of copies        
        for (int i = 0; i < genome.length; i++) {
            if (getAtaks(i,genome) > 0) {
                result.append(" X ");
            } else {
                result.append(" . ");
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            ChessQueens sp = new ChessQueens();
            sp.setParameters("4");
            sp.evaluate();
            System.out.println(sp + " PHENO " + sp.toStringPhenotype());
        }

    }

}
