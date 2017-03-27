package inf101.v17.boulderdash.bdobjects.tests;

import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.*;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import javafx.scene.input.KeyCode;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests different aspects of BDPlayers behaviour 
 * 
 * Notes: *Some tests regarding the player are contained within
 * 'FallingTest'-class.
 * *Many of the "playertests" accounts for the AIBDPlayers behaviour since
 * that class only generates random directions
 */
public class PlayerTest {
    
    private BDMap map;

    /**
     * Tests player movement for all key-directions
     */
    @Test
    public void playerMoves() {
        IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
        grid.set(2, 2, 'p');
        map = new BDMap(grid);

        // find the player
        Position playerPos = new Position(2,2);
        IBDObject player = map.get(playerPos);
        assertTrue(player instanceof BDPlayer);
        
        map.getPlayer().keyPressed(KeyCode.RIGHT);
        map.step();
        assertEquals(player.getPosition(), new Position(3,2));
        
        map.getPlayer().keyPressed(KeyCode.UP);
        map.step();
        assertEquals(player.getPosition(), new Position(3,3));
        
        map.getPlayer().keyPressed(KeyCode.LEFT);
        map.step();
        assertEquals(player.getPosition(), new Position(2,3));
        
        map.getPlayer().keyPressed(KeyCode.DOWN);
        map.step();
        assertEquals(player.getPosition(), new Position(2,2));
    }

    /**
     * Tests to see that the player does not moves through walls
     */
    @Test
    public void playerMovesWall() {
        IGrid<Character> grid = new MyGrid<>(4, 4, '*');
        grid.set(2, 2, 'p');
        map = new BDMap(grid);

        // find the player
        Position playerPos = new Position(2,2);
        IBDObject player = map.get(playerPos);
        assertTrue(player instanceof BDPlayer);

        map.getPlayer().keyPressed(KeyCode.RIGHT);
        map.step();
        assertEquals(player.getPosition(), playerPos);

        map.getPlayer().keyPressed(KeyCode.UP);
        map.step();
        assertEquals(player.getPosition(), playerPos);
        
        map.getPlayer().keyPressed(KeyCode.LEFT);
        map.step();
        assertEquals(player.getPosition(), playerPos);
        
        map.getPlayer().keyPressed(KeyCode.DOWN);
        map.step();
        assertEquals(player.getPosition(), playerPos);
    }

    /**
     * Tests if the player is able to remove sand
     */
    @Test
    public void playerRemovesSand() {
        IGrid<Character> grid = new MyGrid<>(2, 2, ' ');
        grid.set(0, 0, 'p');
        grid.set(1,0, '#');
        map = new BDMap(grid);

        IBDObject player = map.get(new Position(0,0));
        assertTrue(player instanceof BDPlayer);

        IBDObject sand = map.get(new Position(1,0));
        assertTrue(sand instanceof BDSand);

        map.getPlayer().keyPressed(KeyCode.RIGHT);
        map.step();

        map.getPlayer().keyPressed(KeyCode.LEFT);
        map.step();

        assertTrue(map.get(new Position(1,0)) instanceof BDEmpty);
    }

    /**
     * Tests if the player is able to pick up diamonds
     */
    @Test
    public void playerGetsDiamond() {
        IGrid<Character> grid = new MyGrid<>(100, 1, 'd');
        grid.set(0, 0, 'p');
        map = new BDMap(grid);

        IBDObject player = map.get(new Position(0,0));
        assertTrue(player instanceof BDPlayer);

        for (int i = 0; i < 100; i++) {
            int lastPickUp = map.getPlayer().numberOfDiamonds();
            map.getPlayer().keyPressed(KeyCode.RIGHT);
            map.step();
            
            // "i != 99" because then all the diamonds have been picked up
            if (!(map.getPlayer().numberOfDiamonds() > lastPickUp) && i != 99) {
                fail("The player did not pickup the diamonds");
            }
        }
    }

    /**
     * Tests to see if the player is able to break the box and obtain a key
     */
    @Test
    public void playerGetsKey() {
        IGrid<Character> grid = new MyGrid<>(2, 1, ' ');
        grid.set(0, 0, 'p');
        grid.set(1, 0, 'x');
        map = new BDMap(grid);

        IBDObject player = map.get(new Position(0,0));
        assertTrue(player instanceof BDPlayer);

        IBDObject box = map.get(new Position(1,0));
        assertTrue(box instanceof BDBox);
        
        for (int i = 0; i < 4; i++) {
            map.getPlayer().keyPressed(KeyCode.RIGHT);
            map.step();
        }
        assertEquals(1, map.getPlayer().numberOfKeys());
    }

    /**
     * Tests to see if the player is able to open a gate when key is acquired
     */
    @Test
    public void playerOpensGate() {
        IGrid<Character> grid = new MyGrid<>(3, 1, ' ');
        grid.set(0, 0, 'p');
        grid.set(1, 0, 'g');
        map = new BDMap(grid);

        IBDObject player = map.get(new Position(0,0));
        assertTrue(player instanceof BDPlayer);

        IBDObject gate = map.get(new Position(1,0));
        assertTrue(gate instanceof BDGate);
        
        map.getPlayer().setKeys(1);

        for (int i = 0; i < 2; i++) {
            map.getPlayer().keyPressed(KeyCode.RIGHT);
            map.step();
        }
        assertTrue(map.get(new Position(2,0)) instanceof BDPlayer);
    }
}
