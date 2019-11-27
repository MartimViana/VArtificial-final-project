//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2015   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.evolutionary.operator.recombination.real;

import com.evolutionary.operator.recombination.Recombination;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import com.utils.RandomVariable;

/**
 * Created on 2/out/2015, 8:38:28
 *
 * @author zulu - computer
 */
public class MuLX extends Recombination {

    double EXPAND = 0.5;

    @Override
    public Individual[] recombine(Individual... parents) {
        return executeAX((RealVector) parents[0], (RealVector) parents[1]);
    }

    public Individual[] executeAX(RealVector i0, RealVector i1) {
        //get genome values      
        final double[] v0 = i0.getGenome();
        final double[] v1 = i1.getGenome();
        //calculate new genes
        for (int i = 0; i < v0.length; i++) {
            //get distance
            double distance =  v1[i] -  v0[i];
            //get distribution
            double laplace = RandomVariable.laplace(random, 0, distance*EXPAND);
            //Crossover individuals                       
            v0[i] = v0[i]+laplace*i0.getNumberOfCopies();
            v1[i] = v1[i]+laplace*i1.getNumberOfCopies();            
        }
        //set gene values - Nomarlize values outside of boundaries
        i0.setDoubleValues(v0);
        i1.setDoubleValues(v1);
        //return descendants 
        return new RealVector[]{i0, i1};
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder();
        buf.append(toString());
        buf.append("\nLaplace Crossover ");        
        buf.append("\nParameters <PROBABILITY><EXPANSION>");
        buf.append("\n<PROBABILITY> to perform crossover");
        buf.append("\n<EXPANSION> expansion factor");
        return buf.toString();
    }

    @Override
    public String getParameters() {
        return pCrossover + " " + EXPAND;
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
        //split string by withe chars
        String[] aParams = params.split("\\s+");
        try {
            pCrossover = Double.parseDouble(aParams[0]);
        } catch (Exception e) {
        }
        try {
            EXPAND = Double.valueOf(aParams[1]);
        } catch (Exception e) {
        }
    }//----------------------------------------------------------------------------------------------------------

    /**
     * Generate a random number from a uniform random variable.
     *
     * @param min min of the random variable.
     * @param max max of the random variable.
     * @return a double.
     */
    public double uniform(double min, double max) {
        if (min > max) {
            return max + (min - max) * random.nextDouble();
        }
        return min + (max - min) * random.nextDouble();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201905151223L;

    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
}
