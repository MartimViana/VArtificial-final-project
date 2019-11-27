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

package GUI.DisplayProblem;

import GUI.utils.MuGASystem;
import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.solver.EAsolver;
import com.utils.MyNumber;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;

/**
 *
 * @author arm
 */
public abstract class DisplayPopulation extends JPanel {

    EAsolver solver;
    Population pop = null; //population of display

    public abstract boolean isValid(Individual ind);

    protected abstract void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey);

    public DisplayPopulation() {
        setDoubleBuffered(true);
    }

    public void setSolver(EAsolver solver) {
        this.solver = solver;
        setPopulation(solver.parents);
    }

    public void setPopulation(Population pop) {
        this.pop = pop;
        repaint();
    }

    /**
     * display population of the solver
     *
     * @param pop
     * @param gr
     * @param bounds
     */
    public void showPopulation(Population pop, Graphics gr, Rectangle bounds) {
        try {
            this.pop = pop;
//            if (solver instanceof Islands) {
//                GraphicsRIGA.showPopulation((Islands) solver, this, gr, bounds);
//            } else {
                display(pop, gr, bounds);
//            }

        } catch (Exception e) {
        }
    }

    public void display(Population pop, Graphics gr, Rectangle bounds) {
        List<Individual> individuals = new ArrayList<>(pop.getGenomes());
        Collections.sort(individuals);
        Collections.reverse(individuals);
        //----------------------------------------------------------------
        //clean area
        gr.setColor(Color.LIGHT_GRAY);
        gr.fill3DRect(bounds.x, bounds.y, bounds.width, bounds.height, true);
        //----------------------------------------------------------------
        //dimensions of Line of bits
        double dimY = (double) bounds.height / (pop.getPopulationSize() + 0);
        bounds.y += dimY / 8;
        int i = 0;
        int yPosition = 0;
        for (Individual ind : individuals) {
            displayIndividual(gr, ind,
                    bounds.x,
                    bounds.y + (int) (dimY * i++),
                    bounds.width,
                    (int) dimY);
        }
    }

    public void showEmpty(Graphics gr, Rectangle bounds) {
        gr.setColor(new Color(120, 120, 120));
        //gr.clearRect(0, 0, this.getWidth(), this.getHeight());
        gr.fill3DRect(bounds.x, bounds.y, bounds.width, bounds.height, true);
        gr.setFont(new java.awt.Font("Courier", 0, 18));
        gr.setColor(Color.WHITE);
        gr.drawString("Empty Population", bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        return;
    }

    protected void show(Graphics gr, Rectangle bounds) {
        try {
            //empty population
            if (pop == null || pop.isEmpty()) {
                showEmpty(gr, bounds);
                return;
            }
            //genotype too long
            if (pop.getTemplate().getNumGenes() > bounds.width) {
                gr.setFont(new java.awt.Font("Courier", 0, 18));
                gr.setColor(Color.WHITE);
                gr.drawString("Too long genotype!",
                        bounds.x + bounds.width / 2,
                        bounds.y + bounds.height / 2);
                gr.setColor(Color.WHITE);
                return;
            }
            bounds.y = 0;
            showPopulation(pop, gr, bounds);
            //show copyright
            drawInformation(gr, bounds.width - 240, bounds.height - 140);
        } catch (Exception e) {
            System.err.println("ERROR IN DISPALY " + this.getClass().getName() + " show(Graphics gr, Rectangle bounds)");
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (g != null && pop != null) {
            show(g, getBounds());
        }
    }

    protected void drawInformation(Graphics gr, int px, int py) {

//        px = this.getWidth() - 100;
//        py = this.getHeight() - 20;
        gr.setFont(new java.awt.Font("Courier new", Font.BOLD, 20));
        gr.setColor(new Color(0.1f, 0.1f, 0.1f, 0.750f));
        gr.fillRoundRect(px, py, 230, 120, 20, 20);
        gr.setColor(Color.WHITE);        
        gr.drawString("Gener.: " + NumberFormat.getNumberInstance(Locale.UK).format(solver.numGeneration), px + 10, py + 20);
        gr.drawString("Evals.: " + NumberFormat.getNumberInstance(Locale.UK).format(solver.numEvaluations), px + 10, py + 50);
        //gr.drawString("Best " + solver.hallOfFame.iterator().next().getFitness(), 40, py - 30);
       String val =  MyNumber.numberToString(solver.hallOfFame.iterator().next().getFitness(), 10);
        gr.drawString("Best  : " + String.format("%s", val.trim()), px + 10, py + 80);
        gr.setFont(new java.awt.Font("Courier new", Font.BOLD, 12));
         gr.setColor(Color.BLACK);
        gr.drawString(MuGASystem.copyright, px + 135 + 2, py + 110 + 2);
        gr.setColor(Color.LIGHT_GRAY);
        gr.drawString(MuGASystem.copyright, px + 135, py + 110);
    }

    ///////////////////////////////////////////////////////////////////////////
    //------------------    C O L O R S  ------------------------------------//
    ///////////////////////////////////////////////////////////////////////////
    protected static Color getColor(boolean cValue) {
        if (cValue) {
            return Color.BLACK;
        }
        return Color.WHITE;
    }

    protected static Color getDeceptive(boolean cValue) {
        if (cValue) {
            return new Color(1.0f, 0.0f, 0.0f, 1.0f);
        }
        return new Color(1.0f, 0.8f, 0.8f, 1.0f);
    }

    protected static Color getBestColor(boolean cValue) {
        if (cValue) {
            return new Color(0, 255, 0);
        }
        return new Color(200, 255, 200);
    }

    protected static Color getHSBColor(boolean bit, float hue) {
        if (bit) {
            return Color.getHSBColor(hue, 1.0f, 1.0f);
        }
        return Color.getHSBColor(hue, 0.2f, 1.0f);
    }

    public void drawbit(Graphics gr, int x1, int y1, int sizex, int sizey) {
        int round = 6;
        gr.fillRoundRect(x1, y1, sizex, sizey, sizex / round, sizex / round);
    }

    public void drawBestBit(boolean bit, Graphics gr, int x1, int y1, int sizex, int sizey) {        
        drawBlock(gr, getBestColor(bit), x1, y1, sizex, sizey);
    }

    public void drawBit(boolean bit, Graphics gr, int x1, int y1, int sizex, int sizey) {
        drawBlock(gr, getColor(bit), x1, y1, sizex, sizey);
    }

    public String toString() {
        return getClass().getSimpleName();
    }
    
     public void drawBlock(Graphics gr, Color color,int x1, int y1, int sizex, int sizey) {
        gr.setColor(color);
        gr.fillOval(x1, y1, sizex, sizey);
        gr.setColor(Color.BLACK);
        gr.drawOval(x1, y1, sizex, sizey);
    }

    public static DefaultListModel getGraphicsModel(Population pop) {
        DefaultListModel model = new DefaultListModel();
        if (pop == null || pop.isEmpty()) {
            return model;
        }
        Individual ind = pop.getTemplate();
        ArrayList display = MuGASystem.getObjects(MuGASystem.GRAPHICS);

        for (Object object : display) {
            try {// try build display
                if (((DisplayPopulation) object).isValid(ind)) {
                    model.addElement(object);
                }
            } catch (Exception e) {
                //display not avaiable
            }
        }
        return model;
    }
}
