package unsw.javafx;

import unsw.ui.IGridLayer;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class JavaFXLayer implements IGridLayer {
	private ImageView node;
	private GridPane squares;
	private boolean isMounted = true;

	public JavaFXLayer (GridPane squares) {
		this.node = new ImageView();
		this.squares = squares;
		this.squares.getChildren().add(node);
	}

	public void setXPos (int x) {
		if (!isMounted) return;
		GridPane.setColumnIndex(node, x);
	}

	public void setYPos (int y) {
		if (!isMounted) return;
		GridPane.setRowIndex(node, y);
	}

	public void removeSelf () {
		if (!this.isMounted) return;
		
		squares.getChildren().remove(node);		
		this.isMounted = false;
	}

	public void setImage (String imgPath) {		
		if (!isMounted) return;
		node.setImage(new Image(imgPath));
	}
	
	public void toFront() {
		if (!isMounted) return;
		this.node.toFront();
	}
	
	public void toBack() {
		if (!isMounted) return;
		this.node.toBack();
	}
}
