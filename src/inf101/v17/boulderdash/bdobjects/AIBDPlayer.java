package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.input.KeyCode;

import java.util.Random;

/**
 *  A really simple AI-version of player that moves randomly from generated numbers
 *  Change 'player' type to new AIBDPlayer in BDMap-constructor to use this class
 */
public class AIBDPlayer extends BDPlayer {
    
    public AIBDPlayer(BDMap owner) {
        super(owner);
    }
    
    @Override
    public void keyPressed(KeyCode key) {
    }
    
    @Override
    public void step() {
        Random numberGenerator = new Random();
        int randNumber = numberGenerator.nextInt(4);
        switch (randNumber) {
            case 0: askedToGo = Direction.WEST;
            break;
            case 1: askedToGo = Direction.NORTH;
            break;
            case 2: askedToGo = Direction.EAST;
            break;
            case 3: askedToGo = Direction.SOUTH;
        }
        super.step();
    }
}
