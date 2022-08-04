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

        //Todo: refactor using helper methods

        // determine form of shape l
        if(list.get(0).getX() == list.get(3).getX()){ // if top and bottom block have same x, then the l shape is in vertical form
            form = 1;
        }
        if(list.get(0).getY() == list.get(3).getY()){ // if 0 and 3 block have same Y, then the l shape is in horizontal form
            form = 2;
        }
        if(list.get(0).getX() == list.get(3).getX() && list.get(0).getY()+3 == list.get(3).getY() ){ // l shape is in downward form
            form = 3;
        }
        if (list.get(0).getY() == list.get(3).getY() && list.get(0).getX()-3 == list.get(3).getX()){ //l shape is horizontal pt.2
            form = 4;
        }

        // Todo: determine shape of piece

        //rotate accordingly
        switch (key) {

            case UP:
                //l shape
                if(form == 1) {                             // if piece is vertical
                    list.get(1).setX(list.get(0).getX()+1); // moves second block from bottom, 1 space to the right of bottom block
                    list.get(1).setY(list.get(0).getY());
                    list.get(2).setX(list.get(0).getX()+2);// moves second block from top, 2 spaces to the right of bottom block
                    list.get(2).setY(list.get(0).getY());
                    list.get(3).setX(list.get(0).getX()+3); // moves top block 3 spaces to the right of bottom block
                    list.get(3).setY(list.get(0).getY());
                }
                else if(form == 2) {                                      // if piece is horizontal
                    list.get(1).setY(list.get(0).getY()+1);
                    list.get(1).setX(list.get(0).getX());

                    list.get(2).setY(list.get(0).getY()+2);
                    list.get(2).setX(list.get(0).getX());

                    list.get(3).setY(list.get(0).getY()+3);
                    list.get(3).setX(list.get(0).getX());
                }
                else if (form == 3){                                      //if piece is downward
                    list.get(1).setX(list.get(0).getX()-1);
                    list.get(1).setY(list.get(0).getY());
                    list.get(2).setX(list.get(0).getX()-2);
                    list.get(2).setY(list.get(0).getY());
                    list.get(3).setX(list.get(0).getX()-3);
                    list.get(3).setY(list.get(0).getY());
                }
                else if(form == 4) {                                      // if piece is horizontal pt. 2
                    list.get(1).setY(list.get(0).getY()-1);
                    list.get(1).setX(list.get(0).getX());

                    list.get(2).setY(list.get(0).getY()-2);
                    list.get(2).setX(list.get(0).getX());

                    list.get(3).setY(list.get(0).getY()-3);
                    list.get(3).setX(list.get(0).getX());
                }

                //Todo:
                //T shape

                //Square shape

                //L shape

                // Z shape

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
                    if (list.get(0).getX() - list.get(3).getX() < 0)//Is the block pointing up L
                        orientation = 2;
                    else
                        orientation = 1;
                }
                else{//All Ys are same
                    if (list.get(0).getY() - list.get(3).getY() < 0)//Is the block pointing right
                        orientation = 3;
                    else//pointing left
                        orientation = 4;
                }
                break;
            case 5://Dog
                break;
        }
        return orientation;
    }

    //Rotate Block

    //Todo: check if move is valid given the constraints
    public boolean checkMove(ArrayList<Block>list) {

        return true;
    }
}
