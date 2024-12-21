import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameGui extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21; // Количество кирпичей на поле
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
    private boolean useAI = false; // Флаг для управления ИИ
    private Map map;

    private AITrainer aiTrainer; // Тренер ИИ

    public GameGui() {
        // Инициализация AITrainer
        aiTrainer = new AITrainer();

        // Таймер для обработки движения платформы
        moveTimer = new Timer(10, e -> {
            if (!useAI) { // Только для ручного управления
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
            }
            repaint();
        });
        moveTimer.start();

        addKeyListener(this);
        map = new Map(3, 7); // Инициализация карты с кирпичами
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this);
        time.start();
    }

    public void paint(Graphics g) {
        // Фон
        g.setColor(Color.black);
        g.fillRect(1, 1, 600, 592);

        // Отрисовка кирпичей
        map.draw((Graphics2D) g);

        // Границы
        g.setColor(Color.black);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Очки
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Score: " + score, 450, 30);

        // Платформа
        g.setColor(Color.blue);
        g.fillRect(playerX, 550, 50, 8);

        // Мяч
        g.setColor(Color.white);
        g.fillOval(ballposX, ballposY, 20, 20);

        // Сообщение о победе
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!", 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 190, 350);
        }

        // Сообщение о проигрыше
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over", 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 190, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.start();
        if (play) {
            if (useAI) {
                controlPaddleWithAI(); // Управление платформой через ИИ
            }

            // Логика столкновения платформы с мячом
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 50, 8))) {
                ballYdir = -ballYdir;
            }

            // Логика столкновения мяча с кирпичами
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

                        if (ballRect.intersects(rect)) {
                            map.setBrickValue(0, i, j); // Убираем кирпич
                            totalBricks--; // Уменьшаем количество оставшихся кирпичей
                            score += 5; // Добавляем очки

                            // Меняем направление мяча
                            if (ballposX + 19 <= rect.x || ballposX + 1 >= rect.x + rect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            // Движение мяча
            ballposX += ballXdir;
            ballposY += ballYdir;

            // Отражение мяча от стен
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
        int action = aiTrainer.getNextAction(playerX, ballposX, ballposY, ballXdir, ballYdir); // Действие от ИИ
        if (action == -1 && playerX > 0) {
            playerX -= 5; // Движение влево
        } else if (action == 1 && playerX < 540) {
            playerX += 5; // Движение вправо
        }

        // Ограничение платформы в пределах поля
        if (playerX < 0) {
            playerX = 0;
        } else if (playerX > 540) {
            playerX = 540;
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
                totalBricks = 21; // Сброс количества кирпичей
                map = new Map(3, 7); // Пересоздание карты
                repaint();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            useAI = !useAI; // Включить или отключить ИИ
        }
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
    public void keyTyped(KeyEvent e) {
    }
}
