package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;


public class BDRock extends AbstractBDFallingObject {
    /**
     * Will hold image of object
     */
    private ImagePattern image;

    /**
     * Standard constructor for BDRock
     * @param owner
     */
    public BDRock(BDMap owner) {
        super(owner);
        //Find graphics
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("../images/rock.png");
            image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("An imagefile is missing!");
            System.exit(1);
        }
    }


    /**
     * Returns a boolean value based upon the evaluation if the stone can be pushed either east or west.
     * @param dir the direction of the stone if pushed
     * @return
     */
    public boolean push(Direction dir){
        Position rockPos = this.getPosition();
        Position nextPos = rockPos.moveDirection(dir);
        if (owner.canGo(nextPos) && owner.get(nextPos) instanceof BDEmpty && 
                (dir.equals(Direction.EAST) || (dir.equals(Direction.WEST)))) {
            try {
                prepareMove(nextPos);
                step();
                return true;
            } catch (IllegalMoveException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public Paint getColor() {
        return image;
    }
}
