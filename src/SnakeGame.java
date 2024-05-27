import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class SnakeGame extends JFrame {

    private boolean slowDownActive = false;
    private final int WIDTH = 300, HEIGHT = 300; // Size of the game window
    private final int DOT_SIZE = 10; // Size of the snake and food
    private int[] x = new int[900]; // x coordinates of the snake's joints
    private int[] y = new int[900]; // y coordinates of the snake's joints
    private int bodyParts = 4; // Initial size of the snake

    private ArrayList<food> foods = new ArrayList<food>();
    private int fruitsEaten = 0; // Counter for the number of fruits eaten
    private char direction = 'R'; // Initial direction of the snake
    private boolean running = false; // Game state
    private Timer timer; // Timer for game updates
    private JPanel gamePanel; // Panel to control the game drawing area

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

        if (slowDownActive) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - slowDownStartTime;
            // If 5 seconds have passed, deactivate slow down
            if (elapsedTime >= 2500) {
                slowDownActive = false;
            } else {
                slowDownActive = true;
                try {
                    Thread.sleep(10000); // Adjust the delay as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        gamePanel.repaint();
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
        timer = new Timer(63, e -> gameUpdate());//speed of the snake
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
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "quitGame");
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
        int x = r * DOT_SIZE;
        int y = (int) (Math.random() * ((HEIGHT - DOT_SIZE) / DOT_SIZE));
        y *= DOT_SIZE;
        foods.add(new food(x, y, DOT_SIZE, 'f'));
    }

    private void placebFood() {
        int r = (int) (Math.random() * ((WIDTH - DOT_SIZE) / DOT_SIZE));
        int x = r * DOT_SIZE;
        int y = (int) (Math.random() * ((HEIGHT - DOT_SIZE) / DOT_SIZE));
        y *= DOT_SIZE;
        foods.add(new food(x, y, DOT_SIZE, 'b'));
    }

    private void placePU() {
        int r = (int) (Math.random() * ((WIDTH - DOT_SIZE) / DOT_SIZE));
        int x = r * DOT_SIZE;
        int y = (int) (Math.random() * ((HEIGHT - DOT_SIZE) / DOT_SIZE));
        y *= DOT_SIZE;
        foods.add(new food(x, y, DOT_SIZE, 'p'));
    }
    private void gameUpdate() {
        if (running) {
            if (slowDownActive) {
                // Slow down the game update rate
                try {
                    Thread.sleep(100); // Adjust the delay as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
            case 'U': y[0] -= DOT_SIZE; break;
            case 'D': y[0] += DOT_SIZE; break;
            case 'L': x[0] -= DOT_SIZE; break;
            case 'R': x[0] += DOT_SIZE; break;
        }
    }
    private void checkFood() {
        ArrayList<food> foodsCopy = new ArrayList<>(foods); // Create a copy of the foods list
        for (food food : foodsCopy) {
            if ((x[0] == food.getX()) && (y[0] == food.getY())) {
                if (food.getType() == 'f') {
                    bodyParts++;
                    fruitsEaten++;
                    placeFood(); // Place a new regular fruit
                } else if (food.getType() == 'b') {
                    bodyParts += 3;
                    fruitsEaten += 3;

                    Timer powerUpTimer = new Timer(10000, e -> {
                        slowDownActive = false;
                        placebFood();
                    });
                    powerUpTimer.setRepeats(false); // Execute the task only once
                    powerUpTimer.start();
                } else if (food.getType() == 'p') {
                    System.out.println("Power-up is eaten");
                    slowDownActive = true;
                    foods.remove(food); // Remove the eaten power-up from the list

                    // Schedule the placement of a new power-up after 1.5 seconds
                    Timer powerUpTimer = new Timer(2000, e -> {
                        slowDownActive = false;
                        placePU();
                    });
                    powerUpTimer.setRepeats(false); // Execute the task only once
                    powerUpTimer.start();
                }
                foods.remove(food); // Remove the eaten food from the original list
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
        }
    }
    private void restartGame() {
        bodyParts = 4;
        fruitsEaten = 0;
        direction = 'R';
        slowDownActive = false;
        foods.clear(); // Clear the foods list

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

            // Draw the snake
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
        g.setColor(new Color (255,255,255));
        g.drawString("Fruits eaten: " + fruitsEaten, 10, 20);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.drawString("Game Over", WIDTH / 2 - 50, HEIGHT / 2-20);
        g.drawString("Press R to play again", WIDTH/2-80, HEIGHT/2);
        g.drawString("Press esc to quit the game", WIDTH/2-90,HEIGHT/2+20);
        g.drawString("Your Score: " + fruitsEaten, WIDTH / 2 - 50, HEIGHT / 2 + 40);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new SnakeGame();
            frame.setVisible(true);
        });
    }
}