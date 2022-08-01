package bgps.tetrisgensk;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    // Todo: rotateBlocks needs to determine shape of block and rotate and move blocks as needed.
    // Todo: Constructor needs another parameter to determine block shape.

    public void rotateBlocks(ArrayList<Block> list, ArrayList<Block>  nonActiveList, KeyCode key) {

        switch (key) {
            case UP:
                if(list.get(0).getX() == list.get(3).getX()) {
                    list.get(1).setX(list.get(0).getX()+1);
                    list.get(1).setY(list.get(0).getY());
                    list.get(2).setX(list.get(0).getX()+2);
                    list.get(2).setY(list.get(0).getY());
                    list.get(3).setX(list.get(0).getX()+3);
                    list.get(3).setY(list.get(0).getY());
                }
                else {
                    list.get(1).setY(list.get(0).getY()-1);
                    list.get(1).setX(list.get(0).getX());

                    list.get(2).setY(list.get(0).getY()-2);
                    list.get(2).setX(list.get(0).getX());

                    list.get(3).setY(list.get(0).getY()-3);
                    list.get(3).setX(list.get(0).getX());
                }
                break;
            case LEFT:
                try {
                    list.forEach(e -> {
                        nonActiveList.forEach(i -> {
                            if(e.getX()-1 == i.getX() && e.getY() == i.getY()) {
                                throw new RuntimeException();
                            }
                        });

                    });
                    list.forEach(x -> x.setX(x.getX() - 1));
                    break;
                } catch (RuntimeException e) {
                    break;
                }


            case RIGHT:
                try {

                    list.forEach(e -> {
                        nonActiveList.forEach(i -> {
                            if(e.getX()+1 == i.getX() && e.getY() == i.getY()) {
                                throw new RuntimeException();
                            }
                        });
                    });
                    list.forEach(x -> x.setX(x.getX() + 1));
                    break;
                } catch (RuntimeException e) {
                    break;
                }
        }

    }

    public boolean checkForBlockBelow(ArrayList<Block> list, Block b) {
        List temp = list.stream()
                .filter(f -> f.getY() - b.getY() == 1 && f.getX() == b.getX())
                .collect(Collectors.toList());
        return temp.size() > 0;
    }
}
