/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author slucore
 */
public class Sprite {
    
    private static HashMap<String,Sprite> spriteMap = new HashMap<>();
    private static String imageDir = Sprite.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "images/";
    private Image image;
    private String name;
    
    public Sprite(String name, Image image) {
        this.name = name;
        this.image = image;
    }
    
    public Sprite(String name, File file) {
        try {
            this.image = ImageIO.read(file);
            this.name = name;
            spriteMap.put(name, this);
        } catch (IOException ex) {
            System.err.format("I/O exception while reading sprite %s\n", name);
        }
    }
    
    public Sprite(String name, File file, int xStart, int yStart, int xSize, int ySize) {
        try {
            BufferedImage sheet = ImageIO.read(file);
            this.image = sheet.getSubimage(xStart, yStart, xSize, ySize);
            this.name = name;
            spriteMap.put(name, this);
        } catch (IOException ex) {
            System.err.format("I/O exception while reading sprite %s\n", name);
        }
    }
    
    public static void setSpriteDir(String path) {
        imageDir = path;
    }
    
    public String getName() {
        return name;
    }
    
    public Image getImage() {
        return image;
    }
    
    public static Sprite getSprite(String id) {
        if (spriteMap.keySet().contains(id)) {
            return spriteMap.get(id);
        }
        Sprite sprite = new Sprite(id, SpriteEnum.valueOf(id).getFile());
        return new Sprite(id, SpriteEnum.valueOf(id).getFile());
    }
    
    public enum SpriteEnum {
        PLAYER      ("player.png"),
        ALIEN       ("default_50x50.png"),
        SWORD       ("sword_50x50.png"),
        KNOCKOUT    ("knockout_50x50.png"),
        KO          ("KO_50x50.png"),
        NINJA       ("ninja_50x50.png"),
        DEAD_NINJA  ("dead_ninja_50x50.png"),
        SUPERBOMB   ("superbomb_50x50.png"),
        BULLET      ("bullet_10x30.png"),
        SHIP_TITAN  ("ship_titan_75x100.png"),
        SHIP_SC     ("ship_skycutter_75x75.png"),
        SMALL_EXPLOSION ("small_explosion.png");
        
        private String filename;
        SpriteEnum(String filename) {
            this.filename = filename;
        }
        public File getFile() {
            return new File(imageDir + filename);
        }
    }
    
}
