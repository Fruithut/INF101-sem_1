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
    private ImagePattern image;

    /**
     * Standard constructor for BDRock
     * @param owner
     */
    public BDRock(BDMap owner) {
        super(owner);
        InputStream resourceAsStream = getClass().getResourceAsStream("../images/rock.png");
        image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
    }


    /**
     * Returns a boolean value based upon the evaluation if the stone can be pushed either east or west.
     * @param dir the direction of the stone if pushed
     * @return
     */
    public boolean push(Direction dir) {
        Position rockPos = this.getPosition();
        switch (dir) {
            case EAST: if (owner.canGo(rockPos, dir)) {
                if (owner.get(rockPos.getX()+1,rockPos.getY()) instanceof BDEmpty) {
                    try {
                        prepareMove(rockPos.getX()+1,rockPos.getY());
                        step();
                        return true;
                    } catch (IllegalMoveException e) {
                        return false;
                    }
                }
            }
            case WEST: if (owner.canGo(rockPos, dir)) {
                if (owner.get(rockPos.getX()-1,rockPos.getY()) instanceof BDEmpty) {
                    try {
                        prepareMove(rockPos.getX()-1,rockPos.getY());
                        step();
                        return true;
                    } catch (IllegalMoveException e) {
                        return false;
                    }
                }
            }
            default: return false;
        }
    }

    @Override
    public Paint getColor() {
        return image;
    }
}
