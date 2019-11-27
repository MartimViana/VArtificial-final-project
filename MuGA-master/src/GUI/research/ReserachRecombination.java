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
import com.evolutionary.operator.recombination.Recombination;
import com.evolutionary.operator.recombination.real.AX;
import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.Population;
import com.evolutionary.population.SimplePopulation;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import com.evolutionary.problem.real.Sphere;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
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
public class ReserachRecombination extends javax.swing.JFrame {

    Color[] color = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.MAGENTA};
    int N_POINTS = 500;

    int COPIES = 5;
    JFreeChart chart;
    ChartPanel chartPanel;
    Individual ind;
    XYSeriesCollection dataset;
    XYPlot plot;
    Recombination crossover = new AX();
    EAsolver solver = new GA();

    RealVector individual = new Sphere();

    ArrayList<MuGAObject> operators;

    /**
     * Creates new form TestRecombination
     */
    public ReserachRecombination() {
        initComponents();
        txtCurrentValue.setText(N_POINTS + "");
        individual.setParameters("2");

        operators = MuGASystem.getGenetic("com.evolutionary.operator.recombination.real");
        DefaultListModel modelL = new DefaultListModel();
        for (MuGAObject oper : operators) {
            modelL.addElement(oper);
        }
        lstCrossover.setModel(modelL);
        lstCrossover.setSelectedIndex(0);
        btMultipopActionPerformed(null);

    }

    private ChartPanel creatChart(Individual p) {
//        Random rnd = new Random();
        Population pop = solver.parents;

        dataset = createDataset(pop);
        // create a scatter chart...
        chart = ChartFactory.createScatterPlot(
                crossover.getClass().getSimpleName(), "X", "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        plot = chart.getXYPlot();
        XYDotRenderer render = new XYDotRenderer();
        render.setDotHeight(2);
        render.setDotWidth(2);
        //cor dos pontos
//        for (int i = 0; i < 5; i++) {
        render.setSeriesPaint(0, Color.BLACK);
//        }
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.setRenderer(render);
        plot.setBackgroundPaint(Color.WHITE);

        List<Individual> inds = pop.getGenomes();

        for (int i = 0; i < inds.size(); i++) {
            RealVector p1 = (RealVector) inds.get(i);
//            Individual p2 = pop.getGenotype((i + 1) % pop.getNumGenotypes() );
            double x1 = p1.getGenome()[0];
            double y1 = p1.getGenome()[1];
//            Ellipse2D expansion = new Ellipse2D.Double(x1 - DIM, y1 - DIM, DIM * 2, DIM * 2);
//            plot.addAnnotation(new XYShapeAnnotation(expansion, new BasicStroke(8.0f * p1.getNumCopies()), Color.BLACK));
            //          Shape e2 = new Ellipse2D.Double(x1, y1, .01*p1.getNumberOfCopies(), .01*p1.getNumberOfCopies());            
            Shape e2 = new Rectangle2D.Double(x1, y1, 0.01, 0.01);

            plot.addAnnotation(new XYShapeAnnotation(e2,
                    new BasicStroke(10.0f), color[((i) % color.length)]));
        }

        // add the chart to a panel...
        chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    private XYSeriesCollection createDataset(Population pop) {
        List<Individual> inds = pop.getGenomes();
        crossover.setParameters("1.0");
        Population child = crossover.execute(pop.getClone());

        XYSeries[] series = new XYSeries[pop.getPopulationSize() + 1];
        series[0] = new XYSeries("offspring");
        for (int i = 0; i < inds.size(); i++) {
            series[i + 1] = new XYSeries("p" + i + "(" + inds.get(i).getNumberOfCopies() + ")");
        }
        //put one point in each the corner
        double min = ((RealVector) pop.getIndividual(0)).getMinValue();
        double max = ((RealVector) pop.getIndividual(0)).getMaxValue();

        series[0].add(min, min);
        series[0].add(min, max);
        series[0].add(max, max);
        series[0].add(max, min);

        for (int i = 0; i < N_POINTS; i++) {
            Population p = pop.getClone();
            p.evaluate();
            child = crossover.execute(p);
            List<Individual> offspring = child.getGenomes();
            for (Individual individual1 : offspring) {
                RealVector ind = (RealVector) individual1;
                series[0].add(ind.getGenome()[0], ind.getGenome()[1]);
            }

        }

        XYSeriesCollection data = new XYSeriesCollection();
        for (int i = 0; i < series.length; i++) {
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
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btMultipop = new javax.swing.JButton();
        btAddOne = new javax.swing.JButton();

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

        jLabel1.setText("Generation");

        sldCurrent.setMinimum(1);
        sldCurrent.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldCurrentStateChanged(evt);
            }
        });

        txtCurrentValue.setText("50");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sldCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCurrentValue, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCurrentValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sldCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setText("SimpleiPop");
        jButton1.setPreferredSize(new java.awt.Dimension(119, 100));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btMultipop.setText("MultiPop");
        btMultipop.setPreferredSize(new java.awt.Dimension(119, 100));
        btMultipop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMultipopActionPerformed(evt);
            }
        });

        btAddOne.setText("Add One");
        btAddOne.setPreferredSize(new java.awt.Dimension(119, 100));
        btAddOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddOneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btMultipop, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addComponent(btAddOne, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btMultipop, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btAddOne, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SimplePopulation pop = new SimplePopulation();
        List<Individual> lst = solver.parents.getGenomes();
        for (Individual ind : lst) {
            pop.addIndividual(ind);
        }
        solver.parents = pop;
        lstCrossoverMouseClicked(null);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void lstCrossoverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstCrossoverMouseClicked

        crossover = (Recombination) operators.get(lstCrossover.getSelectedIndex()).getObject();
        crossover.setParameters("1.0");
        crossover.setSolver(solver);

        chartPanel = creatChart(individual);
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

    private void btMultipopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMultipopActionPerformed
        MultiPopulation pop = new MultiPopulation();

        pop.addIndividual(getRandom(), 2);
        pop.addIndividual(getRandom(), 4);

        solver.setParents(pop);
        crossover.setSolver(solver);

        chartPanel = creatChart(individual);
        panel.removeAll();
        panel.add(chartPanel);
        panel.setSize(new java.awt.Dimension(500, 500));
        panel.revalidate();
        panel.repaint();

    }//GEN-LAST:event_btMultipopActionPerformed

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


    private void btAddOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddOneActionPerformed
        solver.parents.addIndividual(getRandom(), 1 + solver.random.nextInt(COPIES));
        lstCrossoverMouseClicked(null);
    }//GEN-LAST:event_btAddOneActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ReserachRecombination().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddOne;
    private javax.swing.JButton btMultipop;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstCrossover;
    private javax.swing.JPanel panel;
    private javax.swing.JSlider sldCurrent;
    private javax.swing.JTextField txtCurrentValue;
    // End of variables declaration//GEN-END:variables
}
