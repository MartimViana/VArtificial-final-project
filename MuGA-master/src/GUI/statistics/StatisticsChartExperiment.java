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
package GUI.statistics;

import com.evolutionary.problem.Individual;
import com.evolutionary.report.ReportSolverArray;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Created on 10/abr/2016, 20:05:04
 *
 * @author zulu - computer
 */
public class StatisticsChartExperiment {

    public ArrayList<JFreeChart> charts;

    /**
     * TabedPane to all statisticsLines
     */
    JTabbedPane tabs = new JTabbedPane();

    ArrayList<XYSeriesCollection> statisticDataSet; // render to dataSet

    ArrayList<EAsolver> solvers; // solvers of experiment
    ArrayList<String> solversLabel; // name of solvers ( labels of lines)

    public StatisticsChartExperiment(ArrayList<EAsolver> AsolverArray) {
        solversLabel = getSolverNames(AsolverArray);
        setSolver(AsolverArray);
    }

    /**
     * clean values of datasets
     */
    public void resetDataSets() {
        for (XYSeriesCollection data : statisticDataSet) {
            for (int i = 0; i < data.getSeriesCount(); i++) {
                data.getSeries(i).clear();
            }
        }
    }

    public ArrayList<String> getSolverNames(ArrayList<EAsolver> solverArray) {
        ArrayList<String> lst = new ArrayList<>();
        //----------------------------------------------------------------------
        //change the name of duplicated solvernames
        for (int i = 0; i < solverArray.size(); i++) {
            for (int j = i + 1; j < solverArray.size(); j++) {
                if (solverArray.get(i).getSolverName().equalsIgnoreCase(solverArray.get(j).getSolverName())) {
                    solverArray.get(j).setSolverName(solverArray.get(j).getSolverName() + "_" + j);
                }
            }
        }
        //----------------------------------------------------------------------        
        for (int i = 0; i < solverArray.size(); i++) {
            lst.add(solverArray.get(i).getSolverName());
        }
        return lst;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    public void setSolver(ArrayList<EAsolver> solvers) {
        this.solvers = solvers;
        this.charts = new ArrayList<>();
        //tabed pane
        tabs.removeAll();
        statisticDataSet = new ArrayList<>();
        //----------------------------------------------------------------------
        //Convert statisticsLines to GUI
        ArrayList<AbstractStatistics> stats = solvers.get(0).report.getStatistics();

        for (int i = 0; i < stats.size(); i++) {
            JFreeChart chart = createChart(stats.get(i), StatisticsChartSolver.getChartTitle(solvers.get(0), stats.get(i)), solvers.get(0).problem);
            formatChart(chart, solvers.size());
            tabs.add(new ChartPanel(chart), stats.get(i).getSimpleName());
            charts.add(chart);
        }

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //insert evolutiuon values in the charts rese
        resetDataSets();
        for (int IndexSolver = 0; IndexSolver < solvers.size(); IndexSolver++) {
            EAsolver solver = solvers.get(IndexSolver);

            ReportSolverArray report = (ReportSolverArray) solver.report;
            //no data to display
            if (report.evolution.isEmpty()) {
                continue;
            }

            ArrayList<Double[]> evolution = report.evolution;
            int step = evolution.size() / 500 + 1;
            for (int j = 1; j < evolution.size() - 1; j += step) {
                Double[] values = evolution.get(j); // values of statistics

                for (int indexStat = 0; indexStat < values.length; indexStat++) {
                    int indexChart = solver.report.indexOf(stats.get(indexStat));
                    if (indexChart >= 0) {
                        statisticDataSet.get(indexStat).getSeries(IndexSolver).
                                add(report.getNumberOfEvalutions(j), values[indexChart]);
                    }
                }
            }
        }

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        tabs.revalidate();
    }

    public void updateStats(EAsolver solver) {
        updateStats(solvers.indexOf(solver), solver.numEvaluations, solver.report.getLastValues());
    }

    public void updateStats(int index, double numEvals, Double[] values) {
        for (int i = 0; i < values.length; i++) {
            try {
                statisticDataSet.get(i).getSeries(index).
                    add(numEvals, values[i]);
            } catch (Exception e) {
            }
            
        }

    }

    protected JFreeChart createChart(AbstractStatistics stat, String title, Individual ind) {
        DefaultXYItemRenderer renderer = new DefaultXYItemRenderer();
        String nameRange = stat.getTitle();
//        if (solvers.get(0) instanceof EAsolverArray) {
//            nameRange += "  average(" + ((EAsolverArray) solvers.get(0)).arrayOfSolvers.length
//                    + ")";
//        }
        String xAxisTitle = "Fitness Evaluations - " + solvers.get(0).problem.getNameWithParameters();
        NumberAxis domain = new NumberAxis(xAxisTitle);

        // NumberAxis range = new NumberAxis(nameRange);
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        ValueAxis range = new NumberAxis(nameRange);
        if (ind.isLogScale && stat.logScaleEnabled) {
            range = new LogAxis(stat.getTitle() + "(log scale)");
        }
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        XYPlot xyplot = new XYPlot();
        xyplot.setDomainAxis(domain);
        xyplot.setRangeAxis(range);

        // xyplot.setBackgroundPaint(Color.black);
        XYSeriesCollection dataset = new XYSeriesCollection();

        xyplot.setDataset(dataset);

        renderer.setSeriesPaint(0, Color.black);
        renderer.setSeriesShapesVisible(0, false);
        xyplot.setRenderer(renderer);

        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);

        domain.setTickLabelsVisible(true);
        JFreeChart chart = new JFreeChart(title,
                JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);

        chart.setNotify(true);

        for (int i = 0; i < solversLabel.size(); i++) {
            XYSeries data = new XYSeries(solversLabel.get(i));
            dataset.addSeries(data);
        }
        formatChart(chart, solversLabel.size());

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        statisticDataSet.add(dataset); // save render of statistic
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        return chart;
    }

    protected void formatChart(JFreeChart chart, int series) {
        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);
        ValueAxis yAxis = (ValueAxis) plot.getRangeAxis();
        yAxis.setAutoRange(true);
 //       yAxis.setAutoRangeIncludesZero(false);
//         set the plot's axes to display integers
        TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setStandardTickUnits(ticks);
//         set the renderer's stroke
        Stroke stroke1 = new BasicStroke(
                3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        for (int i = 0; i < series; i++) {
            renderer.setSeriesStroke(i, stroke1);
            float color = (i) / (float) (series);
            renderer.setSeriesPaint(i, Color.getHSBColor(color, 1, 1));
        }
        plot.setRenderer(renderer);
    }

    public Color getColorIndex(int index) {
        JFreeChart chart = charts.get(0);
        XYPlot plot = chart.getXYPlot();
        return (Color) plot.getRenderer().getSeriesPaint(index);

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // CHANGE COLS AND NAMES OF SERIES
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
    public void setColorIndex(Color color, int index) {
        for (JFreeChart chart : charts) {
            XYPlot plot = chart.getXYPlot();
            plot.getRenderer().setSeriesPaint(index, color);
        }
    }
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 

    public void setStrokeSizeIndex(int sizeofStroke, int index) {
        Stroke stroke1 = new BasicStroke(
                3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);

        for (JFreeChart chart : charts) {
            XYPlot plot = chart.getXYPlot();
            plot.getRenderer().setSeriesStroke(index, StrokeChart.getStroke(sizeofStroke));
        }
    }
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 

    public void setNameIndex(String newName, int index) {
        for (JFreeChart chart : charts) {
            XYPlot plot = chart.getXYPlot();
            XYSeriesCollection series = (XYSeriesCollection) plot.getDataset();
            series.getSeries(index).setKey(newName);
        }
    }
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 

    public String getNameIndex(int index) {
        JFreeChart chart = charts.get(0);
        XYPlot plot = chart.getXYPlot();
        XYSeriesCollection series = (XYSeriesCollection) plot.getDataset();
        return series.getSeries(index).getKey().toString();

    }
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201604102005L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
