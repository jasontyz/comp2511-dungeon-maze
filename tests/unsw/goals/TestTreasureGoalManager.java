package unsw.goals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import unsw.observable.Observable;
import unsw.observable.IObservable;
import unsw.components.*;
import unsw.collectables.*;
import unsw.entity.EntityFactory;

class TestTreasureGoalManager {

	IObservable<MoveEvent> keyboardEvents =
		new Observable<>();

	Entity player =
		EntityFactory.createPlayer(0, 0, keyboardEvents);

	TreasureGoalManager manager =
		new TreasureGoalManager();

	private CollisionStrategy pickupTreasure (Entity treasure) {
		return (Entity enttiy, Delta<Point2D> delta) -> {
			CollectorComponent collector =
				this.player.<CollectorComponent>getComponent(CollectorComponent.class);
			CollectableComponent<Treasure> collectable =
				treasure.<CollectableComponent<Treasure>>getComponent(CollectableComponent.class);
			collector.pickupTreasure(collectable);
		};
	}

	@Test
	void testSingleTreasure () {
		manager.setPlayer(player);
		Entity treasure = EntityFactory.createTreasure(1, 1);

		treasure.addCollisionStrategy(player, this.pickupTreasure(treasure));

		manager.addTreasure(treasure);
		assertEquals(false, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(true, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(true, manager.isGoalCompleted());
	}

	@Test
	void testTwoTreaures () {
		manager.setPlayer(player);
		Entity treasure1 = EntityFactory.createTreasure(1, 1);
		Entity treasure2 = EntityFactory.createTreasure(2, 2);

		treasure1.addCollisionStrategy(player, this.pickupTreasure(treasure1));
		treasure2.addCollisionStrategy(player, this.pickupTreasure(treasure2));

		manager.addTreasure(treasure1);
		manager.addTreasure(treasure2);
		assertEquals(false, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(false, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(true, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.up());
		assertEquals(true, manager.isGoalCompleted());
	}
}
