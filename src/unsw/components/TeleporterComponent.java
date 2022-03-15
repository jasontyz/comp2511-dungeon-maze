package unsw.components;

public class TeleporterComponent extends Component {
	
	private TeleporterComponent dual;

	public TeleporterComponent (Entity entity) {
		super(entity);
	}
		
	public void request (TeleporterComponent teleporter) {
		this.dual = teleporter;
		teleporter.ack(this);
	}

	public void ack (TeleporterComponent teleporter) {
		this.dual = teleporter;
	}
	
	public Point2D teleportTo () {
		TransformComponent transform
			= entity.<TransformComponent>getComponent(TransformComponent.class);
		return transform.position;
	}
	
	public void teleport (Entity entity, Delta<Point2D> delta) {
		TransformComponent transform
			= entity.<TransformComponent>getComponent(TransformComponent.class);
		Point2D destPoint = dual.teleportTo();
		
		if (delta.from.x < delta.to.x) {
			transform.translate(destPoint.x + 1, destPoint.y);
		}
		else if (delta.from.x > delta.to.x) {
			transform.translate(destPoint.x - 1, destPoint.y);
		}
		else if (delta.from.y < delta.to.y) {
			transform.translate(destPoint.x, destPoint.y + 1);
		}
		else if (delta.from.y > delta.to.y) {
			transform.translate(destPoint.x, destPoint.y - 1);
		}
	}
}
