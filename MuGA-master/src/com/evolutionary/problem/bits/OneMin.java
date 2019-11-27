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

package com.evolutionary.problem.bits;

/**
 * Created on 29/set/2015, 9:30:30
 *
 * @author zulu - computer
 */
public class OneMin extends BinaryString {

    public OneMin() {
        super(Optimization.MINIMIZE);
    }
    
     public OneMin(String genes) {
        super(genes.length(), Optimization.MINIMIZE);
        setBits(genes);
    }

    @Override
    public boolean isOptimum() {
        return evaluate(getBitsGenome()) == 0;
    }

    @Override
    protected double evaluate(boolean[] genome) {
       return OneMax.ones(genome);
    }

    @Override
    public String getInformation() {
        // public String getInfo() {
        StringBuilder txt = new StringBuilder(super.getInformation());
        txt.append("\n");
        txt.append("\nParameters <SIZE>");
        txt.append("\n           <SIZE>  size of genome");
        txt.append("\n\nCount the number of Zeros in the genome");
        return txt.toString();

    }
    
   

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201509290930L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
