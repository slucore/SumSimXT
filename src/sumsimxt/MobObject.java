/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author slucore
 */
public class MobObject extends GameObject {
    
    private int totalHP;
    private int currentHP;
    private AI ai;
    private Target currentTarget;
    private double xVelocity;
    private double yVelocity;
    private Sprite deathSprite;
    private String name;
    private Point lastPosition;
    private final double ERROR_TOLERANCE = 5.0;
    
    public MobObject(Sprite sprite, Point point, Dimension size, String name, int totalHP, AI ai) {
        super(sprite, point, size);
        this.totalHP = totalHP;
        this.currentHP = totalHP;
        this.ai = ai;
        this.name = name;
        lastPosition = point;
        prepareNextMove();
    }
    
    public String getName() {
        return name;
    }
    
    public void setDeathSprite(Sprite deathSprite) {
        this.deathSprite = deathSprite;
    }
    
    public void move(long millis) {
        double seconds = millis / 1000.0;
        moveX(xVelocity * seconds);
        moveY(yVelocity * seconds);
        
//        List<Point> traversed = new ArrayList<>();
//        while (lastPosition.x != getPoint().x || lastPosition.y != getPoint().y) {
//            traversed.add(lastPosition);
//            if (xVelocity > 0) {
//                lastPosition.x++;
//            }
//            if (xVelocity < 0) {
//                lastPosition.x--;
//            }
//            if (yVelocity > 0) {
//                lastPosition.y++;
//            }
//            if (yVelocity < 0) {
//                lastPosition.y--;
//            }
//        }
//        traversed.add(lastPosition);
//        Point targetPoint = currentTarget.getPoint();
//        for (Point trav : traversed) {
//            if (trav.x == targetPoint.x && trav.y == targetPoint.y) {
//                ai.signalArrived();
//                prepareNextMove();
//            }
//        }
        
        if (Math.abs(getPoint().getX() - currentTarget.getPoint().getX()) < ERROR_TOLERANCE
                && Math.abs(getPoint().getY() - currentTarget.getPoint().getY()) < ERROR_TOLERANCE) {
            ai.signalArrived();
            prepareNextMove();
        }
        
//        if (getPoint().x == currentTarget.getPoint().x && getPoint().y == currentTarget.getPoint().y) {
//            ai.signalArrived();
//            prepareNextMove();
//        } else if ((xVelocity < 0 && getPoint().getX() < currentTarget.getPoint().getX())
//                || (xVelocity > 0 && getPoint().getX() > currentTarget.getPoint().getX())
//                || (yVelocity < 0 && getPoint().getY() < currentTarget.getPoint().getY())
//                || (yVelocity > 0 && getPoint().getY() > currentTarget.getPoint().getY())) {
//            prepareNextMove();
//        }
    }
    
    private void prepareNextMove() {
        currentTarget = ai.getTarget();
        Point whereAmI = this.getPoint();
//        int xMult = 0, yMult = 0;
//        if (currentTarget.getPoint().getX() < whereAmI.getX()) {
//            xMult = -1;
//        } else {
//            xMult = 1;
//        }
//        if (currentTarget.getPoint().getY() < whereAmI.getY()) {
//            yMult = -1;
//        } else {
//            yMult = 1;
//        }
//        xVelocity = xMult * currentTarget.getSpeed();
//        yVelocity = yMult * currentTarget.getSpeed();        
        xVelocity = (currentTarget.getPoint().getX() - whereAmI.getX()) * currentTarget.getSpeed();
        yVelocity = (currentTarget.getPoint().getY() - whereAmI.getY()) * currentTarget.getSpeed();
//        System.out.format("%s:\t(%.1f,%.1f)\t(%.1f,%.1f)\t%.1f\t(%.1f,%.1f)\n", name, whereAmI.getX(), whereAmI.getY(), 
//                currentTarget.getPoint().getX(), currentTarget.getPoint().getY(), currentTarget.getSpeed(),
//                xVelocity, yVelocity);
    }
    
    public void takeDamage(int dmg) {
        currentHP -= dmg;
        if (currentHP <= 0) {
            this.die();
            this.setSprite(deathSprite);
        }
    }
    
}

