package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;

/**
 * A primitive implementation for adding extra sound effects to the different objects
 * Number index used because of the small set of files needed for this particular game.
 */
public class BDSounds {
    
    private static ArrayList<AudioClip> soundList;
    
    public BDSounds() {
        BDMap.setSound(true);
        soundList = new ArrayList<>();
        try {
            AudioClip diamondPickup = new AudioClip(getClass().getResource("../sfx/diamond.wav").toString());
            AudioClip playerDeath = new AudioClip(getClass().getResource("../sfx/death.mp3").toString());
            AudioClip rockPush = new AudioClip(getClass().getResource("../sfx/push.wav").toString());
            AudioClip bugDeath = new AudioClip(getClass().getResource("../sfx/bugdeath.mp3").toString());
            AudioClip rockMiss = new AudioClip(getClass().getResource("../sfx/miss.mp3").toString());
            AudioClip rockThump = new AudioClip(getClass().getResource("../sfx/thump.wav").toString());
            AudioClip move = new AudioClip(getClass().getResource("../sfx/move.wav").toString());
            
            //Adjust volume
            rockPush.setVolume(0.65);
            move.setVolume(0.65);
            rockMiss.setVolume(0.7);
            rockThump.setVolume(1.4);
            
            soundList.add(diamondPickup);
            soundList.add(playerDeath);
            soundList.add(rockPush);
            soundList.add(bugDeath);
            soundList.add(rockMiss);
            soundList.add(rockThump);
            soundList.add(move);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("A soundfile is missing!");
        }
    }

    /**
     * @param fileNumber index of the sound-file
     * @returns An audioclip from the arraylist
     */
    static AudioClip getSound(int fileNumber) {
        if (fileNumber > soundList.size()-1) {
            fileNumber = soundList.size()-1;
        } else if (fileNumber < 0) {
            fileNumber = 0;
        }
        return soundList.get(fileNumber);
    }
    
    
}
