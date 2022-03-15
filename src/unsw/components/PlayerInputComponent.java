package unsw.components;

import unsw.observable.*;

public class PlayerInputComponent extends Component {

	private IObservable<MoveEvent> moveEvents;

	public PlayerInputComponent (
		Entity entity,
		IObservable<MoveEvent> moveEvents) {

		super(entity);
		this.moveEvents = moveEvents;
		moveEvents.addObserver(this::handleMoveEvent);
	}

	private void handleMoveEvent (MoveEvent event) {
		TransformComponent transform =
			this.entity.<TransformComponent>getComponent(TransformComponent.class);
		if (transform == null) return;
		switch (event.direction) {
			case UP: transform.translateY(-1); break;
			case RIGHT: transform.translateX(1); break;
			case DOWN: transform.translateY(1); break;
			case LEFT: transform.translateX(-1); break;
		}
	}
}
