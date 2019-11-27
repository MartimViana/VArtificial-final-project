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

import GUI.setup.SetupSolverDialog;
import GUI.solver.EvolutionEventListener;
import GUI.solver.UiSolver;
import GUI.statistics.StatisticsChartSimulation;
import GUI.statistics.StatisticsChartSolver;
import com.evolutionary.Genetic;
import com.evolutionary.report.ReportSolver;
import com.evolutionary.report.html.WWWSolverSaveReport;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;
import com.evolutionary.solverUtils.EAsolverArray;
import com.evolutionary.solverUtils.FileSimulation;

import com.evolutionary.solverUtils.FileSolver;
import com.utils.MyFile;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author zulu
 */
public class ExploreSolver extends javax.swing.JFrame {

    public static EAsolver defaultSolver = new GA();

    //main meno of this frame
    JFrame mainMenu;

    UiSolver setupSolver = new UiSolver(); // Configuration solver 
    UiSolver runningSolver = new UiSolver(); // running solver

    UIpopulation displayPop;
    StatisticsChartSolver statsPanel;

    JFileChooser fileChooser;

    /**
     * Creates new form ExploreSolver
     */
    public ExploreSolver(JFrame mainMenu) {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        this.setLocationRelativeTo(mainMenu);
        this.mainMenu = mainMenu;
        this.setTitle(Genetic.VERSION);
        initMyComponents();
    }

    public void initMyComponents() {
        pleaseWait.setVisible(false);
        displayPop = new UIpopulation();
        pnPopulation.add(displayPop, BorderLayout.CENTER);
        
        try {
            defaultSolver = FileSolver.loadDefaultSolver();
        } catch (Exception ex) {          
        }
        setSolver(defaultSolver);        
        initSolver();

        runningSolver.addListener(new EvolutionEventListener() {
            @Override
            public void onEvolutionChanges(EAsolver source) {
                displayPop.updatePopulation(source.parents);
                statsPanel.updateStats(source);
            }

            @Override
            public void onEvolutionComplete(EAsolver source) {
                //   source.updateEvolutionStats();
                onEvolutionChanges(source);
                addSimulationResultTXT(source);
                //source.report.save();
            }
        });
        File file = new File(MyFile.getFullFileName(txtPathSimulation.getText()));
        file.getParentFile().mkdirs();
        txtPathSimulation.setText(file.toString());
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(file);
        fileChooser.setSelectedFile(new File(txtPathSimulation.getText()));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Muga Evolution", FileSolver.FILE_EXTENSION_MUGA));
        //  fileChooser.setFileFilter(new FileNameExtensionFilter("Evolution Config", FileSolver.FILE_EXTENSION_CONFIG));

    }

    private void addSimulationResultTXT(EAsolver source) {
        String txt = source.report.getEvolutionString();
        JTextArea txtEvol = new JTextArea(txt);
        txtEvol.setFont(new java.awt.Font("Courier New", 0, 14));
        JScrollPane view = new JScrollPane(txtEvol);
        tpSolver.add("Solver Result", view);
        tpSolver.setSelectedComponent(view);
    }

    public void setSolver(EAsolver newSolver) {
        this.setupSolver.setMySolver(newSolver);

        displayPop.setSolver(setupSolver.getMySolver());
        txtSolver.setText(setupSolver.toString());
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        int selectedIndex = tpSolver.getSelectedIndex();
        // tpSolver.setSelectedComponent(pnPopulation);
        displayPop.setSolver(newSolver);
        tpSolver.removeAll();
        if (newSolver instanceof EAsolverArray) {
            statsPanel = new StatisticsChartSimulation(newSolver);
        } else {
            statsPanel = new StatisticsChartSolver(newSolver);
        }
        tpSolver.addTab("Setup", pnSetupSolver);
        tpSolver.addTab("Population", pnPopulation);
        int totalTabs = statsPanel.getTabs().getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            tpSolver.addTab(statsPanel.getTabs().getTitleAt(0), statsPanel.getTabs().getComponentAt(0));
        }
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: evolution result txt
        if (!newSolver.report.evolution.isEmpty()) {
            addSimulationResultTXT(newSolver);
        }

        if (selectedIndex < tpSolver.getTabCount()) {
            tpSolver.setSelectedIndex(selectedIndex);
        }
        this.revalidate();
        this.repaint();
        runningSolver.setMySolver(newSolver);
        //::::::::::::::::::::::::::::::::

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btMenu = new javax.swing.JButton();
        btIterateSolver = new javax.swing.JButton();
        sldVelocity = new javax.swing.JSlider();
        btInititalize = new javax.swing.JButton();
        btRun = new javax.swing.JToggleButton();
        pleaseWait = new javax.swing.JLabel();
        pnSolver_Pop = new javax.swing.JPanel();
        tpSolver = new javax.swing.JTabbedPane();
        pnSetupSolver = new javax.swing.JPanel();
        pnSimulationFiles = new javax.swing.JPanel();
        btOpen = new javax.swing.JButton();
        btEdit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSolver = new javax.swing.JTextArea();
        pnPopulation = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtPathSimulation = new javax.swing.JTextField();
        btSaveSimulation = new javax.swing.JButton();
        btSaveEvolution1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Main_Menu-32.png"))); // NOI18N
        btMenu.setText("Menu");
        btMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMenuActionPerformed(evt);
            }
        });

        btIterateSolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Iterate_32.png"))); // NOI18N
        btIterateSolver.setText("step by step");
        btIterateSolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btIterateSolverActionPerformed(evt);
            }
        });

        sldVelocity.setMaximum(10);
        sldVelocity.setBorder(javax.swing.BorderFactory.createTitledBorder("Velocity"));
        sldVelocity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldVelocityStateChanged(evt);
            }
        });

        btInititalize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Solver_start_32.png"))); // NOI18N
        btInititalize.setText("Initialize");
        btInititalize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInititalizeActionPerformed(evt);
            }
        });

        btRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Run_32.png"))); // NOI18N
        btRun.setText("Run");
        btRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRunActionPerformed(evt);
            }
        });

        pleaseWait.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        pleaseWait.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/loading2.gif"))); // NOI18N
        pleaseWait.setText("please wait");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(btInititalize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btIterateSolver, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRun, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sldVelocity, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 298, Short.MAX_VALUE)
                .addComponent(btMenu))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(610, 610, 610)
                    .addComponent(pleaseWait, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(sldVelocity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btMenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btRun, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 3, Short.MAX_VALUE))
            .addComponent(btIterateSolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btInititalize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pleaseWait, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        pnSolver_Pop.setLayout(new java.awt.BorderLayout());

        pnSetupSolver.setLayout(new java.awt.BorderLayout());

        btOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Open-24.png"))); // NOI18N
        btOpen.setText("Open Solver");
        btOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOpenActionPerformed(evt);
            }
        });

        btEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Setup_Solver-24.png"))); // NOI18N
        btEdit.setText("Edit Simulation");
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnSimulationFilesLayout = new javax.swing.GroupLayout(pnSimulationFiles);
        pnSimulationFiles.setLayout(pnSimulationFilesLayout);
        pnSimulationFilesLayout.setHorizontalGroup(
            pnSimulationFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSimulationFilesLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(btOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(489, 489, 489))
        );
        pnSimulationFilesLayout.setVerticalGroup(
            pnSimulationFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btOpen)
            .addComponent(btEdit)
        );

        pnSetupSolver.add(pnSimulationFiles, java.awt.BorderLayout.NORTH);

        txtSolver.setColumns(20);
        txtSolver.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        txtSolver.setRows(5);
        jScrollPane2.setViewportView(txtSolver);

        pnSetupSolver.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tpSolver.addTab("Solver", pnSetupSolver);

        pnPopulation.setLayout(new java.awt.BorderLayout());
        tpSolver.addTab("Population", pnPopulation);

        pnSolver_Pop.add(tpSolver, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnSolver_Pop, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        txtPathSimulation.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        txtPathSimulation.setText("evolutions/simulation.muga");
        jPanel4.add(txtPathSimulation, java.awt.BorderLayout.CENTER);

        btSaveSimulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Save-24.png"))); // NOI18N
        btSaveSimulation.setText("Save solver");
        btSaveSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveSimulationActionPerformed(evt);
            }
        });
        jPanel4.add(btSaveSimulation, java.awt.BorderLayout.LINE_START);

        btSaveEvolution1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Save-24.png"))); // NOI18N
        btSaveEvolution1.setText("Save HTML Evolution");
        btSaveEvolution1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveEvolution1ActionPerformed(evt);
            }
        });
        jPanel4.add(btSaveEvolution1, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMenuActionPerformed
        mainMenu.setLocationRelativeTo(this);
        this.dispose();
        mainMenu.setVisible(true);

    }//GEN-LAST:event_btMenuActionPerformed

    private void btOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOpenActionPerformed

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            Thread thr = new Thread(new Runnable() {
//                @Override
//                public void run() {
            pleaseWait.setVisible(true);
            pleaseWait.setText("loading " + fileChooser.getSelectedFile().getName());

            if (fileChooser.getSelectedFile().getPath().endsWith(FileSolver.FILE_EXTENSION_MUGA)) {
                try {
                    ArrayList<EAsolver> lst = FileSimulation.loadSimulation(fileChooser.getSelectedFile().getPath());
                    txtPathSimulation.setText(fileChooser.getSelectedFile().getPath());
                    setSolver(lst.get(0));
                } catch (Exception ex) {
                    txtSolver.setText(ex.getMessage());
                }
            }
            txtPathSimulation.setText(fileChooser.getSelectedFile().getAbsolutePath());
            pleaseWait.setVisible(false);
//                }
//            });
//            thr.start();
//            try {
//                thr.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(ExploreSolver.class.getName()).log(Level.SEVERE, null, ex);
//            }

        }

    }//GEN-LAST:event_btOpenActionPerformed


    private void btEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditActionPerformed
        EAsolver s = FileSolver.loadSolver(txtSolver.getText(), txtPathSimulation.getText());
        SetupSolverDialog setup = new SetupSolverDialog(s, this);
        setSolver(setup.solver);

    }//GEN-LAST:event_btEditActionPerformed


    private void btIterateSolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btIterateSolverActionPerformed
        btRun.setSelected(false);
        if (runningSolver.isRunning()) {
            runningSolver.stop();
        }
        runningSolver.iterate();
    }//GEN-LAST:event_btIterateSolverActionPerformed

    private void btInititalizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInititalizeActionPerformed
        runningSolver.stop();
        initSolver(); // create solver to run
        tpSolver.setSelectedIndex(1); // population
        btRun.setSelected(false);
        // runningSolver.start();
    }//GEN-LAST:event_btInititalizeActionPerformed

    private void btRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRunActionPerformed
        if (!btRun.isSelected()) {
            runningSolver.stop();
            sldVelocity.setEnabled(false);
        } else {
            runningSolver.setPath(txtPathSimulation.getText());
            runningSolver.start();
            sldVelocity.setEnabled(true);
            if (tpSolver.getSelectedIndex() == 0) {
                tpSolver.setSelectedIndex(1);
            }
        }

    }//GEN-LAST:event_btRunActionPerformed

    private void sldVelocityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldVelocityStateChanged
        runningSolver.setTimeToSleep((10 - sldVelocity.getValue()) * 100);
    }//GEN-LAST:event_sldVelocityStateChanged

    private void btSaveSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveSimulationActionPerformed

        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::
        String path = MyFile.getPath(txtPathSimulation.getText());
        String name = MyFile.getFileNameOnly(txtPathSimulation.getText());
        if (name.isEmpty()) {
            name = MyFile.getFileNameOnly(runningSolver.getMySolver().getSolverName());
        }
        name += "." + FileSolver.FILE_EXTENSION_CONFIG;
        File defaultFile = new File(path + name);
        defaultFile.getParentFile().mkdirs();
        fileChooser.setSelectedFile(defaultFile);
        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::

        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getAbsolutePath();
//            fileName = MyFile.changeFileNameExtension(fileName, FileSolver.FILE_EXTENSION_CONFIG);
//            runningSolver.getMySolver().report.save(fileName);
//            fileName = MyFile.changeFileNameExtension(fileName, FileSolver.FILE_EXTENSION_MUGA);
            runningSolver.getMySolver().save(fileName);
            txtPathSimulation.setText(fileName);
            txtSolver.setText(runningSolver.getMySolver().toString());
        }
    }//GEN-LAST:event_btSaveSimulationActionPerformed

    private void btSaveEvolution1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveEvolution1ActionPerformed
        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::
        String path = MyFile.getPath(txtPathSimulation.getText());
        String name = MyFile.getFileNameOnly(txtPathSimulation.getText());
        if (name.isEmpty()) {
            name = MyFile.getFileNameOnly(runningSolver.getMySolver().getSolverName());
        }
        name += "." + FileSolver.FILE_EXTENSION_CONFIG;
        File defaultFile = new File(path + name);
        defaultFile.getParentFile().mkdirs();
        fileChooser.setSelectedFile(defaultFile);
        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                WWWSolverSaveReport.save(runningSolver.getMySolver().report, fileChooser.getSelectedFile().getAbsolutePath(), this);
                txtSolver.setText(runningSolver.getMySolver().toString());
                txtPathSimulation.setText(fileChooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                Logger.getLogger(ExploreSolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btSaveEvolution1ActionPerformed

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
            java.util.logging.Logger.getLogger(ExploreSolver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExploreSolver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExploreSolver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExploreSolver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExploreSolver(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEdit;
    private javax.swing.JButton btInititalize;
    private javax.swing.JButton btIterateSolver;
    private javax.swing.JButton btMenu;
    private javax.swing.JButton btOpen;
    private javax.swing.JToggleButton btRun;
    private javax.swing.JButton btSaveEvolution1;
    private javax.swing.JButton btSaveSimulation;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel pleaseWait;
    private javax.swing.JPanel pnPopulation;
    private javax.swing.JPanel pnSetupSolver;
    private javax.swing.JPanel pnSimulationFiles;
    private javax.swing.JPanel pnSolver_Pop;
    private javax.swing.JSlider sldVelocity;
    private javax.swing.JTabbedPane tpSolver;
    private javax.swing.JTextField txtPathSimulation;
    private javax.swing.JTextArea txtSolver;
    // End of variables declaration//GEN-END:variables

    private void initSolver() {
        //load solver from textArea
        EAsolver solver = FileSolver.loadSolver(txtSolver.getText(), ReportSolver.DEFAULT_FILE_NAME);
        FileSolver.saveDefault(solver);

        int selectedIndex = tpSolver.getSelectedIndex();
        //initialize simulation
        solver.InitializeEvolution(false);
        //display population
        displayPop.setSolver(solver);
        //remove all tabs from GUI
        tpSolver.removeAll();
        //create new Tabs
        if (solver instanceof EAsolverArray) {
            statsPanel = new StatisticsChartSimulation(solver);
        } else {
            statsPanel = new StatisticsChartSolver(solver);
        }
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: SETUP and POPULATION
        tpSolver.addTab("Setup", pnSetupSolver);
        tpSolver.addTab("Population", pnPopulation);
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: STATISTICS
        int totalTabs = statsPanel.getTabs().getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            tpSolver.addTab(statsPanel.getTabs().getTitleAt(0), statsPanel.getTabs().getComponentAt(0));
        }

        if (selectedIndex < tpSolver.getTabCount()) {
            tpSolver.setSelectedIndex(selectedIndex);
        }
        txtSolver.setText(solver.toString());
        this.revalidate();
        this.repaint();
        runningSolver.setMySolver(solver);

    }

}
