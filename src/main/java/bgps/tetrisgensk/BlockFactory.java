package bgps.tetrisgensk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockFactory {

    private final Map<Integer, Integer[][]> shapeMap = new HashMap<>();// Create Hashmap of shapes.

    {
        Integer[][] coordinates = {{0,0},{1,0},{1,1},{2,0}};//T
        shapeMap.put(1,coordinates);
        coordinates = new Integer[][]{{0,0},{1,0},{1,1},{0,1}};//Square
        shapeMap.put(2,coordinates);
        coordinates = new Integer[][]{{0,0},{1,0},{2,0},{3,0}};//Stick
        shapeMap.put(3,coordinates);
        coordinates = new Integer[][]{{0,0},{0,1},{0,2},{1,0}};//L
        shapeMap.put(4,coordinates);
        coordinates = new Integer[][]{{0,0},{1,0},{1,1},{2,1}};//Dog
        shapeMap.put(5,coordinates);
    }

    public BlockFactory(){
    }


    // Todo: Needs to randomly select shape and place blocks into activeBlockList.
    public ArrayList<Block> getNewPiece(){

        ArrayList<Block> list = new ArrayList<>();
        Integer[][] shape = shapeMap.get((int)(Math.floor(Math.random()*5))+1);
        int position = (int)Math.floor(Math.random()*8);

        list.add(new Block(1,shape[0][0]+position,shape[0][1],true));
        list.add(new Block(2,shape[1][0]+position,shape[1][1],true));
        list.add(new Block(3,shape[2][0]+position,shape[2][1],true));
        list.add(new Block(4,shape[3][0]+position,shape[3][1],true));

        return list;
    }

    // Todo: Starting positions will depend on Block Shape.
    //determine starting position of the Block


}
