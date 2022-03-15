package unsw.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import unsw.collectables.*;
import unsw.observable.*;
import unsw.entity.EntityFactory;

class TestZoneComponent {

	IObservable<MoveEvent> keyboardEvents =
		new Observable<>();

	int onEnterCalled = 0, onLeaveCalled = 0;

	void onEnter () {
		onEnterCalled++;
	}

	void onLeave () {
		onLeaveCalled++;
	}
		
	// position player in the top left corner
	Entity player = EntityFactory
		.createPlayer(0, 0, keyboardEvents);

	@Test
	void testOnEnter () {
		// position exit in the bottom right corner
		Entity exit = EntityFactory
			.createExit(5, 5);

		ZoneComponent zone =
			exit.<ZoneComponent>getComponent(ZoneComponent.class);

		zone.onEnter(player, this::onEnter);

		for (int i = 1; i <= 5; i++) {
			// move player down to bottom right corner
			keyboardEvents.notifyObservers(MoveEvent.right());
			keyboardEvents.notifyObservers(MoveEvent.down());
		}

		assertEquals(1, this.onEnterCalled);
	}

	@Test
	void testOnLeave () {
		// position player on the exit to begin
		Entity exit = EntityFactory
			.createExit(0, 0);

		ZoneComponent zone =
			exit.<ZoneComponent>getComponent(ZoneComponent.class);

		zone.onLeave(player, this::onLeave);

		keyboardEvents.notifyObservers(MoveEvent.right());
		keyboardEvents.notifyObservers(MoveEvent.down());

		assertEquals(1, onLeaveCalled);
	}
	
	@Test
	void testMovingOntoAndOffZone () {
		Entity exit = EntityFactory
			.createExit(1, 1);

		ZoneComponent zone =
			exit.<ZoneComponent>getComponent(ZoneComponent.class);

		zone.onEnter(player, this::onEnter);
		zone.onLeave(player, this::onLeave);

		// move player onto exit
		keyboardEvents.notifyObservers(MoveEvent.right());
		keyboardEvents.notifyObservers(MoveEvent.down());

		// move player off exit
		keyboardEvents.notifyObservers(MoveEvent.down());

		// move player on exit
		keyboardEvents.notifyObservers(MoveEvent.up());
		
		// move player off exit
		keyboardEvents.notifyObservers(MoveEvent.right());
		keyboardEvents.notifyObservers(MoveEvent.right());

		assertEquals(2, onEnterCalled);
		assertEquals(2, onLeaveCalled);
	}
}
