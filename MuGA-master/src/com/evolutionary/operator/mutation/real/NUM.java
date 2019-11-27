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
public class NUM extends Mutation {

    // factor of domain size contraction 
    public double B = 2; // power of mutation

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
        //gene values 
        double[] geneValue = ((RealVector) ind).getGenome();
        //for all genes
        double delta, progress = solver.stop.completedRatio(solver);
        for (int i = 0; i < geneValue.length; i++) {

            //probility of mutation increase with number of copies
            if (random.nextDouble() < probability) {

                if (random.nextDouble() < 0.5) {
                    delta = ind.getMaxValue() - geneValue[i];
                } else {
                    delta = ind.getMinValue()-geneValue[i] ;
                }
                delta *= Math.pow(random.nextDouble(),  Math.pow(1 - progress, B / original.getNumberOfCopies()));
                geneValue[i] += delta;
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
            B = Double.parseDouble(par.nextToken());
        } catch (Exception e) {
        }
    }

    @Override
    public String getParameters() {
        return pMutation + " " + B;
    }

    @Override
    public String getInformation() {
        StringBuilder buf = new StringBuilder();
        buf.append(toString());
        buf.append("\nNon Mutation Mutation");
        buf.append("\nMichalewicz’s non-uniform mutation ");
        buf.append("\nPower of mutation <" + B + ">");
        return buf.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 2019050181402L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
