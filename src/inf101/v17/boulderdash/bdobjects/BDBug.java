package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

/**
 * An implementation of a bug.
 *
 * @author larsjaffke
 *
 */
public class BDBug extends AbstractBDKillingObject implements IBDKillable {

	/**
	 * Will hold images of object
	 */
	private ImagePattern leftup, leftdown, rightup, rightdown;

	/**
	 * The amount of diamonds a bug turns into after it got killed.
	 */
	protected static final int DEATH_DIAMONDS = 8;

	/**
	 * The minimum amount of steps any bug has to pause between two moves.
	 */
	protected static final int MIN_PAUSE = 4;

	/**
	 * The position where the bug spawns when the program is loaded.
	 */
	protected Position initialPos;

	// Counts the number of skipped steps.
	protected int movedSince = 0;

	protected int totalStep = 0;

	/**
	 * This field contains the sequence of moves the bug performs repeatedly.
	 * Can be anything, but a logical set up happens in the
	 * initTrajectory()-method.
	 */
	protected List<Position> path;

	/**
	 * Every bug can move at a different speed, this field determines how many
	 * steps it has to wait in between two moves. If nothing is specified, the
	 * value is set to the constant MIN_PAUSE.
	 */
	protected int pause = MIN_PAUSE;

	/**
	 * Determines how far the bug moves when path is set up using the
	 * initTrajectory()-method.
	 */
	protected int radius = 1;

	/**
	 * The standard constructor, where pause is set to MIN_PAUSE and radius to
	 * 1.
	 *
	 * @param owner
	 * @param initialPos
	 * @throws IllegalMoveException
	 */
	public BDBug(BDMap owner, Position initialPos) throws IllegalMoveException {
		super(owner);
		this.initialPos = initialPos;
		initTrajectory();
		
		//Find graphics
		try {
			InputStream resourceAsStream1 = getClass().getResourceAsStream("../images/bug/leftup.png");
			leftup = new ImagePattern(new Image(resourceAsStream1), 0, 0, 1,1, true);
			InputStream resourceAsStream2 = getClass().getResourceAsStream("../images/bug/leftdown.png");
			leftdown = new ImagePattern(new Image(resourceAsStream2), 0, 0, 1,1, true);
			InputStream resourceAsStream3 = getClass().getResourceAsStream("../images/bug/rightup.png");
			rightup = new ImagePattern(new Image(resourceAsStream3), 0, 0, 1,1, true);
			InputStream resourceAsStream4 = getClass().getResourceAsStream("../images/bug/rightdown.png");
			rightdown = new ImagePattern(new Image(resourceAsStream4), 0, 0, 1,1, true);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("An imagefile is missing!");
			System.exit(1);
		}
	}

	/**
	 * With this constructor both radius and pause can be specified as well.
	 * Note that the value of pause always has to be at least MIN_PAUSE,
	 * otherwise the given value has no effect.
	 *
	 * @param owner
	 * @param initialPos
	 * @param radius
	 * @param pause
	 * @throws IllegalMoveException
	 */
	public BDBug(BDMap owner, Position initialPos, int radius, int pause) throws IllegalMoveException {
		super(owner);
		this.initialPos = initialPos;
		this.radius = radius;
		this.pause = pause < MIN_PAUSE ? MIN_PAUSE : pause;
		initTrajectory();
		
		//Find graphics
		try {
			InputStream resourceAsStream1 = getClass().getResourceAsStream("../images/bug/leftup.png");
			leftup = new ImagePattern(new Image(resourceAsStream1), 0, 0, 1,1, true);
			InputStream resourceAsStream2 = getClass().getResourceAsStream("../images/bug/leftdown.png");
			leftdown = new ImagePattern(new Image(resourceAsStream2), 0, 0, 1,1, true);
			InputStream resourceAsStream3 = getClass().getResourceAsStream("../images/bug/rightup.png");
			rightup = new ImagePattern(new Image(resourceAsStream3), 0, 0, 1,1, true);
			InputStream resourceAsStream4 = getClass().getResourceAsStream("../images/bug/rightdown.png");
			rightdown = new ImagePattern(new Image(resourceAsStream4), 0, 0, 1,1, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("An imagefile is missing!");
		}
	}

	@Override
	public Paint getColor() {
		int direction = totalStep % 40;
		if (direction < 20) {
			if (direction % 2 == 0) return leftup;
			return leftdown;
		} else if (direction >= 20) {
			if (direction % 2 == 0) return rightup;
			return rightdown;
		} else return Color.BLACK;
	}

	/**
	 * Initialize the path of this bug, which is its initial position and then
	 * radius times to the left, then up, then right, then down.
	 * 
	 * @throws IllegalMoveException
	 */
	private void initTrajectory() throws IllegalMoveException {
		path = new ArrayList<>(4 * radius);
		path.add(initialPos);
		Position nextPos = initialPos;
		for (int i = 0; i < radius; i++) {
			nextPos = nextPos.moveDirection(Direction.WEST);
			path.add(nextPos);
		}
		for (int i = 0; i < radius; i++) {
			nextPos = nextPos.moveDirection(Direction.NORTH);
			path.add(nextPos);
		}
		for (int i = 0; i < radius; i++) {
			nextPos = nextPos.moveDirection(Direction.EAST);
			path.add(nextPos);
		}
		for (int i = 0; i < radius; i++) {
			nextPos = nextPos.moveDirection(Direction.SOUTH);
			path.add(nextPos);
		}
	}

	@Override
	public void kill() {
		// If a bug is killed it turns into a set of diamonds. Find the
		// DEATH_DIAMONDS nearest
		// empty positions in the map and fill them with diamonds.
		Collection<Position> toDiamonds = owner.getNearestEmpty(owner.getPosition(this), DEATH_DIAMONDS);
		for (Position p : toDiamonds) {
			owner.set(p.getX(), p.getY(), new BDDiamond(owner));
			//Increase diamonds in BDMap
			owner.increaseDiamondCnt();
		}
		if (owner.isSoundOn()) BDSounds.getSound(3).play();
	}

	/**
	 * Sets the next position and returns the current one.
	 * 
	 * @return
	 */
	private void setNextPos() {
		Position cur = owner.getPosition(this);
		int index = path.indexOf(cur);
		try {
			Position nextOne = path.get((index + 1) % path.size());
			// If there is a rock or a diamond in the next position, the bug
			// cannot move.
			IBDObject nextObject = owner.get(nextOne);
			if (nextObject instanceof BDEmpty || nextObject instanceof IBDKillable) {
				prepareMove(nextOne.getX(), nextOne.getY());
			}
		} catch (IllegalMoveException e) {
			// If the bug cannot move where it's supposed to, e.g. when a wall
			// is in its
			// path, there is something wrong with the map -> kill the program.
			System.err.println(e.getMessage());
			System.err.println("You made a mistake with one of the bugs " + " setting up your map.");
			System.exit(1);
		}
	}

	@Override
	public void step() {
		// Only execute the bug's movement after it had its' pause.
		if (movedSince == pause) {
			// Set the next position according to the path
			setNextPos();
			// Check if the bug will hit the player after the next step
			// and if so, kill the player.
			checkAndKillKillable();
			// Update the map.
			// Reset the pause counter
			movedSince = 0;
			// The bug has not moved, so increase the pause counter.
		} else {
			movedSince++;
			totalStep++;
		}
		super.step();
	}
	
	@Override
	public boolean isKillable() {
		return true;
	}
}
