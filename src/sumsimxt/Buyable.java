/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Image;


public class Buyable {

    private int cost;
    private Sprite sprite;
    private String name;
    private static final int ICON_WIDTH = 20;
    private static final int ICON_HEIGHT = 20;
    
    public int getCost() { return cost; }
    public Image getIcon() { return sprite.getImage(); }
    public String getName() { return name; }
    
    public Buyable(String name, Sprite sprite, int cost) {
        this.name = name;
        this.sprite = sprite;
        this.cost = cost;
    }
        
    public static class JetThrusters extends Buyable {
        public JetThrusters() {
            super("Jet Thrusters", Sprite.getScaledSprite("JET_THRUSTERS", ICON_WIDTH, ICON_HEIGHT), 200);
        }
    }
    
    public static class SuperBomb extends Buyable {
        public SuperBomb() {
            super("SuperBomb", Sprite.getScaledSprite("SUPERBOMB_ICON", ICON_WIDTH, ICON_HEIGHT), 200);
        }
    }
    
    public static class Laser extends Buyable {
        public Laser() {
            super("Laser", Sprite.getScaledSprite("BLACK_LASER", ICON_WIDTH, ICON_HEIGHT), 100);
        }
    }
    
    public static class ShieldUpgrade extends Buyable {
        public ShieldUpgrade() {
            super("Shield Recharge", Sprite.getScaledSprite("SHIELD_GREEN_A", ICON_WIDTH, ICON_HEIGHT), 20);
        }
    }
    
    public static class HealthUpgrade extends Buyable {
        public HealthUpgrade() {
            super("Health Upgrade", Sprite.getScaledSprite("HEART", ICON_WIDTH, ICON_HEIGHT), 50);
        }
    }
    
    public static class AirBrakes extends Buyable {
        public AirBrakes() {
            super("Air Brakes", Sprite.getScaledSprite("AIR_BRAKES", ICON_WIDTH, ICON_HEIGHT), 500);
        }
    }
    
}
