package unsw.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import unsw.collectables.*;

class TestCollectorComponent {

	Entity testEntity = new Entity("test_collector_component");
	Entity enemyEntity = new Entity("test_collector_component");

	CollectorComponent collector
		= new CollectorComponent(testEntity);

	CollectableComponent<Sword> xsSword =
		new CollectableComponent<>(testEntity, new Sword(10, 5));

	CollectableComponent<Sword> mdSword =
		new CollectableComponent<>(testEntity, new Sword(20, 5));

	CollectableComponent<IKey> doorKey =
		new CollectableComponent<>(testEntity, new DoorKey(3423432));

	CollectableComponent<IWeapon> invincPotion
		= new CollectableComponent<>(testEntity, new InvincPotion(30 * 1000));

	HealthComponent health = new HealthComponent(enemyEntity, 50);

	LockComponent doorLock = new LockComponent(testEntity, 3423432);

	@Test
	void testPickupSword() {
		testEntity.<CollectableComponent<Sword>>addComponent(xsSword);
		assertTrue(collector.pickupSword(testEntity));
		assertFalse(collector.pickupSword(testEntity));
	}

	@Test
	void testAttackWithWeapon() {
		testEntity.<CollectableComponent<Sword>>addComponent(mdSword);
		enemyEntity.<HealthComponent>addComponent(health);

		assertTrue(collector.pickupSword(testEntity));
		collector.attack(enemyEntity);
		collector.attack(enemyEntity);
		collector.attack(enemyEntity);
		assertEquals(0, health.getHits());
	}

	@Test
	void testAttackWithPotion() {
		testEntity.<CollectableComponent<IWeapon>>addComponent(invincPotion);
		enemyEntity.<HealthComponent>addComponent(health);
		assertTrue(collector.pickupWeapon(testEntity));

		invincPotion.get().activate();
		collector.attack(enemyEntity);
		assertEquals(0, health.getHits());
	}

	@Test
	void testAttackWithUnactivatedPotion() {
		testEntity.<CollectableComponent<IWeapon>>addComponent(invincPotion);
		enemyEntity.<HealthComponent>addComponent(health);
		assertTrue(collector.pickupWeapon(testEntity));
		collector.attack(enemyEntity);
		assertEquals(50, health.getHits());
	}

	@Test
	void testPickupWeapon() {
		testEntity.<CollectableComponent<IWeapon>>addComponent(invincPotion);
		assertTrue(collector.pickupWeapon(testEntity));
	}

	@Test
	void testUnlock() {
		testEntity.<LockComponent>addComponent(doorLock);
		testEntity.<CollectableComponent<IKey>>addComponent(doorKey);
		assertFalse(collector.unlock(testEntity));
		assertTrue(collector.pickupKey(testEntity));
		assertTrue(collector.unlock(testEntity));
	}

	@Test
	void testKeyIsRemoved () {
		testEntity.<LockComponent>addComponent(doorLock);
		testEntity.<CollectableComponent<IKey>>addComponent(doorKey);

		collector.pickupKey(testEntity);
		assertTrue(collector.unlock(testEntity));
		assertFalse(collector.unlock(testEntity));
	}

	@Test
	void testSwordIsRemoved () {
		HealthComponent health = new HealthComponent(enemyEntity, 100);
		enemyEntity.<HealthComponent>addComponent(health);
		testEntity.<CollectableComponent<Sword>>addComponent(xsSword);

		collector.pickupSword(testEntity);

		// reduces health by 10
		collector.attack(enemyEntity); // 90
		collector.attack(enemyEntity); // 80
		collector.attack(enemyEntity); // 70
		collector.attack(enemyEntity); // 60
		collector.attack(enemyEntity); // 50

		// does not reduce health
		collector.attack(enemyEntity);
		collector.attack(enemyEntity);
		collector.attack(enemyEntity);

		assertEquals(50, health.getHits());
	}

	@Test
	void testInvincPotionIsRemoved () {
		HealthComponent health = new HealthComponent(enemyEntity, 100);
		enemyEntity.<HealthComponent>addComponent(health);
		testEntity.<CollectableComponent<IWeapon>>addComponent(invincPotion);
		
		collector.pickupWeapon(testEntity);
		invincPotion.get().notifyObservers(0);

		collector.attack(enemyEntity);
		assertEquals(100, health.getHits());
	}
}
