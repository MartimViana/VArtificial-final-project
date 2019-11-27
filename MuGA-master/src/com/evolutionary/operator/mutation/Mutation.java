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
package com.evolutionary.operator.mutation;

import com.evolutionary.operator.GeneticOperator;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import java.util.Collections;
import java.util.List;

/**
 * Created on 1/out/2015, 11:47:02
 *
 * @author zulu - computer
 */
public abstract class Mutation extends GeneticOperator {

    public abstract void mutate(Individual offspring);

    protected double pMutation = -1; // default probability of mutation

    public Population execute(Population pop) {
        //----------------------------------------------------------------
        List<Individual> children = pop.getGenomes();
        //make new population
        Population offspring = pop.getCleanClone();
        for (Individual descendant : children) {
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //multiset population
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            for (int i = 1; i < descendant.getNumberOfCopies(); i++) {
                //clone de individual and update copynumber
                Individual clone = descendant.getClone();
                clone.setNumberOfCopies(i+1);
                //mutate the clone
                mutate(clone);
                //add clone to the new population
                offspring.addIndividual(clone, 1);
            }
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            // Simple Population - single mutation
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //mutate simple individual
            Individual clone = descendant.getClone();
            clone.setNumberOfCopies(1);
            mutate(clone);
            //add to the new population
            offspring.addIndividual(clone, 1);
        }
        return offspring;
    }

    /**
     * default probabilitiy real coded : 1 bit in each 200 individuals bits code
     * : 1 bit in each indivial
     *
     * @param ind individual template
     * @return default probability of mutation
     */
    public double getMutProbability(Individual ind) {
        if (pMutation == -1) {
            if (ind instanceof RealVector) { // mutação nos reais
                return 0.1 / ind.getNumGenes(); 
            }
            return 1.0 / ind.getNumGenes();
        }
        return pMutation;
    }

    @Override
    public String getParameters() {
        return pMutation + "";
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * update parameters od the operator
     *
     * @param params parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    @Override
    public void setParameters(String params) throws RuntimeException { //update parameters by string values
        super.setParameters(params);
        try {
            //split string by withe chars
            String[] aParams = params.split("\\s+");
            if (aParams.length > 0) {
                pMutation = Double.parseDouble(aParams[0]);
            }
            //validate crossover probability
            if (pMutation != -1 && (pMutation < 0 || pMutation > 1)) {
                throw new RuntimeException("invalid probability of Mutation : " + pMutation);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }//----------------------------------------------------------------------------------------------------------

    @Override
    public Population execute(Population... pop) {
        return execute(pop[0]);
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510011147L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
