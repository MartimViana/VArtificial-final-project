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
public class SimplePermutation extends Permutations {

    public SimplePermutation() {
        super(DEFAULT_SIZE, Optimization.MAXIMIZE);
    }

    @Override
    protected double evaluate(int[] genome) {
        int count = 0;
        for (int i = 0; i < genome.length; i++) {
            count += genome[i] == i ? 1 : 0;

        }
        return count;
    }

    @Override
    public boolean isOptimum() {
        return fitness() == getNumGenes();
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            SimplePermutation sp = new SimplePermutation();
            sp.setParameters("128");
            sp.evaluate();
            System.out.println(sp);

        }

    }

}
