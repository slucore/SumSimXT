/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Point;
import java.awt.geom.Point2D;


public class Target {

    private Point point;
    private double speed;
    
    public Target(Point point, double speed) {
        this.point = point;
        this.speed = speed;
    }
    
    public Point getPoint() {
        return point;
    }
    
    public double getSpeed() {
        return speed;
    }
}
