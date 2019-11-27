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
package GUI;

import GUI.research.ReserachMutation;
import GUI.research.ReserachRecombination;
import GUI.setup.GeneticEventListener;
import GUI.setup.PopulationEventListener;
import GUI.utils.MuGASystem;
import com.evolutionary.Genetic;
import com.evolutionary.operator.GeneticOperator;
import com.evolutionary.population.Population;
import com.evolutionary.report.statistics.AbstractStatistics;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solverUtils.FileSolver;
import com.evolutionary.solver.GA;
import com.evolutionary.stopCriteria.StopCriteria;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author zulu
 */
public class SetupSolverFrm extends javax.swing.JFrame {

    EAsolver solver;

    /**
     * Creates new form SetupSolverFrm
     */
    public SetupSolverFrm() {
        initComponents();
        initMyComponents(new GA());
    }

    /**
     * Creates new form SetupSolverFrm
     */
    public SetupSolverFrm(EAsolver s) {
        initComponents();
        initMyComponents(s);
    }

    public void initMyComponents(EAsolver s) {
        solver = s;
        solver.InitializeEvolution(false);
        loadSolver(solver);
        txtPathSimulation.setText(MuGASystem.mugaPath);
        setupPop.setPopulation(solver.parents, solver.problem);

//----------------------------------------------------------------------------        
//Listener to solver
        setupSolver.load(MuGASystem.SOLVER, solver.getClass().getSimpleName());
        setupSolver.AddEventListener(new GeneticEventListener() {
            @Override
            public void onGeneticChange(Genetic source) {
                solver = (EAsolver) source;
                updateSolverInfo();
            }
        });
        //----------------------------------------------------------------------------        
//Listener to StopCriteria
        setupStopcriteria.load(MuGASystem.STOP_CRITERIA, solver.stop.getClass().getSimpleName());
        setupStopcriteria.AddEventListener(new GeneticEventListener() {
            @Override
            public void onGeneticChange(Genetic source) {
                solver.stop = (StopCriteria) source;
                updateSolverInfo();
            }
        });

//----------------------------------------------------------------------------        
//Listener to population
        setupSelection.load(MuGASystem.SELECTION, solver.selection.getClass().getSimpleName());
        setupPop.AddEventListener(new PopulationEventListener() {
            @Override
            public void onPopulationChange(Population source) {
                solver.setParents(source);
                updateSolverInfo();
            }
        });
//----------------------------------------------------------------------------        
//Listener to Recombination
        setupRecombination.load(MuGASystem.RECOMBINATION, solver.recombination.getClass().getSimpleName());
        setupRecombination.AddEventListener(new GeneticEventListener() {
            @Override
            public void onGeneticChange(Genetic source) {
                solver.recombination = (GeneticOperator) source;
                updateSolverInfo();
            }
        });
//----------------------------------------------------------------------------        
//Listener to Mutation
        setupMutation.load(MuGASystem.MUTATION, solver.mutation.getClass().getSimpleName());
        setupRecombination.AddEventListener(new GeneticEventListener() {
            @Override
            public void onGeneticChange(Genetic source) {
                solver.mutation = (GeneticOperator) source;
                updateSolverInfo();
            }
        });
        //----------------------------------------------------------------------------        
//Listener to Replacement
        setupReplacement.load(MuGASystem.REPLACEMENT, solver.replacement.getClass().getSimpleName());
        setupReplacement.AddEventListener(new GeneticEventListener() {
            @Override
            public void onGeneticChange(Genetic source) {
                solver.replacement = (GeneticOperator) source;
                updateSolverInfo();
            }
        });
        //----------------------------------------------------------------------------        
//Listener to Rescaling
        setupRescaling.load(MuGASystem.RESCALING, solver.rescaling.getClass().getSimpleName());
        setupRescaling.AddEventListener(new GeneticEventListener() {
            @Override
            public void onGeneticChange(Genetic source) {
                solver.rescaling = (GeneticOperator) source;
                updateSolverInfo();
            }
        });
        //----------------------------------------------------------------------------        
        setupStatistics.load(MuGASystem.STATISTICS, "");
    }

    public void updateSolverInfo() {
        txtEAsolver.setText(FileSolver.getConfigurationInfo(solver));
        txtEAsolver.setCaretPosition(0);
    }

    private void loadSolver(EAsolver s) {
        solver = s;
        txtEAsolver.setText(FileSolver.getConfigurationInfo(solver));
        txtEAsolver.setCaretPosition(0);
        setupSolver.setSelected(solver.getClass().getSimpleName());
        setupStopcriteria.setSelected(solver.stop.getClass().getSimpleName());
        setupSelection.setSelected(solver.selection.getClass().getSimpleName());
        setupRecombination.setSelected(solver.recombination.getClass().getSimpleName());
        setupMutation.setSelected(solver.mutation.getClass().getSimpleName());
        setupReplacement.setSelected(solver.replacement.getClass().getSimpleName());
        setupRescaling.setSelected(solver.rescaling.getClass().getSimpleName());
        setupPop.setPopulation(solver.parents, solver.problem);
        loadStatistics();
    }

    public void loadStatistics() {
        DefaultListModel model = new DefaultListModel();
        ArrayList<AbstractStatistics> stats = solver.report.getStatistics();
        for (AbstractStatistics stat : stats) {
            model.addElement(stat);
        }
        lstStatiscs.setModel(model);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        tbSimulation = new javax.swing.JPanel();
        pnSaveLoad = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPathSimulation = new javax.swing.JTextField();
        btSolverSaveSimulation = new javax.swing.JButton();
        btSolverOpen1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEAsolver = new javax.swing.JTextArea();
        pnSolver = new javax.swing.JPanel();
        setupSolver = new GUI.setup.GeneticParameter();
        setupStopcriteria = new GUI.setup.GeneticParameter();
        setupPop = new GUI.setup.PopulationParameters();
        setupSelection = new GUI.setup.GeneticParameter();
        setupRecombination = new GUI.setup.GeneticParameter();
        setupMutation = new GUI.setup.GeneticParameter();
        setupReplacement = new GUI.setup.GeneticParameter();
        setupRescaling = new GUI.setup.GeneticParameter();
        pnStatistics = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstStatiscs = new javax.swing.JList<>();
        setupStatistics = new GUI.setup.GeneticParameter();
        btAddStatistic = new javax.swing.JButton();
        btRemoveStats = new javax.swing.JButton();
        btAddStatistic1 = new javax.swing.JButton();
        btRemoveStatsAll = new javax.swing.JButton();
        pnResearch = new javax.swing.JPanel();
        btRealCodedRecombination = new javax.swing.JButton();
        btRealCodedMutation = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        tbSimulation.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("File name");

        txtPathSimulation.setText("jTextField1");

        btSolverSaveSimulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Save-32.png"))); // NOI18N
        btSolverSaveSimulation.setText("Save");
        btSolverSaveSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSolverSaveSimulationActionPerformed(evt);
            }
        });

        btSolverOpen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Open-32.png"))); // NOI18N
        btSolverOpen1.setText("open");
        btSolverOpen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSolverOpen1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnSaveLoadLayout = new javax.swing.GroupLayout(pnSaveLoad);
        pnSaveLoad.setLayout(pnSaveLoadLayout);
        pnSaveLoadLayout.setHorizontalGroup(
            pnSaveLoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSaveLoadLayout.createSequentialGroup()
                .addComponent(btSolverSaveSimulation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSolverOpen1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPathSimulation, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE))
        );
        pnSaveLoadLayout.setVerticalGroup(
            pnSaveLoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSaveLoadLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(pnSaveLoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPathSimulation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSolverSaveSimulation)
                    .addComponent(btSolverOpen1)))
        );

        tbSimulation.add(pnSaveLoad, java.awt.BorderLayout.PAGE_START);

        txtEAsolver.setColumns(20);
        txtEAsolver.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        txtEAsolver.setRows(5);
        jScrollPane1.setViewportView(txtEAsolver);

        tbSimulation.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Simulation", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Setup_Solver-16.png")), tbSimulation); // NOI18N

        setupSolver.setBorder(javax.swing.BorderFactory.createTitledBorder("Solver Type"));

        setupStopcriteria.setBorder(javax.swing.BorderFactory.createTitledBorder("Stop Criteria"));

        javax.swing.GroupLayout pnSolverLayout = new javax.swing.GroupLayout(pnSolver);
        pnSolver.setLayout(pnSolverLayout);
        pnSolverLayout.setHorizontalGroup(
            pnSolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(setupSolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(setupStopcriteria, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
        );
        pnSolverLayout.setVerticalGroup(
            pnSolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnSolverLayout.createSequentialGroup()
                .addComponent(setupStopcriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setupSolver, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Solver", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Setup-16.png")), pnSolver); // NOI18N
        jTabbedPane2.addTab("Problem", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Population-16.png")), setupPop); // NOI18N
        jTabbedPane2.addTab("Selection", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/DNA-16.png")), setupSelection); // NOI18N
        jTabbedPane2.addTab("Recombination", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/DNA-16.png")), setupRecombination); // NOI18N
        jTabbedPane2.addTab("Mutation", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/DNA-16.png")), setupMutation); // NOI18N
        jTabbedPane2.addTab("Replacement", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/DNA-16.png")), setupReplacement); // NOI18N
        jTabbedPane2.addTab("Rescaling", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/DNA-16.png")), setupRescaling); // NOI18N

        lstStatiscs.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Statistics"));
        lstStatiscs.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstStatiscs);

        btAddStatistic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/ArrowLeft-32.png"))); // NOI18N
        btAddStatistic.setText("Select Stat");
        btAddStatistic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddStatisticActionPerformed(evt);
            }
        });

        btRemoveStats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Delete-32.png"))); // NOI18N
        btRemoveStats.setText("Remove Stat");
        btRemoveStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveStatsActionPerformed(evt);
            }
        });

        btAddStatistic1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/ArrowLeft-32.png"))); // NOI18N
        btAddStatistic1.setText("Select All");
        btAddStatistic1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddStatistic1ActionPerformed(evt);
            }
        });

        btRemoveStatsAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Delete-32.png"))); // NOI18N
        btRemoveStatsAll.setText("Remove All");
        btRemoveStatsAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveStatsAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnStatisticsLayout = new javax.swing.GroupLayout(pnStatistics);
        pnStatistics.setLayout(pnStatisticsLayout);
        pnStatisticsLayout.setHorizontalGroup(
            pnStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStatisticsLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btRemoveStatsAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btRemoveStats, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(btAddStatistic1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btAddStatistic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setupStatistics, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
        );
        pnStatisticsLayout.setVerticalGroup(
            pnStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStatisticsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnStatisticsLayout.createSequentialGroup()
                        .addComponent(btAddStatistic)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btAddStatistic1)
                        .addGap(32, 32, 32)
                        .addComponent(btRemoveStats)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRemoveStatsAll)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addComponent(setupStatistics, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("Statistics", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Charts-16.png")), pnStatistics); // NOI18N

        btRealCodedRecombination.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Research-128.png"))); // NOI18N
        btRealCodedRecombination.setText("Real Coded Recombination");
        btRealCodedRecombination.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btRealCodedRecombination.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btRealCodedRecombination.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btRealCodedRecombination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRealCodedRecombinationActionPerformed(evt);
            }
        });

        btRealCodedMutation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Research-128.png"))); // NOI18N
        btRealCodedMutation.setText("Real Coded Mutation");
        btRealCodedMutation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btRealCodedMutation.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btRealCodedMutation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btRealCodedMutation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRealCodedMutationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnResearchLayout = new javax.swing.GroupLayout(pnResearch);
        pnResearch.setLayout(pnResearchLayout);
        pnResearchLayout.setHorizontalGroup(
            pnResearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnResearchLayout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(btRealCodedRecombination, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116)
                .addComponent(btRealCodedMutation, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(327, Short.MAX_VALUE))
        );
        pnResearchLayout.setVerticalGroup(
            pnResearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnResearchLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(pnResearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btRealCodedRecombination)
                    .addComponent(btRealCodedMutation))
                .addContainerGap(329, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Research", new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Research-16.png")), pnResearch); // NOI18N

        getContentPane().add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSolverSaveSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSolverSaveSimulationActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(txtPathSimulation.getText()));
        file.setFileFilter(new FileNameExtensionFilter("Muga Simulation Files", FileSolver.FILE_EXTENSION_CONFIG));
        int returnVal = file.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            PrintWriter out = null;
            try {
                String fileName = file.getSelectedFile().getPath();
                //insert file Extension if needed
                if (!fileName.endsWith(FileSolver.FILE_EXTENSION_CONFIG)) {
                    fileName = fileName + "." + FileSolver.FILE_EXTENSION_CONFIG;
                }
                out = new PrintWriter(fileName);
                out.println(txtEAsolver.getText());
                txtPathSimulation.setText(fileName);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SetupSolverFrm.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "ERROR : " + ex.getMessage());
            } finally {
                out.close();
            }
        }

    }//GEN-LAST:event_btSolverSaveSimulationActionPerformed

    private void btSolverOpen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSolverOpen1ActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(txtPathSimulation.getText()));
        file.setFileFilter(new FileNameExtensionFilter("Muga Simulation Files", FileSolver.FILE_EXTENSION_CONFIG));
        int returnVal = file.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            loadSolver(FileSolver.load(file.getSelectedFile().getPath()));
            JOptionPane.showMessageDialog(this, "Simulation loaded");
            txtPathSimulation.setText(file.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_btSolverOpen1ActionPerformed

    private void btAddStatisticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddStatisticActionPerformed
        solver.report.addStatistic(setupStatistics.genetic.getClass().getCanonicalName());
        loadStatistics();
        updateSolverInfo();
    }//GEN-LAST:event_btAddStatisticActionPerformed

    private void btRemoveStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveStatsActionPerformed
        if (lstStatiscs.getSelectedIndex() >= 0) {
            solver.report.removeStatistic(lstStatiscs.getSelectedIndex());
        }
        loadStatistics();
        updateSolverInfo();
    }//GEN-LAST:event_btRemoveStatsActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void btAddStatistic1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddStatistic1ActionPerformed
        List<Genetic> allStats = setupStatistics.getAllElements();
        for (Genetic stat : allStats) {
            solver.report.addStatistic(stat.getClass().getCanonicalName());
        }
        loadStatistics();
        updateSolverInfo();
    }//GEN-LAST:event_btAddStatistic1ActionPerformed

    private void btRemoveStatsAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveStatsAllActionPerformed
        solver.report.stats.clear();
        loadStatistics();
        updateSolverInfo();
    }//GEN-LAST:event_btRemoveStatsAllActionPerformed

    private void btRealCodedRecombinationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRealCodedRecombinationActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReserachRecombination().setVisible(true);
            }
        });
    }//GEN-LAST:event_btRealCodedRecombinationActionPerformed

    private void btRealCodedMutationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRealCodedMutationActionPerformed
         java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ReserachMutation().setVisible(true);
            }
        });
    }//GEN-LAST:event_btRealCodedMutationActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SetupSolverFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetupSolverFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetupSolverFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetupSolverFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetupSolverFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddStatistic;
    private javax.swing.JButton btAddStatistic1;
    private javax.swing.JButton btRealCodedMutation;
    private javax.swing.JButton btRealCodedRecombination;
    private javax.swing.JButton btRemoveStats;
    private javax.swing.JButton btRemoveStatsAll;
    private javax.swing.JButton btSolverOpen1;
    private javax.swing.JButton btSolverSaveSimulation;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JList<String> lstStatiscs;
    private javax.swing.JPanel pnResearch;
    private javax.swing.JPanel pnSaveLoad;
    private javax.swing.JPanel pnSolver;
    private javax.swing.JPanel pnStatistics;
    private GUI.setup.GeneticParameter setupMutation;
    private GUI.setup.PopulationParameters setupPop;
    private GUI.setup.GeneticParameter setupRecombination;
    private GUI.setup.GeneticParameter setupReplacement;
    private GUI.setup.GeneticParameter setupRescaling;
    private GUI.setup.GeneticParameter setupSelection;
    private GUI.setup.GeneticParameter setupSolver;
    private GUI.setup.GeneticParameter setupStatistics;
    private GUI.setup.GeneticParameter setupStopcriteria;
    private javax.swing.JPanel tbSimulation;
    private javax.swing.JTextArea txtEAsolver;
    private javax.swing.JTextField txtPathSimulation;
    // End of variables declaration//GEN-END:variables
}
