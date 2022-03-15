package unsw.javafx;

import javafx.scene.layout.GridPane;
import unsw.ui.IGridLayer;
import unsw.ui.IGridLayerFactory;

public class JavaFXLayerFactory implements IGridLayerFactory {
	
	private GridPane squares;
	
	public JavaFXLayerFactory (GridPane squares) {
		this.squares = squares;
	}
	
	public IGridLayer create() {
		return new JavaFXLayer(this.squares);
	}
}
