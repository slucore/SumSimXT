/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author slucore
 */
public class GameObject {
    
    private Sprite sprite;
    private Point point;
    private Dimension size;
    private boolean isAlive = true;
    
    public GameObject(Sprite sprite, Point point, Dimension size) {
        this.sprite = sprite;
        this.point = point;
        this.size = size;
    }
    
    public int getWidth() {
        return size.width;
    }
    public int getHeight() {
        return size.height;
    }
    
    public Sprite getSprite() {
        return sprite;
    }    
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public void moveX(double move) {
        point.setLocation(point.getX() + move, point.getY());
    }
    public void moveY(double move) {
        point.setLocation(point.getX(), point.getY() + move);
    }
    public Point getPoint() {
        return point;
    }
    public void die() {
        isAlive = false;
    }
    public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean set) {
        isAlive = set;
    }
    public void setPoint(Point p) {
        point = p;
    }
    
}
