package inf101.v17.boulderdash.gui;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.net.URL;
import java.util.MissingResourceException;

/**
 * The main class of the BoulderDash program. Sets up a simple gui of a given
 * map and handles the timing.
 *
 */
public class BoulderDashGUI extends Application implements EventHandler<KeyEvent> {
	/**
	 * Determines how many milliseconds pass between two steps of the program.
	 */
	private static final int SPEED = 120;

	private static BDMap theMap;

	private BDMap map;

	private BDMapComponent mapComponent;

	private Text message;

	private AnimationTimer timer;

	public static final double NOMINAL_WIDTH = 1900, NOMINAL_HEIGHT = 1000;

	private Stage stage;

	public BoulderDashGUI() {
		this.map = theMap;
	}

	/**
	 * Runs the program on a given map.
	 *
	 * @param map
	 */
	public static void run(BDMap map) {
		theMap = map;
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {

		//Finds and plays music
		try{URL music = getClass().getResource("../gui/sounds/Pixelland.mp3");
			Media song = new Media(music.toString());
			MediaPlayer player = new MediaPlayer(song);
			player.play();
			player.setVolume(0.5);
			System.out.println("Volume: " + player.getVolume());
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}

		this.stage = stage;
		double spacing = 10;
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		Group root = new Group();
		double width = Math.min(primaryScreenBounds.getWidth() - 40, map.getWidth()*200);
		double height = Math.min(primaryScreenBounds.getHeight() - 100, map.getWidth()*200);
		Scene scene = new Scene(root, width, height, Color.BLACK);
		stage.setScene(scene);

		message = new Text(10, 0, "");
		message.setFont(new Font(26));
		message.setFill(Color.ORANGERED);
		message.setText("Diamonds: " + map.getPlayer().numberOfDiamonds());

		mapComponent = new BDMapComponent(map);
		mapComponent.widthProperty().bind(scene.widthProperty());
		mapComponent.heightProperty().bind(scene.heightProperty());
		mapComponent.heightProperty().bind(Bindings.subtract(Bindings.subtract(scene.heightProperty(),
										   message.getLayoutBounds().getHeight()), spacing));
		// mapComponent.setScaleY(-1.0);

		timer = new AnimationTimer() {

			private long lastUpdateTime;

			@Override
			public void handle(long now) {
				//750_000 as opposed to the original 1_000_000, speeds up the game a little
				if (now - lastUpdateTime > SPEED * 750_000) {
					lastUpdateTime = now;
					step();
				}
			}

		};

		VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.getChildren().add(message);
		vbox.getChildren().add(mapComponent);
		root.getChildren().add(vbox);

		stage.addEventHandler(KeyEvent.KEY_PRESSED, this);
		timer.start();
		stage.show();
	}

	protected void step() {
		if (map.getPlayer().isAlive()) {
			map.step();
			message.setText("DIAMONDS: " + map.getPlayer().numberOfDiamonds());
		} else {
			message.setText("GAME OVER!");
		}
		mapComponent.draw();
	}

	@Override
	public void handle(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code == KeyCode.ESCAPE || code == KeyCode.Q) {
			System.exit(0);
		} else if (code == KeyCode.F) {
			stage.setFullScreen(!stage.isFullScreen());
		} else {
			map.getPlayer().keyPressed(code);
		}
	}
}
