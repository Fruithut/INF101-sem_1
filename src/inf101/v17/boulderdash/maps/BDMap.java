package inf101.v17.boulderdash.maps;

import java.util.*;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.*;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;

/**
 * An implementation of a map
 * 
 * @author larsjaffke
 *
 */
public class BDMap {

	/**
	 * Stores the data of the map
	 */
	protected IGrid<IBDObject> grid;
	
	/**
	 * A separate reference to the player, since it is accessed quite
	 * frequently.
	 */
	protected BDPlayer player;

	/**
	 * Hashmap to connect an IBDObject with a particular Position-object
	 */
	protected HashMap<IBDObject, Position> hashPosition = new HashMap<>();

	/**
	 * Determines if soundeffects are to played or not
	 */
	private static boolean soundOn = false;

	/**
	 * How many diamonds currently on the map
	 */
	private static int diamondCnt = 0;

	

	/**
	 * Main constructor of this class.
	 * 
	 * @param map
	 *            A grid of characters, where each character represents a type
	 *            of {@link #IBDObject}: ' ' - {@link #BDEmpty}, '*' -
	 *            {@link BDWall}, '#' - {@link #BDSand}, 'd' -
	 *            {@link #BDDiamond}, 'b' - {@link BDBug}, 'r' -
	 *            {@link #BDRock}, 'p' - {@link #BDPlayer}
	 * 
	 * @param player
	 *            The player object has to be initialized separately.
	 */
	public BDMap(IGrid<Character> map) {
		grid = new MyGrid<IBDObject>(map.getWidth(), map.getHeight(), null);
		this.player = new BDPlayer(this);
		fillGrid(map);
	}

	/**
	 * True of the object can move in direction dir, false otherwise.
	 * 
	 * @param obj
	 * @param dir
	 * @return
	 */
	public boolean canGo(IBDObject obj, Direction dir) {
		return canGo(this.getPosition(obj), dir);
	}

	/**
	 * True if an object can move to positio (x, y), false otherwise.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canGo(int x, int y) {
		if (!isValidPosition(x, y)) {
			return false;
		}
		if (grid.get(x, y) instanceof BDWall) {
			return false;
		}
		return true;
	}

	/**
	 * See {@link #canGo(int, int)}
	 * 
	 * @param pos
	 * @return
	 */
	public boolean canGo(Position pos) {
		return canGo(pos.getX(), pos.getY());
	}

	/**
	 * Cf. {@link #canGo(IBDObject, Direction)}
	 * 
	 * @param pos
	 * @param dir
	 * @return
	 */
	public boolean canGo(Position pos, Direction dir) {
		switch (dir) {
		case NORTH:
			return canGo(pos.getX(), pos.getY() + 1);
		case SOUTH:
			return canGo(pos.getX(), pos.getY() - 1);
		case EAST:
			return canGo(pos.getX() + 1, pos.getY());
		case WEST:
			return canGo(pos.getX() - 1, pos.getY());
		}
		throw new IllegalArgumentException();
	}

	/**
	 * This method is used to initialize the map with a given map of charaters,
	 * see also {@link #BDMap(IGrid, BDPlayer)}.
	 * 
	 * @param inputmap
	 */
	private void fillGrid(IGrid<Character> inputmap) {
		int width = inputmap.getWidth();
		int height = inputmap.getHeight();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				IBDObject obj = makeObject(inputmap.get(x, y), x, y);
				grid.set(x, y, obj);
				hashPosition.put(obj, new Position(x,y));
			}
		}
	}

	/**
	 * Initialize an object according to the given character, see also
	 * {@link #BDMap(IGrid, BDPlayer)}.
	 * 
	 * @param c
	 * @param x
	 * @param y
	 * @return
	 */
	private IBDObject makeObject(Character c, int x, int y) {
		if (c == 'p') {
			return this.player;
		} else if (c == '*') {
			return new BDWall(this);
		} else if (c == 'b') {
			try {
				return new BDBug(this, new Position(x, y), 1, 10);
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		} else if (c == '#') {
			return new BDSand(this);
		} else if (c == ' ') {
			return new BDEmpty(this);
		} else if (c == 'd') {
			diamondCnt++;
			return new BDDiamond(this);
		} else if (c == 'r') {
			return new BDRock(this);
		} else if (c == 'x') {
			return new BDBox(this);
		} else if (c == 'k') {
			return new BDKey(this);
		} else if (c == 'g') {
			return new BDGate(this);
		} 

		System.err.println("Illegal character in map definition at (" + x + ", " + y + "): '" + c + "'");
		return new BDEmpty(this);
		// alternatively, throw an exception
		// throw new IllegalArgumentException();
	}

	/**
	 * get the object in (x, y)
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public IBDObject get(int x, int y) {
		return grid.get(x, y);
	}

	/**
	 * see {@link #get(int, int)}
	 * 
	 * @param pos
	 * @return
	 */
	public IBDObject get(Position pos) {
		return grid.get(pos.getX(), pos.getY());
	}

	/**
	 * Returns the height of this map.
	 * 
	 * @return
	 */
	public int getHeight() {
		return grid.getHeight();
	}

	/**
	 * Given a position pos, this method computes the nearest n empty fields.
	 * This method is used when a bug dies and had to spread a certain number of
	 * diamonds in neighboring fields.
	 * 
	 * @param pos
	 * @param n
	 * @return
	 */
	public List<Position> getNearestEmpty(Position pos, int n) {
		List<Position> empty = new ArrayList<>();
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (get(x, y) instanceof BDEmpty) {
					empty.add(new Position(x, y));
				}
			}
		}
		if (empty.size() < n) {
			throw new IllegalArgumentException("There are less than " + n + " empty fields on this map");
		}
		Collections.sort(empty, new Comparator<Position>() {

			@Override
			public int compare(Position o1, Position o2) {
				return pos.distanceTo(o1) - pos.distanceTo(o2);
			}
		});
		List<Position> nearby = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			nearby.add(empty.get(i));
		}
		return nearby;
	}

	/**
	 * Get the player in this map.
	 * 
	 * @return
	 */
	public BDPlayer getPlayer() {
		return player;
	}

	/**
	 * Get the position of an object in this map
	 * 
	 * @param object
	 * @return
	 */
	public Position getPosition(IBDObject object) {
		return hashPosition.get(object);
	}

	/**
	 * get the width of this map.
	 * 
	 * @return
	 */
	public int getWidth() {
		return grid.getWidth();
	}

	/**
	 * Check whether (x, y) is a valid position in this map.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isValidPosition(int x, int y) {
		return x > -1 && x < grid.getWidth() && y > -1 && y < grid.getHeight();
	}

	/**
	 * set tile (x, y) to element.
	 * 
	 * @param x
	 * @param y
	 * @param element
	 */
	public void set(int x, int y, IBDObject element) {
		if (!isValidPosition(x, y)) {
			throw new IndexOutOfBoundsException();
		}
		grid.set(x, y, element);
		hashPosition.put(element, new Position(x,y));
	}

	/**
	 * Runs through all of the elements contained in the map and calls their step()-method
	 */
	public void step() {
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				grid.get(i,j).step();
			}
		}

		//Alternative hash-iteration
		/*try {
			for (IBDObject x : hashPosition.keySet()) {
				x.step();
			}
		} catch (ConcurrentModificationException e) {
			//DO NOTHING
		}*/
	}

	/**
	 * Sets sound(sfx) to on or off.
	 * @param soundOn
	 */
	public static void setSound(boolean soundOn) {
		BDMap.soundOn = soundOn;
	}

	/**
	 * @returns a boolean based upon if soundeffects has been turned on or off
	 */
	public static boolean isSoundOn() {
		return soundOn;
	}

	/**
	 * Amount of diamonds on map
	 * @return
	 */
	public static int getDiamondCnt() {
		return diamondCnt;
	}

	/**
	 * Increase diamonds-counter by one
	 */
	public void increaseDiamondCnt() {
		diamondCnt++;
	}
}
