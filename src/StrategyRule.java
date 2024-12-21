public class StrategyRule {
    private int direction; // -1: влево, 1: вправо
    private int speed;

    public StrategyRule(int direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }

    // Проверяем, применимо ли правило к текущему состоянию
    public boolean appliesTo(int playerX, int ballX, int ballY, int ballXDir, int ballYDir) {
        // Если мяч движется вниз и находится в пределах игрового поля
        if (ballY > 300 && ballYDir > 0) {
            // Если платформа находится левее мяча
            if (playerX + 25 < ballX) {
                direction = 1; // Двигаться вправо
                return true;
            }
            // Если платформа находится правее мяча
            else if (playerX + 25 > ballX) {
                direction = -1; // Двигаться влево
                return true;
            }
        }
        // Если мяч находится высоко или движется вверх, остаёмся на месте
        direction = 0;
        return false;
    }

    public int getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }
}
