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
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.report.statistics.FunctionCalls;
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
public class StatisticsChartSolver {

    public static int maximumItemInSeries = 150; // number of display itens from series

    public static String getChartTitle(EAsolver solver, AbstractStatistics stat) {
        if (solver.numberOfRun == 1) {
            return stat.getSimpleName();
        } else {
            return stat.getSimpleName() + " (average of " + solver.numberOfRun + " runs)";
        }

    }

    /**
     * TabedPane to all statistics
     */
    JTabbedPane tabs = new JTabbedPane();
    ArrayList statisticDataSet; // render to dataSet

    public StatisticsChartSolver(EAsolver solver) {
        setSolver(solver);
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    public void setSolver(EAsolver solver) {
        //tabed pane
        tabs.removeAll();
        statisticDataSet = new ArrayList<>();
        //----------------------------------------------------------------------
        //Convert statistics to GUI
        ArrayList<AbstractStatistics> stats = solver.report.getStatistics();

        for (int i = 0; i < stats.size(); i++) {
            JFreeChart chart = createChart(stats.get(i), solver.getSolverName(), getChartTitle(solver, stats.get(i)), solver.problem);
            formatChart(chart, 1);
            tabs.add(new ChartPanel(chart), chart.getXYPlot().getRangeAxis().getLabel());
        }
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: insert data to chart
        //update charts with values
        ArrayList<Double[]> evolution = solver.report.evolution;
        AbstractStatistics evals = new FunctionCalls();
        for (int i = 0; i < evolution.size(); i++) {
            updateStats(solver.report.getStatisticsData(i, evals), evolution.get(i));
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        tabs.revalidate();
    }

    public void updateStats(EAsolver solver) {
        try { //prevent log zero exceptio
            updateStats(solver.numEvaluations, solver.report.getLastValues());
        } catch (Exception e) {
        }
        
    }

    public void updateStats(double numEvals, Double[] values) {
        for (int i = 0; i < values.length; i++) {
            ((XYSeriesCollection) statisticDataSet.get(i)).getSeries(0).
                    add(numEvals, values[i]);
        }

    }

    protected JFreeChart createChart(AbstractStatistics stat, String label, String title, Individual ind) {
        DefaultXYItemRenderer renderer = new DefaultXYItemRenderer();
        String xAxisTitle = "Fitness Evaluations - " + ind.getNameWithParameters();
        NumberAxis domain = new NumberAxis(xAxisTitle);
        
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        ValueAxis range = new NumberAxis(stat.getTitle());
        if (ind.isLogScale && stat.logScaleEnabled) {
            range = new LogAxis(stat.getTitle()+ "(log scale)");
        }
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        XYPlot plot = new XYPlot();
        plot.setDomainAxis(domain);
        plot.setRangeAxis(range);
        // xyplot.setBackgroundPaint(Color.black);
        XYSeriesCollection dataset = new XYSeriesCollection();

        plot.setDataset(dataset);

        renderer.setSeriesPaint(0, Color.black);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);

        domain.setTickLabelsVisible(true);
        JFreeChart chart = new JFreeChart(title,
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        chart.setNotify(true);

        XYSeries data = new XYSeries(label);
        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        if (maximumItemInSeries > 0) {
//            data.setMaximumItemCount(maximumItemInSeries);
//            data.setNotify(true);
//            ValueAxis domainAxis = plot.getDomainAxis();
//            domainAxis.setAutoRange(true);
//            ValueAxis rangeAxis = plot.getRangeAxis();
//            rangeAxis.setAutoRange(true);
        }
        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        dataset.addSeries(data);
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        statisticDataSet.add(dataset); // save render of statistic
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        return chart;
    }

    protected void formatChart(JFreeChart chart, int series) {
        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setAutoRange(true);
        //   yAxis.setAutoRangeIncludesZero(false);
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

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201604102005L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
