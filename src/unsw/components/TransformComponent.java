package unsw.components;

import java.util.Set;
import java.util.HashSet;

import unsw.observable.*;

public class TransformComponent
	extends ObservableComponent<Delta<Point2D>> {

	Point2D position;

	public TransformComponent (Entity entity, int x, int y) {
		super(entity);
		this.position = new Point2D(x, y);
	}

	public void translate (int x, int y) {
		Point2D from = position;
		Point2D to = new Point2D(x, y);
		this.position = to;
		if (!from.equals(to))
			this.notifyObservers(new Delta<Point2D>(from, to));
	}

	public void translate (Point2D point) {
		this.translate(point.x, point.y);
	}
	
	public void translateX (int dx) {
		this.translate(this.position.x + dx, this.position.y);
	}

	public void translateY (int dy) {
		this.translate(this.position.x, this.position.y + dy);
	}

	// -------------------------------------------------------------

	boolean isAt (int x, int y) {
		return this.position.equals(new Point2D(x, y));
	}

	boolean isAt (Point2D point) {
		return this.position.equals(point);
	}

	@Override
	public boolean equals (Object other) {
		if (other == null) return false;
		TransformComponent otherTransform = (TransformComponent) other;
		return this.position.equals(otherTransform.position);
	}
	
	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}
}
