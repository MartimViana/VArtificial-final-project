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

import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.Individual;

/**
 * Created on 2/out/2015, 7:50:41
 *
 * @author zulu - computer
 */
public class FlipBit extends Mutation {

   
        


    @Override
    public void mutate(Individual solution) {
         double mutProbability =  getMutProbability(solution);
        //get individual
        BinaryString mut = (BinaryString) solution;
        //get bits
        boolean[] bits = mut.getBitsGenome();
        //for each bit
        for (int j = 0; j < bits.length; j++) {
            //get chance
            if (random.nextDouble() < mutProbability) {
                //change bit
                bits[j] = !bits[j];
                //remove evaluation
                mut.setNotEvaluated();
            }
        }
    }

    @Override
    public String getInformation() {
        StringBuilder txt = new StringBuilder(getName());
        txt.append("\n" + getClass().getSimpleName() + "( P_MUT )\n");
        txt.append("\n\nmutate bits in the genome");
        txt.append("\n\nParameters:");
        txt.append("\n    <P_MUT>  - probability to mutate one bit");
        txt.append("\n             -1.0 => 1.0 / genome.lenght");

        return txt.toString();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510020750L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
