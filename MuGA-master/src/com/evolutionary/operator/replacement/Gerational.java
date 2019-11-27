/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evolutionary.operator.replacement;

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author manso
 */
public class Gerational extends Replacement {

    protected double ELITISM = 0.0;

    @Override
    public Population execute(Population parents, Population children) {
        int sizeNewPop = parents.getPopulationSize();

        int ELITISM = (int) (Math.ceil(this.ELITISM * sizeNewPop));
        //make new population
        Population selected = children.getCleanClone();

        List<Individual> news = children.getGenomes();
        List<Individual> olders = parents.getGenomes();
        //sort populations 
        Collections.sort(news);
        Collections.reverse(news);
        Collections.sort(olders);
        Collections.reverse(olders);
        //elitism of parents
        for (int i = 0; i < ELITISM; i++) {
            selected.addIndividual(olders.get(i), olders.get(i).getNumberOfCopies());
        }
        //insert new generation
        for (int i = 0; i < news.size() && selected.getPopulationSize() < sizeNewPop; i++) {
            selected.addIndividual(news.get(i), news.get(i).getNumberOfCopies());

        }
        //insert best of old generation
        for (int i = ELITISM; i < olders.size() && selected.getPopulationSize() < sizeNewPop; i++) {
            selected.addIndividual(olders.get(i), olders.get(i).getNumberOfCopies());
        }
        return selected;

    }

    @Override
    public String getInformation() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.toString());
        buf.append("\nSelect best Childrens to next population");
        buf.append("\nIf necessary complete population");
        buf.append("\nwith best parents");
        buf.append("\n\nParameters: <ELITISM>");
        buf.append("\n<ELITISM>percentage of Elitism");

        return buf.toString();
    }
    
       //----------------------------------------------------------------------------------------------------------
    @Override
    public String getParameters() {
        return ELITISM + "";
    }


    @Override
    public void setParameters(String str) {
        StringTokenizer par = new StringTokenizer(str);
        try {
            ELITISM = Double.parseDouble(par.nextToken());
            ELITISM = ELITISM > 1 ? 0.99 : ELITISM < 0 ? 0.01 : ELITISM;
        } catch (Exception e) {
            ELITISM = 0;
        }

    }

    @Override
    public String toString() {
        return super.toString() + " <" + ELITISM + ">";
    }

    @Override
    public Replacement getClone() {
        Gerational clone = (Gerational) super.getClone();
        clone.ELITISM = this.ELITISM;
        return clone;
    }
}
