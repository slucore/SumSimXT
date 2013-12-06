/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Timer;

/**
 *
 * @author slucore
 */
public class ShotObject extends GameObject {
    
    private int damage;
    private Target target;
    private double xVelocity, yVelocity;
    private boolean isExplosive = false;
    private boolean isAlive = true;
    private GameObject source;
    
    public ShotObject(Sprite sprite, Point point, Dimension size, Target target, int damage, GameObject source) {
        super(sprite, point, size);
        this.target = target;
        this.damage = damage;
        xVelocity = (target.getPoint().getX() - point.getX()) * target.getSpeed();
        yVelocity = (target.getPoint().getY() - point.getY()) * target.getSpeed();
        this.source = source;
    }
    
    public ShotObject(Sprite sprite, Point point, Dimension size, double xVelocity, double yVelocity, int damage, GameObject source) {
        super(sprite, point, size);
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.damage = damage;
        this.source = source;
    }
    
    public GameObject getSource() {
        return source;
    }
    
    public boolean isAlive() {
        return isAlive;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public void hitSomething() {
        xVelocity = 0;
        yVelocity = 0;
        isAlive = false;
    }
    
    public void move(long millis) {
        double seconds = millis / 1000.0;
        moveX(xVelocity * seconds);
        moveY(yVelocity * seconds);
    }
    
    public void setExplosive(boolean set) {
        isExplosive = set;
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }
}
