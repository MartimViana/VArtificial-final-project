//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2019   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////

package com.utils;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 18/abr/2019, 9:06:19 
 * @author zulu - computer
 */
public class TestSort {
 
    private static class OBJ implements Comparable<OBJ>{
        int val;
        String elem;

        public OBJ(int val, String elem) {
            this.val = val;
            this.elem = elem;
        }

        @Override
        public int compareTo(OBJ t) {
            return val -t.val;
        }

        @Override
        public String toString() {
            return "\n" + val + "\t" + elem;
        }
        
    }
    public static void main(String[] args) {
        ArrayList<OBJ> l = new ArrayList<>();
        l.add(new OBJ(2, "A"));
        l.add(new OBJ(2, "B"));
        l.add(new OBJ(2, "C"));
        
        l.add(new OBJ(4, "A"));
        l.add(new OBJ(4, "B"));
        l.add(new OBJ(4, "C"));
        
        l.add(new OBJ(3, "A"));
        l.add(new OBJ(3, "B"));
        l.add(new OBJ(3, "C"));
        
        
        l.add(new OBJ(5, "A"));
        l.add(new OBJ(5, "B"));
        l.add(new OBJ(5, "C"));
        
        l.add(new OBJ(1, "A"));
        l.add(new OBJ(1, "B"));
        l.add(new OBJ(1, "C"));
        
        
        Collections.sort(l);
        System.out.println(l);
         String inPath = "http://galleries.daddydiaries3d.com/pics/";
        String outPath = "O:/fotos/";

        for (int i = 44; i < 100; i++) {
            for (int j = 0; j < 20; j++) {
                String imgfile = inPath + String.format("/%03d/%03d.jpg", i, j);
                String diskfile = outPath + String.format("%03d-%03d.jpg", i, j);
                System.out.println(imgfile + " " + diskfile);
                try {
                    URL url = new URL(imgfile);                    
                    Files.copy(url.openStream(), Paths.get(diskfile), StandardCopyOption.REPLACE_EXISTING);                    
                } catch (Exception e) {
                    continue;
                }

            }
        }
    }


    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201904180906L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}