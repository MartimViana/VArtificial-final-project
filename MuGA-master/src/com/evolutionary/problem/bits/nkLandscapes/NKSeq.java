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
package com.evolutionary.problem.bits.nkLandscapes;

import com.evolutionary.problem.bits.BinaryString;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created on 5/jun/2017, 12:31:20
 *
 * @author zulu - computer
 */
public class NKSeq extends NKLandscapeAbstract {

    public static long RANDOM_SEED = 127; // to make the same problems
    public static double OPTIMUM_VALUE = 0; // optimum value
    public static boolean OPTIMUM_STRING[]; // optimum string

    public static List< Pair< int[], double[]>> data;
    public static int N = 128; // number of genes
    public static int K = 3; // number of bits by rules
    public static int M = 128; // number of rules

    static { // create initial landscape
        createLandscape();
    }

    /**
     * creates a landscape with sequencial rules
     */
    public static void createLandscape() {
        // initialize random with a seed to generate same problems
        // and experiments can be reproducted
        Random rnd = new Random(RANDOM_SEED * M * N * K);// change seed
        data = new ArrayList<>();
        for (int i = 0; i < M; i++) { //generate rules
            //sequential bits
            int bits[] = new int[K];
            for (int r = 0; r < K; r++) {
                bits[r] = (i + r) % N;
            }
            //random values [0,1[
            double[] value = new double[1 << K];
            for (int j = 0; j < value.length; j++) {
                value[j] = rnd.nextDouble();
            }
            data.add(Pair.of(bits, value));
        }
        createOptimum(rnd);
    }

    public static void createOptimum(Random rnd) {
        //generate random optimum
        OPTIMUM_STRING = new boolean[N];
        for (int i = 0; i < OPTIMUM_STRING.length; i++) {
            OPTIMUM_STRING[i] = rnd.nextBoolean();
        }
        //updates rules to set optimum
        OPTIMUM_VALUE = setOptimumValue(OPTIMUM_STRING, data);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201706051231L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public NKSeq() {
        super(N, data);
    }

    public NKSeq(NKSeq ind) {
        super(ind);
    }

    @Override
    public NKSeq getClone() {
        return new NKSeq(this);
    }

    /**
     * gets optimum genome by default is not avaiable
     *
     * @return optimum genome
     */
    public boolean[] getOptimumGenome() {
        return OPTIMUM_STRING;
    }

    @Override
    public boolean isOptimum() {
        return getFitness() == OPTIMUM_VALUE;
    }

    @Override
    public String getInformation() {
        // public String getInfo() {
        StringBuilder txt = new StringBuilder(super.getInformation());
        txt.append("\nGenerate a NK sequencial problem ");
        txt.append("\nOptimum string = " + BinaryString.booleanArrayToString(OPTIMUM_STRING));
        txt.append("\nOptimum value  = " + OPTIMUM_VALUE);

        txt.append("\n\nParameters N K M");
        txt.append("\n   <N>  Number of genes");
        txt.append("\n   <K>  Number bits in Rule");
        txt.append("\n   <M>  Number os rules");
        txt.append("\nRules data\n");
        txt.append(getProblemInfo());
        return txt.toString();
    }

    /**
     * update the size of the genome
     *
     * @param params height of the tree ( power of 2 )
     */
    @Override
    public void setParameters(String params) {
        String p[] = params.split(" ");
        int param_N = N;
        int param_K = K;
        int param_M = M;
        try {
            param_N = Integer.parseInt(p[0]);
        } catch (Exception e) {
        }
        try {
            param_K = Integer.parseInt(p[1]);
            param_K = param_K > 10 ? 10 : param_K; // maximum 10 bits
        } catch (Exception e) {
        }
        try {
            param_M = Integer.parseInt(p[2]);
        } catch (Exception e) {
        }
        if (N != param_N || K != param_K || M != param_M) { // create new problem 
            N = param_N;
            K = param_K;
            M = param_M;
            createLandscape();
            this.problem = data;
            super.setParameters(N + "");
        }

    }

    @Override
    public String getParameters() {
        return getNumGenes() + " " + data.get(0).getLeft().length + " " + data.size();
    }

}
