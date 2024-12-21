public class PerformanceData {
    private int score; // Количество очков
    private long duration; // Время в миллисекундах

    public PerformanceData(int score, long duration) {
        this.score = score;
        this.duration = duration;
    }

    public int getScore() {
        return score;
    }

    public long getDuration() {
        return duration;
    }
}
