/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * TestRecombination.java
 *
 * Created on 6/Nov/2010, 10:41:15
 */
package GUI.research;

import GUI.utils.MuGAObject;
import GUI.utils.MuGASystem;
import com.evolutionary.operator.mutation.Mutation;
import com.evolutionary.operator.mutation.real.Gauss;
import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.Population;
import com.evolutionary.population.SimplePopulation;
import com.evolutionary.problem.real.RealVector;
import com.evolutionary.problem.real.Sphere;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author manso
 */
public class ReserachMutation extends javax.swing.JFrame {

    Color[] color = {Color.BLACK, Color.GREEN, Color.RED, Color.ORANGE, Color.YELLOW, Color.MAGENTA};
    int N_POINTS = 500;

    int COPIES = 3;

    JFreeChart chart;
    ChartPanel chartPanel;

    XYSeriesCollection dataset;
    XYPlot plot;
    Mutation mutation = new Gauss();
    EAsolver solver = new GA();

    RealVector individual;

    ArrayList<MuGAObject> operators;

    /**
     * Creates new form TestRecombination
     */
    public ReserachMutation() {
        initComponents();

        individual = new Sphere(new double[]{0, 0}, -1, 1);
        individual.setNumberOfCopies(COPIES);
        solver.parents = new MultiPopulation();
        solver.parents.addIndividual(individual, COPIES);

        txtCurrentValue.setText(N_POINTS + "");

        operators = MuGASystem.getGenetic("com.evolutionary.operator.mutation.real");
        DefaultListModel modelL = new DefaultListModel();
        for (MuGAObject oper : operators) {
            modelL.addElement(oper);
        }
        lstCrossover.setModel(modelL);
        lstCrossover.setSelectedIndex(0);
        lstCrossoverMouseClicked(null);
    }

    private ChartPanel creatChart() {

        dataset = createDataset(solver.parents);
        // create a scatter chart...
        chart = ChartFactory.createScatterPlot(
                mutation.getClass().getSimpleName(), "X", "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        plot = chart.getXYPlot();
        XYDotRenderer render = new XYDotRenderer();
        render.setDotHeight(2);
        render.setDotWidth(2);

        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.setRenderer(render);
        plot.setBackgroundPaint(Color.WHITE);
        for (int i = 0; i <individual.getNumberOfCopies(); i++) {
           plot.getRenderer().setSeriesPaint(i, color[i%color.length]);
        }
        
          double x1 = individual.getGenome()[0];
            double y1 = individual.getGenome()[1];
//            Ellipse2D expansion = new Ellipse2D.Double(x1 - DIM, y1 - DIM, DIM * 2, DIM * 2);
//            plot.addAnnotation(new XYShapeAnnotation(expansion, new BasicStroke(8.0f * p1.getNumCopies()), Color.BLACK));
            //          Shape e2 = new Ellipse2D.Double(x1, y1, .01*p1.getNumberOfCopies(), .01*p1.getNumberOfCopies());            
            Shape e2 = new Rectangle2D.Double(x1, y1, 0.01, 0.01);

            plot.addAnnotation(new XYShapeAnnotation(e2,
                    new BasicStroke(10.0f), Color.MAGENTA));
         

        // add the chart to a panel...
        chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    private XYSeriesCollection createDataset(Population pop) {

        //put one point in each the corner
        double min = individual.getMinValue();
        double max = individual.getMaxValue();

        XYSeries[] series = new XYSeries[individual.getNumberOfCopies()];

        for (int i = individual.getNumberOfCopies()-1 ; i >= 0; i--) {
            series[i] = new XYSeries("child(" + (i+1) + ")");
            series[i].add(min, min);
            series[i].add(min, max);
            series[i].add(max, max);
            series[i].add(max, min);
        }

      
        for (int k = 0; k < N_POINTS; k++) {
            for (int i = individual.getNumberOfCopies() ; i > 0; i--) {
                RealVector ind = (RealVector) individual.getClone();
                ind.setNumberOfCopies(i);
                mutation.mutate(ind);
                series[i-1].add(ind.getGenome()[0], ind.getGenome()[1]);
            }
        }

        XYSeriesCollection data = new XYSeriesCollection();
        for (int i = individual.getNumberOfCopies()-1 ; i >= 0; i--)  {
            data.addSeries(series[i]);                        
        }
        return data;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCrossover = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sldCurrent = new javax.swing.JSlider();
        txtCurrentValue = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtParameters = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel.setPreferredSize(new java.awt.Dimension(900, 900));
        panel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panel, java.awt.BorderLayout.CENTER);

        jPanel4.setPreferredSize(new java.awt.Dimension(200, 800));
        jPanel4.setLayout(new java.awt.BorderLayout());

        lstCrossover.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Multiset_AX", "Multiset_AX_mean", "Multiset_AX_mean2", "RCGA_AX", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstCrossover.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstCrossoverMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstCrossover);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Iterations");

        sldCurrent.setMinimum(1);
        sldCurrent.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldCurrentStateChanged(evt);
            }
        });

        txtCurrentValue.setText("5000");

        jLabel2.setText("Parameters");

        txtParameters.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCurrentValue, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sldCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtParameters)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCurrentValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sldCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParameters, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lstCrossoverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstCrossoverMouseClicked

        mutation = (Mutation) operators.get(lstCrossover.getSelectedIndex()).getObject();
        mutation.setParameters(txtParameters.getText());
        txtParameters.setText(mutation.getParameters());
        mutation.setSolver(solver);

        chartPanel = creatChart();
        panel.removeAll();
        panel.add(chartPanel);
        panel.setSize(new java.awt.Dimension(500, 500));
        panel.revalidate();
        panel.repaint();
    }//GEN-LAST:event_lstCrossoverMouseClicked

    private void sldCurrentStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldCurrentStateChanged
        N_POINTS = sldCurrent.getValue() * 100;
        txtCurrentValue.setText(N_POINTS + "");

    }//GEN-LAST:event_sldCurrentStateChanged

    RealVector getRandom() {
        RealVector ind = individual.getClone();
        ind.randomize();
        double[] v = ind.getGenome();
        for (int i = 0; i < v.length; i++) {
            v[i] *= 0.25;

        }
        ind.setDoubleValuesRaw(v);
        return ind;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ReserachMutation().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstCrossover;
    private javax.swing.JPanel panel;
    private javax.swing.JSlider sldCurrent;
    private javax.swing.JTextField txtCurrentValue;
    private javax.swing.JTextField txtParameters;
    // End of variables declaration//GEN-END:variables
}
