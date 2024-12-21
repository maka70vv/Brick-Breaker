public class ActionData {
    private int playerX;
    private int ballX;
    private int ballY;
    private int ballXDir;
    private int ballYDir;

    public ActionData(int playerX, int ballX, int ballY, int ballXDir, int ballYDir) {
        this.playerX = playerX;
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballXDir = ballXDir;
        this.ballYDir = ballYDir;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getBallX() {
        return ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public int getBallXDir() {
        return ballXDir;
    }

    public int getBallYDir() {
        return ballYDir;
    }
}
