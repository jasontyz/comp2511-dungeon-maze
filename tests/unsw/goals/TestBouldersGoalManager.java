package unsw.goals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import unsw.observable.Observable;
import unsw.observable.IObservable;
import unsw.components.TransformComponent;
import unsw.components.Entity;
import unsw.entity.EntityFactory;

class TestBouldersGoalManager {

	BouldersGoalManager manager =
		new BouldersGoalManager();

	@Test
	void testSingleTreasure () {
		Entity boulder = EntityFactory.createBoulder(1, 1);
		Entity floorSwitch = EntityFactory.createFloorSwitch(1, 2);

		manager.addBoulder(boulder);
		manager.addFloorSwitch(floorSwitch);
		assertEquals(false, manager.isGoalCompleted());
		
		TransformComponent transform =
			boulder.<TransformComponent>getComponent(TransformComponent.class);

		transform.translateY(1);
		assertEquals(true, manager.isGoalCompleted());

		transform.translateY(-1);
		assertEquals(false, manager.isGoalCompleted());
	}

	@Test
	void testTwoFloorSwitches () {
		Entity boulder1 = EntityFactory.createBoulder(1, 1);
		Entity floorSwitch1 = EntityFactory.createFloorSwitch(1, 2);

		Entity boulder2 = EntityFactory.createBoulder(3, 3);
		Entity floorSwitch2 = EntityFactory.createFloorSwitch(4, 3);

		manager.addBoulder(boulder1);
		manager.addBoulder(boulder2);
		manager.addFloorSwitch(floorSwitch1);
		manager.addFloorSwitch(floorSwitch2);
		assertEquals(false, manager.isGoalCompleted());
		
		TransformComponent transform1 =
			boulder1.<TransformComponent>getComponent(TransformComponent.class);

		TransformComponent transform2 =
			boulder2.<TransformComponent>getComponent(TransformComponent.class);

		transform1.translateY(1);
		assertEquals(false, manager.isGoalCompleted());

		transform2.translateX(1);
		assertEquals(true, manager.isGoalCompleted());

		transform1.translateY(-1);
		assertEquals(false, manager.isGoalCompleted());

		transform1.translateY(1);
		assertEquals(true, manager.isGoalCompleted());
	}

	@Test
	void testWhenExcessBoulders () {
		Entity boulder1 = EntityFactory.createBoulder(1, 1);
		Entity floorSwitch1 = EntityFactory.createFloorSwitch(1, 2);

		Entity boulder2 = EntityFactory.createBoulder(3, 3);

		manager.addBoulder(boulder1);
		manager.addBoulder(boulder2);
		manager.addFloorSwitch(floorSwitch1);
		assertEquals(false, manager.isGoalCompleted());
		
		TransformComponent transform1 =
			boulder1.<TransformComponent>getComponent(TransformComponent.class);

		TransformComponent transform2 =
			boulder2.<TransformComponent>getComponent(TransformComponent.class);

		transform1.translateY(1);
		assertEquals(true, manager.isGoalCompleted());
	}
}
