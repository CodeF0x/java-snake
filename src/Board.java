import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Board extends JPanel implements KeyListener {
    int highscore;
    Snake player;
    Food food;
    ArrayList<SnakePart> snakeCoordinates;
    Image background;
    Media deathSound;
    Media eatingSound;
    Media openingSong;

    public Board() {
        // Initialize a JFX here, so MediaPlayer won't complain. Probably a bad practice?
        new JFXPanel();
        this.highscore = 0;

        this.snakeCoordinates = new ArrayList<>();

        snakeCoordinates.add(new SnakePart(150, 150));
        snakeCoordinates.add(new SnakePart(140, 150));
        snakeCoordinates.add(new SnakePart(130, 150));
        snakeCoordinates.add(new SnakePart(120, 150));

        this.player = new Snake(snakeCoordinates);

        this.food = new Food();

        try {
            this.background = ImageIO.read(new File("resources/background.png"));
            this.deathSound = new Media(new File("resources/death-sound.mp3").toURI().toString());
            this.eatingSound = new Media(new File("resources/eating-sound.mp3").toURI().toString());
            this.openingSong = new Media(new File("resources/opening-song.mp3").toURI().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new MediaPlayer(this.openingSong).play();
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Snake");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(3);

        JPanel highscoreContainer = new JPanel();
        highscoreContainer.setBackground(Color.lightGray);

        ScoreSaver scoreSaver = new ScoreSaver();

        JTextPane highscore = new JTextPane();
        highscore.setText("Current score: 0");
        highscore.setEnabled(false);
        highscore.setSize(250, 50);

        JTextPane highscoreMax = new JTextPane();
        try {
            highscoreMax.setText("Highest score: " + scoreSaver.loadHighscore());
        } catch (IOException e) {
            System.out.println("Couldn't read highscore :(");
        }
        highscoreMax.setEnabled(false);
        highscore.setSize(250, 50);

        highscoreContainer.add(BorderLayout.WEST, highscore);
        highscoreContainer.add(BorderLayout.EAST, highscoreMax);

        // When changing sizes here, also need to change sizes in clear method
        Board board = new Board();
        board.setSize(500, 500);

        frame.add(BorderLayout.NORTH, highscoreContainer);
        frame.add(BorderLayout.CENTER, board);
        frame.setSize(500, 500);
        frame.addKeyListener(board);
        frame.setVisible(true);

        board.run(board, highscore, scoreSaver, highscoreMax);
        //});
    }

    @Override
    public void paintComponent(Graphics g) {
        clear(g);
        g.drawImage(this.background, 0, 0, 500, 500, null);

        // Snake
        this.player.coordinates.forEach(snakePart -> {
            g.drawImage(this.player.snakePart, snakePart.x, snakePart.y, 10, 10, null);
        });
        // Food
        g.drawImage(this.food.apple, this.food.foodX, this.food.foodY, 10, 10, null);

        if (ateFood(this.food, g)) {
            this.food.spawn(440, 490);
            this.player.grow();
        }
    }

    public void clear(Graphics g) {
        g.clearRect(0, 0, 500, 500);
    }

    @Override
    public void update(Graphics g) {
        paintComponent(g);
    }

    public boolean ateFood(Food food, Graphics g) {

        if (this.player.coordinates.get(0).x == food.foodX && this.player.coordinates.get(0).y == food.foodY) {
            g.clearRect(food.foodX, food.foodY, 10, 10);
            new MediaPlayer(this.eatingSound).play();
            return true;
        }
        return false;
    }

    public boolean hitSomething() {
        boolean hitLeftWall = this.player.coordinates.get(0).x < 0;
        boolean hitUpperWall = this.player.coordinates.get(0).y < 0;
        boolean hitRightWall = this.player.coordinates.get(0).x > getWidth();
        boolean hitLowerWall = this.player.coordinates.get(0).y > getHeight() - 10;

        // Check if snake has eaten itself
        for (int i = 1; i < this.player.coordinates.size(); i++) {
            if (this.player.coordinates.get(i).x == this.player.coordinates.get(0).x && this.player.coordinates.get(i).y == this.player.coordinates.get(0).y) {
                return true;
            }
        }

        if (hitLeftWall || hitUpperWall || hitRightWall || hitLowerWall) {
            return true;
        }

        return false;
    }

    public void gameOver(Graphics g) {
        clear(g);
        g.drawImage(this.background, 0, 0, 500, 500, null);

        new MediaPlayer(this.deathSound).play();

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
        // todo set correct coords without fancy maths
        g.drawString("Game over!", getWidth() / 2 - 70, getHeight() / 2);
    }

    public void increaseHighscore(JTextPane highscorePanel) {
        this.highscore += 10;

        highscorePanel.setText("Current score: " + Integer.toString(this.highscore));

    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        // Prevent the snake from reversing
        boolean isGoingLeft = this.player.dx == -10;
        boolean isGoingUp = this.player.dy == -10;
        boolean isGoingRight = this.player.dx == 10;
        boolean isGoingDown = this.player.dy == 10;

        if (keyCode == 37 && !isGoingRight) {
            this.player.dx = -10;
            this.player.dy = 0;
        } else if (keyCode == 38 && !isGoingDown) {
            this.player.dx = 0;
            this.player.dy = -10;
        } else if (keyCode == 39 && !isGoingLeft) {
            this.player.dx = 10;
            this.player.dy = 0;
        } else if (keyCode == 40 && !isGoingUp) {
            this.player.dx = 0;
            this.player.dy = 10;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        return;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        return;
    }

    public void run(Board board, JTextPane highscorePanel, ScoreSaver scoreSaver, JTextPane highscoreMax) {
        Thread threaddyKrueger = new Thread() {
            boolean initiallySpawned = false;

            @Override
            public void run() {
                int maxHighscore = 0;
                while (true) {
                    Graphics g = board.getGraphics();

                    if (hitSomething()) {
                        try {
                            maxHighscore = Integer.parseInt(highscoreMax.getText());
                        } catch (Exception e) {
                            try {
                                // Try to get the highest score
                                int currentMaxHighscore = Integer.parseInt(highscoreMax.getText().split(" ")[2]);
                                if (highscoreMax.getText().equals("Highest Score: None yet")) {
                                    scoreSaver.saveHighscore(Integer.toString(highscore));
                                } else if (highscore > currentMaxHighscore) {
                                    scoreSaver.saveHighscore(Integer.toString(highscore));
                                    System.out.println("Highscore saved");
                                }
                            } catch (Exception ex) {
                                // If getting the score in line 193 fails, write 0 anyways
                                try {
                                    scoreSaver.saveHighscore(Integer.toString(highscore));
                                } catch (IOException exc) {

                                }
                            }
                        }

                        this.interrupt();
                        gameOver(g);
                        return;
                    }

                    if (!initiallySpawned) {
                        food.spawn(440, 490);
                        initiallySpawned = true;
                    }

                    player.move();

                    if (ateFood(food, g)) {
                        food.spawn(440, 490);
                        player.grow();
                        increaseHighscore(highscorePanel);
                    }

                    update(g);
                    try {
                        Thread.sleep(1000 / 10);
                    } catch (InterruptedException e) {
                        // Nothing
                    }
                }
            }
        };
        threaddyKrueger.start();
    }
}
