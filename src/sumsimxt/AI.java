/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author slucore
 */
public class AI {
    
    private List<Target> targets = new ArrayList<>();
    private boolean scriptFinished = false;
    
    private int index;
    
    public AI(List<Target> targets) {
        this.targets = targets;
        index = 0;
    }
    
    public void signalArrived() {
        if (index < targets.size() - 1) {
            index++;
        } else {
            scriptFinished = true;
        }
    }
    
    public boolean scriptFinished() {
        return scriptFinished;
    }
    
    public Target getTarget() {
        return targets.get(index);
    }
        
    public enum BasicAIs {
        BACK_AND_FORTH  (),
        ACROSS_LEFT     (),
        ACROSS_RIGHT    (),
        CHARGE          ();
        
    }
    
}
