package unsw.components;

public class ZoneComponent extends Component {

	private boolean isTriggered;

	public interface ZoneHandler {
		void call();
	}

	public ZoneComponent (Entity entity) {
		super(entity);
	}

	public void watch (Entity subject) {
		TransformComponent transform =
			subject.<TransformComponent>getComponent(TransformComponent.class);

		TransformComponent thisTransform =
			this.entity.<TransformComponent>getComponent(TransformComponent.class);

		transform.addObserver((Delta<Point2D> delta) -> {
			if (thisTransform.isAt(delta.to))
				this.isTriggered = true;
			else if (thisTransform.isAt(delta.from))
				this.isTriggered = false;
		});
	}

	public void onEnter (Entity subject, ZoneHandler handler) {
		TransformComponent transform =
			subject.<TransformComponent>getComponent(TransformComponent.class);

		TransformComponent thisTransform =
			this.entity.<TransformComponent>getComponent(TransformComponent.class);

		transform.addObserver((Delta<Point2D> delta) -> {
			if (thisTransform.isAt(delta.to)) {
				this.isTriggered = true;
				handler.call();
			}
		});
	}

	public void onLeave (Entity subject, ZoneHandler handler) {
		TransformComponent transform =
			subject.<TransformComponent>getComponent(TransformComponent.class);

		TransformComponent thisTransform =
			this.entity.<TransformComponent>getComponent(TransformComponent.class);

		transform.addObserver((Delta<Point2D> delta) -> {
			if (thisTransform.isAt(delta.from)) {
				this.isTriggered = false;
				handler.call();
			}
		});
	}
}
