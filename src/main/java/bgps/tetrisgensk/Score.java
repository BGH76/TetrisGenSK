package bgps.tetrisgensk;

public class Score {
    // todo: save score to file
    private enum lineClearValues {
        ONE(10),
        TWO(30),
        THREE(50),
        FOUR(100);
        private final int value;
        lineClearValues(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    private int score;
    Score() {
        this.score = 0;
    }
    public int getScore() {
        return score;
    }
    private void addScore(int score) {
        this.score += score;
    }
    public void clearScore() {
        this.score = 0;
    }
    public void lineClear(int lines) {
        switch (lines) {
            case 1 -> addScore(lineClearValues.ONE.getValue());
            case 2 -> addScore(lineClearValues.TWO.getValue());
            case 3 -> addScore(lineClearValues.THREE.getValue());
            case 4 -> addScore(lineClearValues.FOUR.getValue());
        }
    }
}
