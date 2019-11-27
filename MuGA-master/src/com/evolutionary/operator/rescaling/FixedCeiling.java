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
public class FixedCeiling extends Rescaling {

   
    
     double factor = 2.0;

    @Override
    public Population execute(Population pop) {
        // verify type of population
        if (pop instanceof MultiPopulation) {
            MultiPopulation mpop = (MultiPopulation)pop;
            //for each multi-índividual
            for (Individual ind : pop.getGenomes()) {
                //calculate new number of copies
                int numCopies = (int) Math.ceil(mpop.getCopies(ind) / factor);
                //upadate number of copies
                mpop.setCopies(ind,numCopies < 1 ? 1 : numCopies);
            }
        }        
        return pop;
    }
        
    
     public double getFator(Population pop){
         return factor;
     }
     
     @Override
    public String getInformation() {
      StringBuilder buf = new StringBuilder(Genetic.getFullName(this) + "\n");
        buf.append("\nFixed Rescaling factor");        
	buf.append("\n\nAlgorithm:");
	buf.append("\nfor each individual in population");	
    	buf.append("\n   individual.#copias = ceil( individual.#copias /FACTOR)");
        buf.append("\nnext");	
        buf.append("\nParameters : <FACTOR>");
        buf.append("\n   <FACTOR> factor to aply in copies");        
        return buf.toString();
    }

    //----------------------------------------------------------------------------------------------------------
    @Override
    public String getParameters() {
        return factor + "";
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
            factor = Double.parseDouble(aParams[0]);
            factor = factor < 1 ? 1 : factor; //normalize
        } catch (Exception e) {
        }
    }//----------------------------------------------------------------------------------------------------------

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510031630L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
