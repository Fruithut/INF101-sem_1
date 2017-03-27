package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of the player.
 *
 * @author larsjaffke
 *
 */
public class BDPlayer extends AbstractBDMovingObject implements IBDKillable {

	/**
	 * Will hold images of object
	 */
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
	
	/**
	 * Number of keys collected so far
	 */
	protected int keyCnt = 0;
	
	public BDPlayer(BDMap owner) {
		super(owner);
		//Find graphics
		try {
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
        } catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("An imagefile is missing!");
			System.exit(1);
        }
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
	    if (owner.isSoundOn()) {
	        BDSounds.getSound(1).play();
        }
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
	 * Returns the number of keys collected so far.
	 *
	 * @return
	 */
	public int numberOfKeys() {
		return keyCnt;
	}

	/**
	 * Set the amount of keys the player has
	 * @param keyCnt
	 */
	public void setKeys(int keyCnt) {
		this.keyCnt = keyCnt;
	}

	/**
	 * Determines if the player can move in a certain direction based upon user-input.
	 * NB: Seems awfully cluttered because of all the 'ifs' to determine if the sfx class is in use or not
	 */
	@Override
	public void step() {
		//Find player-position
		Position playerPos = owner.getPlayer().getPosition();
		Boolean legalMove = false;
		Position playerNext = null;

		if (askedToGo != null && owner.canGo(playerPos, askedToGo)) {
			playerNext = playerPos.moveDirection(askedToGo);

			//Store direction in askedToGoLast (animation-behaviour)
			askedToGoLast = askedToGo;

			if (owner.get(playerNext) instanceof BDDiamond) {
				diamondCnt++;
				if (owner.isSoundOn()) BDSounds.getSound(0).play();
				legalMove = true;
			} else if (owner.get(playerNext) instanceof BDRock) {
				if (((BDRock) owner.get(playerNext)).push(askedToGo)) {
					legalMove = true;
					if (owner.isSoundOn()) BDSounds.getSound(2).play();
				} else {
					if (owner.isSoundOn()) BDSounds.getSound(4).play();
				}
			} else if (owner.get(playerNext) instanceof BDBug) {
				kill();
			} else if (owner.get(playerNext) instanceof BDBox) { 
				((BDBox) owner.get(playerNext)).pop();
				if (owner.isSoundOn()) BDSounds.getSound(2).play();
			} else if (owner.get(playerNext) instanceof BDKey) {
				keyCnt++;
				if (owner.isSoundOn()) BDSounds.getSound(7).play();
				legalMove = true;
			} else if (owner.get(playerNext) instanceof BDGate) {
				if (((BDGate) owner.get(playerNext)).unlock(this)) {
					legalMove = true;
					if (owner.isSoundOn()) BDSounds.getSound(9).play();
				} else {
					if (owner.isSoundOn()) BDSounds.getSound(8).play();
				}
			} else {
				legalMove = true;
			}
		}

		/*If movement in the given direction is allowed proceed to prepare
		  player-movement and call step.*/
		if (legalMove) {
			try {
				prepareMove(playerNext);
				super.step();
				if (owner.isSoundOn()) BDSounds.getSound(6).play();
				askedToGoLast = askedToGo;
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}
		askedToGo = null;
		
		/*If player collects all diamonds placed on map -> quit
		Last minute implementation (may not have been tested in every aspect)*/
		if (diamondCnt == owner.getDiamondCnt()) {
			if(owner.isSoundOn()) BDSounds.getSound(10).play();
			//Wait for sound to finish playing
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	@Override
	public boolean isKillable() {
		return true;
	}
	
}
