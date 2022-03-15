package unsw.goals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import unsw.observable.Observable;
import unsw.observable.IObservable;
import unsw.components.Entity;
import unsw.components.MoveEvent;
import unsw.components.ZoneComponent;
import unsw.entity.EntityFactory;

class TestExitGoalManager {

	IObservable<MoveEvent> keyboardEvents =
		new Observable<>();

	Entity player =
		EntityFactory.createPlayer(0, 0, keyboardEvents);

	ExitGoalManager manager =
		new ExitGoalManager();

	@Test
	void testSingleExit () {
		manager.setPlayer(player);
		Entity exit = EntityFactory.createExit(1, 1);

		manager.addExit(exit);
		assertEquals(false, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(true, manager.isGoalCompleted());
	}

	@Test
	void testEnterAndExitMaze () {
		// this can only happen when there exists other
		// goals that still need to be completed
		manager.setPlayer(player);
		Entity exit = EntityFactory.createExit(1, 1);

		manager.addExit(exit);
		assertEquals(false, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(true, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		assertEquals(false, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.up());
		assertEquals(true, manager.isGoalCompleted());
	}

	@Test
	void testWhenMultipleExits () {
		manager.setPlayer(player);

		Entity exit1 = EntityFactory.createExit(1, 1);
		Entity exit2 = EntityFactory.createExit(2, 2);

		manager.addExit(exit1);
		manager.addExit(exit2);

		assertEquals(false, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(true, manager.isGoalCompleted());

		keyboardEvents.notifyObservers(MoveEvent.down());
		assertEquals(false, manager.isGoalCompleted());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertEquals(true, manager.isGoalCompleted());
	}
}
