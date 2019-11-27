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

package com.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 26/mar/2016, 15:12:06
 *
 * @author zulu - computer
 */
public class Permutation {

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::: P E R M U T A I O N S ::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//    List<List> allPerm = new ArrayList();
//    List<List> elems;
//
//    public Permutation(List<List> list) {
//        this.elems = list;
//        build(new ArrayList(), 0);
//    }
    static List<List> all = new ArrayList();
    static List<List> data;
     
    public static synchronized List<List> execute(List<List> list){
         all = new ArrayList();
         data = list;
         doPermut(new ArrayList(), 0);
         return all;
     }
     
      private static void doPermut(List current, int index) {
        if (index == data.size()) {
            all.add(current);
        } else {
            for (Object object : data.get(index)) {
                List copy = new ArrayList(current); // clone actual list
                copy.add(object);
                doPermut(copy, index + 1);
            }
        }
    }
    
//
//    private void build(List current, int index) {
//        if (index == elems.size()) {
//            allPerm.add(current);
//        } else {
//            for (Object object : elems.get(index)) {
//                List copy = new ArrayList(current); // clone actual list
//                copy.add(object);
//                build(copy, index + 1);
//            }
//        }
//    }

    public static void main(String[] args) {
        //        loadSimualtion("experiment.txt");
        String[][] elems = {
            {"1", "2", "3"},
            {"a", "b"},
            {"#", "@"}
        };
        ArrayList<List> el = new ArrayList<>();
        for (String[] x : elems) {
            el.add(Arrays.asList(x));
        }
        for (List list : el) {
            System.out.println("ELEM " + list);
        }

        List<List> x = Permutation.execute(el);
        for (List list :x) {
            System.out.println("LIST " + list);
        }
                
    }

}
