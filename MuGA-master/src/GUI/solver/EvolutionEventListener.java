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

import com.evolutionary.Genetic;
import com.evolutionary.solver.EAsolver;
import java.util.EventListener;

/**
 * Created on 14/mar/2016, 5:08:06
 *
 * @author zulu - computer
 */
public interface EvolutionEventListener extends EventListener {
    void onEvolutionChanges(EAsolver source);
    void onEvolutionComplete(EAsolver source);
}
