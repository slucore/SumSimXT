/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.Dimension;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class HighScoresServer extends JFrame implements Runnable {

    private ServerSocket server;
    private final int PORT_NUMBER = 23750;
    private Executor executor = Executors.newCachedThreadPool();
    private File highScoresFile;
    private String classPath = HighScoresServer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private JPanel overPanel = new JPanel();
    private int id = 0;
    
    public static void main(String args[]) {
        HighScoresServer host = new HighScoresServer();
    }
    
    public HighScoresServer() {
        super("SumSimXT HighScore Server");
        this.add(overPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setPreferredSize(new Dimension(300,100));
        this.setVisible(true);
        this.pack();
        highScoresFile = new File(classPath + "SumSimXT_HighScores.txt");
        try {
            server = new ServerSocket(PORT_NUMBER, 10);
            System.out.format("ServerSocket acquired.\n");
        } catch (IOException e) {
            System.out.format("Requested server port not available.\n");
        }
        executor.execute(this);
    }

    public void run() {
        try {
            while (true) {
                Player player = new Player(server.accept());
                System.out.format("Player %d connected.\n", ++id);
                executor.execute(player);
            }
        } catch (IOException e) {
            System.out.format("I/O exception while connecting player.\n");
        }
    }
    
    private class Player implements Runnable {
        private Socket socket;
        private Scanner input;
        private Formatter output;
        private String initials, score;
        private boolean getInitials = false;
        private boolean getScores = false;
        private boolean done = false;
        public Player(Socket socket) {
            this.socket = socket;
            try {
                input = new Scanner(socket.getInputStream());
                output = new Formatter(socket.getOutputStream());
            } catch (IOException e) {
                System.out.format("I/O exception while connecting player.\n");
            }
        }
        
        public void sendHighScores() {
            System.out.format("Sending scores:\n");
            try {
                BufferedReader br = new BufferedReader(new FileReader(highScoresFile));
                Scanner fileScanner = new Scanner(br);
                while (fileScanner.hasNextLine()) {
                    String cur = fileScanner.nextLine() + "\n";
                    output.format(cur);
                    output.flush();
                    System.out.format("\t%s", cur);
                }
            } catch (FileNotFoundException ex) {
                System.out.format("Couldn't find high scores file.\n");
            } finally {
                output.format("DONE\n");
                output.flush();
            }
        }
        
        public void run() {
            sendHighScores();
            
            List<String> newHighScores = new ArrayList<>();
            String command = "";
            System.out.format("Waiting for new high scores.\n");
            boolean done = false;
            boolean getScores = false;
            while (!done) {
                if (input.hasNextLine()) {
                    command = input.nextLine();
                }
//                if (getInitials) {
//                    initials = command;
//                    getInitials = false;
//                    getScore = true;
//                } else if (getScore) {
//                    score = command;
//                    getScore = false;
//                    done = true;
                if (getScores) {
                    if (command.startsWith("DONE")) {
                        System.out.format("done\n");
                        done = true;
                    } else {
                        System.out.format("\t%s\n", command);
                        newHighScores.add(command);
                    }
                } else if (command.startsWith("HIGH SCORE")) {
                    System.out.format("Receiving new high scores:\n");
                    getScores = true;
                }
            }
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.format("Socket might not have been closed.\n");
            }
            if (!newHighScores.isEmpty()) {
                try {
                    highScoresFile = new File(classPath + "SumSimXT_HighScores.txt");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(highScoresFile));
                    for (String str : newHighScores) {
                        bw.write(str + "\n");
                        bw.flush();
                    }
                    bw.close();
                    System.out.format("Finished writing new high scores file.\n");
                } catch (IOException ex) {
                    System.out.format("Exception while writing new high scores file.\n");
                }
            }
        }
        
    }
    
}
