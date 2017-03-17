package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

/**
 * An implementation of the player.
 *
 * @author larsjaffke
 *
 */
public class BDPlayer extends AbstractBDMovingObject implements IBDKillable {
	private ImagePattern up, down, left, right, still;

	/**
	 * Is the player still alive?
	 */
	protected boolean alive = true;

	/**
	 * The direction indicated by keypresses.
	 */
	protected Direction askedToGo;

	/**
	 * The last move the user wanted to make
	 */
	protected Direction askedToGoLast;

	/**
	 * Number of diamonds collected so far.
	 */
	protected int diamondCnt = 0;

	public BDPlayer(BDMap owner) {
		super(owner);

		InputStream resourceAsStream1 = getClass().getResourceAsStream("../images/player/up.png");
		up = new ImagePattern(new Image(resourceAsStream1), 0, 0, 1,1, true);
		InputStream resourceAsStream2 = getClass().getResourceAsStream("../images/player/down.png");
		down = new ImagePattern(new Image(resourceAsStream2), 0, 0, 1,1, true);
		InputStream resourceAsStream3 = getClass().getResourceAsStream("../images/player/left.png");
		left = new ImagePattern(new Image(resourceAsStream3), 0, 0, 1,1, true);
		InputStream resourceAsStream4 = getClass().getResourceAsStream("../images/player/right.png");
		right = new ImagePattern(new Image(resourceAsStream4), 0, 0, 1,1, true);
		InputStream resourceAsStream5 = getClass().getResourceAsStream("../images/player/still.png");
		still = new ImagePattern(new Image(resourceAsStream5), 0, 0, 1,1, true);
	}

	/**
	 * @return Gets proper image of player based on where it's headed
	 */
	@Override
	public Paint getColor() {
		if (askedToGoLast != null) {
			switch (askedToGoLast) {
				case NORTH:
					return up;
				case SOUTH:
					return down;
				case EAST:
					return right;
				case WEST:
					return left;
			}
		}
		return still;
	}

	/**
	 * @return true if the player is alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Determines direction of player-object based on keyboard-input
	 * @param key
	 */
	public void keyPressed(KeyCode key) {
		switch (key) {
			case LEFT: askedToGo = Direction.WEST;
			break;
			case RIGHT: askedToGo = Direction.EAST;
			break;
			case UP: askedToGo = Direction.NORTH;
			break;
			case DOWN: askedToGo = Direction.SOUTH;
			break;
			default: askedToGo = null;
		}
	}

	@Override
	public void kill() {
		this.alive = false;
	}

	/**
	 * Returns the number of diamonds collected so far.
	 *
	 * @return
	 */
	public int numberOfDiamonds() {
		return diamondCnt;
	}

	/**
	 * COMMENT ON THIS
	 */
	@Override
	public void step() {
		Position playerPos = owner.getPlayer().getPosition();
		Boolean legalMove = false;
		Position playerNext = null;

		if (askedToGo != null && owner.canGo(playerPos, askedToGo)) {
			playerNext = playerPos.moveDirection(askedToGo);

			if (owner.get(playerNext) instanceof BDDiamond) {
				diamondCnt++;
				legalMove = true;
			} else if (owner.get(playerNext) instanceof BDRock) {
				if (((BDRock) owner.get(playerNext)).push(askedToGo)) {
					legalMove = true;
				}
			} else if (owner.get(playerNext) instanceof BDBug) {
				kill();
			} else {
				legalMove = true;
			}
		}

		if (legalMove) {
			try {
				prepareMove(playerNext);
				super.step();
				askedToGoLast = askedToGo;
				askedToGo = null;
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isKillable() {
		return true;
	}
}
