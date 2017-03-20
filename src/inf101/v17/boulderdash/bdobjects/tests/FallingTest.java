package inf101.v17.boulderdash.bdobjects.tests;

import static org.junit.Assert.*;

import inf101.v17.boulderdash.bdobjects.*;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;

/**
 * This class contains tests for the behaviour of diamonds and rocks.
 * Implicit some of the players behaviours are tested.
 */
public class FallingTest {

	private BDMap map;

	@Before
	public void setup() {
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'd');
		grid.set(1,4, 'r');
		grid.set(0, 0, '*');

		/*1 more wall block is added because both diamonds and stones will roll of
			a single wall block*/
		grid.set(1, 0, '*');
		map = new BDMap(grid);
	}

	@Test
	public void fallingTest1() {
		IBDObject obj = map.get(0, 4);
		assertTrue(obj instanceof BDDiamond);

		// four steps later, we've fallen down one step
		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(0, 3));

		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(0, 2));

		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(0, 1));

		// wall reached, no more falling
		for (int i = 0; i < 10; i++)
			map.step();
		assertEquals(obj, map.get(0, 1));
	}

	@Test
	public void fallingTest1Rock() {
		IBDObject obj = map.get(1, 4);
		assertTrue(obj instanceof BDRock);

		// four steps later, we've fallen down one step
		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(1, 3));

		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(1, 2));

		map.step();
		map.step();
		map.step();
		map.step();
		assertEquals(obj, map.get(1, 1));

		// wall reached, no more falling
		for (int i = 0; i < 10; i++)
			map.step();
		assertEquals(obj, map.get(1, 1));
	}
	
	@Test
	public void fallingTest2() {
		checkFall(new Position(0, 4));
	}

	@Test
	public void fallingTest2Rock() {
		checkFall(new Position(1, 4));
	}
	
	@Test
	public void fallingKills1() {
		// diamond two tiles above kills player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'd');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);
		
		checkFall(new Position(0, 4));
		checkFall(new Position(0, 3));
		checkFall(new Position(0, 2));
		assertFalse(map.getPlayer().isAlive());
	}

	@Test
	public void fallingKills1Rock() {
		// diamond two tiles above kills player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'r');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);

		checkFall(new Position(0, 4));
		checkFall(new Position(0, 3));
		checkFall(new Position(0, 2));
		assertFalse(map.getPlayer().isAlive());
	}

	@Test
	public void restingDoesntKill1() {
		// diamond on top of player doesn't kill player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 3, 'd');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);

		for (int i = 0; i < 10; i++)
			map.step();
		assertTrue(map.getPlayer().isAlive());
	}
	
	@Test
	public void restingDoesntKill1Rock() {
		// diamond on top of player doesn't kill player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 3, 'r');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);

		for (int i = 0; i < 10; i++)
			map.step();
		assertTrue(map.getPlayer().isAlive());
	}

	protected Position checkFall(Position pos) {
		IBDObject obj = map.get(pos);
		if (obj instanceof AbstractBDFallingObject) {
			Position next = pos.moveDirection(Direction.SOUTH);
			if (map.canGo(next)) {
				IBDObject target = map.get(next);
				if (target.isEmpty() || target.isKillable()) {
				} else {
					next = pos;
				}
			} else {
				next = pos;
			}

			//map.step(); System.out.println(map.getPosition(object));
			map.step();
			map.step();
			map.step();
			map.step();
			assertEquals(obj, map.get(next));
			return next;
		} else
			return pos;
	}

	/**
	 * Test to see if a rock tumbles downhill
	 */
	@Test
	public void rockTumbles() {
		IGrid<Character> grid = new MyGrid<>(4, 5, ' ');
		grid.set(0,0, 'r');
		grid.set(1,0, 'r');
		grid.set(2,0, 'r');
		grid.set(3,0, 'r');

		grid.set(1,1, 'r');
		grid.set(2,1, 'r');
		grid.set(3,1, 'r');

		grid.set(2,2, 'r');
		grid.set(3,2, 'r');

		grid.set(3,3, 'r');
		grid.set(3,4, 'r');
		map = new BDMap(grid);

		IBDObject obj = map.get(3, 4);
		assertTrue(obj instanceof BDRock);

		for (int i = 0; i < 25; i++) {
			map.step();
		}
		assertEquals(map.get(0,1), obj);
	}
	
	//Rock tests that does not include falling below
	/**
	 * Checks if the rock moves right when pushed
	 */
	@Test
	public void rockIsPushableRight() {
		IGrid<Character> grid = new MyGrid<>(3, 3, ' ');
		grid.set(1, 1, 'r');
		grid.set(0, 1, 'p');
		grid.set(0, 0, '*');
		grid.set(1, 0, '*');
		grid.set(2, 0, '*');
		map = new BDMap(grid);

		IBDObject obj1 = map.get(1, 1);
		assertTrue(obj1 instanceof BDRock);

		IBDObject obj2 = map.get(0, 1);
		assertTrue(obj2 instanceof BDPlayer);
		
		map.getPlayer().keyPressed(KeyCode.RIGHT);
		map.step();
		assertEquals(obj1, map.get(2,1));
	}

	/**
	 * Checks if the rock moves left when pushed
	 */
	@Test
	public void rockIsPushableLeft() {
		IGrid<Character> grid = new MyGrid<>(3, 3, ' ');
		grid.set(2, 1, 'p');
		grid.set(1, 1, 'r');
		grid.set(0, 0, '*');
		grid.set(1, 0, '*');
		grid.set(2, 0, '*');
		map = new BDMap(grid);

		IBDObject obj1 = map.get(2, 1);
		assertTrue(obj1 instanceof BDPlayer);

		IBDObject obj2 = map.get(1, 1);
		assertTrue(obj2 instanceof BDRock);

		map.getPlayer().keyPressed(KeyCode.LEFT);
		map.step();
		assertEquals(obj2, map.get(0,1));
	}

	/**
	 * Tests to see that 2 rocks that lies next to each other will not be pushed
	 */
	@Test
	public void rockIsNotPushable() {
		IGrid<Character> grid = new MyGrid<>(4, 3, ' ');
		grid.set(3, 1, 'p');
		grid.set(2, 1, 'r');
		grid.set(1, 1, 'r');
		grid.set(0, 0, '*');
		grid.set(1, 0, '*');
		grid.set(2, 0, '*');
		map = new BDMap(grid);
		
		IBDObject obj1 = map.get(3, 1);
		assertTrue(obj1 instanceof BDPlayer);

		IBDObject obj2 = map.get(2, 1);
		assertTrue(obj2 instanceof BDRock);
		
		IBDObject obj3 = map.get(1, 1);
		assertTrue(obj3 instanceof BDRock);
		
		for (int i = 0; i < 10; i++) {
			map.getPlayer().keyPressed(KeyCode.LEFT);
			map.step();
		}
		assertEquals(obj1,map.get(3,1));
		assertEquals(obj2,map.get(2,1));
		assertEquals(obj3,map.get(1,1));
	}
}
