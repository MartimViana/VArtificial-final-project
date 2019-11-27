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

package com.evolutionary;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Random;

/**
 *
 * @author zulu
 */
public class Genetic implements Serializable {

    public static Random defaultRandom = new Random();
    public Random random = defaultRandom; // random generator

    public String getInformation() {
        return "Not implemented";
    }

    /**
     * update parameters definition
     *
     * @param params individual parameters
     */
    public void setParameters(String params) throws RuntimeException {
        //do nothing
        // overwrite in the subclasses if needed
    }

    /**
     *
     * @param params individual parameters
     */
    public String getParameters() {
        //do nothing
        return "";
    }

    public String getName() {
        return getFullName(this);
    }

    public String getSimpleName() {
        return getClass().getSimpleName();
    }

    public static String getFullName(Genetic genetic) {
        StringBuilder txt = new StringBuilder(genetic.getClass().getCanonicalName());
        String[] params = genetic.getParameters().split("\\s+");//split string by with chars - spaces tab \n
        txt.append("( ");
        for (int i = 0; i < params.length; i++) {
            txt.append(params[i]);
            if (i < params.length - 1) {
                txt.append(" , ");
            }
        }
        txt.append(" )");
        return txt.toString().trim();
    }

    public static String getCompactName(Genetic genetic) {
        StringBuilder txt = new StringBuilder(genetic.getClass().getSimpleName());
        String[] params = genetic.getParameters().split("\\s+");//split string by with chars - spaces tab \n
        txt.append("(");
        for (int i = 0; i < params.length; i++) {
            txt.append(params[i]);
            if (i < params.length - 1) {
                txt.append("_");
            }
        }
        txt.append(")");
        return txt.toString().trim();
    }

    /**
     * creates a new Genetic object using a string comand <classname parameters>
     * using default constructor and setParameters
     *
     * @param def classname parametrs
     * @return genetic object or null if ERROR
     */
    public static Genetic createNew(String def) throws RuntimeException {
        try {
            String name, params = "";
            if (def.indexOf(' ') > 0) { // name + parameters
                name = def.substring(0, def.indexOf(' ')).trim();
                params = def.substring(def.indexOf(' ')).trim();
            } else { // only the name
                name = def.trim();
            }
            Genetic obj = (Genetic) Class.forName(name).newInstance();
            obj.setParameters(params);
            return obj;
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage());
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * creates a raw clone of a genetic object using default constructor and
     * setParameters
     *
     * @param original object to clone
     * @return raw clone of original
     */
    public static Genetic getClone(Genetic original) throws RuntimeException {
        try {
            //try to use copy contructor
            Constructor c = Class.forName(original.getClass().getCanonicalName()).getConstructor(original.getClass());
            Genetic obj = (Genetic) c.newInstance(original);
            obj.random = original.random;
            return obj;
        } catch (Exception e) {
            //build using default constructor and set method
            Genetic obj = createNew(original.getClass().getCanonicalName()
                    + " "
                    + original.getParameters());
            obj.random = original.random;
            return obj;
        }
    }

    public String getNameWithParameters() {//------------------------------------------ get information
        StringBuilder txt = new StringBuilder();
        String p = getParameters().trim();
        p = p.replaceAll("  "," ");
        p = p.replaceAll(" ", " , ");
        txt.append(getClass().getSimpleName() + "(" + p + ") : ");
        return txt.toString();
    }

    public String getCanonicalFullDefinition() {
        return this.getClass().getCanonicalName()
                + " "
                + this.getParameters();
    }

    public Genetic getClone() {
        return getClone(this);
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setRandomGenerator(Random rnd) {
        this.random = rnd;
    }

    public Random getRandomGenerator() {
        return random;
    }

    public double uniformRandom(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static String VERSION = "MuGA (c) 2019 - ver 5.07";   
    
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201510011056L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2015  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////


}
