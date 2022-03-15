package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import unsw.observable.*;
import unsw.components.Entity;
import unsw.components.MoveEvent;
import unsw.components.TransformComponent;
import unsw.entity.EntityType;
import unsw.javafx.JavaFXLayerFactory;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    private static final int TICK_DURATION_MS = 1000;
	private Dungeon dungeon;
    private IObservable<MoveEvent> moveEvents = new Observable<MoveEvent>();
    private DungeonLoader loader;
    
    private DungeonSceneController sceneController;
    
    private Timeline timeline;
    
    @FXML
    private GridPane squares;

    public DungeonController(
    	DungeonLoader loader,
    	DungeonSceneController sceneController) {
    	
    	this.loader = loader; 	    	
    	this.timeline = new Timeline();
    	this.sceneController = sceneController;
    }
    
    @FXML
    public void initialize () {    	    	    	    
    	
    	this.dungeon = Dungeon
    		.from(loader, this.moveEvents, new JavaFXLayerFactory(squares));   
    	  
		 Image ground = new Image("/dirt_0_new.png");
	
	     // Add the ground first so it is below all other entities
	     for (int x = 0; x < dungeon.getWidth(); x++) {
	         for (int y = 0; y < dungeon.getHeight(); y++) {
	        	 Node node = new ImageView(ground);
	             squares.add(node, x, y);
	             node.toBack();
	         }
	     }	     	     
	     
		this.dungeon.init();
		startGameLoop();
    }    
    
    @FXML
    public void handleKeyPress (KeyEvent event) {

		switch (event.getCode()) {
			case UP: 
				moveEvents.notifyObservers(MoveEvent.up());
				break;
			case RIGHT: 
				moveEvents.notifyObservers(MoveEvent.right()); 
				break;
			case DOWN: 
				moveEvents.notifyObservers(MoveEvent.down()); 
				break;
			case LEFT: 
				moveEvents.notifyObservers(MoveEvent.left());
				break;
			case SPACE:
				this.dungeon.pause();
				stopGameLoop();		
				sceneController
					.showDialog("Game Paused", DungeonStatus.PAUSED, this.squares);
			default:
				break;
		}
		
		this.handleNextFrame();	
	}
    
    public void startGameLoop () {
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	timeline.setAutoReverse(false);
    	
    	KeyFrame keyFrame = 
			new KeyFrame(Duration.millis(TICK_DURATION_MS), e -> { 
				dungeon.update(TICK_DURATION_MS);
				this.handleNextFrame();
			});
    
    	timeline.getKeyFrames().add(keyFrame);
    	timeline.play();
    }
    
    private void handleNextFrame () {
 		dungeon.render();
 		
 		System.out.printf("dungeon is: %s\n", dungeon.getStatus());
 		
 		switch (dungeon.getStatus()) {
 			case SUCCEEDED:
 	    		this.stopGameLoop();
 	    		onGameEnd("You have completed the maze!", DungeonStatus.SUCCEEDED);
 	    		break;
 			case FAILED:
 				this.stopGameLoop();
 				onGameEnd("You DEAD", DungeonStatus.FAILED);
 				break;
 			default:
 		}	
    }
    
    public void stopGameLoop () {
    	timeline.getKeyFrames().clear();
    	timeline.stop();
    }
	
    private void onGameEnd(String message, DungeonStatus status) {    	
    	sceneController.showDialog(message, status, this.squares);
    }
}

