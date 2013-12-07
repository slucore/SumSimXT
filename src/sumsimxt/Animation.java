/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Animation {

    private GameObject thing;
    private List<Sprite> sprites = new ArrayList<>();
    private Timer timer = new Timer();
    
        
    public Animation(GameObject thing, List<Sprite> sprites, long delay) {
        this.thing = thing;
        this.sprites = sprites;
        for (int i = 0; i < sprites.size(); i++) {
            timer.schedule(new AnimTask(sprites.get(i)), delay * i);
        }
    }
    
    public Animation(GameObject thing, List<Sprite> sprites, long delay, int loop) {
        this.thing = thing;
        this.sprites = sprites;
        for (int c = 0; c < loop; c++) {
            for (int i = 0; i < sprites.size(); i++) {
                timer.schedule(new AnimTask(sprites.get(i)), (delay * i) + (c*delay*sprites.size()));
            }
        }
        timer.schedule(new AnimTask(Sprite.getSprite("VANISH")), (loop-1)*delay*sprites.size() + (delay*sprites.size()));
    }
    
    private class AnimTask extends TimerTask {
        private Sprite sprite;
        public AnimTask(Sprite sprite) { this.sprite = sprite; }
        public void run() {
            thing.setSprite(sprite);
        }
    }
        
}
