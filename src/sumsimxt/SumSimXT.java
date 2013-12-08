/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sumsimxt.ShieldSet.Shield;

/**
 *
 * @author slucore
 */
public class SumSimXT extends Canvas implements Runnable {
    
    // Graphics
    private JFrame mainFrame = new JFrame("SumSimXT");
    private JPanel mainPanel = (JPanel) mainFrame.getContentPane();
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics;
    private static final int STANDARD_WIDTH = 1000;
    private static final int STANDARD_HEIGHT = 700;
    private static int gameWidth = STANDARD_WIDTH;
    private static int gameHeight = STANDARD_HEIGHT;
    private boolean fullscreen = false;
    private Image bgA, bgB;
    private int bgScrollerA, bgScrollerB;
    long passed, last;
    
    // Threading and Networking
    private Executor executor = Executors.newCachedThreadPool();
    
    // Game variables
    private String classPath = SumSimXT.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private static PlayerObject player;
    private HashMap<String,Integer> playerLoadout = new HashMap<>();
    private Level currentLevel;
    private MenuListener menuListener = new MenuListener();
    private FlightListener flightListener = new FlightListener();
    private HangarListener hangarListener = new HangarListener();
    boolean startGame = false;
    boolean pauseRequested = false;
    private int movementRequestedX = 0;
    private int hangarRequested = 0;
    private boolean goToHangar = false;
    private boolean embark = false;
    private boolean shotRequested = false;
    private boolean laserRequested = false;
    private boolean superBombRequested = false;
    private boolean superBombFired = false;
    private boolean laserFired = false;
    private boolean warpSpeed = false;
    private boolean warpSpeedScheduled = false;
    private boolean loadNextScheduled = false;
    private boolean playerArrived = false;
    private List<ShotObject> shots = new ArrayList<>();
    private Random rand = new Random();
    private ShieldSet shieldSet;
    private final String highScoresLocation = "http://www.engineering.uiowa.edu/~slucore/SumSimXT_HighScores.txt";
    private List<String> highScores = new ArrayList<>();
    private Sounds sounds;
    private Clip menuSong, hangarSong, levelSong, bossSong;
    private Clip shootSound, laserSound, superBombSound, mobShootSound, mobDeathSound;
    private Clip coinSound, playerHurtSound, playerDeathSound, warpSpeedSound;
    private final int PLAYER_START_X = (gameWidth / 2) - 40;
    private final int PLAYER_START_Y = gameHeight - 110;
    
    // HighScores stuff
    private HighScoreCommunicator hsCom = new HighScoreCommunicator();
    
    public SumSimXT() {
        Sprite.setSpriteDir(classPath + "../../images/sprites/");
        Level.setBackgroundDir(classPath + "../../images/backgrounds/");
        sounds = new Sounds(classPath + "../../sounds/");
        menuSong = sounds.getMenu();
        hangarSong = sounds.getHangarSong();
        levelSong = sounds.getLevelSong();
        bossSong = sounds.getBossSong();
        shootSound = sounds.getShoot();
        laserSound = sounds.getLaser();
        superBombSound = sounds.getSuperBomb();
        mobShootSound = sounds.getMobShoot();
        mobDeathSound = sounds.getMobDeath();
        coinSound = sounds.getCoin();
        playerHurtSound = sounds.getPlayerHurt();
        playerDeathSound = sounds.getPlayerDeath();
        warpSpeedSound = sounds.getWarpSpeed();
        
        this.setIgnoreRepaint(true);        
        switchFullscreen();     // disabled for now
        mainPanel.setPreferredSize(new Dimension(gameWidth,gameHeight));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }
    
//    public List<String> getHighScores() {
//        try {
//            URL highScoresURL = new URL(highScoresLocation);
//            InputStream stream = highScoresURL.openStream();
//            Scanner scan = new Scanner(stream);
//            while (scan.hasNextLine()) {
//                ret.add(scan.nextLine());
//            }
//            stream.close();
//        } catch (MalformedURLException ex) {
//            System.out.format("Malformed URL exception while retrieving high scores.\n");
//        } catch (IOException ex) {
//            System.out.format("I/O exception while retrieving high scores.\n");
//        }
//        return null;
//    }
    
    public void run() {
        // Finish initializing stuff
        mainFrame.getContentPane().add(this);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        this.createBufferStrategy(2);
        bufferStrategy = this.getBufferStrategy();
        graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        this.setFocusable(true);
        this.requestFocus();
        player = new PlayerObject(Sprite.getSprite("SHIP_TITAN"), new Point(PLAYER_START_X, gameHeight + 10), new Dimension(75,100), 5);
        executor.execute(new Runnable() {
            public void run() {
                do {
                    highScores = hsCom.getHighScores();
                } while (!hsCom.highScoresReady());
                System.out.format("done\n");
            }
        });
        
        // Show the Menu
        menuSong.loop(Clip.LOOP_CONTINUOUSLY);
        currentLevel = new Level("Main Menu");
        this.addKeyListener(menuListener);
        int alpha = 0;
        int delta_alpha = 10;
        Font glowFont = new Font("Consolas", Font.BOLD, 72);
        Font highScoresFont = new Font("Consolas", Font.BOLD, 48);
        Color highScoresColor = Color.YELLOW;
        while (!startGame) {
            Image bg = currentLevel.getBackgroundA();
            graphics.drawImage(bg, 0, 0, gameWidth, gameHeight, null);
            graphics.setColor(new Color(0, 255, 0, alpha));         // green text with variable transparency
            graphics.setFont(glowFont);
            graphics.drawString("Press Z to Start", Level.MainMenu.TEXT_POSITION.getX(), Level.MainMenu.TEXT_POSITION.getY());
            graphics.setColor(highScoresColor);
            graphics.setFont(highScoresFont);
            graphics.drawString("HIGH SCORES", Level.MainMenu.HIGH_SCORE_TITLE.getX(), Level.MainMenu.HIGH_SCORE_TITLE.getY());
            if (highScores.size() >= 1) {
                graphics.drawString(highScores.get(0) + ":" + String.format("%7s", highScores.get(1)), 
                        Level.MainMenu.HIGH_SCORE_A.getX(), Level.MainMenu.HIGH_SCORE_A.getY());
            }
            if (highScores.size() >= 3) {
                graphics.drawString(highScores.get(2) + ":" + String.format("%7s", highScores.get(3)), 
                        Level.MainMenu.HIGH_SCORE_B.getX(), Level.MainMenu.HIGH_SCORE_B.getY());
            }
            if (highScores.size() >= 5) {
                graphics.drawString(highScores.get(4) + ":" + String.format("%7s", highScores.get(5)), 
                        Level.MainMenu.HIGH_SCORE_C.getX(), Level.MainMenu.HIGH_SCORE_C.getY());
            }
//            graphics.drawString("SDL:   2500", Level.MainMenu.HIGH_SCORE_A.getX(), Level.MainMenu.HIGH_SCORE_A.getY());
//            graphics.drawString("JJL:   1250", Level.MainMenu.HIGH_SCORE_B.getX(), Level.MainMenu.HIGH_SCORE_B.getY());
//            graphics.drawString("JLR:    500", Level.MainMenu.HIGH_SCORE_C.getX(), Level.MainMenu.HIGH_SCORE_C.getY());
            alpha += delta_alpha;   // fades text in and out
            if (alpha >= 255) {
                alpha = 255;
                delta_alpha = -delta_alpha;
            } else if (alpha <= 0) {
                alpha = 0;
                delta_alpha = -delta_alpha;
            }
            bufferStrategy.show();
            pause(10);  // fps limiter
        }
        this.removeKeyListener(menuListener);
        menuSong.stop();
        
        // Main Game Loop
        currentLevel = new Level("Level 1");
        bgA = currentLevel.getBackgroundA();
        bgB = currentLevel.getBackgroundB();
        bgScrollerA = currentLevel.getBackgroundA().getHeight(null);
        bgScrollerB = currentLevel.getBackgroundB().getHeight(null);
        AlphaComposite parallax = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.75);    // 75% transparency for parallaxed background
        Composite original = graphics.getComposite();
        passed = last = System.currentTimeMillis();     
        boolean levelRunning = true;
        int renderingPass = 0;      // for control of startup lag
//        for (MobObject mob : currentLevel.getMobs()) {
//            System.out.format("%s %d %d\n", mob.getName(), mob.getPoint().x, mob.getPoint().y);
//        }
        shots = new ArrayList<>();
        Timer timer = new Timer();
        Image heart = Sprite.getSprite("HEART").getImage();
        Image bigCoin = Sprite.getSprite("BIG_COIN").getImage();
        Image laserIcon = Sprite.getSprite("LASER_ICON").getImage();
        Image superBombIcon = Sprite.getSprite("SUPERBOMB_ICON").getImage();
        Font coinFont = new Font("Helvetica", Font.BOLD, 36);
        graphics.setFont(coinFont);
        shieldSet = new ShieldSet(1);
        Sprite.getSuperBombExplosion();
        double warpSpeedVelocity = 0;
        
        while (levelRunning) {
            while (pauseRequested) {}
            if (goToHangar) {
                levelSong.stop();
                levelSong = sounds.getLevelSong();
                hangarRequested = 0;
                this.removeKeyListener(flightListener);
                hangar();
                levelSong.loop(Clip.LOOP_CONTINUOUSLY);
                this.addKeyListener(flightListener);
                bgA = currentLevel.getBackgroundA();
                bgB = currentLevel.getBackgroundB();
                bgScrollerA = currentLevel.getBackgroundA().getHeight(null);
                bgScrollerB = currentLevel.getBackgroundB().getHeight(null);
                player.setPoint(new Point((gameWidth / 2) - 40, gameHeight - 125));
                player.setCurrentHP(player.getTotalHP());
                shots = new ArrayList<>();
                passed = last = System.currentTimeMillis();
            }
            passed = System.currentTimeMillis() - last;
            last = System.currentTimeMillis();
            graphics.setColor(currentLevel.getBackgroundColor());
            graphics.fillRect(0,0,gameWidth,gameHeight);
            graphics.drawImage(bgB, 0, 0, gameWidth, gameHeight, 0, bgScrollerB - gameHeight, gameWidth, bgScrollerB, null);
            graphics.setComposite(parallax);
            graphics.drawImage(bgA, 0, 0, gameWidth, gameHeight, 0, bgScrollerA - gameHeight, gameWidth, bgScrollerA, null);
            graphics.setComposite(original);
            bgScrollerA += currentLevel.getBackgroundAScrollRate();
            bgScrollerB += currentLevel.getBackgroundBScrollRate();
            if (bgScrollerA - gameHeight < 0) {
                bgScrollerA = bgA.getHeight(null);
            }
            if (bgScrollerB - gameHeight < 0) {
                bgScrollerB = bgB.getHeight(null);
            }
            if (shotRequested && player.shotCooled()) {
                Sprite shotSprite = Sprite.getSprite("BULLET");
                Point shotPoint = new Point(player.getPoint());
                shotPoint.translate((player.getSprite().getImage().getWidth(null) / 2) - (shotSprite.getImage().getWidth(null) / 2), 0);
//                shotPoint.translate(0, -shotSprite.getImage().getHeight(null));
                player.startShotCooldown();
                shootSound.start();
                shootSound = sounds.getShoot();
                shots.add(new ShotObject(shotSprite, shotPoint, new Dimension(shotSprite.getImage().getWidth(null), shotSprite.getImage().getHeight(null)),
                        0, -player.getShotVelocity(), 50, player));
            }
            if (laserRequested && player.getLasers() > 0) {
                laserRequested = false;
                laserFired = true;
                laserSound.start();
                laserSound = sounds.getLaser();
                player.useLaser();
                timer.schedule(new TimerTask() { public void run() { laserFired = false; }}, 250);
            }
            if (superBombRequested && player.getSuperBombs() > 0) {
                superBombRequested = false;
                superBombFired = true;
                player.useSuperBomb();
                Sprite shotSprite = Sprite.getSprite("SUPERBOMB");
                Point shotPoint = new Point(player.getPoint().x + (player.getWidth() / 2) - (shotSprite.getImage().getWidth(null) / 2), player.getPoint().y);
                ShotObject shot = new ShotObject(shotSprite, shotPoint, new Dimension(shotSprite.getImage().getWidth(null), shotSprite.getImage().getHeight(null)),
                        0, -player.getShotVelocity() * 0.75, 0, player);
                shot.setSuperBomb(true);
                shots.add(shot);
            }
            for (int i = 0; i < shots.size(); i++) {
                ShotObject shot = shots.get(i);
                shot.move(passed);
                if (shot.isSuperBomb() && shot.getPoint().y <= gameHeight / 4 + 50) {
                    if (!superBombSound.isActive()) {
                        superBombSound.start();
                    }
                }
                if (shot.isSuperBomb() && shot.getPoint().y <= gameHeight / 4) {
                    shots.remove(shot);
//                    Point shotPoint = new Point(shot.getPoint().x + (shot.getWidth()/2) - (256/3), shot.getPoint().y + (shot.getHeight()/2) - (256/3));
                    Point shotPoint = new Point(shot.getPoint().x - (256/3), shot.getPoint().y - (256/2));
                    ShotObject explosion = new ShotObject(Sprite.getSprite("VANISH"), shotPoint, 
                            new Dimension(Sprite.SUPERBOMB_EXPLOSION_WIDTH,Sprite.SUPERBOMB_EXPLOSION_HEIGHT), 0, 0, 100, player);
                    explosion.setExplosion(true);
                    shots.add(explosion);
                    new Animation(explosion, Sprite.getSuperBombExplosion(), 100, 2);
                    timer.schedule(new ObjectRemover(explosion, shots), 600);
                    timer.schedule(new TimerTask() { public void run() { superBombSound = sounds.getSuperBomb(); }}, 300);
                }
                if (shot.getPoint().y + shot.getHeight() < 0 || shot.getPoint().y > gameHeight + 100) {
                    shots.remove(shot);
                    continue;
                }
                if (shot.isAlive()) {
                    for (int j = 0; j < shieldSet.getShields().size(); j++) {
                        Shield shield = shieldSet.getShields().get(j);
                        if (detectCollision(shot, shield)) {
                            shield.takeHit();
                            shot.hitSomething();
                            shot.moveX(-(Sprite.getSprite("SMALL_EXPLOSION").getImage().getWidth(null) / 2));
                            new Animation(shot, Sprite.getBulletExplosion(), 50);
                            timer.schedule(new ObjectRemover(shot, shots), 300);
                            if (!shield.isAlive()) {
                                new Animation(shield, Sprite.getBulletExplosion(), 50);
                                timer.schedule(new ObjectRemover(shield, shieldSet.getShields()), 300);
                            }
                        }
                    }
                    if (shot.getSource().equals(player)) {
                        for (int j = 0; j < currentLevel.getMobs().size(); j++) {
                            MobObject mob = currentLevel.getMobs().get(j);
                            if (mob.isAlive() && detectCollision(shot, mob)) {
                                mob.takeDamage(shot.getDamage());
                                if (!shot.isLaser() && !shot.isExplosion()) {
                                    shot.setSprite(Sprite.getSprite("SMALL_EXPLOSION"));
                                    shot.hitSomething();
                                    new Animation(shot, Sprite.getBulletExplosion(), 50);
                                    shot.moveX(-(Sprite.getSprite("SMALL_EXPLOSION").getImage().getWidth(null) / 2));
                                    timer.schedule(new ObjectRemover(shot, shots), 300);
                                }
                                if (!mob.isAlive()) {
//                                    mobDeathSound.start();
//                                    mobDeathSound = sounds.getMobDeath();
                                    timer.schedule(new ObjectRemover(mob, currentLevel.getMobs()), 500);
                                    if (rand.nextInt(10) < 2) {
                                        Sprite shotSprite = Sprite.getSprite("COIN");
                                        Point shotPoint = new Point(mob.getPoint());
                                        shotPoint.translate((mob.getSprite().getImage().getWidth(null) / 2) - (shotSprite.getImage().getWidth(null) / 2), 0);
                                        shots.add(new ShotObject(shotSprite, shotPoint, new Dimension(shotSprite.getImage().getWidth(null), shotSprite.getImage().getHeight(null)),
                                                0, mob.getShotVelocity()*2, -10, mob));
                                    }
                                }
                            }
                        }
                    } else {
                        if (detectCollision(shot, player)) {
                            shot.hitSomething();
                            if (shot.getDamage() > 0) {
                                shot.setSprite(Sprite.getSprite("SMALL_EXPLOSION"));
                                Animation animation = new Animation(shot, Sprite.getBulletExplosion(), 50);
                                shot.moveX(-(Sprite.getSprite("SMALL_EXPLOSION").getImage().getWidth(null) / 2));
                            } else {
                                coinSound.start();
                                coinSound = sounds.getCoin();
                                shot.setSprite(Sprite.getSprite("VANISH"));
                            }
                            timer.schedule(new ObjectRemover(shot, shots), 250);
                            player.takeDamage(shot.getDamage());
                            if (!player.isAlive()) {
                                playerDeathSound.start();
                                playerDeathSound = sounds.getPlayerDeath();
                                this.removeKeyListener(flightListener);
                                player.setXVelocity(0);
                                player.setYVelocity(0);
                                new Animation(player, Sprite.getDeathExplosion(), 50, 3);
                                timer.schedule(new TimerTask() {
                                    public void run() {
                                        goToHangar = true;
                                    }
                                }, 1500);
                            } else {
                                playerHurtSound.start();
                                playerHurtSound = sounds.getPlayerHurt();
                            }
                        }
                    }
                }
                Image shotImage = shot.getSprite().getImage();
                if (!shot.isExplosion()) {
                    graphics.drawImage(shotImage, shot.getPoint().x, shot.getPoint().y, shotImage.getWidth(null), shotImage.getHeight(null), null);
                }
            }
            if (movementRequestedX != 0) {
                player.setXVelocity(player.getXVelocity() + (movementRequestedX * player.getHorizontalAcceleration()));
            } else {
                player.decelerateX(player.getHorizontalDecelerationFactor());
            }
            player.move(passed);
            Image playerImage = player.getSprite().getImage();
            if (!warpSpeed) {
                graphics.drawImage(playerImage, player.getPoint().x, player.getPoint().y, playerImage.getWidth(null), playerImage.getHeight(null), null);
            }
            if (renderingPass > 5) {
                for (int i = 0; i < currentLevel.getMobs().size(); i++) {
                    MobObject mob = currentLevel.getMobs().get(i);
                    mob.move(passed);
                    if (mob.getAI().scriptFinished()) {
                        currentLevel.getMobs().remove(mob);
                        continue;
                    }
                    for (int j = 0; j < shieldSet.getShields().size(); j++) {
                        Shield shield = shieldSet.getShields().get(j);
                        if (detectCollision(mob, shield)) {
                            mob.takeDamage(mob.getCurrentHP());
                            mob.halt();
                            new Animation(mob, Sprite.getBulletExplosion(), 50);
                            timer.schedule(new ObjectRemover(mob, currentLevel.getMobs()), 300);
                            shield.takeHit();
                            if (!shield.isAlive()) {
                                new Animation(shield, Sprite.getBulletExplosion(), 50);
                                timer.schedule(new ObjectRemover(shield, shieldSet.getShields()), 300);
                            }
                        }
                    }
                    if (detectCollision(mob, player)) {
                        mob.takeDamage(mob.getCurrentHP());
                        mob.halt();
                        new Animation(mob, Sprite.getBulletExplosion(), 50);
                        timer.schedule(new ObjectRemover(mob, currentLevel.getMobs()), 300);
                        player.takeDamage(mob.getCollisionDamage());
                        if (!player.isAlive()) {
                            this.removeKeyListener(flightListener);
                            player.setXVelocity(0);
                            player.setYVelocity(0);
                            new Animation(player, Sprite.getDeathExplosion(), 50, 3);
                            timer.schedule(new TimerTask() {
                                public void run() {
                                    goToHangar = true;
                                }
                            }, 1500);
                        }
                    }
                    Image mobImage = mob.getSprite().getImage();
                    graphics.drawImage(mobImage, mob.getPoint().x, mob.getPoint().y, mobImage.getWidth(null), mobImage.getHeight(null), null);
                    if (mob.shotCooled() && mob.wantsToShoot()) {
                        Sprite shotSprite = Sprite.getSprite("MOB_LASER");
                        Point shotPoint = new Point(mob.getPoint());
                        shotPoint.translate((mob.getSprite().getImage().getWidth(null) / 2) - (shotSprite.getImage().getWidth(null) / 2), 0);
                        mob.startShotCooldown();
                        mobShootSound.start();
                        mobShootSound = sounds.getMobShoot();
                        shots.add(new ShotObject(shotSprite, shotPoint, new Dimension(shotSprite.getImage().getWidth(null), shotSprite.getImage().getHeight(null)),
                                0, mob.getShotVelocity(), 1, mob));
                    }
                }
            } else {
                renderingPass++;
                if (renderingPass == 5) {
                    levelSong.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
            if (loadNextScheduled && !currentLevel.getMobs().isEmpty()) {
                loadNextScheduled = false;
            }
            if (currentLevel.getMobs().isEmpty() && !warpSpeedScheduled && !loadNextScheduled) {
                warpSpeedSound.start();
                warpSpeedSound = sounds.getWarpSpeed();
                timer.schedule(new TimerTask() {
                    public void run() {
                        SumSimXT.this.removeKeyListener(flightListener);
                        warpSpeed = true;
                    }}, 2000);
                warpSpeedScheduled = true;
            }
            if (!playerArrived) {
                player.getPoint().y -= 2;
                if (player.getPoint().y <= PLAYER_START_Y) {
                    player.getPoint().y = PLAYER_START_Y;
                    playerArrived = true;
                    this.addKeyListener(flightListener);
                }
            }
            if (laserFired) {
                Sprite shotSprite = Sprite.getSprite("VANISH");
                Point shotPoint = new Point(player.getPoint().x + (player.getWidth() / 2) - 1, 0);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(shotPoint.x-3, 0, 6, player.getPoint().y);
                graphics.setColor(Color.RED);
                graphics.drawLine(shotPoint.x, player.getPoint().y, shotPoint.x, 0);
                ShotObject shot = new ShotObject(shotSprite, shotPoint, new Dimension(3, gameHeight), 0, 0, 1, player);
                shot.setLaser(true);
                shots.add(shot);
                timer.schedule(new ObjectRemover(shot, shots), 250);
            }
            for (int i = 0; i < shieldSet.getShields().size(); i++) {
                Shield shield = shieldSet.getShields().get(i);
                Image shieldImage = shield.getSprite().getImage();
                graphics.drawImage(shieldImage, shield.getPoint().x, shield.getPoint().y, shield.getWidth(), shield.getHeight(), null);
            }
            for (int i = 0; i < player.getCurrentHP(); i++) {
                graphics.drawImage(heart, (heart.getWidth(null) + 5) * i, gameHeight - heart.getHeight(null), heart.getWidth(null), heart.getHeight(null), null);
            }
            for (int i = 1; i <= player.getLasers(); i++) {
                graphics.drawImage(laserIcon, gameWidth - ((laserIcon.getWidth(null) + 5) * i), gameHeight - laserIcon.getHeight(null),
                        laserIcon.getWidth(null), laserIcon.getHeight(null), null);
            }
            for (int i = 1; i <= player.getSuperBombs(); i++) {
                graphics.drawImage(superBombIcon, gameWidth - ((superBombIcon.getWidth(null) + 5)*i), 
                        gameHeight - laserIcon.getHeight(null) - superBombIcon.getHeight(null),
                        superBombIcon.getWidth(null), superBombIcon.getHeight(null), null);
            }
            for (int i = 0; i < shots.size(); i++) {
                if (shots.get(i).isExplosion()) {
                    ShotObject shot = shots.get(i);
                    Image shotImage = shot.getSprite().getImage();
                    graphics.drawImage(shotImage, shot.getPoint().x, shot.getPoint().y, shotImage.getWidth(null), shotImage.getHeight(null), null);
                }
            }
            if (warpSpeed && !loadNextScheduled) {
                player.setXVelocity(0);
                player.setYVelocity(0);
                warpSpeedVelocity -= 0.5;
                player.getPoint().translate(0, (int) warpSpeedVelocity);
                graphics.drawImage(playerImage, player.getPoint().x, player.getPoint().y, playerImage.getWidth(null), playerImage.getHeight(null), null);
                for (int i = 19; i <= 56; i++) {
                    graphics.setColor(getRandomColor());
                    graphics.drawLine(player.getPoint().x + i, player.getPoint().y + player.getHeight(), player.getPoint().x + i, gameHeight);
                }
                if (player.getPoint().y + player.getHeight() < -200) {
                    timer.schedule(new TimerTask() { public void run() {
                        setupNextLevel();
                    }}, 500);
                    loadNextScheduled = true;
                }
            }
            graphics.drawImage(bigCoin, 0, 0, bigCoin.getWidth(null), bigCoin.getHeight(null), null);
            graphics.setColor(Color.YELLOW);
            graphics.drawString(String.format("%d", player.getGold()), bigCoin.getWidth(null) + 5, graphics.getFont().getSize() - 5);
            bufferStrategy.show();
            pause(10);      // fps limiter
        }
    }
    
    private void setupNextLevel() {
        warpSpeedScheduled = false;
        warpSpeed = false;
        currentLevel.rebuildMobs();
        player.getPoint().x = (gameWidth / 2) - 40;
        player.getPoint().y = gameHeight + 10;
        playerArrived = false;
        this.addKeyListener(flightListener);
//        bgA = currentLevel.getBackgroundA();
//        bgB = currentLevel.getBackgroundB();
//        bgScrollerA = currentLevel.getBackgroundA().getHeight(null);
//        bgScrollerB = currentLevel.getBackgroundB().getHeight(null);
        player.setPoint(new Point((gameWidth / 2) - 40, gameHeight - 125));
        player.setCurrentHP(player.getTotalHP());
        shots = new ArrayList<>();
        passed = last = System.currentTimeMillis();
    }
    
    private class ObjectRemover extends TimerTask {
        private GameObject thing;
        private List list;
        public ObjectRemover(GameObject thing, List list) { this.thing = thing; this.list = list; }
        public void run() {
            list.remove(thing);
        }
    }
    
    private boolean detectCollision(GameObject a, GameObject b) {
        if (!a.isAlive() || !b.isAlive()) {
            return false;
        }
        int ax = a.getPoint().x;
        int ay = a.getPoint().y;
        int bx = b.getPoint().x;
        int by = b.getPoint().y;
        if ((ay + a.getHeight()) >= by && ay <= (by + b.getHeight())
                && (ax + a.getWidth()) >= bx && ax <= (bx + b.getWidth())) {
            return true;
        }
        return false;
    }
    
    private void initLevel(Level level) {
        
    }
    
    private void hangar() {
        hangarSong.loop(Clip.LOOP_CONTINUOUSLY);
        goToHangar = false;
        embark = false;
        this.addMouseListener(hangarListener);
        currentLevel = new Level("Hangar");
        Image bg = currentLevel.getBackgroundA();
        graphics.drawImage(bg, 0, 0, gameWidth, gameHeight, null);
        bufferStrategy.show();
        while (!embark) {
            pause(10);
        }
        this.removeMouseListener(hangarListener);
        currentLevel = new Level("Level 1");
        shieldSet = new ShieldSet(1);
        player.revive();
        hangarSong.stop();
        hangarSong = sounds.getHangarSong();
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            System.err.format("Interrupted exception!\n");
            ex.printStackTrace();
        }
    }
    
    private void switchFullscreen() {
        if (fullscreen) {
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = environment.getDefaultScreenDevice();
            device.setFullScreenWindow(mainFrame);
            gameWidth = device.getFullScreenWindow().getWidth();
            gameHeight = device.getFullScreenWindow().getHeight();
            DisplayMode displayMode = new DisplayMode(gameWidth, gameHeight, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
            if (device.isDisplayChangeSupported()) {
                device.setDisplayMode(displayMode);
            }
        } else {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
            gameHeight = STANDARD_HEIGHT;
            gameWidth = STANDARD_WIDTH;
        }
        this.setBounds(0,0,gameWidth,gameHeight);
        mainPanel.setPreferredSize(new Dimension(gameWidth,gameHeight));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
    }
    
    public void shop() {
        Shop shop = new Shop();
    }
    
//    public void sendHighScore() {
//        try {
//            URL highScoresURL = new URL(highScoresLocation);
//            URLConnection con = highScoresURL.openConnection();
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            OutputStream os = con.getOutputStream();
//            Formatter formatter = new Formatter(os);
//            for (int i = 0; i < newHighScores.size(); i++) {
//                formatter.format(newHighScores.get(i));
//            }
//        } catch (MalformedURLException ex) {
//            System.out.format("Malformed URL exception while sending high scores.\n");
//        } catch (IOException ex) {
//            System.out.format("I/O exception while sending high scores.\n");
//        }
        
        
//        try {
//            File file = new File(classPath + "temp.txt");
//            FileUtils.writeLines(file, newHighScores);
//            URL highScoresURL = new URL(highScoresLocation);
//            URLConnection con = new URL(highScoresLocation).openConnection();
//            con.setDoOutput(true);
//            HttpClient client = new DefaultHTTPClient();
//        } catch (IOException ex) {
//            System.out.format("I/O exception while sending high scores.\n");
//        }
//    }
    
    private class FullScreenListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F5) {
                fullscreen = !fullscreen;
                switchFullscreen();
            }
        }
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }
    
    private class HangarListener implements MouseListener {
        public void mouseExited(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {
            double x = ((double) e.getX()) / gameWidth;
            double y = ((double) e.getY()) / gameHeight;
//            System.out.format("%d, %d, %f\n%d, %d, %f\n", e.getX(), WIDTH, x, e.getY(), HEIGHT, y);
            if (y > (300.0/800) && y < (430.0/800) && x > (280.0/1450) && x < (440.0/1450)) {
//                System.out.format("SHOP\n");
                shop();
            } else if (y > (280.0/800) && y < (380.0/800) && x > (800.0/1450) && x < (1280.0/1450)) {
//                System.out.format("EMBARK\n");
                embark = true;
            } else if (y > (710.0/800) && x > (1270.0/1450)) {
//                System.out.format("EXIT\n");
                if (!highScores.isEmpty()) {
                    hsCom.sendHighScores();
                }
                System.exit(0);
            }
        }
    }
    
    private class FlightListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_F5:
                    switchFullscreen();
                case KeyEvent.VK_Z:
                    // fire primary
                    shotRequested = true;
                    break;
                case KeyEvent.VK_X:
                    // fire secondary
                    laserRequested = true;
                    break;
                case KeyEvent.VK_SPACE:
                    // fire bomb
                    superBombRequested = true;
                    break;
                case KeyEvent.VK_P:
                case KeyEvent.VK_ENTER:
                    // pause
                    pauseRequested = !pauseRequested;
                    break;
                case KeyEvent.VK_LEFT:
                    // move left
                    movementRequestedX = -1;
//                    if (player.getXVelocity() > 0) {
//                        player.setXVelocity(0);
//                    } else {
//                        player.setXVelocity(-1 * player.getHorizontalAcceleration());
//                    }
                    //player.setXVelocity(player.getXVelocity() - player.getHorizontalAcceleration());
                    break;
                case KeyEvent.VK_RIGHT:
                    // move right
                    movementRequestedX = 1;
//                    if (player.getXVelocity() < 0) {
//                        player.setXVelocity(0);
//                    } else {
//                        player.setXVelocity(player.getHorizontalAcceleration());
//                    }
                    //player.setXVelocity(player.getXVelocity() + player.getHorizontalAcceleration());
                    break;
                case KeyEvent.VK_Q:
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_H:
                    goToHangar = true;
                    break;
                case KeyEvent.VK_DOWN:
                    // if held, return to hangar
                    hangarRequested++;
                    if (hangarRequested > 10) {
                        goToHangar = true;
                    }
                    break;
                default:
            }
        }
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    movementRequestedX = 0;
                    break;
                case KeyEvent.VK_Z:
                    shotRequested = false;
                    break;
                case KeyEvent.VK_DOWN:
                    hangarRequested = 0;
                    break;
                default:
            }
        }
        public void keyTyped(KeyEvent e) {}
    }
    
    private class MenuListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Z) {
                startGame = true;
                graphics.drawImage(currentLevel.getBackgroundA(), 0, 0, gameWidth, gameHeight, null);
                graphics.setColor(new Color(0, 255, 0, 255));
                graphics.setFont(new Font("Consolas", Font.BOLD, 72));
                graphics.drawString("... LOADING ...", Level.MainMenu.TEXT_POSITION.getX(), Level.MainMenu.TEXT_POSITION.getY());
                bufferStrategy.show();
            }
        }
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }
    
    public static int getGameWidth() {
        return gameWidth;
    }
    
    public static int getGameHeight() {
        return gameHeight;
    }
    
    public static PlayerObject getPlayer() {
        return player;
    }
    
    private Color getRandomColor() {
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }
    
    public static void main(String[] args) {
        SumSimXT main = new SumSimXT();
        Executors.newFixedThreadPool(1).execute(main);
    }
}
