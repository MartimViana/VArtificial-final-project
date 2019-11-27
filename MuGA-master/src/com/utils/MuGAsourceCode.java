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

import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import com.evolutionary.solverUtils.EAsolverArray;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created on 3/fev/2019, 9:01:35
 *
 * @author zulu - computer
 */
public class MuGAsourceCode {

    public static String objectToFile(Object obj) {
        return "src/" + obj.getClass().getCanonicalName().replaceAll("\\.", "/") + ".java";
    }

    public static String importToFile(String name) {
        name = name.trim();
        if (name.endsWith(";")) {
            name = name.substring(0, name.length() - 1);
        }
        return "src/" + name.replaceAll("\\.", "/") + ".java";
    }

    public static String classToSource(String className) throws IOException {
        return new String(
                Files.readAllBytes(Paths.get(className)));
    }

    public static ArrayList<String> getImports(String source) throws IOException  {
        Scanner lines = new Scanner(source);
        ArrayList<String> imports = new ArrayList<>();
        while (lines.hasNext()) {
            String line = lines.nextLine().trim();
            if (line.startsWith("import ")) {
                try {
                    imports.add(importToFile(line.substring(7)));//"import = 7 chras
                } catch (Exception e) {
                    System.err.println("Errer reading source Code " + line);
                }

            } else if (line.startsWith("public class ")) {
                break;
            }
        }
        return imports;
    }

    private static void getSource(Map<String, String> src, ArrayList<String> imports) {
        for (String classImport : imports) {
            if (!src.containsKey(classImport)) {
                try {
                    String source = classToSource(classImport);
                    src.put(classImport, source);
                    getSource(src, getImports(source));
                } catch (Exception ex) {                    
                    System.err.println("Errer reading source Code " + classImport);
                }
            }
        }
    }

    public static String getSourceFromObject(Map<String, String> src, Object obj) {
        return src.get(objectToFile(obj));
    }

    public static Map<String, String> getSourceCode(EAsolver solver) throws IOException {
        HashMap<String, String> code = new HashMap<>();
        if(solver instanceof EAsolverArray){ // template of array solver
            getSourceCode(code, ((EAsolverArray)solver).template);
        }
        getSourceCode(code, solver);
        getSourceCode(code, solver.selection);
        getSourceCode(code, solver.mutation);
        getSourceCode(code, solver.recombination);
        getSourceCode(code, solver.replacement);
        getSourceCode(code, solver.rescaling);
        getSourceCode(code, solver.stop);
        getSourceCode(code, solver.parents);
        getSourceCode(code, solver.problem);
        for (AbstractStatistics stat : solver.report.stats) {
            getSourceCode(code, stat);
        }

        return code;
    }

    public static void getSourceCode(Map<String, String> code, Object obj) throws IOException {
        String file = objectToFile(obj);
        String source = classToSource(objectToFile(obj));
        code.put(file, source);
        getSource(code, getImports(source));
    }

    public static void save(Map<String, String> src, String fileName) throws Exception {
        FileOutputStream fos = new FileOutputStream(fileName);
        ZipOutputStream zipStream = new ZipOutputStream(fos);
        for (String file : src.keySet()) {
            ZipEntry zipEntry = new ZipEntry(file);
            zipStream.putNextEntry(zipEntry);
            zipStream.write(src.get(file).getBytes(StandardCharsets.UTF_8));
            zipStream.closeEntry();
        }

        zipStream.close();
        fos.close();
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> src = getSourceCode(new GA());
        for (String file : src.keySet()) {
            System.out.println(file);
        }
        save(src, "src.zip");
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201902030901L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
