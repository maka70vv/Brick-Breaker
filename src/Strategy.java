import java.util.ArrayList;
import java.util.List;

public class Strategy {
    private List<StrategyRule> rules;
    private int optimalSpeed; // Оптимальная скорость

    public Strategy() {
        rules = new ArrayList<>();
        optimalSpeed = 15; // Начальная скорость
    }

    public void addRule(StrategyRule rule) {
        rules.add(rule);
    }

    public int getAction(int playerX, int ballX, int ballY, int ballXDir, int ballYDir) {
        for (StrategyRule rule : rules) {
            if (rule.appliesTo(playerX, ballX, ballY, ballXDir, ballYDir)) {
                return rule.getDirection();
            }
        }
        // Простая стратегия: двигаться к мячу, если нет применимых правил
        if (playerX + 25 < ballX) {
            return 1; // Двигаться вправо
        } else if (playerX + 25 > ballX) {
            return -1; // Двигаться влево
        }
        return 0; // Оставаться на месте
    }

    public int getOptimalSpeed() {
        return optimalSpeed;
    }

    public void setOptimalSpeed(int speed) {
        this.optimalSpeed = speed;
    }
}
