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

import com.evolutionary.report.statistics.AbstractStatistics;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 *
 * @author ZULU
 */
public class StatisticsBoxAndWhisker {

//    static Color[] color = {Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.MAGENTA, Color.YELLOW, Color.DARK_GRAY, Color.ORANGE, Color.CYAN};
    public static JPanel getBoxAndWhisker(double[][] values, String[] algorithms, AbstractStatistics stat, String title) {
        JPanel pan = new JPanel(new java.awt.BorderLayout());        
        ChartPanel CP = createBoxAndWhiskerChart(values, algorithms, stat, title);
        pan.add(CP, BorderLayout.CENTER);
        pan.validate();
        return pan;
    }

    public static ChartPanel createBoxAndWhiskerChart(double[][] values, String[] algorithms, AbstractStatistics stat, String title) {
        BoxAndWhiskerCategoryDataset data = createDataSet(values, algorithms);
       // JFreeChart chart = createChart(data, stat, title);
        JPanel pan = new JPanel();
        pan.setLayout(new java.awt.BorderLayout());
        return new ChartPanel(createChart(data, stat, title));
    }

    private static BoxAndWhiskerCategoryDataset createDataSet(double[][] value, String[] algorithm) {
        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        for (int i = 0; i < algorithm.length; i++) {
            final List list = new ArrayList();
            for (int k = 0; k < value[i].length; k++) {
                list.add(new Double(value[i][k]));
            }
            dataset.add(list, algorithm[i] + "    ", "");
        }
        return dataset;

    }

    private static JFreeChart createChart(BoxAndWhiskerCategoryDataset dataset, AbstractStatistics stat, String title) {
        final CategoryAxis xAxis = new CategoryAxis("");
        final NumberAxis yAxis = new NumberAxis(stat.getTitle());
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        ValueAxis range = new NumberAxis(stat.getTitle());
        if (stat.logScaleEnabled) {
            range = new LogAxis(stat.getTitle() + "(log scale)");
        }
        //:::::::::::::::::::: LOG SCALE :::::::::::::::::::::::::::::::::::::::
        yAxis.setAutoRangeIncludesZero(false);

        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(true);
        renderer.setMedianVisible(true);
        renderer.setMeanVisible(true);
//        renderer.setArtifactPaint(new Color(128, 128, 128));

        renderer.setMaximumBarWidth(0.05);
        renderer.setItemMargin(0.5);
        renderer.setBaseOutlineStroke(new BasicStroke(1));
        renderer.setBaseOutlinePaint(Color.BLACK);
        renderer.setUseOutlinePaintForWhiskers(true);
 
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, range, renderer);
        //final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
                title,
                new Font("SansSerif", Font.BOLD, 16),
                plot,
                true);

//        // set the background color for the chart...
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(new Color(220, 220, 220));
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        chart.setAntiAlias(true);

        //set colors 
        for (int i = 0; i < dataset.getRowCount(); i++) {
            float color = (i) / (float) (dataset.getRowCount());
            renderer.setSeriesPaint(i, Color.getHSBColor(color, 1, 1));
            //renderer.setSeriesPaint(i, color[i % color.length]);
        }
        return chart;
    }

    public static void saveChart(double[][] values, String[] algorithms, AbstractStatistics stat, String title, String path) {
        saveChart(values, algorithms, stat, title, path, 500);
        saveChart(values, algorithms, stat, title, path, 300);
    }

    public static void saveChart(double[][] values, String[] algorithms, AbstractStatistics stat, String title, String path, int size) {
        try {
            BoxAndWhiskerCategoryDataset data = createDataSet(values, algorithms);
            JFreeChart chart = createChart(data, stat, title);
            //make path
            File dir = new File(path + "boxChart/");
            dir.mkdirs();
            File image = new File(dir, "xpto.png");
            ChartUtilities.saveChartAsPNG(image, chart, 500, size);
        } catch (IOException ex) {
            Logger.getLogger(StatisticsBoxAndWhisker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
