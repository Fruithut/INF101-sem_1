package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

public class BDBox extends AbstractBDObject {
    
    /**
     * Will hold images of object
     */
    private ImagePattern box1, box2, box3;
    
    public int hits = 0;

    public BDBox(BDMap owner) {
        super(owner);
        //Find graphics
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("../images/box1.png");
            box1 = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
            InputStream resourceAsStream2 = getClass().getResourceAsStream("../images/box2.png");
            box2 = new ImagePattern(new Image(resourceAsStream2), 0, 0, 1,1, true);
            InputStream resourceAsStream3 = getClass().getResourceAsStream("../images/box3.png");
            box3 = new ImagePattern(new Image(resourceAsStream3), 0, 0, 1,1, true);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("An imagefile is missing!");
            System.exit(1);
        }
    }

    @Override
    public Paint getColor() {
        if (hits == 0) {
            return box1;
        } else if (hits == 1) {
            return box2;
        }
        return box3;
    }

    /**
     * Determines if the player has hit the box enough to "open" it
     */
    public void pop(){
        if(hits == 2) {
            owner.set(this.getX(), this.getY(), new BDKey(owner));
        } else {
            //Return a sound
            hits++;
        }
    }

    @Override
    public void step() {
        //Empty
    }
}
