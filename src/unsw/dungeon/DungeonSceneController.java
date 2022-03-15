package unsw.dungeon;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DungeonSceneController {
	
	DungeonController dungeonController;
	Stage mainWindow;
	
	public DungeonSceneController (Stage mainWindow, DungeonController dungeonController) {
		this.mainWindow = mainWindow;
		this.dungeonController = dungeonController;
	}
	
	public void showStartMenuScene() {
		String[] levels = getLevelsString("dungeons/");
		
		VBox startMenu = new VBox();
		startMenu.setPadding(new Insets(30, 30, 30, 30));		
		startMenu.setSpacing(15);
		startMenu.setAlignment(Pos.CENTER);
		Label lb = new Label("Choose a level:");
		startMenu.getChildren().add(lb);
		
		for (String level : levels) {
			Button levelBtn = new Button(level.replace(".json", ""));
			levelBtn.setPrefWidth(400);			
			levelBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {				

				DungeonLoader dungeonLoader = null;
				try {
					dungeonLoader = new DungeonLoader(level);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
				dungeonController =  new DungeonController(dungeonLoader, this);		
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
				loader.setController(dungeonController);	
				
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Scene scene = new Scene(root);
				root.requestFocus();
				mainWindow.setScene(scene);
				//System.out.println(this.levelPicked);
			});			
			
			startMenu.getChildren().add(levelBtn);
		}			
		
		Scene scene = new Scene(startMenu);		
		mainWindow.setScene(scene);		
		
		mainWindow.show();	
	}

	public void showDialog(String message, DungeonStatus status, GridPane squares) {
		
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);		
		dialog.initOwner((Stage)squares.getScene().getWindow());
		dialog.initStyle(StageStyle.TRANSPARENT);
		dialog.setOpacity(0.85);
		dialog.setWidth(400);
		dialog.setHeight(200);
		
		HBox btnBox = new HBox(20);
		btnBox.setPadding(new Insets(30, 30, 30, 30));
		btnBox.setAlignment(Pos.CENTER);
		btnBox.setSpacing(20);
		
		VBox dialogVbox = new VBox(40);
		dialogVbox.setAlignment(Pos.CENTER);
		dialogVbox.setPadding(new Insets(30, 30, 30, 30));
		dialogVbox.setSpacing(20);
		
		Label text = new Label(message);
		text.setAlignment(Pos.CENTER);
		
		if (status == DungeonStatus.PAUSED) {
			Button resumeBtn = new Button("Resume");
			resumeBtn.setPadding(new Insets(5, 5, 5, 5));			
			resumeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {	 
				dungeonController.startGameLoop();
				dialog.close();
			});		
			btnBox.getChildren().add(resumeBtn);
		}
		
		Button restartBtn = new Button("Restart");
		restartBtn.setPadding(new Insets(5, 5, 5, 5));
		restartBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {	 
			squares.getChildren().clear();			
			dungeonController.initialize();
			dialog.close();
		});					
		
		Button levelBtn = new Button("Choose new levels");
		levelBtn.setPadding(new Insets(5, 5, 5, 5));
		levelBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {	 
			squares.getChildren().clear();						
			dialog.close();
			showStartMenuScene();
		});	
				
		btnBox.getChildren().addAll(restartBtn, levelBtn);
		
		dialogVbox.getChildren().addAll(text, btnBox);								
		
		Scene dialogScene = new Scene(dialogVbox, 150, 100);
		dialog.setScene(dialogScene);
		dialog.show();
	}
	
	private String[] getLevelsString(String path) {
		return new File(path).list();
	}
}
