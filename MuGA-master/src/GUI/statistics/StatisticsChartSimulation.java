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
import com.evolutionary.report.StatisticElement;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solverUtils.EAsolverArray;
import com.evolutionary.solver.GA;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.xy.YIntervalSeries;

import org.jfree.data.xy.YIntervalSeriesCollection;

/**
 * Created on 11/abr/2016, 10:33:56
 *
 * @author zulu - computer
 */
public class StatisticsChartSimulation extends StatisticsChartSolver {

    public StatisticsChartSimulation(EAsolver solver) {
        super(solver);
    }

    @Override
    public void setSolver(EAsolver solver) {
        //tabed pane
        tabs.removeAll();
        statisticDataSet = new ArrayList<>();
        //----------------------------------------------------------------------
        //Conver statistics to GUI
        ArrayList<AbstractStatistics> stats = solver.report.getStatistics();

        for (int i = 0; i < stats.size(); i++) {
            JFreeChart chart = createChart(stats.get(i), solver.getSolverName(), getChartTitle(solver, stats.get(i)), solver.problem);
            formatChart(chart, 1);
            tabs.add(new ChartPanel(chart), chart.getXYPlot().getRangeAxis().getLabel());
        }
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: insert data to chart
        ReportSolverArray report = (ReportSolverArray) solver.report;
        //update charts with values
        ArrayList<StatisticElement[]> evolutionGeneration = report.evolutionGeneration;
        for (int i = 0; i < evolutionGeneration.size(); i++) {
            updateStats(report.getNumberOfEvalutions(i), evolutionGeneration.get(i));
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        tabs.revalidate();
    }

    @Override
    protected JFreeChart createChart(AbstractStatistics stat, String label, String title, Individual ind) {
        DefaultXYItemRenderer renderer = new DefaultXYItemRenderer();
        String xAxisTitle = "Fitness Evaluations - " + ind.getNameWithParameters();
        NumberAxis domain = new NumberAxis(xAxisTitle);
        //NumberAxis range = new NumberAxis(stat.getSimpleName());
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        ValueAxis range = new NumberAxis(stat.getSimpleName());
        if (ind.isLogScale && stat.logScaleEnabled) {
            range = new LogAxis(stat.getTitle()+"(log scale)");
        }
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        XYPlot xyplot = new XYPlot();
        xyplot.setDomainAxis(domain);
        xyplot.setRangeAxis(range);
        // xyplot.setBackgroundPaint(Color.black);
        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();

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

        YIntervalSeries data = new YIntervalSeries(label);
        dataset.addSeries(data);
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        statisticDataSet.add(dataset); // save render of statistic
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        return chart;
    }

    @Override
    protected void formatChart(JFreeChart chart, int series) {
        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);
        ValueAxis yAxis = (ValueAxis) plot.getRangeAxis();
        yAxis.setAutoRange(true);
  //      yAxis.setAutoRangeIncludesZero(false);
//         set the plot's axes to display integers
        TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setStandardTickUnits(ticks);
//         set the renderer's stroke
        Stroke stroke1 = new BasicStroke(
                3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        DeviationRenderer renderer = new DeviationRenderer(true, false);
        for (int i = 0; i < series; i++) {
            renderer.setSeriesStroke(i, stroke1);
            float color = (i) / (float) (series);
            renderer.setSeriesPaint(i, Color.getHSBColor(color, 1, 1));
            renderer.setSeriesFillPaint(0, setColorTransparency(Color.getHSBColor(color, 1, 1), 0.5f));
        }
        plot.setRenderer(renderer);

    }

    public static Color setColorTransparency(Color c, float transp) {
        return new Color(
                c.getRed() / 255.0f,
                c.getGreen() / 255.0f,
                c.getBlue() / 255.0f,
                transp);
    }

    @Override
    public void updateStats(EAsolver solver) {
        ReportSolverArray report = (ReportSolverArray) solver.report;
        StatisticElement[] values = report.getLastValuesStatistcs();
        updateStats(report.getNumberOfEvalutions(Integer.MAX_VALUE), values); // Integer.MAX_VALUE = maxEvals in last evolution

    }

    public void updateStats(double numEvals, StatisticElement values[]) {
        for (int i = 0; i < values.length; i++) {
            try {
                ((YIntervalSeriesCollection) statisticDataSet.get(i)).getSeries(0).
                    add(numEvals,
                            values[i].getMean(),
                            values[i].getMin(),
                            values[i].getMax());
            } catch (Exception e) {
            }
            
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201604111033L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        EAsolver muga = new GA();
        muga.numberOfRun = 10;
        EAsolverArray solver = new EAsolverArray(muga);

        solver.InitializeEvolution(true);

        StatisticsChartSolver demo = new StatisticsChartSimulation(solver);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 200));
        panel.add(demo.getTabs(), BorderLayout.CENTER);
        JFrame frm = new JFrame("demo Array");
        frm.getContentPane().add(panel);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        solver.iterate();
                        demo.updateStats(solver);
                        frm.repaint();
//                        Thread.sleep(200);

                        //  JOptionPane.showMessageDialog(null, i+"");
                    }

                } catch (Exception ex) {
                    Logger.getLogger(StatisticsChartSimulation.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }
}
