package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

/**
 * An implementation of sand which simply disappears when the player walks over
 * it. Nothing to do here.
 *
 * @author larsjaffke
 *
 */
public class BDSand extends AbstractBDObject {
	/**
	 * Will hold image of object
	 */
	private ImagePattern image;

	public BDSand(BDMap owner) {
		super(owner);
		
		//Find graphics
		try {
			InputStream resourceAsStream = getClass().getResourceAsStream("../images/sand.png");
			image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("An imagefile is missing");
		}
	}

	@Override
	public Paint getColor() {
		return image;
	}

	@Override
	public void step() {
		// DO NOTHING
	}
}
