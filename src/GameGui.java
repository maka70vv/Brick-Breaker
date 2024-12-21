import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameGui extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 28;
    private Timer time;
    private int delay = 1;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private Timer moveTimer;


    private int playerX = 300;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private boolean useAI = true; // Флаг для управления ИИ
    private Map map;

    public GameGui() {
        moveTimer = new Timer(10, e -> {
            if (moveLeft) {
                if (playerX > 0) {
                    playerX -= 15; // Скорость движения влево
                }
            }
            if (moveRight) {
                if (playerX < 540) {
                    playerX += 15; // Скорость движения вправо
                }
            }
            repaint();
        });
        moveTimer.start();


        addKeyListener(this);
        map = new Map(3, 7);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this);
        time.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 600, 592);

        map.draw((Graphics2D) g);

        g.setColor(Color.black);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("" + score, 450, 30);

        g.setColor(Color.blue);
        g.fillRect(playerX, 550, 50, 8);

        g.setColor(Color.white);
        g.fillOval(ballposX, ballposY, 20, 20);

        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("You Won ", 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 280, 350);
        }

        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Game Over", 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 190, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.start();
        if (play) {
            if (useAI) {
                controlPaddleWithAI();
            }

            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 560) {
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    private void controlPaddleWithAI() {
        int paddleCenter = playerX + 25;
        int ballFutureX = ballposX + ballXdir * 10;

        if (paddleCenter < ballFutureX) {
            playerX += Math.min(10, ballFutureX - paddleCenter);
        } else if (paddleCenter > ballFutureX) {
            playerX -= Math.min(10, paddleCenter - ballFutureX);
        }

        if (playerX < 0) {
            playerX = 0;
        } else if (playerX > 540) {
            playerX = 540;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (!useAI) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveRight = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moveLeft = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new Map(3, 7);
                repaint();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            useAI = !useAI; // Включить или отключить ИИ
        }
    }
}
