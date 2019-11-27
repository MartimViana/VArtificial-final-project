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

package com.evolutionary.solver;

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import java.util.List;

public class GA extends EAsolver {

    @Override
    public void iterate() {        
        //select breeder
        selected = selection.execute(parents);
               
        // recombine breeder to make offspring
        Population offspring = recombination.execute(selected.getClone());
       
        //mutate offspring
        offspring = mutation.execute(offspring);
        
        //join parents and offspring
        offspring.evaluate();        
        parents = replacement.execute(parents, offspring);
        
        //normalize number of clones
        parents = rescaling.execute(parents);
        
        //update statistics
        updateEvolutionStats();
  
    }
    
    public static void testSimplePop(Population newPop, String iter){
          List<Individual> np = newPop.getIndividualsList();
        for (Individual individual : np) {
            if(individual.getNumberOfCopies() > 1){
                System.out.println("ERROR " + iter);
            }
        }
    }

    public String getInformation() {
        StringBuilder str = new StringBuilder();
        str.append("\n Genetic Algorithm Solver");
        str.append("\n 1 - create POP");
        str.append("\n 2 - evaluate POP");
        str.append("\n 3 - until STOP criteria");
        str.append("\n    3.1 - MATE = selection(POP)");
        str.append("\n    3.2 - MATE = recombination(MATE)");
        str.append("\n    3.3 - MATE = mutation(MATE)");
        str.append("\n    3.4 - evaluate MATE");
        str.append("\n    3.5 - POP  = replacement(POP,MATE)");
        str.append("\n    3.6 - POP  = rescaling(POP)");
        return str.toString();
    }
}
