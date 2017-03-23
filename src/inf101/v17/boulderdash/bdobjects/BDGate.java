package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

public class BDGate extends AbstractBDObject {
    
    /**
     * Will hold image of object
     */
    private ImagePattern image;
    
    public BDGate(BDMap owner) {
        super(owner);
        //Find graphics
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("../images/gate.png");
            image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("An imagefile is missing!");
        }
    }

    @Override
    public Paint getColor() {
        return image;
    }

    /**
     * Unlocks the gate if the player has a key
     * @param player
     * @return
     */
    public boolean unlock(BDPlayer player) {
        if (player.keyCnt > 0) {
            owner.set(this.getX(), this.getY(), new BDEmpty(owner));
            return true;
        }
        return false;
    }

    @Override
    public void step() {

    }
}
