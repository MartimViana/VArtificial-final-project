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
package com.evolutionary.operator.recombination.permutation;

import com.evolutionary.operator.recombination.*;
import com.evolutionary.Genetic;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.permutation.Permutations;
import java.util.Arrays;
import java.util.Random;

/**
 * Created on 2/out/2015, 8:38:28
 *
 * @author zulu - computer
 */
public class UPX extends Recombination {

    private double pSwap = 0.5;

    @Override
    public Individual[]   recombine(Individual... parents) {
        if (random.nextDouble() < pCrossover) {
            doCrossover((Permutations) parents[0], (Permutations) parents[1], pSwap, random);
        }
        return parents;
    }

    protected static void doCrossover(Permutations i1, Permutations i2, double pSwap, Random random) {
        int[] p1 = i1.getIndexesGenome();
        int[] p2 = i2.getIndexesGenome();
        boolean mask[] = createMask(p1.length, pSwap, random);
        int[] c1 = UX_driver(p1, p2, mask);
        int[] c2 = UX_driver(p2, p1, mask);
        i1.setGenome(c1);
        i2.setGenome(c2);
    }
    private static final int EMPTY = -1;

    public static int[] UX_driver(int[] p1, int[] p2, boolean[] mask) {
        int[] c = new int[p1.length];
        //clone original parents
        int[] a = new int[p1.length];
        System.arraycopy(p1, 0, a, 0, p1.length);
        int[] b = new int[p1.length];
        System.arraycopy(p2, 0, b, 0, p2.length);

        //Copy Genes of A
        for (int i = 0; i < mask.length; i++) {
            if (mask[i]) {
                c[i] = a[i];
                removeElement(a, a[i]);
            } else {
                c[i] = EMPTY;
            }
        }
        //Preserves Genes of B
        for (int i = 0; i < mask.length; i++) {
            if (c[i] == EMPTY && !contains(c, b[i])) {
                c[i] = b[i];
                removeElement(a, b[i]);
            }
        }
        //complete chromossom        
        for (int i = 0; i < mask.length; i++) {
            if (c[i] == EMPTY) {
                c[i] = removeFirst(a);
            }
        }
        return c;
    }

    //Improving Genetic Algorithms by Search Space Reductions
    // (with Applications to Flow Shop Scheduling)
    //http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.50.2308
    public static int removeFirst(int[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] != EMPTY) {
                int aux = v[i];
                v[i] = EMPTY;
                return aux;
            }
        }
        return EMPTY;
    }

    public static void removeElement(int[] v, int elem) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] == elem) {
                v[i] = EMPTY;
                return;
            }
        }
    }

    public static boolean contains(int[] v, int elem) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] == elem) {
                return true;
            }
        }
        return false;
    }

    /**
     * creates a mask to exchange genes
     *
     * @param size
     * @param cuts
     * @return
     */
    public static boolean[] createMask(int size, double probSwap, Random random) {
        boolean[] mask = new boolean[size];
        for (int i = 0; i < mask.length; i++) {
            boolean b = random.nextDouble() < probSwap;
        }
        return mask;
    }

    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder(Genetic.getFullName(this) + "\n");
        buf.append("\n UPX - Uniform Permutation crossover ");
        buf.append("\nRecombine parents whith uniform order-based crossover ");
        buf.append("\n1- Generate a mask to change genes");
        buf.append("\n2- Copy genes from first parent with the mask");
        buf.append("\n3- Copy genes from second parents with the mask");
        buf.append("\n4- Complete genes with the order of the first parent");
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
    public static void main(String[] args) {
        int a[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int b[] = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

        boolean mask[] = {true, true, true,
            false, false, false, false, false, false, false};

        int c[] = UX_driver(a, b, mask);
        System.out.println(Arrays.toString(c));

    }

}
