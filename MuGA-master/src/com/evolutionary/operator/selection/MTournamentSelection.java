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
package com.evolutionary.operator.selection;

import com.evolutionary.Genetic;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import java.util.List;

/**
 * Created on 2/out/2015, 8:05:06
 *
 * @author zulu - computer
 */
public class MTournamentSelection extends Selection {

    /**
     * Size of tournament 1- uniform 2- linear 3- quadratic (DJONG pp 58)
     */
    protected int tourSize;

    public MTournamentSelection() {
        tourSize = 2;
    }

    public MTournamentSelection(int size) {
        tourSize = size;
    }

    @Override
    public Population execute(Population parents) {
        //creates a new population
        Population selectedPop = parents.getCleanClone();
        // calculate number individuals to select
        int numberOfInfividuals = (int) (parents.getPopulationSize() * parentsProportion);
        //gets a list of parents individuals
        List<Individual> lst = parents.getIndividualsList();
        //execute numberOfInfividuals tournaments
        for (int k = 0; k < numberOfInfividuals; k++) {
            //get one individual
            Individual best = lst.get(random.nextInt(lst.size()));
            //get other individuals
            for (int i = 1; i < tourSize; i++) {
                Individual ind = lst.get(random.nextInt(lst.size()));
                //select the best of the tournament
                if (ind.compareTo(best) > 0) {
                    best = ind;
                }
            }
            //add best individual to the new population
            selectedPop.addIndividual(best.getClone());
        }

       
        return selectedPop;
    }

    //----------------------------------------------------------------------------------------------------------
    @Override
    public String getParameters() {
        return parentsProportion + " " + tourSize;
    }

    /**
     * update parameters of the operator
     *
     * @param params parameters separated by spaces
     * @throws RuntimeException not good values to parameters
     */
    @Override
    public void setParameters(String params) throws RuntimeException {
        //split string by withe chars
        String[] aParams = params.split("\\s+");
        try {
            parentsProportion = Double.parseDouble(aParams[0]);
        } catch (Exception e) {
        }
        try {
            tourSize = Integer.parseInt(aParams[1]);
        } catch (Exception e) {
        }

    }//----------------------------------------------------------------------------------------------------------

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(Genetic.getFullName(this));
        txt.append("\n" + getClass().getSimpleName() + "( PROPORTION_POP, SIZE_TOUR )\n");
        txt.append("\n\nSelects parents with tournaments");
        txt.append("\n\nParameters:");
        txt.append("\n    <PROPORTION_POP> proportion of the population to be selected");
        txt.append("\n                     #selected =  PROPORTION_POP * parents.size ");
        txt.append("\n    <SIZE_TOUR> number of individuals in the tournament");

        txt.append("\n\n Refs: http://en.wikipedia.org/wiki/Tournament_selection");
        txt.append("\nRefs: Miller, Brad L.; Goldberg, David E.");
        txt.append("\nGenetic Algorithms, Tournament Selection, and the Effects of Noise");

        return txt.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510020805L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
