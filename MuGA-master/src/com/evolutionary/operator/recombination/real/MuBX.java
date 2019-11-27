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

/**
 * Created on 2/out/2015, 8:38:28
 *
 * @author zulu - computer
 */
public class MuBX extends Recombination {

    double EXPAND = 1.0;

    @Override
    public Individual[] recombine(Individual... parents) {
        return executeBX((RealVector) parents[0], (RealVector) parents[1]);
    }

    public Individual[] executeBX(Individual ind1, Individual ind2) {
        RealVector parent1 = (RealVector) ind1;
        RealVector parent2 = (RealVector) ind2;

        //int numChildren = i0.getNumCopies() + i1.getNumCopies();
        double[] p1 = parent1.getGenome();
        double[] p2 = parent2.getGenome();
        int numberOfGenes = p2.length;

        double[] cv1 = new double[p2.length];
        double[] cv2 = new double[p2.length];

        for (int i = 0; i < numberOfGenes; i++) {

            double d = Math.abs(p1[i] - p2[i]);
            double x1 = Math.min(p1[i], p2[i]) - EXPAND * d * ind1.getNumberOfCopies();
            double x2 = Math.max(p1[i], p2[i]) + EXPAND * d * ind2.getNumberOfCopies();

            cv1[i] = uniform(x1, x2);
            cv2[i] = uniform(x1, x2);
        }
        parent1.setDoubleValues(cv1);
        parent2.setDoubleValues(cv2);

        //return descendants 
        return new RealVector[]{parent1, parent2};
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder();
        buf.append(toString());
        buf.append("\nMultiset Centroid Crossover ");
        buf.append("\nRange[-" + EXPAND + "," + (1.0 + EXPAND) + "]");
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
