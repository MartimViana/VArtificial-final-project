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
import GUI.solver.UIexperiment;
import GUI.statistics.StatisticsChartExperiment;
import com.evolutionary.Genetic;
import com.evolutionary.population.MultiPopulation;
import com.evolutionary.population.SimplePopulation;
import com.evolutionary.report.html.WwwExperimentReport;
import com.evolutionary.solver.EAsolver;
import com.evolutionary.solver.GA;

import com.evolutionary.solverUtils.EAExperiment;
import com.evolutionary.solverUtils.EAsolverArray;
import com.evolutionary.solverUtils.FileSimulation;
import com.evolutionary.solverUtils.FileSolver;
import com.utils.MyFile;
import com.utils.MyString;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author zulu
 */
public class RunSimulation extends javax.swing.JFrame {

    Thread autoRunTextMode = null;

    public static final String tab_RUN_TEXT_MODE = "Running Simulation -  Text Mode";
    public static final String tab_SETUP_EXPERIMENT = "Setup Experiment";

    //main menu of this frame
    JFrame mainMenu;

    EAExperiment simulationEdit = new EAExperiment(); // edit simulation    
    EAExperiment simulationRunning = new EAExperiment(); // simulation running

    UIexperiment uiSolver = new UIexperiment();

    StatisticsChartExperiment statsPanel; // statistics of running simulation

    int selectedSolverIndex = -1;
    JFileChooser fileChooserSimulation;
    JTextArea txtLogSolver = new JTextArea();

    /**
     * Creates new form ExploreSolver
     */
    public RunSimulation(JFrame mainMenu) {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        this.setLocationRelativeTo(mainMenu);
        this.mainMenu = mainMenu;
        this.setTitle(Genetic.VERSION);
        initMyComponents();
    }

    public void initMyComponents() {
        pleaseWait.setVisible(false);
        fileChooserSimulation = new JFileChooser();
        File file = new File(MyFile.getFullFileName(txtPathSimulation.getText()));
        file.getParentFile().mkdirs();
        txtPathSimulation.setText(file.toString());
        fileChooserSimulation.setCurrentDirectory(file);
        fileChooserSimulation.setSelectedFile(new File(txtPathSimulation.getText()));
        fileChooserSimulation.setFileFilter(new FileNameExtensionFilter("Muga Simulation Files", FileSolver.FILE_EXTENSION_MUGA));
        fileChooserSimulation.setMultiSelectionEnabled(true);

        DefaultListModel<String> model = new DefaultListModel<>();
        lstSolvers.setModel(model);
        btRun.setVisible(false);

        try {
            EAExperiment exp = FileSolver.loadDefaultExperiment();
            for (EAsolver solver : exp.solvers) {
                simulationEdit.solvers.add(solver);
            }
        } catch (Exception e) {
            EAsolver m = ExploreSolver.defaultSolver;
            m.parents = new MultiPopulation();
            //m.setSolverName("muGA");
            EAsolver s = new EAsolverArray(m);
            s.setSolverName("MultiPopulation");
            simulationEdit.solvers.add(s);
            //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            m.parents = new SimplePopulation();
            //m.setSolverName("muGA");
            s = new EAsolverArray(m);
            s.setSolverName("Population");
            simulationEdit.solvers.add(s);
        }
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        txtMultipleSolver.setText(simulationEdit.solvers.get(0).toString());
        pbRunSimulation.setVisible(false);
        tpSolver.removeAll();
        tpSolver.add(pnSetupSolver, tab_SETUP_EXPERIMENT);

        displaySolvers();
        lstSolvers.setSelectedIndex(simulationEdit.solvers.size() - 1);

        txtSolver.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int caracterPosition = txtSolver.getCaretPosition();
                    txtSolverFocusLost(null);
                    e.consume();
                    txtSolver.setCaretPosition(caracterPosition);

                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        tpSolver.setSelectedComponent(pnSetupSolver);
    }

    public void loadDefault() {

        ArrayList<EAsolver> sims = new ArrayList<>();
        for (File f : fileChooserSimulation.getSelectedFiles()) {
            try {
                sims.addAll(FileSimulation.loadSimulation(fileChooserSimulation.getSelectedFile().getPath()));
            } catch (Exception ex) {
                pleaseWait.setText(ex.getMessage());
                Logger.getLogger(RunSimulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (sims.size() > 0) {
            simulationEdit.solvers.addAll(sims);
            displaySolvers();
        }
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
        btStart = new javax.swing.JButton();
        btRun = new javax.swing.JToggleButton();
        pleaseWait = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtPathSimulation = new javax.swing.JTextField();
        btSaveSimulation = new javax.swing.JButton();
        btSaveEvolution = new javax.swing.JButton();
        pnSolver_Pop = new javax.swing.JPanel();
        tpSolver = new javax.swing.JTabbedPane();
        pnRunInTextMode = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        pbRunSimulation = new javax.swing.JProgressBar();
        pnSetupSolver = new javax.swing.JPanel();
        pnMultipleSolverEdit = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtMultipleSolver = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        btNew1 = new javax.swing.JButton();
        AddSimulation = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSolver = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSolvers = new javax.swing.JList<>();
        jPanel6 = new javax.swing.JPanel();
        btNew = new javax.swing.JButton();
        btOpen1 = new javax.swing.JButton();
        btEdit = new javax.swing.JButton();
        btClone = new javax.swing.JButton();
        btDeleteSolvers = new javax.swing.JButton();
        btSolveNoGraphics = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Main_Menu-32.png"))); // NOI18N
        btMenu.setText("Menu");
        btMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMenuActionPerformed(evt);
            }
        });

        btStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Solver_start_32.png"))); // NOI18N
        btStart.setText("Start Simulation");
        btStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStartActionPerformed(evt);
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
                .addContainerGap()
                .addComponent(btStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRun, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pleaseWait, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                .addComponent(btMenu))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btStart)
                    .addComponent(btRun)
                    .addComponent(btMenu)
                    .addComponent(pleaseWait))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        txtPathSimulation.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        txtPathSimulation.setText("evolutions/simulation.muga");
        jPanel3.add(txtPathSimulation, java.awt.BorderLayout.CENTER);

        btSaveSimulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Save-24.png"))); // NOI18N
        btSaveSimulation.setText("Save Solvers");
        btSaveSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveSimulationActionPerformed(evt);
            }
        });
        jPanel3.add(btSaveSimulation, java.awt.BorderLayout.LINE_START);

        btSaveEvolution.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Save-24.png"))); // NOI18N
        btSaveEvolution.setText("Save HTML Evolution");
        btSaveEvolution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveEvolutionActionPerformed(evt);
            }
        });
        jPanel3.add(btSaveEvolution, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pnSolver_Pop.setLayout(new java.awt.BorderLayout());

        pnRunInTextMode.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(pbRunSimulation, java.awt.BorderLayout.PAGE_END);

        pnRunInTextMode.add(jPanel7);

        tpSolver.addTab("MultipleSolver", pnRunInTextMode);

        pnSetupSolver.setLayout(new java.awt.BorderLayout());

        pnMultipleSolverEdit.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiple Solvers Definition"));
        pnMultipleSolverEdit.setPreferredSize(new java.awt.Dimension(550, 170));
        pnMultipleSolverEdit.setLayout(new java.awt.BorderLayout(5, 5));

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiple Solver Parameters"));

        txtMultipleSolver.setColumns(20);
        txtMultipleSolver.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        txtMultipleSolver.setRows(5);
        txtMultipleSolver.setPreferredSize(new java.awt.Dimension(300, 89));
        txtMultipleSolver.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMultipleSolverFocusLost(evt);
            }
        });
        jScrollPane4.setViewportView(txtMultipleSolver);

        pnMultipleSolverEdit.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        btNew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/DNA-32.png"))); // NOI18N
        btNew1.setText("Add Multiple Solvers");
        btNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNew1ActionPerformed(evt);
            }
        });
        jPanel4.add(btNew1);

        AddSimulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Open-24.png"))); // NOI18N
        AddSimulation.setText("Open Simulation");
        AddSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddSimulationActionPerformed(evt);
            }
        });
        jPanel4.add(AddSimulation);

        pnMultipleSolverEdit.add(jPanel4, java.awt.BorderLayout.NORTH);

        pnSetupSolver.add(pnMultipleSolverEdit, java.awt.BorderLayout.EAST);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Solvers of Simulation"));
        jPanel2.setLayout(new java.awt.BorderLayout(5, 5));

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Solver Definition"));

        txtSolver.setColumns(20);
        txtSolver.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        txtSolver.setRows(5);
        txtSolver.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSolverFocusLost(evt);
            }
        });
        jScrollPane3.setViewportView(txtSolver);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        lstSolvers.setBorder(javax.swing.BorderFactory.createTitledBorder("List Of Solvers"));
        lstSolvers.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstSolvers.setPreferredSize(new java.awt.Dimension(200, 80));
        lstSolvers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstSolversValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstSolvers);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.WEST);

        jPanel6.setLayout(new java.awt.GridLayout(1, 5, 5, 5));

        btNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/DNA-24.png"))); // NOI18N
        btNew.setText("New");
        btNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNewActionPerformed(evt);
            }
        });
        jPanel6.add(btNew);

        btOpen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/File_Open-24.png"))); // NOI18N
        btOpen1.setText("Open");
        btOpen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOpen1ActionPerformed(evt);
            }
        });
        jPanel6.add(btOpen1);

        btEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Setup_Solver-24.png"))); // NOI18N
        btEdit.setText("Edit");
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });
        jPanel6.add(btEdit);

        btClone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/reload_24.png"))); // NOI18N
        btClone.setText("Clone");
        btClone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloneActionPerformed(evt);
            }
        });
        jPanel6.add(btClone);

        btDeleteSolvers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Delete-24.png"))); // NOI18N
        btDeleteSolvers.setText("Delete");
        btDeleteSolvers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteSolversActionPerformed(evt);
            }
        });
        jPanel6.add(btDeleteSolvers);

        btSolveNoGraphics.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/Solver_start_32.png"))); // NOI18N
        btSolveNoGraphics.setText("solve (no graphics)");
        btSolveNoGraphics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSolveNoGraphicsActionPerformed(evt);
            }
        });
        jPanel6.add(btSolveNoGraphics);

        jPanel2.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        pnSetupSolver.add(jPanel2, java.awt.BorderLayout.CENTER);

        tpSolver.addTab("Experiment", pnSetupSolver);

        pnSolver_Pop.add(tpSolver, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnSolver_Pop, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMenuActionPerformed
        mainMenu.setLocationRelativeTo(this);
        this.dispose();
        mainMenu.setVisible(true);

    }//GEN-LAST:event_btMenuActionPerformed

    private void btNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewActionPerformed
        EAsolver m = new GA();
        EAsolver s = new EAsolverArray(m);
        simulationEdit.solvers.add(s);
        displaySolvers();
        lstSolvers.setSelectedIndex(simulationEdit.solvers.size() - 1);

    }//GEN-LAST:event_btNewActionPerformed

    public void updateSelectedSolver() {
        if (selectedSolverIndex >= 0) {
            EAsolver solver = FileSolver.loadSolver(txtSolver.getText(), "");
            if (solver != null) {
                simulationEdit.solvers.set(selectedSolverIndex, solver);
                displaySolvers();
            }

        }
    }

    private void displaySolvers() {

        //change the name of duplicated solvernames
        for (int i = 0; i < simulationEdit.solvers.size(); i++) {
            for (int j = i + 1; j < simulationEdit.solvers.size(); j++) {
                if (simulationEdit.solvers.get(i).getSolverName().equalsIgnoreCase(simulationEdit.solvers.get(j).getSolverName())) {
                    simulationEdit.solvers.get(j).setSolverName(simulationEdit.solvers.get(j).getSolverName() + "(" + j + ")");
                }
            }
        }

        DefaultListModel lst = new DefaultListModel();
        DefaultListModel old = (DefaultListModel) lstSolvers.getModel();
        boolean update = false;
        for (int i = 0; i < simulationEdit.solvers.size(); i++) {
            if (old.size() > i && !old.getElementAt(i).toString().equalsIgnoreCase(simulationEdit.solvers.get(i).getSolverName())) {
                update = true;
            }
            lst.addElement(simulationEdit.solvers.get(i).getSolverName());
        }
        if (update || lst.size() != old.size()) {
            lstSolvers.setModel(lst);
            lstSolvers.setSelectedIndex(selectedSolverIndex);
        }
    }


    private void btEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditActionPerformed
        EAsolver s = FileSolver.loadSolver(txtSolver.getText(), txtPathSimulation.getText());
        if (s == null) {
            return;
        }
        SetupSolverDialog setup = new SetupSolverDialog(s, this);
        if (setup.solver.numberOfRun < 2) {
            setup.solver.numberOfRun = 32;
        }
        simulationEdit.solvers.set(selectedSolverIndex, setup.solver);
        txtSolver.setText(setup.solver.toString());
        displaySolvers();

    }//GEN-LAST:event_btEditActionPerformed

    private void btDeleteSolversActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteSolversActionPerformed
        if (lstSolvers.getSelectedIndex() >= 0) {
            //------------------------------------------------------------------
            // get all selected solvers
            int[] select = lstSolvers.getSelectedIndices();
            ArrayList<EAsolver> remove = new ArrayList<>();
            for (int i = 0; i < select.length; i++) {
                remove.add(simulationEdit.solvers.get(select[i]));
            }
            //------------------------------------------------------------------
            //remover selected
            simulationEdit.solvers.removeAll(remove);
            txtSolver.setText("");
            if (simulationEdit.solvers.size() > selectedSolverIndex) {
                displaySolvers();
            } else {
                selectedSolverIndex--;
                displaySolvers();
            }
        }
    }//GEN-LAST:event_btDeleteSolversActionPerformed


    private void btStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStartActionPerformed
        if (simulationEdit.solvers.isEmpty()) {
            return;
        }
        pleaseWait.setVisible(true);
        pleaseWait.setText("Starting simulation");

        uiSolver.stopSolvers();
        //Save default
        FileSolver.saveDefault(simulationEdit);

        simulationRunning = simulationEdit.getClone();
        simulationRunning.setFileName(txtPathSimulation.getText());
        //initialize the simulationEdit evolution 
        simulationRunning.initializeEvolution();

        btRun.setVisible(true);
        btStart.setVisible(false);

        uiSolver.initExperiment(simulationRunning.solvers);
        initStatisticsPanels(); //create statistics panels    
        txtLogSolver.append("\n\n\n" + MyString.toString(new Date()) + " :\t Simulation Started\n");
        for (int i = 0; i < simulationRunning.solvers.size(); i++) {
            uiSolver.addListener(i, new EvolutionEventListener() {
                @Override
                public void onEvolutionChanges(EAsolver source) {
                    statsPanel.updateStats(source);
                    pleaseWait.setText(String.format("Generation [%5d] Evaluations [%7d] %s ", source.numGeneration, source.numEvaluations, source.getSolverName()));
                }

                @Override
                public void onEvolutionComplete(EAsolver source) {
                    try {
                        txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t " + source.getSolverName() + " COMPLETED !!" + "\n");

                        txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t "
                                + source.getSolverName() + "Save  Object ... \t" + source.report.path);
                        source.save(source.report.path + "/" + source.getSolverName());

                        txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t "
                                + source.getSolverName() + "Save  Object ... \t" + source.report.path + " DONE !!!\n");

                    } catch (Exception ex) {
                    }

                    txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t " + source.getSolverName() + "Save Text Report ... \t"
                            + source.report.getFileName());

                    txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t " + source.getSolverName() + "Save Text Report ... \t"
                            + source.report.getFileName() + " DONE!!!");

                    if (simulationRunning.isDone()) { // all solvers are done

                        simulationRunning.saveObjectExperiment(txtPathSimulation.getText());

                        displayResults();

                        txtLogSolver.append("\n\n" + MyString.toString(new Date()) + " :\t " + " ALL DONE !!!\n");
                        pleaseWait.setVisible(false);
                    }
                }
            });
        }
        uiSolver.startSolvers();
        btRun.setSelected(true);
        btRun.setText("Stop");
    }//GEN-LAST:event_btStartActionPerformed

    private void displayResults() {
        pleaseWait.setText("Done!");

        simulationRunning.isEvolutionCompletedFlag = true;
        btRun.setVisible(false);
        btStart.setVisible(true);

        JTabbedPane result = simulationRunning.getBoxAndWhiskerTabs();
        //display final result
        tpSolver.addTab("Result", result);
        tpSolver.setSelectedComponent(result);

        JTabbedPane rank = simulationRunning.getRankTabs();
        //display final result
        tpSolver.addTab("Rank", rank);
        tpSolver.add(pnSetupSolver, tab_SETUP_EXPERIMENT, 0);
        pleaseWait.setVisible(false);
    }

    private void initStatisticsPanels() {
        //remove all tabs from GUI
        tpSolver.removeAll();
        //create new Tabs

        statsPanel = new StatisticsChartExperiment(simulationRunning.solvers);
        statsPanel.resetDataSets();

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: SETUP
//        tpSolver.addTab("Multiple Solver", pnRunInTextMode);
//        tpSolver.addTab("Experiment", pnSetupSolver);
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: STATISTICS
        JTabbedPane tpStats = new JTabbedPane();
        txtLogSolver = new JTextArea(simulationRunning.getSolversInfo());
        txtLogSolver.setFont(new Font("courier new", Font.PLAIN, 12));
        tpStats.addTab("Simulation ", new JScrollPane(txtLogSolver));

        int totalTabs = statsPanel.getTabs().getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            tpStats.addTab(statsPanel.getTabs().getTitleAt(0), statsPanel.getTabs().getComponentAt(0));
        }

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: STATISTICS
        tpSolver.addTab("Evolution", tpStats);
        tpSolver.setSelectedComponent(tpStats);
        tpStats.setSelectedIndex(1); // 0-config  1-statistics

        this.revalidate();
        this.repaint();
    }

    private void btRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRunActionPerformed
        try {
            if (autoRunTextMode != null) {
                autoRunTextMode.interrupt();
                autoRunTextMode = null;
            } else {
                uiSolver.stopSolvers();
                pleaseWait.setVisible(false);
                btRun.setVisible(false);
                btStart.setVisible(true);
                tpSolver.add(pnSetupSolver, tab_SETUP_EXPERIMENT, 0);
                tpSolver.setSelectedIndex(0);

            }
        } catch (Exception e) {
        }
        //displayResults();


    }//GEN-LAST:event_btRunActionPerformed

    private void btOpen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOpen1ActionPerformed

        int returnVal = fileChooserSimulation.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            pleaseWait.setVisible(true);
            pleaseWait.setText("loading " + fileChooserSimulation.getSelectedFile().getPath());

            ArrayList<EAsolver> sims = new ArrayList<>();
            for (File f : fileChooserSimulation.getSelectedFiles()) {
                try {
                    sims.addAll(FileSimulation.loadSimulation(fileChooserSimulation.getSelectedFile().getPath()));
                } catch (Exception ex) {
                    pleaseWait.setText(ex.getMessage());
                    Logger.getLogger(RunSimulation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (sims.size() > 0) {
                simulationEdit.solvers.addAll(sims);
                displaySolvers();
            }
        }
        pleaseWait.setVisible(false);
    }//GEN-LAST:event_btOpen1ActionPerformed

    private void btCloneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloneActionPerformed
        if (lstSolvers.getSelectedIndex() >= 0) {
            EAsolver s = FileSolver.loadSolver(txtSolver.getText(), txtPathSimulation.getText());
            if (s == null) {
                return;
            }
            simulationEdit.solvers.add(s);
            displaySolvers();
            lstSolvers.setSelectedIndex(simulationEdit.solvers.size() - 1);
        }


    }//GEN-LAST:event_btCloneActionPerformed

    private void lstSolversValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstSolversValueChanged
        updateSelectedSolver();
        if (lstSolvers.getSelectedIndex() >= 0) {
            txtSolver.setText(simulationEdit.solvers.get(lstSolvers.getSelectedIndex()).toString());
            selectedSolverIndex = lstSolvers.getSelectedIndex();
        } else if (selectedSolverIndex >= 0) {
            txtSolver.setText(simulationEdit.solvers.get(selectedSolverIndex).toString());
            lstSolvers.setSelectedIndex(selectedSolverIndex);
        } else {
            txtSolver.setText("");
            selectedSolverIndex = -1;
        }
    }//GEN-LAST:event_lstSolversValueChanged

    private void txtSolverFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSolverFocusLost
        updateSelectedSolver();
    }//GEN-LAST:event_txtSolverFocusLost

    private void btSaveSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveSimulationActionPerformed

        JFileChooser fileChooser = new JFileChooser();
        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::
        String path = MyFile.getPath(txtPathSimulation.getText());
        String name = MyFile.getFileNameOnly(txtPathSimulation.getText());
        if (name.isEmpty()) {
            name = MyFile.getFileNameOnly(simulationRunning.getFileName());
        }
        name += "." + FileSolver.FILE_EXTENSION_CONFIG;
        File defaultFile = new File(path + name);
        defaultFile.getParentFile().mkdirs();
        fileChooser.setSelectedFile(defaultFile);
        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::
        fileChooser.setFileFilter(new FileNameExtensionFilter("Muga Simulation Files", FileSolver.FILE_EXTENSION_CONFIG));
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            simulationRunning.saveAs(fileChooser.getSelectedFile().getAbsolutePath());
            txtPathSimulation.setText(MyFile.getPath(fileChooser.getSelectedFile().getAbsolutePath()));
        }


    }//GEN-LAST:event_btSaveSimulationActionPerformed

    private void btNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNew1ActionPerformed
        ArrayList<EAsolver> sims = FileSimulation.loadtxtSimulation(txtMultipleSolver.getText(), txtPathSimulation.getText());
        for (EAsolver sim : sims) {
            simulationEdit.solvers.add(sim);
        }
        displaySolvers();
        tpSolver.setSelectedComponent(pnSetupSolver);
        MyFile.saveToFile(txtMultipleSolver.getText(),
                MyFile.getPath(txtPathSimulation.getText())
                + File.separatorChar + "multi_" + MyFile.getFileName(txtPathSimulation.getText()));
    }//GEN-LAST:event_btNew1ActionPerformed

    private void txtMultipleSolverFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMultipleSolverFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMultipleSolverFocusLost

    public void updateRunSimulation(EAsolver solver, String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (solver != null) {
                    txtLogSolver.append(MyString.toString(new Date()) + " :\t " + solver.getSolverName() + " \t" + msg + "\n");
                    pbRunSimulation.setValue(simulationRunning.solvers.indexOf(solver));
                } else {
                    txtLogSolver.append(MyString.toString(new Date()) + " :\t " + msg + "\n");
                }

            }
        });
    }

    private void btSaveEvolutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveEvolutionActionPerformed
        JFileChooser file = new JFileChooser(txtPathSimulation.getText());
        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::
        String path = MyFile.getPath(txtPathSimulation.getText());
        String name = MyFile.getFileNameOnly(txtPathSimulation.getText());
        if (name.isEmpty()) {
            name = MyFile.getFileNameOnly(simulationRunning.getFileName());
        }
        name += "." + FileSolver.FILE_EXTENSION_MUGA;
        File defaultFile = new File(path + name);
        defaultFile.getParentFile().mkdirs();
        file.setSelectedFile(defaultFile);
        //:::::::::: FILE NAME ::::::::::::::::::::::::::::::::::::::::::::::::

        file.setFileFilter(new FileNameExtensionFilter("Muga Evolution", FileSolver.FILE_EXTENSION_MUGA));
        int returnVal = file.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                WwwExperimentReport.save(simulationRunning, file.getSelectedFile().getAbsolutePath(), this);
                txtPathSimulation.setText(MyFile.getPath(file.getSelectedFile().getAbsolutePath()));
            } catch (Exception ex) {
                Logger.getLogger(RunSimulation.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btSaveEvolutionActionPerformed

    private void AddSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSimulationActionPerformed
        int returnVal = fileChooserSimulation.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            (new Thread(() -> {
                //:::::::::::::::::::::::::::::::: SINGLE or ARRAY SOLVER :::::::::::::::::::             
                try {
                    pleaseWait.setVisible(true);
                    pleaseWait.setText("loading " + fileChooserSimulation.getSelectedFile().getAbsolutePath());
                    EAsolver ea = EAsolver.load(fileChooserSimulation.getSelectedFile().getAbsolutePath());
                    simulationEdit.solvers.add(ea);
                } catch (Exception e) {
                    try {
                        //:::::::::::::::::::::::::::::::: EXPERIMENT SOLVER :::::::::::::::::::
                        EAExperiment ex = new EAExperiment();
                        ex.loadObjectExperiment(fileChooserSimulation.getSelectedFile().getAbsolutePath());
                        for (EAsolver ea : ex.solvers) {
                            simulationEdit.solvers.add(ea);
                        }
                    } catch (Exception ex1) {
                        JOptionPane.showMessageDialog(this, "Evolution not found");
                        pleaseWait.setVisible(false);
                        return;
                    }

                }
                pleaseWait.setText("Display Solvers " + fileChooserSimulation.getSelectedFile().getAbsolutePath());
                displaySolvers();
                tpSolver.setSelectedComponent(pnSetupSolver);
                //:::: hide icon
                pleaseWait.setVisible(false);
            })).start();

        }

    }//GEN-LAST:event_AddSimulationActionPerformed

    private void updateGui(String txt) {
        SwingUtilities.invokeLater(() -> {
            pleaseWait.setText(txt);
            String msg = MyString.toString(new Date()) + "\t " + txt;
            txtLogSolver.append("\n" + msg);

            txtLogSolver.setCaretPosition(txtLogSolver.getText().length());

        });
    }


    private void btSolveNoGraphicsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSolveNoGraphicsActionPerformed
        if (simulationEdit.solvers.isEmpty()) {
            return;
        }
        pleaseWait.setVisible(true);
        pleaseWait.setText("Starting simulation");

        uiSolver.stopSolvers();

        simulationRunning = simulationEdit.getClone();
        simulationRunning.setFileName(txtPathSimulation.getText());
        //initialize the simulationEdit evolution 
        simulationRunning.initializeEvolution();

        btRun.setVisible(true);
        btStart.setVisible(false);

        uiSolver.initExperiment(simulationRunning.solvers);

        //remove all tabs from GUI
        tpSolver.removeAll();
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: SETUP
        JTabbedPane tpStats = new JTabbedPane();
        txtLogSolver = new JTextArea(simulationRunning.getSolversInfo());
        txtLogSolver.setFont(new Font("courier new", Font.PLAIN, 12));
        tpStats.addTab("Simulation ", new JScrollPane(txtLogSolver));
        txtLogSolver.append("\n\n\n" + MyString.toString(new Date()) + " :\t Simulation Started\n");
        tpSolver.addTab("Evolution", tpStats);
        tpSolver.setSelectedComponent(tpStats);
        tpStats.setSelectedIndex(0);
        DecimalFormat myFormatter = new DecimalFormat("###,###,###");
        (new Thread(() -> {
            for (int i = 0; i < simulationRunning.solvers.size(); i++) {
                uiSolver.addListener(i, new EvolutionEventListener() {
                    @Override
                    public void onEvolutionChanges(EAsolver source) {
                        if (source.numGeneration < 10
                                || (source.numGeneration < 100 && source.numGeneration % 10 == 0)
                                || source.numGeneration % 100 == 0) {
                            updateGui(String.format("%-20s Evaluations %15s Generation %9d  Completed %10.1f %%",
                                    source.getSolverName(),
                                    myFormatter.format(source.numEvaluations), source.numGeneration, source.stop.completedRatio(source) * 100));
                        }
                    }

                    @Override
                    public void onEvolutionComplete(EAsolver source) {
                        try {
                            txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t " + source.getSolverName() + " COMPLETED !!" + "\n");

                            txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t "
                                    + source.getSolverName() + "Save  Object ... \t" + source.report.path);

                            source.report.saveObject(source.report.path + "/" + source.getSolverName());

                            txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t "
                                    + source.getSolverName() + "Save  Object ... \t" + source.report.path + " DONE !!!\n");

                        } catch (Exception ex) {
                        }

                        txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t " + source.getSolverName() + "Save Text Report ... \t"
                                + source.report.getFileName());

                        EAExperiment.saveEvolutionSolver(source);
                        txtLogSolver.append("\n" + MyString.toString(new Date()) + " :\t " + source.getSolverName() + "Save Text Report ... \t"
                                + source.report.getFileName() + " DONE!!!");

                        if (simulationRunning.isDone()) { // all solvers are done      
                            displayResults();
                            statsPanel = new StatisticsChartExperiment(simulationRunning.solvers);
                            tpStats.addTab("Statistics", statsPanel.getTabs());
                            simulationRunning.saveObjectExperiment(txtPathSimulation.getText());
                            txtLogSolver.append("\n\n" + MyString.toString(new Date()) + " :\t " + " ALL DONE !!!\n");
                            pleaseWait.setVisible(false);
                        }
                    }
                });
            }

        })).start();

        this.revalidate();
        uiSolver.startSolvers();
        btRun.setSelected(true);
        btRun.setText("Stop");
    }//GEN-LAST:event_btSolveNoGraphicsActionPerformed

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
            java.util.logging.Logger.getLogger(RunSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RunSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RunSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RunSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RunSimulation(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddSimulation;
    private javax.swing.JButton btClone;
    private javax.swing.JButton btDeleteSolvers;
    private javax.swing.JButton btEdit;
    private javax.swing.JButton btMenu;
    private javax.swing.JButton btNew;
    private javax.swing.JButton btNew1;
    private javax.swing.JButton btOpen1;
    private javax.swing.JToggleButton btRun;
    private javax.swing.JButton btSaveEvolution;
    private javax.swing.JButton btSaveSimulation;
    private javax.swing.JButton btSolveNoGraphics;
    private javax.swing.JButton btStart;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> lstSolvers;
    private javax.swing.JProgressBar pbRunSimulation;
    private javax.swing.JLabel pleaseWait;
    private javax.swing.JPanel pnMultipleSolverEdit;
    private javax.swing.JPanel pnRunInTextMode;
    private javax.swing.JPanel pnSetupSolver;
    private javax.swing.JPanel pnSolver_Pop;
    private javax.swing.JTabbedPane tpSolver;
    private javax.swing.JTextArea txtMultipleSolver;
    private javax.swing.JTextField txtPathSimulation;
    private javax.swing.JTextArea txtSolver;
    // End of variables declaration//GEN-END:variables

}
