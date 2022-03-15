package unsw.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import unsw.observable.*;

class TestPlayerInputComponent {

	IObservable<MoveEvent> keyboardEvents = new Observable<>();

	Entity testEntity = new Entity("test_player_input");

	PlayerInputComponent playerInput
		= new PlayerInputComponent(testEntity, keyboardEvents);

	TransformComponent transform
		= new TransformComponent(testEntity, 0, 0);

	@Test
	void testMovesEntityOnEvent() {
		testEntity.<PlayerInputComponent>addComponent(playerInput);
		testEntity.<TransformComponent>addComponent(transform);

	 //   0 1 2 3 4 5 
	 // 0 x
	 // 1
	 // 2
	 // 3
	 // 4
	 // 5

		keyboardEvents.notifyObservers(MoveEvent.right());
		assertTrue(transform.isAt(1, 0));

	 //   0 1 2 3 4 5 
	 // 0   x
	 // 1
	 // 2
	 // 3
	 // 4
	 // 5

		keyboardEvents.notifyObservers(MoveEvent.left());
		assertTrue(transform.isAt(0, 0));

	 //   0 1 2 3 4 5 
	 // 0 x 
	 // 1
	 // 2
	 // 3
	 // 4
	 // 5

		keyboardEvents.notifyObservers(MoveEvent.down());
		assertTrue(transform.isAt(0, 1));

	 //   0 1 2 3 4 5 
	 // 0   
	 // 1 x
	 // 2
	 // 3
	 // 4
	 // 5

		keyboardEvents.notifyObservers(MoveEvent.up());
		assertTrue(transform.isAt(0, 0));

	 //   0 1 2 3 4 5 
	 // 0 x 
	 // 1
	 // 2
	 // 3
	 // 4
	 // 5

		keyboardEvents.notifyObservers(MoveEvent.right());
		keyboardEvents.notifyObservers(MoveEvent.right());
		keyboardEvents.notifyObservers(MoveEvent.right());
		assertTrue(transform.isAt(3, 0));

	 //   0 1 2 3 4 5 
	 // 0       x
	 // 1
	 // 2
	 // 3
	 // 4
	 // 5

		keyboardEvents.notifyObservers(MoveEvent.down());
		assertTrue(transform.isAt(3, 1));

	 //   0 1 2 3 4 5 
	 // 0 
	 // 1       x
	 // 2
	 // 3
	 // 4
	 // 5

	}

}
