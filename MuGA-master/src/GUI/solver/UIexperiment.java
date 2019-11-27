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

package GUI.solver;

import com.evolutionary.solver.EAsolver;
import java.util.ArrayList;

/**
 * Created on 23/ago/2016, 18:00:26
 *
 * @author zulu - computer
 */
public class UIexperiment {

    ArrayList<UiSolver> uiArray;

    public void initExperiment(ArrayList<EAsolver> solvers) {
        stopSolvers();
        uiArray = new ArrayList<>();
        for (EAsolver solver : solvers) {
            uiArray.add(new UiSolver(solver));
        }
    }

    public void stopSolvers() {
        if (uiArray != null) {
            for (UiSolver uiSolver : uiArray) {
                if (uiSolver.autorun != null) {
                    uiSolver.stop();
                  //  uiSolver.mySolver.save();
                }
            }
        }
    }
  

    public void startSolvers() {
        if (uiArray != null) {
            for (UiSolver uiSolver : uiArray) {
                uiSolver.start();
            }
        }
    }

    public void addListener(int index, EvolutionEventListener listener) {
        uiArray.get(index).addListener(listener);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201608231800L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
