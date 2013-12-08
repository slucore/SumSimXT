/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;


public class Level {

    private static String backgroundDir;
    private String name;
    private BufferedImage background1, background2;
    private int scrollRateA, scrollRateB;
    private Color bgColor = Color.BLACK;
    private List<MobObject> mobs = new ArrayList<>();
    private int width = SumSimXT.getGameWidth();
    private int height = SumSimXT.getGameHeight();
    
    public Level(String name) {
        this.name = name;
        String bgName1 = "menu.png";
        String bgName2 = "menu.png";
        switch (name) {
            case "Level 1":
                bgColor = Color.BLACK;
                bgName1 = "level1a4_c.png";
                bgName2 = "level1b4_c.png";
                scrollRateA = -5;
                scrollRateB = -2;
                mobs = buildMobsLevel1();
                break;
            case "Level 2":
                bgName1 = "level2.png";
                scrollRateA = 10;
                break;
            case "Level 3":
                bgName1 = "level3.png";
                scrollRateA = 10;
                break;
            case "Main Menu":
                bgName1 = "menu.png";
                mobs = new ArrayList<>();
                break;
            case "Hangar":
                bgName1 = "hangar.png";
                break;
            default:
                bgName1 = "menu.png";
                mobs = new ArrayList<>();
                break;
        }
        try {
            background1 = ImageIO.read(new File(backgroundDir + bgName1));
            background2 = ImageIO.read(new File(backgroundDir + bgName2));
        } catch (IOException ex) {
            System.err.format("Error loading level image.\n");
            ex.printStackTrace();
        }
    }
    
    public void rebuildMobs() {
        mobs = buildMobsLevel1();
    }
    
    private List<MobObject> buildMobsLevel1() {
        Sprite alien = Sprite.getSprite("ALIEN");
        Sprite ninja = Sprite.getSprite("NINJA");
        Sprite sword = Sprite.getSprite("SWORD");
        Sprite knockout = Sprite.getSprite("KNOCKOUT");
        Sprite KO = Sprite.getSprite("KO");
        Sprite dead_ninja = Sprite.getSprite("DEAD_NINJA");
        List<Sprite> mobSprites = new ArrayList<>();
        List<Sprite> deadSprites = new ArrayList<>();
        mobSprites.add(alien);
        mobSprites.add(ninja);
        mobSprites.add(sword);
        deadSprites.add(knockout);
        deadSprites.add(KO);
        Dimension mobDimension = new Dimension(50,50);
        int mobHP = 1;
        Random rand = new Random();
        List<MobObject> ret = new ArrayList<>();
        final int LEFT_BOUND = 350;
        final int RIGHT_BOUND = 950;
        for (int i = LEFT_BOUND; i < RIGHT_BOUND; i += 75) {
            for (int j = -375; j < -75; j += 75) {   // start enemies above screen
                Sprite randSprite = mobSprites.get(rand.nextInt(mobSprites.size()));
                List<Target> aiTargets = new ArrayList<>();
                
                // and here is where the magic happens: set list of point targets and speeds for this enemy's AI
                aiTargets.add(new Target(new Point(i, j + 375), 0.5));
                double horizSpeed = 0.25;
                double vertSpeed = 1;
                for (int yTarget = j + 375; yTarget < SumSimXT.getGameHeight() + 200; ) {
                    aiTargets.add(new Target(new Point(75 + (i - LEFT_BOUND), yTarget), horizSpeed));
                    horizSpeed += 0.05;
                    yTarget += 75;
                    if (yTarget >= SumSimXT.getGameHeight() - 175) {
                        aiTargets.add(new Target(new Point(75 + (i - LEFT_BOUND), SumSimXT.getGameHeight() + 100), vertSpeed * 2));
                        break;
                    }
                    aiTargets.add(new Target(new Point(75 + (i - LEFT_BOUND), yTarget), vertSpeed));
                    aiTargets.add(new Target(new Point(SumSimXT.getGameWidth() - 50 - (RIGHT_BOUND - i), yTarget), horizSpeed));
                    horizSpeed += 0.05;
                    yTarget += 75;
                    if (yTarget >= SumSimXT.getGameHeight() - 175) {
                        aiTargets.add(new Target(new Point(SumSimXT.getGameWidth() - 50 - (RIGHT_BOUND - i), SumSimXT.getGameHeight() + 100), vertSpeed * 2));
                        break;
                    }
                    aiTargets.add(new Target(new Point(SumSimXT.getGameWidth() - 50 - (RIGHT_BOUND - i), yTarget), vertSpeed));
                }
                
                AI mobAI = new AI(aiTargets);
                MobObject mob = new MobObject(randSprite, new Point(i,j), mobDimension, randSprite.getName(), mobHP, mobAI);
                mob.setDeathSprite(deadSprites.get(rand.nextInt(deadSprites.size())));
                if (randSprite.getName().equals("NINJA")) {     // special case: ninjas have to become dead ninjas
                    mob.setDeathSprite(dead_ninja);
                }
                ret.add(mob);
            }
        }
        return ret;
    }
    
    public List<MobObject> getMobs() {
        return mobs;
    }
    
    public Color getBackgroundColor() {
        return bgColor;
    }
    
    public BufferedImage getBackgroundA() {
        return background1;
    }
    
    public BufferedImage getBackgroundB() {
        return background2;
    }
    
    public int getBackgroundAScrollRate() {
        return scrollRateA;
    }
    
    public int getBackgroundBScrollRate() {
        return scrollRateB;
    }
    
    public static void setBackgroundDir(String path) {
        backgroundDir = path;
    }
    
    public enum MainMenu {
        TEXT_POSITION   (SumSimXT.getGameWidth() / 2 - 325, SumSimXT.getGameHeight() / 2 + 25),
        HIGH_SCORE_TITLE(SumSimXT.getGameWidth() / 2 - 150, SumSimXT.getGameHeight() / 2 + 100),
        HIGH_SCORE_A    (SumSimXT.getGameWidth() / 2 - 150, SumSimXT.getGameHeight() / 2 + 150),
        HIGH_SCORE_B    (SumSimXT.getGameWidth() / 2 - 150, SumSimXT.getGameHeight() / 2 + 200),
        HIGH_SCORE_C    (SumSimXT.getGameWidth() / 2 - 150, SumSimXT.getGameHeight() / 2 + 250);
        private int x, y;
        MainMenu(int x, int y) { this.x = x; this.y = y; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
//    public enum Levels {
//        MAIN_MENU   (null, ImageIO.read(new File(backgroundDir + "menu.png")), null);
//        private Level level;
//        Levels(String name, Image background, List<MobObject> mobs) {
//            level = new Level(name, background, mobs);
//        }
//        public Level getLevel() {
//            return level;
//        }
//    }
    
}
