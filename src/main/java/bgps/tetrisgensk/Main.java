package bgps.tetrisgensk;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main extends Application {

    private final int GAME_WIDTH = 250;
    private final int GAME_HEIGHT = 500;
    private final int SIZE = 25;
    private GraphicsContext gc;
    private KeyCode keycode = KeyCode.K;
    private final Controller controller = new Controller();
    private final BlockFactory blockFactory = new BlockFactory();

    private ArrayList<Block> activeBlockList = new ArrayList<>(); // This holds the current block moving. When block active is false remove from this list and add to another.
    private ArrayList<Block> nonActiveBlockList = new ArrayList<>(); // List to hold all non-active blocks
    private ArrayList<Block> futureBlockList = new ArrayList<>(); // Holds the future block that will display on the score panel before blocks are moved to the activeBlockList
    private final Score gameScore = new Score();
    private String name;
    private Timeline timeline;

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
            }
        });
        setUp();
        timeline = new Timeline(new KeyFrame(Duration.millis(230), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void setUp() {
        futureBlockList = blockFactory.getNewPiece();
    }

    public void run(GraphicsContext gc) {
        // Draw squares to show grid
        for(int i = 0; i < GAME_HEIGHT/SIZE; i++){
            for(int j = 0; j< GAME_WIDTH/SIZE; j++) {
                if((i % 2) == (j % 2)) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(SIZE*j, SIZE*i, SIZE, SIZE);
                } else {
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(SIZE*j, SIZE*i, SIZE, SIZE);
                }
            }
        }
        // Score Display
        gc.clearRect(300, 50, 100, 50);
        gc.setFont(Font.font(25));
        gc.setFill(Color.GREEN); // todo: color can be changed to something more fitting.
        gc.fillText("Score\n  " + gameScore.getScore(), 300, 50);

        if(activeBlockList.size() < 1) {
            activeBlockList.addAll(futureBlockList);
            futureBlockList.clear();
        }
        if(futureBlockList.size() < 1) {
            futureBlockList.addAll(blockFactory.getNewPiece());
            gc.clearRect(250, 110, 200, 100);
            futureBlockList.forEach(e -> {
                gc.setFill(e.getColor());
                gc.fillRect(e.getX()*SIZE +195, e.getY()*SIZE+120, SIZE-1,SIZE-1);
            });
        }

        // Rotate active block shape
        controller.rotateBlocks(activeBlockList, nonActiveBlockList,  keycode);

        // Draw active blocks;
        activeBlockList.forEach(e -> {
            gc.setFill(e.getColor());
            gc.fillRect(e.getX() * SIZE, e.getY() * SIZE, SIZE-1, SIZE-1);
        });
        // Check for bottom or hitting another block
        activeBlockList.forEach(e -> {
            if(e.getY() == 19 || controller.checkForBlockBelow(nonActiveBlockList, e)) {
                // todo: probably remove active field from Block. All Block(s) in the non-Active List can be moved down as needed.
                activeBlockList.forEach(i -> i.setActive(false));
            }
        });

        activeBlockList.forEach(j -> {
            if(!j.isActive()) nonActiveBlockList.add(j);
        });


        // Move blocks down
        activeBlockList.forEach(e -> {
            if(e.isActive()) e.setY(e.getY()+1);
        });

        // Remove blocks from active block list
        activeBlockList.removeIf(e -> !e.isActive());

        // draw nonActiveBlockList
        nonActiveBlockList.forEach(e -> {
            gc.setFill(e.getColor());
            gc.fillRect(e.getX() * SIZE, e.getY() * SIZE, SIZE-1, SIZE-1);
        });

        // Check non-active list row by row and determine if the row is full
        // for blocks Y19 look at X0 to X10. Move bottom to top.
        // todo: check for multiple line clears
        for(int i = 19; i > 0; i--) {
            int temp = i;
            List<Block> fullRows = nonActiveBlockList.stream()
                    .filter(f -> f.getY() == temp)
                    .collect(Collectors.toList());
            if(fullRows.size() == 10) {
                fullRows.forEach(e -> {
                    nonActiveBlockList.remove(e);
                    // todo: check for multiple line clears
                    gameScore.lineClear(1);
                    nonActiveBlockList.forEach(b -> {
                        if(b.getY() < temp) {
                            b.setActive(true);
                        }
                    });
                });
            }
        }
        nonActiveBlockList.forEach(e -> {
            if(e.getY() != 19 && !controller.checkForBlockBelow(nonActiveBlockList, e) && e.isActive()) {
                e.setY(e.getY() +1);
            }
        });
        // check for game over
        if(nonActiveBlockList.size() > 18) {
            if(checkForGameOver(nonActiveBlockList)) {
                System.out.println("Game is over"); // todo: remove after testing
                gameScore.saveToFile(name);
                timeline.stop();
            }
        }

        keycode = KeyCode.E;
    }
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
    public static void main(String[] args) {
        launch(args);
    }
}
