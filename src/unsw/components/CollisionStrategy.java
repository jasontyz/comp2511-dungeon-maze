package unsw.components;

public interface CollisionStrategy {
	public void exec (Entity entity, Delta<Point2D> delta);
}
