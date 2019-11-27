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

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zulu
 */
public class MuGASystem {

    public static double ZERO = 1E-10;
    
    public static String mugaPath = "";
    public static String name = "MuGA - Multiset Genetic Algorithm";
    public static String version = "Thesis";
    public static String copyright = "(c)M@nso 2019";
    

    public static String SOLVER = "com.evolutionary.solver";
    public static String STOP_CRITERIA = "com.evolutionary.stopCriteria";

    public static String INDIVIDUALS = "com.evolutionary.problem";

    public static String SELECTION = "com.evolutionary.operator.selection";
    public static String RECOMBINATION = "com.evolutionary.operator.recombination";
    public static String MUTATION = "com.evolutionary.operator.mutation";
    public static String REPLACEMENT = "com.evolutionary.operator.replacement";
    public static String RESCALING = "com.evolutionary.operator.rescaling";

    public static String STATISTICS = "com.evolutionary.report.statistics";
    public static String GRAPHICS = "GUI.DisplayProblem";

    public static ArrayList<String> resources = new ArrayList<>();

    static {
        try {
            //get running source code
            CodeSource src = MuGASystem.class.getProtectionDomain().getCodeSource();
            mugaPath = new File(src.getLocation().getPath()).getParent() + File.separator;
            //running into jar file
            if (src.getLocation().getFile().endsWith(".jar")) {
                loadJarResources(src);
            } else { //running in class file
                getPathClasses(src);
            }
        } catch (Exception ex) {
            Logger.getLogger(MuGASystem.class.getName()).log(Level.SEVERE, null, ex);
            // JOptionPane.showMessageDialog(null, "Loading MuGA RESOURCES ERROR:" + ex.getMessage());
        }
    }

    public static ArrayList<MuGAObject> getGenetic(String path) {
        String pack = path.replaceAll("\\.", "/"); // replace . by /
        ArrayList<MuGAObject> classes = new ArrayList<>();
        for (String str : resources) {
            if (str.startsWith(pack)) {
                try {
                    str = str.replaceAll("/", ".");
                    str = str.substring(0, str.length() - ".class".length());
                    MuGAObject mObj = new MuGAObject(str);
                    if (mObj.getObject() != null) {
                        classes.add(new MuGAObject(str));
                    }
                } catch (Throwable ex) { // catch exceptions and errors
                    //conversion to MuGAObject not possible
                }
            }
        }
        return classes;
    }

    public static ArrayList getObjects(String path) {
        String pack = path.replaceAll("\\.", "/"); // replace . by /
        ArrayList classes = new ArrayList<>();
        for (String str : resources) {
            if (str.startsWith(pack)) {
                try {
                    str = str.replaceAll("/", ".");
                    str = str.substring(0, str.length() - ".class".length());
                    Object mObj = Class.forName(str).newInstance();
                    if (mObj != null) {
                        classes.add(mObj);
                    }
                } catch (Exception ex) {
                }
            }
        }
        return classes;
    }

    /**
     * load classes from jar file
     *
     * @param src source jar file
     * @throws Exception
     */
    private static void loadJarResources(CodeSource src) throws Exception {
        File f = new File(src.getLocation().getFile());
        if (f.exists()) {
            try {
                JarInputStream jarFile = new JarInputStream(
                        new FileInputStream(src.getLocation().getFile()));
                JarEntry jarEntry;
                //process all files in Jar file
                while (true) {
                    jarEntry = jarFile.getNextJarEntry();
                    //end of jar file
                    if (jarEntry == null) {
                        break;
                    }
                    if (!jarEntry.isDirectory()) {                        
                        resources.add(jarEntry.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * load classes from fileSystem
     *
     * @param src executable class
     * @throws Exception
     */
    private static void getPathClasses(CodeSource src) throws Exception {
        getPathClasses(new File(src.getLocation().getPath()), new File(src.getLocation().getFile()));
        int size = src.getLocation().getPath().length() - 1;
        for (int i = 0; i < resources.size(); i++) {
            String file = resources.get(i).substring(size);
            file = file.replaceAll("\\\\", "/");
            //-------------------------------------------------------------------
            if (file.startsWith("/")) { // macOSX put / in the begining of the file path
                file = file.substring(1);
            }
            resources.set(i, file);
        }

    }

    /**
     * load classes from fileSystem
     *
     * @param path path of file system
     * @param root root file
     * @throws Exception
     */
    private static void getPathClasses(File path, File root) throws Exception {
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                resources.add(file.getAbsolutePath());
            } else {
                getPathClasses(path, file);
            }
        }
    }


    /* creates a raw clone of a genetic object using default constructor a
     *
     * @param original object to clone
     * @return raw clone of original
     */
    public static Object getClone(Object original)  {
        try {
            //copy constructor
            Constructor c = Class.forName(original.getClass().getCanonicalName()).getConstructor(original.getClass());
            return c.newInstance(original);
        } catch (Exception e) {
            try {
                //default construtor
                return Class.forName(original.getClass().getCanonicalName()).getConstructor().newInstance();
            } catch (Exception ex) {
                //null
                return null;
            }
        }
    }
}
