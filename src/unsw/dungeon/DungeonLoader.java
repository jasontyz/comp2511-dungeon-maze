package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import unsw.observable.*;
import unsw.collectables.InvincPotion;
import unsw.components.CollectableComponent;
import unsw.components.CollectorComponent;
import unsw.components.Delta;
import unsw.components.Entity;
import unsw.components.FollowComponent;
import unsw.components.MoveEvent;
import unsw.components.Point2D;
import unsw.components.TransformComponent;
import unsw.entity.EntityType;

public class DungeonLoader {

    private JSONObject json;
	private IObservable<MoveEvent> moveEvents = new Observable<>();
	
	public DungeonLoader() {}

    public DungeonLoader(String filename) throws FileNotFoundException  {
		this.setUpLoader(filename);				
		
    }

   public JSONObject getJSON() {
	   return this.json;
   }
    
   public void setUpLoader(String filename) throws FileNotFoundException {
	   String path = "dungeons/" + filename;
		JSONTokener tokener = new JSONTokener(new FileReader(path));
		this.json = new JSONObject(tokener);	
   }
   
   
    private void trackPosition(Entity entity, Node node) {
    
    	TransformComponent transform = entity.<TransformComponent>getComponent(TransformComponent.class);

    	GridPane.setColumnIndex(node, transform.getX());
        GridPane.setRowIndex(node,transform.getY());
        
        transform.addObserver( e -> {
        	GridPane.setColumnIndex(node, transform.getX());
        	GridPane.setRowIndex(node, transform.getY());
        });    
        
        if (entity.type == EntityType.INVINCIBILITY) {
        	CollectableComponent<InvincPotion> invincPotion =
        			entity.<CollectableComponent<InvincPotion>>getComponent(CollectableComponent.class);
        	invincPotion.get().addObserver( i -> {
        		if (i > 0) {        			
        			entity.removeComponent(TransformComponent.class);
        			node.setVisible(false);
        		}
        	});        	
        }
    }
    
//	public DungeonController loadController () {
//		return new DungeonController(this.load(), this.moveEvents, this.initialEntities);
//	}
}
