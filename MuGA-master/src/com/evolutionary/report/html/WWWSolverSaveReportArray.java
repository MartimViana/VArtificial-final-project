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

import GUI.statistics.StatisticsChartSimulation;
import GUI.statistics.StatisticsChartSolver;
import com.evolutionary.population.Population;
import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.ReportSolverArray;
import com.evolutionary.report.StatisticElement;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.report.statistics.BestFitness;
import com.evolutionary.report.statistics.FunctionCalls;
import com.evolutionary.report.statistics.FuncsCallsToOptimum;
import com.evolutionary.report.statistics.GeneticDiversityBinary;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solverUtils.EAsolverArray;
import com.evolutionary.solverUtils.FileSolver;
import com.utils.MyFile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;

/**
 * Created on 25/jan/2019, 14:53:16
 *
 * @author zulu - computer
 */
public class WWWSolverSaveReportArray {

    public static void main(String[] args) throws Exception {
//        EAsolver s = new GA();
//        s.numberOfRun = 64;
//        EAsolverArray array = new EAsolverArray(s);
//
//        array.solve(true);
//        array.report.saveObject("array.muga");
        ReportSolver report = (ReportSolver) ReportSolver.loadReportObject("array.muga");
        ArrayList<AbstractStatistics> templateStats = new ArrayList<>();
        templateStats.add(new BestFitness());
        templateStats.add(new FuncsCallsToOptimum());
        templateStats.add(new FunctionCalls());
        templateStats.add(new GeneticDiversityBinary());

        report.updateStatisticsTemplates(templateStats);

        //System.out.println(s.getEvolutionString());
        new WWWSolverSaveReportArray(report).save("www/onesMax2/ceiling.muga", null);
    }

    public static void save(ReportSolver report, String fileName, Component window) {
        try {
            if (report.solver instanceof EAsolverArray) {
                new WWWSolverSaveReportArray(report).save(fileName, window);
            } else {
                WWWSolverSaveReport.save(report, fileName, window);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static String saveStatisticElement(ReportSolverArray report, int index) throws Exception {
        //Table Header
        ArrayList<StatisticElement[]> statsEvolutions = report.evolutionGeneration;
        StringBuilder txt = new StringBuilder();
        txt.append("<table  align=\"center\" style=\"width:100%\">\n"
                + "<tr><th>Statistic</th>");
        for (String h : StatisticElement.header) {
            txt.append("<th>" + h + "</th>");
        }
        txt.append("</tr>\n");
        //table data
        for (int i = statsEvolutions.size() - 1; i >= 0; i--) {
            txt.append("<tr> <td> " + i + "</td>");
            StatisticElement[] data = statsEvolutions.get(i);
            double[] values = data[index].getData();
            for (int k = 0; k < values.length; k++) {
                txt.append("<td><pre>" + HTMLutils.getNumber(values[k], Locale.ENGLISH) + "</pre></td>");
            }
            txt.append("</tr>\n");
        }

        txt.append("</table>");
        return txt.toString();

    }

    public static String saveEvolutionStatistics(ReportSolverArray report, String template, String frameTarget) throws Exception {
        String solverName = report.solver.getSolverName();
        StringBuilder menuItem = new StringBuilder();
        int width = 800;
        int height = 600;
        File path = new File(report.path);

        StatisticsChartSolver statsPanel = new StatisticsChartSimulation(report.solver);

        for (int index = 0; index < report.stats.size(); index++) {

            String statisticName = report.stats.get(index).getTitle();
            String statisticfile = report.stats.get(index).getSimpleName(); //statsPanel.getTabs().getTitleAt(index);

            ChartPanel gr = (ChartPanel) statsPanel.getTabs().getComponentAt(index);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                    path.getAbsolutePath() + "/" + solverName + "_" + statisticfile + ".jpg"));
            ChartUtilities.writeChartAsJPEG(out, gr.getChart(), width, height);

            String txt = "<img src=\"" + solverName + "_" + statisticfile + ".jpg\"><br>"
                    + saveStatisticElement(report, index);

            String dataHtml = template.replace("_TITLE_", "<h2> " + solverName + " " + statisticName + "</h2>\n");
            dataHtml = dataHtml.replace("_DATA_", txt.toString());
            MyFile.saveToFile(dataHtml, path.getAbsolutePath() + "/" + solverName + "_" + statisticfile + ".html");
            menuItem.append("\n<tr><td><a href=\"" + solverName + "_" + statisticfile + ".html\" "
                    + "target=\"" + frameTarget + "\">" + statisticfile + "</a></td></tr>");

        }

        return "";
    }

    public static String getStatisticString(ReportSolverArray report, String template, String frameTarget, boolean saveArraySolvers) throws Exception {
        Locale local = Locale.getDefault();

        File path = new File(report.path);

        String solverName = report.solver.getSolverName();
        StringBuilder dataCSV = new StringBuilder();
        StringBuilder dataHtml = new StringBuilder();
        dataHtml.append("<table align=\"center\">\n"
                + "<tr><th>Statistic</th>");
        dataCSV.append("Statistic ");
        //Statistics  BestFitness           SucessRate          Individuals  
        for (AbstractStatistics h : report.stats) {
            dataHtml.append("<th><a href=\"" + solverName + "_" + h.toString() + ".html\">" + h.toString() + "</a></th>");
            dataCSV.append(HTMLutils.CSV_COMMA + h.toString());
        }
        dataHtml.append("</td>");

        StatisticElement[] lasStat = report.evolutionGeneration.get(report.evolutionGeneration.size() - 1);
        //MEAN  STD   MIN   MEDIAN MAX          
        for (int line = 0; line < StatisticElement.header.length; line++) {
            dataHtml.append("\n<tr align=\"center\"><td>" + StatisticElement.header[line] + "</td>");
            dataCSV.append("\n" + StatisticElement.header[line]);
            for (int column = 0; column < report.stats.size(); column++) {
                double[] values = lasStat[column].getData();
                dataHtml.append("<td><pre>" + HTMLutils.getNumber(values[line], Locale.ENGLISH) + "</pre></td>");
                dataCSV.append(HTMLutils.CSV_COMMA + String.format(local, "%f", values[line]));
            }
        }
        dataHtml.append("</tr>\n<th>Solvers</th>\n");

        dataCSV.append("\n\nSolvers\n");

        EAsolverArray array = (EAsolverArray) report.solver;
        for (int line = 0; line < array.arrayOfSolvers.length; line++) {

            //link to solver
            String sName = array.arrayOfSolvers[line].getSolverName();
            if (saveArraySolvers) {
                dataHtml.append("\n<tr><td><a href=\"" + sName + "/index.html\" target=\""
                        + frameTarget + "\">" + sName + "</a></td>");
            } else {
                //::::::::::::: SAVE STATISTICS ONLY :::::::::::::::::::::::::::
                String solverItemName = array.arrayOfSolvers[line].getSolverName();
                array.arrayOfSolvers[line].report.setPath(path + "/" + solverItemName + "/");
                WWWSolverSaveReport.saveEvolutionStatistics(array.arrayOfSolvers[line].report, "index.html", template, frameTarget);
                dataHtml.append("\n<tr><td><a href=\"" + sName + "/index.html\" target=\""
                        + frameTarget + "\">" + sName + "</a></td>");
                // dataHtml.append("\n<tr><td>" + sName + "</td>");
            }
            dataCSV.append("Solver " + line);
            Double[] dataValues = array.arrayOfSolvers[line].report.getLastValues();
            for (Double dataValue : dataValues) {
                dataHtml.append("<td><pre>" + HTMLutils.getNumber(dataValue, Locale.ENGLISH) + "</pre></td>");
                dataCSV.append(HTMLutils.CSV_COMMA + String.format(local, "%f", dataValue));
            }
            dataHtml.append("</tr>");
            dataCSV.append("\n");
        }

        dataHtml.append("\n</table>");

        template = template.replace("_TITLE_", HTMLutils.getPageTitle(solverName + " Statistics of last generation",
                solverName + ".csv", solverName + ".muga"));

        template = template.replace("_DATA_", dataHtml.toString());
        MyFile.saveToFile(template, path.getAbsolutePath() + "/" + solverName + ".html");
        MyFile.saveToFile(dataCSV.toString(), path.getAbsolutePath() + "/" + solverName + ".csv");
        report.saveObject(path.getAbsolutePath() + "/" + solverName + "." + FileSolver.FILE_EXTENSION_MUGA);

        return "\n<tr><td><a href=\"" + solverName + ".html\" target=\""
                + frameTarget + "\">Statistics</a></td></tr>";
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    class SaveReportArrayTask extends SwingWorker<Void, String> {

        @Override
        public Void doInBackground() {
            try {
                publish("Starting report");
                publish("Resizing evolution - " + ReportSolver.NUMBER_OF_STATS_EVOLUTION+ " elements");
                report.redimStatisticsEvolution();
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::::::::::::::  B E G I N ::::::::::::::::::::::
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                File path = new File(report.path);
                publish("Publish " + path.getAbsolutePath());

                EAsolverArray array = (EAsolverArray) report.solver;
                String targetFrame = "simulation"; //target name of frame in links
                String solverName = report.solver.getSolverName();

                //::::::::::::::::  index.html  :::::::::::::::::::::::::::::::
                String index = HTMLutils.indexTemplate.replace("_HOME.PAGE_", solverName + "_config.html");
                index = index.replaceAll("_TARGET.NAME_", targetFrame);
                index = index.replaceAll("_SOLVER.NAME_", solverName);

                MyFile.saveToFile(index, path.getAbsolutePath() + "/index.html");
                //::::::::::::::::   CSS - style.css :::::::::::::::::::::::::::                 
                MyFile.saveToFile(HTMLutils.css, path.getAbsolutePath() + "/style.css");

                String template = HTMLutils.pageTemplate.replace("_COPYRIGHT_ ", HTMLutils.getCopyright());

                StringBuilder menuOfSolver = new StringBuilder();
                //::::::::::::::::  Config.html :::::::::::::::::::::::::::                 
                publish("Save configuration " + solverName);
                menuOfSolver.append(WWWSolverSaveReport.saveConfig(report, template, targetFrame));
                //::::::::::::::::  population.html :::::::::::::::::::::::::::                  
                Population hall = report.solver.parents.getCleanClone();
                hall.addAll(report.solver.hallOfFame);
                menuOfSolver.append(WWWSolverSaveReport.savePopulation(report, hall, "hallOfFame", template, targetFrame));
                //:::::::::::::::::::::::::::: Satistics.html  :::::::::::::::::::::::::::       
                publish("Save Statistics " + solverName);
                menuOfSolver.append(getStatisticString((ReportSolverArray) report, template, targetFrame, progressDialog != null));
                //:::::::::::::::: SOLVERS::::::::::::::::::::::::::::
                publish("Save Evolution " + solverName);
                saveEvolutionStatistics((ReportSolverArray) report, template, targetFrame);

                menuOfSolver.append("\n<tr><td style=\"text-align: center\"><h3>Statistics</h3></td></tr>\n");
                for (AbstractStatistics stat : report.stats) {
                    menuOfSolver.append("\n<tr><td><a href=\""
                            + solverName + "_" + stat.getSimpleName() + ".html\" target=\""
                            + targetFrame + "\"> " + stat.getSimpleName() + "</a></td></tr>");
                }

                menuOfSolver.append("\n<tr><td style=\"text-align: center\"><h3>Solvers</h3></td></tr>\n");
                for (EAsolver solver : array.arrayOfSolvers) {
                    if (progressDialog != null) {
                        publish(solverName + " Save Evolution " + solver.getSolverName() + " of " + (array.arrayOfSolvers.length - 1));
                        WWWSolverSaveReport.save(solver.report, path.getAbsolutePath() + "/" + solver.getSolverName() + "/", null);
                    }
                    menuOfSolver.append("\n<tr><td><a href=\""
                            + solver.getSolverName() + "/index.html\" target=\""
                            + targetFrame + "\"> " + solver.getSolverName() + "</a></td></tr>");
                }

                //::::::::::::::::::::::::::::  M E N U  :::::::::::::::::::::::::::
                String menu = HTMLutils.menuTemplate.replace("_TITLE_", "<h3>" + solverName + "</h3>");
                menu = menu.replace("_MENU_", menuOfSolver.toString());
                MyFile.saveToFile(menu, path.getAbsolutePath() + "/" + solverName + "_menu.html");
                publish(solverName + " done !");

                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //::::::::::::::::::::::::::::  E N D !:::::::::::::::::::::::::
                //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                path = new File(path.getAbsolutePath() + "/index.html");
                //java.awt.Desktop.getDesktop().browse(path.toURI());
            } catch (Exception ex) {
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

        public SaveReportArrayTask(JTextArea text) {
            this.text = text;
        }
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    JDialog progressDialog = null; //dialog to be closed by the task
    ReportSolver report;           // report to save
    // Component window;

    private WWWSolverSaveReportArray(ReportSolver report) {
        this.report = report;
    }

    private void save(String fileName, Component window) throws Exception {
        report.setFileName(fileName); // update path and solver name
        if (window != null) {
            //  this.window = window;
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
            txt.setForeground(Color.CYAN);
            txt.setEditable(false);
            contentPane.add(new JScrollPane(txt), BorderLayout.CENTER);
            progressDialog.setContentPane(contentPane);
            progressDialog.pack();

            progressDialog.setLocationRelativeTo(window);
            progressDialog.setLocation(progressDialog.getLocation().x + 200, progressDialog.getLocation().y + 20);
            progressDialog.requestFocusInWindow(); // in front of all
            SaveReportArrayTask task = new SaveReportArrayTask(txt);
            task.execute();
            progressDialog.setVisible(true);
            progressDialog.setLocationRelativeTo(window);
            progressDialog.requestFocusInWindow(); // in front of all

            task.get();
            progressDialog.setVisible(false);
        } else {
            SaveReportArrayTask task = new SaveReportArrayTask(null);
            task.execute();
            task.get();
        }

    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201901251453L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2019  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
