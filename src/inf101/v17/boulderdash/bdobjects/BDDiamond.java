package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

/**
 * A diamond object. All its logic is implemented in the abstract superclass.
 *
 * @author larsjaffke
 *
 */
public class BDDiamond extends AbstractBDFallingObject {
    /**
     * Will hold image of object
     */
	private ImagePattern image;

	public BDDiamond(BDMap owner) {
		super(owner);
		//Find graphics
		try {
			InputStream resourceAsStream = getClass().getResourceAsStream("../images/diamond.png");
			image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("An imagefile is missing!");
			System.exit(1);
		}
	}

	@Override
	public Paint getColor() {
		return image;
	}
	

}
