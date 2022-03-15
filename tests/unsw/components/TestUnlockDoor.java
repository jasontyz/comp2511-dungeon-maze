package unsw.components;

import org.junit.jupiter.api.Test;

import unsw.collectables.DoorKey;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUnlockDoor {

	LockComponent door1 = new LockComponent(new Entity(), 1);
	LockComponent door2 = new LockComponent(new Entity(), 2);
	
	DoorKey key1 = new DoorKey(1);
	DoorKey key2 = new DoorKey(2);
	
	@Test
	void testRightKey() {
		assertEquals(true, key1.unlock(door1));
		assertEquals(true, door1.isUnlocked());
	}
	
	@Test
	void testWrongKey() {
		assertEquals(false, key1.unlock(door2));
		assertEquals(false, door2.isUnlocked());
	}
}
