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
package com.evolutionary.report.statistics;

import com.evolutionary.population.Population;
import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.Individual;
import com.evolutionary.solver.EAsolver;

/**
 * A. Manso, L. Correia Genetic Algorithms using Populations based on Multisets
 * EPIA'2009 - Fourteenth Portuguese Conference on Artificial Intelligence
 * Aveiro , Portugal, Outubro 2009
 *
 * Created on 15/mar/2016, 15:47:45
 *
 * @author zulu - computer
 */
public class GeneticDiversityBinary extends AbstractStatistics {

    public GeneticDiversityBinary() {
        super("Genetic Diversity");
        higherIsBetter = true;
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    public double execute(EAsolver s) {
        Population pop = s.parents;
        return binaryDiversity(pop);
    }

    public double binaryDiversity(Population pop) {
        //permutations
        if (!(pop.getTemplate() instanceof BinaryString)) {
            return 0;
        }

        //calculate histogram of population
        int hist[] = new int[pop.getTemplate().getNumGenes()];
        boolean[] bits;
        for (Individual ind : pop.getGenomes()) {
            bits = ((BinaryString) ind).getBitsGenome();
            for (int i = 0; i < bits.length; i++) {
                if (bits[i]) {
                    hist[i] += pop.getCopies(ind);
                }

            }
        }
        //calculate statistic
        double val = 0.0;
        //number of individuals
        double numPop = pop.getNumberOfIndividuals();
        for (int i = 0; i < hist.length; i++) {
            double ones = hist[i];
            double zeros = (numPop - hist[i]);
//            System.out.print("<" + (ones * zeros) + "> ");
            val += ones * zeros;
        }
        return (val * 4) / (hist.length * numPop * numPop);

    }

    @Override
    public String getInformation() {
        StringBuffer txt = new StringBuffer();
        txt.append("Genetic diversity of the parents population\n");
        txt.append("\nBinaryString : sum of ratios 1/0 for each allel\n");
        txt.append("\nREF: Genetic Algorithms using Populations based on Multisets\n");
        txt.append("A. Manso, L. Correia");
        return txt.toString();

    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603151547L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
