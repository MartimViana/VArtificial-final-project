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

import com.evolutionary.operator.mutation.permutation.SwapGenes;
import com.evolutionary.operator.mutation.real.MuGauss;

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.bits.BinaryString;
import com.evolutionary.problem.permutation.Permutations;
import com.evolutionary.problem.real.RealVector;
import com.evolutionary.solver.EAsolver;

/**
 * Created on 30/jun/2017, 11:54:48
 *
 * @author zulu - computer
 */
public class DefaultMutation extends Mutation {

    Mutation bits = new FlipBit();
    Mutation perm = new SwapGenes();
    Mutation real = new MuGauss();

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201706301154L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2017  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void mutate(Individual offspring) {
        if (offspring instanceof BinaryString) {
            bits.mutate(offspring);
        }
        if (offspring instanceof Permutations) {
            perm.mutate(offspring);
        }
        if (offspring instanceof RealVector) {
            real.mutate(offspring);
        }
    }

    @Override
    public Population execute(Population offspring) {
        if (offspring.getTemplate() instanceof BinaryString) {
            return bits.execute(offspring);
        }
        if (offspring.getTemplate() instanceof Permutations) {
            return perm.execute(offspring);
        }
        if (offspring.getTemplate() instanceof RealVector) {
            return real.execute(offspring);
        }
        return offspring;
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public String getParameters() {
        if (solver == null) {
            return "";
        }
        if (solver.problem instanceof BinaryString) {
            return bits.getParameters();
        }
        if (solver.problem instanceof Permutations) {
            return perm.getParameters();
        }
        if (solver.problem instanceof RealVector) {
            return real.getParameters();
        }
        return "";

    }

    @Override
    public void setParameters(String params) throws RuntimeException { //update parameters by string values
        if (solver == null) {
            return;
        }
        if (solver.problem instanceof BinaryString) {
            bits.setParameters(params);
        }
        if (solver.problem instanceof Permutations) {
            perm.setParameters(params);
        }
        if (solver.problem instanceof RealVector) {
            real.setParameters(params);
        }
    }//----------------------------------------------------------------------------------------------------------

    @Override
    public void setSolver(EAsolver s) {
        this.solver = s;
        this.random = s.random;
        if (solver.problem instanceof BinaryString) {
            bits.setSolver(s);
        }
        if (solver.problem instanceof Permutations) {
            perm.setSolver(s);
        }
        if (solver.problem instanceof RealVector) {
            real.setSolver(s);
        }
    }

    @Override
    public String getInformation() {
        if (solver.problem instanceof BinaryString) {
            return bits.getInformation();
        }
        if (solver.problem instanceof Permutations) {
            return perm.getInformation();
        }
        if (solver.problem instanceof RealVector) {
            return real.getInformation();
        }
        return "";
    }

}
