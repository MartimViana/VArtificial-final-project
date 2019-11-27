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
import com.utils.MyStatistics;
import com.utils.Ranking;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author ZULU
 */
public class StatisticsRankChart {

    public static JPanel getRank(double[][] result, String[] label, AbstractStatistics stat) {
        JPanel pan = new JPanel();
        pan.setLayout(new java.awt.BorderLayout());
        pan.add(getRankChart(result, label, stat), BorderLayout.CENTER);
        pan.validate();
        return pan;
    }

    public static ChartPanel getRankChart(double[][] result, String[] label, AbstractStatistics stat) {
        char[][] compare95 = MyStatistics.compareMeansTtest(result, 0.05, stat.higherIsBetter);
        int[] score95 = MyStatistics.sumMeanComparations(compare95);
        int[] rank95 = Ranking.rank(score95);
        DefaultCategoryDataset data = createDataSet(rank95, label);
        JFreeChart chart = createChart(data, stat.getTitle());
        JPanel pan = new JPanel();
        pan.setLayout(new java.awt.BorderLayout());
        return new ChartPanel(chart);
    }

    private static DefaultCategoryDataset createDataSet(int[] v, String[] label) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < v.length; i++) {
            dataset.addValue(v[i], "", label[i]);
        }
        return dataset;

    }

    private static JFreeChart createChart(DefaultCategoryDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createBarChart(
                title, // chart title
                "", // domain axis label
                "Rank", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                false, // include legend
                true, // tooltips?
                false // URLs?
        );

        final CategoryPlot plot = chart.getCategoryPlot();

        final CategoryAxis axis = plot.getDomainAxis();
//        axis.setCategoryLabelPositions(
//                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0));
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setItemMargin(0.05);

        // change the margin at the top of the range axis...
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //range of axis ( 0 - 110%)
        rangeAxis.setRange(0, dataset.getColumnCount() * 1.1);

        renderer.setItemLabelsVisible(true);

//        LegendTitle legend = chart.getLegend();
//        legend.setPosition(RectangleEdge.TOP);
//        rangeAxis.setInverted(true);
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(new Color(225, 225, 225));
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
//        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);

//        GradientPaint gradientpaint0 = new GradientPaint(0.0F, 0.0F, new Color(230, 230, 250), 0.0F, 0.0F, new Color(136, 136, 255));
        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, new Color(100, 200, 100), 0.0F, 0.0F, new Color(0, 100, 0));
//        GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, new Color(205, 92, 92), 0.0F, 0.0F, new Color(136, 136, 255));
        renderer.setSeriesPaint(0, gradientpaint1);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.1);
        domainAxis.setLowerMargin(0.01);
        domainAxis.setUpperMargin(0.01);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        plot.setDomainAxis(domainAxis);

        final CategoryPlot p = chart.getCategoryPlot();
        DecimalFormat formt = new DecimalFormat("##");
        //show 
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", formt));

//        TextTitle source = new TextTitle(
//                "  MuGA(c)2013");
//        source.setFont(new Font("SansSerif", Font.PLAIN, 10));
//        source.setPaint(Color.DARK_GRAY);
//        source.setPosition(RectangleEdge.BOTTOM);
//        source.setHorizontalAlignment(HorizontalAlignment.LEFT);
//        chart.addSubtitle(source);
        //formatChart(title);
        return chart;
    }

    public static void saveRankChart(int[] v, String[] label, String title, String path) {
        try {
            DefaultCategoryDataset data = createDataSet(v, label);
            JFreeChart chart = createChart(data, title);
            //make path
            File dir = new File(path + "rank/");
            dir.mkdirs();
            File image = new File(dir, path + ".png");
            ChartUtilities.saveChartAsPNG(image, chart, 800, 800);
        } catch (IOException ex) {
            Logger.getLogger(StatisticsRankChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
