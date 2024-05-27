//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.util.ArrayList;
//import java.util.Random;
//
//public class SnakeLite extends JFrame {
//    private static final int BOARD_SIZE = 20;
//    private static final int CELL_SIZE = 20;
//
//    private ArrayList<Point> snake;
//    private Point food;
//    private Direction direction;
//
//    private static final int WIDTH = 300, HEIGHT = 300; // Size of the game window
//    private JPanel gamePanel;
//
//    public SnakeLite() {
//        setResizable(false);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        gamePanel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                doDrawing(g);
//                showScore(g);
//            }
//        };
//        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        gamePanel.setFocusable(true);
//        gamePanel.requestFocusInWindow();
//
//        add(gamePanel);
//        pack();
//        setLocationRelativeTo(null);
//
//        initGame();
//        initKeyBindings();
//    }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//       x     }
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//        });
//
//        initGame();
//        startGameLoop();
//    }
//
//    private void initGame() {
//        snake = new ArrayList<>();
//        snake.add(new Point(BOARD_SIZE / 2, BOARD_SIZE / 2));
//        direction = Direction.RIGHT;
//        spawnFood();
//    }
//
//    private void startGameLoop() {
//        new Thread(() -> {
//            while (true) {
//                updateGame();
//                gamePanel.repaint();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void updateGame() {
//        // Move the snake
//        Point head = new Point(snake.get(0));
//        switch (direction) {
//            case UP:
//                head.y--;
//                break;
//            case DOWN:
//                head.y++;
//                break;
//            case LEFT:
//                head.x--;
//                break;
//            case RIGHT:
//                head.x++;
//                break;
//        }
//
//        // Check for collisions
//        if (head.x < 0 || head.x >= BOARD_SIZE || head.y < 0 || head.y >= BOARD_SIZE || snake.contains(head)) {
//            // Game over
//            return;
//        }
//
//        snake.add(0, head);
//        if (head.equals(food)) {
//            spawnFood();
//        } else {
//            snake.remove(snake.size() - 1);
//        }
//    }
//
//    private void spawnFood() {
//        Random random = new Random();
//        food = new Point(random.nextInt(BOARD_SIZE), random.nextInt(BOARD_SIZE));
//        while (snake.contains(food)) {
//            food = new Point(random.nextInt(BOARD_SIZE), random.nextInt(BOARD_SIZE));
//        }
//    }
//
//    private void drawGame(Graphics g) {
//        // Draw the game board
//        g.setColor(Color.WHITE);
//        for (int x = 0; x < BOARD_SIZE; x++) {
//            for (int y = 0; y < BOARD_SIZE; y++) {
//                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//            }
//        }
//
//        // Draw the snake
//        g.setColor(Color.GREEN);
//        for (Point segment : snake) {
//            g.fillRect(segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//        }
//
//        // Draw the food
//        g.setColor(Color.RED);
//        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//    }
//
//    private void handleKeyPress(int keyCode) {
//        switch (keyCode) {
//            case KeyEvent.VK_UP:
//            case KeyEvent.VK_W:
//                if (direction != Direction.DOWN) {
//                    direction = Direction.UP;
//                }
//                break;
//            case KeyEvent.VK_DOWN:
//            case KeyEvent.VK_S:
//                if (direction != Direction.UP) {
//                    direction = Direction.DOWN;
//                }
//                break;
//            case KeyEvent.VK_LEFT:
//            case KeyEvent.VK_A:
//                if (direction != Direction.RIGHT) {
//                    direction = Direction.LEFT;
//                }
//                break;
//            case KeyEvent.VK_RIGHT:
//            case KeyEvent.VK_D:
//                if (direction != Direction.LEFT) {
//                    direction = Direction.RIGHT;
//                }
//                break;
//        }
//    }
//
//    private enum Direction {
//        UP, DOWN, LEFT, RIGHT
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(SnakeLite::new);
//    }
//}