package unsw.components;

import java.util.List;

public class MoverComponent extends Component {

	private final int strength;

	public MoverComponent (Entity entity, int strength) {
		super(entity);
		this.strength = strength;
	}

	public boolean move (Entity otherEntity, Delta<Point2D> delta, List<Entity> entities) {
		Point2D proposed = this.proposedMove(otherEntity, delta);
		if (proposed == null || !this.canMoveEntityTo(proposed, entities)) return false;
		
		TransformComponent transform =
			otherEntity.<TransformComponent>getComponent(TransformComponent.class);
		transform.translate(proposed);
		return true;
	}

	private Point2D proposedMove (Entity otherEntity, Delta<Point2D> delta) {
		MoveableComponent moveable =
			otherEntity.<MoveableComponent>getComponent(MoveableComponent.class);

		if (moveable == null || moveable.weight > this.strength) return null;

		if (delta.from.x < delta.to.x)
			return new Point2D(delta.to.x + 1, delta.to.y);
		else if (delta.from.x > delta.to.x)
			return new Point2D(delta.to.x - 1, delta.to.y);
		else if (delta.from.y < delta.to.y)
			return new Point2D(delta.to.x, delta.to.y + 1);
		else if (delta.from.y > delta.to.y)
			return new Point2D(delta.to.x, delta.to.y - 1);
		
		return null;
	}

	private boolean canMoveEntityTo (Point2D nextPosition, List<Entity> entities) {
		for (Entity entity : entities) {
			ZoneComponent zone =
				entity.<ZoneComponent>getComponent(ZoneComponent.class);
			if (zone != null) continue;
			
			TransformComponent transform =
				entity.<TransformComponent>getComponent(TransformComponent.class);
			if (transform != null && transform.isAt(nextPosition)) return false;
		}
		return true;
	}
}
