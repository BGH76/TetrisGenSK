package bgps.tetrisgensk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
    // todo: save score to file on gameover
    public void saveToFile(String name) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("scores.txt",true))) {
            bw.write(getScore() + " -- " + name);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
