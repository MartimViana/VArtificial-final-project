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

package GUI.solver;

import com.evolutionary.solver.EAsolver;
import com.evolutionary.solverUtils.EAsolverArray;
import com.utils.MyFile;
import com.utils.MyString;
import com.utils.MyUtils;
import java.util.Date;
import javax.swing.event.EventListenerList;

/**
 *
 * @author antoniomanso
 */
public class UiSolver implements Runnable {

    EAsolver mySolver; // solver / array of solvers
    private int timeToSleep; // thread sleep
    Thread autorun = null; // thread

    public UiSolver() {
        mySolver = null;
    }

    public UiSolver(EAsolver mySolver) {
        this.mySolver = mySolver;
        this.mySolver.InitializeEvolution(false);
    }

    public EAsolver getMySolver() {
        return mySolver;
    }

    public void setMySolver(EAsolver mySolver) {
        this.mySolver = mySolver;
    }

    public EventListenerList getListenerList() {
        return listeners;
    }

    public void setListenerList(EventListenerList ListenerList) {
        this.listeners = ListenerList;
    }

    public void start() {
        if (autorun == null && mySolver != null) {
            autorun = new Thread(this);
            autorun.start();
        } else {
            stop();
        }
    }
    public void setPath(String path){
        mySolver.report.setPath(MyFile.getPath(path));
    }

    public void stop() {
        if (autorun != null) {
            try {
                autorun.interrupt();
                //wait for interruption
//                synchronized (this) {
//                   autorun.wait();
//                }
            } catch (Exception e) {
            }
            autorun = null;
        }
    }

    public synchronized void iterate() {
        //solver not done
        if (!mySolver.isDone()) {
            //next generation
            if (mySolver instanceof EAsolverArray) {
                ((EAsolverArray) mySolver).iterateParallel();
            } else {
                mySolver.iterate();
            }
            //notify changes
            fireEvolutionChanges(mySolver);
            //solver done
            if (mySolver.isDone()) {
                //notify complete
                fireEvolutionComplete(mySolver);
            }
        }
    }

    @Override
    public String toString() {
        return mySolver.toString();
    }

    @Override
    public void run() {
        System.out.println(MyString.toString(new Date()) + " Solving " + mySolver.getSolverName() + " rnd= " + MyUtils.getSeed(mySolver.random));
        while (autorun != null && !mySolver.isDone()) {
            try {
                iterate();
                if (getTimeToSleep() > 0) {
                    Thread.sleep(getTimeToSleep());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println(MyString.toString(new Date()) + " Error UI solver " + mySolver.getSolverName() + " " + ex.getMessage());
                break;
                //return; // thread abborted 
            }

        }
        System.out.println(MyString.toString(new Date()) + " " + mySolver.getSolverName() + " Done !" + mySolver.isDone());
        if( mySolver.isDone()){
            mySolver.save();
        }
        //clean autorun
        autorun = null;
    }

    public boolean isRunning() {
        return autorun != null;
    }

    public boolean isStopped() {
        return mySolver == null || mySolver.stop.isDone(mySolver);
    }

    public boolean isDone() {
        return mySolver.stop.isDone(mySolver);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//:::::::::::::                E V E  N T   L I S T E N E R  ::::::::::::::::::
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
    /**
     * List of Listeners
     */
    protected EventListenerList listeners = new EventListenerList();

    /**
     * Add an event listener to the list.
     *
     * @param Listener the listener
     */
    public void addListener(EvolutionEventListener Listener) {
        listeners.add(EvolutionEventListener.class, Listener);
    }

    /**
     * Fire the event, tell everyone that something has happened.
     *
     * @param obj
     */
    public void fireEvolutionChanges(EAsolver obj) {
        Object[] Listeners = listeners.getListenerList();
        // Process each of the event listeners in turn.
        for (int i = 0; i < Listeners.length; i++) {
            if (Listeners[i] == EvolutionEventListener.class) {
                ((EvolutionEventListener) Listeners[i + 1]).onEvolutionChanges(obj);
            }
        }
    }

    /**
     * Fire the event, tell everyone that something has happened.
     *
     * @param obj
     */
    public void fireEvolutionComplete(EAsolver obj) {
        Object[] Listeners = listeners.getListenerList();
        // Process each of the event listeners in turn.
        for (int i = 0; i < Listeners.length; i++) {
            if (Listeners[i] == EvolutionEventListener.class) {
                try {
                    ((EvolutionEventListener) Listeners[i + 1]).onEvolutionComplete(obj);
                } catch (Throwable e) {
                    //e.printStackTrace();
                }

            }
        }
    }
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 

    /**
     * @return the timeToSleep
     */
    public int getTimeToSleep() {
        return timeToSleep;
    }

    /**
     * @param timeToSleep the timeToSleep to set
     */
    public void setTimeToSleep(int timeToSleep) {
        this.timeToSleep = timeToSleep;
    }

}
