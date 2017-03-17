package inf101.v17.boulderdash.bdobjects;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;

/**
 * An implementation of the player.
 *
 * @author larsjaffke
 *
 */
public class BDPlayer extends AbstractBDMovingObject implements IBDKillable {

	/**
	 * Is the player still alive?
	 */
	protected boolean alive = true;

	/**
	 * The direction indicated by keypresses.
	 */
	protected Direction askedToGo;

	/**
	 * Number of diamonds collected so far.
	 */
	protected int diamondCnt = 0;

	public BDPlayer(BDMap owner) {
		super(owner);
	}

	@Override
	public Color getColor() {
		return Color.BLUE;
	}

	/**
	 * @return true if the player is alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Determines direction of player-object based on input
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
	 * DOCUMENT
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
				askedToGo = null;
				super.step();
			} catch (IllegalMoveException e) {
				//DO NOTHING
			}
		}

		askedToGo = null;
		super.step();
	}

	@Override
	public boolean isKillable() {
		return true;
	}
}
