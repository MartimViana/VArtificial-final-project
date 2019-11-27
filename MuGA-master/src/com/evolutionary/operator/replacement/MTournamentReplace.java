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

package com.evolutionary.operator.replacement;

import com.evolutionary.Genetic;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import java.util.List;

/**
 * Created on 1/out/2015, 12:40:50
 *
 * @author zulu - computer
 */
public class MTournamentReplace extends Replacement {

    protected int poolSizeOffspring = 2; // pool size of offspring
    protected int poolSizeParents = 2; // pool size of parents

    @Override
    public Population execute(Population parents, Population offspring) {
        
        //create new clean population
        Population newPop = parents.getCleanClone();
        //calculate the number of genotypes in main population
        final int numberOfGenotype = parents.getPopulationSize();
        //get list of offsprings
        List<Individual> children = offspring.getIndividualsList();
        //get list of parents
        List<Individual> progenitors = parents.getIndividualsList();
        // while new population is not completed - numberOfGenotype
        // or not have more children
        while (newPop.getPopulationSize() < numberOfGenotype && children.size() >= poolSizeOffspring) {            
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //get random child
            Individual bestChild = children.get(random.nextInt(children.size()));
            //get other children
            for (int i = 1; i < poolSizeOffspring; i++) { // starts in 1
                Individual ind = children.get(random.nextInt(children.size()));
                //select the best of the tournament
                if (ind.compareTo(bestChild) > 0) {
                    bestChild = ind;
                }
            }
            //Pool size of parents is empty
            if (poolSizeParents == 0) {
                newPop.addIndividual(bestChild);
                children.remove(bestChild);
                continue;
            }

            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //get random parent
            Individual bestParent = progenitors.get(random.nextInt(progenitors.size()));
            //get other parents
            for (int i = 1; i < poolSizeParents; i++) {
                Individual ind = progenitors.get(random.nextInt(progenitors.size()));
                //select the best of the tournament
                if (ind.compareTo(bestParent) > 0) {
                    bestParent = ind;
                }
            }
            
            
            //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            //Select best individual - preference to the children
            if (bestParent.compareTo(bestChild) > 0) {//strictly better
                newPop.addIndividual(bestParent);
                progenitors.remove(bestParent);
            } else {
                // appetite to the novelty 
                //same fitness select children
                newPop.addIndividual(bestChild);
                children.remove(bestChild);
            }
           
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        // INVARIANT -  parents population maintains constant size
        // complete population if necessary 
        //Multipopulacion or offspring.size < numberOfGenotype        
        while (newPop.getPopulationSize() < numberOfGenotype) {
            //add random parent
            newPop.addIndividual(progenitors.remove(random.nextInt(progenitors.size())));
        }
        
      
        
        
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        return newPop;
    }

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(Genetic.getFullName(this));
        txt.append("\n" + getClass().getSimpleName() + "(" + poolSizeParents + " ," + poolSizeOffspring + ")\n");

        txt.append("\nSelects new population by tournaments between generations\n");
        txt.append("\n\nInitialize newPop");

        txt.append("\nWhile new generation is not complete");
        txt.append("\n    tourP = Select random <POOL_PAR> elements of  parents ");
        txt.append("\n    tourC = Select random <POOL_OFF> elements of  offspring ");
        txt.append("\n    if( best(tourP) > best(tourC))");
        txt.append("\n        select best(tournP) to  newPop  ");
        txt.append("\n    else");
        txt.append("\n        select best(tournC) to newPop  ");
        txt.append("\n\nParameters :");
        txt.append("\n      POOL_PAR - number of parents selected to the tournament");
        txt.append("\n      POOL_OFF - number of children selected to the tournament");

        return txt.toString();
    }

    //----------------------------------------------------------------------------------------------------------
    @Override
    public String getParameters() {
        return poolSizeParents + " " + poolSizeOffspring;
    }

    /**
     * update parameters of the operator
     *
     * @param param parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    @Override
    public void setParameters(String params) throws RuntimeException {
        //split string by withe chars
        String[] param = params.split("\\s+");
        try {
            poolSizeParents = Integer.parseInt(param[0]);
        } catch (Exception e) {
        }
        try {
            poolSizeOffspring = Integer.parseInt(param[1]);
        } catch (Exception e) {
        }

    }//----------------------------------------------------------------------------------------------------------

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510011240L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
