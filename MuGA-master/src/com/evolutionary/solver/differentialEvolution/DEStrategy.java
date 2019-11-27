package com.evolutionary.solver.differentialEvolution;

import GUI.utils.MuGASystem;
import java.util.ArrayList;
import java.util.Random;

public abstract class DEStrategy /**
 * *********************************************************
 ** **
 ** Parent class for all the different DE-strategies you ** * can choose. ** *
 * ** * Authors: Mikal Keenan ** * Rainer Storn ** * **
 * *********************************************************
 */
{

    protected Random deRandom;

    protected int i, counter;

    abstract public void apply(double F, double Cr, int dim,
            double[] x, double[] gen_best,
            double[][] g0);

    /**
     * *********************************************************
     ** Contains the actual strategy which alters your vectors.**
     * *********************************************************
     */
    public void init(Random deRnd) /**
     * *********************************************************
     ** Link to the random number generator. **
     * *********************************************************
     */
    {
        deRandom = deRnd;
    }

    protected final void prepare(int dim) /**
     * *********************************************************
     ** Fetch a random number ex [0,dim]. **
     * *********************************************************
     */
    {
        i = deRandom.nextInt(dim);
        counter = 0;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //manso 2017
    public static ArrayList<DEStrategy> getStrategies() {
        ArrayList lst = MuGASystem.getObjects("com.evolutionary.solver.differentialEvolution");
        ArrayList<DEStrategy> list = new ArrayList<>();
        for (Object strat : lst) {
            if (strat instanceof DEStrategy) {
                list.add((DEStrategy) strat);
            }
        }
        return list;
    }

    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }

}
