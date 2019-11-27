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

package com.evolutionary.solverUtils;

import com.evolutionary.Genetic;
import com.evolutionary.operator.mutation.Mutation;
import com.evolutionary.operator.recombination.Recombination;
import com.evolutionary.operator.replacement.Replacement;
import com.evolutionary.operator.rescaling.Rescaling;
import com.evolutionary.operator.selection.Selection;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.solver.EAsolver;
import com.utils.MyFile;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created on 1/set/2016, 19:27:13
 *
 * @author zulu - computer
 */
public class EAExperimentRanks {

    public static ArrayList<EAExperiment> splip(EAExperiment simulation, Genetic element) {
        ArrayList<EAExperiment> sims = new ArrayList<>();
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //::::::::: get the diferrents types of genetic elements :::::::::::::::
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        TreeSet<String> typeElements = new TreeSet<>(); // unique
        for (EAsolver s : simulation.solvers) {
            if (element instanceof Individual) {
                typeElements.add(Genetic.getCompactName(s.problem));
            } else if (element instanceof Selection) {
                typeElements.add(Genetic.getCompactName(s.selection));
            } else if (element instanceof Population) {
                typeElements.add(Genetic.getCompactName(s.parents));
            } else if (element instanceof Mutation) {
                typeElements.add(Genetic.getCompactName(s.mutation));
            } else if (element instanceof Recombination) {
                typeElements.add(Genetic.getCompactName(s.recombination));
            } else if (element instanceof Replacement) {
                typeElements.add(Genetic.getCompactName(s.replacement));
            } else if (element instanceof Rescaling) {
                typeElements.add(Genetic.getCompactName(s.rescaling));
            }
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        if (typeElements.size() > 1) { // more than one  element
            //:::::::::::::::::::::: build experiments with the elements ::::::::
            for (String elemName : typeElements) { // iterate Elements
                //new Experiment
                EAExperiment exp = new EAExperiment();
                //select solvers
                for (EAsolver s : simulation.solvers) {
                    if (element instanceof Individual) {
                        if (elemName.equalsIgnoreCase(Genetic.getCompactName(s.problem))) {
                            exp.solvers.add(s);
                        }
                    } else if (element instanceof Population) {
                        if (elemName.equalsIgnoreCase(Genetic.getCompactName(s.parents))) {
                            exp.solvers.add(s);
                        }
                    } else if (element instanceof Selection) {
                        if (elemName.equalsIgnoreCase(Genetic.getCompactName(s.selection))) {
                            exp.solvers.add(s);
                        }
                    } else if (element instanceof Mutation) {
                        if (elemName.equalsIgnoreCase(Genetic.getCompactName(s.mutation))) {
                            exp.solvers.add(s);
                        }
                    } else if (element instanceof Recombination) {
                        if (elemName.equalsIgnoreCase(Genetic.getCompactName(s.recombination))) {
                            exp.solvers.add(s);
                        }
                    } else if (element instanceof Replacement) {
                        if (elemName.equalsIgnoreCase(Genetic.getCompactName(s.replacement))) {
                            exp.solvers.add(s);
                        }
                    } else if (element instanceof Rescaling) {
                        if (elemName.equalsIgnoreCase(Genetic.getCompactName(s.rescaling))) {
                            exp.solvers.add(s);
                        }
                    }
                }//select solvers
                String fileName = MyFile.getPath(simulation.fileName) + "report" + File.separatorChar + elemName;
                exp.setFileName(fileName);
                // save experiment                
                exp.saveRanksReport();
                sims.add(exp);
            }//element name

        }// more than one  element
        else { //only one element
            sims.add(simulation);
        }
        return sims;
    }

    public static void main(String[] args) {
        EAExperiment ex = new EAExperiment();
        ex.loadSolver("simulation/sim1.txt");
        ex.solve(false);
        ArrayList<EAExperiment> e = EAExperimentRanks.splip(ex, ex.solvers.get(0).problem);
        for (EAExperiment eAExperiment : e) {
            System.out.println(" " + eAExperiment.fileName);
        }

    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201609011927L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
