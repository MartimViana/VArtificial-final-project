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

package com.evolutionary.report.html;

import com.evolutionary.solverUtils.EAExperiment;
import com.utils.MyFile;

/**
 * Created on 25/jan/2019, 14:53:16
 *
 * @author zulu - computer
 */
public class HTMLExperiment {

    static String menuBarTemplate = MyFile.readResource("/com/evolutionary/report/html/horizontalMenu.html");
    static String indexTemplate = MyFile.readResource("/com/evolutionary/report/html/horizontalIndex.html");

    public static String saveSolversInfo(EAExperiment experiment) throws Exception {
        StringBuilder menuItem = new StringBuilder();
//        for (EAsolver solver : experiment.solvers) {
//            HTMLSolverArray.saveHtml(solver.report);
//            menuItem.append("\n<li  style=\"float:right\" ><a href=\""
//                    + solver.getSolverName() + File.separatorChar
//                    + "index.html\" target=\"experiment\">"
//                    + solver.getSolverName() + "</a></li>");
//
//        }

        return menuItem.toString();

    }

    public static void saveHtml(EAExperiment experiment) throws Exception {

        String path = MyFile.getPath(experiment.getFileName());
//        File file = new File(path);
//        file.mkdirs();
//
//        MyFile.saveToFile(HTMLSolver.css, path + "style.css");
//        MyFile.saveToFile(indexTemplate, path + "index.html");
//        String template = HTMLSolver.pageTemplate.replace("_COPYRIGHT_ ", getCopyright());
//
//        MyFile.saveToFile(template.replace("_TITLE_", "Experiment"), path + "home.html");
//        MyFile.saveToFile(template.replace("_TITLE_", "Solver Experiment"), path + "solvers.html");
//
//      //  saveSolversInfo(experiment);
//
//        StringBuilder solverMenu = new StringBuilder();
//        solverMenu.append("\n<li><a href=\"home.html\" target=\"experiment\">Home</a></li>");
//        solverMenu.append("\n<li><a href=\"solvers.html\" target=\"experiment\">Solvers</a></li>");
//
//        solverMenu.append(saveSolversInfo(experiment));
//
//        String menu = menuBarTemplate.replace("<h1>_SOLVER.NAME_</h1>", "");
//        menu = menu.replace("_MENU_", solverMenu.toString());
//        MyFile.saveToFile(menu, path + "menu.html");

    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201901251453L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws Exception {
        EAExperiment experiment = new EAExperiment(); 
//        EAsolver m = ExploreSolver.defaultSolver;
//        m.parents = new MultiPopulation();
//        //m.setSolverName("muGA");
//        EAsolver s = new EAsolverArray(m);
//        s.setSolverName("MuGA");        
//        experiment.solvers.add(s);
//        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//        m.parents = new SimplePopulation();
//        //m.setSolverName("muGA");
//        s = new EAsolverArray(m);
//        s.setSolverName("SGA");
//        experiment.solvers.add(s);
//        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//        experiment.setFileName("experiment/experience01.txt");
//        experiment.solve(true);
//        //experiment.saveFinalReport();
//        experiment.saveObjectExperiment("experiment.obj");
        experiment.loadObjectExperiment("experiment.obj");
        //   experiment.saveAsCSV("experiment/experience01.csv");

        HTMLExperiment.saveHtml(experiment);

    }

}
