/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Sounds {

    private static String soundDir;
    
//    private static Clip levelSong;
//    private static Clip bossSong;
//    private static Clip hangarSong;
//    private static Clip mobShoot;
//    private static Clip mobDeath;
//    private static Clip shoot;
//    private static Clip laser;
//    private static Clip superBomb;
//    private static Clip shieldDown;
//    private static Clip menu;
    
    private static final String levelSongString = "level_ambient.wav";
    private static final String bossSongString = "boss_song.wav";
    private static final String hangarSongString = "hangar.wav";
    private static final String mobShootString = "mob_shoot.wav";
    private static final String mobDeathString = "hurt.wav";
    private static final String shootString = "player_shoot.wav";
    private static final String laserString = "player_laser.wav";
    private static final String superBombString = "superbomb.wav";
    private static final String menuString = "menu.wav";
    private static final String playerHurtString = "player_hurt.wav";
    private static final String playerDeathString = "player_death.wav";
    private static final String coinString = "coin.wav";
    private static final String warpSpeedString = "warp_speed.wav";
    
    public Sounds(String soundDir) {
        this.soundDir = soundDir;
//        try {
//            levelSong = AudioSystem.getClip();
//            bossSong = AudioSystem.getClip();
//            hangarSong = AudioSystem.getClip();
//            mobShoot = AudioSystem.getClip();
//            mobDeath = AudioSystem.getClip();
//            shoot = AudioSystem.getClip();
//            laser = AudioSystem.getClip();
//            superBomb = AudioSystem.getClip();
//            shieldDown = AudioSystem.getClip();
//            menu = AudioSystem.getClip();
//
////            hangarSong.open(AudioSystem.getAudioInputStream(new File(soundDir + hangarSongString)));
////            mobShoot.open(AudioSystem.getAudioInputStream(new File(soundDir + mobShootString)));
////            mobDeath.open(AudioSystem.getAudioInputStream(new File(soundDir + mobDeathString)));
////            shoot.open(AudioSystem.getAudioInputStream(new File(soundDir + shootString)));
////            laser.open(AudioSystem.getAudioInputStream(new File(soundDir + laserString)));
////            superBomb.open(AudioSystem.getAudioInputStream(new File(soundDir + superBombString)));
////            menu.open(AudioSystem.getAudioInputStream(new File(soundDir + menuString)));
//        } catch (LineUnavailableException ex) {
//            System.err.format("Audio system unavailable.\n");
//        }
////        } catch (UnsupportedAudioFileException | IOException ex) {
////            System.out.format("Audio system error.\n");
////        }
    }
    
    public Clip reopenClip(String filename) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(soundDir + filename)));
            return clip;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            System.out.format("Audio system error.\n");
        }
        return null;
    }

    public Clip getLevelSong() { return reopenClip( levelSongString); }
    public Clip getBossSong() { return reopenClip( bossSongString); }
    public Clip getHangarSong() { return reopenClip( hangarSongString); }
    public Clip getMobShoot() { return reopenClip( mobShootString); }
    public Clip getMobDeath() { return reopenClip( mobDeathString); }
    public Clip getShoot() { return reopenClip( shootString); }
    public Clip getLaser() { return reopenClip( laserString); }
    public Clip getSuperBomb() { return reopenClip( superBombString); }
    public Clip getMenu() { return reopenClip( menuString); }
    public Clip getPlayerHurt() { return reopenClip( playerHurtString); }
    public Clip getPlayerDeath() { return reopenClip( playerDeathString); }
    public Clip getCoin() { return reopenClip( coinString); }
    public Clip getWarpSpeed() { return reopenClip( warpSpeedString); }
    
}
