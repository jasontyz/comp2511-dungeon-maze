package unsw.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestHealthComponent {

	private static final int STARTING_HEALTH = 90;

	HealthComponent healthComponent = new HealthComponent(new Entity(), STARTING_HEALTH);
	
	@Test
	void testHit() {
		healthComponent.hit(10);
		assertEquals(STARTING_HEALTH - 10, healthComponent.getHits());
	}
	
	@Test
	void testDie() {
		healthComponent.die();
		assertEquals(0, healthComponent.getHits());
	}
}
