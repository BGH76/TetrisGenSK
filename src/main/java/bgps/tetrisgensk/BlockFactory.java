package bgps.tetrisgensk;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockFactory {

    private final Color[] color = {Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE};
    private final Map<Integer, Integer[][]> shapeMap = new HashMap<>();// Create Hashmap of shapes.

    {
        Integer[][] coordinates = {{0, 0}, {1, 0}, {1, 1}, {2, 0}};//T
        shapeMap.put(1, coordinates);
        coordinates = new Integer[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}};//Square
        shapeMap.put(2, coordinates);
        coordinates = new Integer[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}};//Stick
        shapeMap.put(3, coordinates);
        coordinates = new Integer[][]{{0, 0}, {0, 1}, {0, 2}, {1, 0}};//L
        shapeMap.put(4, coordinates);
        coordinates = new Integer[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}};//Dog
        shapeMap.put(5, coordinates);
    }

    public BlockFactory() {
    }


    public ArrayList<Block> getNewPiece() {

        ArrayList<Block> list = new ArrayList<>();
        int temp = (int) (Math.floor(Math.random() * 5)) + 1;
        Integer[][] shape = shapeMap.get(temp);
        int position = 4;
        int colorChoice = (int) (Math.random() * 4);
        list.add(new Block(shape[0][0] + position, shape[0][1], true, color[colorChoice], temp));
        list.add(new Block(shape[1][0] + position, shape[1][1], true, color[colorChoice], temp));
        list.add(new Block(shape[2][0] + position, shape[2][1], true, color[colorChoice], temp));
        list.add(new Block(shape[3][0] + position, shape[3][1], true, color[colorChoice], temp));

        return list;
    }
}
