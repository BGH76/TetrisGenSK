package bgps.tetrisgensk;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    // Todo: rotateBlocks needs to determine shape of block and rotate and move blocks as needed.
    // Todo: prevent blocks from going off the screen. Width is now 250 and height is 500.

    public static int XMAX = 250;
    public static int YMAX = 500;
    private int form; //Stores form of current piece. If form is 1 then shape is in the upright position. (1 = downward, 2 = upward)Horizontal, (3 = right, 4 = left)Vertical


    public void rotateBlocks(ArrayList<Block> list, ArrayList<Block>  nonActiveList, KeyCode key) {

        form = getBlockOrientation(list);

        //rotate accordingly
        switch (key) {

            case UP:
                //T shape
                if(list.get(0).getBlockType() == 1)
                    list = getRotatedTBlock(list,form);

                //Square shape No Rotation Needed

                // Stick shape
                else if(list.get(0).getBlockType() == 3) {
                    getRotatedStickBlock(list,nonActiveList,form);
                }

                //L shape
                else if(list.get(0).getBlockType() == 4)
                    getRotatedLBlock(list,nonActiveList,form);

                // Z shape
                else if(list.get(0).getBlockType() == 5)
                    list = getRotatedDogBlock(list,form);

                break;

        // move right or left
            case LEFT:
                try {
                    list.forEach(e -> nonActiveList.forEach(i -> {
                        if(e.getX()-1 == i.getX() && e.getY() == i.getY()) {
                            throw new RuntimeException();
                        }
                    }));
                    if(list.get(0).getX() > 0)// The condition to stop the pieces from going outside. When rotating the pieces the 1st should be at the extreme left or put the key that is extreme left
                        list.forEach(x -> x.setX(x.getX() - 1));
                    break;
                } catch (RuntimeException e) {
                    break;
                }


            case RIGHT:
                try {

                    list.forEach(e -> nonActiveList.forEach(i -> {
                        if(e.getX()+1 == i.getX() && e.getY() == i.getY()) {
                            throw new RuntimeException();
                        }
                    }));
                    if(list.get(3).getX() < 9)// The condition to stop the pieces from going outside. When rotating the pieces the 3rd should be at the extreme right or put the key that is extreme right
                        list.forEach(x -> x.setX(x.getX() + 1));
                    break;
                } catch (RuntimeException e) {
                    break;
                }
        }

    }

    public boolean checkForBlockBelow(ArrayList<Block> list, Block b) {
        List<Block> temp = list.stream()
                .filter(f -> f.getY() - b.getY() == 1 && f.getX() == b.getX())
                .collect(Collectors.toList());
        return temp.size() > 0;
    }

    //Check Block orientation() The return value is equal to the form attribute
    public int getBlockOrientation(ArrayList<Block> list){
        int orientation  = 0;
        int shape = list.get(0).getBlockType();
        switch (shape){
            case 1://T
                if(list.get(0).getY() == list.get(1).getY() & list.get(0).getY() == list.get(3).getY())//all Ys are the same
                {
                    if (list.get(0).getY() - list.get(2).getY() < 0)//Is the block pointing down T
                        orientation = 1;
                    else
                        orientation = 2;
                }
                else if(list.get(0).getX() == list.get(1).getX() & list.get(0).getX() == list.get(3).getX())//all Xs are the same
                {
                    if (list.get(0).getX() - list.get(2).getX() < 0)//Is the block pointing down T
                        orientation = 3;
                    else
                        orientation = 4;
                }
                break;
            case 2://Square
                break;
            case 3://Stick
                if(list.get(0).getY() == list.get(1).getY() & list.get(0).getY() == list.get(3).getY())//Horizontal
                    orientation = 3;
                else//vertical
                    orientation = 1;
                break;
            case 4://L
                if(list.get(0).getX() == list.get(1).getX() & list.get(0).getX() == list.get(2).getX())//All Xs are same
                {
                    if (list.get(0).getX() - list.get(3).getX() < 0) //Down -> Left
                        orientation = 1;
                    else//Is the block pointing up L -> right
                        orientation = 2;
                }
                else{//All Ys are same
                    if (list.get(0).getY() - list.get(3).getY() < 0)//Is the block pointing right -> down
                        orientation = 4;
                    else//pointing left -> up
                        orientation = 3;
                }
                break;
            case 5://Dog
                if(list.get(0).getX() - list.get(3).getX() == 2)//Horoixontal Z
                {
                    if(list.get(0).getY() > list.get(3).getY())
                        orientation = 4; //left
                    else
                        orientation = 3;//right

                }
                else //Vertical
                {
                    if(list.get(0).getX() > list.get(3).getX())
                        orientation = 1; //Up
                    else
                        orientation = 2;//Down

                }
                break;
        }
        return orientation;
    }

    //Rotate L Block
    public void getRotatedLBlock(ArrayList<Block> list, ArrayList<Block>  nonActiveList, int orientation){

        if(orientation == 1){//Down -> Left
            if(checkForBlocksAndBorder(nonActiveList,list.get(1).getX(),list.get(1).getY(),list.get(0).getX() - 1,list.get(0).getY(),list.get(0).getX() - 2,list.get(0).getY()))
            {
                list.get(3).setX(list.get(1).getX());
                list.get(3).setY(list.get(1).getY());
                list.get(1).setX(list.get(0).getX() - 1);
                list.get(1).setY(list.get(0).getY());
                list.get(2).setX(list.get(0).getX() - 2);
                list.get(2).setY(list.get(0).getY());
            }
        }
        else if(orientation == 2){//up -> right
            if(checkForBlocksAndBorder(nonActiveList,list.get(1).getX(),list.get(1).getY(),list.get(0).getX() + 1,list.get(0).getY(),list.get(0).getX() + 2,list.get(0).getY()))
            {
                list.get(3).setX(list.get(1).getX());
                list.get(3).setY(list.get(1).getY());
                list.get(1).setX(list.get(0).getX() + 1);
                list.get(1).setY(list.get(0).getY());
                list.get(2).setX(list.get(0).getX() + 2);
                list.get(2).setY(list.get(0).getY());
            }
        }
        else if(orientation == 3){//right -> down
            if(checkForBlocksAndBorder(nonActiveList,list.get(1).getX(),list.get(1).getY(),list.get(0).getX(),list.get(0).getY() + 1,list.get(0).getX(),list.get(0).getY() + 2))
            {
                list.get(3).setX(list.get(1).getX());
                list.get(3).setY(list.get(1).getY());
                list.get(1).setX(list.get(0).getX());
                list.get(1).setY(list.get(0).getY() + 1);
                list.get(2).setX(list.get(0).getX());
                list.get(2).setY(list.get(0).getY() + 2);
            }

        }
        else{//left -> up
            if(checkForBlocksAndBorder(nonActiveList,list.get(1).getX(),list.get(1).getY(),list.get(0).getX(),list.get(0).getY() - 1,list.get(0).getX(),list.get(0).getY() - 2))
            {
                list.get(3).setX(list.get(1).getX());
                list.get(3).setY(list.get(1).getY());
                list.get(1).setX(list.get(0).getX());
                list.get(1).setY(list.get(0).getY() - 1);
                list.get(2).setX(list.get(0).getX());
                list.get(2).setY(list.get(0).getY() - 2);
            }
        }

    }

    //Rotate the

    //Rotate Stick Block
    public void getRotatedStickBlock(ArrayList<Block> list, ArrayList<Block>  nonActiveList, int orientation){

        if(orientation == 1)//Vertical
        {//Turn horizontal
            if(checkForBlocksAndBorder(nonActiveList,list.get(0).getX()+1, list.get(0).getY(),list.get(0).getX()+2,list.get(0).getY(),list.get(0).getX()+3,list.get(0).getY()))
            {
                list.get(1).setX(list.get(0).getX() + 1); // moves second block from bottom, 1 space to the right of bottom block
                list.get(1).setY(list.get(0).getY());
                list.get(2).setX(list.get(0).getX() + 2);// moves second block from top, 2 spaces to the right of bottom block
                list.get(2).setY(list.get(0).getY());
                list.get(3).setX(list.get(0).getX() + 3); // moves top block 3 spaces to the right of bottom block
                list.get(3).setY(list.get(0).getY());
            }
            //On rotation it should not hit the sides
        }
        else {//Horizontal turn vertical
            if(checkForBlocksAndBorder(nonActiveList,list.get(3).getX(),list.get(3).getY() + 1,list.get(3).getX(),list.get(3).getY() + 2,list.get(3).getX(),list.get(3).getY() + 3))
            {
                list.get(0).setX(list.get(3).getX()); // moves second block from bottom, 1 space to the right of bottom block
                list.get(0).setY(list.get(3).getY() + 1);
                list.get(1).setX(list.get(3).getX());// moves second block from top, 2 spaces to the right of bottom block
                list.get(1).setY(list.get(3).getY() + 2);
                list.get(2).setX(list.get(3).getX()); // moves top block 3 spaces to the right of bottom block
                list.get(2).setY(list.get(3).getY() + 3);
            }
        }
    }

    //Rotate T Block
    public ArrayList<Block> getRotatedTBlock(ArrayList<Block> list, int orientation){
        return list;
    }

    //Rotate Dog Block
    public ArrayList<Block> getRotatedDogBlock(ArrayList<Block> list, int orientation){
        return list;
    }

    public boolean checkForBlocksAndBorder(ArrayList<Block> list, int  ...pos){
        {
            for(int i = 0; i < pos.length; i += 2) {
                int x = pos[i], y = pos[i+1];
                List<Block> temp = list.stream()
                        .filter(f -> f.getY() == y && f.getX() == x)
                        .collect(Collectors.toList());
                if(temp.size()!=0)
                    return false;
                if(x < 0 | x > 8 | y < 0 |y > 19)
                    return false;
            }
            return true;
        }

    }
    //Todo: check if move is valid given the constraints
    public boolean checkMove(ArrayList<Block>list) {

        return true;
    }
}
