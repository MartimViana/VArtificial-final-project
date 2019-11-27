//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2016   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package GUI.DisplayProblem;

import com.evolutionary.population.Population;
import com.evolutionary.problem.Individual;
import com.evolutionary.problem.real.RealVector;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;
import jv.geom.PgElementSet;
import jv.vecmath.PdVector;
import jv.viewer.PvDisplay;

/**
 * Created on 7/mar/2016, 16:38:37
 *
 * @author zulu - computer
 */
public class Graphics3Dfunc extends DisplayPopulation {

    protected PgElementSet space = null;
    protected PvDisplay disp = null;
    Individual individual;

    protected int numLines = 50;
    protected double normx = 1;
    protected double normy = 1;
    protected double normz = 1;
    protected double normi = 1;
    protected double MIN_VALUE = 0;

    public String toString() {
        return "Display 3D";
    }

    public boolean isValid(Individual ind) {
        return (ind instanceof RealVector)
                && ind.getNumGenes() == 2;
    }

    public double[] getValues(Individual ind) {

        if (ind instanceof RealVector) {
            return ((RealVector) ind).getGenome();
        }
        return null;
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
            List<Individual> individuals = pop.getGenomes();
            Collections.sort(individuals);
            Collections.reverse(individuals);
            //----------------------------------------------------------------
            computeSpace();
            disp.removeGeometries();
            disp.addGeometry(space);
            disp.selectGeometry(space);
            double[] values;
            for (Individual ind : individuals) {
                values = getValues(ind);
                PgElementSet elem = computeIndividual(values[0], values[1], ind.getFitness());
                disp.addGeometry(elem);
            }
            disp.repaint();
        } catch (Exception e) {
        }
    }

    public void computeSpace() {
        //same space computed
        if (space != null
                && individual != null
                && solver.problem.getClass().equals(individual.getClass())
                && solver.problem.getNumGenes() == individual.getNumGenes()) {
            return;
        }
        individual = solver.problem.getClone();
        disp = new PvDisplay();
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add((Component) disp, BorderLayout.CENTER);
        this.revalidate();

        int DIM = 5;
        space = new PgElementSet(3);
        // Allocate space for vertices.
        space.setNumVertices((numLines + DIM * 2) * (numLines + DIM * 2));
        // Compute the vertices.
        int ind = 0;
        double xMin = getMin(individual);
        double xMax = getMax(individual);
        double yMin = getMin(individual);
        double yMax = getMax(individual);
        double stepX = (xMax - xMin) / (numLines - 1);
        double stepy = (yMax - yMin) / (numLines - 1);
        double step = Math.max(stepX, stepy);
        normx = step / stepX;
        normy = step / stepy;
        double x = 0, y = 0, z = 0;
        computeNorms();
        individual.setSolver(null); // avaluations not counting
        double[] values = new double[2];
        for (int ny = -DIM; ny < numLines + DIM; ny++) {
            for (int nx = -DIM; nx < numLines + DIM; nx++) {
                x = xMin + nx * stepX;
                y = yMin + ny * stepy;
                if (ny >= 0 && ny < numLines && nx >= 0 && nx < numLines) {
                    values[0] = x;
                    values[1] = y;
                    setValues(individual, values);
                    individual.evaluate();
                    z = individual.getFitness();
                    space.setVertex(ind++, x * normx, y * normy, z * normz);
                } //borders
                else {
                    space.setVertex(ind++, x * normx, y * normy, MIN_VALUE * normz);
                }
            }
        }
        space.makeQuadrConn(numLines + DIM * 2, numLines + DIM * 2);
        space.makeElementColorsFromZHue();
        space.showElementColors(true);
        space.showEdges(true);
    }

    private double getMin(Individual ind) {
        
        if (ind instanceof RealVector) {
            return ((RealVector) ind).getMinValue();
        }
        return 0;
    }

    private double getMax(Individual ind) {
        
        if (ind instanceof RealVector) {
            return ((RealVector) ind).getMaxValue();
        }
        return 0;
    }

    private void setValues(Individual ind, double[] values) {
       
        if (ind instanceof RealVector) {
            ((RealVector) ind).setDoubleValues(values);
        }
    }

    public void computeNorms() {
        double xMin = getMin(individual);
        double xMax = getMax(individual);
        double yMin = getMin(individual);
        double yMax = getMax(individual);
        double stepX = (xMax - xMin) / (numLines - 1);
        double stepy = (yMax - yMin) / (numLines - 1);
        double zMin = Double.MAX_VALUE;
        double zMax = -Double.MAX_VALUE;
        double[] values = new double[2];
        double x = 0, y = 0, z = 0;
        for (int ny = 0; ny < numLines; ny++) {
            for (int nx = 0; nx < numLines; nx++) {
                values[0] = xMin + nx * stepX;
                values[1] = yMin + ny * stepy;
                setValues(individual, values);
                individual.evaluate();
                z = individual.getFitness();
                if (z < zMin) {
                    zMin = z;
                }
                if (z > zMax) {
                    zMax = z;
                }
            }
        }
        double step = Math.max(stepX, stepy);
        normx = step / stepX;
        normy = step / stepy;
        double stepZ = (zMax - zMin) / (numLines - 1);
        normz = step / (stepZ * 2);
        normi = step / 2;
        MIN_VALUE = zMin;

    }

    public PgElementSet computeIndividual(double x, double y, double z) {
        PgElementSet ind = new PgElementSet(3);
        ind.computeSphere(6, 6, normi * 2);
        PdVector v = new PdVector(x * normx, y * normy, z * normz);
        ind.translate(v);
//        ind.setGlobalEdgeColor(Color.black);
//        ind.setGlobalBndColor(Color.BLACK);
        ind.makeElementColorsFromZ();
        ind.showElementColors(true);
        ind.showEdges(true);
        return ind;
    }

    @Override
    protected void displayIndividual(Graphics gr, Individual ind, int px, int py, int sizex, int sizey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603071638L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
