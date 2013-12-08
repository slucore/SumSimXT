/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

public class HighScoreCommunicator implements Runnable {
    private final String HOST = "127.0.0.1";
    private final int HOST_PORT = 23750;
    private Socket connection = new Socket();
    private Scanner input;
    private Formatter output;
    private List<String> highScores = new ArrayList<>();
    private boolean highScoresReady = false;
    private Executor executor = Executors.newCachedThreadPool();

    public HighScoreCommunicator() {
        try {
            connection = new Socket(InetAddress.getByName(HOST), HOST_PORT);
            input = new Scanner(connection.getInputStream());
            output = new Formatter(connection.getOutputStream());
            System.out.format("Connected to high scores server.\n");
            executor.execute(this);
        } catch (Exception e) {
            System.out.format("High Score server not found.\n");
        }
    }

    public void run() {
        if (highScoresReady) {
            sendHighScores();
        } else {
            boolean done = false;
            while (!done) {
                if (input.hasNextLine()) {
                    String cur = input.nextLine();
                    System.out.format("\t%s\n", cur);
                    if (cur.startsWith("DONE")) {
                        done = true;
                    } else {
                        highScores.add(cur);
                    }
                }
            }
            highScoresReady = true;
        }
    }

    public void sendHighScores() {
        int highScoreA, highScoreB, highScoreC;
        try {
            highScoreA = Integer.parseInt(highScores.get(1));
            highScoreB = Integer.parseInt(highScores.get(3));
            highScoreC = Integer.parseInt(highScores.get(5));            
        } catch (NumberFormatException ex) {
            System.out.format("High scores file is corrupted.\n");
            highScoreA = highScoreB = highScoreC = 9999;
        }
        List<String> newHighScores = new ArrayList<>();
        String initials = "AAA";
        int score = SumSimXT.getPlayer().getGold();
        if (score > highScoreC) {
            initials = JOptionPane.showInputDialog("New High Score! Input your initials:");
        }
        initials = initials.substring(0,3);
        if (score > highScoreA) {
            newHighScores.add(initials);
            newHighScores.add(String.format("%d", score));
            for (int i = 2; i <= 5; i++) {
                newHighScores.add(highScores.get(i));
            }
        } else if (score > highScoreB) {
            newHighScores.add(highScores.get(0));
            newHighScores.add(highScores.get(1));
            newHighScores.add(initials);
            newHighScores.add(String.format("%d", score));
            newHighScores.add(highScores.get(4));
            newHighScores.add(highScores.get(5));
        } else if (score > highScoreC) {
            for (int i = 0; i <= 3; i++) {
                newHighScores.add(highScores.get(i));
            }
            newHighScores.add(initials);
            newHighScores.add(String.format("%d", score));
        }

        output.format("HIGH SCORE\n");
        output.flush();
        if (!newHighScores.isEmpty()) {
            for (String str : newHighScores) {
                output.format(str + "\n");
                output.flush();
            }
        }
        output.format("DONE\n");
        output.flush();
    }

    public boolean highScoresReady() {
        return highScoresReady;
    }

    public List<String> getHighScores() {
        return highScores;
    }
}
