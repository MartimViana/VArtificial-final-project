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

package com.evolutionary.report.statistics;

import com.evolutionary.Genetic;
import com.evolutionary.solver.EAsolver;
import com.utils.MyNumber;

/**
 * Created on 19/jan/2016, 12:07:58
 *
 * @author zulu - computer
 */
public abstract class AbstractStatistics extends Genetic implements Comparable<AbstractStatistics> {

    String title;
    public boolean higherIsBetter = true;
    public boolean logScaleEnabled = false;

    public AbstractStatistics(String title) {
        this.title = title;
    }
    
    public void setSolver(EAsolver s){
        //to update higherIsBetter in statistics that depends  of optimization type (maximize or minimize)
    }

    /**
     * calculate the statistic
     *
     * @param s solver
     * @return value
     */
    public abstract double execute(EAsolver s);

    /**
     * getTitle of the statistic
     *
     * @return getTitle
     */
    public String getTitle() {
        return title;
    }

    /**
     * textual representation of statistic
     *
     * @param s solver
     * @return textual value
     */
    public String getText(EAsolver s) {
        return MyNumber.numberToString(execute(s), title.length());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    public AbstractStatistics getClone(){
        return (AbstractStatistics) Genetic.getClone(this);
    }
    
    @Override
    public boolean equals(Object other){
        return other.getClass().equals(this.getClass());
        
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201601191207L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int compareTo(AbstractStatistics t) {
        return toString().compareTo(t.toString());
    }
}
