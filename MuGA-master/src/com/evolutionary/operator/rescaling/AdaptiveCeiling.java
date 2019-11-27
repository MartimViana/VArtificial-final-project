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
package com.evolutionary.operator.rescaling;

import com.evolutionary.Genetic;
import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;

/**
 * Created on 3/out/2015, 16:30:30
 *
 * @author zulu - computer
 */
public class AdaptiveCeiling extends Rescaling {

    double maxProportion = 1.5;

    @Override
    public Population execute(Population pop) {
        //verify type of population
        if (pop instanceof MultiPopulation) {
            MultiPopulation mpop = (MultiPopulation) pop;
            //calculate the division factor
            double factor = getDivisionFator(mpop);
            //population copies are ok ?
            if (factor < 1) {
                return pop;
            }
            //for each multi-individual in the population
            for (Individual ind : pop.getGenomes()) {
                //update number of copies
                int numCopies = (int) Math.ceil(mpop.getCopies(ind) / factor);
                mpop.setCopies(ind, numCopies);
            }
        }
        return pop;
    }

    public double getDivisionFator(Population pop) {
        double factor = (double) pop.getNumberOfIndividuals() / ((double) pop.getPopulationSize() * maxProportion);
        if (factor < 1) {
            return 1;
        }
        return factor;
    }

    //----------------------------------------------------------------------------------------------------------
    @Override
    public String getParameters() {
        return maxProportion + "";
    }

    /**
     * update parameters of the operator
     *
     * @param params parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    public void setParameters(String params) throws RuntimeException {
        String[] aParams = params.split("\\s+");
        try {
            maxProportion = Double.parseDouble(aParams[0]);
            maxProportion = maxProportion <= 0 ? 0.01 : maxProportion; //normalize
        } catch (Exception e) {
        }
    }//----------------------------------------------------------------------------------------------------------

    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder(Genetic.getFullName(this) + "\n");
        buf.append("\nAdaptive Rescaling by ceiling");
        buf.append("\n\nAlgorithm:");
        buf.append("\nfactor =  pop.#individuals / pop.#genotypes * MAXPROPORTION");
        buf.append("\nfor each individual in population");
        buf.append("\n   individual.#copias = ceil( individual.#copias /factor)");
        buf.append("\nnext");
        buf.append("\nParameters : <MAXPROPORTION>");
        buf.append("\n   <MAXPROPORTION> proportion of individuals desired in the population");
        return buf.toString();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031630L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
