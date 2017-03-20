package inf101.v17.boulderdash.bdobjects.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.BDBug;
import inf101.v17.boulderdash.bdobjects.IBDObject;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;

public class BugTest {

	private BDMap map;

	@Before
	public void setup() {
	}

	@Test
	public void bugMoves() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);

		for(int i = 0; i < 100; i++) {
			map.step();
			if(map.get(bugPos) != bug) { // bug has moved
				// reported position should be different
				assertNotEquals(bugPos, map.getPosition(bug));
				// bug moved –  we're done
				return;
			}

		}

		fail("Bug should have moved in 100 steps!");
	}

	/**
	 * Tests to see that the bug does not move through walls
	 */
	@Test
	public void bugMovesWall() {
		IGrid<Character> grid = new MyGrid<>(4, 4, '*');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);

		for(int i = 0; i < 100; i++) {
			map.step();
			assertEquals(bugPos, map.getPosition(bug));
		}
	}

	/**
	 * Tests to see that the bug does not move through sand
	 */
	@Test
	public void bugMovesSand() {
		IGrid<Character> grid = new MyGrid<>(4, 4, '#');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);

		for(int i = 0; i < 100; i++) {
			map.step();
			assertEquals(bugPos, map.getPosition(bug));
		}
	}

	/**
	 * Tests if the bug moves according to implementation;
	 * WEST -> NORTH -> EAST -> SOUTH
	 */
	@Test
	public void bugMovesDirection() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);

		//Move west
		for (int i = 0; i <= 10; i++) {map.step();}
		assertEquals(1,map.getPosition(bug).getX());
		assertEquals(2,map.getPosition(bug).getY());

		//Move north
		for (int i = 0; i <= 10; i++) {map.step();}
		assertEquals(1,map.getPosition(bug).getX());
		assertEquals(3,map.getPosition(bug).getY());

		//Move east
		for (int i = 0; i <= 10; i++) {map.step();}
		assertEquals(2,map.getPosition(bug).getX());
		assertEquals(3,map.getPosition(bug).getY());

		//Move south
		for (int i = 0; i <= 10; i++) {map.step();}
		assertEquals(2,map.getPosition(bug).getX());
		assertEquals(2,map.getPosition(bug).getY());


	}

	/**
	 * Tests if the player is killed by the bug after 100 steps
	 */
	@Test
	public void bugKills() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'b');
		grid.set(1, 2, 'p');
		map = new BDMap(grid);

		for(int i = 0; i < 100; i++) {map.step();}
		assertEquals(false, map.getPlayer().isAlive());
	}
}
