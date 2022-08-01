package bgps.tetrisgensk;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private final int GAME_WIDTH = 250;
    private final int GAME_HEIGHT = 500;
    private final int SIZE = 25;
    private GraphicsContext gc;
    private KeyCode keycode = KeyCode.K;
    private Controller controller = new Controller();

    private ArrayList<Block> activeBlockList = new ArrayList<>(); // This holds the current block moving. When block active is false remove from this list and add to another.
    private ArrayList<Block> nonActiveBlockList = new ArrayList<>(); // List to hold all non-active blocks

    @Override
    public void start(Stage primaryStage) throws Exception{
        Canvas canvas = new Canvas(GAME_WIDTH + 200, GAME_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(new Scene(new StackPane(canvas)));
        primaryStage.show();
        canvas.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case UP:
                    keycode = KeyCode.UP;
                    break;
                case LEFT:
                    keycode = KeyCode.LEFT;
                    break;
                case RIGHT:
                    keycode = KeyCode.RIGHT;
                    break;
                case DOWN:
                    keycode = KeyCode.DOWN;
                    break;
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

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

        // todo: Call block factory. Pass in activeBlockList. Factory should randomly select shape and add to list.


        // todo: Test code. Blocks should be created and added to the list in the BlockFactory
        if(activeBlockList.size() < 1) {
            activeBlockList.add(new Block(1,5, 0, true)); // todo: need to add shape field to pass into controller and rotate as needed.
            activeBlockList.add(new Block(2, 6, 0, true));
            activeBlockList.add(new Block(3, 7, 0, true));
            activeBlockList.add(new Block(4, 8, 0, true));
        }

        // Rotate active block shape
        controller.rotateBlocks(activeBlockList, nonActiveBlockList,  keycode);

        // Draw active blocks;
        activeBlockList.forEach(e -> {
            gc.setFill(Color.RED);
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
            gc.setFill(Color.RED); //todo: get color from block
            gc.fillRect(e.getX() * SIZE, e.getY() * SIZE, SIZE-1, SIZE-1);
        });

        // Check non-active list row by row and determine if the row is full
        // for blocks Y19 look at X0 to X10. Move bottom to top.
        for(int i = 19; i > 0; i--) {
            List fullRows = nonActiveBlockList.stream()
                    .filter(f -> f.getY() == 19)
                    .collect(Collectors.toList());
            if(fullRows.size() == 10) {
                System.out.println(fullRows);
                fullRows.forEach(e -> {
                    nonActiveBlockList.remove(e);
                    // todo: update score
                });
                nonActiveBlockList.forEach(e -> {
                    e.setY(e.getY() + 1);
                });
            }
        }


        keycode = KeyCode.E;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
