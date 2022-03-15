package unsw.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestEntity {

	Entity entity = new Entity("test");
	
	@Test
	void testAddComponent () {
		TransformComponent transform
			= new TransformComponent(entity, 0, 0);

		entity.<TransformComponent>addComponent(transform);

		assertEquals(transform,
					entity.<TransformComponent>getComponent(TransformComponent.class));
					
		entity.<TransformComponent>addComponent(transform);
	}

	@Test
	void testGetComponent () {
		TransformComponent transform
			= new TransformComponent(entity, 0, 0);

		assertEquals(null,
					entity.<TransformComponent>getComponent(TransformComponent.class));

		entity.<TransformComponent>addComponent(transform);

		assertEquals(transform,
					entity.<TransformComponent>getComponent(TransformComponent.class));
					
	}

	@Test
	void testRemoveComponent () {
		TransformComponent transform
			= new TransformComponent(entity, 0, 0);

		entity.<TransformComponent>addComponent(transform);
		entity.<TransformComponent>removeComponent(TransformComponent.class);

		assertEquals(null,
					entity.<TransformComponent>getComponent(TransformComponent.class));
					
	}

	@Test
	void testAddCollisionStrategy () {
		Entity player = new Entity();
		TransformComponent playerTransform
			= new TransformComponent(entity, 0, 0);
		player.<TransformComponent>addComponent(playerTransform);

		Entity enemy = new Entity();
		TransformComponent enemyTransform
			= new TransformComponent(entity, 20, 20);
		enemy.<TransformComponent>addComponent(enemyTransform);

		player.addCollisionStrategy(enemy, (Entity entity, Delta<Point2D> delta) -> {
			assertEquals(new Point2D(40, 40), delta.from);
			assertEquals(new Point2D(0, 0), delta.to);
		});

		enemyTransform.translate(40, 40); // shouldn't call collision strategy
		enemyTransform.translate(0, 0);   // should call strategy
	}
}
