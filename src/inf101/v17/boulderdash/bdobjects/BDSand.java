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
	private ImagePattern image;

	public BDSand(BDMap owner) {
		super(owner);
		InputStream resourceAsStream = getClass().getResourceAsStream("../images/sand.png");
		image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1,1, true);
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
