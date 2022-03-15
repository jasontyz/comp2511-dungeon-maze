package unsw.components;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class FollowComponent extends Component {

	private Entity target;
	private List<Entity> entities;
	
	public FollowComponent (Entity entity) {
		super(entity);
		this.target = null;
	}

	public void follow (Entity target, List<Entity> entities) {
		this.entities = entities;
		this.target = target;
	}
	
	public void nextMove () {
		if (target == null) return;
		
		TransformComponent transform =
			this.entity.<TransformComponent>getComponent(TransformComponent.class);
		if (transform == null) return;

		List<Point2D> nextMoves = this.proposedNextMoves();
		
		for (Point2D nextMove : nextMoves) {
			if (this.canMoveTo(nextMove)) {
				transform.translate(nextMove);
				return;
			}
		}
	}

	private List<Point2D> proposedNextMoves () {
		List<Point2D> points = new ArrayList<>();
		
		TransformComponent transform =
			this.entity.<TransformComponent>getComponent(TransformComponent.class);

		TransformComponent targetTransform =
			this.target.<TransformComponent>getComponent(TransformComponent.class);
		
		if (transform == null || targetTransform == null) return points;

		int targetX = targetTransform.getX();
		int targetY = targetTransform.getY();
		int thisX = transform.getX();
		int thisY = transform.getY();

		int dx = Math.abs(targetX - thisX);
		int dy = Math.abs(targetY - thisY);
		
		CollectorComponent collector =
			this.target.<CollectorComponent>getComponent(CollectorComponent.class);
		
		int delta = (collector != null && collector.hasPotion()) ? -1 : 1;

		if (dx > dy) {
			if (thisX > targetX)
				points.add(new Point2D(thisX - delta, thisY));
			else if (thisX < targetX)
				points.add(new Point2D(thisX + delta, thisY));
			
			if (thisY > targetY)
				points.add(new Point2D(thisX, thisY - delta));
			else if (thisY < targetY)
				points.add(new Point2D(thisX, thisY + delta));
		}
		
		else {
			if (thisY > targetY)
				points.add(new Point2D(thisX, thisY - delta));
			else if (thisY < targetY)
				points.add(new Point2D(thisX, thisY + delta));
			
			if (thisX > targetX)
				points.add(new Point2D(thisX - delta, thisY));
			else if (thisX < targetX)
				points.add(new Point2D(thisX + delta, thisY));
		}
		
		return points;
	}

	private boolean canMoveTo (Point2D nextPosition) {
		for (Entity entity : this.entities) {
			if (entity == this.target) continue;
			
			ZoneComponent zone =
				entity.<ZoneComponent>getComponent(ZoneComponent.class);
			if (zone != null) continue;
			
			CollectableComponent<?> collectable =
				entity.<CollectableComponent<?>>getComponent(CollectableComponent.class);
			if (collectable != null) continue;
			
			LockComponent lock =
				entity.<LockComponent>getComponent(LockComponent.class);
			if (lock != null) continue;
			
			TransformComponent transform =
				entity.<TransformComponent>getComponent(TransformComponent.class);
			if (transform != null && transform.isAt(nextPosition)) return false;
		}
		
		return true;
	}
}
