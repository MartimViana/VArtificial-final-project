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

import GUI.DisplayProblem.DisplayPopulation;
import GUI.UIpopulation;
import GUI.statistics.StatisticsChartSimulation;
import GUI.statistics.StatisticsChartSolver;
import GUI.utils.MuGASystem;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.report.ReportSolver;
import static com.evolutionary.report.ReportSolver.FIELD_SIZE;
import com.evolutionary.report.ReportSolverArray;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solverUtils.EAExperiment;
import com.evolutionary.solverUtils.EAsolverArray;
import com.evolutionary.solverUtils.FileSolver;
import static com.evolutionary.solverUtils.FileSolver.NUMBER_OF_RUNS;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_MUTATION;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_MUTATION_LIB;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_RECOMBINATION;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_RECOMBINATION_LIB;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_REPLACEMENT;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_REPLACEMENT_LIB;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_RESCALING;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_RESCALING_LIB;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_SELECTION;
import static com.evolutionary.solverUtils.FileSolver.OPERATOR_SELECTION_LIB;
import static com.evolutionary.solverUtils.FileSolver.POPULATION_TYPE;
import static com.evolutionary.solverUtils.FileSolver.POPULATION_TYPE_LIB;
import static com.evolutionary.solverUtils.FileSolver.PROBLEM_NAME;
import static com.evolutionary.solverUtils.FileSolver.PROBLEM_NAME_LIB;
import static com.evolutionary.solverUtils.FileSolver.RANDOM_SEED;
import static com.evolutionary.solverUtils.FileSolver.SOLVER_NAME;
import static com.evolutionary.solverUtils.FileSolver.SOLVER_TYPE;
import static com.evolutionary.solverUtils.FileSolver.STATISTIC;
import static com.evolutionary.solverUtils.FileSolver.STATISTIC_LIB;
import static com.evolutionary.solverUtils.FileSolver.STOP_CRITERIA;
import static com.evolutionary.solverUtils.FileSolver.STOP_CRITERIA_LIB;
import static com.evolutionary.solverUtils.FileSolver.getSimpleGenetic;
import static com.evolutionary.solverUtils.FileSolver.getSolverType;
import com.utils.MuGAsourceCode;
import com.utils.MyFile;
import com.utils.MyString;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;

public class WWWSolverSaveReport {

    public static void main(String[] args) throws Exception {
        java.awt.Desktop.getDesktop().browse(new URI("A:\\PHD\\MuGA_Thesys02_04\\evolutions\\index.html"));
//        EAsolver s = new GA();
//        s.solve(false);
//        new WWWSolverSaveReport(s.report).save("www/solver/teste.muga", null);
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static void save(ReportSolver report, String fileName) throws Exception {
        new WWWSolverSaveReport(report).save(fileName, null);
    }

    public static void save(ReportSolver report, String fileName, Component window) {
        try {
            if (report.solver instanceof EAsolverArray) {
                WWWSolverSaveReportArray.save(report, fileName, window);
            } else {
                new WWWSolverSaveReport(report).save(fileName, window);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static String getIndex(ReportSolver report) {
//        return "index.html";
//    }
//
//    public static String getStatistics(ReportSolver report) {        
//        return  report.solver.getSolverName() + ".html";
//    }
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::  
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    static String saveEvolutionStatistics(ReportSolver report, String htmlFileName, String template, String frame) throws Exception {
        Locale local = Locale.getDefault();

        String solverName = report.solver.getSolverName();
        StringBuilder statsHtml = new StringBuilder();
        StringBuilder statsCSV = new StringBuilder();

        statsHtml.append("<table>");
        statsHtml.append("<tr><th>Statistic</th>");
        statsCSV.append("Statistic");

        //TITLE OF STATS
        statsHtml.append(MyString.setSize("", FIELD_SIZE));
        for (int i = 0; i < report.stats.size(); i++) {
            String statName = report.stats.get(i).toString();
            //statsHtml.append("<th> <a href=\"" + solverName + "_" + statName + ".html\">" + statName + "</a></th>");
            statsHtml.append("<th> " + statName + " </th>");
            statsCSV.append(HTMLutils.CSV_COMMA + statName);
        }
        statsHtml.append("</tr>");

        //Stats data 
        for (int i = report.evolution.size() - 1; i >= 0; i--) {
            statsHtml.append("\n<tr align=\"center\">");
            statsCSV.append("\n");
            Double[] v = report.evolution.get(i);
            statsHtml.append("<td>" + i + "</td>");
            statsCSV.append(i + HTMLutils.CSV_COMMA);
            for (int j = 0; j < v.length; j++) {
                statsHtml.append("<td><pre>" + HTMLutils.getNumber(v[j], Locale.ENGLISH) + "</pre></td>");
                statsCSV.append(String.format(local, "%f", v[j]) + HTMLutils.CSV_COMMA);
            }
            statsHtml.append("\n</tr>");
        }
        statsHtml.append("</table>");

        File path = new File(report.path);

        String stats = template.replace("_TITLE_", HTMLutils.getPageTitle("Evolution of " + solverName + " statistics",
                solverName + ".csv", solverName + ".muga"));

        stats = stats.replace("_DATA_", statsHtml.toString());

        MyFile.saveToFile(stats, path.getAbsolutePath() + "/" + htmlFileName);
        MyFile.saveToFile(statsCSV.toString(), path.getAbsolutePath() + "/" + solverName + ".csv");
        MyFile.saveToFile(report.solver.toString(), path.getAbsolutePath() + "/" + solverName + ".txt");
        report.saveObject(path.getAbsolutePath() + "/" + solverName + "." + FileSolver.FILE_EXTENSION_MUGA);
        return "<tr><td><a href=\"" + htmlFileName + "\" target=\"" + frame + "\">Statistics</a></td></tr>";
    }

    static String getPopulationEvolution(ReportSolver report, String template, String frameDestination) throws Exception {
        String solverName = report.solver.getSolverName();
        StringBuilder verticalMenu = new StringBuilder();
        StringBuilder horizontalMenu = new StringBuilder();
        File path = new File(report.path);

        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = image.createGraphics();

        StatisticsChartSolver statsPanel;
        if (report instanceof ReportSolverArray) {
            statsPanel = new StatisticsChartSimulation(report.solver);
        } else {
            statsPanel = new StatisticsChartSolver(report.solver);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        panel.add(statsPanel.getTabs(), BorderLayout.CENTER);

        int totalTabs = statsPanel.getTabs().getTabCount();
        horizontalMenu.append("\n<ul>");
        for (int i = 0; i < totalTabs; i++) {
            String statTitle = statsPanel.getTabs().getTitleAt(i);
            verticalMenu.append("\n<tr><td><a href=\"" + solverName + "_" + statTitle + ".html\" target=\"" + frameDestination + "\">" + statTitle + "</a></td></tr>");
            horizontalMenu.append("\n<li><a href=\"" + solverName + "_" + statTitle + ".html\" target=\"" + frameDestination + "\">" + statTitle + "</a></li>");
        }
        horizontalMenu.append("\n</ul>");

        for (int i = 0; i < totalTabs; i++) {
            String statTitle = statsPanel.getTabs().getTitleAt(i);
            ChartPanel gr = (ChartPanel) statsPanel.getTabs().getComponentAt(i);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                    path.getAbsolutePath() + "/" + solverName + "_" + statTitle + ".jpg"));
            ChartUtilities.writeChartAsJPEG(out, gr.getChart(), IMG_WIDTH, IMG_HEIGHT);

            String htmlStat = template.replace("_TITLE_", HTMLutils.getPageTitle(report.stats.get(i).getTitle(), ""));
            htmlStat = htmlStat.replace("_DATA_", "<img src=\"" + solverName + "_" + statTitle + ".jpg\">"
                    + "\n<br>" + report.stats.get(i).getInformation().replaceAll("\n", "<br>"));
            MyFile.saveToFile(htmlStat, path.getAbsolutePath() + "/" + solverName + "_" + statTitle + ".html");

        }

        return verticalMenu.toString();
    }

    static String savePopulation(ReportSolver report, Population pop, String menuItemName, String template, String target) throws Exception {
        return savePopulation(report.path, report.solver.getSolverName(), pop, menuItemName, template, target);
    }
    static String savePopulation(String reportPath,String solverName, Population pop, String menuItemName, String template, String target) throws Exception {
        File path = new File(reportPath);

        StringBuilder solverMenu = new StringBuilder();
        StringBuilder popMenu = new StringBuilder();
        popMenu.append("\n<ul>");
        popMenu.append("\n<li><a href=\"" + solverName + "_" + menuItemName + "_txt_genotype.html\" > Genotype </a></li>\n");
        popMenu.append("\n<li><a href=\"" + solverName + "_" + menuItemName + "_txt_phenotype.html\" > Phenotype </a></li>\n");
        //::::::::::::::::::::::: GRAPHICS :::::::::::::::::::::::::::::::::::::
        int width = 1000;
        int height = 800;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = image.createGraphics();
        ArrayList display = MuGASystem.getObjects(MuGASystem.GRAPHICS);
        Individual ind = pop.getTemplate();
        for (Object object : display) {
            try {// try build display
                if (((DisplayPopulation) object).isValid(ind)) {
                    DisplayPopulation dp = (DisplayPopulation) object;
                    String nameDisplay = solverName + "_" + menuItemName + "_gr_" + dp.getClass().getSimpleName();
                    dp.showPopulation(pop, g2d, new Rectangle(0, 0, width, height));
                    ImageIO.write(image, "jpg", new File(path.getAbsolutePath() + "/" + nameDisplay + ".jpg"));
                    popMenu.append("\n<li><a href=\"" + nameDisplay + ".html\" > " + dp.toString() + "</a></li>");
                }
            } catch (Exception e) {
                //display not avaiable
            }
        }
        popMenu.append("\n</ul>");

        String fileName = solverName + "." + FileSolver.FILE_EXTENSION_MUGA;

        for (Object object : display) {
            try {// try build display
                if (((DisplayPopulation) object).isValid(ind)) {
                    DisplayPopulation dp = (DisplayPopulation) object;
                    String nameDisplay = solverName + "_" + menuItemName + "_gr_" + dp.getClass().getSimpleName();
                    String htmlGenotype = template.replace("_TITLE_", HTMLutils.getPageTitle(solverName + " " + dp.toString(), fileName)
                            + "\n" + popMenu.toString());
                    htmlGenotype = htmlGenotype.replace("_DATA_", "\n<img src=\"" + nameDisplay + ".jpg\">");
                    MyFile.saveToFile(htmlGenotype, path.getAbsolutePath() + "/" + nameDisplay + ".html");
                }
            } catch (Exception e) {
                //display not avaiable
            }
        }
        //:::::::::::::::::::::::::::: POPULATION  :::::::::::::::::::::::::::
        String population = template.replace("_DATA_", "\n<pre>"
                + UIpopulation.toPopGenotypeString(pop) + "</pre><hr><pre>"
                + ind.getInformation() + "</pre>");
        population = population.replace("_TITLE_", HTMLutils.getPageTitle(solverName + " Genotype", fileName) + "\n" + popMenu.toString());
        MyFile.saveToFile(population, path.getAbsolutePath() + "/" + solverName + "_" + menuItemName + "_txt_genotype.html");

        population = template.replace("_DATA_", "\n<pre>"
                + UIpopulation.toPhenotypeString(pop) + "</pre><hr><pre>"
                + ind.getInformation() + "</pre>");
        population = population.replace("_TITLE_", HTMLutils.getPageTitle(solverName + " Phenotype", fileName) + "\n" + popMenu.toString());
        MyFile.saveToFile(population, path.getAbsolutePath() + "/" + solverName + "_" + menuItemName + "_txt_phenotype.html");

        solverMenu.append("\n<tr><td><a href=\"" + solverName + "_" + menuItemName + "_txt_genotype.html\" target=\"" + target + "\">"
                + menuItemName + "</a></td></tr>");

        return solverMenu.toString();

    }

    /**
     *
     * @param code source code
     * @param elem element to save
     * @param template html template
     * @param path path to save
     * @param text text of link
     * @return
     */
    private static String getSourceCodeLink(Map<String, String> code, Object elem, String template, String path, String text) {
        String java = MuGAsourceCode.getSourceFromObject(code, elem);
        if (java == null) {
            return text;
        }
        String file = elem.getClass().getSimpleName() + ".java";
        template = template.replaceAll("_FILE_", file);
        template = template.replace("_JAVA_", java);
        template = template.replace("_COPYRIGHT_", HTMLutils.getCopyright());
        String htmlFile = "src_" + elem.getClass().getSimpleName() + ".html";
        MyFile.saveToFile(template, path + htmlFile);
        return "<li><a  href=\"" + htmlFile + "\">" + text + "</a></li>";

    }

    static String saveConfig(ReportSolver report, String template, String target) throws Exception {
        EAsolver solver = report.solver;
        String path = MyFile.getPath(report.path);
        String solverName = report.solver.getSolverName();

        Map<String, String> code = new HashMap<>();
        String zipName = solverName + "_src.zip";
        try {
            code = MuGAsourceCode.getSourceCode(report.solver);
            MuGAsourceCode.save(code, path + "/" + solverName + "_src.zip");
        } catch (Exception e) {
            zipName = ""; //no zipname
        }

        String txtConfig[] = report.solver.toString().split("\n");
        StringBuilder data = new StringBuilder();
        data.append("\n<ul> <table>\n <tr><th width=\"1px\"> Key</th><th> value</td></th>");

//        data.append("\n<tr><td> Source code </td><td>"
//                + "<li> <a   href=\"" + zipName + "\">"
//                + zipName + "</a></li></td></tr>");
        data.append("\n<tr><td>" + SOLVER_NAME + "  </td><td><pre> " + solver.getSolverName() + "</pre></a></td></tr>");
        data.append("\n<tr><td>" + RANDOM_SEED + "  </td><td><pre> " + solver.randomSeed + "</pre></a></td></tr>");
        data.append("\n<tr><td>" + NUMBER_OF_RUNS + "  </td><td><pre> " + solver.numberOfRun + "</pre></a></td></tr>");

        EAsolver EAtemplate = solver instanceof EAsolverArray ? ((EAsolverArray) solver).template : solver;
        data.append("\n<tr><td>" + SOLVER_TYPE + "  </td><td> "
                + getSourceCodeLink(code, EAtemplate, HTMLutils.sourceCode, path, getSolverType(solver))
                + "</td></tr>");

        data.append("\n<tr><td>" + STOP_CRITERIA + "  </td><td> "
                + getSourceCodeLink(code, solver.stop, HTMLutils.sourceCode, path, getSimpleGenetic(STOP_CRITERIA_LIB, solver.stop))
                + "</a></td></tr>");

        data.append("\n<tr><td>" + PROBLEM_NAME + "  </td><td> "
                + getSourceCodeLink(code, solver.problem, HTMLutils.sourceCode, path, getSimpleGenetic(PROBLEM_NAME_LIB, solver.problem))
                + "</td></tr>");

        data.append("\n<tr><td>" + POPULATION_TYPE + "  </td><td>"
                + getSourceCodeLink(code, solver.parents, HTMLutils.sourceCode, path, getSimpleGenetic(POPULATION_TYPE_LIB, solver.parents))
                + "</td></tr>");

        data.append("\n<tr><td>" + OPERATOR_SELECTION + "  </td><td> "
                + getSourceCodeLink(code, solver.selection, HTMLutils.sourceCode, path, getSimpleGenetic(OPERATOR_SELECTION_LIB, solver.selection))
                + "</td></tr>");

        data.append("\n<tr><td>" + OPERATOR_RECOMBINATION + "  </td><td> "
                + getSourceCodeLink(code, solver.recombination, HTMLutils.sourceCode, path, getSimpleGenetic(OPERATOR_RECOMBINATION_LIB, solver.recombination))
                + "</td></tr>");

        data.append("\n<tr><td>" + OPERATOR_MUTATION + "  </td><td>"
                + getSourceCodeLink(code, solver.mutation, HTMLutils.sourceCode, path, getSimpleGenetic(OPERATOR_MUTATION_LIB, solver.mutation))
                + "</td></tr>");

        data.append("\n<tr><td>" + OPERATOR_REPLACEMENT + "  </td><td>"
                + getSourceCodeLink(code, solver.replacement, HTMLutils.sourceCode, path, getSimpleGenetic(OPERATOR_REPLACEMENT_LIB, solver.replacement))
                + "</td></tr>");

        data.append("\n<tr><td>" + OPERATOR_RESCALING + "  </td><td>"
                + getSourceCodeLink(code, solver.rescaling, HTMLutils.sourceCode, path, getSimpleGenetic(OPERATOR_RESCALING_LIB, solver.rescaling))
                + "</td></tr>");

        data.append("\n<tr><td>" + STATISTIC + "  </td><td>\n <table>");
        for (AbstractStatistics s : solver.report.getStatistics()) {
            data.append("\n<tr><td>"
                    + getSourceCodeLink(code, s, HTMLutils.sourceCode, path, getSimpleGenetic(STATISTIC_LIB, s))
                    + "</td></tr>");
        }
        data.append("\n</table>");

        data.append("\n</table> </ul>");

        String config = template.replace("_DATA_", data.toString());

        String configFileName = solverName + "." + FileSolver.FILE_EXTENSION_CONFIG;
        EAExperiment.saveEvolutionSolver(report.solver);
        MyFile.saveToFile(report.solver.toString()
                + "\n\n" + report.getEvolutionString()
                + "\n\n" +
                HTMLutils.getCopyright(),
                 path + configFileName);

        config = config.replace("_TITLE_", HTMLutils.getPageTitle("Configuration of solver " + solverName, configFileName, zipName));

        MyFile.saveToFile(config, path + solverName + "_config.html");
        MyFile.saveToFile(report.solver.toString(), path + solverName + "_config.txt");
        return "\n<tr><td><a href=\"" + report.solver.getSolverName() + "_config.html\" target=\"" + target + "\">Solver</a></td></tr>";

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    class SaveReportTask extends SwingWorker<Void, String> {

        @Override
        public Void doInBackground() {
            try {
                publish("Resizing evolution - " + ReportSolver.NUMBER_OF_STATS_EVOLUTION + " elements");
                report.redimStatisticsEvolution();
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::::::::::::::  B E G I N ::::::::::::::::::::::
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                String targetFrame = "evolution"; //target name of frame in links
                String solverName = report.solver.getSolverName();
                File path = new File(report.path);

                //::::::::::::::::  index.html  :::::::::::::::::::::::::::::::
                publish("Publish " + path.getAbsolutePath());
                String index = HTMLutils.indexTemplate.replace("_HOME.PAGE_", solverName + ".html"); // homepage
                index = index.replaceAll("_TARGET.NAME_", targetFrame);
                index = index.replaceAll("_SOLVER.NAME_", solverName);

                MyFile.saveToFile(index, path.getAbsolutePath() + "/index.html");
                //::::::::::::::::   CSS - style.css :::::::::::::::::::::::::::                 
                MyFile.saveToFile(HTMLutils.css, path.getAbsolutePath() + "/style.css");

                String template = HTMLutils.pageTemplate.replace("_COPYRIGHT_ ", HTMLutils.getCopyright());

                StringBuilder menuOfSolver = new StringBuilder();
                //:::::::::::::::::::::::::::: C O N F I G  :::::::::::::::::::::::::::
                publish("Save config " + solverName);
                menuOfSolver.append(saveConfig(report, template, targetFrame));
                publish("Save Population " + solverName);
                menuOfSolver.append(savePopulation(report, report.solver.parents, "population", template, targetFrame));
                Population hall = report.solver.parents.getCleanClone();
                hall.addAll(report.solver.hallOfFame);
                menuOfSolver.append(savePopulation(report, hall, "hallOfFame", template, targetFrame));
                publish("Save Evolution" + solverName);
                menuOfSolver.append(saveEvolutionStatistics(report, solverName + ".html", template, targetFrame));
                menuOfSolver.append("\n<tr><td style=\"text-align: center\"><h3>Statistics</h3></td></tr>");
                menuOfSolver.append(getPopulationEvolution(report, template, targetFrame));
                //::::::::::::::::::::::::::::  M E N U  :::::::::::::::::::::::::::
                String menu = HTMLutils.menuTemplate.replace("_TITLE_", "<h3>" + solverName + "</h3>");
                menu = menu.replace("_MENU_", menuOfSolver);
                MyFile.saveToFile(menu, path.getAbsolutePath() + "/" + solverName + "_menu.html");

                publish(solverName + " Done!");
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::::::::::::::  E N D !:::::::::::::::::::::::::
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                path = new File(path.getAbsolutePath() + "/index.html");
                java.awt.Desktop.getDesktop().browse(path.toURI());

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(text, ex.getMessage());
                publish(ex.getMessage());
            }
            if (progressDialog != null) {
                progressDialog.dispose();
            }
            return null;
        }

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        @Override
        protected void process(List<String> chunks) {
            if (text != null) {
                text.setText(chunks.get(chunks.size() - 1) + "\n" + text.getText());
                text.setCaretPosition(0);
            } else {
                System.out.println(chunks.get(chunks.size() - 1));
            }
        }
        private JTextArea text;

        public SaveReportTask(JTextArea text) {
            this.text = text;
        }
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    JDialog progressDialog = null; //dialog to be closed by the task
    ReportSolver report;           // report to save
    //Component window;   // report to save

    private WWWSolverSaveReport(ReportSolver report) {
        this.report = report;
    }

    private void save(String fileName, Component window) throws Exception {
        report.setFileName(fileName); // update path and solver name
        if (window != null) {
            progressDialog = new JDialog();
            progressDialog.setModal(true);

            progressDialog.setTitle(report.getFileName());
            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());
            contentPane.setPreferredSize(new Dimension(400, 200));
            final JProgressBar bar = new JProgressBar(0, 100);
            bar.setIndeterminate(true);
            contentPane.add(bar, BorderLayout.NORTH);
            JTextArea txt = new JTextArea();
            txt.setBackground(Color.BLACK);
            txt.setForeground(Color.WHITE);
            txt.setEditable(false);
            contentPane.add(new JScrollPane(txt), BorderLayout.CENTER);
            progressDialog.setContentPane(contentPane);
            progressDialog.pack();

            progressDialog.setLocationRelativeTo(window);
            progressDialog.setLocation(progressDialog.getLocation().x + 200, progressDialog.getLocation().y + 20);
            progressDialog.requestFocusInWindow(); // in front of all
            SaveReportTask task = new SaveReportTask(txt);
            task.execute();
            progressDialog.setVisible(true);
            progressDialog.setLocationRelativeTo(window);
            progressDialog.requestFocusInWindow(); // in front of all

            task.get();
            progressDialog.setVisible(false);
        } else {
            SaveReportTask task = new SaveReportTask(null);
            task.doInBackground();
        }

    }

    public static int IMG_WIDTH = 800;
    public static int IMG_HEIGHT = 600;
}
