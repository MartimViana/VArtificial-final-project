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
package com.evolutionary.operator.mutation.permutation;

import com.evolutionary.operator.mutation.*;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.permutation.Permutations;

/**
 * Created on 2/out/2015, 7:50:41
 *
 * @author zulu - computer
 */
public class SwapGenes extends Mutation {


    @Override
    public void mutate(Individual individual) {
        double mutProbability = getMutProbability(individual);        
        //get indexes
        int[] value = ((Permutations) individual).getIndexesGenome();
        //for each bit
        for (int index = 0; index < value.length; index++) {
            //get chance
            if (random.nextDouble() < mutProbability) {
                //swap index
               int pos =random.nextInt(value.length);
               int aux = value[index];
               value[index] = value[pos];
               value[pos] = aux;
                //remove evaluation
                individual.setNotEvaluated();
            }
        }
    }

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(getName());
        txt.append("\n" + getClass().getSimpleName() + "( P_MUT )\n");
        txt.append("\n\nswap random indexes in the genome");
        txt.append("\n\nParameters:");
        txt.append("\n    <P_MUT>  - probability to mutate one index");
        txt.append("\n             -1.0 => 1.0 / genome.lenght");

        return txt.toString();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510020750L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
