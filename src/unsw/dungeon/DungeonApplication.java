package unsw.dungeon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DungeonApplication extends Application {	
	
	@Override
	public void start(Stage primaryStage) throws Exception {		
		
		DungeonController dungeonController = null;
		
		primaryStage.setTitle("Game of Dungeon");
		
		DungeonSceneController sceneController = new DungeonSceneController(primaryStage, dungeonController);
		
		sceneController.showStartMenuScene();					
		
//		DungeonLoader dungeonLoader = new DungeonLoader("advanced.json");
//		DungeonController controller =  new DungeonController(dungeonLoader);		
//		
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
//		loader.setController(controller);		
//		
//		Parent root = loader.load();
//		Scene scene = new Scene(root);
//		root.requestFocus();
//		primaryStage.setScene(scene);
						
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	private String[] getLevelsString(String path) {
		return new File(path).list();
	}

}
