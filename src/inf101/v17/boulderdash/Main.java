package inf101.v17.boulderdash;

import inf101.v17.boulderdash.bdobjects.BDSounds;
import inf101.v17.boulderdash.gui.BoulderDashGUI;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.boulderdash.maps.MapReader;
import inf101.v17.datastructures.IGrid;

/**
 * Contains the main method to execute the program.
 *
 */
public class Main {
	
	public static void main(String[] args) {
		// This is how you set up the program, change the file path accordingly.
		MapReader reader = new MapReader("testMap.txt");
		IGrid<Character> rawGrid = reader.read();
		
		BDMap map = new BDMap(rawGrid);
		
		//Loads sound-fx and turns sound on for map
		BDSounds soundOn = new BDSounds();

		BoulderDashGUI.run(map);
	}

}
