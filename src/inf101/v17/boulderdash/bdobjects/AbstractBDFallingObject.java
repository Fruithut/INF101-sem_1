package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;

/**
 * Contains most of the logic associated with objects that fall such as rocks
 * and diamonds.
 *
 * @author larsjaffke
 *
 */
public abstract class AbstractBDFallingObject extends AbstractBDKillingObject {

	/**
	 * A timeout between the moment when an object can fall (e.g. the tile
	 * underneath it becomes empty) and the moment it does. This is necessary to
	 * make sure that the player doesn't get killed immediately when walking
	 * under a rock.
	 */
	protected static final int WAIT = 3;

	protected boolean falling = false;

	/**
	 * A counter to keep track when the falling should be executed next, see the
	 * WAIT constant.
	 */
	protected int fallingTimeWaited = 0;

	public AbstractBDFallingObject(BDMap owner) {
		super(owner);
	}

	/**
	 * This method implements the logic of the object falling. It checks whether
	 * it can fall, depending on the object in the tile underneath it, to the sides and diagonally below,
	 * and if so, tries to prepare the move.
	 */
	public void fall() {
		// Wait until its time to fall
		if (falling && fallingTimeWaited < WAIT) {
			fallingTimeWaited++;
			return;
		}
		// The timeout is over, try and prepare the move
		fallingTimeWaited = 0;

		Position pos = owner.getPosition(this);
		// The object cannot fall if it is on the lowest row.
		if (pos.getY() > 0) {
			try {
				// Get the object in the tile below and to the left and right and also beneath left and right.
				Position below = pos.moveDirection(Direction.SOUTH), west = pos.moveDirection(Direction.WEST),
                         east = pos.moveDirection(Direction.EAST), belowWest = west.moveDirection(Direction.SOUTH),
						 belowEast = east.moveDirection(Direction.SOUTH);
				IBDObject under = owner.get(below), left = null, right = null, underLeft = null, underRight = null;

				// Call get on object only if coordinates exists in the grid
				if (pos.getX()+1 < owner.getWidth()) {
					right = owner.get(east);
					underRight = owner.get(belowEast);
				}
                if (pos.getX() > 0) {
					left = owner.get(west);
					underLeft = owner.get(belowWest);
				}

				if (falling) {
					// Fall one step if tile below is empty or killable
					if (under instanceof BDEmpty || under instanceof IBDKillable) {
						prepareMoveTo(Direction.SOUTH);
						super.step();
					} else {
						if (owner.isSoundOn() && this instanceof BDRock) BDSounds.getSound(5).play();
						falling = false;
					}
				} else {
					// start falling if tile below is empty or a bug (allows killing of bugs with pushable/tumbling rocks)
					// *rocks and diamonds resting on top of a bug will kill it
					falling = under instanceof BDEmpty || under instanceof BDBug;
					fallingTimeWaited = 1;
				}
				/*Rocks and diamonds will tumble first to the the left if possible if not then to the right.
				* Tumbling rocks and diamonds can kill both the player and the bugs, but will not kill the player if
				* the player is located directly to the side of an object that would otherwise have tumbled*/
				if (!falling && !(under instanceof BDSand) && !(under instanceof IBDKillable)){
					if ((left instanceof BDEmpty || left instanceof BDBug) && (underLeft instanceof BDEmpty || underLeft instanceof IBDKillable)) {
						prepareMoveTo(Direction.WEST);
						falling = true;
					} else if ((right instanceof BDEmpty || right instanceof BDBug) && (underRight instanceof BDEmpty || underRight instanceof IBDKillable)) {
						prepareMoveTo(Direction.EAST);
						falling = true;
					}
				}
			} catch (IllegalMoveException e) {
				// This should never happen.
				System.out.println(e);
				System.exit(0);
			}
		}
	}

	@Override
	public void step() {
		// (Try to) fall if possible
		fall();
		super.step();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
