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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author slucore
 */
public class Sprite {
    
    private static HashMap<String,Sprite> spriteMap = new HashMap<>();
    private static String imageDir = Sprite.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "../../images/sprites/";
    private Image image;
    private String name;
    private static List<Sprite> bulletExplosion = new ArrayList<>();
    private static List<Sprite> superBombExplosion = new ArrayList<>();
    public static final int SUPERBOMB_EXPLOSION_WIDTH = 226;    // 226
    public static final int SUPERBOMB_EXPLOSION_HEIGHT = 214;   // 214
    
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
    
    public Sprite(String name, File file, boolean map) {
        try {
            this.image = ImageIO.read(file);
            this.name = name;
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

    public static List<Sprite> getDeathExplosion() {
        List<Sprite> ret = new ArrayList<>();
        File file = new File(imageDir + "explosion.png");
        for (int i = 0; i < 128*6; i += 128) {
            Sprite frame = new Sprite("explosion", file, i, 0, 128, 128);
            frame.scaleSprite(SumSimXT.getPlayer().getWidth(),SumSimXT.getPlayer().getHeight());
            ret.add(frame);
        }
        return ret;
    }
    
    public static List<Sprite> getSuperBombExplosion() {
        if (!superBombExplosion.isEmpty()) {
            return superBombExplosion;
        }
        List<Sprite> ret = new ArrayList<>();
//        File file = new File(imageDir + "explosion_huge_256.png");
        File file = new File(imageDir + "explosion_huge_678x214.png");
        for (int i = 0; i < SUPERBOMB_EXPLOSION_WIDTH*3; i += SUPERBOMB_EXPLOSION_WIDTH) {
            Sprite frame = new Sprite("superBomb_explosion", file, i, 0, SUPERBOMB_EXPLOSION_WIDTH, SUPERBOMB_EXPLOSION_HEIGHT);
//            frame.scaleSprite(256,256);
            ret.add(frame);
        }
        superBombExplosion = ret;
        return ret;
    }
    
    public static List<Sprite> getBulletExplosion() {
        if (!bulletExplosion.isEmpty()) {
            return bulletExplosion;
        }
        List<Sprite> ret = new ArrayList<>();
        File file = new File(imageDir + "explosion.png");
        for (int i = 0; i < 128*6; i += 128) {
            Sprite frame = new Sprite("explosion", file, i, 0, 128, 128);
            frame.scaleSprite(50,50);
            ret.add(frame);
        }
        bulletExplosion = ret;
        return ret;
    }
    
    public static Sprite getSprite(String id) {
        if (spriteMap.keySet().contains(id)) {
            return spriteMap.get(id);
        }
//        System.out.format("New sprite query: %s\n", id);
        Sprite sprite = new Sprite(id, SpriteEnum.valueOf(id).getFile());
        return new Sprite(id, SpriteEnum.valueOf(id).getFile());
    }
    
    public static Sprite getScaledSprite(String id, int x, int y) {
        Sprite sprite = new Sprite(id, SpriteEnum.valueOf(id).getFile(), false);
        return sprite.scaleSprite(x, y);
    }
    
    public Sprite scaleSprite(int x, int y) {
        image = image.getScaledInstance(x, y, Image.SCALE_SMOOTH);
        return this;
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
        SMALL_EXPLOSION ("small_explosion.png"),
        MOB_LASER   ("mob_laser_10x20.png"),
        HEART       ("heart_30x25.png"),
        VANISH      ("transparent.png"),
        COIN        ("coin_20x25.png"),
        BIG_COIN    ("coin_40x50.png"),
        SHIELD_GREEN_A  ("shield_green_A.png"),
        SHIELD_GREEN_B  ("shield_green_B.png"),
        SHIELD_RED_A    ("shield_red_A.png"),
        SHIELD_RED_B    ("shield_red_B.png"),
        LASER_ICON      ("laser_30x30.png"),
        SUPERBOMB_ICON  ("superbomb_30x30.png"),
        JET_THRUSTERS   ("superbomb_30x30.png"),
        AIR_BRAKES      ("superbomb_30x30.png"),
        BLACK_LASER     ("mob_laser_20x20.png");
        
        private String filename;
        SpriteEnum(String filename) {
            this.filename = filename;
        }
        public File getFile() {
            return new File(imageDir + filename);
        }
    }
    
}
