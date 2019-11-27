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
package com.evolutionary.operator.mutation.real;

import com.evolutionary.operator.mutation.Mutation;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import java.util.StringTokenizer;

/**
 * Created on 2/out/2015, 7:50:41
 *
 * @author zulu - computer
 */
public class Gauss extends Mutation {

    // factor of domain size contraction 
    public double FACTOR = 0.1; // 10% of space dimension

    public double getScaledDomain(RealVector ind) {
        return (ind.getMaxValue() - ind.getMinValue()) * FACTOR;
    }
    
    public double getDistribution() {
        return random.nextGaussian();
    }

    /**
     * mutates one individual
     *
     * @param original individual to mutate
     */
    public void mutate(Individual original) {
        RealVector ind = (RealVector) original;
        //probability to mutate one gene
        double probability = getMutProbability(ind);
        //dimension of mutation
        double dimension = getScaledDomain(ind);
        //gene values 
        double[] geneValue = ((RealVector) ind).getGenome();
        //for all genes
        for (int i = 0; i < geneValue.length; i++) {
            //probility of mutation increase with number of copies
            if (random.nextDouble() < probability) {
                //mutate by gaussian(0, FACTOR) * dimension
                geneValue[i] += getDistribution() * dimension;
            }
        }
        //update and correct values of individual
        ind.setDoubleValues(geneValue);
    }

    @Override
    public void setParameters(String str) {
        StringTokenizer par = new StringTokenizer(str);
        try {
            pMutation = Double.parseDouble(par.nextToken());
        } catch (Exception e) {
        }
        try {
            FACTOR = Double.parseDouble(par.nextToken());
        } catch (Exception e) {
        }
    }

    @Override
    public String getParameters() {
        return pMutation + " " + FACTOR;
    }

    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder();
        buf.append(toString());
        buf.append("\nMutation Using Gaussian( 0, 1) distribution");
        buf.append("\nFactor of mutation <" + FACTOR + ">");
        buf.append("\nParameters <Probability><DIM>");
        buf.append("\n<Probability> to mutate one gene");
        buf.append("\n<DIM> dimension of mutation [0,1]");
        return buf.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 2019050181402L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
