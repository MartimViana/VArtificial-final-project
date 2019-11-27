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

package com.utils.copyright;

import com.utils.MyFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 18/fev/2019, 20:33:38 
 * @author zulu - computer
 */
public class ReplaceSourceCodeHeader {
   
    public static void replaceHeader(File f,  String copyright){
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for (File file : files) {
                replaceHeader(file,copyright);
            }
        }else if( f.getName().endsWith(".java")){
            try {
                String code = MyFile.readFile(f.getAbsolutePath());
                String source = code.substring(code.indexOf("package"));
                Files.write(Paths.get(f.toURI()), (copyright +"\n" + source).getBytes());
            } catch (IOException ex) {
                Logger.getLogger(ReplaceSourceCodeHeader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    public static void main(String[] args) {
        
        String header = MyFile.readFile("src/com/utils/copyright/copyright.txt");
        replaceHeader(new File("src/"), header);
        
    }


    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201902182033L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}