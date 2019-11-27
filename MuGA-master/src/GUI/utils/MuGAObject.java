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

package GUI.utils;

import com.evolutionary.Genetic;

/**
 *
 * @author Zulu
 */
public class MuGAObject {

    public Genetic obj; //object of class

    public MuGAObject(String name) throws ClassNotFoundException {
        try {
            this.obj = (Genetic)Class.forName(name).newInstance();
        } catch (Exception ex) {
            this.obj = null;
        }
    }

    @Override
    public String toString() {
        return obj.getClass().getSimpleName();
    }

    public Genetic getObject() {
        return obj;
    } 
    public String getClassName() {
        return obj.getClass().getCanonicalName();
    } 
     public String getName() {
        return obj.getClass().getSimpleName();
    } 

}
