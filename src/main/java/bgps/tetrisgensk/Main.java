package bgps.tetrisgensk;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private static Timeline timeline;
    private final int GAME_WIDTH = 250;
    private final int GAME_HEIGHT = 500;
    private final int SIZE = 25;
    private final Controller controller = new Controller();
    private final BlockFactory blockFactory = new BlockFactory();
    private final ArrayList<Block> activeBlockList = new ArrayList<>(); // This holds the current block moving. When block active is false remove from this list and add to another.
    private final ArrayList<Block> nonActiveBlockList = new ArrayList<>(); // List to hold all non-active blocks
    private final Score gameScore = new Score();
    private GraphicsContext gc;
    private KeyCode keycode = KeyCode.K;
    private ArrayList<Block> futureBlockList = new ArrayList<>(); // Holds the future block that will display on the score panel before blocks are moved to the activeBlockList
    private String name;

    static String askForName() {
        TextInputDialog tD = new TextInputDialog("name");
        tD.setTitle("Tetris");
        tD.setHeaderText("Enter your name.");
        tD.setContentText("Name:");
        tD.showAndWait();
        return tD.getResult();
    }

    static boolean checkForGameOver(ArrayList<Block> list) {
        return list.get(list.size() - 1).getY() == 0 || list.get(list.size() - 2).getY() == 0 || list.get(list.size() - 3).getY() == 0 || list.get(list.size() - 4).getY() == 0;
    }

    static void playAgain(GraphicsContext gc, Score score) {
        gc.clearRect(0, 0, 450, 500);
        score.clearScore();
        timeline.play();
    }

    static void gameOverDisplay(GraphicsContext gc, Score gameScore) {
        gc.clearRect(0, 0, 450, 500);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, 450, 500);
        gc.setFill(Color.GREEN);
        gc.setFont(Font.font(35));
        gc.fillText("Game Over!\nScore: " + gameScore.getScore(), 120, 100);
        gc.setFont(Font.font(20));
        gc.fillText("press ( y ) to play again\npress  ( e ) to exit game", 120, 170);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        name = askForName();
        Canvas canvas = new Canvas(GAME_WIDTH + 200, GAME_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(new Scene(new StackPane(canvas)));
        primaryStage.show();
        canvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> keycode = KeyCode.UP;
                case LEFT -> keycode = KeyCode.LEFT;
                case RIGHT -> keycode = KeyCode.RIGHT;
                case DOWN -> keycode = KeyCode.DOWN;
                case Y -> playAgain(gc, gameScore);
                case E -> Platform.exit();
            }
        });
        setUp();
        timeline = new Timeline(new KeyFrame(Duration.millis(250), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void setUp() {
        futureBlockList = blockFactory.getNewPiece();
    }

    public void run(GraphicsContext gc) {
        // Draw squares to show grid
        for (int i = 0; i < GAME_HEIGHT / SIZE; i++) {
            for (int j = 0; j < GAME_WIDTH / SIZE; j++) {
                if ((i % 2) == (j % 2)) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(SIZE * j, SIZE * i, SIZE, SIZE);
                } else {
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(SIZE * j, SIZE * i, SIZE, SIZE);
                }
            }
        }
        // Score Display
        gc.clearRect(300, 50, 100, 50);
        gc.setFont(Font.font(25));
        gc.setFill(Color.GREEN);
        gc.fillText("Score\n  " + gameScore.getScore(), 300, 50);

        if (activeBlockList.size() < 1) {
            activeBlockList.addAll(futureBlockList);
            futureBlockList.clear();
        }
        if (futureBlockList.size() < 1) {
            futureBlockList.addAll(blockFactory.getNewPiece());
            gc.clearRect(250, 110, 200, 100);
            futureBlockList.forEach(e -> {
                gc.setFill(e.getColor());
                gc.fillRect(e.getX() * SIZE + 195, e.getY() * SIZE + 120, SIZE - 1, SIZE - 1);
            });
        }

        // Rotate active block shape
        controller.rotateBlocks(activeBlockList, nonActiveBlockList, keycode);

        // Draw active blocks;
        activeBlockList.forEach(e -> {
            gc.setFill(e.getColor());
            gc.fillRect(e.getX() * SIZE, e.getY() * SIZE, SIZE - 1, SIZE - 1);
        });
        // Check for bottom or hitting another block
        activeBlockList.forEach(e -> {
            if (e.getY() == 19 || controller.checkForBlockBelow(nonActiveBlockList, e)) {
                activeBlockList.forEach(i -> i.setActive(false));
            }
        });

        activeBlockList.forEach(j -> {
            if (!j.isActive()) nonActiveBlockList.add(j);
        });


        // Move blocks down
        activeBlockList.forEach(e -> {
            if (e.isActive()) e.setY(e.getY() + 1);
        });

        // Remove blocks from active block list
        activeBlockList.removeIf(e -> !e.isActive());

        // draw nonActiveBlockList
        nonActiveBlockList.forEach(e -> {
            gc.setFill(e.getColor());
            gc.fillRect(e.getX() * SIZE, e.getY() * SIZE, SIZE - 1, SIZE - 1);
        });

        // Check non-active list row by row and determine if the row is full
        // for blocks Y19 look at X0 to X10. Move bottom to top.
        // todo: check for multiple line clears
        for (int i = 19; i > 0; i--) {
            int temp = i;
            List<Block> fullRows = nonActiveBlockList.stream()
                    .filter(f -> f.getY() == temp)
                    .collect(Collectors.toList());
            if (fullRows.size() == 10) {
                fullRows.forEach(e -> {
                    nonActiveBlockList.remove(e);
                    // todo: check for multiple line clears
                    gameScore.lineClear(1);
                    nonActiveBlockList.forEach(b -> {
                        if (b.getY() < temp) {
                            b.setActive(true);
                        }
                    });
                });
            }
        }
        nonActiveBlockList.forEach(e -> {
            if (e.getY() != 19 && !controller.checkForBlockBelow(nonActiveBlockList, e) && e.isActive()) {
                e.setY(e.getY() + 1);
            }
        });
        // check for game over
        if (nonActiveBlockList.size() > 18) {
            if (checkForGameOver(nonActiveBlockList)) {
                gameScore.saveToFile(name);
                gameOverDisplay(gc, gameScore);
                activeBlockList.clear();
                nonActiveBlockList.clear();
                timeline.pause();
            }
        }
        keycode = KeyCode.E;
    }
}
