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
package com.evolutionary.operator.recombination;

import com.evolutionary.operator.GeneticOperator;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created on 1/out/2015, 11:47:02
 *
 * @author zulu - computer
 */
public abstract class Recombination extends GeneticOperator {

    /**
     * Exchange genes between parents
     *
     * @param parents parents individuals
     * @return offsprings
     */
    public abstract Individual[] recombine(Individual... parents);

    protected static double default_pCrossover = 0.75; // default probability of crossover
    protected double pCrossover;

    public Recombination() {
        this(default_pCrossover);
    }

    public Recombination(double probCrossover) {
        this.pCrossover = probCrossover;
    }

    @Override
    public Population execute(Population... pop) {
        return execute(pop[0]);
    }

    public Population execute(Population offspring) {
        //clean population  
        Population newPopulation = offspring.getCleanClone();
        //individuals in the population
        List<Individual> lst = offspring.getGenomes();
        Collections.shuffle(lst, random);
        //iterate offspring population
        while (!lst.isEmpty()) {
            //select random first parent
            Individual indiv1 = lst.remove(random.nextInt(lst.size())).getClone();
            //Recombination or clone ?
            if (lst.isEmpty() || random.nextDouble() >= pCrossover) {
                //clone of parent
                newPopulation.addIndividual(indiv1, indiv1.getNumberOfCopies());
                continue;
            }// END: if
            //select random second parent
            Individual indiv2 = lst.remove(random.nextInt(lst.size())).getClone();
            //select max recombinations 
            int maxRecombine = Math.max(indiv1.getNumberOfCopies(), indiv2.getNumberOfCopies());
            for (int i = 1; i <= maxRecombine; i++) {
                Individual i1 = indiv1.getClone();
                i1.setNumberOfCopies(Math.min(i, indiv1.getNumberOfCopies()));
                Individual i2 = indiv2.getClone();
                i2.setNumberOfCopies(Math.min(i, indiv2.getNumberOfCopies()));
                //execute crossover
                Individual[] desc = recombine(i1, i2);

                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
             
                //add offsprings to the new population
                if (i <= indiv1.getNumberOfCopies()) {
                    newPopulation.addIndividual(desc[0], 1);
                }
                if (i <= indiv2.getNumberOfCopies()) {
                    newPopulation.addIndividual(desc[1], 1);
                }

            }

        }// END: population
        return newPopulation;
    }

    public double getProbability() {
        return pCrossover;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510011147L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
