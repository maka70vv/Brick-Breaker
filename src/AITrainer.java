import java.util.ArrayList;
import java.util.List;

public class AITrainer {
    private int bestDirection; // Лучшее направление (-1: влево, 1: вправо)
    private double learningRate = 0.1; // Скорость обучения
    private List<Session> sessions; // История действий

    public AITrainer() {
        this.bestDirection = 0; // Начальное значение
        this.sessions = new ArrayList<>();
    }

    // Запись данных о сессии
    public void recordSession(int score, long timeTaken) {
        sessions.add(new Session(score, timeTaken));
        optimizeStrategy();
    }

    // Метод выбора действия
    public int getNextAction(int playerX, int ballX, int ballY, int ballXDir, int ballYDir) {
        // Простая логика: двигаться в сторону мяча
        if (ballX < playerX) {
            return -1; // Влево
        } else if (ballX > playerX) {
            return 1; // Вправо
        } else {
            return 0; // Оставаться на месте
        }
    }

    // Оптимизация стратегии
    private void optimizeStrategy() {
        // Вычисляем среднее время за последние сессии
        double averageTime = sessions.stream()
                .mapToLong(Session::getTimeTaken)
                .average()
                .orElse(0);

        // Ускоряем движение, если среднее время слишком велико
        if (averageTime > 15) {
            learningRate += 0.01; // Увеличиваем скорость обучения
        } else {
            learningRate -= 0.01; // Замедляем обучение
        }

        // Ограничиваем значение скорости обучения
        learningRate = Math.max(0.05, Math.min(0.2, learningRate));
    }

    // Внутренний класс для хранения данных сессии
    private static class Session {
        private final int score; // Очки
        private final long timeTaken; // Затраченное время

        public Session(int score, long timeTaken) {
            this.score = score;
            this.timeTaken = timeTaken;
        }

        public int getScore() {
            return score;
        }

        public long getTimeTaken() {
            return timeTaken;
        }
    }
}
