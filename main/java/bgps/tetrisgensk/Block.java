package bgps.tetrisgensk;

import javafx.scene.paint.Color;

public class Block {

    private int blockNumber;
    private int X;
    private int Y;
    private boolean active;
    private Color color = Color.RED;
    // todo: Make colors random

    public Block(int blockNumber, int x, int y, boolean active) {
        this.blockNumber = blockNumber;
        X = x;
        Y = y;
        this.active = active;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Color getColor() {
        return color;
    }
}

