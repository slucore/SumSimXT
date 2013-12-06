/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;


public class SuperBomb implements Buyable {

    private int cost = 10;
    private Sprite icon;
    private String name = "SuperBomb";
    
    public SuperBomb() {
        icon = Sprite.getSprite("SUPERBOMB");
    }
    
    public Sprite getSprite() {
        return icon;
    }
    
    public int getCost() {
        return cost;
    }
    
    public String getName() {
        return name;
    }
    
}
