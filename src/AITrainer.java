import java.util.ArrayList;
import java.util.List;

public class AITrainer {
    private List<ActionData> trainingData; // Список данных для обучения
    private List<PerformanceData> performanceData; // Результаты работы ИИ
    private boolean trainingMode = false; // Флаг для режима обучения
    private Strategy currentStrategy; // Текущая стратегия

    private long startTime; // Время начала текущей игры
    private int currentScore; // Очки в текущей игре

    public AITrainer() {
        trainingData = new ArrayList<>();
        performanceData = new ArrayList<>();
        currentStrategy = new Strategy();
        resetGameSession();
    }

    // Сброс текущей игровой сессии
    public void resetGameSession() {
        startTime = System.currentTimeMillis();
        currentScore = 0;
    }

    // Запись результата игры (ИИ)
    public void recordPerformance(int finalScore) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        performanceData.add(new PerformanceData(finalScore, duration));
        optimizeStrategy();
    }

    // Метод для записи действий
    public void recordAction(int playerX, int ballX, int ballY, int ballXDir, int ballYDir) {
        if (trainingMode) {
            trainingData.add(new ActionData(playerX, ballX, ballY, ballXDir, ballYDir));
        }
    }

    // Метод для включения режима обучения
    public void enableTrainingMode() {
        trainingMode = true;
    }

    // Метод для отключения режима обучения
    public void disableTrainingMode() {
        trainingMode = false;
    }

    // Обучение модели на собранных данных
    public void trainModel() {
        for (ActionData data : trainingData) {
            if (data.getBallY() > 300 && data.getBallXDir() < 0) {
                currentStrategy.addRule(new StrategyRule(-1, 5)); // Движение влево
            } else {
                currentStrategy.addRule(new StrategyRule(1, 5)); // Движение вправо
            }
        }
    }

    // Оптимизация стратегии на основе результатов
    private void optimizeStrategy() {
        if (!performanceData.isEmpty()) {
            // Анализ производительности
            PerformanceData bestPerformance = performanceData.stream()
                    .min((a, b) -> Long.compare(a.getDuration(), b.getDuration()))
                    .orElse(null);

            if (bestPerformance != null) {
                // Усовершенствуйте стратегию, исходя из лучшей производительности
                currentStrategy.setOptimalSpeed((int) (bestPerformance.getScore() / (bestPerformance.getDuration() / 1000.0)));
            }
        }
    }

    // Возвращает текущую стратегию
    public Strategy getCurrentStrategy() {
        return currentStrategy;
    }

    // Возвращает следующее действие на основе стратегии
    public int getNextAction(int playerX, int ballX, int ballY, int ballXDir, int ballYDir) {
        return currentStrategy.getAction(playerX, ballX, ballY, ballXDir, ballYDir);
    }

}
