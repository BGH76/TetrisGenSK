package bgps.tetrisgensk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockFactory {

    private Map<Integer, Integer[][]> shapeMap = new HashMap<>();// Create Hashmap of shapes.

    public BlockFactory(){
        setShapeMap(1,0,0,1,0,1,1,2,0);//T
        setShapeMap(2,0,0,1,0,1,1,0,1);//Square
        setShapeMap(3,0,0,1,0,2,0,3,0);//Stick
        setShapeMap(4,0,0,0,1,0,2,1,0);//L
        setShapeMap(5,0,0,1,0,1,1,2,1);//Dog
    }

    public  void setShapeMap(Integer key,int ...values){
        Integer[][] coordinates = new Integer[4][2];
        int cnt = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 2; j++) {
                coordinates[i][j] = values[cnt];
                cnt++;
            }
            System.out.println();
        }
        shapeMap.put(key,coordinates);
    }

    // Todo: Needs to randomly select shape and place blocks into activeBlockList.
    public ArrayList<Block> getActiveBlockList(){

        ArrayList<Block> list = new ArrayList<>();
        Integer[][] shape = shapeMap.get((int)(Math.floor(Math.random()*5))+1);
        int postion = (int)Math.floor(Math.random()*6);

        list.add(new Block(1,shape[0][0]+postion,shape[0][1],true));
        list.add(new Block(2,shape[1][0]+postion,shape[1][1],true));
        list.add(new Block(3,shape[2][0]+postion,shape[2][1],true));
        list.add(new Block(4,shape[3][0]+postion,shape[3][1],true));

        return list;
    }

    // Todo: Starting positions will depend on Block Shape.
    //determine starting position of the Block


}
