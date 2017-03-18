package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import java.io.InputStream;

/**
 * Implementation of a piece of a wall.
 *
 * @author larsjaffke
 *
 */
public class BDWall extends AbstractBDObject {
	/**
	 * Will hold image of object
	 */
	private ImagePattern image;

	public BDWall(BDMap owner) {
		super(owner);
		InputStream resourceAsStream = getClass().getResourceAsStream("../images/wall.png");
		image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
	}

	@Override
	public Paint getColor() {
		return image;
	}

	@Override
	public void step() {
		// DO NOTHING, IT'S A WALL
	}
}
