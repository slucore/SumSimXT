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


public class ShieldSet {

    private List<Shield> shields = new ArrayList<>();
    
    public ShieldSet(int fragility) {
        Sprite sprite = Sprite.getSprite("SHIELD_GREEN_A");
        Dimension dim = new Dimension(sprite.getImage().getWidth(null), sprite.getImage().getHeight(null));
        int width = SumSimXT.getGameWidth();
        int height = SumSimXT.getGameHeight() - 200;
        for (int i = 1; i <= 4; i++) {
            Point point = new Point(i*(width/5) - (sprite.getImage().getWidth(null) / 2), height);
            shields.add(new Shield(sprite, point, dim, fragility));
        }
    }
    
    public List<Shield> getShields() {
        return shields;
    }
    
    public class Shield extends GameObject {
        private int HP = 8;
        private int fragility = 1;
        public Shield(Sprite sprite, Point point, Dimension dim, int fragility) {
            super(sprite, point, dim);
            this.fragility = fragility;
        }
        public int getHP() {
            return HP;
        }
        public void takeHit() {
            HP -= fragility;
            switch (HP) {
                case 6:
                    setSprite(Sprite.getSprite("SHIELD_GREEN_B"));
                    break;
                case 4:
                    setSprite(Sprite.getSprite("SHIELD_RED_A"));
                    break;
                case 2:
                    setSprite(Sprite.getSprite("SHIELD_RED_B"));
                    break;
                case 0:
                    setAlive(false);
                default:
            }
        }
    }
    
}
