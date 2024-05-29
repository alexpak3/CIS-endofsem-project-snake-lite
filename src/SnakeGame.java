import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SnakeGame extends JFrame {
    private boolean slowDownActive = false;
    private long slowDownStartTime;
    private final int WIDTH = 300, HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private int[] x = new int[900];
    private int[] y = new int[900];
    private int bodyParts = 4;
    private ArrayList<food> foods = new ArrayList<food>();
    private int fruitsEaten = 0;
    private char direction = 'R';
    private boolean running = true;
    private Timer timer;
    private JPanel gamePanel;

    public SnakeGame() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                doDrawing(g);
                showScore(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        initGame();
        initKeyBindings();
    }

    private void initGame() {
        slowDownActive = false;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }
        placeFood();
        placebFood();
        placePU();
        timer = new Timer(63, e -> gameUpdate());
        timer.start();
        running = true;
    }


    //movement used ai to find me videos to teach me how to do the movement
    private void initKeyBindings() {
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveLeft");
        gamePanel.getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'R') direction = 'L';
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        gamePanel.getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'R') direction = 'L';
            }
        });

        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveRight");
        gamePanel.getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'L') direction = 'R';
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        gamePanel.getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'L') direction = 'R';
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "quitGame");
        gamePanel.getActionMap().put("quitGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    dispose();
                }
            }
        });

        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveUp");
        gamePanel.getActionMap().put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'D') direction = 'U';
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        gamePanel.getActionMap().put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'D') direction = 'U';
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveDown");
        gamePanel.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'U') direction = 'D';
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        gamePanel.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != 'U') direction = 'D';
            }
        });

        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restartGame");
        gamePanel.getActionMap().put("restartGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) restartGame();
            }
        });
    }

    private void placeFood() {
        int r = (int) (Math.random() * ((WIDTH - DOT_SIZE) / DOT_SIZE));
        int xPos = r * DOT_SIZE;
        int yPos = (int) (Math.random() * ((HEIGHT - DOT_SIZE) / DOT_SIZE));
        yPos *= DOT_SIZE;
        foods.add(new food(xPos, yPos, DOT_SIZE, 'f'));
    }

    private void placebFood() {
        int r = (int) (Math.random() * ((WIDTH - DOT_SIZE) / DOT_SIZE));
        int xPos = r * DOT_SIZE;
        int yPos = (int) (Math.random() * ((HEIGHT - DOT_SIZE) / DOT_SIZE));
        yPos *= DOT_SIZE;
        foods.add(new food(xPos, yPos, DOT_SIZE, 'b'));
    }

    private void placePU() {
        int r = (int) (Math.random() * ((WIDTH - DOT_SIZE) / DOT_SIZE));
        int xPos = r * DOT_SIZE;
        int yPos = (int) (Math.random() * ((HEIGHT - DOT_SIZE) / DOT_SIZE));
        yPos *= DOT_SIZE;
        foods.add(new food(xPos, yPos, DOT_SIZE, 'p'));
    }

    private void gameUpdate() {
        if (running) {
            if (slowDownActive) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - slowDownStartTime;

                // If 3 seconds have passed, deactivate slowdown
                if (elapsedTime >= 3000) {
                    slowDownActive = false;
                } else {
                    // Slow down the game update rate
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            move();
            checkFood();
            checkCollisions();
        }
        gamePanel.repaint();
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= DOT_SIZE;
                break;
            case 'D':
                y[0] += DOT_SIZE;
                break;
            case 'L':
                x[0] -= DOT_SIZE;
                break;
            case 'R':
                x[0] += DOT_SIZE;
                break;
        }
    }

    private void checkFood() {
        ArrayList<food> foodsCopy = new ArrayList<>(foods);
        for (food food : foodsCopy) {
            if ((x[0] == food.getX()) && (y[0] == food.getY())) {
                if (food.getType() == 'f') {
                    bodyParts++;
                    fruitsEaten++;
                    placeFood();
                } else if (food.getType() == 'b') {
                    bodyParts += 3;
                    fruitsEaten += 3;
                    // Schedule the placement of a new power-up after 10 seconds
                    Timer bFruitTimer = new Timer(10000, e -> {
                        placebFood();
                    });
                    bFruitTimer.setRepeats(false);
                    bFruitTimer.start();
                } else if (food.getType() == 'p') {
                    slowDownActive = true;
                    slowDownStartTime = System.currentTimeMillis();
                    foods.remove(food);

                    // Schedule the placement of a new power-up after 10 seconds
                    Timer powerUpTimer = new Timer(10000, e -> {
                        slowDownActive = false;
                        placePU();
                    });
                    powerUpTimer.setRepeats(false);
                    powerUpTimer.start();
                }
                foods.remove(food);
            }
        }
    }

    private void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if (y[0] < 0 || y[0] >= HEIGHT || x[0] < 0 || x[0] >= WIDTH) {
            running = false;
        }

        if (!running) {
            timer.stop();
            gamePanel.repaint(); // Repaint the game panel to show the "Game Over" screen
        }
    }


    private void restartGame() {
        bodyParts = 4;
        fruitsEaten = 0;
        direction = 'R';
        slowDownActive = false;
        foods.clear();

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }

        placeFood();
        placebFood();
        placePU();

        timer.start();
        running = true;
    }

    private void doDrawing(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (running) {
            for (food food : foods) {
                if (food.getType() == 'f') {
                    g.setColor(Color.green);
                } else if (food.getType() == 'p') {
                    g.setColor(Color.MAGENTA);
                } else if (food.getType() == 'b') {
                    g.setColor(new Color(0, 255, 150));
                }
                g.fillRect(food.getX(), food.getY(), food.getSize(), food.getSize());
            }

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(0, 255, 255));
                    g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
                } else {
                    g.setColor(new Color(0, 211, 255));
                    g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
                }
            }
        } else {
            gameOver(g);
        }
    }

    private void showScore(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.drawString("Fruits eaten: " + fruitsEaten, 10, 20);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.drawString("Game Over", WIDTH / 2 - 50, HEIGHT / 2 - 20);
        g.drawString("Press R to play again", WIDTH / 2 - 80, HEIGHT / 2);
        g.drawString("Press esc to quit the game", WIDTH / 2 - 90, HEIGHT / 2 + 20);
        g.drawString("Your Score: " + fruitsEaten, WIDTH / 2 - 50, HEIGHT / 2 + 40);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new SnakeGame();
            frame.setVisible(true);
        });
    }
}